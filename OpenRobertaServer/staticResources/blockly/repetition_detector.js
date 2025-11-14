/**
 * @fileoverview Detect repeated subsequences of connected blocks in a workspace
 * and mark them with a warning so they can be highlighted and post-processed.
 */
'use strict';

goog.provide('Blockly.RepetitionDetector');

// Ensure Blockly namespace exists when goog.provide is a noop (demo environment).
if (typeof Blockly === 'undefined') {
  window.Blockly = {};
}
Blockly.RepetitionDetector = Blockly.RepetitionDetector || {};

/**
 * Configuration defaults.
 * @const {!Object}
 */
Blockly.RepetitionDetector.DEFAULTS = {
  maxSubseqLen: 3,    // max subsequence length to consider
  minRepeats: 2,      // minimum number of consecutive repeats
  debounce: 250       // ms to debounce workspace changes
};


/**
 * Convert a leader subsequence (already marked) into a custom component
 * (procedure) and replace other occurrences with calls. This mirrors the
 * behavior implemented in `warning_repetition.js` but exposes it programmatically
 * so UI pages can trigger a conversion without clicking the bubble button.
 * @param {!Blockly.Block} leader The leader block (first block of the subsequence).
 */
Blockly.RepetitionDetector.convertLeaderToCustomComponent = function(leader) {
  if (!leader || !leader.workspace) return;
  // Allow conversion only for blocks explicitly marked as leaders by the
  // detector. This prevents converting arbitrary blocks or internal
  // non-leader blocks.
  if (!leader._repetitionLeader) {
    try { console.warn('convertLeaderToCustomComponent: block is not a marked leader'); } catch (e) {}
    return;
  }
  try {
    // Determine subsequence length L by walking next while marked.
    var L = 1;
    var last = leader;
    while (last && last.getNextBlock && last.getNextBlock() && last.getNextBlock()._repetitionMarked) {
      last = last.getNextBlock();
      L++;
    }

    function blockSignature(b) {
      var t = b.type || (b.getType && b.getType && b.getType()) || '';
      var parts = [t];
      if (b.inputList && b.inputList.length) {
        for (var ii = 0; ii < b.inputList.length; ii++) {
          var input = b.inputList[ii];
          var childType = 'null';
          if (input && input.connection && input.connection.targetBlock) {
            var child = input.connection.targetBlock();
            if (child) childType = child.type || (child.getType && child.getType && child.getType()) || 'unknown';
          }
          parts.push((input.name || 'input') + ':' + childType);
        }
      }
      return parts.join('|');
    }

    var leaderSigs = [];
    var cur = leader;
    for (var k = 0; k < L && cur; k++) {
      leaderSigs.push(blockSignature(cur));
      cur = cur.getNextBlock && cur.getNextBlock();
    }

    var ws = leader.workspace;
    var topBlocks = ws.getTopBlocks(true);
    var allChains = [];
    for (var ti = 0; ti < topBlocks.length; ti++) {
      var chain = [];
      for (var c = topBlocks[ti]; c; c = c.getNextBlock && c.getNextBlock()) chain.push(c);
      allChains.push(chain);
    }

    var occurrences = [];
    for (var ci = 0; ci < allChains.length; ci++) {
      var chain2 = allChains[ci];
      for (var i = 0; i + L <= chain2.length; i++) {
        var match = true;
        for (var j = 0; j < L; j++) {
          if (blockSignature(chain2[i + j]) !== leaderSigs[j]) { match = false; break; }
        }
        if (match) occurrences.push({chainIndex: ci, start: i});
      }
    }

    if (occurrences.length === 0) return;

    var baseName = (Blockly.Msg && Blockly.Msg.PROCEDURES_DEFNORETURN_PROCEDURE) || 'component';
    var procName = Blockly.Procedures.findLegalName(baseName, leader);

    Blockly.Events.setGroup(true);
    var defBlock = ws.newBlock('procedures_defnoreturn');
    try { defBlock.setFieldValue(procName, 'NAME'); } catch (e) {}
    defBlock.initSvg && defBlock.initSvg();
    defBlock.render && defBlock.render();
    try {
      var leaderXY = leader.getRelativeToSurfaceXY && leader.getRelativeToSurfaceXY();
      if (leaderXY) defBlock.moveBy(leaderXY.x - 200, leaderXY.y);
    } catch (e) {}

    // Move selected occurrence blocks into procedure STACK input if possible.
    try {
      // Determine which occurrence corresponds to the clicked leader.
      var firstOccIndex = 0;
      try {
        var leaderLocation = null;
        // Try by reference first.
        for (var ci2 = 0; ci2 < allChains.length; ci2++) {
          var chain2 = allChains[ci2];
          for (var si = 0; si < chain2.length; si++) {
            if (chain2[si] === leader) { leaderLocation = {chainIndex: ci2, start: si}; break; }
          }
          if (leaderLocation) break;
        }
        // If not found by reference, try by id (more robust across clones).
        if (!leaderLocation && leader && leader.id) {
          for (var ci3 = 0; ci3 < allChains.length; ci3++) {
            var chain3 = allChains[ci3];
            for (var sj = 0; sj < chain3.length; sj++) {
              try {
                if (chain3[sj] && chain3[sj].id === leader.id) { leaderLocation = {chainIndex: ci3, start: sj}; break; }
              } catch (ee) {}
            }
            if (leaderLocation) break;
          }
        }
        // If we found a leader location, find which occurrence index matches it.
        if (leaderLocation) {
          for (var occIdx = 0; occIdx < occurrences.length; occIdx++) {
            var o = occurrences[occIdx];
            if (o.chainIndex === leaderLocation.chainIndex && o.start === leaderLocation.start) { firstOccIndex = occIdx; break; }
          }
        }
        console.log('convertLeaderToCustomComponent: leader id=', leader && leader.id, 'leaderLocation=', leaderLocation, 'selectedOccIndex=', firstOccIndex, 'occurrences=', occurrences);
      } catch (e) {
        firstOccIndex = 0;
      }
      var firstOcc = occurrences[firstOccIndex];
      var firstChain = allChains[firstOcc.chainIndex];
      var firstLeader = firstChain[firstOcc.start];
      var lastOfFirst = firstLeader;
      for (var kk = 1; kk < L; kk++) lastOfFirst = lastOfFirst.getNextBlock && lastOfFirst.getNextBlock();

      var stackConn = defBlock.getInput && defBlock.getInput('STACK') && defBlock.getInput('STACK').connection;
      // We must detach exactly the subsequence [firstLeader .. lastOfFirst]
      // from its surrounding neighbours before connecting it into the
      // procedure STACK. Otherwise remaining next/previous links may pull
      // additional blocks into the definition (observed as "whole block"
      // becoming the component).
      try {
        // Surrounding connections (may be null at chain edges).
        var prevTarget = firstLeader.previousConnection && firstLeader.previousConnection.targetConnection;
        var nextTarget = lastOfFirst.nextConnection && lastOfFirst.nextConnection.targetConnection;

        // If both sides exist, reconnect previous -> next to close the gap.
        if (prevTarget && nextTarget) {
          try { prevTarget.connect(nextTarget); } catch (e) { /* best-effort */ }
        }

        // Now disconnect the subsequence's own boundary connections so the
        // blocks become a separate stack that can be attached into STACK.
        try { if (firstLeader.previousConnection && firstLeader.previousConnection.isConnected()) firstLeader.previousConnection.disconnect(); } catch (e) {}
        try { if (lastOfFirst.nextConnection && lastOfFirst.nextConnection.isConnected()) lastOfFirst.nextConnection.disconnect(); } catch (e) {}

        // Finally attach the (now detached) subsequence into the procedure.
        if (stackConn && firstLeader.previousConnection) {
          try { stackConn.connect(firstLeader.previousConnection); } catch (e) { /* ignore */ }
        }

        // Mark blocks in the moved first occurrence as converted so they
        // are ignored by future detection passes. This is a lightweight
        // marker used elsewhere (warning_repetition fallback uses it too).
        try {
          var mb = firstLeader;
          for (var mi = 0; mi < L && mb; mi++) {
            try { mb._convertedToCustom = true; } catch (e) {}
            mb = mb.getNextBlock && mb.getNextBlock();
          }
        } catch (e) {}
      } catch (e) {
        console.warn('Failed to detach and move first occurrence into procedure', e);
      }
    } catch (e) { console.warn('Failed to move first occurrence into procedure', e); }

    // Replace other occurrences with calls and delete originals. Skip the
    // occurrence we moved into the procedure (firstOccIndex).
    for (var oi = 0; oi < occurrences.length; oi++) {
      if (typeof firstOccIndex !== 'undefined' && oi === firstOccIndex) continue;
      var occ = occurrences[oi];
      var chain = allChains[occ.chainIndex];
      var first = chain[occ.start];
      var last = first;
      for (var r = 1; r < L; r++) last = last.getNextBlock && last.getNextBlock();

      var call = ws.newBlock('procedures_callnoreturn');
      try { call.setFieldValue(procName, 'NAME'); } catch (e) {}
      call.initSvg && call.initSvg();
      call.render && call.render();
      try { var firstXY = first.getRelativeToSurfaceXY && first.getRelativeToSurfaceXY(); if (firstXY) call.moveBy(firstXY.x, firstXY.y); } catch (e) {}

      try {
        var prevConn = first.previousConnection && first.previousConnection.targetConnection;
        var nextConn = last.nextConnection && last.nextConnection.targetConnection;
        if (prevConn && call.previousConnection) prevConn.connect(call.previousConnection);
        if (nextConn && call.nextConnection) call.nextConnection.connect(nextConn);
      } catch (e) {}

      for (var dIdx = 0; dIdx < L; dIdx++) {
        try { var b = chain[occ.start + dIdx]; if (b && b.dispose) b.dispose(true, false); } catch (e) {}
      }
    }

    Blockly.Events.setGroup(false);

    // Re-run detector shortly after conversion to refresh marks and avoid
    // race conditions with events generated by connecting/disconnecting.
    try {
      setTimeout(function() {
        try { Blockly.RepetitionDetector.checkWorkspaceForRepetitions(ws); } catch (e) {}
      }, 50);
    } catch (e) {}

    try { defBlock.select && defBlock.select(); } catch (e) {}
  } catch (e) {
    console.error('convertLeaderToCustomComponent failed', e);
  }
};


