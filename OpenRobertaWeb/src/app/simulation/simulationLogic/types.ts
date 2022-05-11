type PointRobotWorld = {
    x: number;
    y: number;
    rx: number;
    ry: number;
    bumped?: boolean;
};
type Bumped = {
    bumped: boolean;
};

type PointRobotWorldBumped = PointRobotWorld & Bumped;

type Point = {
    x: number;
    y: number;
};
type Line = {
    x1: number;
    y1: number;
    x2: number;
    y2: number;
};
type Corner = Point & { isDown: boolean };

type Encoder = {
    rightAngle: number;
    leftAngle: number;
    left: number;
    right: number;
};

type Geometry = {
    x: number;
    y: number;
    w: number;
    h: number;
    radius?: number;
    color: string;
};

type ListenerAction = (...args: Array<any>) => void;
type ListenerRemover = () => boolean;

type SimMouseEvent = {
    startX: number;
    startY: number;
};

type Motor = {
    speed: number;
    port: string;
};

type Key = {
    name: string;
    value: boolean;
    port?: string;
};

type TouchKey = Key & { touchColors: string[] };
type DrawableTouchKey = TouchKey & { x: number; y: number; r: number; color: string; type: string; typeValue: number };
type DrawableMotor = {
    name: string;
    speed: number;
    port: string;
    color: string;
    cx: number;
    cy: number;
    theta: number;
    timeout: 0;
};

type Rectangle = {
    x: number;
    y: number;
    w: number;
    h: number;
};
