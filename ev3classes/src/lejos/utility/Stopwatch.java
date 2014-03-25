package lejos.utility; 

/**
* Elapsed time watch (in milliseconds) <br>
* To use - construct a new instance.  
*  @author Roger Glassey 
*  version 2     
*/
public class Stopwatch
{
/**
records system clock time (in milliseconds) when reset() was executed
*/
	private	int t0 = (int)System.currentTimeMillis(); 
	
/**
Reset watch to zero
*/
	public void reset()
	{
		t0 = (int)System.currentTimeMillis();
	}
/**
Return elapsed time in milliseconds 
*/
	public int elapsed( )
	{
		return (int)System.currentTimeMillis() -t0;
	}		
}
