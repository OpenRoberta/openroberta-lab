package de.fhg.iais.roberta.factory.generic;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.factory.EV3Factory;
import de.fhg.iais.roberta.mode.action.BrickLedColor;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.LightMode;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.action.ev3.ShowPicture;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class RobotModeFactoryTest {
    EV3Factory factory = new EV3Factory(new PluginProperties("ev3lejosv0", "", "", Util.loadProperties("classpath:/ev3lejosv0.properties")));
    BlocklyDropdownFactory dropdownFactory = this.factory.getBlocklyDropdownFactory();

    @Test
    public void getIndexLocationFromString() {
        Assert.assertEquals(this.dropdownFactory.getIndexLocation("FIRST"), IndexLocation.FIRST);
    }

    @Test
    public void getIndexLocationByAlternativeName() {
        Assert.assertEquals(this.dropdownFactory.getIndexLocation("FROMSTART"), IndexLocation.FROM_START);
    }

    @Test(expected = DbcException.class)
    public void invalidIndexLocation() {
        this.dropdownFactory.getIndexLocation("FROMSTART1");
    }

    @Test
    public void getListElementOperationFromString() {
        Assert.assertEquals(this.dropdownFactory.getListElementOpertaion("GET"), ListElementOperations.GET);
    }

    @Test(expected = DbcException.class)
    public void invalidListElementOperation() {
        this.dropdownFactory.getListElementOpertaion("FROMSTART1");
    }

    @Test
    public void getActorPortFromString() {
        Assert.assertEquals(this.dropdownFactory.sanitizePort("A"), "A");
    }

    @Test
    public void getBlinkModeFromString() {
        Assert.assertEquals(this.dropdownFactory.getBlinkMode("FLASH"), LightMode.FLASH);
    }

    @Test(expected = DbcException.class)
    public void invalidBlinkMode() {
        this.dropdownFactory.getBlinkMode("18");
    }

    @Test
    public void getBrickLedColorFromString() {
        Assert.assertEquals(this.dropdownFactory.getBrickLedColor("ORANGE"), BrickLedColor.ORANGE);
    }

    @Test(expected = DbcException.class)
    public void invalidBrickLedColor() {
        this.dropdownFactory.getBrickLedColor("Q");
    }

    @Test
    public void getShowPictureFromString() {
        Assert.assertEquals(this.factory.getShowPicture("EYESCLOSED"), ShowPicture.EYESCLOSED);
    }

    @Test
    public void getShowPictureByAlternativeName() {
        Assert.assertEquals(this.factory.getShowPicture("BRILLE"), ShowPicture.OLDGLASSES);
    }

    @Test(expected = DbcException.class)
    public void invalidShowPicture() {
        this.factory.getShowPicture("Q");
    }

    @Test
    public void getTurnDirectionFromString() {
        Assert.assertEquals(this.dropdownFactory.getTurnDirection("LEFT"), TurnDirection.LEFT);
    }

    @Test(expected = DbcException.class)
    public void invalidTurnDirection() {
        this.dropdownFactory.getTurnDirection("Q");
    }

    @Test
    public void getMotorMoveModeFromString() {
        Assert.assertEquals(this.dropdownFactory.getMotorMoveMode("ROTATIONS"), MotorMoveMode.ROTATIONS);
    }

    @Test(expected = DbcException.class)
    public void invalidMotorMoveMode() {
        this.dropdownFactory.getMotorMoveMode("Q");
    }

    @Test
    public void getMotorStopModeFromString() {
        Assert.assertEquals(this.dropdownFactory.getMotorStopMode("FLOAT"), MotorStopMode.FLOAT);
    }

    @Test(expected = DbcException.class)
    public void invalidMotorStopMode() {
        this.dropdownFactory.getMotorStopMode("Q");
    }

    @Test
    public void getMotorSideFromString() {
        Assert.assertEquals(this.dropdownFactory.getMotorSide("LEFT"), MotorSide.LEFT);
    }

    @Test
    public void getMotorSideByAlternativeName() {
        Assert.assertEquals(this.dropdownFactory.getMotorSide("right"), MotorSide.RIGHT);
    }

    @Test(expected = DbcException.class)
    public void invalidMotorSide() {
        this.dropdownFactory.getMotorSide("Q");
    }

    @Test
    public void getDriveDirectionFromString() {
        Assert.assertEquals(this.dropdownFactory.getDriveDirection("BACKWARD"), DriveDirection.BACKWARD);
    }

    @Test
    public void getDriveDirectionByAlternativeName() {
        Assert.assertEquals(this.dropdownFactory.getDriveDirection("OFF"), DriveDirection.FOREWARD);
    }

    @Test(expected = DbcException.class)
    public void invalidDriveDirection() {
        this.dropdownFactory.getDriveDirection("Q");
    }

    @Test
    public void getColorSensorModeFromString() {
        Assert.assertEquals(this.dropdownFactory.getMode("AMBIENTLIGHT"), SC.AMBIENTLIGHT);
    }

    @Test
    public void getColorSensorModeByAlternativeName() {
        Assert.assertEquals(this.dropdownFactory.getMode("Colour"), SC.COLOUR);
    }

    @Test(expected = DbcException.class)
    public void invalidColorSensorMode() {
        this.dropdownFactory.getMode("Q");
    }

    @Test
    public void getInfraredSensorModeFromString() {
        Assert.assertEquals(this.dropdownFactory.getMode("DISTANCE"), SC.DISTANCE);
    }

    @Test
    public void getInfraredSensorModeByAlternativeName() {
        Assert.assertEquals(this.dropdownFactory.getMode("PRESENCE"), SC.PRESENCE);
    }

    @Test(expected = DbcException.class)
    public void invalidInfraredSensorMode() {
        this.dropdownFactory.getMode("Q");
    }

    @Test
    public void getTimerSensorModeFromString() {
        Assert.assertEquals(this.dropdownFactory.getMode("VALUE"), SC.VALUE);
    }

    @Test
    public void getMotorTachoModeFromString() {
        Assert.assertEquals(this.dropdownFactory.getMode("DISTANCE"), SC.DISTANCE);
    }

    @Test(expected = DbcException.class)
    public void invalidMotorTachoMode() {
        this.dropdownFactory.getMode("Q");
    }

    @Test
    public void getUltrasonicSensorModeFromString() {
        Assert.assertEquals(this.dropdownFactory.getMode("DISTANCE"), SC.DISTANCE);
    }
}
