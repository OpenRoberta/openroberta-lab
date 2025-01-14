var __spreadArray=this&&this.__spreadArray||function(t,i,e){if(e||2===arguments.length)for(var s,o=0,r=i.length;o<r;o++)!s&&o in i||(s||(s=Array.prototype.slice.call(i,0,o)),s[o]=i[o]);return t.concat(s||Array.prototype.slice.call(i))};define(["require","exports","util.roberta","jquery","simulation.objects","simulation.roberta","robot.base","robot.base.mobile"],(function(t,i,e,s,o,r,a,n){Object.defineProperty(i,"__esModule",{value:!0}),i.SimulationScene=i.RcjScoringTool=void 0;var c=function(){function t(t,i,e){this.MAX_TIME=8,this.POINTS_OBSTACLE=15,this.POINTS_GAP=10,this.POINTS_INTERSECTION=10,this.POINTS_VICTIM_MULTI=1.4,this.POINTS_DEADONLY_VICTIM_MULTI=1.2,this.POINTS_LINE=[5,3,1,0],this.running=!1,this.mins=0,this.secs=0,this.csecs=0,this.path=0,this.lastPath=0,this.lastCheckPoint={},this.line=!0,this.prevCheckPointTile={},this.programPaused=!0,this.victimsLocated=0,this.linePoints=1,this.obstaclePoints=0,this.totalScore=0,this.avoidanceGoalIndex=null,this.wasOnLineOnce=!1,this.configData=i,this.robot=t,this.resetObstaclesCallback=e,this.init();var o=this;s("#rcjStartStop").off().on("click",(function(){return s(this).text().indexOf("Start")>=0?(s(this).html("Stop<br>Scoring Run"),o.init(),o.resetObstaclesCallback(),s("#rcjStartStop").addClass("running"),!1):(s(this).html("Start<br>Scoring Run"),clearInterval(o.stopWatch),s("#rcjStartStop").removeClass("running"),s("#rcjLoP").addClass("disabled"),s("#rcjNextCP").addClass("disabled"),o.robot&&o.robot.interpreter.terminate(),o.programPaused=!0,!1)})),s("#rcjLoP").off().on("click",(function(t){o.robot.interpreter.terminate(),o.programPaused=!0,s("#rcjLoP").addClass("disabled"),s("#rcjNextCP").addClass("disabled");var e=r.default.getTilePose(o.lastCheckPoint,i.tiles[o.lastCheckPoint.next],o.prevCheckPointTile);return o.robot.pose=new n.Pose(e.x,e.y,e.theta),o.robot.initialPose=new n.Pose(e.x,e.y,e.theta),o.path=o.lastCheckPoint.index[0],o.lastPath=o.path,o.loPCounter+=1,o.loPSum+=1,o.nextCheckPoint&&o.loPCounter>=3&&s("#rcjNextCP").removeClass("disabled"),!1})),s("#rcjNextCP").off().on("click",(function(t){if(o.robot.interpreter.terminate(),o.programPaused=!0,s("#rcjLoP").addClass("disabled"),s("#rcjNextCP").addClass("disabled"),o.nextCheckPoint){var e=r.default.getTilePose(o.nextCheckPoint,i.tiles[o.nextCheckPoint.next],o.prevNextCheckPoint);o.robot.pose=new n.Pose(e.x,e.y,e.theta),o.robot.initialPose=new n.Pose(e.x,e.y,e.theta),o.path=o.nextCheckPoint.index[0],o.lastPath=o.path,o.loPCounter=0,o.section+=1,o.lastCheckPoint=o.nextCheckPoint,o.setNextCheckPoint()}})),s("#rcjName").text(i.name),s("#rcjTeam").text(t.interpreter.name),s("#rcjTime").text("00:00:0")}return t.prototype.init=function(){this.path=0,this.lastPath=0,this.line=!0,clearInterval(this.stopWatch),this.mins=0,this.secs=0,this.csecs=0,this.running=!0,this.stopWatch=setInterval(this.timer.bind(this),100);var t=this.configData.tiles[this.configData.startTile.x+","+this.configData.startTile.y+",0"];this.initialPose=r.default.getTilePose(t,this.configData.tiles[t.next],null),this.lastTile=t,this.lastCheckPoint=t,this.robot&&(this.robot.initialPose=this.initialPose,this.robot.resetPose()),this.loPCounter=0,this.loPSum=0,this.section=0,this.victimsLocated=0,this.linePoints=0,this.obstaclePoints=0,this.totalScore=0,this.inAvoidanceMode=!1,this.countedTileIndices=[0],this.lastCheckPointIndex=0,this.rescueMulti=1},t.prototype.timer=function(){this.running&&(this.csecs++,10===this.csecs&&(this.secs++,this.csecs=0),60===this.secs&&(this.mins++,this.secs=0),s("#rcjTime").text(("00"+this.mins).slice(-2)+":"+("00"+this.secs).slice(-2)+":"+this.csecs),s("#rcjPath").text(-1===this.path?"wrong":"correct"),s("#rcjLastPath").text(this.lastPath),s("#rcjSection").text(this.section),s("#rcjLoPpS").text(this.loPCounter),s("#rcjLoPCount").text(this.loPSum),s("#rcjLine").text(this.line?"yes":"no"),this.mins>=this.MAX_TIME&&s("#rcjStartStop").trigger("click"),s("#rcjRescueMulti").text(Math.round(100*this.rescueMulti)/100),s("#rcjLinePoints").text(this.linePoints),s("#rcjObstaclePoints").text(this.obstaclePoints),s("#rcjTotalScore").text(this.totalScore))},t.prototype.countObstaclePoints=function(t){t&&!this.countedTileIndices.includes(t.index[0])&&(t.tileType.gaps>0&&(this.obstaclePoints+=this.POINTS_GAP),t.tileType.intersections>0&&(this.obstaclePoints+=this.POINTS_INTERSECTION),this.countedTileIndices.push(t.index[0]))},t.prototype.callAutoLoP=function(){s("#rcjLoP").trigger("click")},t.prototype.update=function(t){if(this.running)if(t instanceof n.RobotBaseMobile){var i=t;this.robot!=i&&(this.robot=i,this.initialPose=this.robot.initialPose),this.programPaused&&(this.programPaused=!1,s("#rcjLoP").removeClass("disabled"),s("#rcjNextCP").addClass("disabled")),this.pose=i.pose;var r=Math.floor((this.pose.x-10)/90),a=Math.floor((this.pose.y-10)/90),c=this.configData.tiles[r+","+a+",0"],h=c&&c.index[0];if(h==this.lastPath||h==this.lastPath+1){if(this.path=h,this.lastPath=h,this.line=i.F.lightValue<70,this.line&&(this.wasOnLineOnce=!0),c&&c.checkPoint||0==h){if(this.lastCheckPoint!=c){var l=this.loPCounter<this.POINTS_LINE.length?this.loPCounter:this.POINTS_LINE.length-1;this.linePoints+=(c.index[0]-this.lastCheckPointIndex)*this.POINTS_LINE[l],this.loPCounter=0,this.section+=1,this.lastCheckPoint=c,this.lastCheckPointIndex=c.index[0],this.setNextCheckPoint()}}else this.prevCheckPointTile=c;this.inAvoidanceMode&&this.line&&c.index[0]==this.avoidanceGoalIndex&&(this.countedTileIndices.includes(c.index[0])||(this.obstaclePoints+=this.POINTS_OBSTACLE,this.countedTileIndices.push(this.avoidanceGoalIndex-1)),this.avoidanceGoalIndex=null,this.inAvoidanceMode=!1),c&&c!==this.lastTile&&(this.wasOnLineOnce||this.inAvoidanceMode?this.countObstaclePoints(this.lastTile):this.callAutoLoP(),this.lastTile=c,this.wasOnLineOnce=!1)}else this.inAvoidanceMode||(this.lastTile.next.length>0&&1===this.configData.tiles[this.lastTile.next].items.obstacles?(this.inAvoidanceMode=!0,this.path+=1,this.lastPath=this.path,this.avoidanceGoalIndex=this.lastTile.index[0]+2):this.lastTile.items.obstacles>0?(this.inAvoidanceMode=!0,this.avoidanceGoalIndex=this.lastTile.index[0]+1):(-1!=this.path&&h&&this.callAutoLoP(),this.path=-1)),this.line=!1;this.totalScore=(this.linePoints+this.obstaclePoints)*this.rescueMulti,this.totalScore=e.round(this.totalScore,2)}else if(t instanceof o.CircleSimulationObject){var d=t;d.inEvacuationZone&&"#33B8CA"===d.color&&(d.selected=!0,s("#simDeleteObject").trigger("click"),this.rescueMulti*=this.POINTS_VICTIM_MULTI,this.victimsLocated+=1),d.inEvacuationZone&&"#000000"===d.color&&(d.selected=!0,s("#simDeleteObject").trigger("click"),this.victimsLocated>1?this.rescueMulti*=this.POINTS_VICTIM_MULTI:this.rescueMulti*=this.POINTS_DEADONLY_VICTIM_MULTI,this.victimsLocated+=1),this.victimsLocated>=3&&s("#rcjStartStop").trigger("click")}},t.prototype.setNextCheckPoint=function(){for(var t=this.configData.tiles[this.lastCheckPoint.next];t&&this.configData.tiles[t.next]&&(this.prevNextCheckPoint=t,!(t=this.configData.tiles[t.next]).checkPoint););t&&t.checkPoint?this.nextCheckPoint=t:(this.nextCheckPoint=null,this.prevNextCheckPoint=null)},t.prototype.openClose=function(){var t=s("#simDiv").position();t.left=12,s("#rcjScoringWindow").toggleSimPopup(t)},t.prototype.destroy=function(){s("#rcjStartStop").html("Start<br>Scoring Run"),s("#rcjStartStop").removeClass("running"),s("#rcjLoP").addClass("disabled"),s("#rcjNextCP").addClass("disabled"),clearInterval(this.stopWatch),this.stopWatch=null,s("#rcjPath").text(""),s("#rcjLastPath").text(""),s("#rcjSection").text(""),s("#rcjLoPpS").text(""),s("#rcjLoPCount").text(""),s("#rcjLine").text(""),s("#rcjName").text(""),s("#rcjTeam").text(""),s("#rcjTime").text("00:00:0"),s("#rcjRescueMulti").text("")},t}();i.RcjScoringTool=c;var h=function(){function t(t){this.DEFAULT_TRAIL_WIDTH=10,this.DEFAULT_TRAIL_COLOR="#000000",this.customBackgroundLoaded=!1,this.ground=new o.Ground(0,0,0,0),this.imgBackgroundList=[],this.imgPath="/css/img/simBackgrounds/",this.playground={x:0,y:0,w:0,h:0},this._colorAreaList=[],this._obstacleList=[],this._rcjList=[],this._markerList=[],this._redrawColorAreas=!1,this._redrawObstacles=!1,this._redrawMarkers=!1,this._robots=[],this._uniqueObjectId=0,this._scoring=!1,this.sim=t,this.uCanvas=document.createElement("canvas"),this.uCtx=this.uCanvas.getContext("2d",{willReadFrequently:!0}),this.udCanvas=document.createElement("canvas"),this.udCtx=this.udCanvas.getContext("2d",{willReadFrequently:!0}),this.bCtx=s("#backgroundLayer")[0].getContext("2d"),this.dCtx=s("#drawLayer")[0].getContext("2d"),this.aCtx=s("#arucoMarkerLayer")[0].getContext("2d"),this.oCtx=s("#objectLayer")[0].getContext("2d"),this.rCtx=s("#robotLayer")[0].getContext("2d"),this.rcjCtx=s("#rcjLayer")[0].getContext("2d")}return Object.defineProperty(t.prototype,"scoring",{get:function(){return this._scoring},set:function(t){this._scoring=t},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"uniqueObjectId",{get:function(){return++this._uniqueObjectId},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"robots",{get:function(){return this._robots},set:function(t){this.clearList(this._robots),this._robots=t},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"obstacleList",{get:function(){return this._obstacleList},set:function(t){this.clearList(this._obstacleList),this._obstacleList=t,this.redrawObstacles=!0},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"rcjList",{get:function(){return this._rcjList},set:function(t){this.clearList(this._rcjList),this._rcjList=t,this.redrawObstacles=!0},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"colorAreaList",{get:function(){return this._colorAreaList},set:function(t){this.clearList(this._colorAreaList),this._colorAreaList=t,this.redrawColorAreas=!0},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"markerList",{get:function(){return this._markerList},set:function(t){this.clearList(this._markerList),this._markerList=t,this.redrawMarkers=!0},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"redrawObstacles",{get:function(){return this._redrawObstacles},set:function(t){this._redrawObstacles=t},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"redrawColorAreas",{get:function(){return this._redrawColorAreas},set:function(t){this._redrawColorAreas=t},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"redrawMarkers",{get:function(){return this._redrawMarkers},set:function(t){this._redrawMarkers=t},enumerable:!1,configurable:!0}),t.prototype.addColorArea=function(t){this.addSimulationObject(this.colorAreaList,t,o.SimObjectType.ColorArea),this.redrawColorAreas=!0},t.prototype.addImportColorAreaList=function(t){var i=this,e=[];t.forEach((function(t){var s=o.SimObjectFactory.getSimObject.apply(o.SimObjectFactory,__spreadArray([t.id,i,i.sim.selectionListener,t.shape,o.SimObjectType.ColorArea,t.p,null,t.color],t.params,!1));e.push(s)})),this.colorAreaList=e},t.prototype.addImportObstacle=function(t){var i=this,e=[];t.forEach((function(t){var s=o.SimObjectFactory.getSimObject.apply(o.SimObjectFactory,__spreadArray([t.id,i,i.sim.selectionListener,t.shape,o.SimObjectType.Obstacle,t.p,null,t.color],t.params,!1));e.push(s)})),this.obstacleList=e},t.prototype.addSomeObstacles=function(t){var i=this,e=this;t.forEach((function(t){var s=o.SimObjectFactory.getSimObject.apply(o.SimObjectFactory,__spreadArray([t.id,i,i.sim.selectionListener,t.shape,o.SimObjectType.Obstacle,t.p,null,t.color],t.params,!1));e.obstacleList.push(s)}))},t.prototype.addImportRcjLabel=function(t){var i=this,e=[];t.forEach((function(t){var s=new o.RcjSimulationLabel(i.uniqueObjectId,i,i.sim.selectionListener,o.SimObjectType.ColorArea,t.x,t.y,t.checkPoint?"checkPoint":t.start?"start":null,t.index[0]);e.push(s)})),this.rcjList=e},t.prototype.addImportMarkerList=function(t){var i=this,e=[];t.forEach((function(t){var s=o.SimObjectFactory.getSimObject.apply(o.SimObjectFactory,__spreadArray([t.id,i,i.sim.selectionListener,t.shape,o.SimObjectType.Marker,t.p,null,t.color],t.params,!1));s.markerId=t.markerId,e.push(s)})),this.markerList=e},t.prototype.addObstacle=function(t){this.addSimulationObject(this.obstacleList,t,o.SimObjectType.Obstacle),this.redrawObstacles=!0},t.prototype.addSimulationObject=function(t,i,e,r){var a=s("#robotLayer");a.attr("tabindex",0),a.trigger("focus");var n=Math.random()*(this.ground.w-300)+100,c=Math.random()*(this.ground.h-200)+100,h=o.SimObjectFactory.getSimObject(this.uniqueObjectId,this,this.sim.selectionListener,i,e,{x:n,y:c},this.backgroundImg.width);i==o.SimObjectShape.Marker&&r&&(h.markerId=r),t.push(h),h.selected=!0},t.prototype.changeColorWithColorPicker=function(t){var i=this.obstacleList.concat(this.colorAreaList).filter((function(t){return t.selected}));1==i.length&&(i[0].color=t,i[0].type===o.SimObjectType.Obstacle?this.redrawObstacles=!0:this.redrawColorAreas=!0)},t.prototype.clearList=function(t){t.forEach((function(t){t.destroy()})),t.length=0},t.prototype.deleteSelectedObject=function(){var t=this;function i(i){for(var e=0;e<i.length;e++)if(i[e].selected)return i[e].destroy(),i.splice(e,1),t.redrawObstacles=!0,!0;return!1}i(this.obstacleList)?this.redrawObstacles=!0:i(this.colorAreaList)?this.redrawColorAreas=!0:i(this.markerList)&&(this.redrawMarkers=!0)},t.prototype.draw=function(t,i){var e=this;this.rCtx.save(),this.rCtx.scale(this.sim.scale,this.sim.scale),this.rCtx.clearRect(this.ground.x-10,this.ground.y-10,this.ground.w+20,this.ground.h+20),this.dCtx.save(),this.dCtx.scale(this.sim.scale,this.sim.scale),this.robots.forEach((function(s){s.draw(e.rCtx,t),s instanceof n.RobotBaseMobile&&i&&(e.backgroundImg.src.indexOf("math")<0?s.drawTrail(e.dCtx,e.udCtx,e.DEFAULT_TRAIL_WIDTH,e.DEFAULT_TRAIL_COLOR):s.drawTrail(e.dCtx,e.udCtx,1,"#ffffff"))})),this.redrawColorAreas&&(this.drawColorAreas(),this.redrawColorAreas=!1),this.redrawObstacles&&(this.drawObstacles(),this.redrawObstacles=!1),this.redrawMarkers&&(this.drawMarkers(),this.redrawMarkers=!1),this.rCtx.restore(),this.dCtx.restore()},t.prototype.drawColorAreas=function(){var t=this,i=this.backgroundImg.width+20,e=this.backgroundImg.height+20;this.uCtx.clearRect(0,0,i,e),this.uCtx.drawImage(this.backgroundImg,10,10,this.backgroundImg.width,this.backgroundImg.height),this.drawPattern(this.uCtx,!1),this.bCtx.restore(),this.bCtx.save(),this.bCtx.drawImage(this.backgroundImg,10*this.sim.scale,10*this.sim.scale,this.backgroundImg.width*this.sim.scale,this.backgroundImg.height*this.sim.scale),this.drawPattern(this.bCtx,!0),this.bCtx.scale(this.sim.scale,this.sim.scale),this.colorAreaList.forEach((function(i){return i.draw(t.bCtx,t.uCtx)}))},t.prototype.drawObstacles=function(){var t=this;this.oCtx.restore(),this.oCtx.save(),this.oCtx.scale(this.sim.scale,this.sim.scale),this.oCtx.clearRect(this.ground.x-10,this.ground.y-10,this.ground.w+20,this.ground.h+20),this.obstacleList.forEach((function(i){return i.draw(t.oCtx,t.uCtx)}))},t.prototype.drawRcjLabel=function(){var t=this;this.rcjCtx.restore(),this.rcjCtx.save(),this.rcjCtx.scale(this.sim.scale,this.sim.scale),this.rcjCtx.clearRect(this.ground.x-10,this.ground.y-10,this.ground.w+20,this.ground.h+20),this.rcjList.forEach((function(i){return i.draw(t.rcjCtx,t.uCtx)}))},t.prototype.drawMarkers=function(){var t=this;this.aCtx.restore(),this.aCtx.save(),this.aCtx.scale(this.sim.scale,this.sim.scale),this.aCtx.clearRect(this.ground.x-10,this.ground.y-10,this.ground.w+20,this.ground.h+20),this.markerList.forEach((function(i){return i.draw(t.aCtx,t.uCtx)}))},t.prototype.drawPattern=function(t,i){if(this.images&&this.images.pattern){var e=10,s=1;i&&(e*=this.sim.scale,s=this.sim.scale),t.beginPath();var o=this.images.pattern;t.strokeStyle=t.createPattern(o,"repeat"),t.lineWidth=e,t.strokeRect(e/2,e/2,this.backgroundImg.width*s+e,this.backgroundImg.height*s+e)}},t.prototype.getRobotPoses=function(){return this.robots.map((function(t){return[t.pose,t.initialPose]}))},t.prototype.handleKeyEvent=function(t){"v"===t.key&&(t.ctrlKey||t.metaKey)&&(this.pasteObject(this.sim.lastMousePosition),t.stopImmediatePropagation()),"Delete"!==t.key&&"Backspace"!==t.key||(this.deleteSelectedObject(),t.stopImmediatePropagation())},t.prototype.init=function(t,i,r,n,c,h,l){var d=this,u=!this.robotType||this.robotType!=t;this.robotType=t;var g=this;i?(s("#canvasDiv").hide(),s("#simDiv>.pace").show(),this.robots=[],a.RobotFactory.createRobots(r,n,c,this.sim.selectionListener,this.robotType).then((function(t){if(d.robots=t.robots,d.robotClass=t.robotClass,d.setRobotPoses(h),d.initViews(),u){d.removeRcjScoringTool(),g.imgBackgroundList=[],g.currentBackground=0,g.obstacleList.length>0&&(g.obstacleList=[]),g.colorAreaList.length>0&&(g.colorAreaList=[]);var i=".svg";e.isIE()&&(i=".png"),g.loadBackgroundImages((function(){g.robots[0].mobile?(s(".simMobile").show(),g.images=g.loadImages(["roadWorks","pattern"],["roadWorks"+i,"wallPattern.png"],(function(){g.ground=new o.Ground(10,10,g.imgBackgroundList[g.currentBackground].width,g.imgBackgroundList[g.currentBackground].height),g.backgroundImg=g.imgBackgroundList[0];var t=new o.RectangleSimulationObject(0,g,g.sim.selectionListener,o.SimObjectType.Obstacle,{x:7*g.backgroundImg.width/9,y:g.backgroundImg.height-2*g.backgroundImg.width/9},g.backgroundImg.width);g.obstacleList.push(t),g.centerBackground(!0),g.initEvents(),g.sim.initColorPicker(a.RobotBase.colorRange),g.showFullyLoadedSim(l),g.sim.start()}))):(s(".simMobile").hide(),g.images={},g.ground=new o.Ground(10,10,g.imgBackgroundList[g.currentBackground].width,g.imgBackgroundList[g.currentBackground].height),g.backgroundImg=g.imgBackgroundList[0],g.centerBackground(!0),g.initEvents(),g.showFullyLoadedSim(l),g.sim.start())}))}d.showFullyLoadedSim(l),d.sim.start()}))):(this.robots.forEach((function(t,i){g.rcjScoringTool&&t.addObserver(g.rcjScoringTool),t.replaceState(r[i]),t.reset()})),this.showFullyLoadedSim(l)),this.robots.forEach((function(t,i){t.time=0}))},t.prototype.showFullyLoadedSim=function(t){this.obstacleList.forEach((function(t){t.removeMouseEvents(),t.addMouseEvents()})),this.markerList.forEach((function(t){t.removeMouseEvents(),t.addMouseEvents()})),this.colorAreaList.forEach((function(t){t.removeMouseEvents(),t.addMouseEvents()})),s("#canvasDiv").fadeIn("slow"),s("#simDiv>.pace").fadeOut("fast"),"function"==typeof t&&t()},t.prototype.initViews=function(){var t=s("#systemValuesView"),i=s("#robotIndex");t.html("");var e="",o=this.robots[0]instanceof n.RobotBaseMobile?this.robots[0].chassis.geom.color:"#ffffff";if(e+='<select id="robotIndex" style="background-color:'+o+'">',this.robots.forEach((function(t){var i=t instanceof n.RobotBaseMobile?t.chassis.geom.color:"#ffffff";e+='<option style="background-color:'+i+'" value="'+t.id+'">'+t.name+"</option>"})),e+="</select>",t.append('<div><label id="robotLabel">Program Name</label><span style="width:auto">'+e+"</span></div>"),i.off("change.sim"),this.robots.length>1){var r=this;i.on("change.sim",(function(){var t=Number(s(this).val());r.robots[t].selected=!0,r.sim.selectionListener.fire(null)}))}},t.prototype.initEvents=function(){var t=this,i=0;s(window).off("resize.sim").on("resize.sim",(function(e,s){i>3||"loaded"==s?(t.centerBackground(!1),i=0):i++})),s("#robotLayer").off("keydown.sim").on("keydown.sim",this.handleKeyEvent.bind(this))},t.prototype.loadBackgroundImages=function(t){var i,s;s=e.isIE()?".png":".svg";for(var o=(i=this.robots[0].mobile?this.robots[0].imgList.map((function(t){return t.endsWith("jpg")?t:"".concat(t).concat(s)})):[this.robotType+"Background"+s]).length,r=this,a=function(){if(0==--o&&(t(),e.isLocalStorageAvailable()&&r.robots[0].mobile)){var i=localStorage.getItem("customBackground");if(i){try{JSON.parse(i)}catch(t){localStorage.setItem("customBackground",JSON.stringify({image:i,timestamp:(new Date).getTime()})),i=localStorage.getItem("customBackground")}var s=JSON.parse(i);if((new Date).getTime()-s.timestamp>54432e5)localStorage.removeItem("customBackground");else{var a=s.image,n=new Image;n.src="data:image/png;base64,"+a,r.imgBackgroundList.push(n),r.customBackgroundLoaded=!0}}}},n=0;n<i.length;){var c=this.imgBackgroundList[n]=new Image;c.onload=a,c.onerror=function(t){console.error(t)},c.src=this.imgPath+i[n++]}},t.prototype.loadImages=function(t,i,e){for(var s=0,o=t.length,r=function(){0==--o&&e()},a={};s<t.length;){var n=a[t[s]]=new Image;n.onload=r,n.onerror=function(t){console.error(t)},n.src=this.imgPath+i[s++]}return a},t.prototype.pasteObject=function(t){if(this.objectToCopy){var i=o.SimObjectFactory.copy(this.objectToCopy);i.moveTo(t),this.objectToCopy.type===o.SimObjectType.Obstacle?(this.obstacleList.push(i),this.redrawObstacles=!0):this.objectToCopy.type===o.SimObjectType.ColorArea?(this.colorAreaList.push(i),this.redrawColorAreas=!0):this.objectToCopy.type===o.SimObjectType.Marker&&(this.markerList.push(i),this.redrawMarkers=!0)}},t.prototype.resetAllCanvas=function(t){var i=this.sim.scale,e=(this.playground.w-(this.backgroundImg.width+20)*i)/2+25,o=(this.playground.h-(this.backgroundImg.height+20)*i)/2,r=Math.round((this.backgroundImg.width+20)*i),a=Math.round((this.backgroundImg.height+20)*i),n=s("#simDiv"),c=s("#canvasDiv");n.hasClass("shifting")&&n.hasClass("rightActive")&&c.css({top:o+"px",left:e+"px"}),this.oCtx.canvas.width=r,this.oCtx.canvas.height=a,this.rcjCtx.canvas.width=r,this.rcjCtx.canvas.height=a,this.rCtx.canvas.width=r,this.rCtx.canvas.height=a,this.dCtx.canvas.width=r,this.dCtx.canvas.height=a,this.bCtx.canvas.width=r,this.bCtx.canvas.height=a,this.aCtx.canvas.width=r,this.aCtx.canvas.height=a,t&&(this.uCanvas.width=this.backgroundImg.width+20,this.uCanvas.height=this.backgroundImg.height+20,this.udCanvas.width=this.backgroundImg.width+20,this.udCanvas.height=this.backgroundImg.height+20,this.uCtx.drawImage(this.backgroundImg,10,10,this.backgroundImg.width,this.backgroundImg.height),this.drawPattern(this.uCtx,!1)),this.bCtx.restore(),this.bCtx.save(),this.bCtx.drawImage(this.backgroundImg,10*i,10*i,this.backgroundImg.width*i,this.backgroundImg.height*i),this.drawPattern(this.bCtx,!0),this.dCtx.restore(),this.dCtx.save(),this.dCtx.drawImage(this.udCanvas,0,0,this.backgroundImg.width+20,this.backgroundImg.height+20,0,0,r,a),this.drawColorAreas(),this.drawObstacles(),this.drawMarkers(),this.drawRcjLabel()},t.prototype.centerBackground=function(t){var i=s("#simDiv"),e=s("#canvasDiv"),o=i.offset().top;this.playground.w=i.outerWidth()-50,this.playground.h=s(window).height()-o;var r=this.playground.w/(this.backgroundImg.width+20),a=this.playground.h/(this.backgroundImg.height+20);this.sim.scale=Math.min(r,a);var n=(this.playground.w-(this.backgroundImg.width+20)*this.sim.scale)/2+25,c=(this.playground.h-(this.backgroundImg.height+20)*this.sim.scale)/2;e.css({top:c+"px",left:n+"px"}),this.resetAllCanvas(t)},t.prototype.setRobotPoses=function(t){var i=this;t.forEach((function(t,e){i.robots[e]&&(i.robots[e].pose=new n.Pose(t[0].x,t[0].y,t[0].theta),i.robots[e].initialPose=new n.Pose(t[1].x,t[1].y,t[1].theta))}))},t.prototype.stepBackground=function(t){var i=2==this.currentBackground&&this.imgBackgroundList[2].currentSrc.includes("robertaBackground");i&&((e=this.obstacleList.find((function(t){return 0===t.myId})))&&(e.img=null));t<0?(this.currentBackground++,this.currentBackground%=this.imgBackgroundList.length):this.currentBackground=t,i=2==this.currentBackground&&this.imgBackgroundList[2].currentSrc.includes("robertaBackground");var e,r="std"===this.sim.configType?this.sim.getConfigData():null;(this.obstacleList=[],this.colorAreaList=[],this.markerList=[],this.rcjList=[],this.ground.w=this.imgBackgroundList[this.currentBackground].width,this.ground.h=this.imgBackgroundList[this.currentBackground].height,this.backgroundImg=this.imgBackgroundList[this.currentBackground],this.centerBackground(!0),"std"===this.sim.configType?this.sim.setNewConfig(r):(this.sim.configType="std",s("#rcjScoringWindow").fadeOut(),this.removeRcjScoringTool(),this.imgBackgroundList.pop()),i)&&((e=this.obstacleList.find((function(t){if(t.type===o.SimObjectType.Obstacle)return t.h=100,t.w=100,!0})))&&(e.img=this.images.roadWorks))},t.prototype.update=function(t,i){var e=this,s=this.obstacleList.slice();this.robots.forEach((function(t){return s.push(t.chassis)})),s.push(this.ground),this.robots.forEach((function(e){return e.updateActions(e,t,i)})),i&&this.obstacleList.forEach((function(t){var i=t;i.updateAction&&i.updateAction()})),this.robots.forEach((function(o){var r=s.slice(),a=[];for(o.updateSensors(i,t,e.uCtx,e.udCtx,r,e.markerList,a);a.length>0;){a[0].updateSensor(e.uCtx,r,a),a.shift()}})),this.draw(t,i)},t.prototype.toggleTrail=function(){this.robots.forEach((function(t){t.hasTrail=!t.hasTrail,t.pose.xOld=t.pose.x,t.pose.yOld=t.pose.y}))},t.prototype.resetPoseAndDrawings=function(){this.robots.forEach((function(t){return t.resetPose()})),this.dCtx.canvas.width=this.dCtx.canvas.width,this.udCtx.canvas.width=this.udCtx.canvas.width,this.rcjCtx.canvas.width=this.rcjCtx.canvas.width},t.prototype.addMarker=function(t){this.addSimulationObject(this.markerList,o.SimObjectShape.Marker,o.SimObjectType.Marker,t),this._redrawMarkers=!0},t.prototype.setRcjScoringTool=function(t,i,e){this.rcjScoringTool=new c(t,i,e),this.scoring=!0;var o=this;s("#simCompetition").show(),s("#simCompetition").off(),s("#simCompetition").onWrap("click",(function(){o.rcjScoringTool.openClose()})),this.obstacleList.forEach((function(t){t.addObserver&&"function"==typeof t.addObserver&&t.addObserver(o.rcjScoringTool)}))},t.prototype.removeRcjScoringTool=function(){this.rcjScoringTool&&this.rcjScoringTool.destroy(),this.rcjScoringTool=null,this.scoring=!1,s("#simCompetition").hide(),s("#simCompetition").off()},t}();i.SimulationScene=h}));
//# sourceMappingURL=simulation.scene.js.map
//# sourceMappingURL=simulation.scene.js.map
