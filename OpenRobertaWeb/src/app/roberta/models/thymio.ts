import {flatbuffers} from 'flatbuffers';
import {mobsya} from 'thymio_generated';
// @ts-ignore
import * as FlexBuffers from '@cor3ntin/flexbuffers-wasm';

import 'isomorphic-ws'
// @ts-ignore
import * as isEqual from 'lodash.isequal';
/**
 * @private
 */
const MIN_PROTOCOL_VERSION = 1
/**
 * @private
 */
const PROTOCOL_VERSION = 1



/**
 * Each variable has a name an a value
 * When communication with a Thymio 2, variables are always an array of int16.
 * Sending an unique value to a Thymio 2 is equivalent to sending an array of size 1.
 * Sending a value of an expected type will raise an exception.
 */
export type Variables = Map<string, any>;


/**
 * Each event has a name an a value
 * When communication with a Thymio 2, event are always an array 0 or more int16.
 * The arity of the event must match [[EventDescription.fixed_size]]
 * Sending a event of an unexpected type or size will raise an exception.
 */
export type Events = Map<string, any>;

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
class Request {

    /** Error type of a failed request */
    public ErrorType = mobsya.fb.ErrorType;

    _promise : Promise<any>
    private _then: (args: Promise<any>) => void;
    private _onerror: (args: Promise<any>) => void;
    private _request_id: number;

    constructor(request_id : number) {
        var then    = undefined;
        var onerror = undefined;
        this._promise = new Promise((resolve, reject) => {
            then    = resolve;
            onerror = reject;
        });
        this._then = then
        this._onerror = onerror
        this._request_id = request_id
    }

    _trigger_error(err : any) {
        this._onerror(err)
    }

    _trigger_then(...args : any) {
        return this._then.apply(this, args);
    }
}

/**
 * Error raised when a request is made
 * against a node that does not exist
 */

class InvalidNodeIDException {

    message: string;
    name: string;

    constructor() {
        this.message = "Invalid Node Id";
        this.name = 'InvalidNodeIDException';
    }
}

/**
 * NodeId stores the 128bit UUID of the node
 */

export class NodeId {

    private _data: Uint8Array;

    constructor(array : Uint8Array) {
        if(array.length != 16) {
            throw new InvalidNodeIDException()
        }
        this._data = array
    }

    /**
     * RFC 4122 compatible GUID - little-endian
     */
    get data() {
        return this._data;
    }


    /**
     * Return a string representation of the GUID
     * compatible with microsof representation scheme
     *
     * {XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX}
     */
    toString() {
        let res = []
        this._data.forEach(byte => {
                res.push(byte.toString(16).padStart(2, '0'))
                if([4, 7, 10, 13].includes(res.length))
                    res.push('-');
            }
        );
        return '{' + res.join('') + '}';
    }
}


/** Description of an aseba virtual machine. */
export class AsebaVMDescription {

    /**
     * Amount of memory reserved for bytecode on this VM, in bytes
     */
    bytecode_size:number = 0;
     /**
     * Amount of memory reserved for data on this VM, in bytes
     */
    data_size:number = 0;
     /**
     * Size of the stack of the vm, in bytes
     */
    stack_size:number = 0;

    /**
     * List of the persistent, non-shared variables available on this VM
     */
    variables:Array<any>;
    /**
     * List of the persistent, non-shared internal events that this vm can
     * react to. VM events can not be be emitted.
     */
    events:Array<any>;
     /**
     * List of the natives functions available on this VM
     */
    functions:Array<any>;
}

/**
 * Description of a shared event.
 *
 */
export class EventDescription {

    /**
     * Name of the event
     *
     * Needs to be a valid Aseba Identifier (start by a letter, no space)
     */
    name:string;
    /**
     * Thymio 2 only supprts events that are fixed-size int16[]
     *
     * fixed_size describes the size, in word, of the event
     */
    fixed_size:number = 0;
    /**
     * Index of the event
     * @internal
     */
    index:number = -1;
}

/**
 * Group and Node share a common base interface.
 */
export interface IBasicNode {
    /**
     * Unique Id of the node
     */
    readonly id : NodeId;

    /**
     * Request the TDM to dispatch modifications of the
     * shared variables and event description for a group
     * or a Thymio to this client.
     *
     * This *must* be called for the following event to be triggered
     *  * [[IGroup.onEventsDescriptionsChanged]]
     *  * [[IGroup.onVariablesChanged]]
     *  * [[INode.onEventsDescriptionsChanged]]
     *  * [[INode.onSharedVariablesChanged]]
     *
     * @param flags bitflag of [[fb.mobsya.WatchableInfo]]
     */
    watchSharedVariablesAndEvents(enable : boolean) : Promise<any>

    /**
     * Send events to a group
     * Each event must be registed on the group (see [[setEventsDescriptions]])
     *
     * When sending events to Thymio 2, the value of each event must be an array of int16 matching [[EventDescription.fixed_size]]
     * @param events events to broadcast
     */
    emitEvents(events : Events) : Promise<any>
    emitEvents(key : string, value : any) : Promise<any>
    emitEvents(key : string) : Promise<any>
}

/**
 * @private
 */
class BasicNode implements IBasicNode {
    protected  _monitoring_flags:number;
    protected  _client:Client;
    protected  _id:NodeId;

    constructor(client :Client, id : NodeId) {
        this._monitoring_flags = 0
        this._client = client
        this._id = id;
    }

    get id() {
        return this._id
    }

