import * as GUISTATE_C from 'guiState.controller';
//@ts-ignore
import * as Blockly from 'blockly';
import * as $ from 'jquery';
import 'jquery-validate';
import 'jquery-hotkeys';
import 'bootstrap-tagsinput';
import 'bootstrap.wysiwyg';

const INITIAL_WIDTH = 0.3;
let blocklyWorkspace;
/**
 *
 */
export function init(): void {
    blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
    initView();
    initEvents();
}

function initView(): void {
    //@ts-ignore
    $('#infoContent').wysiwyg();
    //@ts-ignore
    $('#infoTags').tagsinput('removeAll');
    $('#infoContent').attr('data-placeholder', Blockly.Msg.INFO_DOCUMENTATION_HINT || 'Document your program here ...');
    $('.bootstrap-tagsinput input').attr('placeholder', Blockly.Msg.INFO_TAGS || 'Tags');
}

export function switchLanguage(): void {
    $('#infoContent').attr('data-placeholder', Blockly.Msg.INFO_DOCUMENTATION_HINT || 'Document your program here ...');
    $('.bootstrap-tagsinput input').attr('placeholder', Blockly.Msg.INFO_TAGS || 'Tags');
}

function initEvents(): void {
    $('#infoButton').off('click touchend');
    $('#infoButton').onWrap('click touchend', function(event): boolean {
        toggleInfo($(this));
        return false;
    });
    $(window).on('resize', function(e): void {
        if ($('#infoDiv').hasClass('rightActive')) {
            $('#infoContainer').css({
                width: $('#infoDiv').outerWidth(),
                height: $('#infoDiv').outerHeight() - $('.btn-toolbar.editor').outerHeight() - 57
            });
        }
    });
    $('#infoContent, #infoTags').on('change', function(): void {
        // TODO: here should be an onWrap. But this change is called during a run of another wrapped callback
        blocklyWorkspace.description = $('#infoContent').html();
        blocklyWorkspace.tags = $('#infoTags').val();
        if (GUISTATE_C.isProgramSaved()) {
            GUISTATE_C.setProgramSaved(false);
        }
        if (typeof $('#infoContent').html() === 'string' && $('#infoContent').html().length) {
            $('#infoButton').addClass('notEmpty');
        } else {
            $('#infoButton').removeClass('notEmpty');
        }
    });
    // prevent to copy eg ms word formatting!
    $('[contenteditable]#infoContent').onWrap('paste', function(e): void {
        e.preventDefault();
        let text: string = '';
        if (e.clipboardData || e.originalEvent.clipboardData) {
            text = (e.originalEvent || e).clipboardData.getData('text/plain');
            //@ts-ignore
        } else if (window.clipboardData) {
            //@ts-ignore
            text = window.clipboardData.getData('Text');
        }
        if (document.queryCommandSupported('insertText')) {
            document.execCommand('insertText', false, text);
        } else {
            document.execCommand('paste', false, text);
        }
        $('#infoContent').trigger('change');
    });
}

function toggleInfo($button): void {
    if ($('#infoButton').hasClass('rightActive')) {
        $('#blocklyDiv').closeRightView();
    } else {
        $('#infoContent').html(blocklyWorkspace.description);
        $('.bootstrap-tagsinput input').attr('placeholder', Blockly.Msg.INFO_TAGS || 'Tags');
        //@ts-ignore
        $('#infoTags').tagsinput('add', blocklyWorkspace.tags);
        $button.openRightView($('#infoDiv'), INITIAL_WIDTH);
    }
}
