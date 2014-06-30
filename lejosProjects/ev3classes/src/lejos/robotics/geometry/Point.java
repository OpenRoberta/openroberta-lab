package lejos.robotics.geometry;

import java.awt.geom.*;

/**
 * Point with float co-ordinates for use in navigation.
 * This class includes methods allow it to behave as a vector in 2 dimensional
 * vector space. It includes the standard  vector  arithmetic operatins of addition,  * subtraction, scalar multiplication, and inner  product.
 * 
 * @author Lawrie Griffiths ,  Roger Glassey
 *
 */
public class Point extends Point2D.Float {
   /**
    * returns a Point at location x,y
    * @param x coordinate
    * @param y coordinate
    */
	public Point(float x, float y) {
		super(x,y);
	}
/**
      * Returns a point ad distance 1 from the origin and an angle <code>radans</code> to the x-axis
      * @param radians 
      */
    public Point(float radians)
    {
        this.x = (float)Math.cos(radians);
        this.y = -(float)Math.sin(radians);
    }

/**
 * Returns the direction angle from this point to the Point p
 * @param p the Point to determine the angle to
 * @return the angle in degrees
 */
    public float angleTo(Point p)
    {
      return (float)Math.toDegrees(Math.atan2(p.getY()-y,  p.getX()-x));
    }

     /**
   *Translates this point, at location (x, y), by dx along the x axis and
   * dy along the y axis so that it now represents the point (x + dx, y + dy).
   * @param dx
   * @param dy
   */
    public void translate(float dx, float dy)
    {
      x += dx;
      y += dy;
    }

    /*
     * Copy this vector to another vector
     */
    public Point copyTo(Point p)
    {
        p.x = x;
        p.y = y;
        return p;
    }
    
    /**
     * returns a clone of itself
     * @return  clone of this point
     */

    @Override
    public Point clone()
    {
        return new Point(x, y);
    }
/**
     * Returns the vector sum of <code>this</code> and other
     * @param other the point added to <code>this</code>
     * @return vector sum
     */
    public Point add(Point other)
    {
        return new Point(this.x + other.x, this.y + other.y);
    }
    
     /**
     *  Vector addition; add other to <code>this</code>
     * @param other is added to <code>this</code>
     * @return  <code>this</code> after the addition
     */
    public Point addWith(Point other)
    {
        x += other.x;
        y += other.y;
        return this;
    }
/**
     * Makes <code>this</code> a copy of the other point
     * @param other 
     */
    public void moveTo(Point other)
    {
        x = other.x;
        y = other.y;
    }
/**
     * Vector subtraction
     * @param other is subtracted from <code>this</code>
     * @return a new point; this point  is unchanged
     */
    public Point subtract(Point other)
    {
        return new Point(this.x - other.x, this.y - other.y);
    }
/**
     * 
     * Vector subtraction
     * @param length of a copy of <code>this</code>
     * @return a new vector, obtained b subtracting a scaled version of this point
     */
    public Point subtract(float length)
    {
        return this.subtract(this.getNormalized().multiply(length));
    }
/**
     * Scalar multiplication
     * @param scale multilies the length of this to give a new length
     * @return a new copy of this, with length scaled
     */
    public Point multiply(float scale)
    {
        return new Point(this.x * scale , this.y * scale);
    }
/**
     * get a copy of <code>this</code> with length 1
     * @return a new vector of unit length
     */
    public Point getNormalized()
    {
        return new Point(this.x / length(), this.y / length());
    }
 /**
     * same as multiply(-1);
     * @return  this pointing in the opposite direction
     */  
    public Point reverse()
    {
        return this.multiply(-1.0F);
    }

 /**
     * Finds the orthogonal projection of this point onto the line.
     * The projection may lie on an extension of the line 
     * @param line onto which the projection is made
     * @return  the projection
     */

    public Point projectOn(Line line)
    {
       Point origin = line.getP1();
       Point basis = line.getP2().subtract(origin);
       Point xx = this.subtract(origin);
       float lamda = xx.dotProduct(basis)/basis.dotProduct(basis);
       Point projection = basis.multiply(lamda);
       projection = projection.add(origin);
       return projection;
    }
    
    /**
     * returns the angle in radians of this point from the origin.
     * The X- axis ie at angle 0.
     * @return  the angle in radians
     */

    public float angle()
    {
        return (float)Math.atan2(this.y, this.x);
    }
/**
     * calculate left orthogonal vector of <code>this</code>
     * @return orthogonal vector
     */
    public Point leftOrth()
    {
        return new Point(-y, x);
    }

    /**
     * calculate the right handed cartesian  orthogonal of this poiont
     * @return orthogonal vector
     */
    public Point rightOrth()
    {
        return new Point(y, -x);
    }

   
/**
     * vector subtraction
     * @param other is subtracted from <code>this</code>
     * @return this point after subtraction
     */
    public Point subtractWith(Point other)
    {
        x -= other.x;
        y -= other.y;
        return this;
    }
/**
     * scalar multiplication
     * @param scale
     * @return scaled this point after multiplication
     */
    public Point multiplyBy(float scale)
    {
        x *= scale;
        y *= scale;
        return this;
    }

    /**
     * Returns the length of this  vector
     * @return the length
     */
    public float length()
    {
        return (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    /**
     * Sets this vector's length to 1 unit while retaining direction
     * @return  this vector normalized
     */
    public Point normalize()
    {
        float length = length();
        x /= length;
        y /= length;
        return this;
    }

    /**
     * Turns this vector into its left-handed cartesian orthagonal
     */
    public Point makeLeftOrth()
    {
        float temp = x;
        x = -y;
        y = temp;
        return this;
    }

    /**
     * Turns this vector into its right-handed cartesian orthagonal
     */
    public Point makeRightOrth()
    {
        float temp = x;
        x = y;
        y = -temp;
        return this;
    }
    /**
     * Returns the inner dot product.
     * @return dot product of this with other
     */
 
    public float dotProduct(Point other)
    {
        return this.x * other.x + this.y * other.y;
    }


     /**
     * Returns a new point at the specified distance in the direction angle  from
     * this point.
     * @param distance the distance to the new point
     * @param angle the angle to the new point
     * @return the new point
     */
    public Point pointAt(float distance, float angle)
    {
      float xx = distance*(float)Math.cos(Math.toRadians(angle)) + (float)getX();
      float yy = distance*(float)Math.sin(Math.toRadians(angle)) + (float)getY();
      return new Point(xx,yy);
    }
}