    watchSharedVariablesAndEvents(enable : boolean) {
        return this._set_monitoring_flags(mobsya.fb.WatchableInfo.SharedEventsDescription | mobsya.fb.WatchableInfo.SharedVariables, enable)
    }

    _set_monitoring_flags(flag, set) {
        const old = this._monitoring_flags;
        if(set)
            this._monitoring_flags |= flag
        else
            this._monitoring_flags &= ~flag

        if(old != this._monitoring_flags) {
            return this._client._watch(this._id, this._monitoring_flags)
        }
    }

    emitEvents(map_or_key : string | Events, value? : any) {
        if(typeof value !== "undefined") {
            const tmp = new Map();
            tmp.set(map_or_key, value);
            map_or_key = tmp
        }
        else if(typeof map_or_key === "string" || map_or_key instanceof String) {
            const tmp = new Map();
            tmp.set(map_or_key, null);
            map_or_key = tmp
        }
        return this._client._emit_events(this._id, map_or_key);
    }
}

/**
 * A group represents a list of robots
 * sharing the same persistent state.
 *
 * Group hold the events and shared variables for all robots in the group.
 *
 * Events sent to a group or a robots will be broa{FlexBuffers} from dcasted to all robots
 * in that group.
 *
 */
export interface IGroup extends IBasicNode {

    /**
     * Shared variables registered on this group
     * @see [[setVariables]]
     */
    readonly variables : Variables;

    /**
     * List of events registered on this group
     */
    readonly eventsDescriptions : EventDescription[];

     /**
     * All nodes registed on this group
     */
    readonly nodes : Node[];

    /**
     * @event
     * Emitted each time the list of registered events changes
     * @see [[watchSharedVariablesAndEvents]]
     */
    onEventsDescriptionsChanged : (events: EventDescription[]) => void;
    /**
     * @event
     * Emitted each time the list of registered variables changes.
     * @see [[watchSharedVariablesAndEvents]]
     */
    onVariablesChanged : (variables: Variables) => void;


    /**
     * Set the shared variables registered on the group.
     *
     * @param variables variables to set. Will erase any previous
     * registered variables.
     *
     * This client must holds a lock on at least one node of this group
     * for the operation to be sucessful
     *
     * @see [[INode.lock]]
     *
     */
    setVariables(variables : Variables) : Promise<any>;

    /**
     * Set the events registered on the group.
     *
     * @param events variables to set. Will erase any previously
     * registered events.
     *
     * This client must holds a lock on at least one node of this group
     * for the operation to be sucessful
     *
     * @see [[INode.lock]]
     *
     */
    setEventsDescriptions(events : EventDescription[])  : Promise<any>;

}

/**
 * @private
 */
class Group extends BasicNode implements IGroup{

    _variables:any;
    _events:any;
    private _on_variables_changed: (variables: Variables) => void;
    private _on_events_descriptions_changed: (events: EventDescription[]) => void;

    constructor(client : Client, id: NodeId) {
        super(client, id)
        this._variables = null;
        this._events = null;
        this._on_variables_changed = undefined;
        this._on_events_descriptions_changed = undefined;
    }

    get variables() {
        return this._variables;
    }

    setVariables(variables: Variables) {
        return this._client._set_variables(this._id, variables);
    }

    get eventsDescriptions() {
        return this._events;
    }

    setEventsDescriptions(events: any) {
        return this._client._set_events_descriptions(this._id, events)
    }

    get nodes() {
        return this._client._nodes_from_id(this.id)
    }

    get onEventsDescriptionsChanged() {
        return this._on_events_descriptions_changed
    }

    set onEventsDescriptionsChanged(cb) {
        this._on_events_descriptions_changed = cb
    }


    get onVariablesChanged() {
        return this._on_variables_changed
    }

    set onVariablesChanged(cb) {
        this._on_variables_changed = cb
    }
}

export import NodeStatus = mobsya.fb.NodeStatus;
export import NodeType = mobsya.fb.NodeType;
export import ProgrammingLanguage = mobsya.fb.ProgrammingLanguage;
export import VMExecutionState = mobsya.fb.VMExecutionState;


/**
 * A node represents a physical or virtual Thymio
 */
export interface INode extends IBasicNode {

    /**
     * Group to which this robot belongs
     *
     */
    readonly group  : Group;

    readonly type   : NodeType;

    /**
     * Status of the robot
     */
    readonly status : NodeStatus;

    /**
     * Name of the robot
     */
    readonly name   : string;

    /**
     * A string representaion of the type of the robot
     * see [[type]]
     */
    readonly statusAsString : string;

    /**
     * A string representaion of the status of the robot
     * see [[type]]
     */
    readonly typeAsString : string

    /**
     * Shorthand for status == NodeStatus.Ready,
     * see [[type]]
     */
    readonly isReady  : boolean;

    /**
     * Known events
     *
     * @see [[IGroup.variable]]
     */
    readonly sharedVariables : Variables;

    /**
     * Known events
     *
     * @see [[IGroup.eventsDescriptions]]
     */
    readonly eventsDescriptions : EventDescription[]

    /**
     * @event
     */
    onVmExecutionStateChanged : (...args: any) => void;

    /**
     * Emitted when the status of the robot changes
     * @event
     */
    onStatusChanged: (newStatus : NodeStatus) => void;

    /**
     * Emitted when the name of the robot changes
     * @see [[rename]], [[name]]
     * @event
     */
    onNameChanged: (newName : string) => void;

