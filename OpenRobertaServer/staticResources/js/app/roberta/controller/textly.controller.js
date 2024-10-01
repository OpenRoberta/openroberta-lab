define(["require","exports","guiState.controller","jquery","program.model","program.controller","message","blockly","aceEditor"],(function(t,e,o,i,a,r,n,s,l){Object.defineProperty(e,"__esModule",{value:!0}),e.init=void 0;var c=!1,g="";function d(t,e){if(("tabTextly"===o.getPrevView()||"tabConfiguration"===o.getPrevView()||"tabNN"===o.getPrevView())&&l.wasEditedByUser()){g=l.getTextlyEditorCode();var d=o.getBlocklyWorkspace(),y=s.Xml.workspaceToDom(d),b=s.Xml.domToText(y),m=o.getConfigurationXML(),u=!o.isConfigurationStandard()&&!o.isConfigurationAnonymous()?o.getConfigurationName():void 0,f=o.getLanguage();a.setTextly(o.getProgramName(),u,b,m,g,r.getSSID(),r.getPassword(),f,(function(a){if("ok"!=a.rc)return i("#tabTextly").tabWrapShow(),i("#show-message-confirm").oneWrap("shown.bs.modal",(function(){i("#confirm").off(),i("#confirm").on("click",(function(t){t.preventDefault(),l.setTextlyEditorCode(g),c=!0;var e,o=(e=a.textlyErrors,console.log(e),e.map((function(t){var e=t.message,o=s.Msg[t.message];return void 0!==o&&""!==o||(o=e),{row:t.line-1,column:t.charPositionInLine,text:o,type:"error"}})));l.setTextlyAnnotations(o),i(".modal").modal("hide")})),i("#confirmCancel").off(),i("#confirmCancel").on("click",(function(t){t.preventDefault(),l.setWasEditedByUser(!1),i(e).tabWrapShow()}))})),n.displayMessage("TEXTLY_EDITOR_CLOSE_CONFIRMATION","TEXTLYPOPUP","",!0,!1),!1;t&&(r.reloadProgram(a),i(e).tabWrapShow()),o.setState(a),l.setTextlyEditorCode(a.programAsTextly),l.setWasEditedByUser(!1)}))}}e.init=function(){i("#tabTextly").onWrap("shown.bs.tab",(function(){var t,e,g,d,y,b,m;i("#textlyEditorPane").show(),l.setTextlyVisibility(!0,"visibility"),l.setTextlyEditable(!0),o.setView("tabTextly"),c?c=!1:(t=!0,e=o.getBlocklyWorkspace(),g=s.Xml.workspaceToDom(e),d=s.Xml.domToText(g),y=o.isConfigurationStandard()||o.isConfigurationAnonymous()?void 0:o.getConfigurationName(),b=o.isConfigurationAnonymous()?o.getConfigurationXML():void 0,m=o.getLanguage(),a.showTextly(o.getProgramName(),y,d,b,r.getSSID(),r.getPassword(),m,(function(e){r.reloadProgram(e),"ok"==e.rc?(t&&i("#tabTextly").tabWrapShow(),o.setState(e),l.setTextlyEditorCode(e.programAsTextly)):n.displayInformation(e,e.message,e.message,e.parameters)})))}),"after show source code aceEditorController"),i("#tabProgram").onWrap("click",(function(t){d(t,"#tabProgram")}),"back to previous view"),i("#tabConfiguration").onWrap("click",(function(t){d(t,"#tabConfiguration")}),"back to previous view"),i("#tabTextly").onWrap("hide.bs.tab",(function(){i("#aceTextlyEditor").css("visibility","hidden"),l.setTextlyEditable(!1),i("#textlyEditorPane").hide()}),"hide tabTextly"),i("#tabNN").onWrap("shown.bs.tab",(function(t){l.setTextlyVisibility(!1,"visibility"),d(t,"#tabNN")}),"show tabNN"),i("#tabNNlearn").onWrap("shown.bs.tab",(function(t){l.setTextlyVisibility(!1,"visibility"),i("#nn-learn").show(),d(t,"#tabNN"),i("#main-section").resize()}),"show tabNNLearn")}}));
//# sourceMappingURL=textly.controller.js.map
//# sourceMappingURL=textly.controller.js.map