/**
 * Initialize the detector for a workspace.
 * Registers a debounced change listener that runs the check.
 * Returns an object with a `dispose` method to remove the listener.
 * @param {!Blockly.Workspace} workspace
 * @param {!Object=} opt_options
 * @return {{dispose: function()}}
 */
Blockly.RepetitionDetector.initWorkspace = function(workspace, opt_options) {
  var options = {};
  for (var k in Blockly.RepetitionDetector.DEFAULTS) options[k] =
      Blockly.RepetitionDetector.DEFAULTS[k];
  if (opt_options) {
    for (var k2 in opt_options) options[k2] = opt_options[k2];
  }

  var timeoutId = null;

  var listener = function(event) {
    // Debounce rapid events
    if (timeoutId) {
      clearTimeout(timeoutId);
    }
    timeoutId = setTimeout(function() {
      timeoutId = null;
      try {
        Blockly.RepetitionDetector.checkWorkspaceForRepetitions(workspace, options);
      } catch (e) {
        // Ensure listener doesn't break on unexpected errors.
        window.console && console.error && console.error('RepetitionDetector error', e);
      }
    }, options.debounce);
  };

  // Register and run an initial pass.
  workspace.addChangeListener(listener);
  Blockly.RepetitionDetector.checkWorkspaceForRepetitions(workspace, options);

  return {
    dispose: function() {
      workspace.removeChangeListener(listener);
    }
  };
}

