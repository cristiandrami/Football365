package com.cristiandrami.football365.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cristiandrami.football365.MainActivity;
import com.cristiandrami.football365.R;
import com.cristiandrami.football365.model.Utilities;
import com.cristiandrami.football365.model.login.EmailPasswordUserLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private Button loginButton;
    private TextInputEditText emailTextField;
    private TextInputEditText passwordTextField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        if(firebaseAuth.getCurrentUser()!=null)
        {
            goToMainActivity();
            //finish();
        }
        bindGraphicalObjects();

        setListeners();
    }

    private void setListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        EmailPasswordUserLogin user= new EmailPasswordUserLogin();
        user.setEmail(emailTextField.getText().toString());
        user.setPassword(passwordTextField.getText().toString());


        firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("login: ", "signInWithEmail:success");
                    Toast.makeText(LoginActivity.this, "Welcome",
                            Toast.LENGTH_SHORT).show();

                    goToMainActivity();
                    finish();

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("login", "signInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, Utilities.LOGIN_FAILED,
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void goToMainActivity() {
       Intent switchToMainActivity = new Intent(LoginActivity.this, MainActivity.class);
       startActivity(switchToMainActivity);
    }

    private void bindGraphicalObjects() {
        loginButton= findViewById(R.id.login_button);
        emailTextField = findViewById(R.id.email_field_edit_text);
        passwordTextField = findViewById(R.id.password_field_edit_text);

    }
}