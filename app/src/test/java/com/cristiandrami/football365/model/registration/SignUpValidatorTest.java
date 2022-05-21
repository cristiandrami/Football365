package com.cristiandrami.football365.model.registration;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;


/***
 * Test cases:
 *
 *
 *
 *
 *
 */

class SignUpValidatorTest {

    private static RegistrationUser registrationUser;
    @BeforeAll
    public static void setup(){
        registrationUser= new RegistrationUser();
    }

    @Nested
    class EmailValidationTests{
        @DisplayName("email is not valid if it is empty")
        @Test
         void should_validateRegistration_returns_a_ValidationUser_object_with_false_email_fields_if_email_is_empty(){
            String email="";
            registrationUser.setEmail(email);

            ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

            boolean expectedValue=false;
            boolean obtainedValue=userValidated.isValidEmail();
            assertThat(obtainedValue).isEqualTo(expectedValue);

        }

        @DisplayName("validation is not valid with a mail without @")
        @Test
         void should_validateRegistration_returns_a_ValidationUser_object_with_false_email_fields_if_email_is_without_snail(){
            String email="cristian.com";
            registrationUser.setEmail(email);

            ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

            boolean expectedValue=false;
            boolean obtainedValue=userValidated.isValidEmail();
            assertThat(obtainedValue).isEqualTo(expectedValue);

        }

        @DisplayName("validation is not valid with a mail without a domain")
        @Test
         void should_validateRegistration_returns_a_ValidationUser_object_with_false_email_fields_if_email_is_without_domain(){
            String email="cristian@";
            registrationUser.setEmail(email);

            ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

            boolean expectedValue=false;
            boolean obtainedValue=userValidated.isValidEmail();
            assertThat(obtainedValue).isEqualTo(expectedValue);

        }

        @DisplayName("validation is not valid with a mail without initial part before @")
        @Test
         void should_validateRegistration_returns_a_ValidationUser_object_with_false_email_fields_if_email_is_without_initial_part(){
            String email="@gmail.com";
            registrationUser.setEmail(email);

            ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

            boolean expectedValue=false;
            boolean obtainedValue=userValidated.isValidEmail();
            assertThat(obtainedValue).isEqualTo(expectedValue);

        }

        @ParameterizedTest
        @ValueSource(strings = {"test@gmail.com","test@yahoo.com" ,"test@yourdomain.com" ,"test@domain.org", "test@domain.net", "test123@domain.org",
                "123test@domain.org" , "test??@domain.org"})
         void should_validateRegistration_returns_a_ValidationUser_object_with_true_email_fields_if_email_matches_constraints(String email){

            registrationUser.setEmail(email);

            ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

            boolean expectedValue=true;
            boolean obtainedValue=userValidated.isValidEmail();
            assertThat(obtainedValue).isEqualTo(expectedValue);

        }

    }

     @Nested
     class PasswordValidationTests{
         @DisplayName("password is not valid if it is empty")
         @Test
          void should_validateRegistration_returns_a_ValidationUser_object_with_false_fields_if_password_is_empty(){
             String password="";
             registrationUser.setPassword(password);

             ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

             boolean expectedValue=false;
             boolean obtainedValue=userValidated.isValidPassword();
             assertThat(obtainedValue).isEqualTo(expectedValue);

         }

         @DisplayName("password is not valid if its length is < 8 ")
         @Test
          void should_validateRegistration_returns_a_ValidationUser_object_with_false_fields_if_password_length_is_less_than_8(){
             String password="Ci3!!";
             registrationUser.setPassword(password);

             ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

             boolean expectedValue=false;
             boolean obtainedValue=userValidated.isValidPassword();
             assertThat(obtainedValue).isEqualTo(expectedValue);

         }

         @DisplayName("password is not valid if its length is > 20 ")
         @Test
          void should_validateRegistration_returns_a_ValidationUser_object_with_false_fields_if_password_length_is_more_than_20(){
             String password="Ci3!!zzzzzaaaaammmmml";
             registrationUser.setPassword(password);

             ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

             boolean expectedValue=false;
             boolean obtainedValue=userValidated.isValidPassword();
             assertThat(obtainedValue).isEqualTo(expectedValue);

         }

