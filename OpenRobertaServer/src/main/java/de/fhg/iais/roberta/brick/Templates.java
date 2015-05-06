package de.fhg.iais.roberta.brick;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Templates {
    private static final Logger LOG = LoggerFactory.getLogger(Templates.class);
    private final Map<String, String> templates = new ConcurrentHashMap<>();

    public Templates() {
        String beginner = "" + " <toolbox_set id='toolbox' style='display: none'> " //
            + "  <category name='TOOLBOX_ACTION'> " //
            + "    <block type='robActions_motorDiff_on'>" //
            + "      <value name='POWER'>" //
            + "        <block type='math_number'>" //
            + "          <field name='NUM'>30</field>" //
            + "        </block>" //
            + "      </value>" //
            + "    </block>" //
            + "    <block type='robActions_motorDiff_on_for'>" //
            + "      <value name='POWER'>" //
            + "        <block type='math_number'>" //
            + "          <field name='NUM'>30</field>" //
            + "        </block>" //
            + "      </value>" //
            + "      <value name='DISTANCE'>" //
            + "        <block type='math_number'>" //
            + "          <field name='NUM'>20</field>" //
            + "        </block>" //
            + "      </value>" //
            + "    </block>" //
            + "    <block type='robActions_motorDiff_stop'>" //
            + "    </block>" //
            + "    <block type='robActions_motorDiff_turn'>" //
            + "      <value name='POWER'>" //
            + "        <block type='math_number'>" //
            + "          <field name='NUM'>30</field>" //
            + "        </block>" //
            + "      </value>" //
            + "    </block>" //
            + "    <block type='robActions_motorDiff_turn_for'>" //
            + "      <value name='POWER'>" //
            + "        <block type='math_number'>" //
            + "          <field name='NUM'>30</field>" //
            + "        </block>" //
            + "      </value>" //
            + "      <value name='DEGREE'>" //
            + "        <block type='math_number'>" //
            + "          <field name='NUM'>20</field>" //
            + "        </block>" //
            + "      </value>" //
            + "    </block>" //
            + "    <block type='robActions_display_text'>" //
            + "      <value name='OUT'>" //
            + "        <block type='text'>" //
            + "          <field name='TEXT'>Hallo</field>" //
            + "        </block>" //
            + "      </value>" //
            + "      <value name='COL'>" //
            + "        <block type='math_number'>" //
            + "          <field name='NUM'>0</field>" //
            + "        </block>" //
            + "      </value>" //
            + "      <value name='ROW'>" //
            + "        <block type='math_number'>" //
            + "          <field name='NUM'>0</field>" //
            + "        </block>" //
            + "      </value>" //
            + "    </block>" //
            + "    <block type='robActions_display_clear'>" //
            + "    </block>" //
            + "    <block type='robActions_play_tone'>" //
            + "      <value name='FREQUENCE'>" //
            + "        <block type='math_number'>" //
            + "          <field name='NUM'>300</field>" //
            + "        </block>" //
            + "      </value>" //
            + "      <value name='DURATION'>" //
            + "        <block type='math_number'>" //
            + "          <field name='NUM'>100</field>" //
            + "        </block>" //
            + "      </value>" //
            + "    </block>" //
            + "    <block type='robActions_play_setVolume'>" //
            + "      <value name='VOLUME'>" //
            + "        <block type='math_number'>" //
            + "          <field name='NUM'>50</field>" //
            + "        </block>" //
            + "      </value>" //
            + "    </block>" //
            + "    <block type='robActions_brickLight_on'></block>" //
            + "    <block type='robActions_brickLight_off'></block>" //
            + "    <block type='robActions_brickLight_reset'></block>" //
            + "  </category> " //
            + "  <category name='TOOLBOX_SENSOR'> "
            + "      <block type='robSensors_touch_isPressed'>"
            + "      </block>"
            + "      <block type='robSensors_ultrasonic_getSample'>"
            + "        <field name='SENSORPORT'>4</field> "
            + "      </block>"
            + "      <block type='robSensors_colour_getSample'>"
            + "        <field name='SENSORPORT'>3</field> "
            + "      </block>"
            + "      <block type='robSensors_infrared_getSample'>"
            + "        <field name='SENSORPORT'>4</field> "
            + "      </block>"
            + "      <block type='robSensors_encoder_reset'>"
            + "      </block>"
            + "      <block type='robSensors_encoder_getSample'>"
            + "      </block>"
            + "      <block type='robSensors_key_isPressed'>"
            + "      </block>"
            + "      <block type='robSensors_gyro_reset'>"
            + "        <field name='SENSORPORT'>2</field> "
            + "      </block>"
            + "      <block type='robSensors_gyro_getSample'>"
            + "        <field name='SENSORPORT'>2</field> "
            + "      </block>"
            + "      <block type='robSensors_timer_getSample'>"
            + "      </block>"
            + "      <block type='robSensors_timer_reset'>"
            + "      </block>" //
            + "  </category> "
            + "  <category name='TOOLBOX_CONTROL'> "
            + "      <block type='robControls_if'/> "
            + "      <block type='robControls_ifElse'/> "
            + "      <block type='robControls_loopForever'/> "
            + "      <block type='controls_repeat'> "
            + "      </block> "
            + "      <block type='robControls_wait_time'> "
            + "        <value name='WAIT'> "
            + "          <block type='math_number'> "
            + "            <field name='NUM'>500</field> "
            + "          </block> " //
            + "        </value> "
            + "      </block> "
            + "      <block type='robControls_wait_for'> "
            + "        <value name='WAIT0'> "
            + "          <block type='logic_compare'> "
            + "            <value name='A'> "
            + "              <block type='robSensors_getSample'> "
            + "              </block> " //
            + "            </value> "
            + "            <value name='B'> "
            + "              <block type='logic_boolean'> "
            + "              </block> " //
            + "            </value> "
            + "          </block> " //
            + "        </value> "
            + "      </block> " //
            + "  </category> "
            + "  <category name='TOOLBOX_LOGIC'> "
            + "    <block type='logic_compare'/> "
            + "    <block type='logic_operation'/> "
            + "    <block type='logic_boolean'/> " //
            + "  </category> "
            + "  <category name='TOOLBOX_MATH'> "
            + "    <block type='math_number'/> "
            + "    <block type='math_arithmetic'/> " //
            + "  </category> "
            + "  <category name='TOOLBOX_TEXT'> " //
            + "    <block type='text'/> "
            + "  </category> " //
            + "  <category name='TOOLBOX_COLOUR'> "
            + "    <block type='robColour_picker'> "
            + "      <field name='COLOUR'>#585858</field> "
            + "    </block> " //
            + "    <block type='robColour_picker'> "
            + "      <field name='COLOUR'>#000000</field> "
            + "    </block> " //
            + "    <block type='robColour_picker'> "
            + "      <field name='COLOUR'>#0057a6</field> "
            + "    </block> " //
            + "    <block type='robColour_picker'> "
            + "      <field name='COLOUR'>#00642e</field> "
            + "    </block> " //
            + "    <block type='robColour_picker'> "
            + "      <field name='COLOUR'>#f7d117</field> "
            + "    </block> " //
            + "    <block type='robColour_picker'> "
            + "      <field name='COLOUR'>#b30006</field> "
            + "    </block> " //
            + "    <block type='robColour_picker'> "
            + "      <field name='COLOUR'>#FFFFFF</field> "
            + "    </block> " //
            + "    <block type='robColour_picker'> "
            + "      <field name='COLOUR'>#532115</field> "
            + "    </block> " //
            + "  </category> "
            + "  <category name='TOOLBOX_VARIABLE' custom='VARIABLE'/> "
            + "</toolbox_set> ";
        String expert =
            ""
                + " <toolbox_set id='toolbox' style='display: none'> "
                + "  <category name='TOOLBOX_ACTION'> "
                + "    <category name='TOOLBOX_MOVE'> "
                + "      <block type='robActions_motor_on'>" //
                + "        <field name='MOTORPORT'>B</field> "
                + "          <value name='POWER'>" //
                + "            <block type='math_number'>" //
                + "              <field name='NUM'>30</field>" //
                + "            </block>" //
                + "          </value>" //
                + "      </block>" //
                + "      <block type='robActions_motor_on_for'>" //
                + "        <field name='MOTORPORT'>B</field> "
                + "        <value name='POWER'>" //
                + "          <block type='math_number'>" //
                + "            <field name='NUM'>30</field>" //
                + "          </block>" //
                + "        </value>" //
                + "        <value name='VALUE'>" //
                + "          <block type='math_number'>" //
                + "            <field name='NUM'>1</field>" //
                + "          </block>" //
                + "        </value>" //
                + "      </block>" //
                + "      <block type='robActions_motor_getPower'>" //
                + "        <field name='MOTORPORT'>B</field> "
                + "      </block>" //
                // + "    <block type='robActions_motor_setPower'>" //
                // + "        <field name='MOTORPORT'>B</field> "
                // + "      <value name='POWER'>" //
                // + "        <block type='math_number'>" //
                // + "          <field name='NUM'>30</field>" //
                // + "        </block>" //
                // + "      </value>" //
                // + "    </block>" //
                + "      <block type='robActions_motor_stop'>" //
                + "        <field name='MOTORPORT'>A</field> "
                + "      </block>" //
                + "    </category> "
                + "    <category name='TOOLBOX_DRIVE'> "
                + "      <block type='robActions_motorDiff_on'>" //
                + "        <value name='POWER'>" //
                + "          <block type='math_number'>" //
                + "            <field name='NUM'>30</field>" //
                + "          </block>" //
                + "        </value>" //
                + "      </block>" //
                + "      <block type='robActions_motorDiff_on_for'>" //
                + "        <value name='POWER'>" //
                + "          <block type='math_number'>" //
                + "            <field name='NUM'>30</field>" //
                + "          </block>" //
                + "        </value>" //
                + "        <value name='DISTANCE'>" //
                + "          <block type='math_number'>" //
                + "            <field name='NUM'>20</field>" //
                + "          </block>" //
                + "        </value>" //
                + "      </block>" //
                + "      <block type='robActions_motorDiff_stop'>" //
                + "      </block>" //
                + "      <block type='robActions_motorDiff_turn'>" //
                + "        <value name='POWER'>" //
                + "          <block type='math_number'>" //
                + "            <field name='NUM'>30</field>" //
                + "          </block>" //
                + "        </value>" //
                + "      </block>" //
                + "      <block type='robActions_motorDiff_turn_for'>" //
                + "        <value name='POWER'>" //
                + "          <block type='math_number'>" //
                + "            <field name='NUM'>30</field>" //
                + "          </block>" //
                + "        </value>" //
                + "        <value name='DEGREE'>" //
                + "          <block type='math_number'>" //
                + "            <field name='NUM'>20</field>" //
                + "          </block>" //
                + "        </value>" //
                + "      </block>" //
                + "    </category> " //
                + "    <category name='TOOLBOX_DISPLAY'> "
                + "      <block type='robActions_display_text'>" //
                + "        <value name='OUT'>" //
                + "          <block type='text'>" //
                + "            <field name='TEXT'>Hallo</field>" //
                + "          </block>" //
                + "        </value>" //
                + "        <value name='COL'>" //
                + "          <block type='math_number'>" //
                + "            <field name='NUM'>0</field>" //
                + "          </block>" //
                + "        </value>" //
                + "        <value name='ROW'>" //
                + "          <block type='math_number'>" //
                + "            <field name='NUM'>0</field>" //
                + "          </block>" //
                + "        </value>" //
                + "      </block>" //
                + "      <block type='robActions_display_picture'>" //
                + "        <value name='X'>" //
                + "          <block type='math_number'>" //
                + "            <field name='NUM'>0</field>" //
                + "          </block>" //
                + "        </value>" //
                + "        <value name='Y'>" //
                + "          <block type='math_number'>" //
                + "            <field name='NUM'>0</field>" //
                + "          </block>" //
                + "        </value>" //
                + "      </block>" //
                + "      <block type='robActions_display_clear'>" //
                + "      </block>" //
                + "    </category> "
                + "    <category name='TOOLBOX_SOUND'> "
                + "      <block type='robActions_play_tone'>" //
                + "        <value name='FREQUENCE'>" //
                + "          <block type='math_number'>" //
                + "            <field name='NUM'>300</field>" //
                + "          </block>" //
                + "        </value>" //
                + "        <value name='DURATION'>" //
                + "          <block type='math_number'>" //
                + "            <field name='NUM'>100</field>" //
                + "          </block>" //
                + "        </value>" //
                + "      </block>" //
                + "      <block type='robActions_play_file'>" //
                + "      </block>" //
                + "      <block type='robActions_play_setVolume'>" //
                + "        <value name='VOLUME'>" //
                + "          <block type='math_number'>" //
                + "            <field name='NUM'>50</field>" //
                + "          </block>" //
                + "        </value>" //
                + "      </block>" //
                + "      <block type='robActions_play_getVolume'>" //
                + "      </block>" //
                + "    </category> "
                + "    <category name='TOOLBOX_LIGHT'> "
                + "      <block type='robActions_brickLight_on'></block>" //
                + "      <block type='robActions_brickLight_off'></block>" //
                + "      <block type='robActions_brickLight_reset'></block>" //
                + "    </category> " //
                + "  </category> " //
                + "  <category name='TOOLBOX_SENSOR'> "
                + "    <block type='robSensors_touch_isPressed'>"
                + "    </block>"
                + "    <block type='robSensors_ultrasonic_getSample'>"
                + "      <field name='SENSORPORT'>4</field> " //
                + "    </block>"
                + "    <block type='robSensors_colour_getSample'>"
                + "      <field name='SENSORPORT'>3</field> " //
                + "    </block>"
                + "    <block type='robSensors_infrared_getSample'>"
                + "      <field name='SENSORPORT'>4</field> " //
                + "    </block>"
                + "    <block type='robSensors_encoder_reset'>"
                + "    </block>"
                + "    <block type='robSensors_encoder_getSample'>"
                + "    </block>"
                + "    <block type='robSensors_key_isPressed'>"
                + "    </block>" //
                + "    <block type='robSensors_gyro_reset'>"
                + "      <field name='SENSORPORT'>2</field> " //
                + "    </block>"
                + "    <block type='robSensors_gyro_getSample'>"
                + "      <field name='SENSORPORT'>2</field> " //
                + "    </block>"
                + "    <block type='robSensors_timer_getSample'>"
                + "    </block>" //
                + "    <block type='robSensors_timer_reset'>"
                + "    </block>" //
                + "  </category> "
                + "  <category name='TOOLBOX_CONTROL'> "
                + "    <category name='TOOLBOX_DECISION'> "
                + "      <block type='robControls_if'/> "
                + "      <block type='robControls_ifElse'/> "
                + "    </category> " //
                + "    <category name='TOOLBOX_LOOP'> "
                + "      <block type='robControls_loopForever'/> "
                + "      <block type='controls_repeat_ext'> "
                + "        <value name='TIMES'> "
                + "          <block type='math_number'> "
                + "            <field name='NUM'>10</field> "
                + "          </block> " //
                + "        </value> "
                + "      </block> "
                + "      <block type='controls_whileUntil'/> "
                + "      <block type='controls_for'> "
                + "        <value name='FROM'> "
                + "          <block type='math_number'> "
                + "            <field name='NUM'>1</field> "
                + "          </block> " //
                + "        </value> "
                + "        <value name='TO'> "
                + "          <block type='math_number'> "
                + "            <field name='NUM'>10</field> "
                + "          </block> " //
                + "        </value> "
                + "        <value name='BY'> "
                + "          <block type='math_number'> "
                + "            <field name='NUM'>1</field> "
                + "          </block> " //
                + "        </value> "
                + "      </block> " //
                + "      <block type='controls_forEach'/> "
                + "      <block type='controls_flow_statements'/> "
                + "    </category> " //
                + "    <category name='TOOLBOX_WAIT'> "
                + "      <block type='robControls_wait'> " //
                + "      </block> "
                + "      <block type='robControls_wait_time'> "
                + "        <value name='WAIT'> "
                + "          <block type='math_number'> "
                + "            <field name='NUM'>500</field> "
                + "          </block> " //
                + "        </value> "
                + "      </block> "
                + "      <block type='robControls_wait_for'> "
                + "        <value name='WAIT0'> "
                + "          <block type='logic_compare'> "
                + "            <value name='A'> "
                + "              <block type='robSensors_getSample'> "
                + "              </block> " //
                + "            </value> "
                + "            <value name='B'> "
                + "              <block type='logic_boolean'> "
                + "              </block> " //
                + "            </value> "
                + "          </block> " //
                + "        </value> "
                + "      </block> " //
                + "    </category> "
                // TODO implement in next release
                /*
                 * + "    <category name='Tasks'> " // +
                 * "      <block type='robControls_activity'></block> " // +
                 * "      <block type='robControls_start_activity'></block> " //
                 * + "    </category> " //
                 */+ "  </category> "
                + "  <category name='TOOLBOX_LOGIC'> "
                + "    <block type='logic_compare'/> "
                + "    <block type='logic_operation'/> "
                + "    <block type='logic_negate'/> "
                + "    <block type='logic_boolean'/> "
                + "    <block type='logic_null'/> "
                + "    <block type='logic_ternary'/> " //
                + "  </category> "
                + "  <category name='TOOLBOX_MATH'> "
                + "    <block type='math_number'/> "
                + "    <block type='math_arithmetic'/> "
                + "    <block type='math_single'/> "
                + "    <block type='math_trig'/> "
                + "    <block type='math_constant'/> "
                + "    <block type='math_number_property'/> "
                + "    <block type='math_change'> "
                + "      <value name='DELTA'> "
                + "        <block type='math_number'> "
                + "          <field name='NUM'>1</field> "
                + "        </block> " //
                + "      </value> " //
                + "    </block> "
                + "    <block type='math_round'/> "
                + "    <block type='math_on_list'/> "
                + "    <block type='math_modulo'/> "
                + "    <block type='math_constrain'> "
                + "      <value name='LOW'> "
                + "        <block type='math_number'> "
                + "          <field name='NUM'>1</field> "
                + "        </block> " //
                + "      </value> "
                + "      <value name='HIGH'> "
                + "        <block type='math_number'> "
                + "          <field name='NUM'>100</field> "
                + "        </block> " //
                + "      </value> " //
                + "    </block> "
                + "    <block type='math_random_int'> "
                + "      <value name='FROM'> "
                + "        <block type='math_number'> "
                + "          <field name='NUM'>1</field> "
                + "        </block> " //
                + "      </value> "
                + "      <value name='TO'> "
                + "        <block type='math_number'> "
                + "          <field name='NUM'>100</field> "
                + "        </block> " //
                + "      </value> " //
                + "    </block> "
                + "    <block type='math_random_float'/> " //
                + "  </category> "
                + "  <category name='TOOLBOX_TEXT'> " //
                + "    <block type='text'/> "
                + "    <block type='robText_join'/> "
                + "    <block type='text_append'> "
                + "      <value name='TEXT'> "
                + "        <block type='text'/> " //
                + "      </value> "
                + "    </block> " //
                + "  </category> "
                + "  <category name='TOOLBOX_LIST'> "
                + "    <block type='lists_create_empty'/> "
                + "    <block type='robLists_create_with'> "
                + "      <value name='ADD0'>"
                + "        <block type='math_number'></block> "
                + "      </value> "
                + "      <value name='ADD1'>"
                + "        <block type='math_number'></block> "
                + "      </value> "
                + "      <value name='ADD2'>"
                + "        <block type='math_number'></block> "
                + "      </value> "
                + "    </block> " //
                + "    <block type='lists_repeat'> "
                + "      <value name='NUM'> "
                + "        <block type='math_number'> "
                + "          <field name='NUM'>5</field> "
                + "        </block> " //
                + "      </value> " //
                + "    </block> "
                + "    <block type='lists_length'/> "
                + "    <block type='lists_isEmpty'/> "
                + "    <block type='lists_indexOf'> " //
                + "    </block> "
                + "    <block type='lists_getIndex'> " //
                + "    </block> "
                + "    <block type='lists_setIndex'> " //
                + "    </block> "
                + "    <block type='lists_getSublist'> " //
                + "    </block> "
                + "  </category> " //
                + "  <category name='TOOLBOX_COLOUR'> "
                + "    <block type='robColour_picker'> " //
                + "    </block> "
                + "  </category> "
                + "  <category name='TOOLBOX_VARIABLE' custom='VARIABLE'/> "
                + "  <category name='TOOLBOX_PROCEDURE' custom='PROCEDURE'/> "
                + "  <category name='TOOLBOX_COMMUNICATION'> "
                + "     <block type='robCommunication_startConnection'></block>"
                + "     <block type='robCommunication_sendBlock'></block>"
                + "     <block type='robCommunication_receiveBlock'></block>"
                + "     <block type='robCommunication_waitForConnection'></block>"
                + " </category>"
                + "</toolbox_set> ";
        String brickEV3 =
            ""
                + "<toolbox_set id='toolbox' style='display: none'>"
                + "  <block type='robBrick_ultrasonic'></block>"
                + "  <block type='robBrick_colour'></block>"
                + "  <block type='robBrick_infrared'></block>"
                + "  <block type='robBrick_touch'></block>"
                + "  <block type='robBrick_gyro'></block>"
                + "  <block type='robBrick_motor_middle'></block>"
                + "  <block type='robBrick_motor_big'>"
                + "    <field name='MOTOR_DRIVE'>RIGHT</field>" //
                + "  </block>" //
                + "  <block type='robBrick_actor'></block>" //
                + "</toolbox_set>";
        this.templates.put("beginner", beginner);
        this.templates.put("expert", expert);
        this.templates.put("brickEV3", brickEV3);
        LOG.info("created");
    }

    public String get(String key) {
        return this.templates.get(key);
    }
}