define([ 'exports', 'comm' ], function(exports, COMM) {

    /**
     * Initialize gui state object
     */
    function init() {
        var ready = new $.Deferred();

        exports.server = {};
        exports.server.ping = true;

        exports.gui = {};
        exports.gui.view = '';
        exports.gui.prevView = '';
        exports.gui.language = '';
        exports.gui.robot = '';
        exports.gui.blocklyWorkspace = '';
        exports.gui.bricklyWorkspace = '';
        exports.gui.program = {};
        exports.gui.program.toolbox = {};
        exports.gui.program.toolbox.a = '';
        exports.gui.program.prog = {};
        exports.gui.program = {};
        exports.gui.program.toolbox = {};
        exports.gui.program.prog = {};
        exports.gui.program.download = false;
        exports.gui.configuration = {};
        exports.gui.configuration.toolbox = '';
        exports.gui.configuration.conf = '';
        exports.gui.connection = '';
        exports.gui.vendor = '';
        exports.gui.sim = '';
        exports.gui.connectionType = {
            TOKEN : 'token',
            AGENT : 'arduinoAgent',
            AUTO : 'autoConnection',
            AGENTORTOKEN : 'arduinoAgentOrToken'
        }

        exports.user = {};
        exports.user.id = -1;
        exports.user.accountName = '';
        exports.user.name = '';
        exports.user.isAccountActivated = false;

        //exports.socket.portNames = [];
        //exports.socket.vendorIds = [];

        exports.program = {};
        exports.program.name = '';
        exports.program.saved = true;
        exports.program.shared = true;
        exports.program.timestamp = '';
        exports.program.source = '';
        exports.program.fileExtension = ''
        exports.program.xml = '';
        exports.program.toolbox = {};
        exports.program.toolbox.level = '';
        exports.program.toolbox.xml = '';

        exports.configuration = {};
        exports.configuration.name = '';
        exports.configuration.saved = true;
        exports.configuration.timestamp = '';
        exports.configuration.xml = '';

        exports.toolbox = '';

        exports.robot = {};
        exports.robot.token = '';
        exports.robot.name = '';
        exports.robot.state = '';
        exports.robot.battery = '';
        exports.robot.version = '';
        exports.robot.fWName = '';
        exports.robot.sensorValues = '';
        exports.robot.nepoExitValue = 0;
        exports.robot.time = -1;
        exports.robot.robotPort = '';
        exports.robot.socket = null;

        exports.tutorials = {};

        var getInitFromServer = function() {
            return COMM.json("/admin", {
                "cmd" : "init"
            }, function(result) {
                if (result.rc === 'ok') {
                    $.extend(exports.server, result.server);
                    exports.server.version = result["server.version"];
                    exports.server.time = result.serverTime;
                    ready.resolve();
                }
            }, 'init gui state model');
        }

        // check if tutorials are available
        $.ajax({
            url : "../tutorial/",
            success : function(data) {
                var tutorialPathsList = [];
                $(data).find("a[href*='\.json']:not(a[href*='\.gz'])").each(function() {
                    tutorialPathsList.push($(this).attr("href"));
                });
                function readTutorialsRecursive() {
                    var tutorialPath = tutorialPathsList.splice(0, 1);
                    // list of files is empty?
                    if (tutorialPath.length == 0) {
                        return $.Deferred().resolve().promise();
                    }
                    return $.getJSON('..' + tutorialPath).done(function(data) {
                        // store the available tutorial objects
                        if (data.name) {
                            var tutorialId = data.name.toLowerCase().replace(/ /g, "");
                            exports.tutorials[tutorialId] = data;
                        } else {
                            console.error('"' + tutorialPath + '" is not a valid tutorial file! No name could be found.');
                        }
                    }).fail(function(e, r) {
                        // this should not happen
                        console.error('"' + tutorialPath + '" is not a valid json file! The reason is probably a', r);
                        return readTutorialsRecursive();
                    }).then(function() {
                        // check for more tutorials
                        return readTutorialsRecursive();
                    });
                }
                // all tutorials stored? do the last step for initializing the GUI
                readTutorialsRecursive().always(function() {
                    getInitFromServer();
                });
            },
            // no tutorial folder available? ignore, do the last step for initializing the GUI
            error : getInitFromServer,
        });
        return ready.promise();
    }
    exports.init = init;
});
