import * as MSG from 'message';
import * as UTIL from 'util';
import * as GUISTATE_C from 'guiState.controller';
import * as NN_CTRL from 'nn.controller';
import * as TOUR_C from 'tour.controller';
import * as PROG_C from 'program.controller';
import * as PROGRAM from 'program.model';
import * as PROGRAM_M from 'program.model';
// @ts-ignore
import * as Blockly from 'blockly';
import * as $ from 'jquery';
import 'jquery-validate';
import { SimObjectShape } from 'simulation.objects';
import WebotsSimulation from 'simulation.webots';
import { SimulationRoberta } from 'simulation.roberta';
import CONST from 'simulation.constants';
import * as PROGLIST from 'progList.model';

const INITIAL_WIDTH = 0.5;

export interface Simulation {
    isInterpreterRunning(): boolean;

    init(result: object[], refresh: boolean, callback: () => void, robotType?: string): Promise<void>;

    stopProgram(): void;

    importImage(): void;

    resetPose(): void;

    stop(): void;

    run(result: object[], callbackOnEnd: () => void);
}

class ProgSimController {
    protected SIM: Simulation;
    private static _progSimInstance: ProgSimController;
    protected readonly blocklyWorkspace: any;

    protected constructor() {
        this.blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
        this.initEvents();
    }

    public static createProgSimInstance(): void {
        if (!this._progSimInstance) {
            this._progSimInstance = new ProgSimController();
        }
    }

    initEvents() {
        let C = this;
        $('#simButton').off();
        $('#simButton').onWrap(
            'click touchend',
            function (event, multi: boolean) {
                if (GUISTATE_C.hasWebotsSim()) {
                    C.SIM = WebotsSimulation;
                } else {
                    C.SIM = SimulationRoberta.Instance;
                    if (SimulationRoberta.Instance.debugMode) {
                        SimulationRoberta.Instance.updateDebugMode(false);
                    }
                }
                // Workaround for IOS speech synthesis, speech must be triggered once by a button click explicitly before it can be used programmatically
                window.speechSynthesis && window.speechSynthesis.speak && window.speechSynthesis.speak(new SpeechSynthesisUtterance(''));
                C.toggleSim($(this));
                return false;
            },
            'sim open/close clicked'
        );
    }