         @DisplayName("password is not valid if doesn't contain an upper letter ")
         @Test
          void should_validateRegistration_returns_a_ValidationUser_object_with_false_fields_if_password_doesNot_contain_an_upperLetter(){
             String password="ci3!!zz000";
             registrationUser.setPassword(password);

             ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

             boolean expectedValue=false;
             boolean obtainedValue=userValidated.isValidPassword();
             assertThat(obtainedValue).isEqualTo(expectedValue);

         }

         @DisplayName("password is not valid if doesn't contain a lower letter ")
         @Test
          void should_validateRegistration_returns_a_ValidationUser_object_with_false_fields_if_password_doesNot_contain_a_lowerLetter(){
             String password="CI3!!ZZ000";
             registrationUser.setPassword(password);

             ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

             boolean expectedValue=false;
             boolean obtainedValue=userValidated.isValidPassword();
             assertThat(obtainedValue).isEqualTo(expectedValue);

         }

         @DisplayName("password is not valid if doesn't contain a number ")
         @Test
          void should_validateRegistration_returns_a_ValidationUser_object_with_false_fields_if_password_doesNot_contain_a_number(){
             String password="Ci!!zzCwwe";
             registrationUser.setPassword(password);

             ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

             boolean expectedValue=false;
             boolean obtainedValue=userValidated.isValidPassword();
             assertThat(obtainedValue).isEqualTo(expectedValue);

         }

         @DisplayName("password is not valid if doesn't contain a special char -> ?@#$%^&-")
         @Test
          void should_validateRegistration_returns_a_ValidationUser_object_with_false_fields_if_password_doesNot_contain_a_specialChar(){
             String password="Ci333zzCwwe";
             registrationUser.setPassword(password);

             ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

             boolean expectedValue=false;
             boolean obtainedValue=userValidated.isValidPassword();
             assertThat(obtainedValue).isEqualTo(expectedValue);

         }

         @ParameterizedTest
         @ValueSource(strings = {"Pass201!", "Pass201?" , "Pass201@" , "Pass201#", "Pass201$",
                 "Pass201%" , "Pass201^",  "Pass201&", "Pass201-" , "pAss$201", "@pAss$201",  "2pAss$cc", "pAss$201ccc&$", "pAss$201-@123456789c"})
          void should_validateRegistration_returns_a_ValidationUser_object_with_true_fields_if_password_matches_constraints(String password){

             registrationUser.setPassword(password);

             ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

             boolean expectedValue=true;
             boolean obtainedValue=userValidated.isValidPassword();
             assertThat(obtainedValue).isEqualTo(expectedValue);

         }


         @DisplayName("repeated password is not valid if isn't equals to password")
         @Test
          void should_validateRegistration_returns_a_ValidationUser_object_with_false_repeated_password_if_passwords_doesNotMatch(){

             //See setup method
             String repeatedPassword="Ci333zz!!C";
             registrationUser.setRepeatedPassword(repeatedPassword);

             ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

             boolean expectedValue=false;
             boolean obtainedValue=userValidated.isValidRepeatedPassword();
             assertThat(obtainedValue).isEqualTo(expectedValue);

         }

     }

     @Nested
     class FirstNameValidationTest{
         @DisplayName("first name is not valid if it is empty")
         @Test
          void should_validateRegistration_returns_a_ValidationUser_object_with_false_firstName_field_if_firstName_is_empty(){
             String firstName="";
             registrationUser.setFirstName(firstName);

             ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

             boolean expectedValue=false;
             boolean obtainedValue=userValidated.isValidFirstName();
             assertThat(obtainedValue).isEqualTo(expectedValue);

         }

         @DisplayName("first name is not valid if it contains char different to alphabet chars")
         @ParameterizedTest
         @ValueSource(strings = {"Cristian2", "1Cristian", "1Cristian2", "Cri2stian",  "Cri!stian","Cristian!", "@Cris@ian", "?Cristian-"})
          void should_validateRegistration_returns_a_ValidationUser_object_with_false_firstName_field_if_firstName_contains_notAlphabetChars(String firstName){
             registrationUser.setFirstName(firstName);

             ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

             boolean expectedValue=false;
             boolean obtainedValue=userValidated.isValidFirstName();
             assertThat(obtainedValue).isEqualTo(expectedValue);

         }

     }

