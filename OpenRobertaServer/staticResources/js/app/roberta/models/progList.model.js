/**
 * Rest calls to the server related to program operations (save, delete,
 * share...)
 * 
 * @module rest/program
 */
define([ 'exports', 'comm' ], function(exports, COMM) {

    /**
     * Refresh program list
     */
    function loadProgList(successFn) {
        COMM.json("/program/listing/names", {
        }, successFn, "load program list");
    }
    exports.loadProgList = loadProgList;

    /**
     * Refresh example list
     */
    function loadExampleList(successFn) {
        COMM.json("/program/examples/names", {
        }, successFn, "load example list");
    }
    exports.loadExampleList = loadExampleList;

    /**
     * Refresh example list
     */
    function loadGalleryList(successFn) {
        $(".pace").show();
        COMM.json("/program/gallery", {
        }, successFn, "load gallery list");
        $(".pace").fadeOut(500);
    }
    exports.loadGalleryList = loadGalleryList;

});
