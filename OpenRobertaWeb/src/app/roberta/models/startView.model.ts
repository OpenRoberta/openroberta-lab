import * as $ from 'jquery';

/**
 * Fetch RSS from the roberta homepage
 */
export function fetchRSS(successFn, errorFn) {
    $.ajax({
        url: 'https://www.roberta-home.de/?type=9818',
        dataType: 'xml',
        error: errorFn,
        success: successFn,
    });
}
