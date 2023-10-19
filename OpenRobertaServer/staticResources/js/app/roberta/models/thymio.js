var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
define(["require", "exports", "flatbuffers", "thymio_generated", "@cor3ntin/flexbuffers-wasm", "lodash.isequal", "isomorphic-ws"], function (require, exports, flatbuffers_1, thymio_generated_1, FlexBuffers, isEqual) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.createClient = exports.Node = exports.VMExecutionState = exports.ProgrammingLanguage = exports.NodeType = exports.NodeStatus = exports.EventDescription = exports.AsebaVMDescription = exports.NodeId = void 0;
    /**
     * @private
     */
    var MIN_PROTOCOL_VERSION = 1;
    /**
     * @private
     */
    var PROTOCOL_VERSION = 1;
    /**
        @private
        Represent a network request to the server
        (Thymio 3 or Thymio Device manager)
        This class exposes the same interface and behavior
        as ES7 Promise type, and trigger the promise or raise
        an exception once the request is completed and the server
        replies
    
        Request is an implementation detail,
        client code should only deal with Promises
    */
    var Request = /** @class */ (function () {
        function Request(request_id) {
            /** Error type of a failed request */
            this.ErrorType = thymio_generated_1.mobsya.fb.ErrorType;
            var then = undefined;
            var onerror = undefined;
            this._promise = new Promise(function (resolve, reject) {
                then = resolve;
                onerror = reject;
            });
            this._then = then;
            this._onerror = onerror;
            this._request_id = request_id;
        }
        Request.prototype._trigger_error = function (err) {
            this._onerror(err);
        };
        Request.prototype._trigger_then = function () {
            var args = [];
            for (var _i = 0; _i < arguments.length; _i++) {
                args[_i] = arguments[_i];
            }
            return this._then.apply(this, args);
        };
        return Request;
    }());
    /**
     * Error raised when a request is made
     * against a node that does not exist
     */
    var InvalidNodeIDException = /** @class */ (function () {
        function InvalidNodeIDException() {
            this.message = "Invalid Node Id";
            this.name = 'InvalidNodeIDException';
        }
        return InvalidNodeIDException;
    }());
    /**
     * NodeId stores the 128bit UUID of the node
     */
    var NodeId = /** @class */ (function () {
        function NodeId(array) {
            if (array.length != 16) {
                throw new InvalidNodeIDException();
            }
            this._data = array;
        }
        Object.defineProperty(NodeId.prototype, "data", {
            /**
             * RFC 4122 compatible GUID - little-endian
             */
            get: function () {
                return this._data;
            },
            enumerable: false,
            configurable: true
        });
        /**
         * Return a string representation of the GUID
         * compatible with microsof representation scheme
         *
         * {XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX}
         */
        NodeId.prototype.toString = function () {
            var res = [];
            this._data.forEach(function (byte) {
                res.push(byte.toString(16).padStart(2, '0'));
                if ([4, 7, 10, 13].includes(res.length))
                    res.push('-');
            });
            return '{' + res.join('') + '}';
        };
        return NodeId;
    }());
    exports.NodeId = NodeId;
    /** Description of an aseba virtual machine. */
    var AsebaVMDescription = /** @class */ (function () {
        function AsebaVMDescription() {
            /**
             * Amount of memory reserved for bytecode on this VM, in bytes
             */
            this.bytecode_size = 0;
            /**
            * Amount of memory reserved for data on this VM, in bytes
            */
            this.data_size = 0;
            /**
            * Size of the stack of the vm, in bytes
            */
            this.stack_size = 0;
        }
        return AsebaVMDescription;
    }());
    exports.AsebaVMDescription = AsebaVMDescription;
    /**
     * Description of a shared event.
     *
     */
    var EventDescription = /** @class */ (function () {
        function EventDescription() {
            /**
             * Thymio 2 only supprts events that are fixed-size int16[]
             *
             * fixed_size describes the size, in word, of the event
             */
            this.fixed_size = 0;
            /**
             * Index of the event
             * @internal
             */
            this.index = -1;
        }
        return EventDescription;
    }());
    exports.EventDescription = EventDescription;
    /**
     * @private
     */
    var BasicNode = /** @class */ (function () {
        function BasicNode(client, id) {
            this._monitoring_flags = 0;
            this._client = client;
            this._id = id;
        }
        Object.defineProperty(BasicNode.prototype, "id", {
            get: function () {
                return this._id;
            },
            enumerable: false,
            configurable: true
        });
        BasicNode.prototype.watchSharedVariablesAndEvents = function (enable) {
            return this._set_monitoring_flags(thymio_generated_1.mobsya.fb.WatchableInfo.SharedEventsDescription | thymio_generated_1.mobsya.fb.WatchableInfo.SharedVariables, enable);
        };
        BasicNode.prototype._set_monitoring_flags = function (flag, set) {
            var old = this._monitoring_flags;
            if (set)
                this._monitoring_flags |= flag;
            else
                this._monitoring_flags &= ~flag;
            if (old != this._monitoring_flags) {
                return this._client._watch(this._id, this._monitoring_flags);
            }
        };
        BasicNode.prototype.emitEvents = function (map_or_key, value) {
            if (typeof value !== "undefined") {
                var tmp = new Map();
                tmp.set(map_or_key, value);
                map_or_key = tmp;
            }
            else if (typeof map_or_key === "string" || map_or_key instanceof String) {
                var tmp = new Map();
                tmp.set(map_or_key, null);
                map_or_key = tmp;
            }
            return this._client._emit_events(this._id, map_or_key);
        };
        return BasicNode;
    }());
    /**
     * @private
     */
    var Group = /** @class */ (function (_super) {
        __extends(Group, _super);
        function Group(client, id) {
            var _this = _super.call(this, client, id) || this;
            _this._variables = null;
            _this._events = null;
            _this._on_variables_changed = undefined;
            _this._on_events_descriptions_changed = undefined;
            return _this;
        }
        Object.defineProperty(Group.prototype, "variables", {
            get: function () {
                return this._variables;
            },
            enumerable: false,
            configurable: true
        });
        Group.prototype.setVariables = function (variables) {
            return this._client._set_variables(this._id, variables);
        };
        Object.defineProperty(Group.prototype, "eventsDescriptions", {
            get: function () {
                return this._events;
            },
            enumerable: false,
            configurable: true
        });
        Group.prototype.setEventsDescriptions = function (events) {
            return this._client._set_events_descriptions(this._id, events);
        };
        Object.defineProperty(Group.prototype, "nodes", {
            get: function () {
                return this._client._nodes_from_id(this.id);
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(Group.prototype, "onEventsDescriptionsChanged", {
            get: function () {
                return this._on_events_descriptions_changed;
            },
            set: function (cb) {
                this._on_events_descriptions_changed = cb;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(Group.prototype, "onVariablesChanged", {
            get: function () {
                return this._on_variables_changed;
            },
            set: function (cb) {
                this._on_variables_changed = cb;
            },
            enumerable: false,
            configurable: true
        });
        return Group;
    }(BasicNode));
    exports.NodeStatus = thymio_generated_1.mobsya.fb.NodeStatus;
    exports.NodeType = thymio_generated_1.mobsya.fb.NodeType;
    exports.ProgrammingLanguage = thymio_generated_1.mobsya.fb.ProgrammingLanguage;
    exports.VMExecutionState = thymio_generated_1.mobsya.fb.VMExecutionState;
    /**
     * @private
     */
    var Node = /** @class */ (function (_super) {
        __extends(Node, _super);
        function Node(client, id, status, type) {
            var _this = _super.call(this, client, id) || this;
            _this.Status = thymio_generated_1.mobsya.fb.NodeStatus;
            _this.VMExecutionState = thymio_generated_1.mobsya.fb.VMExecutionState;
            _this.Type = thymio_generated_1.mobsya.fb.NodeType;
            _this._desc = null;
            _this._status = status;
            _this._type = type;
            _this._group = null;
            _this.onGroupChanged = undefined;
            _this.onVmExecutionStateChanged = undefined;
            return _this;
        }
        Object.defineProperty(Node.prototype, "group", {
            get: function () {
                return this._group;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(Node.prototype, "type", {
            /** return the node type*/
            get: function () {
                return this._type;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(Node.prototype, "status", {
            /** The node status
             *  @type {mobsya.fb.NodeStatus}
             */
            get: function () {
                return this._status;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(Node.prototype, "name", {
            /** The node name
             *  @type {mobsya.fb.NodeStatus}
             */
            get: function () {
                return this._name;
            },
            enumerable: false,
            configurable: true
        });
        Node.prototype.rename = function (new_name) {
            return this._client._rename_node(this._id, new_name);
        };
        Object.defineProperty(Node.prototype, "isReady", {
            /** Whether the node is ready (connected, and locked)
             *  @type {boolean}
             */
            get: function () {
                return this._status == thymio_generated_1.mobsya.fb.NodeStatus.ready;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(Node.prototype, "statusAsString", {
            /** The node status converted to string.
             *  @type {string}
             */
            get: function () {
                switch (this.status) {
                    case thymio_generated_1.mobsya.fb.NodeStatus.connected: return "connected";
                    case thymio_generated_1.mobsya.fb.NodeStatus.ready: return "ready";
                    case thymio_generated_1.mobsya.fb.NodeStatus.available: return "available";
                    case thymio_generated_1.mobsya.fb.NodeStatus.busy: return "busy";
                    case thymio_generated_1.mobsya.fb.NodeStatus.disconnected: return "disconnected";
                }
                return "unknown";
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(Node.prototype, "typeAsString", {
            /** The node type converted to string.
             *  @type {string}
             */
            get: function () {
                switch (this.type) {
                    case thymio_generated_1.mobsya.fb.NodeType.Thymio2: return "Thymio 2";
                    case thymio_generated_1.mobsya.fb.NodeType.Thymio2Wireless: return "Thymio Wireless";
                    case thymio_generated_1.mobsya.fb.NodeType.SimulatedThymio2: return "Simulated Thymio 2";
                    case thymio_generated_1.mobsya.fb.NodeType.DummyNode: return "Dummy Node";
                }
                return "unknown";
            },
            enumerable: false,
            configurable: true
        });
        Node.prototype.lock = function () {
            return this._client._lock_node(this._id);
        };
        Node.prototype.unlock = function () {
            return this._client._unlock_node(this._id);
        };
        Node.prototype.asebaVMDescription = function () {
            return this._client._request_aseba_vm_description(this._id);
        };
        Object.defineProperty(Node.prototype, "sharedVariables", {
            get: function () {
                return this.group.variables;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(Node.prototype, "eventsDescriptions", {
            get: function () {
                return this.group.eventsDescriptions;
            },
            enumerable: false,
            configurable: true
        });
        Node.prototype.sendAsebaProgram = function (code) {
            return this._client._send_program(this._id, code, thymio_generated_1.mobsya.fb.ProgrammingLanguage.Aseba);
        };
        /** Load an aesl program on the VM
         *  The device must be locked & ready before calling this function
         *  @param {external:String} code - the aseba code to load
         *  @throws {mobsya.fb.Error}
         *  @see lock
         */
        Node.prototype.send_aesl_program = function (code) {
            return this._client._send_program(this._id, code, thymio_generated_1.mobsya.fb.ProgrammingLanguage.Aesl);
        };
        Node.prototype.runProgram = function () {
            return this._client._set_vm_execution_state(this._id, thymio_generated_1.mobsya.fb.VMExecutionStateCommand.Run);
        };
        Node.prototype.flashProgram = function () {
            return this._client._set_vm_execution_state(this._id, thymio_generated_1.mobsya.fb.VMExecutionStateCommand.WriteProgramToDeviceMemory);
        };
        Node.prototype.setVariables = function (map) {
            return this._client._set_variables(this._id, map);
        };
        Node.prototype.setSharedVariables = function (variables) {
            return this._group.setVariables(variables);
        };
        Node.prototype.setEventsDescriptions = function (events) {
            return this._client._set_events_descriptions(this._id, events);
        };
        Node.prototype.setScratchPad = function (text, language) {
            return this._client._set_scratchpad(this._id, text, language);
        };
        Object.defineProperty(Node.prototype, "onVariablesChanged", {
            get: function () {
                return this._on_vars_changed_cb;
            },
            set: function (cb) {
                this._set_monitoring_flags(thymio_generated_1.mobsya.fb.WatchableInfo.Variables, !!cb);
                this._on_vars_changed_cb = cb;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(Node.prototype, "onEvents", {
            get: function () {
                return this._on_events_cb;
            },
            set: function (cb) {
                this._set_monitoring_flags(thymio_generated_1.mobsya.fb.WatchableInfo.Events, !!cb);
                this._on_events_cb = cb;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(Node.prototype, "onEventsDescriptionsChanged", {
            get: function () {
                return this._group.onEventsDescriptionsChanged;
            },
            set: function (cb) {
                this._group.onEventsDescriptionsChanged = cb;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(Node.prototype, "onSharedVariablesChanged", {
            get: function () {
                return this._group.onVariablesChanged;
            },
            set: function (cb) {
                this._group.onVariablesChanged = cb;
            },
            enumerable: false,
            configurable: true
        });
        Node.prototype._set_status = function (status) {
            if (status != this._status) {
                this._status = status;
                if (this.onStatusChanged) {
                    this.onStatusChanged(status);
                }
            }
        };
        Node.prototype._set_name = function (name) {
            if (name != this._name) {
                this._name = name;
                if (this._name && this.onNameChanged) {
                    this.onNameChanged(name);
                }
            }
        };
        Node.prototype._set_group = function (group) {
            if (group != this._group) {
                this._group = group;
                if (this._group && this.onGroupChanged) {
                    this.onGroupChanged(group);
                }
            }
        };
        return Node;
    }(BasicNode));
    exports.Node = Node;
    /**
     * @private
     */
    var Client = /** @class */ (function () {
        /**
         *  @param {external:String} url : Web socket address
         *  @see lock
         */
        function Client(url) {
            var _this = this;
            this.onNodesChanged = undefined;
            this.onClose = undefined;
            //In progress requests (id : node)
            this._requests = new Map();
            //Known nodes (id : node)
            this._nodes = new Map();
            this._flex = new FlexBuffers();
            this._flex.onRuntimeInitialized = function () {
                _this._socket = new WebSocket(url);
                _this._socket.binaryType = 'arraybuffer';
                _this._socket.onopen = _this._onopen.bind(_this);
                _this._socket.onmessage = _this._onmessage.bind(_this);
                _this._socket.onclose = _this._onclose.bind(_this);
            };
        }
        Object.defineProperty(Client.prototype, "nodes", {
            get: function () {
                return Array.from(this._nodes.values());
            },
            enumerable: false,
            configurable: true
        });
        Client.prototype._onopen = function () {
            console.log("connected, sending protocol version");
            var builder = new flatbuffers_1.flatbuffers.Builder();
            thymio_generated_1.mobsya.fb.ConnectionHandshake.startConnectionHandshake(builder);
            thymio_generated_1.mobsya.fb.ConnectionHandshake.addProtocolVersion(builder, PROTOCOL_VERSION);
            thymio_generated_1.mobsya.fb.ConnectionHandshake.addMinProtocolVersion(builder, MIN_PROTOCOL_VERSION);
            this._wrap_message_and_send(builder, thymio_generated_1.mobsya.fb.ConnectionHandshake.endConnectionHandshake(builder), thymio_generated_1.mobsya.fb.AnyMessage.ConnectionHandshake);
        };
        Client.prototype._onclose = function (event) {
            console.log("disconnected");
            if (this.onClose) {
                this.onClose(event);
            }
        };
        Client.prototype._onmessage = function (event) {
            var data = new Uint8Array(event.data);
            var buf = new flatbuffers_1.flatbuffers.ByteBuffer(data);
            var message = thymio_generated_1.mobsya.fb.Message.getRootAsMessage(buf, null);
            switch (message.messageType()) {
                case thymio_generated_1.mobsya.fb.AnyMessage.ConnectionHandshake: {
                    var hs = message.message(new thymio_generated_1.mobsya.fb.ConnectionHandshake());
                    console.log("Handshake complete: Protocol version ".concat(hs.protocolVersion()));
                    break;
                }
                case thymio_generated_1.mobsya.fb.AnyMessage.NodesChanged: {
                    if (this.onNodesChanged)
                        this.onNodesChanged(this._nodes_changed_as_node_list(message.message(new thymio_generated_1.mobsya.fb.NodesChanged())));
                    break;
                }
                case thymio_generated_1.mobsya.fb.AnyMessage.NodeAsebaVMDescription: {
                    var msg = message.message(new thymio_generated_1.mobsya.fb.NodeAsebaVMDescription());
                    var req = this._get_request(msg.requestId());
                    var id = this._id(msg.nodeId());
                    if (req) {
                        req._trigger_then(id, this._unserialize_aseba_vm_description(msg));
                    }
                    break;
                }
                case thymio_generated_1.mobsya.fb.AnyMessage.RequestCompleted: {
                    var msg = message.message(new thymio_generated_1.mobsya.fb.RequestCompleted());
                    var req = this._get_request(msg.requestId());
                    if (req) {
                        req._trigger_then();
                    }
                    break;
                }
                case thymio_generated_1.mobsya.fb.AnyMessage.Error: {
                    var msg = message.message(new thymio_generated_1.mobsya.fb.Error());
                    var req = this._get_request(msg.requestId());
                    if (req) {
                        req._trigger_error(msg.error());
                    }
                    break;
                }
                case thymio_generated_1.mobsya.fb.AnyMessage.CompilationResultSuccess: {
                    var msg = message.message(new thymio_generated_1.mobsya.fb.CompilationResultSuccess());
                    var req = this._get_request(msg.requestId());
                    if (req) {
                        req._trigger_then();
                    }
                    break;
                }
                case thymio_generated_1.mobsya.fb.AnyMessage.CompilationResultFailure: {
                    var msg = message.message(new thymio_generated_1.mobsya.fb.CompilationResultFailure());
                    var req = this._get_request(msg.requestId());
                    if (req) {
                        //TODO
                        req._trigger_error("Compilation error");
                    }
                    break;
                }
                case thymio_generated_1.mobsya.fb.AnyMessage.VariablesChanged: {
                    var msg = message.message(new thymio_generated_1.mobsya.fb.VariablesChanged());
                    var id_1 = this._id(msg.nodeId());
                    var nodes = this._nodes_from_id(id_1);
                    if (nodes.length == 0)
                        break;
                    var vars_1 = new Map();
                    for (var i = 0; i < msg.varsLength(); i++) {
                        var v = msg.vars(i);
                        var myarray = Uint8Array.from(v.valueArray());
                        var val = this._flex.toJSObject(myarray);
                        if (!isNaN(val)) {
                            val = new Number(val);
                        }
                        vars_1.set(v.name(), val);
                    }
                    nodes.forEach(function (node) {
                        if (isEqual(node.id, id_1) && node.onVariablesChanged) {
                            node.onVariablesChanged(vars_1);
                        }
                        else {
                            node.group._variables = vars_1;
                            if (node.group.onVariablesChanged) {
                                node.group.onVariablesChanged(vars_1);
                            }
                        }
                    });
                    break;
                }
                case thymio_generated_1.mobsya.fb.AnyMessage.EventsEmitted: {
                    var msg = message.message(new thymio_generated_1.mobsya.fb.EventsEmitted());
                    var id = this._id(msg.nodeId());
                    var node = this._nodes.get(id.toString());
                    if (!node || !node.onEvents)
                        break;
                    var vars = new Map();
                    for (var i = 0; i < msg.eventsLength(); i++) {
                        var v = msg.events(i);
                        var myarray = Uint8Array.from(v.valueArray());
                        var val = this._flex.toJSObject(myarray);
                        vars.set(v.name(), val);
                    }
                    node.onEvents(vars);
                    break;
                }
                case thymio_generated_1.mobsya.fb.AnyMessage.EventsDescriptionsChanged: {
                    var msg = message.message(new thymio_generated_1.mobsya.fb.EventsDescriptionsChanged());
                    var id = this._id(msg.nodeOrGroupId());
                    var group = this._group_from_id(id);
                    if (!group)
                        break;
                    var events = [];
                    for (var i = 0; i < msg.eventsLength(); i++) {
                        var v = msg.events(i);
                        var e = new EventDescription;
                        e.name = v.name();
                        e.fixed_size = v.fixedSized();
                        events.push(e);
                    }
                    group._events = events;
                    if (group.onEventsDescriptionsChanged)
                        group.onEventsDescriptionsChanged(events);
                    break;
                }
                case thymio_generated_1.mobsya.fb.AnyMessage.VMExecutionStateChanged: {
                    var msg = message.message(new thymio_generated_1.mobsya.fb.VMExecutionStateChanged());
                    var id = this._id(msg.nodeId());
                    var node = this._nodes.get(id.toString());
                    if (!node || !node.onVmExecutionStateChanged)
                        break;
                    node.onVmExecutionStateChanged(msg.state(), msg.line(), msg.error(), msg.errorMsg());
                    break;
                }
                case thymio_generated_1.mobsya.fb.AnyMessage.ScratchpadUpdate: {
                    var msg = message.message(new thymio_generated_1.mobsya.fb.ScratchpadUpdate());
                    var id = this._id(msg.nodeId());
                    var node = this._nodes.get(id.toString());
                    if (!node || !node.onScratchpadChanged)
                        break;
                    node.onScratchpadChanged(msg.text(), msg.language());
                    break;
                }
            }
        };
        Client.prototype._request_aseba_vm_description = function (id) {
            var builder = new flatbuffers_1.flatbuffers.Builder();
            var req_id = this._gen_request_id();
            var nodeOffset = this._create_node_id(builder, id);
            thymio_generated_1.mobsya.fb.RequestNodeAsebaVMDescription.startRequestNodeAsebaVMDescription(builder);
            thymio_generated_1.mobsya.fb.RequestNodeAsebaVMDescription.addRequestId(builder, req_id);
            thymio_generated_1.mobsya.fb.RequestNodeAsebaVMDescription.addNodeId(builder, nodeOffset);
            var offset = thymio_generated_1.mobsya.fb.RequestNodeAsebaVMDescription.endRequestNodeAsebaVMDescription(builder);
            this._wrap_message_and_send(builder, offset, thymio_generated_1.mobsya.fb.AnyMessage.RequestNodeAsebaVMDescription);
            return this._prepare_request(req_id);
        };
        Client.prototype._send_program = function (id, code, language) {
            var builder = new flatbuffers_1.flatbuffers.Builder();
            var req_id = this._gen_request_id();
            var codeOffset = builder.createString(code);
            var nodeOffset = this._create_node_id(builder, id);
            thymio_generated_1.mobsya.fb.CompileAndLoadCodeOnVM.startCompileAndLoadCodeOnVM(builder);
            thymio_generated_1.mobsya.fb.CompileAndLoadCodeOnVM.addRequestId(builder, req_id);
            thymio_generated_1.mobsya.fb.CompileAndLoadCodeOnVM.addNodeId(builder, nodeOffset);
            thymio_generated_1.mobsya.fb.CompileAndLoadCodeOnVM.addProgram(builder, codeOffset);
            thymio_generated_1.mobsya.fb.CompileAndLoadCodeOnVM.addLanguage(builder, language);
            thymio_generated_1.mobsya.fb.CompileAndLoadCodeOnVM.addOptions(builder, thymio_generated_1.mobsya.fb.CompilationOptions.LoadOnTarget);
            var offset = thymio_generated_1.mobsya.fb.CompileAndLoadCodeOnVM.endCompileAndLoadCodeOnVM(builder);
            this._wrap_message_and_send(builder, offset, thymio_generated_1.mobsya.fb.AnyMessage.CompileAndLoadCodeOnVM);
            return this._prepare_request(req_id);
        };
        Client.prototype._set_vm_execution_state = function (id, command) {
            var builder = new flatbuffers_1.flatbuffers.Builder();
            var req_id = this._gen_request_id();
            var nodeOffset = this._create_node_id(builder, id);
            thymio_generated_1.mobsya.fb.SetVMExecutionState.startSetVMExecutionState(builder);
            thymio_generated_1.mobsya.fb.SetVMExecutionState.addRequestId(builder, req_id);
            thymio_generated_1.mobsya.fb.SetVMExecutionState.addNodeId(builder, nodeOffset);
            thymio_generated_1.mobsya.fb.SetVMExecutionState.addCommand(builder, command);
            var offset = thymio_generated_1.mobsya.fb.SetVMExecutionState.endSetVMExecutionState(builder);
            this._wrap_message_and_send(builder, offset, thymio_generated_1.mobsya.fb.AnyMessage.SetVMExecutionState);
            return this._prepare_request(req_id);
        };
        //TODO : check variable types, etc
        Client.prototype._set_variables = function (id, variables) {
            var builder = new flatbuffers_1.flatbuffers.Builder();
            var req_id = this._gen_request_id();
            var nodeOffset = this._create_node_id(builder, id);
            var varsOffset = this._serialize_variables(builder, variables);
            thymio_generated_1.mobsya.fb.SetVariables.startSetVariables(builder);
            thymio_generated_1.mobsya.fb.SetVariables.addNodeOrGroupId(builder, nodeOffset);
            thymio_generated_1.mobsya.fb.SetVariables.addRequestId(builder, req_id);
            thymio_generated_1.mobsya.fb.SetVariables.addVars(builder, varsOffset);
            var tableOffset = thymio_generated_1.mobsya.fb.SetVariables.endSetVariables(builder);
            this._wrap_message_and_send(builder, tableOffset, thymio_generated_1.mobsya.fb.AnyMessage.SetVariables);
            return this._prepare_request(req_id);
        };
        Client.prototype._serialize_variables = function (builder, variables) {
            var _this = this;
            var offsets = [];
            if (!(variables instanceof Map)) {
                variables = new Map(Object.entries(variables));
            }
            variables.forEach(function (value, name, _map) {
                offsets.push(_this._serialize_variable(builder, name, value));
            });
            return thymio_generated_1.mobsya.fb.SetVariables.createVarsVector(builder, offsets);
        };
        Client.prototype._serialize_variable = function (builder, name, value) {
            var nameOffset = builder.createString(name);
            var buffer = this._flex.fromJSObject(value);
            var bufferOffset = thymio_generated_1.mobsya.fb.NodeVariable.createValueVector(builder, buffer);
            thymio_generated_1.mobsya.fb.NodeVariable.startNodeVariable(builder);
            thymio_generated_1.mobsya.fb.NodeVariable.addName(builder, nameOffset);
            thymio_generated_1.mobsya.fb.NodeVariable.addValue(builder, bufferOffset);
            return thymio_generated_1.mobsya.fb.NodeVariable.endNodeVariable(builder);
        };
        Client.prototype._emit_events = function (id, variables) {
            var builder = new flatbuffers_1.flatbuffers.Builder();
            var req_id = this._gen_request_id();
            var nodeOffset = this._create_node_id(builder, id);
            var varsOffset = this._serialize_variables(builder, variables);
            thymio_generated_1.mobsya.fb.SendEvents.startSendEvents(builder);
            thymio_generated_1.mobsya.fb.SendEvents.addNodeId(builder, nodeOffset);
            thymio_generated_1.mobsya.fb.SendEvents.addRequestId(builder, req_id);
            thymio_generated_1.mobsya.fb.SendEvents.addEvents(builder, varsOffset);
            var tableOffset = thymio_generated_1.mobsya.fb.SendEvents.endSendEvents(builder);
            this._wrap_message_and_send(builder, tableOffset, thymio_generated_1.mobsya.fb.AnyMessage.SendEvents);
            return this._prepare_request(req_id);
        };
        Client.prototype._set_events_descriptions = function (id, events) {
            var builder = new flatbuffers_1.flatbuffers.Builder();
            var req_id = this._gen_request_id();
            var nodeOffset = this._create_node_id(builder, id);
            var eventsOffset = this._serialize_events_descriptions(builder, events);
            thymio_generated_1.mobsya.fb.RegisterEvents.startRegisterEvents(builder);
            thymio_generated_1.mobsya.fb.RegisterEvents.addNodeOrGroupId(builder, nodeOffset);
            thymio_generated_1.mobsya.fb.RegisterEvents.addRequestId(builder, req_id);
            thymio_generated_1.mobsya.fb.RegisterEvents.addEvents(builder, eventsOffset);
            var tableOffset = thymio_generated_1.mobsya.fb.RegisterEvents.endRegisterEvents(builder);
            this._wrap_message_and_send(builder, tableOffset, thymio_generated_1.mobsya.fb.AnyMessage.RegisterEvents);
            return this._prepare_request(req_id);
        };
        Client.prototype._serialize_events_descriptions = function (builder, events) {
            var _this = this;
            var offsets = [];
            events.forEach(function (event) {
                offsets.push(_this._serialize_event_description(builder, event.name, event.fixed_size));
            });
            return thymio_generated_1.mobsya.fb.RegisterEvents.createEventsVector(builder, offsets);
        };
        Client.prototype._serialize_event_description = function (builder, name, fixed_size) {
            var nameOffset = builder.createString(name);
            thymio_generated_1.mobsya.fb.EventDescription.startEventDescription(builder);
            thymio_generated_1.mobsya.fb.EventDescription.addName(builder, nameOffset);
            thymio_generated_1.mobsya.fb.EventDescription.addFixedSized(builder, fixed_size);
            return thymio_generated_1.mobsya.fb.EventDescription.endEventDescription(builder);
        };
        Client.prototype._set_scratchpad = function (id, text, language) {
            var builder = new flatbuffers_1.flatbuffers.Builder();
            var req_id = this._gen_request_id();
            var nodeOffset = this._create_node_id(builder, id);
            var textOffset = builder.createString(text);
            thymio_generated_1.mobsya.fb.ScratchpadUpdate.startScratchpadUpdate(builder);
            thymio_generated_1.mobsya.fb.ScratchpadUpdate.addRequestId(builder, req_id);
            thymio_generated_1.mobsya.fb.ScratchpadUpdate.addNodeId(builder, nodeOffset);
            thymio_generated_1.mobsya.fb.ScratchpadUpdate.addScratchpadId(builder, nodeOffset);
            thymio_generated_1.mobsya.fb.ScratchpadUpdate.addText(builder, textOffset);
            thymio_generated_1.mobsya.fb.ScratchpadUpdate.addLanguage(builder, language);
            var offset = thymio_generated_1.mobsya.fb.ScratchpadUpdate.endScratchpadUpdate(builder);
            this._wrap_message_and_send(builder, offset, thymio_generated_1.mobsya.fb.AnyMessage.ScratchpadUpdate);
            return this._prepare_request(req_id);
        };
        /* request the description of the aseba vm for the node with the given id */
        Client.prototype._lock_node = function (id) {
            var builder = new flatbuffers_1.flatbuffers.Builder();
            var req_id = this._gen_request_id();
            var nodeOffset = this._create_node_id(builder, id);
            thymio_generated_1.mobsya.fb.LockNode.startLockNode(builder);
            thymio_generated_1.mobsya.fb.LockNode.addRequestId(builder, req_id);
            thymio_generated_1.mobsya.fb.LockNode.addNodeId(builder, nodeOffset);
            var offset = thymio_generated_1.mobsya.fb.LockNode.endLockNode(builder);
            this._wrap_message_and_send(builder, offset, thymio_generated_1.mobsya.fb.AnyMessage.LockNode);
            return this._prepare_request(req_id);
        };
        Client.prototype._unlock_node = function (id) {
            var builder = new flatbuffers_1.flatbuffers.Builder();
            var req_id = this._gen_request_id();
            var nodeOffset = this._create_node_id(builder, id);
            thymio_generated_1.mobsya.fb.UnlockNode.startUnlockNode(builder);
            thymio_generated_1.mobsya.fb.UnlockNode.addRequestId(builder, req_id);
            thymio_generated_1.mobsya.fb.UnlockNode.addNodeId(builder, nodeOffset);
            var offset = thymio_generated_1.mobsya.fb.UnlockNode.endUnlockNode(builder);
            this._wrap_message_and_send(builder, offset, thymio_generated_1.mobsya.fb.AnyMessage.UnlockNode);
            return this._prepare_request(req_id);
        };
        Client.prototype._rename_node = function (id, name) {
            var builder = new flatbuffers_1.flatbuffers.Builder();
            var req_id = this._gen_request_id();
            var nodeOffset = this._create_node_id(builder, id);
            var nameOffset = builder.createString(name);
            thymio_generated_1.mobsya.fb.RenameNode.startRenameNode(builder);
            thymio_generated_1.mobsya.fb.RenameNode.addRequestId(builder, req_id);
            thymio_generated_1.mobsya.fb.RenameNode.addNodeId(builder, nodeOffset);
            thymio_generated_1.mobsya.fb.RenameNode.addNewName(builder, nameOffset);
            var offset = thymio_generated_1.mobsya.fb.RenameNode.endRenameNode(builder);
            this._wrap_message_and_send(builder, offset, thymio_generated_1.mobsya.fb.AnyMessage.RenameNode);
            return this._prepare_request(req_id);
        };
        Client.prototype._watch = function (id, monitoring_flags) {
            var builder = new flatbuffers_1.flatbuffers.Builder();
            var req_id = this._gen_request_id();
            var nodeOffset = this._create_node_id(builder, id);
            thymio_generated_1.mobsya.fb.WatchNode.startWatchNode(builder);
            thymio_generated_1.mobsya.fb.WatchNode.addRequestId(builder, req_id);
            thymio_generated_1.mobsya.fb.WatchNode.addNodeOrGroupId(builder, nodeOffset);
            thymio_generated_1.mobsya.fb.WatchNode.addInfoType(builder, monitoring_flags);
            var offset = thymio_generated_1.mobsya.fb.WatchNode.endWatchNode(builder);
            this._wrap_message_and_send(builder, offset, thymio_generated_1.mobsya.fb.AnyMessage.WatchNode);
            return this._prepare_request(req_id);
        };
        Client.prototype._nodes_changed_as_node_list = function (msg) {
            var nodes = [];
            for (var i = 0; i < msg.nodesLength(); i++) {
                var n = msg.nodes(i);
                var id = this._id(n.nodeId());
                var group_id = this._id(n.groupId());
                var node = this._nodes.get(id.toString());
                if (!node) {
                    node = new Node(this, id, n.status(), n.type());
                    node._name = n.name();
                    this._nodes.set(id.toString(), node);
                }
                if (n.status() == exports.NodeStatus.disconnected) {
                    this._nodes.delete(id.toString());
                }
                nodes.push(node);
                node._set_status(n.status());
                node._set_name(n.name());
                node._set_group(this._group_from_id(group_id));
            }
            return nodes;
        };
        Client.prototype._nodes_from_id = function (id) {
            return Array.from(this._nodes.values()).filter(function (node) { return isEqual(node.id, id) || (node.group && isEqual(node.group.id, id)); });
        };
        Client.prototype._group_from_id = function (id) {
            var node = Array.from(this._nodes.values()).find(function (node) { return node.group && isEqual(node.group.id, id); });
            if (node && node.group)
                return node.group;
            return new Group(this, id);
        };
        Client.prototype._id = function (fb_id) {
            return fb_id ? new NodeId(fb_id.idArray()) : null;
        };
        Client.prototype._unserialize_aseba_vm_description = function (msg) {
            var desc = new AsebaVMDescription();
            desc.bytecode_size = msg.bytecodeSize();
            desc.data_size = msg.dataSize();
            desc.stack_size = msg.stackSize();
            for (var i = 0; i < msg.variablesLength(); i++) {
                var v = msg.variables(i);
                desc.variables.push({ "name": v.name(), "size": v.size() });
            }
            for (var i = 0; i < msg.eventsLength(); i++) {
                var v = msg.events(i);
                desc.events.push({ "name": v.name(), "description": v.description() });
            }
            for (var i = 0; i < msg.functionsLength(); i++) {
                var v = msg.functions(i);
                var params = [];
                for (var j = 0; j < v.parametersLength(); j++) {
                    var p = v.parameters(i);
                    params.push({ "name": v.name(), "size": p.size() });
                }
                desc.functions.push({ "name": v.name(), "description": v.description(), "params": params });
            }
            return desc;
        };
        Client.prototype._create_node_id = function (builder, id) {
            var offset = thymio_generated_1.mobsya.fb.NodeId.createIdVector(builder, id.data);
            thymio_generated_1.mobsya.fb.NodeId.startNodeId(builder);
            thymio_generated_1.mobsya.fb.NodeId.addId(builder, offset);
            return thymio_generated_1.mobsya.fb.NodeId.endNodeId(builder);
        };
        Client.prototype._wrap_message_and_send = function (builder, offset, type) {
            thymio_generated_1.mobsya.fb.Message.startMessage(builder);
            thymio_generated_1.mobsya.fb.Message.addMessageType(builder, type);
            thymio_generated_1.mobsya.fb.Message.addMessage(builder, offset);
            var builtMsg = thymio_generated_1.mobsya.fb.Message.endMessage(builder);
            builder.finish(builtMsg);
            this._socket.send(builder.asUint8Array());
        };
        Client.prototype._gen_request_id = function () {
            var n = 0;
            do {
                n = Math.floor(Math.random() * (0xffffffff - 2)) + 1;
            } while (this._requests.has(n));
            return n;
        };
        Client.prototype._get_request = function (id) {
            var req = this._requests.get(id);
            if (req != undefined)
                this._requests.delete(id);
            if (req == undefined) {
                console.error("unknown request ".concat(id));
            }
            return req;
        };
        Client.prototype._prepare_request = function (req_id) {
            var req = new Request(req_id);
            this._requests.set(req_id, req);
            return req._promise;
        };
        return Client;
    }());
    /**
     * Connects to a Thymio Device Manager or a Thymio 3
     *
     * This Function is the main entry point of the API
     *
     * @param url WebSocket server to connect to.
     * The of the url is `ws://<server>:<port>`
     * This url need to be obtained by an implementation-defined manner.
     *
     * The server must be a Thymio Device Manger or a Thymio 3
     * Or otherwise implement the Thymio Device Manager protocol
     */
    function createClient(url) {
        return new Client(url);
    }
    exports.createClient = createClient;
});
