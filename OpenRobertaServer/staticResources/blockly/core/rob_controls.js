/**
 * @license
 * Visual Blocks Editor
 *
 * Copyright 2015 Google Inc.
 * https://developers.google.com/blockly/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @fileoverview Object representing a button icons.
 * @author carloslfu@gmail.com (Carlos Galarza)
 */
'use strict';

goog.provide('Blockly.RobControls');

goog.require('goog.dom');


/**
 * Class for a button controls.
 * @param {!Blockly.Workspace} workspace The workspace to sit in.
 * @constructor
 */
Blockly.RobControls = function(workspace, zoom) {
  this.workspace_ = workspace;
  this.zoom_ = zoom;
};

/**
 * Width of the button controls.
 * @type {number}
 * @private
 */
Blockly.RobControls.prototype.WIDTH_ = 348;

/**
 * Height of the button controls.
 * @type {number}
 * @private
 */
Blockly.RobControls.prototype.HEIGHT_ = 48;

/**
 * Distance between button controls and bottom edge of workspace.
 * @type {number}
 * @private
 */
Blockly.RobControls.prototype.MARGIN_BOTTOM_ = 12;

/**
 * Distance between button controls and right edge of workspace.
 * @type {number}
 * @private
 */
Blockly.RobControls.prototype.MARGIN_SIDE_ = 62;

/**
 * Paths for icons.
 * @type {String}
 * @private
 */
Blockly.RobControls.prototype.PATH_RUNONROBOT_ = 
  'M15.396 23.433c2.641-2.574 6.604-6.433 6.604-6.433s-3.963-3.859-6.604-6.433 '+
  'c-.363-.349-.853-.567-1.396-.567-1.104 0-2 .896-2 2v10c0 1.104.896 2 2 2 '+
  '.543 0 1.033-.218 1.396-.567z';
Blockly.RobControls.prototype.PATH_RUNINSIM_ = 
  'M13.998 12.002l.085.078 5.051 4.92-5.096 4.964-.038.036-.002-9.998m.002 '+
  '-2.002c-1.104 0-2 .896-2 2v10c0 1.104.896 2 2 2 .543 0 1.033-.218 1.393 '+
  '-.568 2.644-2.573 6.607-6.432 6.607-6.432s-3.963-3.859-6.604-6.433c-.363 '+
  '-.349-.853-.567-1.396-.567z';
Blockly.RobControls.prototype.PATH_SAVEPROGRAM_ =  
  'M21 13 l-.351.015c-.825-2.377-3.062-4.015-5.649-4.015-3.309 0-6 2.691-6 '+
  '6l.001.126c-1.724.445-3.001 2.013-3.001 3.874 0 2.206 1.794 4 4 4 h5v-4.586 '+
  'l-1.293 1.293c-.195.195-.451.293-.707.293s-.512-.098-.707-.293c-.391-.391 '+
  '-.391-1.023 0-1.414l2.999-2.999c.093-.093.203-.166.326-.217.244-.101.52 '+
  '-.101.764 0 .123.051.233.124.326.217l2.999 2.999c.391.391.391 1.023 0 1.414 '+
  '-.195.195-.451.293-.707.293s-.512-.098-.707-.293l-1.293-1.293v4.586h4c2.757 '+
  '0 5-2.243 5-5s-2.243-5-5-5z';
Blockly.RobControls.prototype.PATH_ZOOMIN_ =  
  'M18 16h-2v-2c0-.276-.224-.5-.5-.5s-.5.224-.5.5v2h-2c-.276 0-.5.224-.5.5 '+
  's.224.5.5.5h2v2c0 .276.224.5.5.5s.5-.224.5-.5v-2h2c.276 0 .5-.224.5-.5s '+
  '-.224-.5-.5-.5zM23.432 19.97l-.536-.537-.749-.75c.227-.688.354-1.42.354 '+
  '-2.183 0-3.859-3.141-7-7-7s-7 3.141-7 7 3.141 7 7 7c.763 0 1.496-.127 '+
  '2.184-.354l.75.749 1.512 1.51.061.061.064.055c.601.506 1.348.784 2.104.784 '+
  '1.726 0 3.13-1.404 3.13-3.131 0-.84-.328-1.628-.924-2.218l-.95-.986zm '+
  '-12.932-3.47c0-2.757 2.243-5 5-5s5 2.243 5 5-2.243 5-5 5-5-2.243-5-5z';
