package com.coderlytics.apexblogger.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.coderlytics.apexblogger.databinding.ActivitySignupBinding;
import com.coderlytics.apexblogger.utils.MyUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth auth;

    ActivitySignupBinding binding;

    private static final String TAG = "SignupActivity";

    AlertDialog loading_dialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference blogsRef = db.collection("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loading_dialog = MyUtils.getLoadingDialog(this);
        auth = FirebaseAuth.getInstance();
    }


    public void onSignUpClick(View view) {
        String name = String.valueOf(binding.name.getText());
        String email = String.valueOf(binding.email.getText());
        String pass = String.valueOf(binding.pass.getText());

        if (name.isEmpty()) {
            binding.nameField.setError("Enter name");
            return;
        }

        binding.nameField.setError(null);


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

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

//                loading_dialog.dismiss();

                FirebaseUser firebaseUser = auth.getCurrentUser();

                String userid = firebaseUser.getUid();

                DocumentReference blogRef = blogsRef.document(userid);

                Map<String, Object> register_user = new HashMap<>();
                register_user.put("id", userid);
                register_user.put("email", email);
                register_user.put("password", pass);
                register_user.put("username", name);
                register_user.put("phone", "phone");
                register_user.put("age", "age");
                register_user.put("gender", "gender");
                register_user.put("created_at", new Date());
                register_user.put("updated_at", new Date());
                register_user.put("imageURL", "default");

                blogRef.set(register_user).addOnSuccessListener(aVoid -> {
                    loading_dialog.dismiss();
                    Toast.makeText(this, "Signup Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();

                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Signup Failed!", Toast.LENGTH_SHORT).show();
                    loading_dialog.dismiss();
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());

                });

            } else {
                loading_dialog.dismiss();
                Log.d(TAG, "onComplete: " + task.getException().toString());
                Toast.makeText(SignupActivity.this, "You Can't register with this email or password", Toast.LENGTH_SHORT).show();
            }

        });

    }
}