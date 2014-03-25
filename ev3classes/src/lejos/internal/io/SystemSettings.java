package lejos.internal.io;

public class SystemSettings {

	/**
	 * Get the value for a leJOS persistent setting as a String
	 * 
	 * @param key the name of the setting
	 * @param defaultValue the default value
	 * @return the value
	 */
	public static String getStringSetting(String key, String defaultValue) {
		return Settings.getProperty(key, defaultValue);
	}
	
	/**
	 * Get the value for a leJOS persistent setting as an Integer
	 * 
	 * @param key the name of the setting
	 * @param defaultValue the default value
	 * @return the value
	 */
	public static int getIntSetting(String key, int defaultValue) {
		String s = getStringSetting(key, null);
		if (s == null)
			return defaultValue;
			
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
}
