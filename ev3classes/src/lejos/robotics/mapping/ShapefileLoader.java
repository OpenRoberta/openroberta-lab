package lejos.robotics.mapping;

import java.io.*;
import java.util.ArrayList;

import lejos.robotics.geometry.Line;
import lejos.robotics.geometry.Rectangle;


/**
 * <p>This class loads map data from a Shapefile and produces a LineMap object, which can
 * be used by the leJOS navigation package.</p>
 * 
 * <p>There are many map editors which can use the Shapefile format (OpenEV, Global Mapper). Once you
 * have created a map, export it as Shapefile. This will produce three files ending in .shp .shx and 
 * .dbf. The only file used by this class is .shp.</p>  
 * 
 * <p>NOTE: Shapefiles can only contain one type of shape data (polygon or polyline, not both). A single file can't
 * mix polylines with polygons.</p>
 * 
 * <p>This class' code can parse points and multipoints. However, a LineMap object can't deal with 
 * points (only lines) so points and multipoints are discarded.</p>
 *  
 * @author BB
 *
 */
public class ShapefileLoader {

	/* OTHER POTENTIAL MAP FILE FORMATS TO ADD:
	 * (none have really been researched yet for viability)
	 * KML
	 * GML
	 * WMS? (more of a service than a file)
	 * MIF/MID (MapInfo)
	 * SVG (Scalable Vector Graphics)
	 * EPS (Encapsulated Post Script)
	 * DXF (Autodesk)
	 * AI (Adobe Illustrator)
	 * 
	 */
	
	// 2D shape types types:
	private static final byte NULL_SHAPE = 0;
	private static final byte POINT = 1;
	private static final byte POLYLINE = 3;
	private static final byte POLYGON = 5;
	private static final byte MULTIPOINT = 8;
	
	private final int SHAPEFILE_ID = 0x0000270a;
	DataInputStream data_is = null;
	
	/**
	 * Creates a ShapefileLoader object using an input stream. Likely you will use a FileInputStream
	 * which points to the *.shp file containing the map data.
	 * @param in
	 */
	public ShapefileLoader(InputStream in) {
		this.data_is = new DataInputStream(in);
	}
	
