package com.cristiandrami.football365.model.login;

public class UserLogin {

    private String email;
    private String password;

    public UserLogin() {
        /**This is an empty constructor*/
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
