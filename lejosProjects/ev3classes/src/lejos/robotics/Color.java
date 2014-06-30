package lejos.robotics;

/**
 * Representation of a color, used by color sensors and color detectors.
 */
public class Color {	
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;
	public static final int YELLOW = 3;
	public static final int MAGENTA = 4;
	public static final int ORANGE = 5;
	public static final int WHITE = 6;
    public static final int BLACK = 7;
    public static final int PINK = 8;
    public static final int GRAY = 9;
    public static final int LIGHT_GRAY = 10;
    public static final int DARK_GRAY = 11;
    public static final int CYAN = 12;
    public static final int BROWN = 13;
    public static final int NONE = -1;
	
	private int red;
	private int blue;
	private int green;
	
	private int color;
	
	public Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public Color(int red, int green, int blue, int color) {
		this(red, green, blue);
		this.color = color;
	}
	
	// TODO: Perhaps getRGBComponent(int color) method? Store three colors in int[3] in that case.
	
	// TODO: Could also include JSE API getRGB() which returns the RGB value representing the color in the 
	// default sRGB ColorModel. (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blue). 
	
	/**
	 * Returns the red component in the range 0-255 in the default sRGB space.
	 * @return the red component.
	 */
	public int getRed() {
		return this.red;
	}

	/**
	 * Returns the green component in the range 0-255 in the default sRGB space.
	 * @return the green component.
	 */
	public int getGreen() {
		return this.green;
	}

	/**
	 * Returns the blue component in the range 0-255 in the default sRGB space.
	 * @return the blue component.
	 */
	public int getBlue() {
		return this.blue;
	}
	
	public int getColor() {
		return this.color;
	}
	
	// TODO: Perhaps set methods?
}
