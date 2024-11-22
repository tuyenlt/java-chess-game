package chessgame.ui;

public class AppState {
    private static boolean isSecondaryPaneOpened = false;

    private static boolean isSuccessfulRegistered = false;

    public static boolean isSecondaryPaneOpened() {
        return isSecondaryPaneOpened;
    }

    public static void setSecondaryPaneOpened(boolean opened) {
        isSecondaryPaneOpened = opened;
    }

    public static boolean isSuccessfulRegistered() {
        return isSuccessfulRegistered;
    }

    public static void setSuccessfulRegistered(boolean successful) {
        isSuccessfulRegistered = successful;
    }

}