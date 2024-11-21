package chessgame.ui;

import chessgame.network.ClientNetwork;
import chessgame.network.User;

public class LoginInfo {
    private User user;
    private static ClientNetwork client;

    private String usernameLogin = "";
    private String passwordLogin = "";

    public static ClientNetwork getClient() {
        return client;
    }

    public String getUsernameLogin() {
        return usernameLogin;
    }

    public static void setClient(ClientNetwork client) {
        LoginInfo.client = client;
    }

    public void setUsernameLogin(String usernameLogin) {
        this.usernameLogin = usernameLogin;
    }

    public void setPasswordLogin(String passwordLogin) {
        this.passwordLogin = passwordLogin;
    }

    public String getPasswordLogin() {
        return passwordLogin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
