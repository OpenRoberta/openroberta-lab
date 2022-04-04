import { RobotBase, SelectionListener } from 'robot.base';
import { Interpreter } from '../../nepostackmachine/interpreter.interpreter';

export abstract class RobotBaseStationary extends RobotBase {
    constructor(id: number, configuration: object, interpreter: Interpreter, name: string, mySelectionListener: SelectionListener) {
        super(id, configuration, interpreter, name, mySelectionListener);
        this.mobile = false;
    }
}
