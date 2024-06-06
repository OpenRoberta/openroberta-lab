var __awaiter=this&&this.__awaiter||function(t,r,e,n){return new(e||(e=Promise))((function(o,i){function a(t){try{u(n.next(t))}catch(t){i(t)}}function c(t){try{u(n.throw(t))}catch(t){i(t)}}function u(t){var r;t.done?o(t.value):(r=t.value,r instanceof e?r:new e((function(t){t(r)}))).then(a,c)}u((n=n.apply(t,r||[])).next())}))},__generator=this&&this.__generator||function(t,r){var e,n,o,i,a={label:0,sent:function(){if(1&o[0])throw o[1];return o[1]},trys:[],ops:[]};return i={next:c(0),throw:c(1),return:c(2)},"function"==typeof Symbol&&(i[Symbol.iterator]=function(){return this}),i;function c(c){return function(u){return function(c){if(e)throw new TypeError("Generator is already executing.");for(;i&&(i=0,c[0]&&(a=0)),a;)try{if(e=1,n&&(o=2&c[0]?n.return:c[0]?n.throw||((o=n.return)&&o.call(n),0):n.next)&&!(o=o.call(n,c[1])).done)return o;switch(n=0,o&&(c=[2&c[0],o.value]),c[0]){case 0:case 1:o=c;break;case 4:return a.label++,{value:c[1],done:!1};case 5:a.label++,n=c[1],c=[0];continue;case 7:c=a.ops.pop(),a.trys.pop();continue;default:if(!(o=a.trys,(o=o.length>0&&o[o.length-1])||6!==c[0]&&2!==c[0])){a=0;continue}if(3===c[0]&&(!o||c[1]>o[0]&&c[1]<o[3])){a.label=c[1];break}if(6===c[0]&&a.label<o[1]){a.label=o[1],o=c;break}if(o&&a.label<o[2]){a.label=o[2],a.ops.push(c);break}o[2]&&a.ops.pop(),a.trys.pop();continue}c=r.call(t,a)}catch(t){c=[6,t],n=0}finally{e=o=0}if(5&c[0])throw c[1];return{value:c[0]?c[1]:void 0,done:!0}}([c,u])}}};define(["require","exports","microbit-universal-hex"],(function(t,r,e){var n=this;Object.defineProperty(r,"__esModule",{value:!0}),r.fetchMicroPython=r.microPythonConfig=void 0;var o={name:"MicroPython (micro:bit V2)",url:"https://github.com/microbit-foundation/micropython-microbit-v2/releases/download/v2.1.2/micropython-microbit-v2.1.2.hex",boardId:e.microbitBoardId.V2,version:"2.1.2",web:"https://github.com/microbit-foundation/micropython-microbit-v2/releases/tag/v2.1.2"};r.microPythonConfig={versions:[{name:"MicroPython (micro:bit V1)",url:"https://github.com/bbcmicrobit/micropython/releases/download/v1.1.1/micropython-microbit-v1.1.1.hex",boardId:e.microbitBoardId.V1,version:"1.1.1",web:"https://github.com/bbcmicrobit/micropython/releases/tag/v1.1.1"},o],stubs:"main"};var i=function(t){return __awaiter(n,void 0,void 0,(function(){var r;return __generator(this,(function(e){switch(e.label){case 0:return[4,fetch(t)];case 1:if(200!==(r=e.sent()).status)throw new Error("Unexpected status: ".concat(r.statusText," ").concat(r.status));return[2,r.text()]}}))}))};r.fetchMicroPython=function(){return __awaiter(n,void 0,void 0,(function(){var t=this;return __generator(this,(function(e){return[2,Promise.all(r.microPythonConfig.versions.map((function(r){var e=r.boardId,n=r.url;return __awaiter(t,void 0,void 0,(function(){var t;return __generator(this,(function(r){switch(r.label){case 0:return[4,i(n)];case 1:return t=r.sent(),[2,{boardId:e,hex:t}]}}))}))})))]}))}))}}));
//# sourceMappingURL=micropython.js.map
//# sourceMappingURL=micropython.js.map
