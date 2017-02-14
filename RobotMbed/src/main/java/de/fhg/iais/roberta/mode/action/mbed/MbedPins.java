package de.fhg.iais.roberta.mode.action.mbed;

import de.fhg.iais.roberta.util.dbc.DbcException;

public enum MbedPins {
    P0( "P12", 0 ), P1( "P0", 1 ), P2( "P1", 2 ), P3( "P16", 3 );

    private final String mbedPin;
    private final int pinNumber;

    private MbedPins(String mbedPin, int pinNumber) {
        this.mbedPin = mbedPin;
        this.pinNumber = pinNumber;
    }

    public String getCalliopeName() {
        return this.mbedPin;
    }

    public static MbedPins findPin(String pinNum) {
        int pinNumber = Integer.valueOf(pinNum);
        for ( MbedPins pin : MbedPins.values() ) {
            if ( pinNumber == pin.getPinNumber() ) {
                return pin;
            }
        }
        throw new DbcException("Invalid Pin!");
    }

    public int getPinNumber() {
        return this.pinNumber;
    }
}