    /**
     * Notify when one or several events are emmited
     * by the robot.
     *
     * @event
     */
    onEvents: (events : Events) => void;

    /**
     * Notify changes in the event table of the group associated to this
     * robot
     *
     * @event
     * @see [[IGroup.onEventsDescriptionsChanged]]
     */
    onEventsDescriptionsChanged: (events : EventDescription[]) => void;

    /**
     * Notify of shared variables changes in the group associated to that
     * robot
     *
     * @event
     * @see [[IGroup.onVariablesChanged]]
     */
    onSharedVariablesChanged: (sharedVariables : Variables) => void;
    /**
     * Notifiy of robot variables changes.
     * These are both the variables of the robot and the variables defined
     * in the currently executing programm.
     * @event
     */
    onVariablesChanged: (variables : Variables) => void;


    /**
     * Notifiy of scratchpad changes.
     * @event
     */
    onScratchpadChanged: (text: string, language: mobsya.fb.ProgrammingLanguage) => void;


    /**
     * Emitted when the robot is associated to another group
     * When a group change happens, it is the responsability of
     * the application to rewire the group-monitoring callbacks
     * such as [[onSharedVariablesChanged]] and [[onEventsDescriptionsChanged]].
     *
     * [[watchSharedVariablesAndEvents]] may also need to be reconfigured after a group
     * change.
     *
     * @event
     */
    onGroupChanged: (group: Group) => void;


     /** Lock the device
     *  Locking a device is akin to take sole ownership of it until the connection
     *  is closed or the unlock method is explicitely called
     *
     *  The device must be in the available state before it can be locked.
     *  Once a device is locked, all client will see the device becoming busy.
     *
     *  If the device can not be locked, an [[mobsya.fb.Error]] is raised.
     *
     * @throws [[mobsya.fb.Error]]
     */
    lock() : Promise<any> ;

    /** Unlock the device
     * Once a device is unlocked, all client will see the device becoming available.
     * Once unlock, a device can't be written to until loc
     * @throws [[mobsya.fb.Error]]
     *
     * @see [[lock]]
     */
    unlock() : Promise<any> ;

    /**var ws = null

if (typeof WebSocket !== 'undefined') {
  ws = WebSocket
} else if (typeof MozWebSocket !== 'undefined') {
  ws = MozWebSocket
} else if (typeof global !== 'undefined') {
  ws = global.WebSocket || global.MozWebSocket
} else if (typeof window !== 'undefined') {
  ws = window.WebSocket || window.MozWebSocket
} else if (typeof self !== 'undefined') {
  ws = self.WebSocket || self.MozWebSocket
}
     * Send a request to rename a node
     * @throws [[mobsya.fb.Error]]
     *
     */
    rename(newName : string) : Promise<any>;


    /** Get the description from the device
     *  The device must be in the available state before requesting the VM.
     *
     * @throws [[mobsya.fb.Error]]
     *
     * @see [[lock]]
     */
    asebaVMDescription() : Promise<any>



    /** Load an aseba program on the VM
     * The device must be locked & ready before calling this function
     * The code is not directly executed. instead, {runProgram} must be call to start the execution
     * @param code - the aseba code to load
     *
     * @throws [[mobsya.fb.Error]]
     *
     * @see [[lock]]
     */
    sendAsebaProgram(code : string) : Promise<any>;

    /** Run the code currently loaded on the vm
     * The device must be locked & ready before calling this function
     *
     * @throws [[mobsya.fb.Error]]
     * @see [[lock]]
     */
    runProgram() : Promise<any> ;

    /** Flash the code currently loaded on the vm to the flash memory
     * The device must be locked & ready before calling this function
     *
     * @throws [[mobsya.fb.Error]]
     * @see [[lock]]
     */
    flashProgram() : Promise<any> ;

    /**
     * Set the values of the specified variables.
     * Unlike [[setSharedVariables]], existing variables
     * not modifieed by this function are left unmodified
     *
     * @param variables the variables to modify
     */
    setVariables(variables : Variables) : Promise<any> ;

    /**
     * Set the shared variables on the group associated with this robot
     *
     * Overwrite all existing shared variables
     * the node must be locked.
     *
     * @see [[lock]]
     *
     */
    setSharedVariables(variables : Variables) : Promise<any>

     /**
     * Set the event descriptions on the group associated with this robot
     *
     * Overwrite all existing events descriptions
     * the node must be locked.
     *
     * @see [[lock]]
     *
     */
    setEventsDescriptions(events : EventDescription[]) : Promise<any>


    /**
     * Set the content of the scratchpad associated with this node
     *
     * The scratchpad is shared accross applications and persistent
     * across reconnections.
     */
    setScratchPad(text : string, language: mobsya.fb.ProgrammingLanguage) : Promise<any>
}


/**
 * @private
 */
export class Node extends BasicNode implements INode {


    Status = mobsya.fb.NodeStatus;
    VMExecutionState = mobsya.fb.VMExecutionState;
    Type = mobsya.fb.NodeType;

    private _status: NodeStatus;
    private _type: NodeType;
    private _desc: AsebaVMDescription = null;
    _name: string;
    private _on_vars_changed_cb: (variables: Variables) => void;
    private _on_events_cb: (events: Events) => void;
    private _group: Group;
    onGroupChanged: (group: Group) => void;
    onVmExecutionStateChanged : (...args: any) => void;
    onStatusChanged: (newStatus : NodeStatus) => void;
    onNameChanged: (newStatus : string) => void;
    onScratchpadChanged: (text: string, language: mobsya.fb.ProgrammingLanguage) => void;

