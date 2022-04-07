package com.cristiandrami.football365.model.registration;

public class ValidationUser {
    private boolean firstName;
    private boolean lastName;
    private boolean password;
    private boolean repeatedPassword;
    private boolean email;

    public boolean isValidUser() {
        return validUser;
    }

    public void setValidUser(boolean validUser) {
        this.validUser = validUser;
    }

    private boolean validUser=true;

    public boolean isValidFirstName() {
        return firstName;
    }

    public void setFirstName(boolean firstName) {
        this.firstName = firstName;
    }

    public boolean isValidLastName() {
        return lastName;
    }

    public void setLastName(boolean lastName) {
        this.lastName = lastName;
    }

    public boolean isValidPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }

    public boolean isValidRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(boolean repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

    public boolean isValidEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }
}
