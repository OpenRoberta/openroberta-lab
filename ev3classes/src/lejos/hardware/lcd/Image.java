package lejos.hardware.lcd;

import java.io.InputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.Serializable;

import lejos.internal.ev3.EV3GraphicsLCD;


/**
 * Provides support for in memory images.
 * The format of the bitmap is in standard leJOS format (so aligned for use on
 * EV3 LCD display). There is one bit per pixel. The pixels are packed into bytes
 * with each byte spanning 8 scan lines. The least significant bit of each byte
 * is the pixel for the top most scan line, the most significant bit is the
 * 8th scan line. Values of 1 represent black. 0 white. This class implements a
 * sub set of the standard lcdui Image class. Only mutable images are supported
 * and the ARGB methods are not available.
 * 
 * TODO: This file needs to be updated to match the EV3 image format.
 * @author Andre Nijholt & Andy Shaw
 */
public class Image implements Serializable
{
	private static final long serialVersionUID = -3934835136206856658L;

	// header is LNI0 => 0x4c4e4930 (big endian)
	private static final int LNI0_HEADER = 0x4c4e4930;
	
    private final int width;
    private final int height;
    private final byte[] data;

    /**
     * Create an image using an already existing byte array. The byte array is
     * used to store the image data. The array may already be initialized with
     * image data.
     * <br>Note: This is a non standard constructor.
     * @param width width of the image
     * @param height height of the image
     * @param data The byte array to be used for image store/
     */
    public Image(int width, int height, byte[] data)
    {
        this.width = width;
        this.height = height;
        this.data = data;
    }

    /**
     * Create ablank image of the requested size.
     * @param width
     * @param height
     * @return Returns the new image.
     */
    public static Image createImage(int width, int height)
    {
        byte[] imageData = new byte[width * (height + 7) / 8];
        return new Image(width, height, imageData);
    }

    /**
     * Read image from file. An image file has the following format:
     * <table border="1">
     * <tr>
     * <th>1st byte - 4th byte</th>
     * <th>5th byte - 8th byte</th>
     * <th>9th byte</th>
     * <th>10th byte ....</th>
     * </tr>
     * <tr>
     * <td><i>image-width (int)</i></td>
     * <td><i>image-height (int)</i></td>
     * <td><code>0x00</code>(<i>image data delimit</i>)</td>
     * <td><i>byte image data</i>....</td>
     * </table>
     * <p>
     * For example:
     * </p>
     * After a file with content
     * <table border="1">
     * <tr>
     * <th>width (int)</th>
     * <th>height (int)</th>
     * <th>delimit</th>
     * <th colspan="3">byte data</th>
     * </tr>
     * <tr>
     * <td>00 00 00 03</td>
     * <td>00 00 00 05</td>
     * <td>00</td>
     * <td>00</td>
     * <td>02</td>
     * <td>1f</td>
     * </tr>
     * </table>
     * was read, this method will return an object which is equivalent to
     * <div style="margin-left:4em;"><code>new Image(3, 5, new byte[] {(byte)0x00, (byte)0x02, (byte)0x1f})</code></div>
     * @param s The input stream for the image file.
     * @return an ev3 image object.
     * @throws IOException if an input or output error occurs or file format is not correct.
     * @see Image
     * @see Image#Image(int, int, byte[])
     */
    public static Image createImage(InputStream s) throws IOException
    {
        DataInputStream in = new DataInputStream(s);
        int p, w, h;
        p = in.readInt();
        w = in.readUnsignedShort();
        h = in.readUnsignedShort();
        if (p != LNI0_HEADER)
            throw new IOException("File format error!");
        
        byte[] imageData = new byte[w * ((h + 7) / 8)];
        in.readFully(imageData);
        
        return new Image(w, h, imageData);
    }

    /**
     * Creates a new image based upon the transformed region of another image
     * @param image Source image
     * @param x x co-ordinate of the source region
     * @param y y co-ordinate of the source region
     * @param w width of the source region
     * @param h height of the source region
     * @param transform Transform to be applied
     * @return New image
     * @see EV3GraphicsLCD#drawRegion(javax.microedition.lcdui.Image, int, int, int, int, int, int, int, int)
     * #see game.Sprite
     */
    public static Image createImage(Image image, int x, int y, int w, int h, int transform)
    {
        int ow = w;
        int oh = h;
        // Work out what shape the new image will be...
        switch (transform)
        {
            case GraphicsLCD.TRANS_MIRROR:
            case EV3GraphicsLCD.TRANS_MIRROR_ROT180:
            case EV3GraphicsLCD.TRANS_ROT180:
            case EV3GraphicsLCD.TRANS_NONE:
                break;
            case EV3GraphicsLCD.TRANS_MIRROR_ROT270:
            case EV3GraphicsLCD.TRANS_MIRROR_ROT90:
            case EV3GraphicsLCD.TRANS_ROT270:
            case EV3GraphicsLCD.TRANS_ROT90:
                ow = h;
                oh = w;
                break;
        }
        // Create empty new image
        Image newImage = createImage(ow, oh);
        GraphicsLCD g = newImage.getGraphics();
        g.drawRegion(image, x, y, w, h, transform, 0, 0, 0);
        return newImage;
    }

    /**
     * Return the width of the image.
     * @return Image width
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * return the height of the image.
     * @return image height
     */
    public int getHeight()
    {
        return height;


    }

    /**
     * Return the byte array used to hold the image data.
     * <br>Note: This is a non standard method.
     * @return The image byte array.
     */
    public byte[] getData()
    {
        return data;
    }

    /**
     * Returns a graphics object that can be used to draw to the image.
     * @return graphics object.
     * @see EV3GraphicsLCD
     */
    public EV3GraphicsLCD getGraphics()
    {
        return new EV3GraphicsLCD(data, width, height);
    }
}
