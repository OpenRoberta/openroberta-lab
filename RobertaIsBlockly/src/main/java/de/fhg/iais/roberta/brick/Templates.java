package de.fhg.iais.roberta.brick;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

@Singleton
public class Templates {
    private static final Logger LOG = LoggerFactory.getLogger(Templates.class);
    private final Map<String, String> templates = new ConcurrentHashMap<>();

    public Templates() {
        String eins = "" //
            + "<xml id='toolbox' style='display: none'>" //
            + "  <category name='Aktion'>" //
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
            + "          <field name='NUM'>10</field>" //
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
            + "      <value name='DISTANCE'>" //
            + "        <block type='math_number'>" //
            + "          <field name='NUM'>10</field>" //
            + "        </block>" //
            + "      </value>" //
            + "    </block>" //
            + "    <block type='robActions_display'>" //
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
            + "    <block type='robActions_playTone'>" //
            + "      <value name='FREQUENZ'>" //
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
            + "    <block type='robActions_brickLight'></block>" //
            + "  </category>" //
            + "  <category name='Kontrolle'>" //
            + "    <block type='robControls_wait'>" //
            + "      <value name='VALUE'>" //
            + "        <block type='math_number'>" //
            + "          <field name='NUM'>25</field>" //
            + "        </block>" //
            + "      </value>" //
            + "    </block>" //
            + "    <block type='robControls_loopUntil'>" //
            + "      <value name='VALUE'>" //
            + "        <block type='math_number'>" //
            + "          <field name='NUM'>30</field>" //
            + "        </block>" //
            + "      </value>" //
            + "    </block>" //
            + "    <block type='robControls_loopForever'>" //
            + "    </block>" //
            + "    <block type='robControls_ifElse'>" //
            + "      <value name='VALUE'>" //
            + "        <block type='math_number'>" //
            + "          <field name='NUM'>25</field>" //
            + "        </block>" //
            + "      </value>" //
            + "    </block>" //
            + "  </category>" //
            + "</xml>";
        String zwei =
            ""
                + " <xml id='toolbox' style='display: none'> "
                + "  <category name='Aktion'> "
                + "  <category name='Bewegung'> "
                + "    <block type='robActions_motorBig_on'>" //
                + "        <field name='MOTORPORT'>B</field> "
                + "      <value name='POWER'>" //
                + "        <block type='math_number'>" //
                + "          <field name='NUM'>30</field>" //
                + "        </block>" //
                + "      </value>" //
                + "    </block>" //
                + "    <block type='robActions_motorBig_on_for'>" //
                + "        <field name='MOTORPORT'>B</field> "
                + "      <value name='POWER'>" //
                + "        <block type='math_number'>" //
                + "          <field name='NUM'>30</field>" //
                + "        </block>" //
                + "      </value>" //
                + "      <value name='VALUE'>" //
                + "        <block type='math_number'>" //
                + "          <field name='NUM'>1</field>" //
                + "        </block>" //
                + "      </value>" //
                + "    </block>" //
                + "    <block type='robActions_motorBig_getPower'>" //
                + "        <field name='MOTORPORT'>B</field> "
                + "    </block>" //
                + "    <block type='robActions_motorBig_setPower'>" //
                + "        <field name='MOTORPORT'>B</field> "
                + "      <value name='POWER'>" //
                + "        <block type='math_number'>" //
                + "          <field name='NUM'>30</field>" //
                + "        </block>" //
                + "      </value>" //
                + "    </block>" //
                + "    <block type='robActions_motorBig_stop'>" //
                + "        <field name='MOTORPORT'>A</field> "
                + "    </block>" //
                + "    <block type='robActions_motorMiddle_on'>" //
                + "        <field name='MOTORPORT'>A</field> "
                + "      <value name='POWER'>" //
                + "        <block type='math_number'>" //
                + "          <field name='NUM'>30</field>" //
                + "        </block>" //
                + "      </value>" //
                + "    </block>" //
                + "    <block type='robActions_motorMiddle_on_for'>" //
                + "        <field name='MOTORPORT'>A</field> "
                + "      <value name='POWER'>" //
                + "        <block type='math_number'>" //
                + "          <field name='NUM'>30</field>" //
                + "        </block>" //
                + "      </value>" //
                + "      <value name='VALUE'>" //
                + "        <block type='math_number'>" //
                + "          <field name='NUM'>1</field>" //
                + "        </block>" //
                + "      </value>" //
                + "    </block>" //
                + "    <block type='robActions_motorMiddle_getPower'>" //
                + "        <field name='MOTORPORT'>A</field> "
                + "    </block>" //
                + "    <block type='robActions_motorMiddle_setPower'>" //
                + "        <field name='MOTORPORT'>A</field> "
                + "      <value name='POWER'>" //
                + "        <block type='math_number'>" //
                + "          <field name='NUM'>30</field>" //
                + "        </block>" //
                + "      </value>" //
                + "    </block>" //
                + "    <block type='robActions_motorMiddle_stop'>" //
                + "        <field name='MOTORPORT'>A</field> "
                + "    </block>" //
                + "  </category> "
                + "  <category name='Anzeige'> "
                + "  </category> "
                + "  <category name='Klang'> "
                + "  </category> "
                + "  <category name='Statusleuchte'> "
                + "  </category> "
                + "  </category> "
                + "  <category name='Sensoren'> "
                + "    <category name='Berührungssensor'> "
                + "      <block type='robSensors_touch_isPressed'>"
                + "      </block>"
                + "    </category> "
                + "    <category name='Ultraschallsensor'> "
                + "      <block type='robSensors_ultrasonic_setMode'> "
                + "        <field name='SENSORPORT'>4</field> "
                + "      </block>"
                + "      <block type='robSensors_ultrasonic_getMode'>"
                + "        <field name='SENSORPORT'>4</field> "
                + "      </block>"
                + "      <block type='robSensors_ultrasonic_getSample'>"
                + "        <field name='SENSORPORT'>4</field> "
                + "      </block>"
                + "    </category> "
                + "    <category name='Farbsensor'> "
                + "      <block type='robSensors_colour_setMode'>"
                + "        <field name='SENSORPORT'>3</field> "
                + "      </block>"
                + "      <block type='robSensors_colour_getMode'>"
                + "        <field name='SENSORPORT'>3</field> "
                + "      </block>"
                + "      <block type='robSensors_colour_getSample'>"
                + "        <field name='SENSORPORT'>3</field> "
                + "      </block>"
                + "    </category> "
                + "    <category name='Infrarotsensor'> "
                + "      <block type='robSensors_infrared_setMode'>"
                + "        <field name='SENSORPORT'>4</field> "
                + "      </block>"
                + "      <block type='robSensors_infrared_getMode'>"
                + "        <field name='SENSORPORT'>4</field> "
                + "      </block>"
                + "      <block type='robSensors_infrared_getSample'>"
                + "        <field name='SENSORPORT'>4</field> "
                + "      </block>"
                + "    </category> "
                + "    <category name='Drehsensor'> "
                + "      <block type='robSensors_encoder_setMode'>"
                + "      </block>"
                + "      <block type='robSensors_encoder_getMode'>"
                + "      </block>"
                + "      <block type='robSensors_encoder_reset'>"
                + "      </block>"
                + "      <block type='robSensors_encoder_getSample'>"
                + "      </block>"
                + "    </category> "
                + "    <category name='Steintasten'> "
                + "      <block type='robSensors_key_isPressed'>"
                + "      </block>"
                + "      <block type='robSensors_key_waitForPress'>"
                + "      </block>"
                + "      <block type='robSensors_key_waitForPressAndRelease'>"
                + "      </block>"
                + "    </category> "
                + "    <category name='Kreiselsensor'> "
                + "      <block type='robSensors_gyro_setMode'>"
                + "        <field name='SENSORPORT'>2</field> "
                + "      </block>"
                + "      <block type='robSensors_gyro_getMode'>"
                + "        <field name='SENSORPORT'>2</field> "
                + "      </block>"
                + "      <block type='robSensors_gyro_reset'>"
                + "        <field name='SENSORPORT'>2</field> "
                + "      </block>"
                + "      <block type='robSensors_gyro_getSample'>"
                + "        <field name='SENSORPORT'>2</field> "
                + "      </block>"
                + "    </category> "
                + "    <category name='Zeitgeber'> "

                + "      <block type='robSensors_timer_getSample'>"
                + "      </block>"
                + "      <block type='robSensors_timer_reset'>"
                + "      </block>"
                + "    </category> "
                + "  </category> "
                + "  <category name='Kontrolle'> "
                + "    <category name='Entscheidung'> "
                + "      <block type='controls_if'/> "
                + "    </category> "
                + "    <category name='Schleifen'> "
                + "      <block type='controls_repeat_ext'> "
                + "        <value name='TIMES'> "
                + "          <block type='math_number'> "
                + "            <field name='NUM'>10</field> "
                + "          </block> "
                + "        </value> "
                + "      </block> "
                + "      <block type='controls_whileUntil'/> "
                + "      <block type='controls_for'> "
                + "        <value name='FROM'> "
                + "          <block type='math_number'> "
                + "            <field name='NUM'>1</field> "
                + "          </block> "
                + "        </value> "
                + "        <value name='TO'> "
                + "          <block type='math_number'> "
                + "            <field name='NUM'>10</field> "
                + "          </block> "
                + "        </value> "
                + "        <value name='BY'> "
                + "          <block type='math_number'> "
                + "            <field name='NUM'>1</field> "
                + "          </block> "
                + "        </value> "
                + "      </block> "
                + "      <block type='controls_forEach'/> "
                + "      <block type='controls_flow_statements'/> "
                + "    </category> "
                + "    <category name='Tasks'> "
                + "      <block type='robControls_activity'> "
                + "        <value name='ACTIVITY'> "
                + "          <block type='variables_get'> "
                + "            <field name='VAR'>zwei</field> "
                + "          </block> "
                + "        </value> "
                + "      </block> "
                + "      <block type='robControls_start_activity'> "
                + "        <value name='ACTIVITY'> "
                + "          <block type='variables_get'> "
                + "            <field name='VAR'>zwei</field> "
                + "          </block> "
                + "        </value> "
                + "      </block> "
                + "    </category> "
                + "  </category> "
                + "  <category name='Logik'> "
                + "    <block type='logic_compare'/> "
                + "    <block type='logic_operation'/> "
                + "    <block type='logic_negate'/> "
                + "    <block type='logic_boolean'/> "
                + "    <block type='logic_null'/> "
                + "    <block type='logic_ternary'/> "
                + "  </category> "
                + "  <category name='Mathematik'> "
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
                + "        </block> "
                + "      </value> "
                + "    </block> "
                + "    <block type='math_round'/> "
                + "    <block type='math_on_list'/> "
                + "    <block type='math_modulo'/> "
                + "    <block type='math_constrain'> "
                + "      <value name='LOW'> "
                + "        <block type='math_number'> "
                + "          <field name='NUM'>1</field> "
                + "        </block> "
                + "      </value> "
                + "      <value name='HIGH'> "
                + "        <block type='math_number'> "
                + "          <field name='NUM'>100</field> "
                + "        </block> "
                + "      </value> "
                + "    </block> "
                + "    <block type='math_random_int'> "
                + "      <value name='FROM'> "
                + "        <block type='math_number'> "
                + "          <field name='NUM'>1</field> "
                + "        </block> "
                + "      </value> "
                + "      <value name='TO'> "
                + "        <block type='math_number'> "
                + "          <field name='NUM'>100</field> "
                + "        </block> "
                + "      </value> "
                + "    </block> "
                + "    <block type='math_random_float'/> "
                + "  </category> "
                + "  <category name='Text'> "
                + "    <block type='text'/> "
                + "    <block type='text_join'/> "
                + "    <block type='text_append'> "
                + "      <value name='TEXT'> "
                + "        <block type='text'/> "
                + "      </value> "
                + "    </block> "
                + "    <block type='text_length'/> "
                + "    <block type='text_isEmpty'/> "
                + "    <block type='text_indexOf'> "
                + "      <value name='VALUE'> "
                + "        <block type='variables_get'> "
                + "          <field name='VAR'>text</field> "
                + "        </block> "
                + "      </value> "
                + "    </block> "
                + "    <block type='text_charAt'> "
                + "      <value name='VALUE'> "
                + "        <block type='variables_get'> "
                + "          <field name='VAR'>text</field> "
                + "        </block> "
                + "      </value> "
                + "    </block> "
                + "    <block type='text_getSubstring'> "
                + "      <value name='STRING'> "
                + "        <block type='variables_get'> "
                + "          <field name='VAR'>text</field> "
                + "        </block> "
                + "      </value> "
                + "    </block> "
                + "    <block type='text_changeCase'/> "
                + "    <block type='text_trim'/> "
                // + "    <block type='text_print'/> " TODO Ausgabe Display
                + "    <block type='text_prompt'/> "
                + "  </category> "
                + "  <category name='Listen'> "
                + "    <block type='lists_create_empty'/> "
                + "    <block type='lists_create_with'/> "
                + "    <block type='lists_repeat'> "
                + "      <value name='NUM'> "
                + "        <block type='math_number'> "
                + "          <field name='NUM'>5</field> "
                + "        </block> "
                + "      </value> "
                + "    </block> "
                + "    <block type='lists_length'/> "
                + "    <block type='lists_isEmpty'/> "
                + "    <block type='lists_indexOf'> "
                + "      <value name='VALUE'> "
                + "        <block type='variables_get'> "
                + "          <field name='VAR'>liste</field> "
                + "        </block> "
                + "      </value> "
                + "    </block> "
                + "    <block type='lists_getIndex'> "
                + "      <value name='VALUE'> "
                + "        <block type='variables_get'> "
                + "          <field name='VAR'>liste</field> "
                + "        </block> "
                + "      </value> "
                + "    </block> "
                + "    <block type='lists_setIndex'> "
                + "      <value name='LIST'> "
                + "        <block type='variables_get'> "
                + "          <field name='VAR'>liste</field> "
                + "        </block> "
                + "      </value> "
                + "    </block> "
                + "    <block type='lists_getSublist'> "
                + "      <value name='LIST'> "
                + "        <block type='variables_get'> "
                + "          <field name='VAR'>liste</field> "
                + "        </block> "
                + "      </value> "
                + "    </block> "
                + "  </category> "
                + "  <category name='Variablen' custom='VARIABLE'/> "
                + "  <category name='Funktionen' custom='PROCEDURE'/> "
                + "</xml> ";
        this.templates.put("1", eins);
        this.templates.put("2", zwei);
        LOG.info("created");
    }

    public String get(String key) {
        return this.templates.get(key);
    }
}