    protected addControlEvents() {
        let SIM = this.SIM;
        let C = this;
        $('#simControl').onWrap(
            'click.sim',
            function () {
                if (!SIM.isInterpreterRunning()) {
                    NN_CTRL.mkNNfromProgramStartBlock();
                    let myCallback = function (result) {
                        if (result.rc == 'ok') {
                            MSG.displayMessage('MESSAGE_EDIT_START', 'TOAST', GUISTATE_C.getProgramName(), null, null);
                            $('#simControl').addClass('typcn-media-stop').removeClass('typcn-media-play-outline');
                            $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_STOP_TOOLTIP);
                            result.savedName = GUISTATE_C.getProgramName();
                            SIM.run([result], function () {
                                $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                                $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
                            });
                        } else {
                            MSG.displayInformation(result, '', result.message, '', null);
                        }
                        PROG_C.reloadProgram(result);
                    };
                    C.prepareProgram(myCallback);
                } else {
                    $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                    $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
                    SIM.stopProgram();
                }
                return false;
            },
            'sim control clicked'
        );
    }

    protected addConfigEvents() {
        let SIM = this.SIM;
        let C = this;

        if (UTIL.isIE() || UTIL.isEdge()) {
            // TODO IE and Edge: Input event not firing for file type of input
            $('#simImport').hide();
        } else {
            $('#simImport').show();
            $('#simImport').onWrap('click.sim', function () {
                SIM.importImage();
                return false;
            });
        }

        $('#simRobot').onWrap(
            'click.sim',
            function () {
                let robot = GUISTATE_C.getRobot();
                let position = $('#simDiv').position();
                position.left += 48;
                $('#simRobotWindow').toggleSimPopup(position);
                return false;
            },
            'sim robot view clicked'
        );

        $('#simValues').onWrap(
            'click.sim',
            function () {
                let position = $('#simDiv').position();
                position.left = $(window).width() - ($('#simValuesWindow').width() + 12);
                $('#simValuesWindow').toggleSimPopup(position);
                return false;
            },
            'sim values view clicked'
        );

        $('.simWindow .close').onWrap(
            'click.sim',
            function () {
                $($(this).parents('.simWindow:first')).animate(
                    {
                        opacity: 'hide',
                        top: 'hide',
                    },
                    300
                );
                return false;
            },
            'sim window close clicked'
        );

        $('#simResetPose').onWrap(
            'click.sim',
            function () {
                SIM.resetPose();
                return false;
            },
            'sim reset pose clicked'
        );

        $('.simAddMarker').onWrap(
            'click.sim',
            function (e) {
                let id = e.target.getAttribute('data-marker') || e.currentTarget.text;
                (SIM as SimulationRoberta).addMarker && (SIM as SimulationRoberta).addMarker(id);
                return false;
            },
            'sim add marker clicked'
        );

        $('#simMarkerDeleteAll').onWrap(
            'click.sim',
            function (e) {
                (SIM as SimulationRoberta).deleteAllMarker && (SIM as SimulationRoberta).deleteAllMarker();
                return false;
            },
            'sim delete all marker clicked'
        );

        $('#simAddObstacleRectangle').onWrap(
            'click.sim',
            function () {
                (SIM as SimulationRoberta).addObstacle && (SIM as SimulationRoberta).addObstacle(SimObjectShape.Rectangle);
                return false;
            },
            'sim add rectangle obstacle clicked'
        );

        $('#simAddObstacleTriangle').onWrap(
            'click.sim',
            function () {
                (SIM as SimulationRoberta).addObstacle && (SIM as SimulationRoberta).addObstacle(SimObjectShape.Triangle);
                return false;
            },
            'sim add triangle obstacle clicked'
        );

        $('#simAddObstacleCircle').onWrap(
            'click.sim',
            function () {
                (SIM as SimulationRoberta).addObstacle && (SIM as SimulationRoberta).addObstacle(SimObjectShape.Circle);
                return false;
            },
            'sim add circle obstacle clicked'
        );
        $('#simObstacleDeleteAll').onWrap(
            'click.sim',
            function () {
                (SIM as SimulationRoberta).deleteAllObstacle && (SIM as SimulationRoberta).deleteAllObstacle();
                return false;
            },
            'sim delete all obstacles clicked'
        );

        $('#simAddAreaRectangle').onWrap('click.sim', function () {
            (SIM as SimulationRoberta).addColorArea && (SIM as SimulationRoberta).addColorArea(SimObjectShape.Rectangle);
            return false;
        });

        $('#simAddAreaTriangle').onWrap(
            'click.sim',
            function () {
                (SIM as SimulationRoberta).addColorArea && (SIM as SimulationRoberta).addColorArea(SimObjectShape.Triangle);
                return false;
            },
            'sim add triangle color area clicked'
        );

        $('#simAddAreaCircle').onWrap(
            'click.sim',
            function () {
                (SIM as SimulationRoberta).addColorArea && (SIM as SimulationRoberta).addColorArea(SimObjectShape.Circle);
                return false;
            },
            'sim add circle color area clicked'
        );

        $('#simAreaDeleteAll').onWrap(
            'click.sim',
            function () {
                (SIM as SimulationRoberta).deleteAllColorArea && (SIM as SimulationRoberta).deleteAllColorArea();
                return false;
            },
            'sim delete all color areas clicked'
        );

        $('#simChangeObjectColor').onWrap(
            'click.sim',
            function () {
                if (!$('#simChangeObjectColor').hasClass('disabled')) {
                    (SIM as SimulationRoberta).toggleColorPicker && (SIM as SimulationRoberta).toggleColorPicker();
                }
                return false;
            },
            'sim change color clicked'
        );

        $('#simDeleteObject').onWrap(
            'click.sim',
            function () {
                if (!$('#simDeleteObject').hasClass('disabled')) {
                    (SIM as SimulationRoberta).deleteSelectedObject && (SIM as SimulationRoberta).deleteSelectedObject();
                }
                return false;
            },
            'sim delete selected object clicked'
        );

        $('#simDownloadConfig').onWrap(
            'click.sim',
            function () {
                if ((SIM as SimulationRoberta).exportConfigData) {
                    let filename = GUISTATE_C.getProgramName() + '-sim_configuration.json';
                    UTIL.download(filename, JSON.stringify((SIM as SimulationRoberta).exportConfigData()));
                    MSG.displayMessage('MENU_MESSAGE_DOWNLOAD', 'TOAST', filename, null, null);
                }
                return false;
            },
            'sim download configuration clicked'
        );

        $('#simUploadConfig').onWrap(
            'click.sim',
            function () {
                (SIM as SimulationRoberta).importConfigData && (SIM as SimulationRoberta).importConfigData();
                return false;
            },
            'sim upload configuration clicked'
        );

        $('#simScene').onWrap(
            'click.sim',
            function () {
                (SIM as SimulationRoberta).setBackground && (SIM as SimulationRoberta).setBackground(-1);
                return false;
            },
            'sim scroll background image clicked'
        );
        $('#simTrail').onWrap(
            'click.sim',
            function () {
                $(this).toggleClass('typcn-chart-line-outline');
                $(this).toggleClass('typcn-chart-line');
                (SIM as SimulationRoberta).toggleTrail && (SIM as SimulationRoberta).toggleTrail();
                return false;
            },
            'sim toggle draw trail clicked'
        );
    }

    protected removeControl(): void {
        $('*').off('click.sim');
    }

    protected toggleSim($button: JQuery) {
        if ($('.fromRight.rightActive').hasClass('shifting')) {
            return;
        }
        let C = this;
        if ($button.hasClass('rightActive')) {
            $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play').removeClass('typcn-media-stop');
            (C.SIM as SimulationRoberta).endDebugging && (C.SIM as SimulationRoberta).endDebugging();
            C.SIM.stopProgram();
            $('#blockly').closeRightView(() => {
                C.SIM.stop();
            });
            UTIL.closeSimRobotWindow();
        } else {
            this.resetButtons();
            this.removeControl();
            this.addControlEvents();
            this.addConfigEvents();
            let myCallback = function (result) {
                if (result.rc == 'ok') {
                    result.savedName = GUISTATE_C.getProgramName();
                    C.SIM.init([result], true, null, GUISTATE_C.getRobotGroup());
                    $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play');
                    if (TOUR_C.getInstance() && TOUR_C.getInstance().trigger) {
                        TOUR_C.getInstance().trigger('startSim');
                    }
                    $button.openRightView($('#simDiv'), INITIAL_WIDTH);
                    UTIL.openSimRobotWindow();
                } else {
                    MSG.displayInformation(result, '', result.message, '', null);
                }
                PROG_C.reloadProgram(result);
            };
            this.prepareProgram(myCallback);
        }
        return false;
    }

    protected resetButtons() {
        $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop debug');
        $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
        $('#simTrail').addClass('typcn-chart-line-outline').removeClass('typcn-chart-line');
        $('.simChangeObject').removeClass('disabled').addClass('disabled');
        $('.debug').hide();
    }

    protected prepareProgram(callback: (result) => void) {
        let xmlProgram = Blockly.Xml.workspaceToDom(this.blocklyWorkspace);
        let xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
        let isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
        let configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
        let xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;
        let language = GUISTATE_C.getLanguage();
        PROGRAM.runInSim(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, language, callback);
    }
}