    constructor(client : Client, id : NodeId, status : NodeStatus, type : NodeType) {
        super(client, id)
        this._status = status;
        this._type = type
        this._group = null
        this.onGroupChanged = undefined;
        this.onVmExecutionStateChanged = undefined
    }

    get group() {
        return this._group
    }

    /** return the node type*/
    get type() {
        return this._type
    }

    /** The node status
     *  @type {mobsya.fb.NodeStatus}
     */
    get status() {
        return this._status
    }

    /** The node name
     *  @type {mobsya.fb.NodeStatus}
     */
    get name() {
        return this._name
    }


    rename(new_name : string) {
        return this._client._rename_node(this._id, new_name)

    }

    /** Whether the node is ready (connected, and locked)
     *  @type {boolean}
     */
    get isReady() {
        return this._status == mobsya.fb.NodeStatus.ready
    }

    /** The node status converted to string.
     *  @type {string}
     */
    get statusAsString() {
        switch(this.status) {
            case mobsya.fb.NodeStatus.connected: return "connected"
            case mobsya.fb.NodeStatus.ready: return "ready"
            case mobsya.fb.NodeStatus.available: return "available"
            case mobsya.fb.NodeStatus.busy: return "busy"
            case mobsya.fb.NodeStatus.disconnected: return "disconnected"
        }
        return "unknown"
    }

    /** The node type converted to string.
     *  @type {string}
     */
    get typeAsString() {
        switch(this.type) {
            case mobsya.fb.NodeType.Thymio2: return "Thymio 2"
            case mobsya.fb.NodeType.Thymio2Wireless: return "Thymio Wireless"
            case mobsya.fb.NodeType.SimulatedThymio2: return "Simulated Thymio 2"
            case mobsya.fb.NodeType.DummyNode: return "Dummy Node"
        }
        return "unknown"
    }
    lock() {
        return this._client._lock_node(this._id)
    }

    unlock() {
        return this._client._unlock_node(this._id)
    }

    asebaVMDescription() {
        return this._client._request_aseba_vm_description(this._id);
    }

    get sharedVariables() {
        return this.group.variables
    }

    get eventsDescriptions() {
        return this.group.eventsDescriptions
    }

    sendAsebaProgram(code : string) {
        return this._client._send_program(this._id, code, mobsya.fb.ProgrammingLanguage.Aseba);
    }

    /** Load an aesl program on the VM
     *  The device must be locked & ready before calling this function
     *  @param {external:String} code - the aseba code to load
     *  @throws {mobsya.fb.Error}
     *  @see lock
     */
    send_aesl_program(code) {
        return this._client._send_program(this._id, code, mobsya.fb.ProgrammingLanguage.Aesl);
    }

    runProgram() {
        return this._client._set_vm_execution_state(this._id, mobsya.fb.VMExecutionStateCommand.Run);
    }

    flashProgram() {
        return this._client._set_vm_execution_state(this._id,
			mobsya.fb.VMExecutionStateCommand.WriteProgramToDeviceMemory);
    }

    setVariables(map) {
        return this._client._set_variables(this._id, map);
    }

    setSharedVariables(variables) {
        return this._group.setVariables(variables)
    }

    setEventsDescriptions(events) {
        return this._client._set_events_descriptions(this._id, events)
    }

    setScratchPad(text, language) {
        return this._client._set_scratchpad(this._id, text, language)
    }


    get onVariablesChanged() {
        return this._on_vars_changed_cb;
    }

    set onVariablesChanged(cb) {
        this._set_monitoring_flags(mobsya.fb.WatchableInfo.Variables, !!cb)
        this._on_vars_changed_cb = cb;
    }

    get onEvents() {
        return this._on_events_cb;
    }

    set onEvents(cb) {
        this._set_monitoring_flags(mobsya.fb.WatchableInfo.Events, !!cb)
        this._on_events_cb = cb;
    }


    get onEventsDescriptionsChanged() {
        return this._group.onEventsDescriptionsChanged
    }

    set onEventsDescriptionsChanged(cb) {
        this._group.onEventsDescriptionsChanged = cb
    }


    get onSharedVariablesChanged() {
        return this._group.onVariablesChanged
    }

    set onSharedVariablesChanged(cb) {
        this._group.onVariablesChanged = cb
    }

    _set_status(status: NodeStatus) {
        if(status != this._status) {
            this._status = status
            if(this.onStatusChanged) {
                this.onStatusChanged(status)
            }
        }
    }

    _set_name(name : string) {
        if(name != this._name) {
            this._name = name
            if(this._name && this.onNameChanged) {
                this.onNameChanged(name)
            }
        }
    }

    _set_group(group : Group) {
        if(group != this._group) {
            this._group = group
            if(this._group && this.onGroupChanged) {
                this.onGroupChanged(group)
            }
        }
    }
}


/**
 *  This interface represents the connection with a remote
 *  Thymio Device Manager or Thymio 3
 *
 *  Once the connection established nodes connections and
 *  modification events will be broadcast through
 *  onNodesChanged.
 *
 *  When the connection is first establised,
 *  onNodesChanged will be called with the list of all known
 *  nodes.
 *
 * @see  [[createClient]]
 *
 *
 */
export interface IClient {
    /**
     * List of connected nodes
     */
    readonly nodes: INode[];
    /**
     * @param nodes Nodes whose status has changed
     * @event
     */
    onNodesChanged: (nodes: INode[]) => void;


