<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:b="http://de.fhg.iais.roberta.blockly" version="1.0">
    <xsl:output method="xml" omit-xml-declaration="yes" indent="yes" />
    <xsl:strip-space elements="*" />
    <!-- identity -->
    <xsl:template match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()" />
        </xsl:copy>
    </xsl:template>
    <!-- block_set attributes may be missing -->
    <xsl:template match="b:block_set">
        <xsl:copy>
            <xsl:if test="not(./@xmlversion)">
                <xsl:attribute name="xmlversion">2.0</xsl:attribute>
            </xsl:if>
            <xsl:if test="not(./@robottype)">
                <xsl:attribute name="robottype">ev3</xsl:attribute>
            </xsl:if>

            <xsl:if test="not(./@tags)">
                <xsl:attribute name="tags" />
            </xsl:if>
            <xsl:if test="not(./@description)">
                <xsl:attribute name="description" />
            </xsl:if>
            <xsl:apply-templates select="@* | node()" />
        </xsl:copy>
    </xsl:template>
    <!-- description should not be escaped -->
    <xsl:template match="b:block_set/@description">
        <xsl:copy>
            <xsl:value-of select="." disable-output-escaping="yes" />
        </xsl:copy>
    </xsl:template>
    <!-- intask attribute may be missing -->
    <xsl:template match="b:block">
        <xsl:copy>
            <xsl:apply-templates select="@*" />
            <xsl:if test="not(./@intask)">
                <xsl:attribute name="intask">true</xsl:attribute>
            </xsl:if>
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <!-- debug flag may be missing -->
    <xsl:template match="b:block[./@type='robControls_start' and (ancestor::b:block_set/@robottype = 'ev3' or not(ancestor::b:block_set/@robottype))]">
        <xsl:copy>
            <!-- intask attribute may be missing -->
            <xsl:if test="not(./@intask)">
                <xsl:attribute name="intask">true</xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="@*" />
            <xsl:apply-templates select="b:mutation" />
            <xsl:if test="not(./b:mutation[./@declare])">
                <xsl:element name="{'mutation'}" namespace="">
                    <xsl:attribute name="declare">false</xsl:attribute>
                </xsl:element>
            </xsl:if>
            <xsl:if test="not(./b:field[./@name = 'DEBUG'])">
                <xsl:element name="{'field'}" namespace="">
                    <xsl:attribute name="name">DEBUG</xsl:attribute>
                    <xsl:text>FALSE</xsl:text>
                </xsl:element>
            </xsl:if>
            <xsl:apply-templates select="*[not(self::b:mutation)]" />
        </xsl:copy>
    </xsl:template>
    <!-- _getSample blocks have a different structure, they always need port, slot and mode, sometimes the mutation needs to be adapted as well -->
    <xsl:template match="b:block[contains(./@type, '_getSample')]">
        <xsl:copy>
            <xsl:apply-templates select="@*" />
            <!-- intask attribute may be missing -->
            <xsl:if test="not(./@intask)">
                <xsl:attribute name="intask">true</xsl:attribute>
            </xsl:if>
            <xsl:variable name="newMode">
                <xsl:choose>
                    <!-- treat ROLL differently -->
                    <xsl:when test="./b:field[./@name = 'MODE']/text() = 'ROLL'">
                        <xsl:text>ANGLE</xsl:text>
                    </xsl:when>
                    <!-- use the existing mode if one exists -->
                    <xsl:when test="./b:mutation/@input">
                        <xsl:value-of select="./b:mutation/@input" />
                    </xsl:when>
                    <xsl:when
                        test="./b:field[./@name != 'KEY' and ./@name != 'SENSORPORT' and ./@name != 'PIN' and ./@name != 'ARM' and ./@name != 'ARMPAIR' and ./@name != 'DIRECTION']">
                        <xsl:value-of select="./b:field" />
                    </xsl:when>
                    <!-- otherwise use default values -->
                    <xsl:when test="./@type = 'robSensors_accelerometer_getSample'">
                        <xsl:choose>
                            <xsl:when
                                test="ancestor::b:block_set/@robottype = 'mbot' or ancestor::b:block_set/@robottype = 'sensebox' or ancestor::b:block_set/@robottype = 'arduino'">
                                <xsl:text>X</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>VALUE</xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:when test="./@type = 'robSensors_battery_getSample'">VALUE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_code_getSample'">VALUE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_colour_getSample'">COLOUR</xsl:when>
                    <xsl:when test="./@type = 'robSensors_compass_getSample'">
                        <xsl:choose>
                            <xsl:when test="ancestor::b:block_set/@robottype = 'sensebox'">X</xsl:when>
                            <xsl:otherwise>ANGLE</xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:when test="./@type = 'robSensors_detectface_getSample'">NAMEONE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_drop_getSample'">VALUE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_drop_off_getSample'">DISTANCE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_detectmark_getSample'">IDONE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_encoder_getSample'">DEGREE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_electriccurrent_getSample'">VALUE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_fsr_getSample'">VALUE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_gesture_getSample'">UP</xsl:when>
                    <xsl:when test="./@type = 'robSensors_gyro_getSample'">
                        <xsl:choose>
                            <xsl:when
                                test="ancestor::b:block_set/@robottype = 'mbot' or ancestor::b:block_set/@robottype = 'sensebox' or ancestor::b:block_set/@robottype = 'arduino'">
                                <xsl:text>X</xsl:text>
                            </xsl:when>
                            <xsl:when test="ancestor::b:block_set/@robottype = 'wedo'">TILTED</xsl:when>
                            <xsl:otherwise>ANGLE</xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:when test="./@type = 'robSensors_htcolour_getSample'">COLOUR</xsl:when>
                    <xsl:when test="./@type = 'robSensors_humidity_getSample'">HUMIDITY</xsl:when>
                    <xsl:when test="./@type = 'robSensors_infrared_getSample'">
                        <xsl:choose>
                            <xsl:when test="ancestor::b:block_set/@robottype = 'mbot' or ancestor::b:block_set/@robottype = 'calliope'">LINE</xsl:when>
                            <xsl:when test="ancestor::b:block_set/@robottype = 'bob3'">AMBIENTLIGHT</xsl:when>
                            <xsl:when test="ancestor::b:block_set/@robottype = 'arduino'">VALUE</xsl:when>
                            <xsl:when test="ancestor::b:block_set/@robottype = 'botnroll' or ancestor::b:block_set/@robottype = 'edison'">OBSTACLE</xsl:when>
                            <xsl:otherwise>DISTANCE</xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:when test="./@type = 'robSensors_irseeker_getSample'">MODULATED</xsl:when>
                    <xsl:when test="./@type = 'robSensors_key_getSample'">PRESSED</xsl:when>
                    <xsl:when test="./@type = 'robSensors_light_getSample'">
                        <xsl:choose>
                            <xsl:when test="ancestor::b:block_set/@robottype = 'calliope' or ancestor::b:block_set/@robottype = 'microbit'">VALUE</xsl:when>
                            <xsl:otherwise>LIGHT</xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:when test="./@type = 'robSensors_lightveml_getSample'">LIGHT</xsl:when>
                    <xsl:when test="./@type = 'robSensors_moisture_getSample'">VALUE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_motion_getSample'">PRESENCE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_out_getSample'">ANALOG</xsl:when>
                    <xsl:when test="./@type = 'robSensors_pin_getSample'">ANALOG</xsl:when>
                    <xsl:when test="./@type = 'robSensors_potentiometer_getSample'">VALUE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_pintouch_getSample'">PRESSED</xsl:when>
                    <xsl:when test="./@type = 'robSensors_pulse_getSample'">VALUE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_rssi_getSample'">VALUE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_rfid_getSample'">IDONE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_sound_getSample'">SOUND</xsl:when>
                    <xsl:when test="./@type = 'robSensors_temperature_getSample'">
                        <xsl:choose>
                            <xsl:when test="ancestor::b:block_set/@robottype = 'mbot'
                            or ancestor::b:block_set/@robottype = 'calliope'
                            or ancestor::b:block_set/@robottype = 'microbit'">VALUE
                            </xsl:when>
                            <xsl:otherwise>TEMPERATURE</xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:when test="./@type = 'robSensors_timer_getSample'">VALUE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_touch_getSample'">PRESSED</xsl:when>
                    <xsl:when test="./@type = 'robSensors_ultrasonic_getSample'">DISTANCE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_wall_getSample'">DISTANCE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_flame_getSample'">VALUE</xsl:when>
                    <xsl:when test="./@type = 'robSensors_joystick_getSample'">X</xsl:when>
                    <xsl:when test="./@type = 'robSensors_particle_getSample'">PM25</xsl:when>
                    <xsl:when test="./@type = 'robSensors_gps_getSample'">LATITUDE</xsl:when>
                </xsl:choose>
            </xsl:variable>
            <xsl:variable name="newPort">
                <xsl:choose>
                    <xsl:when
                        test="./b:mutation/@input = 'GYRO_ANGLE' and (ancestor::b:block_set/@robottype = 'calliope' or ancestor::b:block_set/@robottype = 'microbit')">
                        X
                    </xsl:when>
                    <xsl:when test=".. = 'ANGLE' and (ancestor::b:block_set/@robottype = 'calliope' or ancestor::b:block_set/@robottype = 'microbit')">X
                    </xsl:when>
                    <xsl:when test=".. = 'ROLL' and (ancestor::b:block_set/@robottype = 'calliope' or ancestor::b:block_set/@robottype = 'microbit')">Y
                    </xsl:when>
                    <xsl:when
                        test="./b:field[./@name = 'SENSORPORT' or ./@name = 'KEY' or ./@name = 'PIN' or ./@name = 'ARM' or ./@name = 'DIRECTION' or ./@name = 'MOTORPORT']">
                        <xsl:value-of
                            select="./b:field[./@name = 'SENSORPORT' or ./@name = 'KEY' or ./@name = 'PIN' or ./@name = 'ARM' or ./@name = 'DIRECTION' or ./@name = 'MOTORPORT']" />
                    </xsl:when>
                    <xsl:when
                        test="./b:field[./@name = 'SENSORTYPE'] = 'NAO_RECOGNIZEWORD' or ./b:field[./@name = 'SENSORTYPE'] = 'NAO_TOUCHSENSOR' and ancestor::b:block_set/@robottype = 'nao'">
                        <xsl:text>HEAD</xsl:text>
                    </xsl:when>
                    <xsl:otherwise />
                </xsl:choose>
            </xsl:variable>
            <xsl:variable name="newSlot">
                <xsl:choose>
                    <xsl:when test="./b:field[./@name = 'ARMPAIR'] and ancestor::b:block_set/@robottype = 'bob3'">
                        <xsl:value-of select="./b:field[./@name = 'ARMPAIR']" />
                    </xsl:when>
                    <xsl:when
                        test="(./b:field[./@name = 'SENSORTYPE'] = 'NAO_RECOGNIZEWORD' or ./b:field[./@name = 'SENSORTYPE'] = 'NAO_TOUCHSENSOR') and ancestor::b:block_set/@robottype = 'nao'">
                        <xsl:text>FRONT</xsl:text>
                    </xsl:when>
                    <xsl:otherwise />
                </xsl:choose>
            </xsl:variable>
            <xsl:choose>
                <xsl:when test="./b:mutation and not(./b:mutation/@protocol)">
                    <xsl:copy-of select="./b:mutation" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:element name="{'mutation'}" namespace="">
                        <xsl:attribute name="mode">
                            <xsl:choose>
                                <xsl:when test="./@type = 'robSensors_encoder_getSample'">
                                    <xsl:text>DEGREE</xsl:text>
                                </xsl:when>
                                <xsl:when
                                    test="./@type = 'robSensors_gyro_getSample' and (ancestor::b:block_set/@robottype = 'calliope' or ancestor::b:block_set/@robottype = 'microbit')">
                                    <xsl:text>ANGLE</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="$newMode" />
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                    </xsl:element>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="(./b:field/@name = 'MODE' or ./b:field/@name = 'SENSORTYPE')
                 and (not(./b:field[@name = 'MODE']/text() = 'ROLL') and (ancestor::b:block_set/@robottype = 'calliope' or ancestor::b:block_set/@robottype = 'microbit'))">
                    <xsl:copy-of select="./b:field[./@name = 'MODE']" />
                </xsl:when>
                <!-- do not output MODE if SENSORTYPE already exists -->
                <xsl:when test="./b:field/@name = 'SENSORTYPE'" />
                <xsl:otherwise>
                    <xsl:element name="{'field'}" namespace="">
                        <xsl:attribute name="name">MODE</xsl:attribute>
                        <xsl:value-of select="$newMode" />
                    </xsl:element>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:copy-of select="./b:field[./@name = 'SENSORTYPE']" />
            <xsl:choose>
                <xsl:when test="./b:field/@name = 'SENSORPORT'">
                    <xsl:copy-of select="./b:field[./@name = 'SENSORPORT']" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:element name="{'field'}" namespace="">
                        <xsl:attribute name="name">SENSORPORT</xsl:attribute>
                        <xsl:if test="$newPort">
                            <xsl:value-of select="$newPort" />
                        </xsl:if>
                    </xsl:element>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="./b:field/@name = 'SLOT'">
                    <xsl:copy-of select="./b:field[./@name = 'SLOT']" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:element name="{'field'}" namespace="">
                        <xsl:attribute name="name">SLOT</xsl:attribute>
                        <xsl:if test="$newSlot">
                            <xsl:value-of select="$newSlot" />
                        </xsl:if>
                    </xsl:element>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
    <!-- some action blocks may need additional fields -->
    <xsl:template match="b:block[contains(./@type, 'Actions')]">
        <xsl:copy>
            <xsl:apply-templates select="@*" />
            <!-- intask attribute may be missing -->
            <xsl:if test="not(./@intask)">
                <xsl:attribute name="intask">true</xsl:attribute>
            </xsl:if>
            <xsl:if test="./@type = 'robActions_write_pin'">
                <xsl:if test="not(b:mutation)">
                    <xsl:element name="{'mutation'}" namespace="">
                        <xsl:attribute name="protocol">
                            <xsl:value-of select="b:field[./@name = 'MODE']" />
                        </xsl:attribute>
                    </xsl:element>
                </xsl:if>
            </xsl:if>
            <xsl:if test="./@type = 'mbedActions_leds_off' or ./@type = 'mbedActions_leds_on'">
                <xsl:if test="not(b:field/@name = 'ACTORPORT')">
                    <xsl:element name="{'field'}" namespace="">
                        <xsl:attribute name="name">ACTORPORT</xsl:attribute>
                        <xsl:text>0</xsl:text>
                    </xsl:element>
                </xsl:if>
            </xsl:if>
            <xsl:if test="./@type = 'mbedActions_display_text'">
                <xsl:if test="not(b:field/@name = 'TYPE')">
                    <xsl:element name="{'field'}" namespace="">
                        <xsl:attribute name="name">TYPE</xsl:attribute>
                        <xsl:text>TEXT</xsl:text>
                    </xsl:element>
                </xsl:if>
            </xsl:if>
            <xsl:if test="./@type = 'mbedActions_motors_on'">
                <xsl:if test="not(b:field/@name = 'A')">
                    <xsl:element name="{'field'}" namespace="">
                        <xsl:attribute name="name">A</xsl:attribute>
                        <xsl:text>A</xsl:text>
                    </xsl:element>
                </xsl:if>
            </xsl:if>
            <xsl:if test="./@type = 'mbedActions_motors_on'">
                <xsl:if test="not(b:field/@name = 'A')">
                    <xsl:element name="{'field'}" namespace="">
                        <xsl:attribute name="name">B</xsl:attribute>
                        <xsl:text>B</xsl:text>
                    </xsl:element>
                </xsl:if>
            </xsl:if>
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <!-- mbedCommunication blocks may need additional fields -->
    <xsl:template match="b:block[./@type = 'mbedCommunication_receiveBlock' or ./@type = 'mbedCommunication_sendBlock']">
        <xsl:copy>
            <xsl:apply-templates select="@*" />
            <!-- intask attribute may be missing -->
            <xsl:if test="not(./@intask)">
                <xsl:attribute name="intask">true</xsl:attribute>
            </xsl:if>
            <xsl:if test="not(./b:mutation)">
                <xsl:element name="{'mutation'}" namespace="">
                    <xsl:attribute name="datatype">Number</xsl:attribute>
                </xsl:element>
            </xsl:if>
            <xsl:apply-templates select="b:mutation" />
            <xsl:apply-templates select="b:field[@name = 'TYPE']" />
            <xsl:if test="./@type = 'mbedCommunication_sendBlock' and not(b:field/@name = 'POWER')">
                <xsl:element name="{'field'}" namespace="">
                    <xsl:attribute name="name">POWER</xsl:attribute>
                    <xsl:text>0</xsl:text>
                </xsl:element>
            </xsl:if>
            <xsl:apply-templates select="*[not(self::b:mutation) and not(self::b:field/@name = 'TYPE')]" />
        </xsl:copy>
    </xsl:template>
    <!-- mbedCommunication blocks may need additional fields -->
    <xsl:template match="b:block[./@type = 'robCommunication_receiveBlock' or ./@type = 'robCommunication_sendBlock']">
        <xsl:copy>
            <xsl:apply-templates select="@*" />
            <!-- intask attribute may be missing -->
            <xsl:if test="not(./@intask)">
                <xsl:attribute name="intask">true</xsl:attribute>
            </xsl:if>
            <xsl:if test="not(./b:mutation)">
                <xsl:element name="{'mutation'}" namespace="">
                    <xsl:attribute name="datatype">String</xsl:attribute>
                </xsl:element>
            </xsl:if>
            <xsl:if test="not(b:field/@name = 'TYPE')">
                <xsl:element name="{'field'}" namespace="">
                    <xsl:attribute name="name">TYPE</xsl:attribute>
                    <xsl:text>String</xsl:text>
                </xsl:element>
            </xsl:if>
            <xsl:if test="not(b:field/@name = 'PROTOCOL')">
                <xsl:element name="{'field'}" namespace="">
                    <xsl:attribute name="name">PROTOCOL</xsl:attribute>
                    <xsl:text>BLUETOOTH</xsl:text>
                </xsl:element>
            </xsl:if>
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <!-- return_type should be removed if the mutation has a value -->
    <xsl:template match="b:mutation[./@value]">
        <xsl:copy>
            <xsl:apply-templates select="@value" />
        </xsl:copy>
    </xsl:template>
    <!-- elseif should be lowercase -->
    <xsl:template match="b:mutation[./@elseIf]">
        <xsl:copy>
            <xsl:attribute name="elseif">
                <xsl:value-of select="./@elseIf" />
            </xsl:attribute>
        </xsl:copy>
    </xsl:template>
    <!-- mutation operator_range does not exist anymore (?) -->
    <xsl:template match="b:mutation[./@operator_range and (ancestor::b:block_set/@robottype = 'ev3' or not(ancestor::b:block_set/@robottype))]" />
    <!-- mutation datatype is always required for statements -->
    <xsl:template match="b:mutation[./@statement]">
        <xsl:copy>
            <xsl:apply-templates select="@*" />
            <xsl:if test="not(./@datatype)">
                <xsl:choose>
                    <xsl:when test="../b:value[./@name = 'VALUE']/descendant::b:mutation/@datatype = 'Array_Number'">
                        <xsl:attribute name="datatype">Number</xsl:attribute>
                    </xsl:when>
                    <xsl:when test="../b:value[./@name = 'VALUE']/descendant::b:mutation/@datatype = 'Array_String'">
                        <xsl:attribute name="datatype">String</xsl:attribute>
                    </xsl:when>
                    <xsl:when test="../b:value[./@name = 'VALUE']/descendant::b:mutation/@datatype = 'Array_Boolean'">
                        <xsl:attribute name="datatype">Boolean</xsl:attribute>
                    </xsl:when>
                    <xsl:when test="../b:value[./@name = 'VALUE']/descendant::b:mutation/@datatype = 'Array_Colour'">
                        <xsl:attribute name="datatype">Colour</xsl:attribute>
                    </xsl:when>
                    <xsl:when test="../b:value[./@name = 'VALUE']/descendant::b:mutation/@datatype = 'Array_Image'">
                        <xsl:attribute name="datatype">Image</xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:attribute name="datatype" />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <!-- these blocks do not require the inline attribute -->
    <xsl:template match="b:block/@inline[(. = 'true') and (
    ancestor::b:block/@type = 'robLists_getIndex'
    or ancestor::b:block/@type = 'logic_compare'
    or ancestor::b:block/@type = 'logic_operation'
    or ancestor::b:block/@type = 'math_arithmetic'
    or ancestor::b:block/@type = 'math_random_int'
    or ancestor::b:block/@type = 'math_round'
    or ancestor::b:block/@type = 'math_number_property'
    or ancestor::b:block/@type = 'robMath_change'
    or ancestor::b:block/@type = 'robText_append'
    or ancestor::b:block/@type = 'robCommunication_sendBlock'
    or ancestor::b:block/@type = 'robCommunication_receiveBlock'
    or ancestor::b:block/@type = 'robCommunication_startConnection'
    or ancestor::b:block/@type = 'robControls_for')]" />
</xsl:stylesheet>