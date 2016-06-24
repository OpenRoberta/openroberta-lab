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
Blockly.RobControls.prototype.WIDTH_ = 198;

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
Blockly.RobControls.prototype.PATH_RUNONBRICK_ = 
  'M15.396 23.433c2.641-2.574 6.604-6.433 6.604-6.433s-3.963-3.859-6.604-6.433 '+
  'c-.363-.349-.853-.567-1.396-.567-1.104 0-2 .896-2 2v10c0 1.104.896 2 2 2 '+
  '.543 0 1.033-.218 1.396-.567z';
Blockly.RobControls.prototype.PATH_RUNINSIM_ = 
  'M13.998 12.002l.085.078 5.051 4.92-5.096 4.964-.038.036-.002-9.998m.002 '+
  '-2.002c-1.104 0-2 .896-2 2v10c0 1.104.896 2 2 2 .543 0 1.033-.218 1.393 '+
  '-.568 2.644-2.573 6.607-6.432 6.607-6.432s-3.963-3.859-6.604-6.433c-.363 '+
  '-.349-.853-.567-1.396-.567z';
Blockly.RobControls.prototype.PATH_SIMSTOP_ = 
  'M20 10h-8c-1.1 0-2 .9-2 2v8c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2v-8c0-1.1-.9-2-2 '+
  '-2z';
Blockly.RobControls.prototype.PATH_SIMFORWARD_ = 
  'M14.396 22.433c2.641-2.574 6.604-6.433 6.604-6.433s-3.963-3.859-6.604-6.433 '+
  'c-.363-.349-.853-.567-1.396-.567-1.104 0-2 .896-2 2v10c0 1.104.896 2 2 2 '+
  '.543 0 1.033-.218 1.396-.567z';
Blockly.RobControls.prototype.PATH_SIMPAUSE_ = 
  'M12 10c-1.104 0-2 .896-2 2v8c0 1.104.896 2 2 2s2-.896 2-2v-8c0-1.104-.896-2 '+
  '-2-2zM19 10c-1.104 0-2 .896-2 2v8c0 1.104.896 2 2 2s2-.896 2-2v-8c0-1.104 '+
  '-.896-2-2-2z';
Blockly.RobControls.prototype.PATH_SIMSTEP_ = 
  'M23 21.5v-10c0-1.104-0.895-2-2-2s-2 0.896-2 2v10c0 1.104 0.896 2 2 2 1.105 '+
  '0 2-0.896 2-2zM12.396 22.934c2.641-2.575 6.604-6.434 6.604-6.434s-3.963 '+
  '-3.859-6.604-6.433c-0.363-0.349-0.853-0.567-1.396-0.567-1.104 0-2 0.896-2 '+
  '2v10c0 1.104 0.896 2 2 2 0.543 0 1.033-0.218 1.396-0.566z';
Blockly.RobControls.prototype.PATH_SAVEPROGRAM_ =  
  'M21 13 l-.351.015c-.825-2.377-3.062-4.015-5.649-4.015-3.309 0-6 2.691-6 '+
  '6l.001.126c-1.724.445-3.001 2.013-3.001 3.874 0 2.206 1.794 4 4 4 h5v-4.586 '+
  'l-1.293 1.293c-.195.195-.451.293-.707.293s-.512-.098-.707-.293c-.391-.391 '+
  '-.391-1.023 0-1.414l2.999-2.999c.093-.093.203-.166.326-.217.244-.101.52 '+
  '-.101.764 0 .123.051.233.124.326.217l2.999 2.999c.391.391.391 1.023 0 1.414 '+
  '-.195.195-.451.293-.707.293s-.512-.098-.707-.293l-1.293-1.293v4.586h4c2.757 '+
  '0 5-2.243 5-5s-2.243-5-5-5z';