    /**
     * Fired when the websocket is closed
     * @param event
     * @event
     */
    onClose: (event: CloseEvent) => void;
}

/**
 * @private
 */
class Client implements IClient {


    private _requests: Map<number, Request>;
    private _nodes:  Map<string, Node>;
    private _flex:   FlexBuffers;
    private _socket: WebSocket;

    onNodesChanged: (nodes: Node[]) => void = undefined;
    onClose: (event: CloseEvent) => void = undefined;

    /**
     *  @param {external:String} url : Web socket address
     *  @see lock
     */
    constructor(url: string) {
        //In progress requests (id : node)
        this._requests = new Map();
        //Known nodes (id : node)
        this._nodes    = new Map();
        this._flex = new FlexBuffers();
        this._flex.onRuntimeInitialized = () => {
            this._socket = new WebSocket(url)
            this._socket.binaryType = 'arraybuffer';
            this._socket.onopen = this._onopen.bind(this)
            this._socket.onmessage = this._onmessage.bind(this)
            this._socket.onclose = this._onclose.bind(this)
        }
    }


    get nodes() {
        return Array.from(this._nodes.values());
    }

    private _onopen() {
        console.log("connected, sending protocol version")
        const builder = new flatbuffers.Builder();
        mobsya.fb.ConnectionHandshake.startConnectionHandshake(builder)
        mobsya.fb.ConnectionHandshake.addProtocolVersion(builder, PROTOCOL_VERSION)
        mobsya.fb.ConnectionHandshake.addMinProtocolVersion(builder, MIN_PROTOCOL_VERSION)
        this._wrap_message_and_send(builder, mobsya.fb.ConnectionHandshake.endConnectionHandshake(builder), mobsya.fb.AnyMessage.ConnectionHandshake)
    }

    private _onclose(event: CloseEvent) {
        console.log("disconnected")
        if(this.onClose) {
            this.onClose(event);
        }
    }

    private _onmessage (event: MessageEvent) {
        let data = new Uint8Array(event.data)
        let buf  = new flatbuffers.ByteBuffer(data);

        let message = mobsya.fb.Message.getRootAsMessage(buf, null)
        switch(message.messageType()) {
            case mobsya.fb.AnyMessage.ConnectionHandshake: {
                const hs = message.message(new mobsya.fb.ConnectionHandshake())
                console.log(`Handshake complete: Protocol version ${hs.protocolVersion()}`)
                break;
            }
            case mobsya.fb.AnyMessage.NodesChanged: {
                if(this.onNodesChanged)
                    this.onNodesChanged(this._nodes_changed_as_node_list(message.message(new mobsya.fb.NodesChanged())))
                break;
            }
            case mobsya.fb.AnyMessage.NodeAsebaVMDescription: {
                let msg = message.message(new mobsya.fb.NodeAsebaVMDescription())
                let req = this._get_request(msg.requestId())
                const id = this._id(msg.nodeId())
                if(req) {
                    req._trigger_then(id, this._unserialize_aseba_vm_description(msg))
                }
                break;
            }
            case mobsya.fb.AnyMessage.RequestCompleted: {
                let msg = message.message(new mobsya.fb.RequestCompleted())
                let req = this._get_request(msg.requestId())
                if(req) {
                    req._trigger_then()
                }
                break;
            }
            case mobsya.fb.AnyMessage.Error: {
                let msg = message.message(new mobsya.fb.Error())
                let req = this._get_request(msg.requestId())
                if(req) {
                    req._trigger_error(msg.error())
                }
                break
            }
            case mobsya.fb.AnyMessage.CompilationResultSuccess: {
                let msg = message.message(new mobsya.fb.CompilationResultSuccess())
                let req = this._get_request(msg.requestId())
                if(req) {
                    req._trigger_then()
                }
                break
            }
            case mobsya.fb.AnyMessage.CompilationResultFailure: {
                let msg = message.message(new mobsya.fb.CompilationResultFailure())
                let req = this._get_request(msg.requestId())
                if(req) {
                    //TODO
                    req._trigger_error("Compilation error")
                }
                break
            }
            case mobsya.fb.AnyMessage.VariablesChanged: {
                const msg = message.message(new mobsya.fb.VariablesChanged())
                const id = this._id(msg.nodeId())
                const nodes = this._nodes_from_id(id)
                if(nodes.length == 0)
                    break;
                const vars = new Map<string, any>();
                for(let i = 0; i < msg.varsLength(); i++) {
                    const v = msg.vars(i);
                    const myarray = Uint8Array.from(v.valueArray())
                    let val = this._flex.toJSObject(myarray)
                    if(!isNaN(val)) {
                        val = new Number(val)
                    }
                    vars.set(v.name(), val)
                }
                nodes.forEach(node => {
                    if(isEqual(node.id, id) && node.onVariablesChanged) {
                        node.onVariablesChanged(vars)
                    }
                    else {
                        node.group._variables = vars
                        if(node.group.onVariablesChanged) {
                            node.group.onVariablesChanged(vars);
                        }
                    }
                })
                break
            }

            case mobsya.fb.AnyMessage.EventsEmitted: {
                const msg = message.message(new mobsya.fb.EventsEmitted())
                const id = this._id(msg.nodeId())
                const node = this._nodes.get(id.toString())
                if(!node || !node.onEvents)
                    break;
                const vars = new Map<string, any>();
                for(let i = 0; i < msg.eventsLength(); i++) {
                    const v = msg.events(i);
                    const myarray = Uint8Array.from(v.valueArray())
                    const val = this._flex.toJSObject(myarray)
                    vars.set(v.name(), val)
                }
                node.onEvents(vars)
                break
            }

            case mobsya.fb.AnyMessage.EventsDescriptionsChanged: {
                const msg = message.message(new mobsya.fb.EventsDescriptionsChanged())
                const id = this._id(msg.nodeOrGroupId())
                const group = this._group_from_id(id)
                if(!group)
                    break;
                const events = [];
                for(let i = 0; i < msg.eventsLength(); i++) {
                    const v = msg.events(i);
                    let  e = new EventDescription
                    e.name = v.name()
                    e.fixed_size = v.fixedSized();
                    events.push(e);
                }
                group._events = events
                if(group.onEventsDescriptionsChanged)
                    group.onEventsDescriptionsChanged(events)
                break;
            }

            case mobsya.fb.AnyMessage.VMExecutionStateChanged: {
                const msg = message.message(new mobsya.fb.VMExecutionStateChanged())
                const id = this._id(msg.nodeId())
                const node = this._nodes.get(id.toString())
                if(!node || !node.onVmExecutionStateChanged)
                    break;
                node.onVmExecutionStateChanged(msg.state(), msg.line(), msg.error(), msg.errorMsg())
                break
            }

            case mobsya.fb.AnyMessage.ScratchpadUpdate: {
                const msg = message.message(new mobsya.fb.ScratchpadUpdate())
                const id = this._id(msg.nodeId())
                const node = this._nodes.get(id.toString())
                if(!node || !node.onScratchpadChanged)
                    break;
                node.onScratchpadChanged(msg.text(), msg.language())
                break
            }
        }
    }

