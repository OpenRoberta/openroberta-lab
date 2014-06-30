package lejos.robotics.objectdetection;

import java.util.ArrayList;

/**
 * An adapter to make it easier to implement FeatureDetector classes. The scan() method is the only method 
 * which must be implemented by the actual class.
 *  
 * @author BB
 *
 */
public abstract class FeatureDetectorAdapter implements FeatureDetector {

	private ArrayList<FeatureListener> listeners = null;	
	private boolean enabled = true;
	private int delay = 0; 
	
	public FeatureDetectorAdapter(int delay) {
		this.delay = delay;
		Thread x = new MonitorThread();
		x.setDaemon(true);
		x.start();
	}
	
	public void addListener(FeatureListener l){
		if(listeners == null )listeners = new ArrayList<FeatureListener>();
		listeners.add(l);
	}

	public void enableDetection(boolean enable) {
		// TODO: Optionally do a real disable where it ends thread (true test in thread loop) and 
		// enabling it will start thread (if thread is null/not running).
		this.enabled = enable;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getDelay() {
		return delay;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	/**
	 * Thread to monitor the range finder.
	 *
	 */
	private class MonitorThread extends Thread{

		long prev_time;
		
		@Override
		public void run() {
			while(true) {
				// Only performs scan if detection is enabled.
				Feature f = (enabled?scan():null);
				if(f != null) notifyListeners(f);
				
				try {
					long elapsed_time = System.currentTimeMillis() - prev_time;
					
					
					long actual_delay = delay - elapsed_time;
					if(actual_delay < 0) actual_delay = 0;
					
					Thread.sleep(actual_delay);
					prev_time = System.currentTimeMillis();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void notifyListeners(Feature feature) {
		if(listeners != null) { 
			for(FeatureListener l : listeners) {
				l.featureDetected(feature, this);
			}
		}
	}
	
	public abstract Feature scan();

}
