/**
 * @fileoverview Image blocks for MekeBlock.
 * @requires Blockly.Blocks
 * @author Evgeniya
 */
'use strict';

goog.provide('Blockly.Blocks.makeblockImage');

goog.require('Blockly.Blocks');

//TODO once this block will be used, move it to another file

/**
 * @lends Block
 */

Blockly.Blocks['makeblockImage_image'] = {
  /**
   * Represents an image.
   *
   * @constructs mbedImage_image
   * @this.Blockly.Block
   * @returns immediately
   * @memberof Block
   */
  init : function() {
      this.setColour(Blockly.CAT_IMAGE_RGB);
      this.appendDummyInput().appendField("    0      1      2      3     4     5     6     7     8      9      10      11     12     13     14     15");
      for (var i = 0; i < 8; i++){
        var input = this.appendDummyInput();
        input.appendField(i.toString());
        for (var j = 0; j < 16; j++){
          input.appendField(new Blockly.FieldTextInput('  ', this.validate_), "P" + j + i).appendField(' ');
        }
      }
     this.setOutput(true, 'Image');
     this.setTooltip(Blockly.Msg.IMAGE_TOOLTIP);
   },

   validate_ : function(p) {
      if (!Blockly.FieldTextInput.htmlInput_)
          return p;
      if (p == Blockly.FieldTextInput.htmlInput_.value) {
          if (p == '  ') {
              Blockly.FieldTextInput.htmlInput_.value = '#';
              return '#';
          } else if (p == '#') {
              Blockly.FieldTextInput.htmlInput_.value = '  ';
              return '  ';
          } else if (p.match(/^[1-8]$/)) {
              Blockly.FieldTextInput.htmlInput_.value = p;
              return p;
          } else if (p == ' ' || p == '0') {
              Blockly.FieldTextInput.htmlInput_.value = '  ';
              return '  ';
          } else if (p.substring(0, 2) == '  ') {
              Blockly.FieldTextInput.htmlInput_.value = p.substr(2);
              return p.substr(2);
          } else if (p.substring(0, 1) == '#') {
              Blockly.FieldTextInput.htmlInput_.value = p.substr(1);
              return p.substr(1);
          } else if (!p.match(/^[1-8]$/)) {
              Blockly.FieldTextInput.htmlInput_.value = '#';
              return '#';
          } else {
              Blockly.FieldTextInput.htmlInput_.value = '  ';
              return '  ';
          }
      }
      Blockly.FieldTextInput.htmlInput_.value = '';
      return p;
    }
};
