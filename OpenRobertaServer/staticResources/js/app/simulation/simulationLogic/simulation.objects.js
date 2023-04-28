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
var __spreadArray = (this && this.__spreadArray) || function (to, from, pack) {
    if (pack || arguments.length === 2) for (var i = 0, l = from.length, ar; i < l; i++) {
        if (ar || !(i in from)) {
            if (!ar) ar = Array.prototype.slice.call(from, 0, i);
            ar[i] = from[i];
        }
    }
    return to.concat(ar || Array.prototype.slice.call(from));
};
define(["require", "exports", "jquery", "util", "simulation.roberta", "simulation.math"], function (require, exports, $, UTIL, simulation_roberta_1, SIMATH) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.Ground = exports.TriangleSimulationObject = exports.CircleSimulationObject = exports.MarkerSimulationObject = exports.RectangleSimulationObject = exports.SimObjectShape = exports.SimObjectType = exports.SimObjectFactory = exports.BaseSimulationObject = void 0;
    var BaseSimulationObject = /** @class */ (function () {
        function BaseSimulationObject(myId, myScene, mySelectionListener, type, optColor) {
            this.SHIFT = 1;
            this.MIN_SIZE_OBJECT = 10;
            this.isDown = false;
            this.mouseOldX = 0;
            this.mouseOldY = 0;
            this._selected = false;
            this.myId = myId;
            this.myScene = myScene;
            this.mySelectionListener = mySelectionListener;
            this.remover = this.mySelectionListener.add(this.handleNewSelection.bind(this));
            this.type = type;
            if (optColor) {
                this.color = optColor;
            }
            else {
                if (type === SimObjectType.Obstacle) {
                    this.color = '#33B8CA';
                }
                else {
                    this.color = '#FBED00';
                }
            }
            this.addMouseEvents();
        }
        Object.defineProperty(BaseSimulationObject.prototype, "selected", {
            get: function () {
                return this._selected;
            },
            set: function (value) {
                this._selected = value;
                if (value) {
                    this.mySelectionListener.fire(this);
                    if (this.type !== SimObjectType.Marker) {
                        this.myScene.sim.enableChangeObjectButtons();
                    }
                }
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(BaseSimulationObject.prototype, "color", {
            get: function () {
                return this._color;
            },
            set: function (value) {
                this._color = value;
                this._hsv = SIMATH.hexToHsv(value);
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(BaseSimulationObject.prototype, "hsv", {
            get: function () {
                return this._hsv;
            },
            enumerable: false,
            configurable: true
        });
        BaseSimulationObject.prototype.addMouseEvents = function () {
            var $robotLayer = $('#robotLayer');
            $robotLayer.on('mousedown.' + this.myId + ' touchstart.' + this.myId, this.handleMouseDown.bind(this));
            $robotLayer.on('mousemove.' + this.myId + ' touchmove.' + this.myId, this.handleMouseMove.bind(this));
            $robotLayer.on('mouseup.' + this.myId + ' touchend.' + this.myId + 'mouseout.' + this.myId + 'touchcancel.' + this.myId, this.handleMouseOutUp.bind(this));
            $robotLayer.on('keydown.' + this.myId + '', this.handleKeyEvent.bind(this));
        };
        BaseSimulationObject.prototype.removeMouseEvents = function () {
            $('#robotLayer').off('.' + this.myId);
        };
        BaseSimulationObject.prototype.handleNewSelection = function (who) {
            if (who !== this && this.selected) {
                this.selected = false;
                this.isDown = false;
                this.corners.forEach(function (corner) { return (corner.isDown = false); });
                this.myScene.sim.disableChangeObjectButtons();
                this.redraw();
            }
        };
        BaseSimulationObject.prototype.getTolerance = function () {
            return 100;
        };
        BaseSimulationObject.prototype.handleMouseOutUp = function (e) {
            if (this.selected) {
                e.stopImmediatePropagation();
                this.isDown = false;
                this.corners.forEach(function (corner) { return (corner.isDown = false); });
                this.redraw();
            }
        };
        BaseSimulationObject.prototype.destroy = function () {
            this.removeMouseEvents();
            this.selected = false;
            this.mySelectionListener.remove(this.remover);
            this.myScene.sim.disableChangeObjectButtons();
        };
        BaseSimulationObject.prototype.redraw = function () {
            switch (this.type) {
                case SimObjectType.ColorArea:
                    this.myScene.redrawColorAreas = true;
                    break;
                case SimObjectType.Obstacle:
                    this.myScene.redrawObstacles = true;
                    break;
                case SimObjectType.Marker:
                    this.myScene.redrawMarkers = true;
                    break;
                default:
                    break;
            }
        };
        return BaseSimulationObject;
    }());
    exports.BaseSimulationObject = BaseSimulationObject;
    var SimObjectFactory = /** @class */ (function () {
        function SimObjectFactory() {
        }
        SimObjectFactory.getSimObject = function (id, myScene, selectionListener, shape, type, origin, optColor) {
            var params = [];
            for (var _i = 7; _i < arguments.length; _i++) {
                params[_i - 7] = arguments[_i];
            }
            switch (shape) {
                case SimObjectShape.Rectangle: {
                    return new (RectangleSimulationObject.bind.apply(RectangleSimulationObject, __spreadArray([void 0, id, myScene, selectionListener, type, origin, optColor], params, false)))();
                }
                case SimObjectShape.Triangle: {
                    return new (TriangleSimulationObject.bind.apply(TriangleSimulationObject, __spreadArray([void 0, id, myScene, selectionListener, type, origin, optColor], params, false)))();
                }
                case SimObjectShape.Circle: {
                    return new (CircleSimulationObject.bind.apply(CircleSimulationObject, __spreadArray([void 0, id, myScene, selectionListener, type, origin, optColor], params, false)))();
                }
                case SimObjectShape.Marker: {
                    return new MarkerSimulationObject(id, myScene, selectionListener, type, origin);
                }
            }
        };
        SimObjectFactory.copy = function (object) {
            var id = object.myScene.uniqueObjectId;
            if (object instanceof RectangleSimulationObject) {
                return SimObjectFactory.getSimObject.apply(SimObjectFactory, __spreadArray([id,
                    object.myScene,
                    object.mySelectionListener,
                    SimObjectShape.Rectangle,
                    object.type,
                    {
                        x: -1000,
                        y: -1000,
                    },
                    object.color], [object.w, object.h], false));
            }
            else if (object instanceof TriangleSimulationObject) {
                return SimObjectFactory.getSimObject.apply(SimObjectFactory, __spreadArray([id,
                    object.myScene,
                    object.mySelectionListener,
                    SimObjectShape.Triangle,
                    object.type,
                    {
                        x: -1000,
                        y: -1000,
                    },
                    object.color], [object.ax, object.ay, object.bx, object.by, object.cx, object.cy], false));
            }
            else if (object instanceof CircleSimulationObject) {
                return SimObjectFactory.getSimObject.apply(SimObjectFactory, __spreadArray([id,
                    object.myScene,
                    object.mySelectionListener,
                    SimObjectShape.Circle,
                    object.type,
                    {
                        x: -1000,
                        y: -1000,
                    },
                    object.color], [object.r], false));
            }
        };
        return SimObjectFactory;
    }());
    exports.SimObjectFactory = SimObjectFactory;
    var SimObjectType;
    (function (SimObjectType) {
        SimObjectType["Obstacle"] = "OBSTACLE";
        SimObjectType["ColorArea"] = "COLORAREA";
        SimObjectType["Passiv"] = "PASSIV";
        SimObjectType["Marker"] = "MARKER";
    })(SimObjectType = exports.SimObjectType || (exports.SimObjectType = {}));
    var SimObjectShape;
    (function (SimObjectShape) {
        SimObjectShape["Rectangle"] = "RECTANGLE";
        SimObjectShape["Triangle"] = "TRIANGLE";
        SimObjectShape["Circle"] = "CIRCLE";
        SimObjectShape["Marker"] = "MARKER";
    })(SimObjectShape = exports.SimObjectShape || (exports.SimObjectShape = {}));
    var RectangleSimulationObject = /** @class */ (function (_super) {
        __extends(RectangleSimulationObject, _super);
        function RectangleSimulationObject(myId, myScene, mySelectionListener, type, p, optColor) {
            var params = [];
            for (var _i = 6; _i < arguments.length; _i++) {
                params[_i - 6] = arguments[_i];
            }
            var _this = _super.call(this, myId, myScene, mySelectionListener, type, optColor) || this;
            _this.w = 100;
            _this.h = 100;
            _this.theta = 0; // not used
            _this.corners = [];
            _this.x = p.x;
            _this.y = p.y;
            if (params.length == 2) {
                _this.w = params[0];
                _this.h = params[1];
            }
            if (typeof optColor !== 'string') {
                _this._img = optColor;
            }
            _this.updateCorners();
            return _this;
        }
        Object.defineProperty(RectangleSimulationObject.prototype, "img", {
            get: function () {
                return this._img;
            },
            set: function (value) {
                this._img = value;
            },
            enumerable: false,
            configurable: true
        });
        RectangleSimulationObject.prototype.draw = function (ctx, uCtx) {
            ctx.save();
            uCtx.save();
            ctx.fillStyle = this.color;
            switch (this.type) {
                case SimObjectType.Obstacle: {
                    ctx.shadowColor = '#3e3e3e';
                    ctx.shadowOffsetY = 5;
                    ctx.shadowOffsetX = 5;
                    ctx.shadowBlur = 5;
                    if (!this._img) {
                        ctx.fillRect(this.x, this.y, this.w, this.h);
                    }
                    else {
                        ctx.drawImage(this._img, this.x, this.y, this.w, this.h);
                    }
                    break;
                }
                case SimObjectType.ColorArea: {
                    uCtx.fillStyle = this.color;
                    if (this._img) {
                        uCtx.drawImage(this._img, this.x, this.y, this.w, this.h);
                        ctx.drawImage(this._img, this.x, this.y, this.w, this.h);
                    }
                    else {
                        uCtx.fillRect(this.x, this.y, this.w, this.h);
                        ctx.fillRect(this.x, this.y, this.w, this.h);
                    }
                    break;
                }
                default:
                    break;
            }
            if (this.selected) {
                ctx.restore();
                ctx.save();
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'gray';
                ctx.fillStyle = 'black';
                this.corners.forEach(function (corner) {
                    if (corner.isDown) {
                        ctx.fillStyle = 'gray';
                    }
                    else {
                        ctx.fillStyle = 'black';
                    }
                    ctx.beginPath();
                    ctx.arc(corner.x, corner.y, 5, 0, 2 * Math.PI);
                    ctx.stroke();
                    ctx.fill();
                });
            }
            ctx.restore();
            uCtx.restore();
        };
        RectangleSimulationObject.prototype.handleKeyEvent = function (e) {
            if (this.selected) {
                var keyName = e.key;
                switch (keyName) {
                    case 'ArrowUp':
                        this.y -= this.SHIFT;
                        break;
                    case 'ArrowLeft':
                        this.x -= this.SHIFT;
                        break;
                    case 'ArrowDown':
                        this.y += this.SHIFT;
                        break;
                    case 'ArrowRight':
                        this.x += this.SHIFT;
                        break;
                    case 'c':
                        if (e.ctrlKey || e.metaKey) {
                            var id = this.myScene.uniqueObjectId;
                            var shape = SimObjectShape.Rectangle;
                            this.myScene.objectToCopy = SimObjectFactory.copy(this);
                            e.preventDefault(); // Prevent the default copy behavior of Safari, which may beep or otherwise indicate an error due to the lack of a selection. See https://github.com/google/blockly/pull/4925.
                        }
                        break;
                    default:
                    // nothing to do so far
                }
                if (keyName !== 'Delete' && keyName !== 'Backspace') {
                    e.stopImmediatePropagation();
                }
                this.updateCorners();
                $('#robotLayer').attr('tabindex', 0);
                $('#robotLayer').trigger('focus');
            }
        };
        RectangleSimulationObject.prototype.handleMouseDown = function (e) {
            if (e && !e.startX) {
                UTIL.extendMouseEvent(e, simulation_roberta_1.SimulationRoberta.Instance.scale, $('#robotLayer'));
            }
            var myEvent = e;
            this.isDown = this.isMouseOn(myEvent);
            if (this.isDown) {
                e.stopImmediatePropagation();
                this.mouseOldX = myEvent.startX;
                this.mouseOldY = myEvent.startY;
            }
            if (this.isDown && !this.selected) {
                $('#robotLayer').css('cursor', 'pointer');
                this.selected = true;
                //TODO redraw ?
            }
            else if (this.selected) {
                this.corners.forEach(function (corner) {
                    corner.isDown = UTIL.checkInCircle(myEvent.startX, myEvent.startY, corner.x, corner.y, 15);
                });
                var mySelectedCornerIndex = this.corners.map(function (corner) { return corner.isDown; }).indexOf(true);
                if (mySelectedCornerIndex >= 0) {
                    e.stopImmediatePropagation();
                    switch (mySelectedCornerIndex) {
                        case 0:
                        case 2:
                            $('#robotLayer').css('cursor', 'nwse-resize');
                            break;
                        case 1:
                        case 3:
                            $('#robotLayer').css('cursor', 'nesw-resize');
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        RectangleSimulationObject.prototype.handleMouseMove = function (e) {
            if (e && !e.startX) {
                UTIL.extendMouseEvent(e, simulation_roberta_1.SimulationRoberta.Instance.scale, $('#robotLayer'));
            }
            var myEvent = e;
            var dx = myEvent.startX - this.mouseOldX;
            var dy = myEvent.startY - this.mouseOldY;
            this.mouseOldX = myEvent.startX;
            this.mouseOldY = myEvent.startY;
            var mySelectedCornerIndex = this.corners.map(function (corner) { return corner.isDown; }).indexOf(true);
            var onMe = this.isMouseOn(myEvent);
            if (onMe) {
                $('#robotLayer').css('cursor', 'pointer');
                $('#robotLayer').data('hovered', true);
                if (this.selected) {
                    e.stopImmediatePropagation();
                }
            }
            var myOnCornerIndex = -1;
            for (var i = 0; i < this.corners.length; i++) {
                if (UTIL.checkInCircle(myEvent.startX, myEvent.startY, this.corners[i].x, this.corners[i].y, 15)) {
                    myOnCornerIndex = i;
                    break;
                }
            }
            if (myOnCornerIndex > -1 && this.selected) {
                e.stopImmediatePropagation();
                switch (myOnCornerIndex) {
                    case 0:
                    case 2:
                        $('#robotLayer').css('cursor', 'nwse-resize');
                        break;
                    case 1:
                    case 3:
                        $('#robotLayer').css('cursor', 'nesw-resize');
                        break;
                    default:
                        break;
                }
            }
            if (mySelectedCornerIndex >= 0 && this.selected) {
                if (this.w >= this.MIN_SIZE_OBJECT && this.h >= this.MIN_SIZE_OBJECT) {
                    switch (mySelectedCornerIndex) {
                        case 0:
                            this.x += dx;
                            this.y += dy;
                            this.w -= dx;
                            this.h -= dy;
                            break;
                        case 1:
                            this.y += dy;
                            this.w += dx;
                            this.h -= dy;
                            break;
                        case 2:
                            this.w += dx;
                            this.h += dy;
                            break;
                        case 3:
                            this.x += dx;
                            this.w -= dx;
                            this.h += dy;
                            break;
                        default:
                            break;
                    }
                }
                else if (this.w < this.MIN_SIZE_OBJECT) {
                    if (mySelectedCornerIndex == 0 || mySelectedCornerIndex == 3) {
                        this.x -= this.MIN_SIZE_OBJECT - this.w;
                    }
                    this.w = this.MIN_SIZE_OBJECT;
                }
                else if (this.h < this.MIN_SIZE_OBJECT) {
                    if (mySelectedCornerIndex == 1 || mySelectedCornerIndex == 2) {
                        this.y -= this.MIN_SIZE_OBJECT - this.h;
                    }
                    this.h = this.MIN_SIZE_OBJECT;
                }
                this.updateCorners();
            }
            else {
                if (this.isDown) {
                    this.x += dx;
                    this.y += dy;
                    this.updateCorners();
                }
            }
        };
        RectangleSimulationObject.prototype.getLines = function () {
            return UTIL.getLinesFromRectangle(this);
        };
        RectangleSimulationObject.prototype.moveTo = function (p) {
            this.x = p.x - this.w / 2;
            this.y = p.y - this.h / 2;
            this.updateCorners();
        };
        RectangleSimulationObject.prototype.updateCorners = function () {
            if (this.corners.length == 0) {
                this.corners[0] = { x: this.x, y: this.y, isDown: false };
                this.corners[1] = { x: this.x + this.w, y: this.y, isDown: false };
                this.corners[2] = { x: this.x + this.w, y: this.y + this.h, isDown: false };
                this.corners[3] = { x: this.x, y: this.y + this.h, isDown: false };
            }
            else {
                this.corners[0] = { x: this.x, y: this.y, isDown: this.corners[0].isDown };
                this.corners[1] = { x: this.x + this.w, y: this.y, isDown: this.corners[1].isDown };
                this.corners[2] = { x: this.x + this.w, y: this.y + this.h, isDown: this.corners[2].isDown };
                this.corners[3] = { x: this.x, y: this.y + this.h, isDown: this.corners[3].isDown };
            }
            this.redraw();
        };
        RectangleSimulationObject.prototype.isMouseOn = function (myEvent) {
            return myEvent.startX > this.x && myEvent.startX < this.x + this.w && myEvent.startY > this.y && myEvent.startY < this.y + this.h;
        };
        return RectangleSimulationObject;
    }(BaseSimulationObject));
    exports.RectangleSimulationObject = RectangleSimulationObject;
    var MarkerSimulationObject = /** @class */ (function (_super) {
        __extends(MarkerSimulationObject, _super);
        function MarkerSimulationObject(myId, myScene, mySelectionListener, type, p) {
            var _this = _super.call(this, myId, myScene, mySelectionListener, type, p) || this;
            _this.MARKER_OFFSET = 33;
            _this.MARKER_LABEL_OFFSET = 40;
            _this.w = 36;
            _this.h = 36;
            _this.updateCorners();
            return _this;
        }
        MarkerSimulationObject.prototype.draw = function (ctx, uCtx) {
            ctx.save();
            ctx.fillStyle = '#ffffff';
            var border = this.w / 12;
            ctx.fillRect(this.x - border, this.y - border - 1, this.w + 2 * border, this.h + 2 * border);
            ctx.fillStyle = '#000000';
            ctx.fillText(String(this.markerId), this.x + this.MARKER_LABEL_OFFSET, this.y + this.h / 2);
            ctx.font = '' + this.w + 'px typicons';
            ctx.textAlign = 'left';
            ctx.fillText(window
                .getComputedStyle($('.typcn.typcn-' + this.markerId)[0], ':before')
                .content.replace(/"/, '')
                .replace(/"/, ''), this.x, this.y + this.MARKER_OFFSET);
            if (this.selected) {
                ctx.restore();
                ctx.save();
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'gray';
                ctx.fillStyle = 'black';
                this.corners.forEach(function (corner) {
                    if (corner.isDown) {
                        ctx.fillStyle = 'gray';
                    }
                    else {
                        ctx.fillStyle = 'black';
                    }
                    ctx.beginPath();
                    ctx.arc(corner.x, corner.y, 5, 0, 2 * Math.PI);
                    ctx.stroke();
                    ctx.fill();
                });
            }
            ctx.restore();
        };
        MarkerSimulationObject.prototype.handleMouseDown = function (e) {
            if (e && !e.startX) {
                UTIL.extendMouseEvent(e, simulation_roberta_1.SimulationRoberta.Instance.scale, $('#robotLayer'));
            }
            var myEvent = e;
            this.isDown = this.isMouseOn(myEvent);
            if (this.isDown) {
                e.stopImmediatePropagation();
                this.mouseOldX = myEvent.startX;
                this.mouseOldY = myEvent.startY;
            }
            if (this.isDown && !this.selected) {
                $('#robotLayer').css('cursor', 'pointer');
                this.selected = true;
                //TODO redraw ?
            }
        };
        MarkerSimulationObject.prototype.handleMouseMove = function (e) {
            if (e && !e.startX) {
                UTIL.extendMouseEvent(e, simulation_roberta_1.SimulationRoberta.Instance.scale, $('#robotLayer'));
            }
            var myEvent = e;
            var dx = myEvent.startX - this.mouseOldX;
            var dy = myEvent.startY - this.mouseOldY;
            this.mouseOldX = myEvent.startX;
            this.mouseOldY = myEvent.startY;
            var onMe = this.isMouseOn(myEvent);
            if (onMe) {
                $('#robotLayer').css('cursor', 'pointer');
                $('#robotLayer').data('hovered', true);
                if (this.selected) {
                    e.stopImmediatePropagation();
                }
            }
            if (this.selected && this.isDown) {
                this.x += dx;
                this.y += dy;
                this.updateCorners();
            }
        };
        return MarkerSimulationObject;
    }(RectangleSimulationObject));
    exports.MarkerSimulationObject = MarkerSimulationObject;
    var CircleSimulationObject = /** @class */ (function (_super) {
        __extends(CircleSimulationObject, _super);
        function CircleSimulationObject(myId, myScene, mySelectionListener, type, p, optColor) {
            var params = [];
            for (var _i = 6; _i < arguments.length; _i++) {
                params[_i - 6] = arguments[_i];
            }
            var _this = _super.call(this, myId, myScene, mySelectionListener, type, optColor) || this;
            _this.defaultRadius = 50;
            _this.corners = [];
            _this.x = p.x;
            _this.y = p.y;
            if (params.length == 1) {
                _this.r = params[0];
            }
            else {
                _this.r = _this.defaultRadius;
            }
            _this.updateCorners();
            return _this;
        }
        CircleSimulationObject.prototype.draw = function (ctx, uCtx) {
            ctx.save();
            uCtx.save();
            if (this.type === SimObjectType.Obstacle || this.type === SimObjectType.ColorArea) {
                if (this.type === SimObjectType.Obstacle) {
                    ctx.shadowColor = '#3e3e3e';
                    ctx.shadowOffsetY = 5;
                    ctx.shadowOffsetX = 5;
                    ctx.shadowBlur = 5;
                }
                ctx.fillStyle = this.color;
                ctx.beginPath();
                ctx.arc(this.x, this.y, this.r, 0, 2 * Math.PI);
                ctx.fill();
            }
            if (this.type === SimObjectType.ColorArea) {
                uCtx.fillStyle = this.color;
                uCtx.beginPath();
                uCtx.arc(this.x, this.y, this.r, 0, 2 * Math.PI);
                uCtx.fill();
            }
            if (this.selected) {
                var cx = Math.round(this.x);
                var cy = Math.round(this.y);
                var r = Math.round(this.r);
                ctx.restore();
                ctx.save();
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'gray';
                ctx.fillStyle = 'black';
                ctx.beginPath();
                ctx.arc(Math.round(cx - r), Math.round(cy - r), 5, 0, 2 * Math.PI);
                ctx.stroke();
                ctx.fill();
                ctx.beginPath();
                ctx.arc(Math.round(cx + r), Math.round(cy - r), 5, 0, 2 * Math.PI);
                ctx.stroke();
                ctx.fill();
                ctx.beginPath();
                ctx.arc(Math.round(cx - r), Math.round(cy + r), 5, 0, 2 * Math.PI);
                ctx.stroke();
                ctx.fill();
                ctx.beginPath();
                ctx.arc(Math.round(cx + r), Math.round(cy + r), 5, 0, 2 * Math.PI);
                ctx.stroke();
                ctx.fill();
            }
            ctx.restore();
            uCtx.restore();
        };
        CircleSimulationObject.prototype.handleKeyEvent = function (e) {
            if (this.selected) {
                e.stopImmediatePropagation();
                var keyName = e.key;
                switch (keyName) {
                    case 'ArrowUp':
                        this.y -= this.SHIFT;
                        break;
                    case 'ArrowLeft':
                        this.x -= this.SHIFT;
                        break;
                    case 'ArrowDown':
                        this.y += this.SHIFT;
                        break;
                    case 'ArrowRight':
                        this.x += this.SHIFT;
                        break;
                    default:
                    // nothing to do so far
                }
                if (e.key === 'c' && (e.ctrlKey || e.metaKey)) {
                    var id = this.myScene.uniqueObjectId;
                    var shape = SimObjectShape.Circle;
                    this.myScene.objectToCopy = SimObjectFactory.copy(this);
                    e.preventDefault(); // Prevent the default copy behavior of Safari, which may beep or otherwise indicate an error due to the lack of a selection. See https://github.com/google/blockly/pull/4925.
                }
                this.updateCorners();
                $('#robotLayer').attr('tabindex', 0);
                $('#robotLayer').trigger('focus');
            }
        };
        CircleSimulationObject.prototype.handleMouseDown = function (e) {
            if (e && !e.startX) {
                UTIL.extendMouseEvent(e, simulation_roberta_1.SimulationRoberta.Instance.scale, $('#robotLayer'));
            }
            var myEvent = e;
            this.isDown = UTIL.checkInCircle(myEvent.startX, myEvent.startY, this.x, this.y, this.r);
            if (this.isDown) {
                e.stopImmediatePropagation();
                this.mouseOldX = myEvent.startX;
                this.mouseOldY = myEvent.startY;
            }
            if (this.isDown && !this.selected) {
                $('#robotLayer').css('cursor', 'pointer');
                this.selected = true;
                //TODO redraw ?
            }
            else if (this.selected) {
                this.corners.forEach(function (corner) {
                    corner.isDown = UTIL.checkInCircle(myEvent.startX, myEvent.startY, corner.x, corner.y, 15);
                });
                var mySelectedCornerIndex = this.corners.map(function (corner) { return corner.isDown; }).indexOf(true);
                if (mySelectedCornerIndex >= 0) {
                    e.stopImmediatePropagation();
                    switch (mySelectedCornerIndex) {
                        case 0:
                        case 2:
                            $('#robotLayer').css('cursor', 'nwse-resize');
                            break;
                        case 1:
                        case 3:
                            $('#robotLayer').css('cursor', 'nesw-resize');
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        CircleSimulationObject.prototype.handleMouseMove = function (e) {
            if (e && !e.startX) {
                UTIL.extendMouseEvent(e, simulation_roberta_1.SimulationRoberta.Instance.scale, $('#robotLayer'));
            }
            var myEvent = e;
            var dx = myEvent.startX - this.mouseOldX;
            var dy = myEvent.startY - this.mouseOldY;
            this.mouseOldX = myEvent.startX;
            this.mouseOldY = myEvent.startY;
            var mySelectedCornerIndex = this.corners.map(function (corner) { return corner.isDown; }).indexOf(true);
            var onMe = UTIL.checkInCircle(myEvent.startX, myEvent.startY, this.x, this.y, this.r);
            if (onMe) {
                $('#robotLayer').css('cursor', 'pointer');
                $('#robotLayer').data('hovered', true);
                if (this.selected) {
                    e.stopImmediatePropagation();
                }
            }
            var myOnCornerIndex = -1;
            for (var i = 0; i < this.corners.length; i++) {
                if (UTIL.checkInCircle(myEvent.startX, myEvent.startY, this.corners[i].x, this.corners[i].y, 15)) {
                    myOnCornerIndex = i;
                    break;
                }
            }
            if (myOnCornerIndex > -1 && this.selected) {
                e.stopImmediatePropagation();
                switch (myOnCornerIndex) {
                    case 0:
                    case 2:
                        $('#robotLayer').css('cursor', 'nwse-resize');
                        break;
                    case 1:
                    case 3:
                        $('#robotLayer').css('cursor', 'nesw-resize');
                        break;
                    default:
                        break;
                }
            }
            if (mySelectedCornerIndex >= 0 && this.selected) {
                if (Math.abs(dx) >= Math.abs(dy)) {
                    if (myEvent.startX < this.x) {
                        this.r -= dx;
                    }
                    else {
                        this.r += dx;
                    }
                }
                else {
                    if (myEvent.startY < this.y) {
                        this.r -= dy;
                    }
                    else {
                        this.r += dy;
                    }
                }
                this.r = Math.max(this.r, this.MIN_SIZE_OBJECT);
                this.updateCorners();
            }
            else {
                if (this.isDown) {
                    this.x += dx;
                    this.y += dy;
                    this.updateCorners();
                    //TODO redraw
                }
            }
        };
        /**
         * Not supported for circles
         */
        CircleSimulationObject.prototype.getLines = function () {
            throw new Error('Should never be called');
        };
        CircleSimulationObject.prototype.moveTo = function (p) {
            this.x = p.x;
            this.y = p.y;
            this.updateCorners();
        };
        CircleSimulationObject.prototype.updateCorners = function () {
            if (this.corners.length == 0) {
                this.corners[0] = { x: this.x - this.r, y: this.y - this.r, isDown: false };
                this.corners[1] = { x: this.x + this.r, y: this.y - this.r, isDown: false };
                this.corners[2] = { x: this.x + this.r, y: this.y + this.r, isDown: false };
                this.corners[3] = { x: this.x - this.r, y: this.y + this.r, isDown: false };
            }
            else {
                this.corners[0] = { x: this.x - this.r, y: this.y - this.r, isDown: this.corners[0].isDown };
                this.corners[1] = { x: this.x + this.r, y: this.y - this.r, isDown: this.corners[1].isDown };
                this.corners[2] = { x: this.x + this.r, y: this.y + this.r, isDown: this.corners[2].isDown };
                this.corners[3] = { x: this.x - this.r, y: this.y + this.r, isDown: this.corners[3].isDown };
            }
            this.redraw();
        };
        return CircleSimulationObject;
    }(BaseSimulationObject));
    exports.CircleSimulationObject = CircleSimulationObject;
    var TriangleSimulationObject = /** @class */ (function (_super) {
        __extends(TriangleSimulationObject, _super);
        function TriangleSimulationObject(myId, myScene, mySelectionListener, type, p, optColor) {
            var params = [];
            for (var _i = 6; _i < arguments.length; _i++) {
                params[_i - 6] = arguments[_i];
            }
            var _this = _super.call(this, myId, myScene, mySelectionListener, type, optColor) || this;
            _this.defaultSize = 50;
            _this.corners = [];
            if (params.length == 6) {
                _this.ax = params[0];
                _this.ay = params[1];
                _this.bx = params[2];
                _this.by = params[3];
                _this.cx = params[4];
                _this.cy = params[5];
                _this.updateCorners();
            }
            else {
                _this.ax = p.x - _this.defaultSize;
                _this.ay = p.y + _this.defaultSize;
                _this.bx = p.x;
                _this.by = p.y - _this.defaultSize;
                _this.cx = p.x + _this.defaultSize;
                _this.cy = p.y + _this.defaultSize;
            }
            _this.updateCorners();
            return _this;
        }
        TriangleSimulationObject.prototype.draw = function (ctx, uCtx) {
            ctx.save();
            uCtx.save();
            if (this.type === SimObjectType.Obstacle || this.type === SimObjectType.ColorArea) {
                if (this.type === SimObjectType.Obstacle) {
                    ctx.shadowColor = '#3e3e3e';
                    ctx.shadowOffsetY = 5;
                    ctx.shadowOffsetX = 5;
                    ctx.shadowBlur = 5;
                }
                ctx.fillStyle = this.color;
                ctx.beginPath();
                ctx.moveTo(this.ax, this.ay);
                ctx.lineTo(this.bx, this.by);
                ctx.lineTo(this.cx, this.cy);
                ctx.fill();
            }
            if (this.type === SimObjectType.ColorArea) {
                uCtx.fillStyle = this.color;
                uCtx.beginPath();
                uCtx.moveTo(this.ax, this.ay);
                uCtx.lineTo(this.bx, this.by);
                uCtx.lineTo(this.cx, this.cy);
                uCtx.fill();
            }
            if (this.selected) {
                ctx.restore();
                ctx.save();
                ctx.lineWidth = 2;
                ctx.strokeStyle = 'gray';
                ctx.fillStyle = 'black';
                ctx.beginPath();
                ctx.arc(Math.round(this.ax), Math.round(this.ay), 5, 0, 2 * Math.PI);
                ctx.stroke();
                ctx.fill();
                ctx.beginPath();
                ctx.arc(Math.round(this.bx), Math.round(this.by), 5, 0, 2 * Math.PI);
                ctx.stroke();
                ctx.fill();
                ctx.beginPath();
                ctx.arc(Math.round(this.cx), Math.round(this.cy), 5, 0, 2 * Math.PI);
                ctx.stroke();
                ctx.fill();
            }
            ctx.restore();
            uCtx.restore();
        };
        TriangleSimulationObject.prototype.handleKeyEvent = function (e) {
            if (this.selected) {
                e.stopImmediatePropagation();
                var keyName = e.key;
                switch (keyName) {
                    case 'ArrowUp':
                        this.ay -= this.SHIFT;
                        this.by -= this.SHIFT;
                        this.cy -= this.SHIFT;
                        break;
                    case 'ArrowLeft':
                        this.ax -= this.SHIFT;
                        this.bx -= this.SHIFT;
                        this.cx -= this.SHIFT;
                        break;
                    case 'ArrowDown':
                        this.ay += this.SHIFT;
                        this.by += this.SHIFT;
                        this.cy += this.SHIFT;
                        break;
                    case 'ArrowRight':
                        this.ax += this.SHIFT;
                        this.bx += this.SHIFT;
                        this.cx += this.SHIFT;
                        break;
                    default:
                    // nothing to do so far
                }
                if (e.key === 'c' && (e.ctrlKey || e.metaKey)) {
                    var id = this.myScene.uniqueObjectId;
                    var shape = SimObjectShape.Triangle;
                    this.myScene.objectToCopy = SimObjectFactory.copy(this);
                    e.preventDefault(); // Prevent the default copy behavior of Safari, which may beep or otherwise indicate an error due to the lack of a selection. See https://github.com/google/blockly/pull/4925.
                }
                this.updateCorners();
                $('#robotLayer').attr('tabindex', 0);
                $('#robotLayer').trigger('focus');
            }
        };
        TriangleSimulationObject.prototype.handleMouseDown = function (e) {
            if (e && !e.startX) {
                UTIL.extendMouseEvent(e, simulation_roberta_1.SimulationRoberta.Instance.scale, $('#robotLayer'));
            }
            var myEvent = e;
            this.isDown = this.isMouseOn(myEvent);
            if (this.isDown) {
                e.stopImmediatePropagation();
                this.mouseOldX = myEvent.startX;
                this.mouseOldY = myEvent.startY;
            }
            if (this.isDown && !this.selected) {
                $('#robotLayer').css('cursor', 'pointer');
                $('#robotLayer').data('hovered', true);
                this.selected = true;
                //TODO redraw ?
            }
            else if (this.selected) {
                this.corners.forEach(function (corner) {
                    corner.isDown = UTIL.checkInCircle(myEvent.startX, myEvent.startY, corner.x, corner.y, 15);
                });
                var mySelectedCornerIndex = this.corners.map(function (corner) { return corner.isDown; }).indexOf(true);
                if (mySelectedCornerIndex >= 0) {
                    e.stopImmediatePropagation();
                    $('#robotLayer').css('cursor', 'move');
                }
            }
        };
        TriangleSimulationObject.prototype.handleMouseMove = function (e) {
            if (e && !e.startX) {
                UTIL.extendMouseEvent(e, simulation_roberta_1.SimulationRoberta.Instance.scale, $('#robotLayer'));
            }
            var myEvent = e;
            var dx = myEvent.startX - this.mouseOldX;
            var dy = myEvent.startY - this.mouseOldY;
            this.mouseOldX = myEvent.startX;
            this.mouseOldY = myEvent.startY;
            var mySelectedCornerIndex = this.corners.map(function (corner) { return corner.isDown; }).indexOf(true);
            var onMe = this.isMouseOn(myEvent);
            if (onMe) {
                $('#robotLayer').css('cursor', 'pointer');
                $('#robotLayer').data('hovered', true);
                if (this.selected) {
                    e.stopImmediatePropagation();
                }
            }
            var myOnCornerIndex = -1;
            for (var i = 0; i < this.corners.length; i++) {
                if (UTIL.checkInCircle(myEvent.startX, myEvent.startY, this.corners[i].x, this.corners[i].y, 15)) {
                    myOnCornerIndex = i;
                    break;
                }
            }
            if (myOnCornerIndex > -1 && this.selected) {
                $('#robotLayer').css('cursor', 'move');
                e.stopImmediatePropagation();
            }
            if (mySelectedCornerIndex >= 0 && this.selected) {
                switch (mySelectedCornerIndex) {
                    case 0:
                        this.ax += dx;
                        this.ay += dy;
                        break;
                    case 1:
                        this.bx += dx;
                        this.by += dy;
                        break;
                    case 2:
                        this.cx += dx;
                        this.cy += dy;
                        break;
                    default:
                        break;
                }
                this.updateCorners();
            }
            else {
                if (this.isDown) {
                    //$('#robotLayer').css('cursor', 'pointer');
                    this.ax += dx;
                    this.ay += dy;
                    this.bx += dx;
                    this.by += dy;
                    this.cx += dx;
                    this.cy += dy;
                    this.updateCorners();
                    //TODO redraw
                }
            }
        };
        TriangleSimulationObject.prototype.getLines = function () {
            return [
                {
                    x1: this.ax,
                    x2: this.bx,
                    y1: this.ay,
                    y2: this.by,
                },
                {
                    x1: this.bx,
                    x2: this.cx,
                    y1: this.by,
                    y2: this.cy,
                },
                {
                    x1: this.ax,
                    x2: this.cx,
                    y1: this.ay,
                    y2: this.cy,
                },
            ];
        };
        TriangleSimulationObject.prototype.moveTo = function (p) {
            var diffx = this.ax - p.x;
            var diffy = this.ay - p.y;
            this.ax = p.x;
            this.ay = p.y;
            this.bx -= diffx;
            this.by -= diffy;
            this.cx -= diffx;
            this.cy -= diffy;
            this.updateCorners();
        };
        TriangleSimulationObject.prototype.isMouseOn = function (myEvent) {
            var areaOrig = Math.floor(Math.abs((this.bx - this.ax) * (this.cy - this.ay) - (this.cx - this.ax) * (this.by - this.ay)));
            var area1 = Math.floor(Math.abs((this.ax - myEvent.startX) * (this.by - myEvent.startY) - (this.bx - myEvent.startX) * (this.ay - myEvent.startY)));
            var area2 = Math.floor(Math.abs((this.bx - myEvent.startX) * (this.cy - myEvent.startY) - (this.cx - myEvent.startX) * (this.by - myEvent.startY)));
            var area3 = Math.floor(Math.abs((this.cx - myEvent.startX) * (this.ay - myEvent.startY) - (this.ax - myEvent.startX) * (this.cy - myEvent.startY)));
            return area1 + area2 + area3 <= areaOrig;
        };
        TriangleSimulationObject.prototype.updateCorners = function () {
            if (this.corners.length == 0) {
                this.corners[0] = { x: this.ax, y: this.ay, isDown: false };
                this.corners[1] = { x: this.bx, y: this.by, isDown: false };
                this.corners[2] = { x: this.cx, y: this.cy, isDown: false };
            }
            else {
                this.corners[0] = { x: this.ax, y: this.ay, isDown: this.corners[0].isDown };
                this.corners[1] = { x: this.bx, y: this.by, isDown: this.corners[1].isDown };
                this.corners[2] = { x: this.cx, y: this.cy, isDown: this.corners[2].isDown };
            }
            this.redraw();
        };
        return TriangleSimulationObject;
    }(BaseSimulationObject));
    exports.TriangleSimulationObject = TriangleSimulationObject;
    var Ground = /** @class */ (function () {
        function Ground(x, y, w, h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
        Ground.prototype.getLines = function () {
            return UTIL.getLinesFromRectangle(this);
            return [
                {
                    x1: this.x,
                    x2: this.x,
                    y1: this.y,
                    y2: this.y + this.h,
                },
                {
                    x1: this.x,
                    x2: this.x + this.w,
                    y1: this.y,
                    y2: this.y,
                },
                {
                    x1: this.x + this.w,
                    x2: this.x,
                    y1: this.y + this.h,
                    y2: this.y + this.h,
                },
                {
                    x1: this.x + this.w,
                    x2: this.x + this.w,
                    y1: this.y + this.h,
                    y2: this.y,
                },
            ];
        };
        Ground.prototype.getTolerance = function () {
            return 0;
        };
        return Ground;
    }());
    exports.Ground = Ground;
});
