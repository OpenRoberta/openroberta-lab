package lejos.robotics.objectdetection;

import java.util.ArrayList;

import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;

/**
 * <p>If you have a robot with multiple sensors (touch and range) and would like them to report to one
 * listener, or if you want to control them at the same time (such as disabling them all at once) you can
 * use this class.</p>
 * 
 * <p>This class maintains its own thread for checking the FeatureDetectors.</p>
 * 
 * @author BB
 *
 */
public class FusorDetector implements FeatureDetector, FeatureListener {
	// TODO: Make inner FeatureListener
	
	private ArrayList<FeatureDetector> detectors = null;
	
	private ArrayList<RangeReadings> readings = null;
	
	/**
	 * The delay (in ms) between notifying listeners of detected features (if any are detected).
	 */
	private int delay;
	
	private boolean enabled = true;
	
	private ArrayList<FeatureListener> listeners = null;
	
	public FusorDetector() {
		detectors = new ArrayList<FeatureDetector>();
		readings = new ArrayList<RangeReadings>();
		
		Thread x = new NotifyThread();
		x.setDaemon(true);
		x.start();
	}
	
	/**
	 * This method adds another FeatureDetector to the FusorDetector. This method will set the delay
	 * for this class to the largest delay if comes across from all the FeatureDetector objects added 
	 * to it.
	 * @param detector
	 */
	public void addDetector(FeatureDetector detector) {
		// Add this class as a FeatureListener:
		detector.addListener(this);
		
		// TODO: Does ArrayList already check if redundant objects are added?
		if(!detectors.contains(detector)) detectors.add(detector);
		
		// Set delay to the largest delay it comes across:
		if(detector.getDelay() > this.delay)
			this.delay = detector.getDelay();
		readings.add(new RangeReadings(0)); // Add dummy object to expand size of RangeReadings
	}
	/**
	 * This method scans all the sensors added to this object and returns the amalgamated results.
	 * NOTE: This method is not called by the thread code.		
	 */
	public Feature scan() {
		RangeReadings rr = new RangeReadings(0);
		for(FeatureDetector d : detectors) {
			Feature df = d.scan();
			if(df != null) {
				RangeReadings temp = df.getRangeReadings();
				for(int i=0;i<temp.size();i++)
					rr.add(temp.get(i));
			}	
		}
		if(rr.size()<= 0) return null;
			
		return new RangeFeature(rr);
	}

	/**
	 * This method must deal with different delays from different sensors. e.g. Touch is 50 ms,
	 * while ultrasonic is 250 ms. But must amalgamate readings that are captured over same period.
	 * SOLUTION: Use listeners. Get max scan time (e.g. 250 ms) and wait that long for each listener to
	 * report in before reporting all at once.		
	 */
	public void featureDetected(Feature feature, FeatureDetector detector) {
		// 1. Need to know who is reporting this otherwise can accumulate 5 touch sensor readings (50 ms each)
		// while range sensor produces one (250 ms). Need to ID who is reporting via comparison against set of detectors.
		int index = detectors.indexOf(detector);
		
		// 2. Store latest from each object in an array (arraylist size of detectors) corresponding to that particular
		// FeatureDetector. 
		
		if(index != -1) {// -1 means detector not present in detectors 
			RangeReadings rrtemp = feature.getRangeReadings();
			readings.add(index, rrtemp);
		}
						
		// 3. Thread waits "delay" for them to report. Accumulate each one as described in 2. When time is up,
		// make one object accumulated from the array. (see NotifyThread)
		
		// 4. After delay, thread notifies all listeners IF features isn't equal to null. Then makes new readings object.
		// (see NotifyThread)
	}

	/**
	 * Thread to periodically notify listeners.
	 *
	 */
	private class NotifyThread extends Thread{
		
		@Override
		public void run() {
			while(true) {
				
				if(enabled) {
					// 0. Make new RangeReadings object
					RangeReadings rrs = new RangeReadings(0); 
					
					// 1. Check if there are features gathered by listener code
					for(RangeReadings r : readings){
						
						// 2. Amalgamate them into one Feature object
						// Now get them all and add them to rrs
						for(RangeReading rtemp : r) {
							rrs.add(rtemp);
						}
					}
					
					// 3. Check if anything exists. If so, notify all listeners.
					if(rrs.size() > 0)
						notifyListeners(new RangeFeature(rrs));
					
					// 4. Now clear out the detectors so they aren't resent next loop.
					readings.clear();
					
					// 5. And add dummies to it to make it proper size.
					for(int i=0;i<detectors.size();i++) 
						readings.add(new RangeReadings(0));
						
				}
				
				try { // TODO: Technically this should be at start of loop?
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void notifyListeners(Feature feature) {
		if(listeners != null) { 
			for(FeatureListener l : listeners) {
				l.featureDetected(feature, this);
			}
		}
	}
	
	public void addListener(FeatureListener listener) {
		if(this.listeners == null )this.listeners = new ArrayList<FeatureListener>();
		this.listeners.add(listener);
	}

	/**
	 * This method enables/disables automatic scanning and listener reporting for this object and
	 * all FeatureDetectors used in this FusorDetector object. 
	 */
	public void enableDetection(boolean on) {
		enabled = on;
		for(FeatureDetector fd : detectors) {
			fd.enableDetection(on);
		}
	}

	public int getDelay() {
		return delay;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
}
