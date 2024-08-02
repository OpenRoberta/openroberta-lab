/*
 important note:
    AceAjax types are incomplete some typing errors have to be suppressed with it-ignore
*/

let codeView: AceAjax.Editor;
let editor: AceAjax.Editor;
let currentLanguage: string;
let wasEdited: boolean = false;
let previousLineCount: number = 0;
let initialized: boolean = false;

export function init() {
    if (initialized) return;

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

    editor.session.on('changeFold', () => {
        resetActiveLine(editor);
    });

    editor.selection.on('changeSelection', () => {
        resetActiveLine(editor);
    });

    codeView.session.on('changeFold', () => {
        highlightEverySecondLine(codeView);
    });

    $(window).resize(function () {
        codeView.resize();
        editor.resize();
    });
}

export function wasEditedByUser() {
    return wasEdited;
}

export function getCurrentLanguage() {
    return currentLanguage;
}

export function setWasEditedByUser(edited: boolean) {
    wasEdited = edited;
}

export function getEditorCode() {
    return editor.getValue();
}

export function setEditorCode(sourceCode: string) {
    editor.setValue(sourceCode, 0);
    editor.clearSelection();
    editor.focus();
    resetActiveLine(editor);
}

export function setViewCode(sourceCode: string) {
    codeView.setValue(sourceCode, 0);
    codeView.clearSelection();
    codeView.moveCursorTo(0, 0);
    highlightEverySecondLine(codeView);
}

export function setCodeLanguage(languageFileExtension: string) {
    let langToSet: string;
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

function applyDefaultSettings(ed: AceAjax.Editor) {
    ed.session.setUseWrapMode(true);
    ed.setShowPrintMargin(false);
}

function resetActiveLine(ed: AceAjax.Editor) {
    ed.setHighlightActiveLine(false);
    // @ts-ignore
    if (ed.getSelectedText().length == 0) {
        ed.setHighlightActiveLine(true);
    }
}

function getNumberOfVisibleRows(ed: AceAjax.Editor) {
    let hiddenRows: number = 0;

    //TODO add fold type once AceAjax typings are complete
    //@ts-ignore
    ed.session.getAllFolds().forEach(function (fold: any) {
        let startRow: number = fold.start.row;
        let endRow: number = fold.end.row;
        hiddenRows += endRow - startRow;
    });

    return ed.session.getLength() - hiddenRows;
}

// Function to style every second line
function highlightEverySecondLine(ed: AceAjax.Editor) {
    for (let id in ed.session.getMarkers(false)) {
        ed.session.removeMarker(Number(id));
    }

    for (let i = 0; i < getNumberOfVisibleRows(ed); i++) {
        if (i % 2 === 1) {
            ed.session.addGutterDecoration(i, 'ace_lineBackgroundGrey');
            ed.session.highlightLines(i, i, 'ace_lineBackgroundGrey', false);
        } else {
            ed.session.highlightLines(i, i, 'ace_lineBackgroundWhite', false);
        }
    }
}