export const createProgSimInstance = ProgSimController.createProgSimInstance;

class ProgSimDebugController extends ProgSimController {
    private static _progSimDebugInstance: ProgSimDebugController;

    public static createProgSimDebugInstance(): void {
        if (!this._progSimDebugInstance) {
            this._progSimDebugInstance = new ProgSimDebugController();
        }
    }

    override initEvents() {
        let C = this;
        $('#simDebugButton').off();
        $('#simDebugButton').onWrap(
            'click touchend',
            function (event, multi: boolean) {
                C.SIM = SimulationRoberta.Instance;
                SimulationRoberta.Instance.updateDebugMode(true);
                // Workaround for IOS speech synthesis, speech must be triggered once by a button click explicitly before it can be used programmatically
                window.speechSynthesis && window.speechSynthesis.speak && window.speechSynthesis.speak(new SpeechSynthesisUtterance(''));
                C.toggleSim($(this));
                return false;
            },
            'sim debug open/close clicked'
        );
    }

    override addControlEvents() {
        let C = this;
        $('#simControl').onWrap(
            'click.sim',
            function (event) {
                C.toggleSimEvent(CONST.DEBUG_BREAKPOINT);
            },
            'sim control clicked'
        );

        $('#simControlStepInto').onWrap(
            'click.sim',
            function (event) {
                C.toggleSimEvent(CONST.DEBUG_STEP_INTO);
            },
            'sim debug step into clicked'
        );

        $('#simControlStepOver').onWrap(
            'click.sim',
            function (event) {
                C.toggleSimEvent(CONST.DEBUG_STEP_OVER);
            },
            'sim debug step over clicked'
        );

        $('#simStop').onWrap(
            'click.sim',
            function (event) {
                $('#simStop').addClass('disabled');
                $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play');
                C.SIM.stopProgram();
            },
            'sim stop clicked'
        );
    }

