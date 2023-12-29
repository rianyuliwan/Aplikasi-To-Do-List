package Entity;


import java.util.UUID;

public class LoginRegister {
    private String username;
    private String password;

    public LoginRegister() {
    }

    public LoginRegister(String username, String password) {

        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}