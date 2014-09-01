package lejos.ev3.startup;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.CommonLCD;
import lejos.hardware.lcd.TextLCD;

/**
 * Abrams version of a more detailed GraphicMenu for the file menu.
 * 
 * @author Abram Early
 *
 */
public class GraphicListMenu extends GraphicMenu {
	
	private TextLCD lcd = LocalEV3.get().getTextLCD();
	private int _offset = 0;
	
	public GraphicListMenu(String[] items, String[] icons) {
		super(items, icons, -1);
	}
	
	@Override
	protected void animate(int selectedIndex, int finalIndex, int animateDirection)
	{
		this.display(finalIndex, animateDirection, 0);
	}
	
	@Override
	protected void display(int selectedIndex, int animateDirection, int tick)
	{
		if(_title != null) {
			lcd.drawString(_title, 0, _topRow - 1);
		}
			
		int max = _topRow + _height;
		for (int i = _topRow; i < max; i++){
			lcd.clear(i);
			int idx = i - _topRow + _topIndex;
			if (idx >= 0 && idx < _length){
				lcd.drawChar(idx == selectedIndex ? SEL_CHAR : ' ', 0, i);
				if (_offset < _items[idx].length()) lcd.drawString(_items[idx].substring(_offset), 3, i);
			}
		}
		lcd.refresh();
	}
	
	@Override
	public int select(int selectedIndex, int timeout) 
	{ 
		_offset = 0;
		int maxLen = 0;
		for(String s: _items) {
			if (s.length() > maxLen) maxLen = s.length();
		}
		
		if (selectedIndex >= _length)
			//might result in -1
			selectedIndex = _length -1;
		if (selectedIndex < 0)
			selectedIndex = 0;
		
		_quit = false;
		resetTimeout();

		if (_topIndex > selectedIndex)
			_topIndex = selectedIndex;
		if (_topIndex > _length - _height)
			_topIndex = _length - _height;			
		display(selectedIndex, 0,0);
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
			
			if(button == Button.ID_ENTER && selectedIndex >= 0 && selectedIndex < _length){
				clearArea();
				return selectedIndex;
			}
			if(button == Button.ID_ESCAPE)
				return -1; //Escape
			int temp = selectedIndex;
			int dir = 0;
			if(button == Button.ID_DOWN && (!(_length <= 2 && selectedIndex > 0) || get2IconMode()))//scroll forward
			{
				selectedIndex++;
				// check for index out of bounds
				if(selectedIndex >= _length)
				{
					selectedIndex = 0;
					_topIndex = 0;
				}
				else if(selectedIndex >= _topIndex + _height){
					_topIndex = selectedIndex - _height + 1;
				}
				dir = -1;

			} else if(button == Button.ID_UP && (!(_length <= 2 && selectedIndex < _length-1) || get2IconMode()))//scroll backward
			{
				selectedIndex --;
				// check for index out of bounds
				if(selectedIndex < 0)
				{			
					selectedIndex  = _length - 1;
					_topIndex = _length - _height;
				}
				else if(selectedIndex < _topIndex){
					_topIndex = selectedIndex;
				}
				dir = 1;

			} else if(button == Button.ID_LEFT) {
				if (_offset > 0) _offset--;
			} else if(button == Button.ID_RIGHT) {
				if (_offset < maxLen - 15) _offset++;
			}
			if (_length > 1) animate(temp,selectedIndex,dir);
		}
	}

	@Override
	public void clearArea(){
		// TODO : Check what this is for
		lcd.bitBlt(null, 30, 100, 0, 0, 0, 16, 30, 100, CommonLCD.ROP_CLEAR);
	}
	
	@Override
	protected boolean get2IconMode() {return true;} // Wrap With 2 Icons
}
