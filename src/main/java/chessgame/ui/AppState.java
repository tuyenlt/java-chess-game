package chessgame.ui;

public class AppState {
    private static boolean isSecondaryPaneOpened = false;
    private static boolean validNameUser = false;

    public static boolean isSecondaryPaneOpened() {
        return isSecondaryPaneOpened;
    }

    public static void setSecondaryPaneOpened(boolean opened) {
        isSecondaryPaneOpened = opened;
    }

    public static boolean isValidNameUser() {
        return validNameUser;
    }

    public static void setValidNameUser(boolean checkValid) {
        validNameUser = checkValid;
    }

}