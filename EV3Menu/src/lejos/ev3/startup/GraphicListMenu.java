package lejos.ev3.startup;

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
			//System.out.println("Displaying title " + _title + " on line " + (_topRow - 1));
			lcd.drawString(_title, 0, _topRow - 1);
		}
			
		int max = _topRow + _height;
		for (int i = _topRow; i < max; i++){
			lcd.drawString(BLANK, 0, i);
			int idx = i - _topRow + _topIndex;
			if (idx >= 0 && idx < _length){
				lcd.drawChar(idx == selectedIndex ? SEL_CHAR : ' ', 0, i);
				lcd.drawString(_items[idx], 3, i);
			}
		}
		lcd.refresh();
	}
	@Override
	public void clearArea(){
		lcd.bitBlt(null, 30, 100, 0, 0, 0, 16, 30, 100, CommonLCD.ROP_CLEAR);
	}
	
	@Override
	protected boolean get2IconMode(){return true;} // Wrap With 2 Icons
}
