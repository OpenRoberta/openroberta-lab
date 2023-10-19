define(["require", "exports", "jquery"], function (require, exports, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.fetchRSS = void 0;
    /**
     * Fetch RSS from the roberta homepage
     */
    function fetchRSS(successFn, errorFn) {
        $.ajax({
            url: 'https://www.roberta-home.de/?type=9818',
            dataType: 'xml',
            error: errorFn,
            success: successFn,
        });
    }
    exports.fetchRSS = fetchRSS;
});
