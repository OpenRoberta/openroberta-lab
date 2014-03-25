package lejos.robotics;

import java.io.IOException;

/**
 * Interface for real time clock devices
 * 
 * @author Lawrie Griffiths
 *
 */
public interface Clock {
	public int getYear() throws IOException;
	
	public int getMonth() throws IOException;
	
	public int getDay()throws IOException;
	
	public int getHour() throws IOException;
	
	public int getMinute() throws IOException;
	
	public int getSecond() throws IOException;
	
	public int getDayOfWeek() throws IOException;
	
	public void setHourMode(boolean mode) throws IOException;
	
	public String getDateString() throws IOException;
	
	public String getTimeString() throws IOException;
	
	public String getAMPM() throws IOException;
	
	public byte getByte(int loc) throws IndexOutOfBoundsException,IOException;
	
	public void setByte(int loc,byte b) throws IndexOutOfBoundsException,IOException;
	
	public void setDate(int m,int d,int y) throws IllegalArgumentException,IOException ;
	
	public void setTime(int h,int m,int s) throws IllegalArgumentException,IOException;
}
