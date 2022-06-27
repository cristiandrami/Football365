package com.cristiandrami.football365.ui.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cristiandrami.football365.EmailConfirmationActivity;
import com.cristiandrami.football365.R;
import com.cristiandrami.football365.model.registration.RegistrationUser;
import com.cristiandrami.football365.model.registration.SignUpValidator;
import com.cristiandrami.football365.model.registration.ValidationUser;
import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


/**
 * This class is used as a Controller for the Registration Activity
 * It contains all methods to manage the Registration Activity
 *
 * @author Cristian D. Dramisino
 *
 */

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private FirebaseFirestore firestoneFirestoreDB = FirebaseFirestore.getInstance();
    private TextInputEditText firstNameTextField;
    private TextInputEditText lastNameTextField;
    private TextInputEditText passwordTextField;
    private TextInputEditText repeatedPasswordTextField;
    private TextInputEditText emailTextField;
    private TextInputLayout firstNameTextLayout;
    private TextInputLayout lastNameTextLayout;
    private TextInputLayout passwordTextLayout;
    private TextInputLayout repeatedPasswordTextLayout;
    private TextInputLayout emailTextLayout;

    private Button registrationButton;
    private Button switchToLoginActivityButton;

    private TextView backToLoginTextView;

    private ProgressBar registrationProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);
        this.getSupportActionBar().hide();

        bindGraphicalComponents();

        setRegistrationButtonListener();
        setSwitchToLoginButtonListener();
        setBackToLoginActivityListener();
        setPasswordTextFieldListener();
        setRepeatedPasswordTextFieldListener();

    }

    private void setRepeatedPasswordTextFieldListener() {


        repeatedPasswordTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /***
                 * This method is not needed
                 */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                repeatedPasswordTextLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                /***
                 * This method is not needed
                 */
            }
        });
    }

    private void setPasswordTextFieldListener() {
        passwordTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /***
                 * This method is not needed
                 */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordTextLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                /***
                 * This method is not needed
                 */
            }
        });
    }

    private void setBackToLoginActivityListener() {
        backToLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setSwitchToLoginButtonListener() {
        switchToLoginActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void setRegistrationButtonListener() {
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchRegistrationButtonAndProgressBar(View.GONE, View.VISIBLE);

                RegistrationUser newUser= new RegistrationUser();
                setNewUserFields(newUser);
                ValidationUser validatedUser= SignUpValidator.validateRegistration(newUser);
                if(validatedUser.isValidUser()){
                    registerUserCredentialsAndData(newUser);

                }else{
                    setInvalidFields(validatedUser);
                    switchRegistrationButtonAndProgressBar(View.VISIBLE, View.GONE);

                }
            }
        });
    }

    private void switchRegistrationButtonAndProgressBar(int buttonVisibility, int progressBarVisibility) {
        registrationButton.setVisibility(buttonVisibility);
        registrationProgressBar.setVisibility(progressBarVisibility);
    }

    private void registerUserCredentialsAndData(RegistrationUser newUser) {
       firebaseAuth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Map<String, Object> user = new HashMap<>();
                   user.put(UtilitiesStrings.FIREBASE_DOCUMENT_FIELD_FIRST_NAME, newUser.getFirstName());
                   user.put(UtilitiesStrings.FIREBASE_DOCUMENT_FIELD_LAST_NAME, newUser.getLastName());
                   user.put(UtilitiesStrings.FIREBASE_DOCUMENT_FIELD_EMAIL, newUser.getEmail());
                   addUserDataToDatabase(user, newUser.getEmail());
               }
               else{
                   switchRegistrationButtonAndProgressBar(View.VISIBLE, View.GONE);
                   emailTextLayout.setError(getString(R.string.email_already_used));
               }
           }
       });
    }


    private void addUserDataToDatabase(Map<String, Object> user, String email) {

        firestoneFirestoreDB.collection(UtilitiesStrings.FIREBASE_USERS_COLLECTION_NAME).document(email).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    firebaseAuth.getCurrentUser().sendEmailVerification();

                    switchToEmailConfirmationActivity();
                    finish();
                }
            }
        });
    }

    private void switchToEmailConfirmationActivity() {
        Intent switchToEmailVerificationActivity = new Intent(RegistrationActivity.this, EmailConfirmationActivity.class);
        startActivity(switchToEmailVerificationActivity);
    }



    private void setInvalidFields(ValidationUser validatedUser) {
        if(!validatedUser.isValidEmail()) emailTextLayout.setError(this.getString(R.string.email_not_valid));
        if(!validatedUser.isValidLastName()) lastNameTextLayout.setError(this.getString(R.string.last_name_empty));
        if(!validatedUser.isValidFirstName()) firstNameTextLayout.setError(this.getString(R.string.first_name_empty));
        if(!validatedUser.isValidPassword()){
            passwordTextLayout.setError(this.getString(R.string.password_not_valid));

        }
        if(!validatedUser.isValidRepeatedPassword()){
            repeatedPasswordTextLayout.setError((this.getString(R.string.repeated_password_doesnt_match)));

        }


    }


    private void setNewUserFields(RegistrationUser newUserToValidate) {
        newUserToValidate.setEmail(emailTextField.getText().toString().trim());
        newUserToValidate.setFirstName(firstNameTextField.getText().toString().trim());
        newUserToValidate.setLastName(lastNameTextField.getText().toString().trim());
        newUserToValidate.setPassword(passwordTextField.getText().toString().trim());
        newUserToValidate.setRepeatedPassword(repeatedPasswordTextField.getText().toString().trim());
    }


    private void bindGraphicalComponents() {
        firstNameTextField= findViewById(R.id.first_name_field_edit_text);
        lastNameTextField= findViewById(R.id.last_name_field_edit_text);
        passwordTextField= findViewById(R.id.activity_registration_password_field_edit_text);
        repeatedPasswordTextField= findViewById(R.id.repeat_password_field_edit_text);
        emailTextField=findViewById(R.id.activity_login_email_field_edit_text);
        registrationButton=findViewById(R.id.registration_button);
        switchToLoginActivityButton=findViewById(R.id.switch_to_login_activity_button);
        backToLoginTextView=findViewById(R.id.activity_registration_return_to_login_text_view);
        registrationProgressBar=findViewById(R.id.activity_registration_registration_progress_bar);



        firstNameTextLayout= findViewById(R.id.activity_registration_first_name_field_layout);
        lastNameTextLayout= findViewById(R.id.activity_registration_last_name_field_layout);
        passwordTextLayout= findViewById(R.id.activity_registration_password_field_layout);
        repeatedPasswordTextLayout= findViewById(R.id.activity_registration_repeat_password_field_layout);
        emailTextLayout= findViewById(R.id.activity_registration_email_field_layout);

    }
}