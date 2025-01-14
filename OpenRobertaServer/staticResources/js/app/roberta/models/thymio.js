var __extends=this&&this.__extends||function(){var e=function(t,n){return e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)Object.prototype.hasOwnProperty.call(t,n)&&(e[n]=t[n])},e(t,n)};return function(t,n){if("function"!=typeof n&&null!==n)throw new TypeError("Class extends value "+String(n)+" is not a constructor or null");function s(){this.constructor=t}e(t,n),t.prototype=null===n?Object.create(n):(s.prototype=n.prototype,new s)}}();define(["require","exports","flatbuffers","thymio_generated","@cor3ntin/flexbuffers-wasm","lodash.isequal","isomorphic-ws"],(function(e,t,n,s,o,a){Object.defineProperty(t,"__esModule",{value:!0}),t.createClient=t.Node=t.VMExecutionState=t.ProgrammingLanguage=t.NodeType=t.NodeStatus=t.EventDescription=t.AsebaVMDescription=t.NodeId=void 0;var r=function(){function e(e){this.ErrorType=s.mobsya.fb.ErrorType;var t=void 0,n=void 0;this._promise=new Promise((function(e,s){t=e,n=s})),this._then=t,this._onerror=n,this._request_id=e}return e.prototype._trigger_error=function(e){this._onerror(e)},e.prototype._trigger_then=function(){for(var e=[],t=0;t<arguments.length;t++)e[t]=arguments[t];return this._then.apply(this,e)},e}(),i=function(){this.message="Invalid Node Id",this.name="InvalidNodeIDException"},d=function(){function e(e){if(16!=e.length)throw new i;this._data=e}return Object.defineProperty(e.prototype,"data",{get:function(){return this._data},enumerable:!1,configurable:!0}),e.prototype.toString=function(){var e=[];return this._data.forEach((function(t){e.push(t.toString(16).padStart(2,"0")),[4,7,10,13].includes(e.length)&&e.push("-")})),"{"+e.join("")+"}"},e}();t.NodeId=d;var _=function(){this.bytecode_size=0,this.data_size=0,this.stack_size=0};t.AsebaVMDescription=_;var u=function(){this.fixed_size=0,this.index=-1};t.EventDescription=u;var b=function(){function e(e,t){this._monitoring_flags=0,this._client=e,this._id=t}return Object.defineProperty(e.prototype,"id",{get:function(){return this._id},enumerable:!1,configurable:!0}),e.prototype.watchSharedVariablesAndEvents=function(e){return this._set_monitoring_flags(s.mobsya.fb.WatchableInfo.SharedEventsDescription|s.mobsya.fb.WatchableInfo.SharedVariables,e)},e.prototype._set_monitoring_flags=function(e,t){var n=this._monitoring_flags;if(t?this._monitoring_flags|=e:this._monitoring_flags&=~e,n!=this._monitoring_flags)return this._client._watch(this._id,this._monitoring_flags)},e.prototype.emitEvents=function(e,t){if(void 0!==t)(n=new Map).set(e,t),e=n;else if("string"==typeof e||e instanceof String){var n;(n=new Map).set(e,null),e=n}return this._client._emit_events(this._id,e)},e}(),c=function(e){function t(t,n){var s=e.call(this,t,n)||this;return s._variables=null,s._events=null,s._on_variables_changed=void 0,s._on_events_descriptions_changed=void 0,s}return __extends(t,e),Object.defineProperty(t.prototype,"variables",{get:function(){return this._variables},enumerable:!1,configurable:!0}),t.prototype.setVariables=function(e){return this._client._set_variables(this._id,e)},Object.defineProperty(t.prototype,"eventsDescriptions",{get:function(){return this._events},enumerable:!1,configurable:!0}),t.prototype.setEventsDescriptions=function(e){return this._client._set_events_descriptions(this._id,e)},Object.defineProperty(t.prototype,"nodes",{get:function(){return this._client._nodes_from_id(this.id)},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"onEventsDescriptionsChanged",{get:function(){return this._on_events_descriptions_changed},set:function(e){this._on_events_descriptions_changed=e},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"onVariablesChanged",{get:function(){return this._on_variables_changed},set:function(e){this._on_variables_changed=e},enumerable:!1,configurable:!0}),t}(b);t.NodeStatus=s.mobsya.fb.NodeStatus,t.NodeType=s.mobsya.fb.NodeType,t.ProgrammingLanguage=s.mobsya.fb.ProgrammingLanguage,t.VMExecutionState=s.mobsya.fb.VMExecutionState;var f=function(e){function t(t,n,o,a){var r=e.call(this,t,n)||this;return r.Status=s.mobsya.fb.NodeStatus,r.VMExecutionState=s.mobsya.fb.VMExecutionState,r.Type=s.mobsya.fb.NodeType,r._desc=null,r._status=o,r._type=a,r._group=null,r.onGroupChanged=void 0,r.onVmExecutionStateChanged=void 0,r}return __extends(t,e),Object.defineProperty(t.prototype,"group",{get:function(){return this._group},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"type",{get:function(){return this._type},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"status",{get:function(){return this._status},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"name",{get:function(){return this._name},enumerable:!1,configurable:!0}),t.prototype.rename=function(e){return this._client._rename_node(this._id,e)},Object.defineProperty(t.prototype,"isReady",{get:function(){return this._status==s.mobsya.fb.NodeStatus.ready},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"statusAsString",{get:function(){switch(this.status){case s.mobsya.fb.NodeStatus.connected:return"connected";case s.mobsya.fb.NodeStatus.ready:return"ready";case s.mobsya.fb.NodeStatus.available:return"available";case s.mobsya.fb.NodeStatus.busy:return"busy";case s.mobsya.fb.NodeStatus.disconnected:return"disconnected"}return"unknown"},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"typeAsString",{get:function(){switch(this.type){case s.mobsya.fb.NodeType.Thymio2:return"Thymio 2";case s.mobsya.fb.NodeType.Thymio2Wireless:return"Thymio Wireless";case s.mobsya.fb.NodeType.SimulatedThymio2:return"Simulated Thymio 2";case s.mobsya.fb.NodeType.DummyNode:return"Dummy Node"}return"unknown"},enumerable:!1,configurable:!0}),t.prototype.lock=function(){return this._client._lock_node(this._id)},t.prototype.unlock=function(){return this._client._unlock_node(this._id)},t.prototype.asebaVMDescription=function(){return this._client._request_aseba_vm_description(this._id)},Object.defineProperty(t.prototype,"sharedVariables",{get:function(){return this.group.variables},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"eventsDescriptions",{get:function(){return this.group.eventsDescriptions},enumerable:!1,configurable:!0}),t.prototype.sendAsebaProgram=function(e){return this._client._send_program(this._id,e,s.mobsya.fb.ProgrammingLanguage.Aseba)},t.prototype.send_aesl_program=function(e){return this._client._send_program(this._id,e,s.mobsya.fb.ProgrammingLanguage.Aesl)},t.prototype.runProgram=function(){return this._client._set_vm_execution_state(this._id,s.mobsya.fb.VMExecutionStateCommand.Run)},t.prototype.flashProgram=function(){return this._client._set_vm_execution_state(this._id,s.mobsya.fb.VMExecutionStateCommand.WriteProgramToDeviceMemory)},t.prototype.setVariables=function(e){return this._client._set_variables(this._id,e)},t.prototype.setSharedVariables=function(e){return this._group.setVariables(e)},t.prototype.setEventsDescriptions=function(e){return this._client._set_events_descriptions(this._id,e)},t.prototype.setScratchPad=function(e,t){return this._client._set_scratchpad(this._id,e,t)},Object.defineProperty(t.prototype,"onVariablesChanged",{get:function(){return this._on_vars_changed_cb},set:function(e){this._set_monitoring_flags(s.mobsya.fb.WatchableInfo.Variables,!!e),this._on_vars_changed_cb=e},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"onEvents",{get:function(){return this._on_events_cb},set:function(e){this._set_monitoring_flags(s.mobsya.fb.WatchableInfo.Events,!!e),this._on_events_cb=e},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"onEventsDescriptionsChanged",{get:function(){return this._group.onEventsDescriptionsChanged},set:function(e){this._group.onEventsDescriptionsChanged=e},enumerable:!1,configurable:!0}),Object.defineProperty(t.prototype,"onSharedVariablesChanged",{get:function(){return this._group.onVariablesChanged},set:function(e){this._group.onVariablesChanged=e},enumerable:!1,configurable:!0}),t.prototype._set_status=function(e){e!=this._status&&(this._status=e,this.onStatusChanged&&this.onStatusChanged(e))},t.prototype._set_name=function(e){e!=this._name&&(this._name=e,this._name&&this.onNameChanged&&this.onNameChanged(e))},t.prototype._set_group=function(e){e!=this._group&&(this._group=e,this._group&&this.onGroupChanged&&this.onGroupChanged(e))},t}(b);t.Node=f;var p=function(){function e(e){var t=this;this.onNodesChanged=void 0,this.onClose=void 0,this._requests=new Map,this._nodes=new Map,this._flex=new o,this._flex.onRuntimeInitialized=function(){t._socket=new WebSocket(e),t._socket.binaryType="arraybuffer",t._socket.onopen=t._onopen.bind(t),t._socket.onmessage=t._onmessage.bind(t),t._socket.onclose=t._onclose.bind(t)}}return Object.defineProperty(e.prototype,"nodes",{get:function(){return Array.from(this._nodes.values())},enumerable:!1,configurable:!0}),e.prototype._onopen=function(){console.log("connected, sending protocol version");var e=new n.flatbuffers.Builder;s.mobsya.fb.ConnectionHandshake.startConnectionHandshake(e),s.mobsya.fb.ConnectionHandshake.addProtocolVersion(e,1),s.mobsya.fb.ConnectionHandshake.addMinProtocolVersion(e,1),this._wrap_message_and_send(e,s.mobsya.fb.ConnectionHandshake.endConnectionHandshake(e),s.mobsya.fb.AnyMessage.ConnectionHandshake)},e.prototype._onclose=function(e){console.log("disconnected"),this.onClose&&this.onClose(e)},e.prototype._onmessage=function(e){var t=new Uint8Array(e.data),o=new n.flatbuffers.ByteBuffer(t),r=s.mobsya.fb.Message.getRootAsMessage(o,null);switch(r.messageType()){case s.mobsya.fb.AnyMessage.ConnectionHandshake:var i=r.message(new s.mobsya.fb.ConnectionHandshake);console.log("Handshake complete: Protocol version ".concat(i.protocolVersion()));break;case s.mobsya.fb.AnyMessage.NodesChanged:this.onNodesChanged&&this.onNodesChanged(this._nodes_changed_as_node_list(r.message(new s.mobsya.fb.NodesChanged)));break;case s.mobsya.fb.AnyMessage.NodeAsebaVMDescription:var d=r.message(new s.mobsya.fb.NodeAsebaVMDescription),_=this._get_request(d.requestId()),b=this._id(d.nodeId());_&&_._trigger_then(b,this._unserialize_aseba_vm_description(d));break;case s.mobsya.fb.AnyMessage.RequestCompleted:d=r.message(new s.mobsya.fb.RequestCompleted);(_=this._get_request(d.requestId()))&&_._trigger_then();break;case s.mobsya.fb.AnyMessage.Error:d=r.message(new s.mobsya.fb.Error);(_=this._get_request(d.requestId()))&&_._trigger_error(d.error());break;case s.mobsya.fb.AnyMessage.CompilationResultSuccess:d=r.message(new s.mobsya.fb.CompilationResultSuccess);(_=this._get_request(d.requestId()))&&_._trigger_then();break;case s.mobsya.fb.AnyMessage.CompilationResultFailure:d=r.message(new s.mobsya.fb.CompilationResultFailure);(_=this._get_request(d.requestId()))&&_._trigger_error("Compilation error");break;case s.mobsya.fb.AnyMessage.VariablesChanged:d=r.message(new s.mobsya.fb.VariablesChanged);var c=this._id(d.nodeId()),f=this._nodes_from_id(c);if(0==f.length)break;for(var p=new Map,h=0;h<d.varsLength();h++){var m=d.vars(h),y=Uint8Array.from(m.valueArray()),g=this._flex.toJSObject(y);isNaN(g)||(g=new Number(g)),p.set(m.name(),g)}f.forEach((function(e){a(e.id,c)&&e.onVariablesChanged?e.onVariablesChanged(p):(e.group._variables=p,e.group.onVariablesChanged&&e.group.onVariablesChanged(p))}));break;case s.mobsya.fb.AnyMessage.EventsEmitted:d=r.message(new s.mobsya.fb.EventsEmitted),b=this._id(d.nodeId());if(!(V=this._nodes.get(b.toString()))||!V.onEvents)break;var l=new Map;for(h=0;h<d.eventsLength();h++){m=d.events(h),y=Uint8Array.from(m.valueArray()),g=this._flex.toJSObject(y);l.set(m.name(),g)}V.onEvents(l);break;case s.mobsya.fb.AnyMessage.EventsDescriptionsChanged:d=r.message(new s.mobsya.fb.EventsDescriptionsChanged),b=this._id(d.nodeOrGroupId());var v=this._group_from_id(b);if(!v)break;var S=[];for(h=0;h<d.eventsLength();h++){m=d.events(h);var N=new u;N.name=m.name(),N.fixed_size=m.fixedSized(),S.push(N)}v._events=S,v.onEventsDescriptionsChanged&&v.onEventsDescriptionsChanged(S);break;case s.mobsya.fb.AnyMessage.VMExecutionStateChanged:d=r.message(new s.mobsya.fb.VMExecutionStateChanged),b=this._id(d.nodeId());if(!(V=this._nodes.get(b.toString()))||!V.onVmExecutionStateChanged)break;V.onVmExecutionStateChanged(d.state(),d.line(),d.error(),d.errorMsg());break;case s.mobsya.fb.AnyMessage.ScratchpadUpdate:var V;d=r.message(new s.mobsya.fb.ScratchpadUpdate),b=this._id(d.nodeId());if(!(V=this._nodes.get(b.toString()))||!V.onScratchpadChanged)break;V.onScratchpadChanged(d.text(),d.language())}},e.prototype._request_aseba_vm_description=function(e){var t=new n.flatbuffers.Builder,o=this._gen_request_id(),a=this._create_node_id(t,e);s.mobsya.fb.RequestNodeAsebaVMDescription.startRequestNodeAsebaVMDescription(t),s.mobsya.fb.RequestNodeAsebaVMDescription.addRequestId(t,o),s.mobsya.fb.RequestNodeAsebaVMDescription.addNodeId(t,a);var r=s.mobsya.fb.RequestNodeAsebaVMDescription.endRequestNodeAsebaVMDescription(t);return this._wrap_message_and_send(t,r,s.mobsya.fb.AnyMessage.RequestNodeAsebaVMDescription),this._prepare_request(o)},e.prototype._send_program=function(e,t,o){var a=new n.flatbuffers.Builder,r=this._gen_request_id(),i=a.createString(t),d=this._create_node_id(a,e);s.mobsya.fb.CompileAndLoadCodeOnVM.startCompileAndLoadCodeOnVM(a),s.mobsya.fb.CompileAndLoadCodeOnVM.addRequestId(a,r),s.mobsya.fb.CompileAndLoadCodeOnVM.addNodeId(a,d),s.mobsya.fb.CompileAndLoadCodeOnVM.addProgram(a,i),s.mobsya.fb.CompileAndLoadCodeOnVM.addLanguage(a,o),s.mobsya.fb.CompileAndLoadCodeOnVM.addOptions(a,s.mobsya.fb.CompilationOptions.LoadOnTarget);var _=s.mobsya.fb.CompileAndLoadCodeOnVM.endCompileAndLoadCodeOnVM(a);return this._wrap_message_and_send(a,_,s.mobsya.fb.AnyMessage.CompileAndLoadCodeOnVM),this._prepare_request(r)},e.prototype._set_vm_execution_state=function(e,t){var o=new n.flatbuffers.Builder,a=this._gen_request_id(),r=this._create_node_id(o,e);s.mobsya.fb.SetVMExecutionState.startSetVMExecutionState(o),s.mobsya.fb.SetVMExecutionState.addRequestId(o,a),s.mobsya.fb.SetVMExecutionState.addNodeId(o,r),s.mobsya.fb.SetVMExecutionState.addCommand(o,t);var i=s.mobsya.fb.SetVMExecutionState.endSetVMExecutionState(o);return this._wrap_message_and_send(o,i,s.mobsya.fb.AnyMessage.SetVMExecutionState),this._prepare_request(a)},e.prototype._set_variables=function(e,t){var o=new n.flatbuffers.Builder,a=this._gen_request_id(),r=this._create_node_id(o,e),i=this._serialize_variables(o,t);s.mobsya.fb.SetVariables.startSetVariables(o),s.mobsya.fb.SetVariables.addNodeOrGroupId(o,r),s.mobsya.fb.SetVariables.addRequestId(o,a),s.mobsya.fb.SetVariables.addVars(o,i);var d=s.mobsya.fb.SetVariables.endSetVariables(o);return this._wrap_message_and_send(o,d,s.mobsya.fb.AnyMessage.SetVariables),this._prepare_request(a)},e.prototype._serialize_variables=function(e,t){var n=this,o=[];return t instanceof Map||(t=new Map(Object.entries(t))),t.forEach((function(t,s,a){o.push(n._serialize_variable(e,s,t))})),s.mobsya.fb.SetVariables.createVarsVector(e,o)},e.prototype._serialize_variable=function(e,t,n){var o=e.createString(t),a=this._flex.fromJSObject(n),r=s.mobsya.fb.NodeVariable.createValueVector(e,a);return s.mobsya.fb.NodeVariable.startNodeVariable(e),s.mobsya.fb.NodeVariable.addName(e,o),s.mobsya.fb.NodeVariable.addValue(e,r),s.mobsya.fb.NodeVariable.endNodeVariable(e)},e.prototype._emit_events=function(e,t){var o=new n.flatbuffers.Builder,a=this._gen_request_id(),r=this._create_node_id(o,e),i=this._serialize_variables(o,t);s.mobsya.fb.SendEvents.startSendEvents(o),s.mobsya.fb.SendEvents.addNodeId(o,r),s.mobsya.fb.SendEvents.addRequestId(o,a),s.mobsya.fb.SendEvents.addEvents(o,i);var d=s.mobsya.fb.SendEvents.endSendEvents(o);return this._wrap_message_and_send(o,d,s.mobsya.fb.AnyMessage.SendEvents),this._prepare_request(a)},e.prototype._set_events_descriptions=function(e,t){var o=new n.flatbuffers.Builder,a=this._gen_request_id(),r=this._create_node_id(o,e),i=this._serialize_events_descriptions(o,t);s.mobsya.fb.RegisterEvents.startRegisterEvents(o),s.mobsya.fb.RegisterEvents.addNodeOrGroupId(o,r),s.mobsya.fb.RegisterEvents.addRequestId(o,a),s.mobsya.fb.RegisterEvents.addEvents(o,i);var d=s.mobsya.fb.RegisterEvents.endRegisterEvents(o);return this._wrap_message_and_send(o,d,s.mobsya.fb.AnyMessage.RegisterEvents),this._prepare_request(a)},e.prototype._serialize_events_descriptions=function(e,t){var n=this,o=[];return t.forEach((function(t){o.push(n._serialize_event_description(e,t.name,t.fixed_size))})),s.mobsya.fb.RegisterEvents.createEventsVector(e,o)},e.prototype._serialize_event_description=function(e,t,n){var o=e.createString(t);return s.mobsya.fb.EventDescription.startEventDescription(e),s.mobsya.fb.EventDescription.addName(e,o),s.mobsya.fb.EventDescription.addFixedSized(e,n),s.mobsya.fb.EventDescription.endEventDescription(e)},e.prototype._set_scratchpad=function(e,t,o){var a=new n.flatbuffers.Builder,r=this._gen_request_id(),i=this._create_node_id(a,e),d=a.createString(t);s.mobsya.fb.ScratchpadUpdate.startScratchpadUpdate(a),s.mobsya.fb.ScratchpadUpdate.addRequestId(a,r),s.mobsya.fb.ScratchpadUpdate.addNodeId(a,i),s.mobsya.fb.ScratchpadUpdate.addScratchpadId(a,i),s.mobsya.fb.ScratchpadUpdate.addText(a,d),s.mobsya.fb.ScratchpadUpdate.addLanguage(a,o);var _=s.mobsya.fb.ScratchpadUpdate.endScratchpadUpdate(a);return this._wrap_message_and_send(a,_,s.mobsya.fb.AnyMessage.ScratchpadUpdate),this._prepare_request(r)},e.prototype._lock_node=function(e){var t=new n.flatbuffers.Builder,o=this._gen_request_id(),a=this._create_node_id(t,e);s.mobsya.fb.LockNode.startLockNode(t),s.mobsya.fb.LockNode.addRequestId(t,o),s.mobsya.fb.LockNode.addNodeId(t,a);var r=s.mobsya.fb.LockNode.endLockNode(t);return this._wrap_message_and_send(t,r,s.mobsya.fb.AnyMessage.LockNode),this._prepare_request(o)},e.prototype._unlock_node=function(e){var t=new n.flatbuffers.Builder,o=this._gen_request_id(),a=this._create_node_id(t,e);s.mobsya.fb.UnlockNode.startUnlockNode(t),s.mobsya.fb.UnlockNode.addRequestId(t,o),s.mobsya.fb.UnlockNode.addNodeId(t,a);var r=s.mobsya.fb.UnlockNode.endUnlockNode(t);return this._wrap_message_and_send(t,r,s.mobsya.fb.AnyMessage.UnlockNode),this._prepare_request(o)},e.prototype._rename_node=function(e,t){var o=new n.flatbuffers.Builder,a=this._gen_request_id(),r=this._create_node_id(o,e),i=o.createString(t);s.mobsya.fb.RenameNode.startRenameNode(o),s.mobsya.fb.RenameNode.addRequestId(o,a),s.mobsya.fb.RenameNode.addNodeId(o,r),s.mobsya.fb.RenameNode.addNewName(o,i);var d=s.mobsya.fb.RenameNode.endRenameNode(o);return this._wrap_message_and_send(o,d,s.mobsya.fb.AnyMessage.RenameNode),this._prepare_request(a)},e.prototype._watch=function(e,t){var o=new n.flatbuffers.Builder,a=this._gen_request_id(),r=this._create_node_id(o,e);s.mobsya.fb.WatchNode.startWatchNode(o),s.mobsya.fb.WatchNode.addRequestId(o,a),s.mobsya.fb.WatchNode.addNodeOrGroupId(o,r),s.mobsya.fb.WatchNode.addInfoType(o,t);var i=s.mobsya.fb.WatchNode.endWatchNode(o);return this._wrap_message_and_send(o,i,s.mobsya.fb.AnyMessage.WatchNode),this._prepare_request(a)},e.prototype._nodes_changed_as_node_list=function(e){for(var n=[],s=0;s<e.nodesLength();s++){var o=e.nodes(s),a=this._id(o.nodeId()),r=this._id(o.groupId()),i=this._nodes.get(a.toString());i||((i=new f(this,a,o.status(),o.type()))._name=o.name(),this._nodes.set(a.toString(),i)),o.status()==t.NodeStatus.disconnected&&this._nodes.delete(a.toString()),n.push(i),i._set_status(o.status()),i._set_name(o.name()),i._set_group(this._group_from_id(r))}return n},e.prototype._nodes_from_id=function(e){return Array.from(this._nodes.values()).filter((function(t){return a(t.id,e)||t.group&&a(t.group.id,e)}))},e.prototype._group_from_id=function(e){var t=Array.from(this._nodes.values()).find((function(t){return t.group&&a(t.group.id,e)}));return t&&t.group?t.group:new c(this,e)},e.prototype._id=function(e){return e?new d(e.idArray()):null},e.prototype._unserialize_aseba_vm_description=function(e){var t=new _;t.bytecode_size=e.bytecodeSize(),t.data_size=e.dataSize(),t.stack_size=e.stackSize();for(var n=0;n<e.variablesLength();n++){var s=e.variables(n);t.variables.push({name:s.name(),size:s.size()})}for(n=0;n<e.eventsLength();n++){s=e.events(n);t.events.push({name:s.name(),description:s.description()})}for(n=0;n<e.functionsLength();n++){s=e.functions(n);for(var o=[],a=0;a<s.parametersLength();a++){var r=s.parameters(n);o.push({name:s.name(),size:r.size()})}t.functions.push({name:s.name(),description:s.description(),params:o})}return t},e.prototype._create_node_id=function(e,t){var n=s.mobsya.fb.NodeId.createIdVector(e,t.data);return s.mobsya.fb.NodeId.startNodeId(e),s.mobsya.fb.NodeId.addId(e,n),s.mobsya.fb.NodeId.endNodeId(e)},e.prototype._wrap_message_and_send=function(e,t,n){s.mobsya.fb.Message.startMessage(e),s.mobsya.fb.Message.addMessageType(e,n),s.mobsya.fb.Message.addMessage(e,t);var o=s.mobsya.fb.Message.endMessage(e);e.finish(o),this._socket.send(e.asUint8Array())},e.prototype._gen_request_id=function(){var e=0;do{e=Math.floor(4294967293*Math.random())+1}while(this._requests.has(e));return e},e.prototype._get_request=function(e){var t=this._requests.get(e);return null!=t&&this._requests.delete(e),null==t&&console.error("unknown request ".concat(e)),t},e.prototype._prepare_request=function(e){var t=new r(e);return this._requests.set(e,t),t._promise},e}();t.createClient=function(e){return new p(e)}}));
//# sourceMappingURL=thymio.js.map
//# sourceMappingURL=thymio.js.map
