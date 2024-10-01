/*
 important note:
    AceAjax types are incomplete some typing errors have to be suppressed with it-ignore
*/

let codeView: AceAjax.Editor;
let editor: AceAjax.Editor;
let editorTextly: AceAjax.Editor;
let currentLanguage: string;
let wasEdited: boolean = false;
let previousLineCount: number = 0;
let initialized: boolean = false;

var customCompleter = {
    getCompletions: function (editor, session, pos, prefix, callback) {
        var controlFlowKeywords = [
            'if',
            'else',
            'else if',
            '++',
            'while',
            'for',
            'for each',
            'break',
            'continue',
            'waitUntil',
            'orWaitFor',
            'wait ms',
            'return',
            'testme',
        ];

        var functionNames = [
            'sin',
            'cos',
            'tan',
            'asin',
            'acos',
            'atan',
            'exp',
            'square',
            'sqrt',
            'abs',
            'log10',
            'log',
            'randomInt',
            'randomFloat',
            'randomItem',
            'floor',
            'ceil',
            'round',
            'isEven',
            'isOdd',
            'isPrime',
            'isWhole',
            'isEmpty',
            'isPositive',
            'isNegative',
            'isDivisibleBy',
            'sum',
            'max',
            'min',
            'average',
            'median',
            'stddev',
            'size',
            'indexOfFirst',
            'indexOfLast',
            'get',
            'getFromEnd',
            'getFirst',
            'getLast',
            'getAndRemove',
            'getAndRemoveFromEnd',
            'getAndRemoveFirst',
            'getAndRemoveLast',
            'createListWith',
            'subList',
            'subListFromIndexToLast',
            'subListFromIndexToEnd',
            'subListFromFirstToIndex',
            'subListFromFirstToLast',
            'subListFromFirstToEnd',
            'subListFromEndToIndex',
            'subListFromEndToEnd',
            'subListFromEndToLast',
            'print',
            'createTextWith',
            'constrain',
            'getRGB',
        ];

        var completions = [];
        controlFlowKeywords.forEach(function (word) {
            completions.push({
                caption: word,
                value: word,
                meta: 'control',
                score: 1000,
            });
        });
        functionNames.forEach(function (word) {
            completions.push({
                caption: word,
                value: word,
                meta: 'function',
                score: 1000,
            });
        });
        callback(null, completions);
    },
};

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

    editorTextly = ace.edit('aceTextlyEditor');
    applyDefaultSettings(editorTextly);
    editorTextly.session.setUseWrapMode(true);
    editorTextly.setHighlightActiveLine(true);
    editorTextly.setShowPrintMargin(false);
    editorTextly.session.setMode('ace/mode/my-mode');
    // @ts-ignore
    aceTextlyEditor.completers = [customCompleter];
    editorTextly.setOptions({
        enableBasicAutocompletion: true,
        enableSnippets: true,
        enableLiveAutocompletion: true,
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

    editorTextly.session.on('change', function () {
        if (previousLineCount !== editorTextly.session.getLength()) {
            previousLineCount = editorTextly.session.getLength();
            resetActiveLine(editorTextly);
        }
        wasEdited = true;
    });

    editorTextly.session.on('changeFold', () => {
        resetActiveLine(editorTextly);
    });

    editorTextly.selection.on('changeSelection', () => {
        resetActiveLine(editorTextly);
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

export function getTextlyEditorCode() {
    return editorTextly.getValue();
}

export function setEditorCode(sourceCode: string) {
    editor.setValue(sourceCode, 0);
    editor.clearSelection();
    editor.focus();
    resetActiveLine(editor);
}

export function setTextlyEditorCode(sourceCode: string) {
    editorTextly.setValue(sourceCode, 0);
    editorTextly.clearSelection();
    editorTextly.focus();
    resetActiveLine(editorTextly);
}

export function setViewCode(sourceCode: string) {
    codeView.setValue(sourceCode, 0);
    codeView.clearSelection();
    codeView.moveCursorTo(0, 0);
    highlightEverySecondLine(codeView);
}

export function setTextlyAnnotations(annotations: any) {
    editorTextly.session.setAnnotations(annotations);
}
export function setTextlyEditable(editable) {
    if (editable) {
        editorTextly.setReadOnly(false);
        editorTextly.getSession().setUseWorker(true);
    } else {
        editorTextly.setReadOnly(true);
        editorTextly.getSession().setUseWorker(false);
    }
}

export function setTextlyVisibility(visible, method = 'display') {
    if (method === 'display') {
        if (visible) {
            $('#aceTextlyEditor').css('display', 'block');
        } else {
            $('#aceTextlyEditor').css('display', 'none');
        }
    } else if (method === 'visibility') {
        if (visible) {
            $('#aceTextlyEditor').css('visibility', 'visible');
        } else {
            $('#aceTextlyEditor').css('visibility', 'hidden');
        }
    }
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
