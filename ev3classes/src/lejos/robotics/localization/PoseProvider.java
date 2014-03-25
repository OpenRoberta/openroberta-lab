package lejos.robotics.localization;

import lejos.robotics.navigation.Pose;

/**
 * Provides the coordinate and heading info via a Pose object.
 * @author NXJ Team
 *
 */
public interface PoseProvider {
	
	public Pose getPose();
    
	public void setPose(Pose aPose);		
}