/**
 * Check a workspace for repeated subsequences and mark blocks found.
 * This is a best-effort, configurable detector. It walks linear "next" chains
 * starting at top-level blocks.
 * @param {!Blockly.Workspace} workspace
 * @param {!Object=} opt_options
 */
Blockly.RepetitionDetector.checkWorkspaceForRepetitions = function(workspace, opt_options) {
  var options = opt_options || Blockly.RepetitionDetector.DEFAULTS;

  // Cleanup: some blocks may have a stale `_convertedToCustom` marker left
  // behind after conversions/undos. If a block is marked as converted but is
  // no longer inside a procedure definition, remove that marker so the block
  // can participate in future repetition detection rounds.
  try {
    var allForCleanup = workspace.getAllBlocks ? workspace.getAllBlocks() : [];
    for (var _ci = 0; _ci < allForCleanup.length; _ci++) {
      var _b = allForCleanup[_ci];
      if (!_b || !_b._convertedToCustom) continue;
      // Walk surround parents to see if block is inside a procedure definition.
      var insideProc = false;
      try {
        var p = _b.getSurroundParent && _b.getSurroundParent();
        while (p) {
          var ptype = p.type || (p.getType && p.getType && p.getType()) || '';
          if (typeof ptype === 'string' && ptype.indexOf && ptype.indexOf('procedures_def') === 0) {
            insideProc = true;
            break;
          }
          p = p.getSurroundParent && p.getSurroundParent();
        }
      } catch (e) {
        // ignore and conservatively keep the flag for safety
        insideProc = true;
      }
      if (!insideProc) {
        try { delete _b._convertedToCustom; } catch (e) {}
      }
    }
  } catch (e) {
    // Don't let cleanup break detection.
  }

  // Collect all blocks that should be marked.
  var toMark = Object.create(null);
  // Collect leader blocks (first block of each repeated subsequence). We
  // declare this at function scope so leaders found in any chain/L are
  // available when applying warnings later.
  var leaders = Object.create(null);

  // Helper: decide whether a block should be considered a "custom component"
  // (procedure definition, procedure call, or previously converted custom).
  function isCustomComponentBlock(b) {
    if (!b) return false;
    try {
      var t = b.type || (b.getType && b.getType && b.getType()) || '';
      if (typeof t === 'string' && t.indexOf && t.indexOf('procedures_') === 0) return true;
      if (b._convertedToCustom) return true;
    } catch (e) {
      // Be conservative: if anything fails, don't treat as custom.
    }
    return false;
  }

  var topBlocks = workspace.getTopBlocks(true);
  // Collect all chain segments (we break chains at custom component blocks).
  var allChains = [];

  // Ensure a per-workspace history of previously-detected subsequence keys.
  // This lets us remember a subsequence that was converted/deleted so a later
  // re-insertion of the same subsequence can still be marked as a repeat.
  try {
    if (!workspace._repetitionHistory) workspace._repetitionHistory = Object.create(null);
  } catch (e) {}

  
  //console.log("TopBlocksAqui",topBlocks)

  for (var t = 0; t < topBlocks.length; t++) {
    var top = topBlocks[t];
    // Walk linear chain via getNextBlock(). We only consider the linear 'stack'
    // formed by next/previous connections. However, break chains at custom
    // component blocks so they are not considered for repetition detection.
    var chain = [];
  for (var cur = top; cur; cur = (cur.getNextBlock && cur.getNextBlock())) {
      if (isCustomComponentBlock(cur)) {
        // If we hit a custom component inside a top-level chain, close the
        // current chain segment and start a fresh one after it.
        if (chain.length) {
          allChains.push(chain);
          chain = [];
        }
        // Skip the custom component itself.
        continue;
      }
      chain.push(cur);
    }
    if (chain.length) {
      allChains.push(chain);
    }

    try { console.log('chainAqui', chain); } catch (e) {}

    var n = chain.length;
    if (n === 0) continue;

    // Map blocks to a structural signature for comparison. The signature
    // includes the block type and the types of blocks attached to its inputs
    // (one level deep). This allows detecting repeated structures like
    // "repeat ... do <print>" even when the repeat block appears later.
    var signatures = chain.map(function(b) {
      var t = b.type || (b.getType && b.getType && b.getType()) || '';
      var parts = [t];
      // If block has inputList, iterate inputs in order and include attached
      // child type (if any). Otherwise fall back to empty marker.
      if (b.inputList && b.inputList.length) {
        for (var ii = 0; ii < b.inputList.length; ii++) {
          var input = b.inputList[ii];
          var childType = 'null';
          if (input && input.connection && input.connection.targetBlock) {
            var child = input.connection.targetBlock();
            if (child) childType = child.type || (child.getType && child.getType && child.getType()) || 'unknown';
          }
          parts.push((input.name || 'input') + ':' + childType);
        }
      }
      return parts.join('|');
    });

    console.log('signaturesAqui',signatures)


    // For each possible subsequence length, find repeated occurrences anywhere
    var maxL = Math.min(options.maxSubseqLen, n);
    for (var L = 1; L <= maxL; L++) {
      // Map from subsequence key to list of start indices where it appears.
      var map = Object.create(null);
      for (var i = 0; i + L <= n; i++) {
        // Build a key for signatures[i..i+L-1]
        var keyParts = [];
        for (var k = 0; k < L; k++) keyParts.push(signatures[i + k]);
        var key = keyParts.join('||');
        (map[key] || (map[key] = [])).push(i);
      }

    console.log('mapAquiL',L,map)
      
    console.log(JSON.stringify(map))



    //===MARK REPEATS ESTRUCTURE===

      // For any subsequence that appears at least minRepeats times,
      // mark all blocks in all occurrences. Additionally, if a subsequence
      // was seen previously (stored in workspace._repetitionHistory) but
      // currently has only a single occurrence because others were removed
      // via conversion, we still mark the current occurrence so users see
      // it can be turned back into a reusable component.
      // Track leaders (the first block of each matching subsequence) so we
      // can place the "Reusable component" warning only on that block.
    for (var key2 in map) {
        var occ = map[key2];
        // If we have a remembered occurrence from before, treat this as
        // repeated even if there is only one current occurrence.
        var remembered = false;
        try { remembered = !!(workspace._repetitionHistory && workspace._repetitionHistory[key2]); } catch (e) {}
        if (occ.length >= options.minRepeats) {
          // remember this subsequence for future additions
          try { workspace._repetitionHistory[key2] = true; } catch (e) {}
        } else if (occ.length > 0 && remembered) {
          // artificially treat as repeated so we mark the existing
          // occurrence (this helps when previous occurrences were
          // converted into components).
          // We don't update the remembered flag here (it's already true).
        }
        if (occ.length >= options.minRepeats || (occ.length > 0 && remembered)) {
          for (var oi = 0; oi < occ.length; oi++) {
            var start = occ[oi];
                for (var j = 0; j < L; j++) {
                  var idx = start + j;
                  var blk = chain[idx];
                  // Never mark custom component blocks even if they somehow
                  // appear here; our chain-building should avoid them but be safe.
                  if (isCustomComponentBlock(blk)) continue;
                  toMark[blk.id] = true;
              // Mark the first block of this subsequence as the leader.
            if (j === 0) {
              leaders[blk.id] = true;
              try { blk._repetitionLeader = true; } catch (e) {}
            }
              // Also mark immediate input children (and their next chains) so the
              // whole attached structure is included in the marking.
              if (blk.inputList && blk.inputList.length) {
                for (var inpi = 0; inpi < blk.inputList.length; inpi++) {
                  var input = blk.inputList[inpi];
                  if (input && input.connection && input.connection.targetBlock) {
                    var child = input.connection.targetBlock();
                    for (var c = child; c; c = (c.getNextBlock && c.getNextBlock())) {
                      if (!c) break;
                      toMark[c.id] = true;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  // Apply warnings: for each block in workspace, set or clear warning
  var allBlocks = workspace.getAllBlocks();

  //console.log('toMarkAqui',toMark);
  //console.log('allBlocksAqui',allBlocks);

  // Note: previously this code disabled/re-enabled Blockly.Events to avoid
  // feedback loops when making UI changes. That behavior has been removed so
  // the detector no longer toggles global event handling.
  // If a drag is in progress, avoid making DOM/UI changes that can
  // interfere with dragging (creating icons/bubbles, connecting/disconnecting
  // blocks, etc). If we detect a drag, schedule a re-check shortly after
  // to apply UI updates when the drag ends.
  


  // COLOR HIGHLIGHTING AND WARNING ICONS

  console.log('toMarkAqui',toMark);

  console.log('allBlocksAqui',allBlocks);



  var dragInProgress = (typeof Blockly !== 'undefined' && Blockly.dragMode_ !== Blockly.DRAG_NONE);
  var pendingRefreshScheduled = false;

  for (var bi = 0; bi < allBlocks.length; bi++) {




    var b = allBlocks[bi];

    // Skip marking UI for custom component blocks entirely. If they were
    // previously marked, clear their marks and restore colours/warnings.
    if (isCustomComponentBlock(b)) {
      if (b._repetitionMarked) {
        b.setWarningText && b.setWarningText(null);
        b._repetitionMarked = false;
        try { delete b._repetitionLeader; } catch (e) {}
        try {
          if (b._savedColour !== undefined && b.setColour) {
            b.setColour(b._savedColour);
            delete b._savedColour;
          }
        } catch (e) {}
      }
      continue;
    }


    if (toMark[b.id]) {
      // Only set warning on leader blocks. The `leaders` map may be undefined
      // if no repeats were found in the earlier scope; guard accordingly.
      if (typeof leaders !== 'undefined' && leaders[b.id]) {
        // If a specialized WarningRepetition class is available, use it so
        // the bubble contains the conversion button and custom behavior.
        if (typeof Blockly.WarningRepetition !== 'undefined') {
          try {
            try { console.log('RepetitionDetector: creating WarningRepetition for block', b && b.id); } catch (e) {}
            // If the block already has a different warning, remove it.
            if (b.warning && !(b.warning instanceof Blockly.WarningRepetition)) {
              b.warning.dispose();
            }
            // Only create the SVG-based icon if the block is rendered and has
            // an SVG root. Creating icons for unrendered blocks can cause
            // null-pointer errors deep in the rendering/bubble code.
            if (b.rendered && b.getSvgRoot && b.getSvgRoot()) {
              if (!b.warning) {
                // Create the custom warning icon instance for this block.
                b.warning = new Blockly.WarningRepetition(b);
                try { console.log('RepetitionDetector: created b.warning', !!b.warning); } catch (e) {}
              }
            } else {
              // Defer creation until the block is rendered. Schedule a
              // re-check shortly to pick it up when available.
              b._repetitionPendingIcon = true;
              if (!pendingRefreshScheduled) {
                pendingRefreshScheduled = true;
                setTimeout(function() {
                  try {
                    Blockly.RepetitionDetector.checkWorkspaceForRepetitions(workspace, options);
                  } catch (e) {}
                }, 350);
              }
            }
            // Set the repetition-specific text under a unique id so we can
            // manage it independently of other warnings.
            try { console.log('RepetitionDetector: setting repetition text on', b.id); } catch (e) {}
            b.warning.setText('Reusable component', 'repetition');
          } catch (e) {
            // Fall back to the generic API if the specialized class fails.
            b.setWarningText && b.setWarningText('Reusable component');
          }
        } else {
          b.setWarningText && b.setWarningText('Reusable component');
        }
      } else {
        // Ensure children do not show the reusable text. Only clear the
        // specific repetition warning id so other warnings remain.
        if (b.warning && typeof Blockly.WarningRepetition !== 'undefined' &&
            b.warning instanceof Blockly.WarningRepetition) {
          b.warning.setText('', 'repetition');
        } else {
          b.setWarningText && b.setWarningText(null);
        }
      }
      // Mark flag for external UI code.
      b._repetitionMarked = true;
      // If a drag is happening, skip DOM/UI updates now and schedule a
      // re-run after the drag ends to avoid freezing or broken pointer
      // state caused by creating icons/bubbles mid-drag.
      if (dragInProgress) {
        // Mark that we need to refresh UI later.
        b._repetitionPendingUi = true;
        if (!pendingRefreshScheduled) {
          pendingRefreshScheduled = true;
          // Re-run detector after a short delay to apply pending UI changes.
          setTimeout(function() {
            try {
              Blockly.RepetitionDetector.checkWorkspaceForRepetitions(workspace, options);
            } catch (e) {}
          }, 350);
        }
      } else {
        // Apply a brighter colour highlight while saving the original.
        try {
          if (b.getColour && b.setColour) {
            if (b._savedColour === undefined) {
              // Preserve the original colour so we can restore it later.
              var orig = b.getColour();
              b._savedColour = orig;
              // var highlight = brightenHex(orig, 0.60); // 35% towards white
              // b.setColour(highlight);
            }
          }
        } catch (e) {
          // Non-fatal; don't break the detector if colouring fails.
        }
      }
      } else {
      if (b._repetitionMarked) {
        // Remove previous mark and restore original colour/warning
        b.setWarningText && b.setWarningText(null);
        b._repetitionMarked = false;
        try { delete b._repetitionLeader; } catch (e) {}
        try {
          if (b._savedColour !== undefined && b.setColour) {
            b.setColour(b._savedColour);
            delete b._savedColour;
          }
        } catch (e) {
          // swallow
        }
      }
    }
  }

  // Re-enable events if we disabled them above.
  // No event re-enable step: we intentionally avoid toggling Blockly.Events.
};
