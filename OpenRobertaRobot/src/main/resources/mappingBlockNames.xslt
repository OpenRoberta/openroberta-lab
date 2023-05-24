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
    <!-- simple remappings of the block type -->
    <xsl:template match="b:block/@type">
        <xsl:variable name="newType">
            <xsl:choose>
                <xsl:when test=". = 'mbedSensors_timer_getSample'">robSensors_timer_getSample</xsl:when>
                <xsl:when test=". = 'robSensors_battery_voltage'">robSensors_battery_getSample</xsl:when>
                <xsl:when test=". = 'robSensors_key_isPressed'">robSensors_key_getSample</xsl:when>
                <xsl:when test=". = 'robSensors_touch_isPressed'">robSensors_touch_getSample</xsl:when>
                <xsl:when test=". = 'robSensors_getSample_ardu'">robSensors_getSample</xsl:when>
                <xsl:when test=". = 'bob3Sensors_ambientlight'">robSensors_infrared_getSample</xsl:when>
                <xsl:when test=". = 'bob3Sensors_temperature_getSample'">robSensors_temperature_getSample</xsl:when>
                <xsl:when test=". = 'bob3Sensors_getCode'">robSensors_code_getSample</xsl:when>
                <xsl:when test=". = 'bob3Sensors_touch_getSample'">robSensors_pintouch_getSample</xsl:when>
                <xsl:when test=". = 'bob3Sensors_getSample_bob3'">robSensors_getSample</xsl:when>
                <xsl:when test=". = 'mbedSensors_getSample'">robSensors_getSample</xsl:when>
                <xsl:when test=". = 'mbedSensors_key_isPressed'">robSensors_key_getSample</xsl:when>
                <xsl:when test=". = 'mbedSensors_pin_isTouched'">robSensors_pintouch_getSample</xsl:when>
                <xsl:when test=". = 'mbedSensors_gesture_isActive'">robSensors_gesture_getSample</xsl:when>
                <xsl:when test=". = 'mbedSensors_compass_getSample'">robSensors_compass_getSample</xsl:when>
                <xsl:when test=". = 'mbedSensors_microphone_getSample'">robSensors_sound_getSample</xsl:when>
                <xsl:when test=". = 'mbedSensors_temperature_getSample'">robSensors_temperature_getSample</xsl:when>
                <xsl:when test=". = 'mbedSensors_getRssi'">robSensors_rssi_getSample</xsl:when>
                <xsl:when test=". = 'mbedSensors_ambientLight_getSample'">robSensors_light_getSample</xsl:when>
                <xsl:when test=". = 'mbedSensors_pin_getSample'">robSensors_pin_getSample</xsl:when>
                <xsl:when test=". = 'mbedSensors_rotation_getSample'">robSensors_gyro_getSample</xsl:when>
                <xsl:when test=". = 'mbedSensors_acceleration_getSample'">robSensors_accelerometer_getSample</xsl:when>
                <xsl:when test=". = 'naoSensors_accelerometer'">robSensors_accelerometer_getSample</xsl:when>
                <xsl:when test=". = 'naoActions_setLanguage'">robActions_setLanguage</xsl:when>
                <xsl:when test=". = 'naoActions_sayText'">robActions_sayText</xsl:when>
                <xsl:when test=". = 'naoControls_wait_for'">robControls_wait_for</xsl:when>
                <xsl:when test=". = 'naoSensors_getSample'">robSensors_getSample</xsl:when>
                <xsl:when test=". = 'sim_getSample'">robSensors_getSample</xsl:when>
                <xsl:when test=". = 'sim_LED_on'">robActions_brickLight_on</xsl:when>
                <xsl:when test=". = 'sim_LED_off'">robActions_brickLight_off</xsl:when>
                <xsl:when test=". = 'sim_ultrasonic_getSample'">robSensors_ultrasonic_getSample</xsl:when>
                <xsl:when test=". = 'sim_touch_isPressed'">robSensors_touch_getSample</xsl:when>
                <xsl:when test=". = 'sim_motor_on'">robActions_motor_on</xsl:when>
                <xsl:when test=". = 'sim_motor_stop'">robActions_motor_stop</xsl:when>
                <xsl:when test=". = 'robActions_write_to_pin'">robActions_write_pin</xsl:when>
                <xsl:when test=". = 'robSensors_pin_getSample' and ancestor::b:block_set/@robottype = 'arduino'">robSensors_out_getSample</xsl:when>
                <xsl:when test=". = 'robActions_brickLight_on' and
                    (ancestor::b:block_set/@robottype = 'arduino' or
                     ancestor::b:block_set/@robottype = 'botnroll' or
                     ancestor::b:block_set/@robottype = 'calliope' or
                     ancestor::b:block_set/@robottype = 'festobionic' or
                     ancestor::b:block_set/@robottype = 'nano33ble' or
                     ancestor::b:block_set/@robottype = 'sensebox')">
                    <xsl:text>robActions_inbuilt_led</xsl:text>
                </xsl:when>
                <xsl:when test=". = 'robActions_led_on' and
                    (ancestor::b:block_set/@robottype = 'arduino' or
                     ancestor::b:block_set/@robottype = 'mbot' or
                     ancestor::b:block_set/@robottype = 'nano33ble' or
                     ancestor::b:block_set/@robottype = 'sensebox' or
                     ancestor::b:block_set/@robottype = 'thymio' or
                     ancestor::b:block_set/@robottype = 'wedo')">
                    <xsl:text>robActions_rgbled_on</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:attribute name="type">
            <xsl:value-of select="$newType"/>
        </xsl:attribute>
    </xsl:template>
</xsl:stylesheet>