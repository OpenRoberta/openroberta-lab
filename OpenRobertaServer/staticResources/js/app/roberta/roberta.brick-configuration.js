define([ 'exports', 'message', 'log', 'util', 'rest.configuration', 'roberta.user-state', 'roberta.robot', 'roberta.navigation', 'roberta.brickly', 'jquery' ],
        function(exports, MSG, LOG, UTIL, CONFIGURATION, userState, ROBERTA_ROBOT, ROBERTA_NAVIGATION, BRICKLY, $) {

            var $formSingleModal;

            /**
             * Save configuration to server
             */
            function saveConfigurationToServer() {
                if (userState.configuration) {
                    $('.modal').modal('hide'); // close all opened popups
                    var xmlText = exports.getXmlOfConfiguration(BRICKLY.getBricklyWorkspace());
                    CONFIGURATION.saveConfigurationToServer(userState.configuration, xmlText, function(result) {
                        if (result.rc === 'ok') {
                            userState.configurationSaved = true;
                            $('#menuSaveConfig').parent().addClass('disabled');
                            BRICKLY.getBricklyWorkspace().robControls.disable('saveProgram');
                            LOG.info('save brick configuration ' + userState.configuration);
                        }
                        MSG.displayInformation(result, "MESSAGE_EDIT_SAVE_CONFIGURATION", result.message, userState.configuration);
                    });
                }
            }

            /**
             * Save configuration with new name to server
             */
            function saveAsConfigurationToServer() {
                $formSingleModal.validate();
                if ($formSingleModal.valid()) {
                    var confName = $('#singleModalInput').val().trim();
                    var xmlText = exports.getXmlOfConfiguration(BRICKLY.getBricklyWorkspace());
                    CONFIGURATION.saveAsConfigurationToServer(confName, xmlText, function(result) {
                        if (result.rc === 'ok') {
                            setConfiguration(confName);
                            $('.modal').modal('hide'); // close all opened popups
                            LOG.info('save brick configuration ' + userState.configuration);
                        }
                        MSG.displayInformation(result, "MESSAGE_EDIT_SAVE_CONFIGURATION_AS", result.message, userState.configuration);
                    });
                }
            }

            /**
             * Load the configuration that was selected in configurations list
             */
            function loadConfigurationFromListing() {
                var $configurationRow = $('#configurationNameTable .selected');
                if ($configurationRow.length > 0) {
                    var configurationName = $configurationRow[0].children[0].textContent;
                    CONFIGURATION.loadConfigurationFromListing(configurationName, userState.accountName, function(result) {
                        if (result.rc === 'ok') {
                            $('#tabConfiguration').one('shown.bs.tab', function() {
                                showConfiguration(result, true, configurationName);
                            });
                            $('#tabConfiguration').trigger('click');
                            userState.configurationSaved = 'new';
                            $('#menuSaveConfig').parent().addClass('disabled');
                            BRICKLY.getBricklyWorkspace().robControls.disable('saveProgram');
                            ROBERTA_ROBOT.setState(result);
                            LOG.info('loadFromConfigurationList ' + configurationName + ' signed in: ' + userState.id);
                        }
                        MSG.displayInformation(result, "", result.message);
                    });
                }
            }

            /**
             * Show configuration in brickly iframe
             * 
             * @param {result}
             *            Result-object of server call
             * @param {load}
             *            load configuration into workspace
             * @param {name}
             *            name of configuration
             */
            function showConfiguration(result, load, name) {
                UTIL.response(result);
                if (result.rc === 'ok') {
                    ROBERTA_NAVIGATION.switchToBrickly();
                    setConfiguration(name);
                    BRICKLY.showConfiguration(result.data, load);
                    LOG.info('show configuration ' + userState.configuration + ' signed in: ' + userState.id);
                }
            }

            /**
             * Delete the configurations that were selected in configurations
             * list
             */
            function deleteConfigurationFromListing() {
                var $configurationRow = $('#configurationNameTable .selected');
                for (var i = 0; i < $configurationRow.length; i++) {
                    var configurationName = $configurationRow[i].children[0].textContent;
                    LOG.info('deleteFromConfigurationList ' + configurationName + ' signed in: ' + userState.id);
                    CONFIGURATION.deleteConfigurationFromListing(configurationName, function(result) {
                        if (result.rc === 'ok') {
                            UTIL.response(result);
                            CONFIGURATION.refreshList(showConfigurations);
                            ROBERTA_ROBOT.setState(result);
                        }
                        MSG.displayInformation(result, "MESSAGE_CONFIGURATION_DELETED", result.message, configurationName);
                    });
                }
            }

            /**
             * Display configurations in a table
             * 
             * @param {result}
             *            result object of server call
             */
            function showConfigurations(result) {
                UTIL.response(result);
                if (result.rc === 'ok') {
                    var $table = $('#configurationNameTable').dataTable();
                    $table.fnClearTable();
                    if (result.configurationNames.length > 0) {
                        $table.fnAddData(result.configurationNames);
                    }
                    ROBERTA_ROBOT.setState(result);
                }
            }
            exports.showConfigurations = showConfigurations;

            function initBrickConfigurationForms() {
                $formSingleModal = $('#single-modal-form');

                // load configuration
                $('#loadConfigurationFromListing').onWrap('click', function() {
                    ROBERTA_NAVIGATION.activateProgConfigMenu();
                    loadConfigurationFromListing();
                }, 'load configuration from configuration list');

                // delete configuration
                $('#doDeleteConfiguration').onWrap('click', function() {
                    deleteConfigurationFromListing();
                    $('.modal').modal('hide'); // close all opened popups
                }, 'delete configuration from configurations list');

                // confirm configuration deletion
                $('#deleteConfigurationFromListing').onWrap('click', function() {
                    if ($('#configurationNameTable .selected').length > 0) {
                        $("#confirmDeleteConfiguration").modal("show");
                    }
                }, 'Ask for confirmation to delete a configuration');
            }
            exports.initBrickConfigurationForms = initBrickConfigurationForms;

            function save() {
                saveConfigurationToServer();
            }
            exports.save = save;

            function showSaveAsModal() {
                $.validator.addMethod("regex", function(value, element, regexp) {
                    value = value.trim();
                    return value.match(regexp);
                }, "No special Characters allowed here. Use only upper and lowercase letters (A through Z; a through z) and numbers.");

                UTIL.showSingleModal(function() {
                    $('#singleModalInput').attr('type', 'text');
                    $('#single-modal h3').text(Blockly.Msg["MENU_SAVE_AS"]);
                    $('#single-modal label').text(Blockly.Msg["POPUP_NAME"]);
                }, saveAsConfigurationToServer, function() {

                }, {
                    rules : {
                        singleModalInput : {
                            required : true,
                            regex : /^[a-zA-Z_öäüÖÄÜß$€][a-zA-Z0-9_öäüÖÄÜß$€]*$/
                        }
                    },
                    errorClass : "form-invalid",
                    errorPlacement : function(label, element) {
                        label.insertAfter(element);
                    },
                    messages : {
                        singleModalInput : {
                            required : jQuery.validator.format(Blockly.Msg["VALIDATION_FIELD_REQUIRED"]),
                            regex : jQuery.validator.format(Blockly.Msg["MESSAGE_INVALID_NAME"])
                        }
                    }
                });
            }
            exports.showSaveAsModal = showSaveAsModal;

            /**
             * Set configuration name
             * 
             * @param {name}
             *            Name to be set
             */
            function setConfiguration(name) {
                if (name) {
                    userState.configuration = name;
                    $('#tabConfigurationName').text(name);
                }
            }
            exports.setConfiguration = setConfiguration;

            /**
             * Save configuration to server
             * 
             * @param {name}
             *            configuration name
             * 
             */
            function getXmlOfConfiguration() {
                var xml = Blockly.Xml.workspaceToDom(BRICKLY.getBricklyWorkspace());
                var xml_text = Blockly.Xml.domToText(xml);
                return xml_text;
            }
            exports.getXmlOfConfiguration = getXmlOfConfiguration;

            /**
             * New program
             */
            function newConfiguration(opt_further) {
                var further = opt_further || false;
                if (further || userState.configurationSaved) {
                    BRICKLY.initConfigurationEnvironment();
                    setConfiguration(userState.robot.toUpperCase() + "basis");
                    $('#menuSaveConfig').parent().addClass('disabled');
                    BRICKLY.getBricklyWorkspace().robControls.disable('saveProgram');
                } else {
                    $('#confirmContinue').data('type', 'configuration');
                    if (userState.id === -1) {
                        MSG.displayMessage("POPUP_BEFOREUNLOAD", "POPUP", "", true);
                    } else {
                        MSG.displayMessage("POPUP_BEFOREUNLOAD_LOGGEDIN", "POPUP", "", true);
                    }
                }
            }
            exports.newConfiguration = newConfiguration;
        });
