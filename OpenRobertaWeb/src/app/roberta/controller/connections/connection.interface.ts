export interface ConnectionInterface {
    init(): void;

    terminate(): void;

    runOnBrick(configName: string, xmlTextProgram: string, xmlConfigText: string): void;

    runNative(sourceCode: string): void;

    /**
     * this gets called if stopProgram is pressed, button  is only available if property is set, some but not all systems need this function
     * leave empty if not needed
     * @see progRun.controller#stopProgram
     */
    stopProgram(): void;

    /**
     * replaces guistate isRobotConnected most devices just return true here even if not connected
     * check out: <br>
     * @see setConnectionState
     * @see guiState.controller#isRobotConnected
     * @see setView
     */
    isRobotConnected(): boolean;

    /**
     * replaces setState switch case logic most devices just do nothing here
     * this gets called in guistate setState you may incoroprate update logic that has to be done with each state change
     * this will be called when pinging
     * @see setPing
     * @see setPingTime
     */
    setState(): void;

    /**
     * replaces guistate update menu status, this function may be reworked all together once its clear who and why this is called / with what arbument
     * this is currently a hack for agent
     * @param numOfConnections
     */
    updateMenuStatus(numOfConnections): void;

    reset2DefaultFirmware(): void;

    showConnectionModal(): void;

    showRobotInfo(): void;

    showWlanModal(): void;
}
