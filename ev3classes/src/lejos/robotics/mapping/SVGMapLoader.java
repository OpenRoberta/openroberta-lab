package lejos.robotics.mapping;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import lejos.robotics.geometry.Line;
import lejos.robotics.geometry.Rectangle;

/**
 * <p>This class loads map data from an SVG and produces a LineMap object, which can
 * be used by the leJOS navigation package.</p>
 * 
 * @author Lawrie Griffiths/Juan Antonio Brenha Moral
 *
 */
public class SVGMapLoader {
	  private static final float MAX_BOUND = 10000f; // Bounds must be smaller than this
	  
	  private InputStream in = null;
	  
	  public SVGMapLoader(InputStream in) {
		 this.in = in;
	  }

	  public LineMap readLineMap() throws XMLStreamException {
	    XMLInputFactory factory = XMLInputFactory.newInstance();
	    XMLStreamReader parser = factory.createXMLStreamReader(in);
	    ArrayList<Line> lines = new ArrayList<Line>();
	    float minX = MAX_BOUND, minY = MAX_BOUND, maxX = 0f, maxY = 0f;
	    
	    while (true) {
	      int event = parser.next();
	      if (event == XMLStreamConstants.END_DOCUMENT) {
	         parser.close();
	         break;
	      } else if (event == XMLStreamConstants.START_ELEMENT && parser.getLocalName().equals("line")) { 
	        float x1 = Float.parseFloat(parser.getAttributeValue(null,"x1"));
	        float y1 = Float.parseFloat(parser.getAttributeValue(null,"y1"));
	        float x2 = Float.parseFloat(parser.getAttributeValue(null,"x2"));
	        float y2 = Float.parseFloat(parser.getAttributeValue(null,"y2"));
	        if (x1 < minX) minX = x1;
	        if (x2 < minX)minX = x2;
	        if (x1 > maxX) maxX = x1;
	        if (x2 > maxX) maxX = x2;
	        if (y1 < minY) minY = y1;
	        if (y2 < minY) minY = y2;
	        if (y1 > maxY) maxY = y1;
	        if (y2 > maxY) maxY = y2;        
	        
	        lines.add(new Line(x1,y1,x2,y2));      
	      }
	    }
	    
	    return new LineMap(lines.toArray(new Line[lines.size()]), new Rectangle(minX, minY, maxX - minX, maxY - minY )).flip();
	  }
}
