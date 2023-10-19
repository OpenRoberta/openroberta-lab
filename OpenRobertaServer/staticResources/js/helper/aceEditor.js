/*
 important note:
    AceAjax types are incomplete some typing errors have to be suppressed with it-ignore
*/
define(["require", "exports"], function (require, exports) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.setCodeLanguage = exports.setViewCode = exports.setEditorCode = exports.getEditorCode = exports.setWasEditedByUser = exports.getCurrentLanguage = exports.wasEditedByUser = exports.init = void 0;
    var codeView;
    var editor;
    var currentLanguage;
    var wasEdited = false;
    var previousLineCount = 0;
    var initialized = false;
    function init() {
        if (initialized)
            return;
        initialized = true;
        ace.require('ace/ext/language_tools');
        codeView = ace.edit('codeContent');
        applyDefaultSettings(codeView);
        codeView.setOptions({
            readOnly: true,
            highlightActiveLine: false,
            highlightGutterLine: false,
        });
        editor = ace.edit('aceEditor');
        applyDefaultSettings(editor);
        editor.setOptions({
            enableBasicAutocompletion: true,
            enableSnippets: false,
            enableLiveAutocompletion: false,
        });
        editor.session.on('change', function () {
            if (previousLineCount !== editor.session.getLength()) {
                previousLineCount = editor.session.getLength();
                resetActiveLine(editor);
            }
            wasEdited = true;
        });
        editor.session.on('changeFold', function () {
            resetActiveLine(editor);
        });
        editor.selection.on('changeSelection', function () {
            resetActiveLine(editor);
        });
        codeView.session.on('changeFold', function () {
            highlightEverySecondLine(codeView);
        });
        $(window).resize(function () {
            codeView.resize();
            editor.resize();
        });
    }
    exports.init = init;
    function wasEditedByUser() {
        return wasEdited;
    }
    exports.wasEditedByUser = wasEditedByUser;
    function getCurrentLanguage() {
        return currentLanguage;
    }
    exports.getCurrentLanguage = getCurrentLanguage;
    function setWasEditedByUser(edited) {
        wasEdited = edited;
    }
    exports.setWasEditedByUser = setWasEditedByUser;
    function getEditorCode() {
        return editor.getValue();
    }
    exports.getEditorCode = getEditorCode;
    function setEditorCode(sourceCode) {
        editor.setValue(sourceCode, 0);
        editor.clearSelection();
        editor.focus();
        resetActiveLine(editor);
    }
    exports.setEditorCode = setEditorCode;
    function setViewCode(sourceCode) {
        codeView.setValue(sourceCode, 0);
        codeView.clearSelection();
        codeView.moveCursorTo(0, 0);
        highlightEverySecondLine(codeView);
    }
    exports.setViewCode = setViewCode;
    function setCodeLanguage(languageFileExtension) {
        var langToSet;
        switch (languageFileExtension) {
            case 'py':
                langToSet = 'python';
                break;
            case 'java':
                langToSet = 'java';
                break;
            case 'ino':
            case 'nxc':
            case 'cpp':
                langToSet = 'c_cpp';
                break;
            case 'json':
                langToSet = 'json';
                break;
            default:
                langToSet = 'python';
        }
        editor.session.setMode('ace/mode/' + langToSet);
        codeView.session.setMode('ace/mode/' + langToSet);
        previousLineCount = editor.session.getLength();
        currentLanguage = langToSet;
    }
    exports.setCodeLanguage = setCodeLanguage;
    function applyDefaultSettings(ed) {
        ed.session.setUseWrapMode(true);
        ed.setShowPrintMargin(false);
    }
    function resetActiveLine(ed) {
        ed.setHighlightActiveLine(false);
        // @ts-ignore
        if (ed.getSelectedText().length == 0) {
            ed.setHighlightActiveLine(true);
        }
    }
    function getNumberOfVisibleRows(ed) {
        var hiddenRows = 0;
        //TODO add fold type once AceAjax typings are complete
        //@ts-ignore
        ed.session.getAllFolds().forEach(function (fold) {
            var startRow = fold.start.row;
            var endRow = fold.end.row;
            hiddenRows += endRow - startRow;
        });
        return ed.session.getLength() - hiddenRows;
    }
    // Function to style every second line
    function highlightEverySecondLine(ed) {
        for (var id in ed.session.getMarkers(false)) {
            ed.session.removeMarker(Number(id));
        }
        for (var i = 0; i < getNumberOfVisibleRows(ed); i++) {
            if (i % 2 === 1) {
                ed.session.addGutterDecoration(i, 'ace_lineBackgroundGrey');
                ed.session.highlightLines(i, i, 'ace_lineBackgroundGrey', false);
            }
            else {
                ed.session.highlightLines(i, i, 'ace_lineBackgroundWhite', false);
            }
        }
    }
});
