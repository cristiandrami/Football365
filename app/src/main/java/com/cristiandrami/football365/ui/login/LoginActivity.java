package com.cristiandrami.football365.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cristiandrami.football365.MainActivity;
import com.cristiandrami.football365.R;
import com.cristiandrami.football365.model.utilities.matches_utilities.CompetitionsUtilities;
import com.cristiandrami.football365.ui.registration.RegistrationActivity;
import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
import com.cristiandrami.football365.model.login.UserLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This is the class that manages the Login activity, it is a graphical controller
 *
 * {@link FirebaseAuth} is used to manage the user authentication
 * @see FirebaseAuth
 * @author Cristian D. Dramisino
 *
 */

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private Button loginButton;
    private Button registrationLinkButton;
    private TextInputEditText emailTextField;
    private TextInputEditText passwordTextField;


    /**
     * This method is called when we open the app, here we check if a user is already logged in,
     * if a user is logged in the app switches directly to the MainActivity*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().hide();

        setupApplication();

        if(firebaseAuth.getCurrentUser()!=null) {
            switchToMainActivity();
            finish();
        }
        bindGraphicalObjects();

        setLoginButtonListener();
        setRegistrationButtonLinkListener();
    }

    private void setupApplication() {
        CompetitionsUtilities.getInstance();
    }

    private void setRegistrationButtonLinkListener() {
        registrationLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToRegistrationActivity();
            }
        });

    }

    private void switchToRegistrationActivity() {
        Intent switchToRegistrationActivity= new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(switchToRegistrationActivity);
    }

    private void setLoginButtonListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    /**
     * This method used to do the login,
     * if the email and password are correct the app switches to MainActivity
     * if the email or the password are not correct the UI show the error to the user*/
    private void login() {
        UserLogin user= new UserLogin();
        user.setEmail(emailTextField.getText().toString().trim());
        user.setPassword(passwordTextField.getText().toString());


        firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information

                    Toast.makeText(LoginActivity.this, "Welcome",
                            Toast.LENGTH_SHORT).show();

                    switchToMainActivity();
                    finish();

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(LoginActivity.this, UtilitiesStrings.LOGIN_FAILED,
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void switchToMainActivity() {
       Intent switchToMainActivity = new Intent(LoginActivity.this, MainActivity.class);
       startActivity(switchToMainActivity);
    }

    private void bindGraphicalObjects() {
        loginButton= findViewById(R.id.login_button);
        emailTextField = findViewById(R.id.email_field_edit_text);
        passwordTextField = findViewById(R.id.password_field_edit_text);
        registrationLinkButton= findViewById(R.id.register_button_link);

    }
}