    private toggleSimEvent(event: string) {
        let C = this;
        let SIM: SimulationRoberta = this.SIM as SimulationRoberta;
        if ($('#simControl').hasClass('typcn-media-play-outline')) {
            let myCallback = function (result) {
                if (result.rc == 'ok') {
                    result.savedName = GUISTATE_C.getProgramName();
                    SIM.run([result], function () {
                        $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-play');
                        $('#simStop').addClass('disabled');
                    });
                    SIM.interpreterAddEvent(event);
                }
                $('#simControl').removeClass('typcn-media-play-outline').addClass('typcn-media-play');
                $('#simStop').removeClass('disabled');
            };
            C.prepareProgram(myCallback);
        } else if ($('#simControl').hasClass('typcn-media-play')) {
            SIM.setPause(false);
            SIM.interpreterAddEvent(event);
        } else {
            if ($('#simControl').hasClass('typcn-media-stop')) {
                $('#simControl').addClass('blue').removeClass('typcn-media-stop');
                $('#simControl').attr('data-original-title', Blockly.Msg.MENU_DEBUG_STEP_BREAKPOINT_TOOLTIP);
                $('#simStop').show();
            }
            $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play');
            SIM.stopProgram();
        }
    }

    override resetButtons() {
        super.resetButtons();
        $('#simStop').addClass('disabled');
        $('#simControl').addClass('debug');
        $('#simControl').attr('data-original-title', Blockly.Msg.MENU_DEBUG_STEP_BREAKPOINT_TOOLTIP);
        $('.debug').show();
    }
}

export const createProgSimDebugInstance = ProgSimDebugController.createProgSimDebugInstance;

class ProgSimMultiController extends ProgSimController {
    private static _progSimMultiInstance: ProgSimMultiController;
    private extractedPrograms: any[];
    private progList: any[];
    private selectedPrograms: any[];

    public static createProgSimMultiInstance(): void {
        if (!this._progSimMultiInstance) {
            this._progSimMultiInstance = new ProgSimMultiController();
        }
    }

    override initEvents() {
        let C = this;
        $('#head-navigation-program-edit').onWrap('click', '.dropdown-menu li:not(.disabled) a', function (event) {
            let targetId =
                event.target.id ||
                (event.target.children[0] && event.target.children[0].id) ||
                (event.target.previousSibling && event.target.previousSibling.id);
            if (targetId === 'menuRunMulipleSim') {
                C.SIM = SimulationRoberta.Instance;
                C.showListProg();
                $('.debug').hide();
                return false;
            }
        });
        $('#loadMultipleSimPrograms').off();
        $('#loadMultipleSimPrograms').onWrap(
            'click',
            function () {
                window.speechSynthesis && window.speechSynthesis.speak && window.speechSynthesis.speak(new SpeechSynthesisUtterance(''));
                C.selectedPrograms = [];
                let dataList = $('#multipleRobotsTable').bootstrapTable('getData');
                dataList.forEach((row, index) => {
                    if (row.num > 0) {
                        let myProg = row;
                        myProg['times'] = row.num;
                        C.selectedPrograms.push(myProg);
                    }
                });
                C.loadProgramms().then((loadedPrograms) => {
                    Promise.all(loadedPrograms).then(
                        (values) => {
                            if (C.extractedPrograms.length >= 1) {
                                C.toggleMultiSim($('#simButton'), C.getSortedExtractedPrograms());
                            }
                        },
                        (values) => {
                            $('#blockly').closeRightView(() => {
                                C.SIM.stop();
                            });
                            MSG.displayInformation({ rc: 'error' }, '', values.message, values.name, null);
                        }
                    );
                    $('#showMultipleSimPrograms').modal('hide');
                });
            },
            'multi sim load clicked'
        );
        $('#showMultipleSimPrograms').on('show.bs.modal', () => {
            let myheight = Math.min($(window).height() - 200, 532);
            $('#showMultipleSimPrograms .fixed-table-body').height(myheight);
        });
    }

    private getSortedExtractedPrograms(): any[] {
        let mySortedExtractedPrograms = [];
        let C = this;
        this.selectedPrograms.forEach((selected) => {
            let myProgram = C.extractedPrograms.find((element) => element.programName == selected.programName);
            for (let i = 0; i < selected.times; i++) {
                mySortedExtractedPrograms.push(myProgram);
            }
        });
        return mySortedExtractedPrograms;
    }