	/**
	 * Retrieves a LineMap object from the Shapefile input stream.
	 * @return the line map
	 * @throws IOException
	 */
	public LineMap readLineMap() throws IOException {
		ArrayList <Line> lines = new ArrayList <Line> ();
		
		int fileCode = data_is.readInt(); // Big Endian
		if(fileCode != SHAPEFILE_ID) throw new IOException("File is not a Shapefile");
		data_is.skipBytes(20); // Five int32 unused by Shapefile
		/*int fileLength =*/ data_is.readInt();
		//System.out.println("Length: " + fileLength); // TODO: Docs say length is in 16-bit words. Unsure if this is strictly correct. Seems higher than what hex editor shows.
        /*int version =*/ readLEInt();
        //System.out.println("Version: " + version); 
        /*int shapeType =*/ readLEInt();
        //System.out.println("Shape type: " + shapeType);
        // These x and y min/max values define bounding rectangle:        
        double xMin = readLEDouble();
        double yMin = readLEDouble();
        double xMax = readLEDouble();
        double yMax = readLEDouble();
        // Create bounding rectangle:
        Rectangle rect = new Rectangle((float)xMin, (float)yMin, (float)(xMax - xMin), (float)(yMax - yMin));
        /*double zMin =*/ readLEDouble();
        /*double zMax =*/ readLEDouble();
        /*double mMin =*/ readLEDouble();
        /*double mMax =*/ readLEDouble();
        // TODO These values seem to be rounded down to nearest 0.5. Must round them up?
        //System.out.println("Xmin " + xMin + "  Ymin " + yMin);
        //System.out.println("Xmax " + xMax + "  Ymax " + yMax);
                
        try { // TODO: This is a cheesy way to detect EOF condition. Not very good coding.
        while(2 > 1) { // TODO: Temp code to keep it looping. Should really detect EOF condition.
        
        // NOW ONTO READING INDIVIDUAL SHAPES:
        // Record Header (2 values):
        /*int recordNum =*/ data_is.readInt();
        int recordLen = data_is.readInt(); // TODO: in 16-bit words. Might cause bug if number of shapes gets bigger than 16-bit short?
        
        // Record (variable length depending on shape type):
        int recShapeType = readLEInt();
        
        // Now to read the actual shape data
        switch (recShapeType) {
        	case NULL_SHAPE:
        		break;
        	case POINT:
        		// DO WE REALLY NEED TO DEAL WITH POINT? Feature might use them possibly.
        		/*double pointX =*/ readLEDouble(); // TODO: skip bytes instead
                /*double pointY =*/ readLEDouble();
                break;
        	case POLYLINE:
        		// NOTE: Data structure for polygon/polyline is identical. Code should work for both.
        	case POLYGON:
        		// Polygons can contain multiple polygons, such as a donut with outer ring and inner ring for hole.
        		// Max bounding rect: 4 doubles in a row. TODO: Discard bounding rect. values and skip instead.
        		/*double polyxMin =*/ readLEDouble();
                /*double polyyMin =*/ readLEDouble();
                /*double polyxMax =*/ readLEDouble();
                /*double polyyMax =*/ readLEDouble();
                int numParts = readLEInt();
                int numPoints = readLEInt();
                
                // Retrieve array of indexes for each part in the polygon
                int [] partIndex = new int[numParts];
                for(int i=0;i<numParts;i++) {
                	partIndex[i] = readLEInt();
                }
                
                // Now go through numParts times pulling out points
                double firstX=0;
            	double firstY=0;
            	for(int i=0;i<numPoints-1;i++) {
                	// Could check here if onto new polygon (i = next index). If so, do something with line formation.
                	for(int j=0;j<numParts;j++) {
                		if(i == partIndex[j]) {
                			firstX = readLEDouble();
                        	firstY = readLEDouble();
                			continue;
                		}
                	}
                	                	
                	double secondX = readLEDouble();
                	double secondY = readLEDouble();
                	Line myLine = new Line((float)firstX, (float)firstY, (float)secondX, (float)secondY);
                	lines.add(myLine);
                	firstX = secondX;
                	firstY = secondY;
                }
                
        		break;
        	case MULTIPOINT:
        		// TODO: DO WE REALLY NEED TO DEAL WITH MULTIPOINT? Comment out and skip bytes?
        		/*double multixMin = */readLEDouble();
                /*double multiyMin = */readLEDouble();
                /*double multixMax = */readLEDouble();
                /*double multiyMax = */readLEDouble();
                int multiPoints = readLEInt();
                double [] xVals = new double[multiPoints];
                double [] yVals = new double[multiPoints];
                for(int i=0;i<multiPoints;i++) {
                	xVals[i] = readLEDouble();
                	yVals[i] = readLEDouble();
                }
        		break;
        	default:
        		// IGNORE REST OF SHAPE TYPES and skip over data using recordLen value
        		//System.out.println("Some other unknown shape");
        		data_is.skipBytes(recordLen); // TODO: Check if this works on polyline or point
        }
	    
        } // END OF WHILE
        } catch(EOFException e) {
        	// End of File, just needs to continue
        }
        Line [] arrList = new Line [lines.size()];
        return new LineMap(lines.toArray(arrList), rect);
	}
	
	/**
     * Translates a little endian int into a big endian int
     * 
     * @return int A big endian int
     */
    private int readLEInt() throws IOException {
        int byte1, byte2, byte3, byte4;
        synchronized (this) {
            byte1 = data_is.read();
            byte2 = data_is.read();
            byte3 = data_is.read();
            byte4 = data_is.read();
        }
        if (byte4 == -1) {
            throw new EOFException();
        }
        return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }
	
    /**
     * Reads a little endian double into a big endian double
     * 
     * @return double A big endian double
     */
    private final double readLEDouble() throws IOException {
        return Double.longBitsToDouble(this.readLELong());
    }
    
    /**
     * Translates a little endian long into a big endian long
     * 
     * @return long A big endian long
     */
    private long readLELong() throws IOException {
        long byte1 = data_is.read();
        long byte2 = data_is.read();
        long byte3 = data_is.read();
        long byte4 = data_is.read();
        long byte5 = data_is.read();
        long byte6 = data_is.read();
        long byte7 = data_is.read();
        long byte8 = data_is.read();
        if (byte8 == -1) {
            throw new EOFException();
        }
        return (byte8 << 56) + (byte7 << 48) + (byte6 << 40) + (byte5 << 32)
                + (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }
}
