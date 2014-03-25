package lejos.robotics.geometry;

import java.awt.geom.*;
/**
 * Represents a line and supports calculating the point of intersection of two
 * line segments.
 * 
 * @author Lawrie Griffiths
 * 
 * <br/><br/>WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 * 
 */
public class Line extends Line2D.Float {

  public Line(float x1, float y1, float x2, float y2) {
    super(x1,y1,x2,y2);
  }

  /**
   * Calculate the point of intersection of two lines. 
   * 
   * @param l the second line
   * 
   * @return the point of intersection or null if the lines do not intercept or are coincident
   */
  public Point intersectsAt(Line l) {
    float x, y, a1, a2, b1, b2;
    
    if (y2 == y1 && l.y2 == l.y1) return null; // horizontal parallel
    if (x2 == x1 && l.x2 == l.x1) return null; // vertical parallel

    // Find the point of intersection of the lines extended to infinity
    if (x1 == x2 && l.y1 == l.y2) { // perpendicular
      x = x1;
      y = l.y1;
    } else if (y1 == y2 && l.x1 == l.x2) { // perpendicular
      x = l.x1;
      y = y1;
    } else if (y2 == y1 || l.y2 == l.y1) { // one line is horizontal
      a1 = (y2 - y1) / (x2 - x1);
      b1 = y1 - a1 * x1;
      a2 = (l.y2 - l.y1) / (l.x2 - l.x1);
      b2 = l.y1 - a2 * l.x1;

      if (a1 == a2) return null; // parallel
      x = (b2 - b1) / (a1 - a2);
      y = a1 * x + b1;
    } else {
      a1 = (x2 - x1) / (y2 - y1);
      b1 = x1 - a1 * y1;
      a2 = (l.x2 - l.x1) / (l.y2 - l.y1);
      b2 = l.x1 - a2 * l.y1;

      if (a1 == a2) return null; // parallel
      y = (b2 - b1) / (a1 - a2);
      x = a1 * y + b1;
    }
    
    // Check that the point of intersection is within both line segments
    if (!between(x,x1,x2)) return null;
    if (!between(y,y1,y2)) return null;
    if (!between(x,l.x1,l.x2)) return null;
    if (!between(y,l.y1,l.y2)) return null;

    return new Point(x, y);
  }
  
  /**
   * Returns the minimum distance between two line segments--this line segment and another. If they intersect 
   * the distance is 0. Lines can be parallel or skewed (non-parallel).
   * @param seg The other line segment.
   * @return The distance between the two line segments.
   */
  public double segDist(Line seg) {
	  if(this.intersectsLine(seg))
		  return 0;
	  double a = Line2D.ptSegDist(this.getX1(), this.getY1(), this.getX2(), this.getY2(), seg.getX1(), seg.getY1());
	  double b = Line2D.ptSegDist(this.getX1(), this.getY1(), this.getX2(), this.getY2(), seg.getX2(), seg.getY2());
	  double c = Line2D.ptSegDist(seg.getX1(), seg.getY1(), seg.getX2(), seg.getY2(), this.getX1(), this.getY1());
	  double d = Line2D.ptSegDist(seg.getX1(), seg.getY1(), seg.getX2(), seg.getY2(), this.getX2(), this.getY2());
	  
	  double minDist = a;
	  minDist = (b<minDist?b:minDist);
	  minDist = (c<minDist?c:minDist);
	  minDist = (d<minDist?d:minDist);
	  
	  return minDist;
  }
  
  /**
   * Return true iff x is between x1 and x2
   */
  private boolean between(float x, float x1, float x2) {
    if (x1 <= x2 && x >= x1 && x <= x2) return true;
    if (x2 < x1 && x >= x2 && x <= x1) return true;
    return false;
  }
/**
 *  Make this line longer by an amount delta at each end.
 * Used by DijkstraPathFinder to use the same LineMap as the a RangeScanner
 * in MCL navigation.
 * @param delta  the amount added to each end
 */
  public void lengthen( float delta)
  {
    double  angle = Math.atan2(y2 - y1,x2- x1);
    x1 = x1 - delta * (float)Math.cos(angle);
    y1 = y1 - delta * (float)Math.sin(angle);
    x2 = x2 + delta * (float)Math.cos(angle);
    y2 = y2 + delta * (float)Math.sin(angle);


  }
  /**
   * Return the length of the line
   * 
   * @return the length of the line
   */
  public float length() {
    return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }
  
  @Override
  public Point getP1() {
	  return new Point(x1,y1);
  }
  
  @Override
  public Point getP2() {
	  return new Point(x2,y2);
  }
}

