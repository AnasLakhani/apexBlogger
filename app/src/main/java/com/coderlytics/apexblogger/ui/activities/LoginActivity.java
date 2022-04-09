package com.coderlytics.apexblogger.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.coderlytics.apexblogger.databinding.ActivityLoginBinding;
import com.coderlytics.apexblogger.utils.MyUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    FirebaseAuth auth;

    AlertDialog loading_dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loading_dialog = MyUtils.getLoadingDialog(this);
        auth = FirebaseAuth.getInstance();

    }

    public void onSignUp(View view) {
        startActivity(new Intent(this, SignupActivity.class));
    }


    public void onLoginClick(View view) {
        String email = String.valueOf(binding.email.getText());
        String pass = String.valueOf(binding.pass.getText());

        if (email.isEmpty()) {
            binding.emailField.setError("Enter email");
            return;
        }

        binding.emailField.setError(null);

        if (pass.isEmpty()) {
            binding.passField.setError("Enter pass");
            return;
        }

        binding.passField.setError(null);

        MyUtils.hideKeyboard(this);

        loading_dialog.show();

        auth.signInWithEmailAndPassword(email, pass).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        loading_dialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
