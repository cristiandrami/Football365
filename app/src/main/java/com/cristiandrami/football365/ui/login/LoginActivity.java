package com.cristiandrami.football365.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.cristiandrami.football365.EmailConfirmationActivity;
import com.cristiandrami.football365.MainActivity;
import com.cristiandrami.football365.R;
import com.cristiandrami.football365.ResetPasswordActivity;
import com.cristiandrami.football365.model.utilities.matches_utilities.CompetitionsUtilities;
import com.cristiandrami.football365.ui.registration.RegistrationActivity;
import com.cristiandrami.football365.model.login.UserLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
    private TextInputLayout passwordTextLayout;
    private TextInputLayout emailTextLayout;
    private ProgressBar loginProgressBar;
    private Button forgetPasswordButton;


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
            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                switchToMainActivity();
                finish();
            }else{
                switchToEmailVerificationActivity();
            }

        }
        bindGraphicalObjects();

        setLoginButtonListener();
        setRegistrationButtonLinkListener();
        setPasswordTextFieldListener();
        setForgetPasswordButtonListener();

    }

    private void setForgetPasswordButtonListener() {
        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchToResetPasswordActivity = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(switchToResetPasswordActivity);
            }
        });

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

    private void setPasswordTextFieldListener() {
        passwordTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordTextLayout.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        boolean areFieldNotEmpty=true;
        UserLogin user= new UserLogin();
        user.setEmail(emailTextField.getText().toString().trim());
        user.setPassword(passwordTextField.getText().toString());

        if(user.getEmail().isEmpty()){
            emailTextLayout.setError(getString(R.string.email_empty));
            areFieldNotEmpty=false;
        }
        if(user.getPassword().isEmpty()){
            passwordTextLayout.setError(getString(R.string.password_empty));
            areFieldNotEmpty=false;

        }
        if(areFieldNotEmpty){
            setProgressBarAndButtonVisibility(View.GONE, View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        if(firebaseAuth.getCurrentUser().isEmailVerified()){
                            switchToMainActivity();
                            finish();
                        }else{

                            setProgressBarAndButtonVisibility(View.VISIBLE, View.GONE);
                            switchToEmailVerificationActivity();
                        }


                    } else {
                        setProgressBarAndButtonVisibility(View.VISIBLE, View.GONE);
                        emailTextLayout.setError(getString(R.string.login_failed));
                    }
                }
            });
        }





    }

    private void setProgressBarAndButtonVisibility(int loginButtonVisibility, int loginProgressBarVisibility) {
        loginButton.setVisibility(loginButtonVisibility);
        loginProgressBar.setVisibility(loginProgressBarVisibility);
    }

    private void switchToEmailVerificationActivity() {
        Intent switchToEmailVerificationActivity = new Intent(LoginActivity.this, EmailConfirmationActivity.class);
        startActivity(switchToEmailVerificationActivity);
    }

    private void switchToMainActivity() {
       Intent switchToMainActivity = new Intent(LoginActivity.this, MainActivity.class);
       startActivity(switchToMainActivity);
    }

    private void bindGraphicalObjects() {
        loginProgressBar=findViewById(R.id.activity_login_progress_bar);
        loginButton= findViewById(R.id.login_button);
        emailTextField = findViewById(R.id.activity_login_email_field_edit_text);
        passwordTextField = findViewById(R.id.activity_login_password_field_edit_text);
        registrationLinkButton= findViewById(R.id.register_button_link);
        passwordTextLayout= findViewById(R.id.activity_login_password_field_layout);
        emailTextLayout=findViewById(R.id.activity_login_email_field_layout);
        forgetPasswordButton= findViewById(R.id.activity_login_forget_password_button);

    }
}