    @Nested
    class LastNameValidationTest{
        @DisplayName("last name is not valid if it is empty")
        @Test
         void should_validateRegistration_returns_a_ValidationUser_object_with_false_lastName_field_if_lastName_is_empty(){
            String lastName="";
            registrationUser.setLastName(lastName);

            ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

            boolean expectedValue=false;
            boolean obtainedValue=userValidated.isValidLastName();
            assertThat(obtainedValue).isEqualTo(expectedValue);

        }

        @DisplayName("last name is not valid if it contains char different to alphabet chars")
        @ParameterizedTest
        @ValueSource(strings = {"Cristian2", "1Cristian", "1Cristian2", "Cri2stian",  "Cri!stian","Cristian!", "@Cris@ian", "?Cristian-"})
         void should_validateRegistration_returns_a_ValidationUser_object_with_false_lastName_field_if_lastName_contains_notAlphabetChars(String firstName){
            registrationUser.setLastName(firstName);

            ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

            boolean expectedValue=false;
            boolean obtainedValue=userValidated.isValidLastName();
            assertThat(obtainedValue).isEqualTo(expectedValue);

        }

    }



    @DisplayName("user is valid if all fields are valid")
    @Test
     void should_isValidUser_returns_true_if_all_fields_are_valid(){
        registrationUser.setFirstName("Cristian");
        registrationUser.setLastName("Dramisino");
        registrationUser.setEmail("testemail@gmail.com");
        registrationUser.setPassword("Password1234!!");
        registrationUser.setRepeatedPassword("Password1234!!");
        ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUser);

        boolean expectedValue=true;
        boolean obtainedValue=userValidated.isValidUser();
        assertThat(obtainedValue).isEqualTo(expectedValue);
    }

    @DisplayName("user is not valid if almost one  field is not valid")
    @ParameterizedTest
    @MethodSource("registrationUserParametrizedObjects")
     void should_isValidUser_returns_false_if_one_field_isNot_valid(RegistrationUser registrationUserParametrized){
        ValidationUser userValidated=SignUpValidator.validateRegistration(registrationUserParametrized);

        boolean expectedValue=false;
        boolean obtainedValue=userValidated.isValidUser();
        assertThat(obtainedValue).isEqualTo(expectedValue);
    }


    private static Stream<Arguments> registrationUserParametrizedObjects() {
        RegistrationUser user1= new RegistrationUser();
        user1.setFirstName("");
        user1.setLastName("Dramisino");
        user1.setEmail("testemail@gmail.com");
        user1.setPassword("Password1234!!");
        user1.setRepeatedPassword("Password1234!!");

        RegistrationUser user2= new RegistrationUser();
        user2.setFirstName("Cristian");
        user2.setLastName("");
        user2.setEmail("testemail@gmail.com");
        user2.setPassword("Password1234!!");
        user2.setRepeatedPassword("Password1234!!");

        RegistrationUser user3= new RegistrationUser();
        user3.setFirstName("Cristian");
        user3.setLastName("Dramisino");
        user3.setEmail("");
        user3.setPassword("Password1234!!");
        user3.setRepeatedPassword("Password1234!!");

        RegistrationUser user4= new RegistrationUser();
        user4.setFirstName("Cristian");
        user4.setLastName("Dramisino");
        user4.setEmail("testemail@gmail.com");
        user4.setPassword("");
        user4.setRepeatedPassword("Password1234!!");

        RegistrationUser user5= new RegistrationUser();
        user5.setFirstName("Cristian");
        user5.setLastName("Dramisino");
        user5.setEmail("testemail@gmail.com");
        user5.setPassword("Password1234!!");
        user5.setRepeatedPassword("");


        return Stream.of(
                Arguments.of(user1),
                Arguments.of(user2),
                Arguments.of(user3),
                Arguments.of(user4),
                Arguments.of(user5)
        );
    }







}
