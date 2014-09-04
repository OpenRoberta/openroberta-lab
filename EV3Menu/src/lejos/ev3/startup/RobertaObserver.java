package lejos.ev3.startup;

public class RobertaObserver {

    private static boolean isDownloaded = false;
    private static boolean isExecuted = true;
    private static boolean autorun = true;

    private static String userFileName = "";

    private static int menuIndex;

    public static boolean isDownloaded() {
        return isDownloaded;
    }

    public static void setDownloaded(boolean isDownloaded) {
        RobertaObserver.isDownloaded = isDownloaded;
    }

    public static boolean isExecuted() {
        return isExecuted;
    }

    public static void setExecuted(boolean isExecuted) {
        RobertaObserver.isExecuted = isExecuted;
    }

    public static boolean isAutorun() {
        return autorun;
    }

    public static void setAutorun(boolean autorun) {
        RobertaObserver.autorun = autorun;
    }

    public static String getUserFileName() {
        return userFileName;
    }

    public static void setUserFileName(String userFileName) {
        RobertaObserver.userFileName = userFileName;
    }

    public static int getMenuIndex() {
        return menuIndex;
    }

    public static void setMenuIndex(int menuIndex) {
        RobertaObserver.menuIndex = menuIndex;
    }

}
