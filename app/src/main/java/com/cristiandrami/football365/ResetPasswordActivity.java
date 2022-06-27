package com.cristiandrami.football365;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cristiandrami.football365.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextView emailSentText;
    private TextView emailSendInformation;
    private Button sendEmailButton;
    private Button gotToLoginButton;
    private ProgressBar progressBar;
    private TextInputEditText emailTextField;
    private TextInputLayout emailLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        setGraphicalBinding();

        setListenersOnSendEmailButton();

        setListenersOnGoToLoginButton();
    }

    private void setListenersOnGoToLoginButton() {
        gotToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void setListenersOnSendEmailButton() {
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailLayout.setError(null);
                String email=emailTextField.getText().toString().trim();
                if(email.isEmpty()){
                    emailLayout.setError(getString(R.string.email_empty));
                }else{
                    sendEmailButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    emailSendInformation.setVisibility(View.GONE);

                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                emailSentText.setVisibility(View.VISIBLE);
                                gotToLoginButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                emailLayout.setVisibility(View.GONE);
                            }else{
                                sendEmailButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                emailSendInformation.setVisibility(View.VISIBLE);
                                emailLayout.setError(getString(R.string.email_not_used));
                            }
                        }
                    });
                }
            }
        });
    }

    private void setGraphicalBinding() {
        emailSentText= findViewById(R.id.activity_reset_password_email_sent);
        emailSendInformation= findViewById(R.id.activity_reset_password_email_information);
        sendEmailButton= findViewById(R.id.activity_reset_password_send_email_button);
        gotToLoginButton = findViewById(R.id.activity_reset_password_go_to_login_button);
        progressBar= findViewById(R.id.activity_reset_password_progress_bar);
        emailTextField= findViewById(R.id.activity_reset_password_field_edit_text);
        emailLayout= findViewById(R.id.activity_reset_password_field_layout);
    }
}