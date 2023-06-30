<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:b="http://de.fhg.iais.roberta.blockly" version="1.0">
    <xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>
    <xsl:strip-space elements="*"/>
    <!-- identity -->
    <xsl:template match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>
    <!-- description should not be escaped -->
    <xsl:template match="b:block_set/@description">
        <xsl:copy>
            <xsl:value-of select="." disable-output-escaping="yes"/>
        </xsl:copy>
    </xsl:template>
    <!-- simple remappings of the mutation input -->
    <xsl:template match="b:mutation/@input">
        <xsl:variable name="newInput">
            <xsl:choose>
                <xsl:when test=". = 'TIME'">TIMER_VALUE</xsl:when>
                <xsl:when test=". = 'KEYS_PRESSED'">KEY_PRESSED</xsl:when>
                <xsl:when test=". = 'TEMPERATURE'">TEMPERATURE_VALUE</xsl:when>
                <xsl:when test=". = 'MICROPHONE'">SOUND_SOUND</xsl:when>
                <xsl:when test=". = 'LIGHT_LEVEL'">
                    <xsl:choose>
                        <xsl:when test="ancestor::b:block_set/@robottype = 'calliope'">LIGHT_VALUE</xsl:when>
                        <xsl:otherwise>INFRARED_AMBIENTLIGHT</xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test=". = 'ORIENTATION'">GYRO_ANGLE</xsl:when>
                <xsl:when test=". = 'ACCELERATION'">ACCELEROMETER_VALUE</xsl:when>
                <xsl:when test=". = 'GESTURE_ACTIVE'">
                    <xsl:text>GESTURE_</xsl:text><xsl:value-of select="ancestor::b:block[contains(@type, '_getSample')]/b:field[@name = 'GESTURE']"/>
                </xsl:when>
                <xsl:when test=". = 'PIN_PULSE_HIGH'">PIN_PULSEHIGH</xsl:when>
                <xsl:when test=". = 'PIN_PULSE_LOW'">PIN_PULSELOW</xsl:when>
                <xsl:when test=". = 'PIN_TOUCHED'">PINTOUCH_PRESSED</xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:attribute name="input">
            <xsl:value-of select="$newInput"/>
        </xsl:attribute>
    </xsl:template>
    <!-- simple remappings of the mutation mode -->
    <xsl:template match="b:mutation/@mode">
        <xsl:variable name="newMode">
            <xsl:choose>
                <xsl:when test=". = 'SEEK'">PRESENCE</xsl:when>
                <xsl:when test=". = 'RED'">LIGHT</xsl:when>
                <xsl:when test=". = 'PIN_PULSE_HIGH'">PIN_PULSEHIGH</xsl:when>
                <xsl:when test=". = 'PIN_PULSE_LOW'">PIN_PULSELOW</xsl:when>
                <xsl:when test=". = 'PULSE_HIGH'">PULSEHIGH</xsl:when>
                <xsl:when test=". = 'PULSE_LOW'">PULSELOW</xsl:when>
                <xsl:when test=". = 'PIN_TOUCHED'">PINTOUCH_PRESSED</xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:attribute name="mode">
            <xsl:value-of select="$newMode"/>
        </xsl:attribute>
    </xsl:template>
    <!-- some field require a different content based on the type and previous content -->
    <xsl:template match="b:field/text()">
        <xsl:choose>
            <xsl:when
                    test=". = 'RED' and (ancestor::b:block[1]/@type = 'robSensors_colour_getSample' or ancestor::b:block[1]/@type = 'robSensors_light_getSample')">
                <xsl:text>LIGHT</xsl:text>
            </xsl:when>
            <xsl:when test=". = 'SEEK' and ancestor::b:block[1]/@type = 'robSensors_infrared_getSample'">PRESENCE</xsl:when>
            <xsl:when
                    test="ancestor::b:block[1]/@type = 'robSensors_getSample'">
                <xsl:choose>
                    <xsl:when test=". = 'TOUCH'">TOUCH_PRESSED</xsl:when>
                    <xsl:when test=". = 'TIME'">TIMER_VALUE</xsl:when>
                    <xsl:when test=". = 'KEYS_PRESSED'">KEY_PRESSED</xsl:when>
                    <xsl:when test=". = 'TEMPERATURE'">TEMPERATURE_VALUE</xsl:when>
                    <xsl:when test=". = 'MICROPHONE'">SOUND_SOUND</xsl:when>
                    <xsl:when test=". = 'LIGHT_LEVEL'">
                        <xsl:choose>
                            <xsl:when test="ancestor::b:block_set/@robottype = 'calliope'">LIGHT_VALUE</xsl:when>
                            <xsl:otherwise>INFRARED_AMBIENTLIGHT</xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:when test=". = 'ORIENTATION'">GYRO_ANGLE</xsl:when>
                    <xsl:when test=". = 'ACCELERATION'">ACCELEROMETER_VALUE</xsl:when>
                    <xsl:when test=". = 'PIN_PULSE_HIGH'">PIN_PULSEHIGH</xsl:when>
                    <xsl:when test=". = 'PIN_PULSE_LOW'">PIN_PULSELOW</xsl:when>
                    <xsl:when test=". = 'PIN_TOUCHED'">PINTOUCH_PRESSED</xsl:when>
                    <xsl:when test=". = 'button_a'">A</xsl:when>
                    <xsl:when test=". = 'button_b'">B</xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="."/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:when test=". = 'PULSE_HIGH'">PULSEHIGH</xsl:when>
            <xsl:when test=". = 'PULSE_LOW'">PULSELOW</xsl:when>
            <xsl:when test=". = 'TRUE' and ancestor::b:block[1]/@type = 'robControls_start' and (ancestor::b:block_set/@robottype = 'calliope' or ancestor::b:block_set/@robottype = 'microbit')"/>
            <xsl:when test=". = 'button_a' and (ancestor::b:field/@name = 'SENSORPORT' or ancestor::b:field/@name = 'KEY')">A</xsl:when>
            <xsl:when test=". = 'button_b' and (ancestor::b:field/@name = 'SENSORPORT' or ancestor::b:field/@name = 'KEY')">B</xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- some field blocks may need adapted attributes and contents -->
    <xsl:template match="b:field">
        <xsl:choose>
            <xsl:when test="./@name = 'VALUETYPE' and ../@type = 'robActions_write_pin'">
                <xsl:copy>
                    <xsl:attribute name="name">MODE</xsl:attribute>
                    <xsl:value-of select="."/>
                </xsl:copy>
            </xsl:when>
            <xsl:when test="./@name = 'PIN' and ../@type = 'robActions_write_pin'">
                <xsl:copy>
                    <xsl:attribute name="name">ACTORPORT</xsl:attribute>
                    <xsl:value-of select="."/>
                </xsl:copy>
            </xsl:when>
            <xsl:when test="./@name = 'GESTURE' and ../@type = 'robSensors_getSample'">
                <xsl:copy>
                    <xsl:attribute name="name">GESTURE</xsl:attribute>GESTURE_<xsl:value-of select="."/>
                </xsl:copy>
            </xsl:when>
            <xsl:when test="./@name = 'SENSORTYPE' and contains(../@type, '_getSample') and ../b:field[@name = 'GESTURE']">
                <xsl:copy>
                    <xsl:attribute name="name">SENSORTYPE</xsl:attribute>GESTURE_<xsl:value-of select="../b:field[@name = 'GESTURE']"/>
                </xsl:copy>
            </xsl:when>
            <xsl:when test="./@name = 'MODE' and ../b:field[@name = 'GESTURE']">
                <xsl:copy>
                    <xsl:attribute name="name">MODE</xsl:attribute>
                    <xsl:value-of select="../b:field[@name = 'GESTURE']"/>
                </xsl:copy>
            </xsl:when>
            <xsl:when test="./@name = 'MODE' and ./text() = 'PITCH'">ANGLE</xsl:when>
            <xsl:when test="./@name = 'SENSORNUM' or (./@name = 'MOTORPORT' and ancestor::b:block/@type = 'robSensors_encoder_reset')">
                <xsl:copy>
                    <xsl:attribute name="name">SENSORPORT</xsl:attribute>
                    <xsl:value-of select="."/>
                </xsl:copy>
            </xsl:when>
            <xsl:when test="./@name = 'LEDSIDE'">
                <xsl:copy>
                    <xsl:attribute name="name">ACTORPORT</xsl:attribute>
                    <xsl:choose>
                        <xsl:when test="(../@type = 'actions_rgbLed_on_nibo') or (../@type = 'actions_rgbLed_off_nibo')">
                            <xsl:choose>
                                <xsl:when test="./text() = 'Left'">
                                    <xsl:value-of select="'EYE_2'"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="'EYE_1'"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:when test="(ancestor::b:block_set/@robottype = 'festobionicflower')">
                            <xsl:value-of select="'R'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="."/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:copy>
            </xsl:when>
            <xsl:when test="./@name = 'SWITCH_BLINK'">
                <xsl:copy>
                    <xsl:attribute name="name">MODE</xsl:attribute>
                    <xsl:value-of select="."/>
                </xsl:copy>
            </xsl:when>
            <xsl:when test="./@name = 'SWITCH_COLOR'">
                <xsl:copy>
                    <xsl:attribute name="name">COLOUR</xsl:attribute>
                    <xsl:value-of select="."/>
                </xsl:copy>
            </xsl:when>
            <xsl:when test="./@name = 'LEDSTATE'">
                <xsl:copy>
                    <xsl:attribute name="name">MODE</xsl:attribute>
                    <xsl:value-of select="."/>
                </xsl:copy>
            </xsl:when>
            <xsl:when test="./@name = 'SENSORPORT' and ../@type = 'robActions_sensorLight_on'">
                <xsl:copy>
                    <xsl:attribute name="name">ACTORPORT</xsl:attribute>
                    <xsl:value-of select="."/>
                </xsl:copy>
            </xsl:when>
            <xsl:when test="./@name = 'LED' and ancestor::b:block_set/@robottype = 'nao'">
                <xsl:copy>
                    <xsl:attribute name="name">ACTORPORT</xsl:attribute>
                    <xsl:value-of select="."/>
                </xsl:copy>
            </xsl:when>
            <xsl:when test="./@name = 'LED' and ancestor::b:block_set/@robottype = 'nao'">
                <xsl:copy>
                    <xsl:attribute name="name">ACTORPORT</xsl:attribute>
                    <xsl:value-of select="."/>
                </xsl:copy>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="@* | node()"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="b:value">
        <xsl:choose>
            <xsl:when test="./@name = 'COLOR'">
                <xsl:copy>
                    <xsl:attribute name="name">COLOUR</xsl:attribute>
                    <xsl:copy-of select="./b:block"/>
                </xsl:copy>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="@* | node()"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="b:mutation/@datatype">
        <xsl:choose>
            <xsl:when test=". = 'Array_number'">
                <xsl:attribute name="datatype">
                    <xsl:text>Array_Number</xsl:text>
                </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>