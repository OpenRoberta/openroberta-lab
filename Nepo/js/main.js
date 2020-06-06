require.config({
    paths: {
        'blockly': '../node_modules/blockly/blockly_compressed',
        'jquery': '../node_modules/jquery/dist/jquery.min',
        'ajv': '../node_modules/ajv/dist/ajv.bundle',
        'playground': '../js/playground',
    }
});
require(['require', 'exports', 'blockly', 'jquery', 'ajv', 'playground'], function (require, exports, Blockly, $, ajv, playground) {
    $(document).ready(function () {
        //window.xml2js = require('xml2js'.parseString);
        playground.init();
    });
});
//# sourceMappingURL=main.js.map