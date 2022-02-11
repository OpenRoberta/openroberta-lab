package de.poulter.roberta.syntax.action.mbed;

public enum Direction {

    Forward,
    Backward,
    Stop,
    Numeric;

    public static Direction fromString(String direction) {
        if (direction == null) return Stop;
        return Direction.valueOf(direction);
    }
        
}