    _request_aseba_vm_description(id : NodeId) {
        const builder = new flatbuffers.Builder();
        const req_id  = this._gen_request_id()
        const nodeOffset = this._create_node_id(builder, id)

        mobsya.fb.RequestNodeAsebaVMDescription.startRequestNodeAsebaVMDescription(builder)
        mobsya.fb.RequestNodeAsebaVMDescription.addRequestId(builder, req_id)
        mobsya.fb.RequestNodeAsebaVMDescription.addNodeId(builder, nodeOffset)
        const offset = mobsya.fb.RequestNodeAsebaVMDescription.endRequestNodeAsebaVMDescription(builder)
        this._wrap_message_and_send(builder, offset, mobsya.fb.AnyMessage.RequestNodeAsebaVMDescription)
        return this._prepare_request(req_id)
    }

    _send_program(id : NodeId, code : string, language : mobsya.fb.ProgrammingLanguage) {
        const builder = new flatbuffers.Builder();
        const req_id  = this._gen_request_id()
        const codeOffset = builder.createString(code)
        const nodeOffset = this._create_node_id(builder, id)
        mobsya.fb.CompileAndLoadCodeOnVM.startCompileAndLoadCodeOnVM(builder)
        mobsya.fb.CompileAndLoadCodeOnVM.addRequestId(builder, req_id)
        mobsya.fb.CompileAndLoadCodeOnVM.addNodeId(builder, nodeOffset)
        mobsya.fb.CompileAndLoadCodeOnVM.addProgram(builder, codeOffset)
        mobsya.fb.CompileAndLoadCodeOnVM.addLanguage(builder, language)
        mobsya.fb.CompileAndLoadCodeOnVM.addOptions(builder, mobsya.fb.CompilationOptions.LoadOnTarget)
        const offset = mobsya.fb.CompileAndLoadCodeOnVM.endCompileAndLoadCodeOnVM(builder)
        this._wrap_message_and_send(builder, offset, mobsya.fb.AnyMessage.CompileAndLoadCodeOnVM)
        return this._prepare_request(req_id)
    }

    _set_vm_execution_state(id : NodeId, command : mobsya.fb.VMExecutionStateCommand) {
        let builder = new flatbuffers.Builder();
        let req_id  = this._gen_request_id()
        const nodeOffset = this._create_node_id(builder, id)
        mobsya.fb.SetVMExecutionState.startSetVMExecutionState(builder)
        mobsya.fb.SetVMExecutionState.addRequestId(builder, req_id)
        mobsya.fb.SetVMExecutionState.addNodeId(builder, nodeOffset)
        mobsya.fb.SetVMExecutionState.addCommand(builder, command)
        const offset = mobsya.fb.SetVMExecutionState.endSetVMExecutionState(builder)
        this._wrap_message_and_send(builder, offset, mobsya.fb.AnyMessage.SetVMExecutionState)
        return this._prepare_request(req_id)
    }

    //TODO : check variable types, etc
    _set_variables(id : NodeId, variables : Variables) {
        let builder = new flatbuffers.Builder();
        let req_id  = this._gen_request_id()
        const nodeOffset = this._create_node_id(builder, id)
        const varsOffset = this._serialize_variables(builder, variables)
        mobsya.fb.SetVariables.startSetVariables(builder)
        mobsya.fb.SetVariables.addNodeOrGroupId(builder, nodeOffset)
        mobsya.fb.SetVariables.addRequestId(builder, req_id)
        mobsya.fb.SetVariables.addVars(builder, varsOffset)
        const tableOffset = mobsya.fb.SetVariables.endSetVariables(builder)
        this._wrap_message_and_send(builder, tableOffset, mobsya.fb.AnyMessage.SetVariables)
        return this._prepare_request(req_id)
    }