Blockly.RobControls.prototype.PATH_ZOOMOUT_ =  
  'M18 16h-5c-.276 0-.5.224-.5.5s.224.5.5.5h5c.276 0 .5-.224.5-.5s-.224-.5-.5 '+
  '-.5zM24.381 20.956l-.949-.986-.537-.537-.749-.75c.227-.688.354-1.42.354 '+
  '-2.183 0-3.859-3.14-7-7-7s-7 3.141-7 7 3.14 7 7 7c.763 0 1.496-.127 2.184 '+
  '-.354l.75.749 1.512 1.51.06.061.065.055c.601.506 1.348.784 2.104.784 1.726 '+
  '0 3.13-1.404 3.13-3.131 0-.84-.328-1.628-.924-2.218zm-13.881-4.456c0-2.757 '+
  '2.243-5 5-5s5 2.243 5 5-2.243 5-5 5-5-2.243-5-5z';
Blockly.RobControls.prototype.PATH_ZOOMRESET_ =  
  'M21.657 10.304c-3.124-3.073-8.189-3.073-11.313 0-3.124 3.074-3.124 8.057 0 '+
  '11.13l5.656 5.565 5.657-5.565c3.124-3.073 3.124-8.056 0-11.13zm-5.657 '+
  '8.195c-.668 0-1.295-.26-1.768-.732-.975-.975-.975-2.561 0-3.536.472-.472 '+
  '1.1-.732 1.768-.732s1.296.26 1.768.732c.975.975.975 2.562 0 3.536-.472.472 '+
  '-1.1.732-1.768.732z';
Blockly.RobControls.prototype.PATH_SHOWCODE_ =  
  'M12.671 21.5c-.512 0-1.023-.195-1.414-.586l-4.414-4.414 4.414-4.414c.781 '+
  '-.781 2.049-.781 2.828 0 .781.781.781 2.047 0 2.828l-1.585 1.586 1.585 '+
  '1.586c.781.781.781 2.047 0 2.828-.39.391-.902.586-1.414.586z M20.329 21.5 '+
  'c-.512 0-1.024-.195-1.414-.586-.781-.781-.781-2.047 0-2.828l1.585-1.586 '+
  '-1.585-1.586c-.781-.781-.781-2.047 0-2.828.779-.781 2.047-.781 2.828 0 '+
  'l4.414 4.414-4.414 4.414c-.39.391-.902.586-1.414.586z';
/**
 * The SVG group containing the button controls.
 * @type {Element}
 * @private
 */
Blockly.RobControls.prototype.svgGroup_ = null;

/**
 * Left coordinate of the button controls.
 * @type {number}
 * @private
 */
Blockly.RobControls.prototype.left_ = 0;

/**
 * Top coordinate of the button controls.
 * @type {number}
 * @private
 */
Blockly.RobControls.prototype.top_ = 0;

/**
 * Create the button controls.
 * @return {!Element} The button controls SVG group.
 */
