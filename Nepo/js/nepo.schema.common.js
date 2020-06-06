define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.schema = void 0;
    exports.schema = {
        "$schema": "http://json-schema.org/draft-07/schema#",
        "$id": "http://open-roberta.org/common.schema.json",
        "title": "common",
        "properties": {
            "title": { "type": "string" },
            "robotGroup": {
                "type": "string",
            },
            "blocks": {
                "type": "array"
            }
        },
        "required": ["title"]
    };
});
//# sourceMappingURL=nepo.schema.common.js.map