    private _serialize_variables(builder : flatbuffers.Builder, variables : Variables) {
        const offsets = []
        if (!(variables instanceof Map)) {
            variables = new Map(Object.entries(variables))
        }

        variables.forEach( (value, name, _map) => {
            offsets.push(this._serialize_variable(builder, name, value))
        })
        return mobsya.fb.SetVariables.createVarsVector(builder, offsets)
    }

    private _serialize_variable(builder : flatbuffers.Builder, name : string, value : any) {
        const nameOffset = builder.createString(name)
        const buffer = this._flex.fromJSObject(value)
        const bufferOffset = mobsya.fb.NodeVariable.createValueVector(builder, buffer)
        mobsya.fb.NodeVariable.startNodeVariable(builder)
        mobsya.fb.NodeVariable.addName(builder, nameOffset)
        mobsya.fb.NodeVariable.addValue(builder, bufferOffset)
        return mobsya.fb.NodeVariable.endNodeVariable(builder)
    }

    _emit_events(id : NodeId, variables : Variables) {
        let builder = new flatbuffers.Builder();
        let req_id  = this._gen_request_id()
        const nodeOffset = this._create_node_id(builder, id)
        const varsOffset = this._serialize_variables(builder, variables)
        mobsya.fb.SendEvents.startSendEvents(builder)
        mobsya.fb.SendEvents.addNodeId(builder, nodeOffset)
        mobsya.fb.SendEvents.addRequestId(builder, req_id)
        mobsya.fb.SendEvents.addEvents(builder, varsOffset)
        const tableOffset = mobsya.fb.SendEvents.endSendEvents(builder)
        this._wrap_message_and_send(builder, tableOffset, mobsya.fb.AnyMessage.SendEvents)
        return this._prepare_request(req_id)
    }

    _set_events_descriptions(id : NodeId,  events : EventDescription[]) {
        let builder = new flatbuffers.Builder();
        let req_id  = this._gen_request_id()
        const nodeOffset = this._create_node_id(builder, id)
        const eventsOffset = this._serialize_events_descriptions(builder, events)
        mobsya.fb.RegisterEvents.startRegisterEvents(builder)
        mobsya.fb.RegisterEvents.addNodeOrGroupId(builder, nodeOffset)
        mobsya.fb.RegisterEvents.addRequestId(builder, req_id)
        mobsya.fb.RegisterEvents.addEvents(builder, eventsOffset)
        const tableOffset = mobsya.fb.RegisterEvents.endRegisterEvents(builder)
        this._wrap_message_and_send(builder, tableOffset, mobsya.fb.AnyMessage.RegisterEvents)
        return this._prepare_request(req_id)
    }


    private _serialize_events_descriptions(builder : flatbuffers.Builder, events : EventDescription[]) {
        const offsets = []
        events.forEach( (event) => {
            offsets.push(this._serialize_event_description(builder, event.name, event.fixed_size))
        })
        return mobsya.fb.RegisterEvents.createEventsVector(builder, offsets)
    }


    private _serialize_event_description(builder : flatbuffers.Builder, name : string, fixed_size : number) {
        const nameOffset = builder.createString(name)
        mobsya.fb.EventDescription.startEventDescription(builder)
        mobsya.fb.EventDescription.addName(builder, nameOffset)
        mobsya.fb.EventDescription.addFixedSized(builder, fixed_size)
        return mobsya.fb.EventDescription.endEventDescription(builder)
    }

    _set_scratchpad(id : NodeId, text: string, language: mobsya.fb.ProgrammingLanguage) {
        const builder = new flatbuffers.Builder();
        const req_id  = this._gen_request_id()
        const nodeOffset = this._create_node_id(builder, id)
        const textOffset = builder.createString(text)
        mobsya.fb.ScratchpadUpdate.startScratchpadUpdate(builder)
        mobsya.fb.ScratchpadUpdate.addRequestId(builder, req_id)
        mobsya.fb.ScratchpadUpdate.addNodeId(builder, nodeOffset)
        mobsya.fb.ScratchpadUpdate.addScratchpadId(builder, nodeOffset)
        mobsya.fb.ScratchpadUpdate.addText(builder, textOffset)
        mobsya.fb.ScratchpadUpdate.addLanguage(builder, language)
        let offset = mobsya.fb.ScratchpadUpdate.endScratchpadUpdate(builder)
        this._wrap_message_and_send(builder, offset, mobsya.fb.AnyMessage.ScratchpadUpdate)
        return this._prepare_request(req_id)
    }

    /* request the description of the aseba vm for the node with the given id */
    _lock_node(id : NodeId) {
        let builder = new flatbuffers.Builder();
        let req_id  = this._gen_request_id()
        const nodeOffset = this._create_node_id(builder, id)

        mobsya.fb.LockNode.startLockNode(builder)
        mobsya.fb.LockNode.addRequestId(builder, req_id)
        mobsya.fb.LockNode.addNodeId(builder, nodeOffset)
        let offset = mobsya.fb.LockNode.endLockNode(builder)
        this._wrap_message_and_send(builder, offset, mobsya.fb.AnyMessage.LockNode)
        return this._prepare_request(req_id)
    }

    _unlock_node(id : NodeId) {
        let builder = new flatbuffers.Builder();
        let req_id  = this._gen_request_id()
        const nodeOffset = this._create_node_id(builder, id)

        mobsya.fb.UnlockNode.startUnlockNode(builder)
        mobsya.fb.UnlockNode.addRequestId(builder, req_id)
        mobsya.fb.UnlockNode.addNodeId(builder, nodeOffset)
        let offset = mobsya.fb.UnlockNode.endUnlockNode(builder)
        this._wrap_message_and_send(builder, offset, mobsya.fb.AnyMessage.UnlockNode)
        return this._prepare_request(req_id)
    }


