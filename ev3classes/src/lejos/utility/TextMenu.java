package lejos.utility;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.TextLCD;

/**
 *Displays a list of items.  The select() method allows the user to scroll the list using the right and left keys to scroll forward and backward 
 * through the list. The location of the list , and an optional title can be specified.
 * @author Roger Glassey   Feb 20, 2007
 */

public class TextMenu  
{
	/**
	 *location of the top row of the list; set by constructor, used by display()
	 */
	protected int _topRow = 0;
	
	/** 
	 * number of rows displayed; set by constructor, used by display()
	 */
	protected int _height = 8;
	
	/**
	 *optional menu title displayed immediately above the list of items
	 */
	protected String _title;
	
	/**
	 *array of items to be displayed ;set by constructor, used by select();
	 */
	protected String[] _items;
	
	/**
	 * effective length of items array  - number of items before null 
	 */
	protected int _length;
	
	/**
	 * index of the list item at the top of the list; set by constructor, used by select()
 	 **/
	protected int _topIndex = 0;  
	
	/**
 	 *identifies the currently selected item
 	 */
	protected static final char SEL_CHAR = '>';
	
	/**
	 * boolean to cause select to quit 
	 */
	protected boolean _quit = false;
	
	/**
	 * start time for select()
	 */
	protected int _startTime;
	
	/**
	 * Timout used for {@link Button#waitForAnyPress(int)} in {@link #select(int, int)}.
	 */
	protected static final int BUTTON_POLL_INTERVAL = 10; // Time to wait for button press
	
	protected TextLCD lcd =  BrickFinder.getDefault().getTextLCD();

	/**
	 * This constructor sets location of the top row of the item list to row 0 of the display.
	 */
	public TextMenu( String[] items)
	{
		this(items, 0, null);
	}
	
	/**
	 * This constructor allows specification location of the item list .
	 */
	public TextMenu( String[] items, int topRow)
	{
		this(items, topRow, null);
	}
	
	/**
	 * This constuctor allows the specfication of a title (of up to 16 characters) and the location of the item list <br>
	 * The title is displayed in the row above the item list.
	 * @param items  -  string array containing the menu items. No items beyond the first null will be displayed.
	 */	
	public TextMenu(String[] items, int topRow, String title)
	{
		if (topRow < 0 || (topRow == 0 && title != null))
			throw new IllegalArgumentException("illegal topRow argument");
		
		_topRow = topRow;
		setTitle(title);
		this.setItems(items);
	}
	
	/**
	 * set menu title. 
	 * @param title  the new title
	 */
	public void setTitle(String title) 
	{
		_title = title;
		if(_topRow <= 0)
			_topRow = 1;		
		_height = 8 - _topRow;
		if(_height > _length)
			_height = _length;
	}
	
	/**
	 * set the array of items to be displayed
	 * @param items
	 */
	public void setItems(String[] items)
	{
		_items = items;
		
		if (items == null)
			_length = 0;
		else
		{
			int i = 0;
			while(i < items.length && items[i] != null)
				i++;
			_length = i;
		}
		_height = 8 - _topRow;
		if(_height > _length)
			_height = _length;		
	}
	
	/**
	 * Allows the user to scroll through the items, using the right and left buttons (forward and back)  The Enter key closes the menu <br>
	 * and returns the index of the selected item. <br>
	 * The menu display wraps items that scroll off the top will reappear on the bottom and vice versa.
	 * 
	 * The selectedIndex is set to the first menu item.
	 * 
	 * @return the index of the selected item
	 **/
	public int select() 
	{ 
	   return select(0,0); 
	} 
	
	/**
	 * Version of select without timeout
	 */
	public int select(int selectedIndex) {
		return select(selectedIndex, 0);
	}

	/**
	 * Allows the user to scroll through the items, using the right and left buttons (forward and back)  The Enter key closes the menu <br>
	 * and returns the index of the selected item. <br>
	 * The menu display wraps items that scroll off the top will reappear on the bottom and vice versa.
	 * 
	 * This version of select allows the selected index to be set when the menu is first displayed.
	 * 
	 * @param selectedIndex the index to start the menu on
	 * @return the index of the selected item
	 **/
	public int select(int selectedIndex, int timeout) 
	{ 
		if (selectedIndex >= _length)
			//might result in -1
			selectedIndex = _length -1;
		if (selectedIndex < 0)
			selectedIndex = 0;
		
//		if (_length<_size) _size = _length;
		_quit = false;
		resetTimeout();
//		LCD.clear();
		if (_topIndex > selectedIndex)
			_topIndex = selectedIndex;
		if (_topIndex > _length - _height)
			_topIndex = _length - _height;			
		display(selectedIndex, _topIndex);
		while(true)
		{
			int button;
			do
			{				
				if (_quit)
					return -2; // quit by another thread
				
				if (timeout > 0 && System.currentTimeMillis() - _startTime >= timeout) 
					return -3; // timeout
				
				button = Button.waitForAnyPress(BUTTON_POLL_INTERVAL);
			} while (button == 0);
			
			if(button == Button.ID_ENTER && selectedIndex >= 0 && selectedIndex < _length)
				return selectedIndex;
			if(button == Button.ID_ESCAPE)
				return -1; //Escape
			if(button == Button.ID_DOWN)//scroll forward
			{
				selectedIndex++;
				// check for index out of bounds
				if(selectedIndex >= _length)
				{
					selectedIndex = 0;
					_topIndex = 0;
				}
				else if(selectedIndex >= _topIndex + _height)
				{
					_topIndex = selectedIndex - _height + 1;
				}
			}
			if(button == Button.ID_UP)//scroll backward
			{
				selectedIndex --;
				// check for index out of bounds
				if(selectedIndex < 0)
				{
					selectedIndex  = _length - 1;
					_topIndex = _length - _height;
				}
				else if(selectedIndex < _topIndex)
				{
					_topIndex = selectedIndex;
				}
			}
			display(selectedIndex, _topIndex);
		}
	}
	
	/**
	 * method to call from another thread to quit the menu
	 */
    public void quit()
    {
    	_quit = true;
    }
	
	/**
	 * helper method used by select()
	 */
	protected void display(int selectedIndex, int topIndex)
	{
		//LCD.asyncRefreshWait();
		if(_title != null)
			lcd.drawString(_title, 0, _topRow - 1);
		int max = _topRow + _height;
		for (int i = _topRow; i < max; i++)
		{
			lcd.clear(i);
			int idx = i - _topRow + topIndex;
			if (idx >= 0 && idx < _length)
			{
				lcd.drawChar(idx == selectedIndex ? SEL_CHAR : ' ', 0, i);
				lcd.drawString(_items[idx], 1, i);
			}
		}
		lcd.refresh();
	}
	
	/**
	 * Returns list of items in this menu; 
	 * @return the array of item names
	 */
	public String[] getItems()
	{
	   return _items;
	}
	
	/**
	 * Reset the timeout period.
	 */
	public void resetTimeout() {
		_startTime = (int) System.currentTimeMillis();
	}	
}