Blockly.RobControls.prototype.PATH_ZOOM_ =  
  'M17 8c-3.859 0-7 3.141-7 7 0 .763.127 1.495.354 2.183l-.749.75-.511.512 '+
  '-1.008 1.045c-.562.557-.891 1.345-.891 2.185 0 1.727 1.404 3.131 3.13 '+
  '3.131.757 0 1.504-.278 2.104-.784l.064-.055.061-.061 1.512-1.51.75-.749 '+
  'c.688.226 1.421.353 2.184.353 3.859 0 7-3.141 7-7s-3.141-7-7-7zm0 12c '+
  '-2.757 0-5-2.243-5-5s2.243-5 5-5 5 2.243 5 5-2.243 5-5 5zM17 11c-2.205 0-4 '+
  '1.794-4 4s1.795 4 4 4 4-1.794 4-4-1.795-4-4-4zm0 7c-1.656 0-3-1.344-3-3s '+
  '1.344-3 3-3 3 1.344 3 3-1.344 3-3 3z';
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
  'M15 10.1C15.1 9 13.3 8.7 13 9.8 12.8 10.2 13.4 11.5 12.7 11.3 11.6 10.2 '+
  '10.6 9.1 9.4 8.1 8.3 7.6 7.4 9.2 8.4 9.8 9.5 10.9 10.6 12 11.7 13.1 10.9 '+
  '13.2 9.7 12.8 9.1 13.7 8.6 14.9 10.1 15.2 11 15c1.3 0 2.7 0 4 0 0-1.6 0-3.3 '+
  '0-4.9zm8.7 12.2c-1.1-1.1-2.2-2.2-3.3-3.3 0.8-0.1 2.1 0.3 2.5-0.7 0.3-1.2 '+
  '-1.1-1.5-2-1.3-1.3 0-2.6 0-3.9 0 0 1.8 0 3.5 0 5.3 0.2 1.1 2 0.9 2-0.2 0 '+
  '-0.4-0.3-2 0.5-1.2 1 1 2 2.1 3.1 3 0.9 0.6 2-0.8 1.2-1.6l0 0 0 0c0 0 0 0 0 '+
  '0zM10.1 17c-1.1-0.1-1.4 1.7-0.3 2 0.4 0.3 1.7-0.3 1.5 0.3-1.1 1.1-2.3 2.2 '+
  '-3.3 3.3-0.5 1.1 1.1 2 1.8 1C10.9 22.5 12 21.4 13.1 20.3 13.2 21.1 12.8 '+
  '22.3 13.7 22.9 14.9 23.4 15.2 21.9 15 21 15 19.7 15 18.3 15 17 13.4 17 11.7 '+
  '17 10.1 17zM22.3 8.3C21.2 9.4 20.1 10.5 19 11.6 18.9 10.7 19.3 9.5 18.3 9 '+
  '17.1 8.7 16.8 10.2 17 11.1 17 12.4 17 13.7 17 15c1.8 0 3.5 0 5.3 0C23.3 '+
  '14.7 23.1 13 22 13 21.6 13 20 13.2 20.9 12.5 21.9 11.5 22.9 10.5 23.9 9.5 '+
  '24.4 8.6 23.1 7.5 22.3 8.3L22.3 8.3 22.3 8.3z';
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
  this.simVisible_ = false;
  var control = this;  
  this.svgGroup_ = Blockly.createSvgElement('g', {'class': 'blocklyButtons'}, null);
  this.runOnBrick = this.createButton_(this.PATH_RUNONBRICK_, 0, 0, Blockly.Msg.MENU_START_BRICK);
  this.runOnBrick.setAttribute("id", "runInSim");
  this.runInSim = this.createButton_(this.PATH_RUNINSIM_, 1, 0, Blockly.Msg.MENU_START_SIM);
  this.simStop = this.createButton_(this.PATH_SIMSTOP_, 1, 0, Blockly.Msg.MENU_SIM_STOP);