    _rename_node(id : NodeId, name : string) {
        let builder = new flatbuffers.Builder();
        let req_id  = this._gen_request_id()
        const nodeOffset = this._create_node_id(builder, id)
        const nameOffset = builder.createString(name);

        mobsya.fb.RenameNode.startRenameNode(builder)
        mobsya.fb.RenameNode.addRequestId(builder, req_id)
        mobsya.fb.RenameNode.addNodeId(builder, nodeOffset)
        mobsya.fb.RenameNode.addNewName(builder, nameOffset)
        let offset = mobsya.fb.RenameNode.endRenameNode(builder)
        this._wrap_message_and_send(builder, offset, mobsya.fb.AnyMessage.RenameNode)
        return this._prepare_request(req_id)
    }

    _watch(id : NodeId, monitoring_flags : number) {
        let builder = new flatbuffers.Builder();
        let req_id  = this._gen_request_id()
        const nodeOffset = this._create_node_id(builder, id)
        mobsya.fb.WatchNode.startWatchNode(builder)
        mobsya.fb.WatchNode.addRequestId(builder, req_id)
        mobsya.fb.WatchNode.addNodeOrGroupId(builder, nodeOffset)
        mobsya.fb.WatchNode.addInfoType(builder, monitoring_flags)
        let offset = mobsya.fb.WatchNode.endWatchNode(builder)
        this._wrap_message_and_send(builder, offset, mobsya.fb.AnyMessage.WatchNode)
        return this._prepare_request(req_id)
    }

    private _nodes_changed_as_node_list(msg: mobsya.fb.NodesChanged) {
        let nodes = []
        for(let i = 0; i < msg.nodesLength(); i++) {
            const n = msg.nodes(i);
            const id = this._id(n.nodeId())
            const group_id = this._id(n.groupId())
            let node = this._nodes.get(id.toString())
            if(!node) {
                node = new Node(this, id, n.status(), n.type())
                node._name = n.name()
                this._nodes.set(id.toString(), node)
            }
            if(n.status() == NodeStatus.disconnected) {
                this._nodes.delete(id.toString())
            }
            nodes.push(node)
            node._set_status(n.status())
            node._set_name(n.name())
            node._set_group(this._group_from_id(group_id))
        }
        return nodes
    }

    _nodes_from_id(id : NodeId) {
        return Array.from(this._nodes.values()).filter(node => isEqual(node.id, id) || (node.group && isEqual(node.group.id, id)));
    }

    _group_from_id(id : NodeId) {
        let node =  Array.from(this._nodes.values()).find(node => node.group && isEqual(node.group.id, id));
        if(node && node.group)
            return node.group
        return new Group(this, id)
    }

    private _id(fb_id : mobsya.fb.NodeId) {
        return fb_id ? new NodeId(fb_id.idArray()) : null
    }

    private _unserialize_aseba_vm_description(msg : mobsya.fb.NodeAsebaVMDescription) {
        let desc = new AsebaVMDescription()
        desc.bytecode_size = msg.bytecodeSize()
        desc.data_size  = msg.dataSize()
        desc.stack_size = msg.stackSize()

        for(let i = 0; i < msg.variablesLength(); i++) {
            const v = msg.variables(i);
            desc.variables.push({"name" : v.name(), "size" : v.size()})
        }

        for(let i = 0; i < msg.eventsLength(); i++) {
            const v = msg.events(i);
            desc.events.push({"name" : v.name(), "description" : v.description()})
        }

        for(let i = 0; i < msg.functionsLength(); i++) {
            const v = msg.functions(i);
            let params = []
            for(let j = 0; j < v.parametersLength(); j++) {
                const p = v.parameters(i);
                params.push({"name" : v.name(), "size" : p.size()})
            }
            desc.functions.push({"name" : v.name(), "description" : v.description(), "params": params})
        }
        return desc
    }

    private _create_node_id(builder : flatbuffers.Builder, id : NodeId) {
        const offset = mobsya.fb.NodeId.createIdVector(builder, id.data);
        mobsya.fb.NodeId.startNodeId(builder)
        mobsya.fb.NodeId.addId(builder, offset)
        return mobsya.fb.NodeId.endNodeId(builder)
    }

    private _wrap_message_and_send(builder : flatbuffers.Builder, offset : flatbuffers.Offset, type : number) {
        mobsya.fb.Message.startMessage(builder)
        mobsya.fb.Message.addMessageType(builder, type)
        mobsya.fb.Message.addMessage(builder, offset)
        const builtMsg = mobsya.fb.Message.endMessage(builder)
        builder.finish(builtMsg);
        this._socket.send(builder.asUint8Array())
    }

    private _gen_request_id() {
        let n = 0
        do {
            n = Math.floor(Math.random() * (0xffffffff - 2)) + 1
        }while(this._requests.has(n))
        return n
    }

    private _get_request(id : number) {
        const req = this._requests.get(id)
        if(req != undefined)
            this._requests.delete(id)
            if(req == undefined) {
                console.error(`unknown request ${id}`)
            }
            return req
    }
    private _prepare_request(req_id : number) {
        let req = new Request(req_id)
        this._requests.set(req_id, req)
        return req._promise
    }
}

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
export function createClient(url : string) : IClient {
    return new Client(url)
}
