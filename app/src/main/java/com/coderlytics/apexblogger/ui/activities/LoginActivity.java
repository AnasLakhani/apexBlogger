package com.coderlytics.apexblogger.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.coderlytics.apexblogger.databinding.ActivityLoginBinding;
import com.coderlytics.apexblogger.model.UsersResponse;
import com.coderlytics.apexblogger.utils.MyUtils;
import com.coderlytics.apexblogger.utils.SpHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    FirebaseAuth auth;

    AlertDialog loading_dialog;

    DocumentReference docRef;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "LoginActivity";

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
                addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        loading_dialog.dismiss();
                        return;
                    }

                    docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    docRef.get().addOnSuccessListener(documentSnapshot -> {
                        loading_dialog.dismiss();

                        if (!documentSnapshot.exists()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                            return;
                        }

                        UsersResponse types = documentSnapshot.toObject(UsersResponse.class);

                        if (types == null) return;
                        String AGE = types.getAge();
                        String EMAIL = types.getEmail();
                        String GENDER = types.getGender();
                        String IMAGEURL = types.getImageURL();
                        String ID = types.getId();
                        String PHONE = types.getPhone();
                        String ROLE = types.getRole();
                        String USERNAME = types.getUsername();

                        SpHelper.makeLogin(LoginActivity.this, AGE,
                                EMAIL,
                                GENDER,
                                IMAGEURL,
                                ID,
                                PHONE,
                                ROLE,
                                USERNAME);

                        Toast.makeText(LoginActivity.this, "Authentication Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                    }).addOnFailureListener(e -> {
                        loading_dialog.dismiss();
                        Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                        Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                    });

                });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }
}