//  this.simStep = this.createButton_(this.PATH_SIMSTEP_, 1, 1);
//  this.simForward = this.createButton_(this.PATH_SIMFORWARD_, 1, 2);
//  this.simPause = this.createButton_(this.PATH_SIMPAUSE_, 1, 2);
  this.runInSim.setAttribute("id", "runInSim");
  this.simStop.setAttribute("id", "simStop");
  this.simStop.setAttribute('class', 'robButtonHidden');
//  this.simForward.setAttribute("id", "simForward");
//  this.simForward.setAttribute('class', 'robButtonHidden');
//  this.simStep.setAttribute("id", "simStep");
//  this.simStep.setAttribute('class', 'robButtonHidden');
//  this.simPause.setAttribute("id", "simPause");
//  this.simPause.setAttribute('class', 'robButtonHidden');
  this.saveProgram = this.createButton_(this.PATH_SAVEPROGRAM_, 2, 0, Blockly.Msg.MENU_SAVE);
  this.saveProgram.setAttribute("id", "saveProgram");
   
  if (this.zoom_) {
    this.zoomVisible_ = false;
    var zoom = this.createButton_(this.PATH_ZOOM_, 3, 0, Blockly.Msg.MENU_ZOOM);
    var zoominSvg = this.createButton_(this.PATH_ZOOMIN_, 3, 0, Blockly.Msg.MENU_ZOOM_IN);
    var zoomresetSvg = this.createButton_(this.PATH_ZOOMRESET_, 3, 1, Blockly.Msg.MENU_ZOOM_RESET);
    var zoomoutSvg = this.createButton_(this.PATH_ZOOMOUT_, 3, 2, Blockly.Msg.MENU_ZOOM_OUT);
    zoominSvg.setAttribute('class', 'robButtonHidden');
    zoomoutSvg.setAttribute('class', 'robButtonHidden');
    zoomresetSvg.setAttribute('class', 'robButtonHidden');

    // Attach event listeners. 
    Blockly.bindEvent_(zoomresetSvg, 'mousedown', workspace, function(e) {
      workspace.setScale(1);
      workspace.scrollCenter();
      e.stopPropagation();
      e.preventDefault()
      control.showZoom(false);
    });
    Blockly.bindEvent_(zoominSvg, 'mousedown', null, function(e) {
      workspace.zoomCenter(1);
      e.stopPropagation();
      e.preventDefault()
   });
    Blockly.bindEvent_(zoomoutSvg, 'mousedown', null, function(e) {
      workspace.zoomCenter(-1);
      e.stopPropagation();
      e.preventDefault()
    });
    Blockly.bindEvent_(zoom, 'mousedown', null, function(e) {
      control.showZoom(true);
      e.stopPropagation();  // Don't start a workspace scroll.
    });
    this.zoominSvg = zoominSvg;
    this.zoom = zoom;
    this.zoomoutSvg = zoomoutSvg;
    this.zoomresetSvg = zoomresetSvg;
  }
  return this.svgGroup_;
};

/**
 * Create a roberta button
 * @param {!Element} The button group.
 * @param {String} The path.
 * @return {!Element} The button.
 * @private
 */
