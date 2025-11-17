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

  // Only convert blocks flagged as leaders.
  if (!leader._repetitionLeader) {
    console.warn('convertLeaderToCustomComponent: block is not a marked leader');
    return;
  }

  try {
    const ws = leader.workspace;

    // -------------------------------------------------------------
    // 1) Determine subsequence length (L)
    // -------------------------------------------------------------
    let L = 1;
    let cursor = leader;
    while (cursor?.getNextBlock?.()?._repetitionMarked) {
      cursor = cursor.getNextBlock();
      L++;
    }

    // -------------------------------------------------------------
    // 2) Utility: generate a structural signature for each block
    // -------------------------------------------------------------
    function blockSignature(b) {
      const type = b.type || b.getType?.() || '';
      const parts = [type];

      if (b.inputList?.length) {
        for (const input of b.inputList) {
          const child = input.connection?.targetBlock?.();
          const childType = child ? (child.type || child.getType?.() || 'unknown') : 'null';
          parts.push(`${input.name || 'input'}:${childType}`);
        }
      }
      return parts.join('|');
    }

    // -------------------------------------------------------------
    // 3) Build signatures for leader subsequence
    // -------------------------------------------------------------
    const leaderSigs = [];
    cursor = leader;
    for (let i = 0; i < L && cursor; i++) {
      leaderSigs.push(blockSignature(cursor));
      cursor = cursor.getNextBlock?.();
    }

    // -------------------------------------------------------------
    // 4) Gather all top-level block chains
    // -------------------------------------------------------------
    const topBlocks = ws.getTopBlocks(true);
    const allChains = topBlocks.map(top => {
      const chain = [];
      for (let b = top; b; b = b.getNextBlock?.()) chain.push(b);
      return chain;
    });

    // -------------------------------------------------------------
    // 5) Find all occurrences of the same subsequence
    // -------------------------------------------------------------
    const occurrences = [];
    allChains.forEach((chain, chainIndex) => {
      for (let i = 0; i + L <= chain.length; i++) {
        let matched = true;
        for (let j = 0; j < L; j++) {
          if (blockSignature(chain[i + j]) !== leaderSigs[j]) {
            matched = false;
            break;
          }
        }
        if (matched) occurrences.push({ chainIndex, start: i });
      }
    });

    if (occurrences.length === 0) return;

    // -------------------------------------------------------------
    // 6) Generate procedure definition block
    // -------------------------------------------------------------
    const baseName =
      (Blockly.Msg && Blockly.Msg.PROCEDURES_DEFNORETURN_PROCEDURE) ||
      'component';
    const procName = Blockly.Procedures.findLegalName(baseName, leader);

    Blockly.Events.setGroup(true);

    const defBlock = ws.newBlock('procedures_defnoreturn');
    defBlock.setFieldValue?.(procName, 'NAME');
    defBlock.initSvg?.();
    defBlock.render?.();

    const leaderXY = leader.getRelativeToSurfaceXY?.();
    if (leaderXY)
      defBlock.moveBy(leaderXY.x - 200, leaderXY.y);

    // -------------------------------------------------------------
    // 7) Find which occurrence is the actual clicked leader
    // -------------------------------------------------------------
    let selectedIndex = 0;

    function findLeaderLocation() {
      // Try reference by object
      for (let ci = 0; ci < allChains.length; ci++) {
        for (let si = 0; si < allChains[ci].length; si++) {
          if (allChains[ci][si] === leader) return { chainIndex: ci, start: si };
        }
      }
      // Try reference by ID
      for (let ci = 0; ci < allChains.length; ci++) {
        for (let si = 0; si < allChains[ci].length; si++) {
          if (allChains[ci][si]?.id === leader.id)
            return { chainIndex: ci, start: si };
        }
      }
      return null;
    }

    const leaderLoc = findLeaderLocation();
    if (leaderLoc) {
      for (let i = 0; i < occurrences.length; i++) {
        const occ = occurrences[i];
        if (occ.chainIndex === leaderLoc.chainIndex && occ.start === leaderLoc.start) {
          selectedIndex = i;
          break;
        }
      }
    }

    // -------------------------------------------------------------
    // 8) Move the FIRST (selected) occurrence into the procedure
    // -------------------------------------------------------------
    const firstOcc = occurrences[selectedIndex];
    const firstChain = allChains[firstOcc.chainIndex];
    const firstLeader = firstChain[firstOcc.start];

    let lastInSeq = firstLeader;
    for (let i = 1; i < L; i++) {
      lastInSeq = lastInSeq.getNextBlock?.();
    }

    const stackConn = defBlock.getInput?.('STACK')?.connection;

    // Disconnect the subsequence cleanly
    const prevConnTarget =
      firstLeader.previousConnection?.targetConnection || null;
    const nextConnTarget =
      lastInSeq.nextConnection?.targetConnection || null;

    // Close the gap: reconnect prev → next
    if (prevConnTarget && nextConnTarget) {
      try { prevConnTarget.connect(nextConnTarget); } catch {}
    }

    // Now detach boundaries
    try { firstLeader.previousConnection?.disconnect(); } catch {}
    try { lastInSeq.nextConnection?.disconnect(); } catch {}

    // Attach subsequence into procedure
    if (stackConn && firstLeader.previousConnection) {
      try { stackConn.connect(firstLeader.previousConnection); } catch {}
    }

    // Mark blocks as converted (prevent future detection)
    {
      let b = firstLeader;
      for (let i = 0; i < L && b; i++) {
        b._convertedToCustom = true;
        b = b.getNextBlock?.();
      }
    }

    // -------------------------------------------------------------
    // 9) Replace all other occurrences with calls
    // -------------------------------------------------------------
    occurrences.forEach((occ, idx) => {
      if (idx === selectedIndex) return;

      const chain = allChains[occ.chainIndex];
      const startBlock = chain[occ.start];

      let endBlock = startBlock;
      for (let i = 1; i < L; i++) endBlock = endBlock.getNextBlock?.();

      // Create call block
      const call = ws.newBlock('procedures_callnoreturn');
      call.setFieldValue?.(procName, 'NAME');
      call.initSvg?.();
      call.render?.();

      const xy = startBlock.getRelativeToSurfaceXY?.();
      if (xy) call.moveBy(xy.x, xy.y);

      // Connect call block in place of the sequence
      const prev = startBlock.previousConnection?.targetConnection;
      const next = endBlock.nextConnection?.targetConnection;

      if (prev && call.previousConnection) prev.connect(call.previousConnection);
      if (next && call.nextConnection) call.nextConnection.connect(next);

      // Delete original blocks
      for (let i = 0; i < L; i++) {
        chain[occ.start + i]?.dispose?.(true, false);
      }
    });

    Blockly.Events.setGroup(false);

    // -------------------------------------------------------------
    // 10) Refresh repetition detector
    // -------------------------------------------------------------
    setTimeout(() => {
      try {
        Blockly.RepetitionDetector.checkWorkspaceForRepetitions(ws);
      } catch {}
    }, 50);

    // -------------------------------------------------------------
    // 11) Focus user on definition block
    // -------------------------------------------------------------
    defBlock.select?.();

  } catch (err) {
    console.error('convertLeaderToCustomComponent failed', err);
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
