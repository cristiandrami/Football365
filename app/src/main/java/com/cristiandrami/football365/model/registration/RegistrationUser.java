package com.cristiandrami.football365.model.registration;
/**
 * This class is used to store temporarily the information about a new registration
 * It contains all information about new users.
 * It is used to find if the information are valid or not
 * @see SignUpValidator
 * @author Cristian D. Dramisino
 *
 */
public class RegistrationUser {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String repeatedPassword;

    public RegistrationUser(){
        /**This is an empty constructor*/
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

    @Override
    public String toString() {
        return "RegistrationUser{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", repeatedPassword='" + repeatedPassword + '\'' +
                '}';
    }
}