Blockly.RobControls.prototype.createButton_ = function(pathD, posX, posY, tooltip) {
  var button = Blockly.createSvgElement('g',
      {'class': 'robButton',
       'transform': 'translate(' + (posX * 50) + ',' + (posY * -50) + ')'}, this.svgGroup_);
  var rect = Blockly.createSvgElement('rect',
      {'class': 'blocklyButtonBack',
       'y': '0',
       'rx': '2',
       'ry': '2',
       'width':'48',
       'height':'48',
      });
  rect.tooltip = tooltip;
  Blockly.Tooltip.bindMouseEvents(rect);
  button.appendChild(rect);
  var path = Blockly.createSvgElement('path',
      {'class': 'blocklyButtonPath',
       'd': pathD,
       'transform': 'scale(1.5)',
       'fill-rule': 'evenodd',
       'stroke-width': '0px',
       'fill': '#333'
      });
  path.tooltip = tooltip;
  Blockly.Tooltip.bindMouseEvents(path);
  button.appendChild(path);
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
        this.WIDTH_ - this.MARGIN_SIDE_; //- Blockly.Scrollbar.scrollbarThickness;
  }
  if (this.simVisible_){
    this.left_ += 159;
  }
  this.top_ = metrics.viewHeight + metrics.absoluteTop -
      this.HEIGHT_ - this.MARGIN_BOTTOM_; //- Blockly.Scrollbar.scrollbarThickness;
  this.svgGroup_.setAttribute('transform',
      'translate(' + this.left_ + ',' + (this.top_) + ')');
};

Blockly.RobControls.prototype.disable = function(button) {
  this[button].setAttribute('class', 'robButton disabled');
};

Blockly.RobControls.prototype.enable = function(button) {
  this[button].setAttribute('class', 'robButton');
};

Blockly.RobControls.prototype.showZoom = function(visible) {
  if (!this.zoom_ || this.zoomVisible_ === visible)
    return;
  if (visible) {
    this.zoom.setAttribute('class', 'robButtonHidden');
    this.zoominSvg.setAttribute('class', 'robButton');
    this.zoomoutSvg.setAttribute('class', 'robButton');
    this.zoomresetSvg.setAttribute('class', 'robButton');
  } else {
    this.zoom.setAttribute('class', 'robButton');
    this.zoominSvg.setAttribute('class', 'robButtonHidden');
    this.zoomoutSvg.setAttribute('class', 'robButtonHidden');
    this.zoomresetSvg.setAttribute('class', 'robButtonHidden');
  }
  this.zoomVisible_ = visible;
};

Blockly.RobControls.prototype.toogleSim = function() {
  if (!this.simVisible_ ) {     
    this.runInSim.setAttribute('class', 'robButtonHidden ');
    this.simStop.setAttribute('class', 'robButton simStop');
//    this.simForward.setAttribute('class', 'robButton simForward');
//    this.simStep.setAttribute('class', 'robButton simStep');
//    this.simPause.setAttribute('class', 'robButtonHidden');
    this.simVisible_ = true;
  } else {
    this.runInSim.setAttribute('class', 'robButton runInSim');
    this.simStop.setAttribute('class', 'robButtonHidden');
//    this.simForward.setAttribute('class', 'robButtonHidden');
//    this.simStep.setAttribute('class', 'robButtonHidden');
//    this.simPause.setAttribute('class', 'robButtonHidden');
    this.simVisible_ = false;
  }
};

Blockly.RobControls.prototype.setSimForward = function(visible) {
  if (this.simForwardVisible_ === visible)
    return;
//  if (visible) {
//    this.simForward.setAttribute('class', 'robButton simForward');
//    this.simPause.setAttribute('class', 'robButtonHidden');
//  } else {
//    this.simForward.setAttribute('class', 'robButtonHidden');
//    this.simPause.setAttribute('class', 'robButton simPause');
//  }
  this.simForwardVisible_ = visible;
};

Blockly.RobControls.prototype.setSimStart = function(visible) {
  if (this.simStartVisible_ === visible)
    return;
  if (visible) {
    this.runInSim.setAttribute('class', 'robButton runInSim');
    this.simStop.setAttribute('class', 'robButtonHidden');
    this.simStartVisible_ = true;
  } else {
    this.runInSim.setAttribute('class', 'robButtonHidden');
    this.simStop.setAttribute('class', 'robButton simStop');
    this.simStartVisible_ = false;
  }
};

Blockly.RobControls.prototype.dispose = function() {
    if (this.svgGroup_) {
    goog.dom.removeNode(this.svgGroup_);
    this.svgGroup_ = null;
  }
  this.workspace_ = null;
}