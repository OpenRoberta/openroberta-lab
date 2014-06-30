package lejos.robotics;

public interface ColorIdentifier
{
    /**
     * Return an enumerated constant that indicates the color detected. e.g. Color.BLUE
     * @return An integer from the Color constants, such as Color.BLUE
     */
    public int getColorID();
}
