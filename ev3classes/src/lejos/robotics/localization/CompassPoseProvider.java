package lejos.robotics.localization;

import lejos.robotics.DirectionFinder;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;

/**
 * Pose Provider using a compass (or other direction finder) to provide 
 * location and heading data.
 * 
 * Note: This is a temporary class to allow access compass data until we have
 * a more encompassing solution for data from multiple instrumentation.
 * @author BB
 *
 */
public class CompassPoseProvider extends OdometryPoseProvider {

	private DirectionFinder compass;
	
	public CompassPoseProvider(MoveProvider mp, DirectionFinder compass) {
		super(mp);
		this.compass = compass;
	}

	public Pose getPose() {
		Pose temp = super.getPose();
		temp.setHeading(compass.getDegreesCartesian());
		return temp;
	}
}
