package lejos.robotics.mapping;

import java.io.*;

import lejos.robotics.Transmittable;
import lejos.robotics.geometry.*;
import lejos.robotics.mapping.RangeMap;
import lejos.robotics.navigation.Pose;


/**
 * A map of a room or other closed environment, represented by line segments
 * 
 * @author Lawrie Griffiths
 * 
 */
public class LineMap implements RangeMap, Transmittable {
  private Line[] lines;
  private Rectangle boundingRect;

  /**
   * Calculate the range of a robot to the nearest wall
   * 
   * @param pose the pose of the robot
   * @return the range or -1 if not in range
   */
  public float range(Pose pose) {
    Line l = new  Line(pose.getX(), pose.getY(), pose.getX() + 254f
    	        * (float) Math.cos(Math.toRadians(pose.getHeading())), pose.getY() + 254f
    	        * (float) Math.sin(Math.toRadians(pose.getHeading())));
    Line rl = null;

    for (int i = 0; i < lines.length; i++) {
      Point p = lines[i].intersectsAt(l);
      if (p == null) continue; // Does not intersect
      Line tl = new Line(pose.getX(), pose.getY(), p.x, p.y);

      // If the range line intersects more than one map line
      // then take the shortest distance.
      if (rl == null || tl.length() < rl.length()) rl = tl;
    }
    return (rl == null ? -1 : rl.length());
  }

  /**
   * Create a map from an array of line segments and a bounding rectangle
   * 
   * @param lines the line segments
   * @param boundingRect the bounding rectangle
   */
  public LineMap(Line[] lines, Rectangle boundingRect) {
    this.lines = lines;
    this.boundingRect = boundingRect;
  }
  
  /**
   * Constructor to use when map will be loaded from a data stream
   */
  public LineMap() {
  }

  /**
   * Check if a point is within the mapped area
   * 
   * @param p the Point
   * @return true iff the point is with the mapped area
   */
  public boolean inside(Point p) {
    if (p.x < boundingRect.x || p.y < boundingRect.y) return false;
    if (p.x > boundingRect.x + boundingRect.width
        || p.y > boundingRect.y + boundingRect.height) return false;

    // Create a line from the point to the left
    Line l = new Line(p.x, p.y, p.x - boundingRect.width, p.y);

    // Count intersections
    int count = 0;
    for (int i = 0; i < lines.length; i++) {
      if (lines[i].intersectsAt(l) != null) count++;
    }
    // We are inside if the number of intersections is odd
    return count % 2 == 1;
  }

  /**
   * Return the bounding rectangle of the mapped area
   * 
   * @return the bounding rectangle
   */
  public Rectangle getBoundingRect() {
    return boundingRect;
  }
  
  /**
   * Dump the map to a DataOutputStream
   * @param dos the stream
   * @throws IOException
   */
  public void dumpObject(DataOutputStream dos) throws IOException {
      dos.writeInt(lines.length);
      for(int i=0;i<lines.length;i++) {
        dos.writeFloat(lines[i].x1);
        dos.writeFloat(lines[i].y1);
        dos.writeFloat(lines[i].x2);
        dos.writeFloat(lines[i].y2);
        dos.flush();
      }  
      dos.writeFloat(boundingRect.x);
      dos.writeFloat(boundingRect.y);
      dos.writeFloat(boundingRect.width);
      dos.writeFloat(boundingRect.height);
      dos.flush();
  }
  /**
   * Load a map from a DataInputStream
   * 
   * @param dis the stream
   * @throws IOException
   */
  public void loadObject(DataInputStream dis) throws IOException {
      lines = new Line[dis.readInt()];
      for(int i=0;i<lines.length;i++) {
        float x1 = dis.readFloat();
        float y1 = dis.readFloat(); 
        float x2 = dis.readFloat();
        float y2 = dis.readFloat();
        lines[i] = new Line(x1,y1,x2,y2);
      }     
      boundingRect = new Rectangle(dis.readFloat(),dis.readFloat(),dis.readFloat(),dis.readFloat());
  }
  
  /**
   * Get the lines as an array
   * 
   * @return the lines as an array
   */
  public Line[] getLines() {
	  return lines;
  }
  
  /**
   * Create an SVG map file
   * 
   * @param fileName the name of the file to create or overwrite
   * @throws IOException
   */
  public void createSVGFile(String fileName) throws IOException {
    File mapFile = new File(fileName);
    FileOutputStream fos = new FileOutputStream(mapFile);
    PrintStream ps = new PrintStream(fos);
    ps.println("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + boundingRect.width + "px\" height=\""
            + boundingRect.height + "px\" viewBox=\""+boundingRect.x+" "+boundingRect.y+" "+boundingRect.width+" "+boundingRect.height+"\">");
    ps.println("<g>");
    for(int i=0;i<lines.length;i++) {
      ps.println("<line stroke=\"#000000\" x1=\"" + lines[i].x1 + "\" y1=\"" + lines[i].y1 + "\" x2=\"" + lines[i].x2 + "\" y2=\"" + lines[i].y2 + "\"/>");
    }
    ps.println("</g>");
    ps.println("</svg>");
    ps.close();
    fos.close();
  }
  
  /**
   * Create a line map with the y axis flipped
   *  
   * @return the new LineMap
   */
  public LineMap flip() {
	  float maxY = boundingRect.y + boundingRect.height;
	  Line[] ll = new Line[lines.length];
	  
	  for(int i=0;i<lines.length;i++) {
		  ll[i] = new Line(lines[i].x1, maxY - lines[i].y1, lines[i].x2, maxY - lines[i].y2);
	  }
	  
	  return new LineMap(ll, boundingRect);
  }
}

