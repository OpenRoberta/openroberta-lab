define(["require","exports","log","jquery","blockly"],(function(e,s,a,o,t){Object.defineProperty(s,"__esModule",{value:!0}),s.displayInformation=s.displayMessage=s.displayToastMessages=s.displayPopupMessage=void 0;var i=[],n=3e3;function l(e,s,a,t){o("#confirm").attr("value",a),t?(o("#confirmCancel").attr("value",t),o("#messageConfirm").attr("lkey",e),o("#messageConfirm").html(s),o("#show-message-confirm").modal("show")):(o("#message").attr("lkey",e),o("#message").html(s),o("#show-message").modal("show"))}function r(){o("#toastText").html(i[i.length-1]),o("#toastContainer").delay(100).fadeIn("slow",(function(){o(this).delay(n).fadeOut("slow",(function(){i.pop(),i.length>0&&r()}))}))}function f(e,s,o,n,f){var p="string"!=typeof n&&n,d="";if(f&&(d="_"+f.toUpperCase()),null!=e){(e.indexOf(".")>=0||e.toUpperCase()!=e)&&a.info("Invalid message-key received: "+e);var c="Blockly.Msg."+e+d,g=t.Msg[e+d]||t.Msg[e];if(void 0!==g&&""!==g||(g=e),"string"==typeof o)g=g.indexOf("$")>=0?g.replace("$",o):g.replace(/\{[^\}]+\}/g,o);else if("object"==typeof o)if(g.indexOf("$")>=0){var m=Object.keys(o);g=g.replace("$",o[m[0]])}else Object.keys(o).forEach((function(e){o.hasOwnProperty(e)&&(g=g.replace("{"+e+"}",o[e]))}));"POPUP"===s?p?l(c,g+t.Msg.POPUP_CONFIRM_CONTINUE,"OK",t.Msg.POPUP_CANCEL):l(c,g,"OK"):"TOAST"===s&&(i.unshift(g),1===i.length&&r())}}s.displayPopupMessage=l,s.displayToastMessages=r,s.displayMessage=f,s.displayInformation=function(e,s,a,t,i){"ok"===e.rc?(o(".modal").modal("hide"),f(s,"TOAST",t,!1,i)):void 0===e.parameters?f(a,"POPUP",t,!1,i):f(a,"POPUP",e.parameters,!1,i)}}));
//# sourceMappingURL=msg.js.map
//# sourceMappingURL=msg.js.map
