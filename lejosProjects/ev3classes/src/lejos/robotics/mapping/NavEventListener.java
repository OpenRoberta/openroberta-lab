package lejos.robotics.mapping;

import lejos.robotics.mapping.NavigationModel.NavEvent;

public interface NavEventListener {
	public void whenConnected();
	
	public void eventReceived(NavEvent navEvent);
}
