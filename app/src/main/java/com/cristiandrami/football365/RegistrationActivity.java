package com.cristiandrami.football365;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cristiandrami.football365.model.Utilities;
import com.cristiandrami.football365.model.registration.RegistrationUser;
import com.cristiandrami.football365.model.registration.SignUpValidator;
import com.cristiandrami.football365.model.registration.ValidationUser;
import com.cristiandrami.football365.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private Button registrationButton;
    private Button switchToLoginActivityButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        bindGraphicalComponents();

        setRegistrationButtonListener();
    }

    private void setRegistrationButtonListener() {
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrationUser newUser= new RegistrationUser();
                setNewUserFields(newUser);
                ValidationUser validatedUser= SignUpValidator.validateRegistration(newUser);
                if(validatedUser.isValidUser()){
                    registerUser(newUser);
                }else{
                    setInvalidFields(validatedUser);
                }
            }
        });
    }

    private void registerUser(RegistrationUser newUser) {
       firebaseAuth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Map<String, Object> user = new HashMap<>();
                   user.put("firstName", newUser.getFirstName());
                   user.put("lastName", newUser.getLastName());
                   user.put("email", newUser.getEmail());
                   addToDatabase(user, newUser.getEmail());
               }
               else{
                   Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
               }
           }
       });
    }


    private void addToDatabase(Map<String, Object> user, String email) {
        firestoneFirestoreDB.collection("users").document(email).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i("user","user added");
                    switchToMainActivity();
                }
            }
        });
    }

    private void switchToMainActivity() {
       Intent switchToMainActivityIntent= new Intent(RegistrationActivity.this, MainActivity.class);
       startActivity(switchToMainActivityIntent);
    }

    private void setInvalidFields(ValidationUser validatedUser) {
        if(!validatedUser.isValidEmail()) emailTextField.setError(this.getString(R.string.email_not_valid));
        if(!validatedUser.isValidLastName()) lastNameTextField.setError(this.getString(R.string.last_name_empty));
        if(!validatedUser.isValidFirstName()) firstNameTextField.setError(this.getString(R.string.first_name_empty));
        if(!validatedUser.isValidPassword()) passwordTextField.setError(this.getString(R.string.password_not_valid));
        if(!validatedUser.isValidRepeatedPassword()) repeatedPasswordTextField.setError((this.getString(R.string.repeated_password_doesnt_match)));


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
        passwordTextField= findViewById(R.id.password_field_edit_text);
        repeatedPasswordTextField= findViewById(R.id.repeat_password_field_edit_text);
        emailTextField=findViewById(R.id.email_field_edit_text);
        registrationButton=findViewById(R.id.registration_button);
        switchToLoginActivityButton=findViewById(R.id.switch_to_login_activity_button);


    }
}