    private async loadProgramms(): Promise<any[]> {
        let numberOfPrograms = 0;
        let C = this;
        C.extractedPrograms = [];
        let myPromises: Promise<void>[] = [];
        for (let item of this.selectedPrograms) {
            if (item.blockly) {
                myPromises.push(
                    new Promise<void>((resolve, reject) => {
                        C.prepareProgram(function (result) {
                            C.loadStackMachineCode(item, resolve, reject, result);
                        });
                    })
                );
            } else {
                myPromises.push(
                    new Promise<void>((resolve, reject) => {
                        PROGRAM_M.loadProgramFromListing(item.programName, item.owner, item.creator, function (result) {
                            C.loadStackMachineCode(item, resolve, reject, result);
                        });
                    })
                );
            }
        }
        return myPromises;
    }

    private loadStackMachineCode(item, resolve, reject, result) {
        let C = this;
        if (result.rc === 'ok') {
            let loadResult = result;
            let xmlTextProgram = result.progXML;
            let isNamedConfig = result.configName !== GUISTATE_C.getRobotGroup().toUpperCase() + 'basis' && result.configName !== '';
            let configName = isNamedConfig ? result.configName : undefined;
            let xmlConfigText =
                result.configName !== ''
                    ? result.confXML
                        ? result.confXML
                        : GUISTATE_C.isConfigurationAnonymous()
                        ? GUISTATE_C.getConfigurationXML()
                        : undefined
                    : undefined;
            let language = GUISTATE_C.getLanguage();
            PROGRAM_M.runInSim(result.programName, configName, xmlTextProgram, xmlConfigText, language, function (result) {
                if (result.rc === 'ok') {
                    let combinedResult = loadResult;
                    for (let resultProp in result) {
                        combinedResult[resultProp] = result[resultProp];
                    }
                    C.extractedPrograms.push(combinedResult);
                    resolve('ok');
                } else {
                    reject({ message: result.message, name: item.programName });
                }
            });
        } else {
            reject({ message: result.message, name: item.programName });
        }
    }

    showListProg() {
        let C = this;
        let dataList = [];
        dataList.push({
            programName: GUISTATE_C.getProgramName(),
            robot: GUISTATE_C.getRobotGroup(),
            creator: '',
            date: '',
            num: 0,
            blockly: true,
        });
        if (GUISTATE_C.isUserLoggedIn()) {
            PROGLIST.loadProgList(function (result) {
                if (result.rc === 'ok') {
                    C.progList = result;
                    $('#multipleRobotsTable').bootstrapTable('destroy'); //refreshing the table
                    let robotType = GUISTATE_C.getRobot();
                    result.programNames.forEach(function (item, i, oriarray) {
                        dataList.push({
                            programName: item[0],
                            robot: robotType,
                            creator: item[1],
                            owner: item[3],
                            date: item[4],
                            num: 0,
                        });
                    });
                }
                C.showTable(dataList);
            });
        } else {
            this.showTable(dataList);
        }
    }

    private showTable(dataList: any[]) {
        let C = this;
        $('#multipleRobotsTable').bootstrapTable({
            sortName: 'name',
            toggle: 'multipleRobotsTable',
            iconsPrefix: 'typcn',
            search: true,
            icons: {
                paginationSwitchDown: 'typcn-document-text',
                paginationSwitchUp: 'typcn-book',
                refresh: 'typcn-refresh',
            },
            pagination: 'true',
            buttonsAlign: 'right',
            resizable: 'true',
            rowStyle: C.rowStyle,

            columns: [
                {
                    field: 'programName',
                    title: "<span lkey='Blockly.Msg.DATATABLE_PROGRAM_NAME'>" + (Blockly.Msg.DATATABLE_PROGRAM_NAME || 'Name des Programms') + '</span>',
                    sortable: true,
                },
                {
                    field: 'creator',
                    title: "<span lkey='Blockly.Msg.DATATABLE_CREATED_BY'>" + (Blockly.Msg.DATATABLE_CREATED_BY || 'Erzeugt von') + '</span>',
                    sortable: true,
                },
                {
                    field: 'owner',
                    visible: false,
                },
                {
                    field: 'date',
                    title: "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>" + (Blockly.Msg.DATATABLE_CREATED_ON || 'Erzeugt am') + '</span>',
                    sortable: true,
                    formatter: UTIL.formatDate,
                },
                {
                    field: 'num',
                    events: C.eventTimesProgram,
                    align: 'left',
                    valign: 'top',
                    formatter: C.formatTimesProgram,
                    width: '117px',
                },
            ],
            data: dataList,
        });
        $('#showMultipleSimPrograms').modal('show');
    }