Blockly.RobControls.prototype.createDom = function() {
  var workspace = this.workspace_;
  this.svgGroup_ = Blockly.createSvgElement('g', {'class': 'blocklyButtons'}, null);
  var runOnBrick = this.createButton_(this.PATH_RUNONROBOT_, 0);
  runOnBrick.setAttribute("id", "runOnBrick");
  var runInSim = this.createButton_(this.PATH_RUNINSIM_, 1);
  runInSim.setAttribute("id", "runInSim");
  var saveProgram = this.createButton_(this.PATH_SAVEPROGRAM_, 2);
  saveProgram.setAttribute("id", "saveProgram");
  var showCode = this.createButton_(this.PATH_SHOWCODE_, 3);
  showCode.setAttribute("id", "showCode");
  console.log(document.getElementById("showCode"));
  
  if (this.zoom_) {
    var zoominSvg = this.createButton_(this.PATH_ZOOMIN_, 4);
    var zoomoutSvg = this.createButton_(this.PATH_ZOOMOUT_, 5);
    var zoomresetSvg = this.createButton_(this.PATH_ZOOMRESET_, 6);

    // Attach event listeners.
    Blockly.bindEvent_(zoomresetSvg, 'mousedown', workspace, workspace.zoomReset);
    Blockly.bindEvent_(zoominSvg, 'mousedown', null, function(e) {
      workspace.zoomCenter(1);
      e.stopPropagation();  // Don't start a workspace scroll.
    });
    Blockly.bindEvent_(zoomoutSvg, 'mousedown', null, function(e) {
      workspace.zoomCenter(-1);
      e.stopPropagation();  // Don't start a workspace scroll.
    });
  }
  
  Blockly.bindEvent_(runOnBrick, 'mousedown', null, function(e) {
    if (ROBERTA_PROGRAM && typeof ROBERTA_PROGRAM.runOnBrick() === 'function') {
      LOG.info('save program from blockly button');
      ROBERTA_PROGRAM.runOnBrick();
    } else {
      console.warn("Warning: function 'runOnBrick' is not available, are you in playground?");
    }
  });
  Blockly.bindEvent_(runInSim, 'mousedown', null, function(e) {
    if (ROBERTA_PROGRAM && typeof ROBERTA_PROGRAM.runInSim() === 'function') {
      LOG.info('save program from blockly button');
      ROBERTA_PROGRAM.runInSim();
    } else {
      console.warn("Warning: function 'runInSim' is not available, are you in playground?");
    }
  });
  Blockly.bindEvent_(saveProgram, 'mousedown', null, function(e) {
  if (ROBERTA_PROGRAM && typeof ROBERTA_PROGRAM.save === 'function') {
      LOG.info('save program from blockly button');
      ROBERTA_PROGRAM.save();
    } else {
      console.warn("Warning: function 'save' is not available, are you in playground?");
    }
  });
  Blockly.bindEvent_(showCode, 'mousedown', null, function(e) {
  if (ROBERTA_PROGRAM && typeof ROBERTA_PROGRAM.showCode === 'function') {
      LOG.info('save program from blockly button');
      ROBERTA_PROGRAM.showCode();
    } else {
      console.warn("Warning: function 'showCode' is not available, are you in playground?");
    }
  });
  this.runOnBrick = runOnBrick;
  this.saveProgram = saveProgram;
  this.runInSim = runInSim;
  this.showCode = showCode;

  return this.svgGroup_;
};

/**
 * Create a roberta button
 * @param {!Element} The button group.
 * @param {String} The path.
 * @return {!Element} The button.
 * @private
 */
Blockly.RobControls.prototype.createButton_ = function(path, pos) {
  var button = Blockly.createSvgElement('g',
      {'class': 'robButton',
       'transform': 'translate(' + (pos * 50) + ',0)'}, this.svgGroup_);
  Blockly.createSvgElement('rect',
      {'class': 'blocklyButtonBack',
       'x': '0',
       'y': '0',
       'rx': '2',
       'ry': '2',
       'width':'48',
       'height':'48',
      },
      button);
  Blockly.createSvgElement('path',
      {'class': 'blocklyButtonPath',
       'd': path,
       'transform': 'scale(1.5)',
       'fill-rule': 'evenodd',
       'fill':'#fff',
       'stroke-width': '0px',
       'fill': '#333'
      },
      button);
  return button;
}

/**
 * Dispose of this button controls.
 * Unlink from all DOM elements to prevent memory leaks.
 */
Blockly.RobControls.prototype.dispose = function() {
  if (this.svgGroup_) {
    goog.dom.removeNode(this.svgGroup_);
    this.svgGroup_ = null;
  }
  this.workspace_ = null;
};

/**
 * Move the button controls to the bottom-right corner.
 */
Blockly.RobControls.prototype.position = function() {
  var metrics = this.workspace_.getMetrics();
  if (!metrics) {
    // There are no metrics available (workspace is probably not visible).
    return;
  }
  if (this.workspace_.RTL) {
    this.left_ = this.MARGIN_SIDE_ + Blockly.Scrollbar.scrollbarThickness;
  } else {
    this.left_ = metrics.viewWidth + metrics.absoluteLeft -
        this.WIDTH_ - this.MARGIN_SIDE_ - Blockly.Scrollbar.scrollbarThickness;
  }
  this.top_ = metrics.viewHeight + metrics.absoluteTop -
      this.HEIGHT_ - this.MARGIN_BOTTOM_ - Blockly.Scrollbar.scrollbarThickness;
  this.svgGroup_.setAttribute('transform',
      'translate(' + this.left_ + ',' + (this.top_) + ')');
};

Blockly.RobControls.prototype.disable = function(button) {
  this[button].setAttribute('class', 'robButton disabled');
};

Blockly.RobControls.prototype.enable = function(button) {
  this[button].setAttribute('class', 'robButton');
};
