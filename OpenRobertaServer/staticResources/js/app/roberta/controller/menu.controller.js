define(["require","exports","message","comm","wrap","robot.controller","user.controller","notification.controller","guiState.controller","program.controller","progRun.controller","configuration.controller","import.controller","tour.controller","sourceCodeEditor.controller","jquery","blockly","progTutorial.controller","util.roberta","connection.controller","slick"],(function(e,o,t,a,n,r,i,c,l,s,u,d,p,g,h,m,b,f,k,w){Object.defineProperty(o,"__esModule",{value:!0}),o.init=void 0;var v,P=0,S="?",y="&",C="=",W="forgotPassword",L="activateAccount",R="loadSystem",O="tutorial",T="gallery",I="tour",_="kiosk",B="exampleView",M="loadProgram";function x(e){var o,t=decodeURIComponent(document.location.toString()),a=t.indexOf("?"),n=t=t.substring(a+1),r=n.indexOf("<");r>=0&&(n=t.substring(0,r),o=t.substring(r));var i,c,l=n.split(y);for(c=0;c<l.length;c++)if((i=l[c].split(C))[0]===e)return void 0===i[1]||("loadProgram"===i[0]?o:i[1])}function N(){null!=w.getConnectionInstance()&&w.getConnectionInstance().terminate(),m(".fromRight.rightActive").length>0?m("#blocklyDiv").closeRightView((function(){return k.closeSimRobotWindow(),m("#tabStart").tabWrapShow()})):m("#tabStart").tabWrapShow()}o.init=function(e){e&&e instanceof Function?(v=e,m("#startupVersion").text(l.getServerVersion()),function(){m(".navbar-collapse a:not(.dropdown-toggle)").click((function(){m(".dropdown-menu.show").collapse("hide"),m(".navbar-collapse.show").collapse("hide")})),navigator.userAgent.match(/iPad/i)||navigator.userAgent.match(/iPhone/i)||(m('[rel="tooltip"]').not(".rightMenuButton").tooltip({container:"body",placement:"right",trigger:"hover"}),m('[rel="tooltip"].rightMenuButton').tooltip({container:"body",placement:"left",trigger:"hover"}));if(document.addEventListener("gesturestart",(function(e){e.preventDefault(),e.stopPropagation()})),m(".blocklyButtonBack, .blocklyWidgetDiv, #head-navigation, #main-section, #tutorial-navigation").on("mousedown touchstart keydown",(function(e){if(m(e.target).not(".blocklyTreeLabel, .blocklytreerow, .toolboxicon, .goog-palette-colorswatch, .goog-menu-vertical, .goog-menuitem-checkbox, div.goog-menuitem-content, div.goog-menuitem, img").length>0){if(m(e.target).filter(".blocklyHtmlInput").length>0&&!e.metaKey)return;b&&b.getMainWorkspace()&&b.hideChaff()}})),m(".modal").onWrap("shown.bs.modal",(function(){m(this).find("[autofocus]").focus()})),m(".navbar-collapse").on("click",".dropdown-menu a,.visible-xs",(function(e){m("#navbarCollapse").collapse("hide")})),m("#head-navigation-gallery").on("click","a,.visible-xs",(function(e){m("#navbarCollapse").collapse("hide")})),l.isPublicServerVersion()){var e='<div href="#" id="feedbackButton" class="rightMenuButton" rel="tooltip" data-bs-original-title="" title=""><span id="" class="feedbackButton typcn typcn-feedback"></span></div>';m("#rightMenuDiv").append(e),window.onmessage=function(e){if("closeFeedback"===e.data)m("#feedbackIframe").oneWrap("load",(function(){setTimeout((function(){m("#feedbackIframe").attr("src","about:blank"),m("#feedbackModal").modal("hide")}),1e3)}));else if(e.data.indexOf("feedbackHeight")>=0){var o=e.data.split(":")[1]||400;m("#feedbackIframe").height(o)}},m("#feedbackButton").on("click","",(function(e){m("#feedbackModal").on("show.bs.modal",(function(){"de"===l.getLanguage().toLowerCase()?m("#feedbackIframe").attr("src","https://www.roberta-home.de/lab/feedback/"):m("#feedbackIframe").attr("src","https://www.roberta-home.de/en/lab/feedback/")})),m("#feedbackModal").modal("show")}))}m("#head-navigation-program-edit").on("click",".dropdown-menu li:not(.disabled) a",(function(e){var o=function(e){switch(e.target.id||e.currentTarget.id){case"menuRunProg":u.runOnBrick();break;case"menuRunSim":m("#simButton").clickWrap();break;case"menuCheckProg":s.checkProgram();break;case"menuNewProg":s.newProgram();break;case"menuListProg":m("#tabProgList").data("type","Programs"),m("#tabProgList").tabWrapShow();break;case"menuListExamples":m("#tabProgList").data("type","Examples"),m("#tabProgList").tabWrapShow();break;case"menuSaveProg":s.saveToServer();break;case"menuSaveAsProg":s.showSaveAsModal();break;case"menuShowCode":m("#codeButton").clickWrap();break;case"menuSourceCodeEditor":h.clickSourceCodeEditor();break;case"menuExportProg":s.exportXml();break;case"menuExportAllProgs":s.exportAllXml();break;case"menuLinkProg":s.linkProgram();break;case"menuToolboxBeginner":m('.levelTabs a[href="#beginner"]').tabWrapShow();break;case"menuToolboxExpert":m('.levelTabs a[href="#expert"]').tabWrapShow();break;case"menuDefaultFirmware":w.getConnectionInstance().reset2DefaultFirmware()}};n.wrapUI(o,"edit menu click")(e)})),m("#head-navigation-configuration-edit").onWrap("click",".dropdown-menu li:not(.disabled) a",(function(e){switch(m(".modal").modal("hide"),e.target.id){case"menuCheckConfig":t.displayMessage("MESSAGE_NOT_AVAILABLE","POPUP","");break;case"menuNewConfig":d.newConfiguration();break;case"menuListConfig":m("#tabConfList").tabWrapShow();break;case"menuSaveConfig":d.saveToServer();break;case"menuSaveAsConfig":d.showSaveAsModal()}}),"configuration edit clicked"),m("#head-navigation-robot").onWrap("click",".dropdown-menu li:not(.disabled) a",(function(e){m(".modal").modal("hide");var o=e.target.parentElement.dataset.type;if(o)r.switchRobot(o);else{var t=e.currentTarget.id;"menuConnect"===t?w.getConnectionInstance().onClickMenuConnect():"menuRobotInfo"===t?w.getConnectionInstance().onClickShowRobotInfo():"menuWlan"===t?w.getConnectionInstance().onClickShowWlanForm():"menuRobotSwitch"===t&&N()}}),"robot clicked"),m("#head-navigation-help").onWrap("click",".dropdown-menu li:not(.disabled) a",(function(e){m(".modal").modal("hide");var o=e.target.id;"menuShowStart"===o?N():"menuAbout"===o?(m("#version").text(l.getServerVersion()),m("#show-about").modal("show")):"menuLogging"===o&&m("#tabLogList").tabWrapShow()}),"help clicked"),m("#head-navigation-user").onWrap("click",".dropdown-menu li:not(.disabled) a",(function(e){switch(m(".modal").modal("hide"),e.target.id){case"menuUserGroupLogin":i.showUserGroupLoginForm();break;case"menuLogout":i.logout();break;case"menuGroupPanel":m("#tabUserGroupList").tabWrapShow();break;case"menuChangeUser":i.showUserDataForm();break;case"menuDeleteUser":i.showDeleteUserModal();break;case"menuStateInfo":i.showUserInfo();break;case"menuNotification":c.showNotificationModal()}return!1}),"user clicked"),m("#logoShowStart").onWrap("click",(function(){return N()})),m("#menuTabProgram").onWrap("click","",(function(e){m("#tabSimulation").hasClass("tabClicked")&&m(".scroller-left").clickWrap(),m(".scroller-left").clickWrap(),m("#tabProgram").tabWrapShow()}),"tabProgram clicked"),m("#head-navigation-gallery").onWrap("click",(function(e){return m("#tabGalleryList").tabWrapShow(),!1}),"gallery clicked"),m("#head-navigation-tutorial").onWrap("click",(function(e){return m("#tabTutorialList").tabWrapShow(),!1}),"tutorial clicked"),m("#menuTabConfiguration").onWrap("click","",(function(e){(m("#tabProgram").hasClass("tabClicked")||m("#tabConfiguration").hasClass("tabClicked"))&&m(".scroller-right").clickWrap(),m("#tabConfiguration").clickWrap()}),"tabConfiguration clicked"),m("#menuTabNN").onWrap("click","",(function(e){(m("#tabProgram").hasClass("tabClicked")||m("#tabConfiguration").hasClass("tabClicked")||m("#tabNN").hasClass("tabClicked"))&&m(".scroller-right").clickWrap(),m("#tabNN").clickWrap()}),"tabNN clicked"),m("#menuTabNNLearn").onWrap("click","",(function(e){(m("#tabProgram").hasClass("tabClicked")||m("#tabConfiguration").hasClass("tabClicked")||m("#tabNNlearn").hasClass("tabClicked"))&&m(".scroller-right").clickWrap(),m("#tabNNlearn").clickWrap()}),"tabNNlearn clicked"),m(".navbar-fixed-top").on("mouseleave",(function(e){m(".navbar-fixed-top .dropdown").removeClass("open")})),m(".menuGeneral").onWrap("click",(function(e){window.open("https://jira.iais.fraunhofer.de/wiki/display/ORInfo")}),"head navigation menu item general clicked"),m(".menuFaq").onWrap("click",(function(e){window.open("https://jira.iais.fraunhofer.de/wiki/display/ORInfo/FAQ")}),"head navigation menu item faq clicked"),m(".shortcut").onWrap("click",(function(e){window.open("https://jira.iais.fraunhofer.de/wiki/display/ORInfo/FAQ")}),"head navigation menu item faq (shortcut) clicked"),m(".menuAboutProject").onWrap("click",(function(e){"de"==l.getLanguage()?window.open("https://www.roberta-home.de/index.php?id=135"):window.open("https://www.roberta-home.de/index.php?id=135&L=1")}),"head navigation menu item about clicked"),m(".menuLogin").onWrap("click",(function(e){i.showLoginForm()}),"head navigation menu item login clicked"),m(".menuImportProg").onWrap("click",(function(e){p.importXml()})),m("#startPopupBack").on("click",(function(e){m("#popup-robot-main").removeClass("hidden",1e3),m(".popup-robot.robotSubGroup").addClass("hidden",1e3),m(".robotSpecial").removeClass("robotSpecial"),m("#startPopupBack").addClass("hidden"),m("#popup-robot-main").slick("refresh")}));var o=0,a=0;m(".popup-robot").on("mousedown",(function(e){o=e.clientX,a=e.clientY})),m(".popup-robot").onWrap("click",(function(e){if(!(Math.abs(e.clientX-o)>=3||Math.abs(e.clientY-a)>=3)){e.preventDefault(),m("#startPopupBack").clickWrap();var n=e.target.dataset.type||e.currentTarget.dataset.type,i=e.target.dataset.group||e.currentTarget.dataset.group;if(e.target.className.indexOf("info")>=0)window.open(l.getRobots()[n].info,"_blank");else{if(n){if(i)return m("#popup-robot-main").addClass("hidden"),m(".popup-robot."+n).removeClass("hidden"),m(".popup-robot."+n).addClass("robotSpecial"),void m("#startPopupBack").removeClass("hidden");if(m("#checkbox_id").is(":checked")){k.cleanUri();var c=window.location.toString();c+=S+R+C+n,window.history.replaceState({},document.title,c),m("#show-message").oneWrap("hidden.bs.modal",(function(e){e.preventDefault(),k.cleanUri(),r.switchRobot(n,!0)})),t.displayMessage("POPUP_CREATE_BOOKMARK","POPUP","")}else r.switchRobot(n,!0)}m("#show-startup-message").modal("hide")}}}),"robot choosen in start popup"),m("#moreReleases").onWrap("click",(function(e){m("#oldReleases").show({start:function(){m("#moreReleases").addClass("hidden")}})}),"show more releases clicked"),m("#goToWiki").onWrap("click",(function(e){e.preventDefault(),window.open("https://jira.iais.fraunhofer.de/wiki/display/ORInfo","_blank"),e.stopPropagation(),m("#show-startup-message").modal("show")}),"go to wiki clicked"),m(".cancelPopup").onWrap("click",(function(){m(".ui-dialog-titlebar-close").clickWrap()}),"cancel popup clicked"),m("#about-join").onWrap("click",(function(){m("#show-about").modal("hide")}),"hide show about clicked"),m(window).on("beforeunload",(function(e){return b.Msg.POPUP_BEFOREUNLOAD})),m("#navbarCollapse").on("shown.bs.collapse",(function(){var e=Math.min(m(this).height(),Math.max(m("#blocklyDiv").height(),m("#brickly").height(),m("#nn").height()));m(this).css("height",e)})),m(document).onWrap("keydown",(function(e){if("tabProgram"==l.getView()){if((e.metaKey||e.ctrlKey)&&50==e.which){e.preventDefault();var o=l.getBlocklyWorkspace().newBlock("robActions_debug");return o.initSvg(),o.render(),o.setInTask(!1),!1}if((e.metaKey||e.ctrlKey)&&51==e.which){e.preventDefault();var a=l.getBlocklyWorkspace().newBlock("robActions_assert");a.initSvg(),a.setInTask(!1),a.render();var n=l.getBlocklyWorkspace().newBlock("logic_compare");n.initSvg(),n.setMovable(!1),n.setInTask(!1),n.setDeletable(!1),n.render();var r=a.getInput("OUT").connection,i=n.outputConnection;return r.connect(i),!1}var c;return(e.metaKey||e.ctrlKey)&&52==e.which?(e.preventDefault(),(c=l.getBlocklyWorkspace().newBlock("robActions_eval_expr")).initSvg(),c.render(),c.setInTask(!1),!1):(e.metaKey||e.ctrlKey)&&53==e.which?(e.preventDefault(),(c=l.getBlocklyWorkspace().newBlock("robActions_eval_stmt")).initSvg(),c.render(),c.setInTask(!1),!1):((e.metaKey||e.ctrlKey)&&83==e.which&&(e.preventDefault(),l.isUserLoggedIn()?"NEPOprog"===l.getProgramName()||e.shiftKey?s.showSaveAsModal():l.isProgramSaved()||s.saveToServer():t.displayMessage("ORA_PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN","POPUP","")),(e.metaKey||e.ctrlKey)&&82==e.which&&(e.preventDefault(),l.isRunEnabled()&&u.runOnBrick()),(e.metaKey||e.ctrlKey)&&77==e.which&&(e.preventDefault(),l.isUserLoggedIn()?(m("#progList").trigger("Programs"),m('.navbar-nav a[href="#progList"]').tab("show")):t.displayMessage("ORA_PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN","POPUP","")),(e.metaKey||e.ctrlKey)&&73==e.which?(e.preventDefault(),p.importXml(),!1):(e.metaKey||e.ctrlKey)&&69==e.which?(e.preventDefault(),s.exportXml(),!1):void 0)}}))}(),function e(){setTimeout((function(){(P+=1e3)>=l.getPingTime()&&l.doPing()&&(a.ping((function(e){l.setState(e)})),P=0),e()}),1e3)}(),function(){var e=new URL(document.location),o=e.protocol+"//"+e.host,t=e.hash&&e.toString().indexOf("#")<=o.length+1||!1,a=""!==e.search||!1;if(t){var n=!0,r=void 0,c=decodeURI(document.location.hash).split("&&");"#overview"===c[0]?(l.setStartWithoutPopup(),v("ev3lejosv1",(function(){s.newProgram(!0),g.start("overview")})),r=o+S+"tour"+C+"overview"):"#loadProgram"===c[0]&&c.length>=4?(l.setStartWithoutPopup(),v&&v instanceof Function&&v(c[1],p.loadProgramFromXML,[c[2],c[3]]),r=o+S+"loadProgram"+C+"_vC353v-LPr_"):"#loadSystem"===c[0]&&c.length>=2?(l.setStartWithoutPopup(),v&&v instanceof Function&&v(c[1]),r=o+S+"loadSystem"+C+c[1]):"#gallery"===c[0]?(n=!1,r=o+S+"loadSystem"+C+"<ROBOT_SYSTEM>"+y+T):"#tutorial"===c[0]&&(n=!1,r=o+S+"loadSystem"+C+"<ROBOT_SYSTEM>"+y+O,c.length>1&&(r+=C+c[1],c.length>2&&(r+=y+c[2])));var u=void 0;"de"===l.getLanguage()&&n?u="Die eingegebenen URL-Parameter sind in dieser Form veraltet und werden bald nicht mehr unterstützt.\nBitte verwende ab sofort nur noch folgende Schreibweise:\n":"en"===l.getLanguage()&&n?u="The URL parameters entered are outdated in this form and will soon no longer be supported.\nPlease use only the following spelling from now on:":"de"!==l.getLanguage()||n?"en"!==l.getLanguage()||n||(u="The URL parameters entered are outdated in this form and are no longer supported.\nPlease use only the following spelling from now on:"):u="Die eingegebenen URL-Parameter sind in dieser Form veraltet und werden nicht mehr unterstützt.\nBitte verwende ab sofort nur noch folgende Schreibweise:\n",u+=r,alert(u)}if(a){var d=x(W);d&&i.showResetPassword(d);var h=x(L);h&&i.activateAccount(h);var b=x(I);b&&(l.setStartWithoutPopup(),v("ev3lejosv1",(function(){s.newProgram(!0),g.start(b)})));var k=x(R);if(k){l.setStartWithoutPopup();var w=void 0,P=[],N=x(O),A=x(M),E=x(B),U=x(T);if(N)if("true"===N||!0===N)w=function(){m('.navbar-nav a[href="#tutorialList"]').tab("show")};else{var D=x(_);!D||!0!==D&&"true"!==D||l.setKioskMode(!0),w=function(e){f.loadFromTutorial(e)},P.push(N)}else A?(w=p.loadProgramFromXML,P.push("NEPOprog"),P.push(A)):E?w=function(){m("#menuListExamples").clickWrap()}:U&&(w=function(){m("#tabGalleryList").tabWrapShow()});v&&v instanceof Function&&v(k,w,P)}}}(),k.cleanUri()):alert("Problem")}}));
//# sourceMappingURL=menu.controller.js.map
//# sourceMappingURL=menu.controller.js.map