    private rowStyle(row, index) {
        if (row.creator === '') {
            return { css: { 'background-color': '#EEEEEE' } };
        } else {
            return {};
        }
    }

    private formatTimesProgram(value, row, index) {
        return (
            '<div class="input-group"><input type="button" value="-" class="button-minus" data-field="quantity"><input type="number" step="1" max="9" min="0" value=' +
            value +
            ' name="quantity" class="quantity-field"><input type="button" value="+" class="button-plus" data-field="quantity"></div>'
        );
    }

    private eventTimesProgram: object = {
        'input .quantity-field': function (e, value, row, index) {
            e.preventDefault();
            let parent = $(e.target).closest('div');
            let currentVal = parseInt(<string>parent.find('input[name=quantity]').val(), 10);
            let newValue: number = currentVal;
            if (currentVal < 0) {
                newValue = 0;
            } else if (currentVal > 10) {
                newValue = 9;
            }
            parent.find('input[name=quantity]').val(newValue);
            $('#multipleRobotsTable').bootstrapTable('updateCell', { index: index, field: 'num', value: newValue });
        },
        'click .input-group .button-plus': function (e, value, row, index) {
            e.preventDefault();
            let fieldName = $(e.target).data('field');
            let parent = $(e.target).closest('div');
            let currentVal = parseInt(<string>parent.find('input[name=' + fieldName + ']').val(), 10);
            let newValue: number = currentVal;
            if (!isNaN(currentVal)) {
                if (currentVal < 9) {
                    newValue++;
                }
            } else {
                newValue = 0;
            }
            parent.find('input[name=quantity]').val(newValue);
            $('#multipleRobotsTable').bootstrapTable('updateCell', { index: index, field: 'num', value: newValue });
        },
        'click .input-group .button-minus': function (e, value, row, index) {
            e.preventDefault();
            let fieldName = $(e.target).data('field');
            let parent = $(e.target).closest('div');
            let currentVal = parseInt(<string>parent.find('input[name=' + fieldName + ']').val(), 10);
            let newValue: number = currentVal;
            if (!isNaN(currentVal)) {
                if (currentVal > 0) {
                    newValue--;
                }
            } else {
                newValue = 0;
            }
            parent.find('input[name=quantity]').val(newValue);
            $('#multipleRobotsTable').bootstrapTable('updateCell', { index: index, field: 'num', value: newValue });
        },
    };

    override addControlEvents() {
        let SIM = this.SIM;
        let C = this;
        $('#simControl').onWrap(
            'click.sim',
            function () {
                if (!SIM.isInterpreterRunning()) {
                    NN_CTRL.mkNNfromProgramStartBlock();
                    $('#simControl').addClass('typcn-media-stop').removeClass('typcn-media-play-outline');
                    $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_STOP_TOOLTIP);
                    C.loadProgramms().then((loadedPrograms) => {
                        Promise.all(loadedPrograms).then(
                            (values) => {
                                SIM.run(C.getSortedExtractedPrograms(), function () {
                                    $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                                    $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
                                });
                            },
                            (values) => {
                                $('#blockly').closeRightView(() => {
                                    this.SIM.stop();
                                });
                                MSG.displayInformation({ rc: 'error' }, '', values.message, values.name, null);
                            }
                        );
                    });
                } else {
                    $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                    $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
                    SIM.stopProgram();
                }
                return false;
            },
            'sim control clicked'
        );
    }

    toggleMultiSim($button: JQuery, mySortedExtractedPrograms: any[]) {
        if ($('.fromRight.rightActive').hasClass('shifting')) {
            return;
        }
        let C = this;
        C.SIM.stopProgram();
        this.resetButtons();
        this.removeControl();
        this.addControlEvents();
        this.addConfigEvents();
        C.SIM.init(mySortedExtractedPrograms, true, null, GUISTATE_C.getRobotGroup());
        $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play');
        if (TOUR_C.getInstance() && TOUR_C.getInstance().trigger) {
            TOUR_C.getInstance().trigger('startSim');
        }
        $button.openRightView($('#simDiv'), INITIAL_WIDTH);
        UTIL.openSimRobotWindow();
        return false;
    }
}

export const createProgSimMultiInstance = ProgSimMultiController.createProgSimMultiInstance;
