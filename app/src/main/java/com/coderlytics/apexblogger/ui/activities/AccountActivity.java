package com.coderlytics.apexblogger.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.coderlytics.apexblogger.R;
import com.coderlytics.apexblogger.databinding.ActivityAccountBinding;
import com.coderlytics.apexblogger.model.UsersResponse;
import com.coderlytics.apexblogger.utils.MyUtils;
import com.coderlytics.apexblogger.utils.SpHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AccountActivity extends AppCompatActivity {

    ActivityAccountBinding binding;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "AccountActivity";

    AlertDialog loading_dialog;

    DocumentReference docRef;

    private Uri image_uri = null;

    public static final int IMAGE_SELECT_CODE = 1001;

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loading_dialog = MyUtils.getLoadingDialog(this);
        docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        getProfile();

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditClicked();
            }
        });


    }

    Map<String, Object> register_user = new HashMap<>();

    private void onEditClicked() {

        String name = String.valueOf(binding.name.getText());
        String fName = String.valueOf(binding.fName.getText());
        String lLast = String.valueOf(binding.lName.getText());
        String email = String.valueOf(binding.email.getText());
        String phone = String.valueOf(binding.phone.getText());
        String gender = String.valueOf(binding.gender.getText());
        String age = String.valueOf(binding.age.getText());

        if (TextUtils.isEmpty(name)) {
            binding.nameField.setError("Enter Name");
            binding.name.requestFocus();
            return;
        }

        binding.nameField.setError(null);

        if (TextUtils.isEmpty(fName)) {
            binding.fNameField.setError("Enter First Name");
            binding.fName.requestFocus();
            return;
        }

        binding.fNameField.setError(null);

        if (TextUtils.isEmpty(lLast)) {
            binding.lastNameField.setError("Enter Last Name");
            binding.lName.requestFocus();
            return;
        }

        binding.lastNameField.setError(null);

        if (TextUtils.isEmpty(email)) {
            binding.emailField.setError("Enter Email");
            binding.email.requestFocus();
            return;
        }

        binding.emailField.setError(null);


        if (TextUtils.isEmpty(phone)) {
            binding.phoneField.setError("Enter Phone");
            binding.phone.requestFocus();
            return;
        }

        binding.phoneField.setError(null);

        if (TextUtils.isEmpty(gender)) {
            binding.genderField.setError("Enter Gender");
            binding.gender.requestFocus();
            return;
        }

        binding.genderField.setError(null);


        if (TextUtils.isEmpty(age)) {
            binding.ageField.setError("Enter Age");
            binding.age.requestFocus();
            return;
        }

        binding.ageField.setError(null);


        loading_dialog.show();

        register_user.put("email", email);
        register_user.put("username", name);
        register_user.put("first_name", fName);
        register_user.put("last_name", lLast);
        register_user.put("phone", phone);
        register_user.put("age", age);
        register_user.put("gender", gender);
        register_user.put("updated_at", new Date());

        if (image_uri != null) {



            mStorageRef.child("users/" + SpHelper.getValue(this
                    ,SpHelper.ID)).putFile(image_uri)

                    .addOnSuccessListener(taskSnapshot -> {
                        Log.d(TAG, "onViewClicked: " + "photo upload");

                        Objects.requireNonNull(Objects.requireNonNull(taskSnapshot
                                        .getMetadata()).getReference()).getDownloadUrl()

                                .addOnSuccessListener(uri -> {
                                    register_user.put("imageURL", uri.toString());
                                    setData();
                                })
                                .addOnFailureListener(e -> {
                                    loading_dialog.dismiss();
                                    if (e instanceof IOException)
                                        Toast.makeText(AccountActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                                    else
                                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                });
                    })

                    .addOnFailureListener(e -> {
                        loading_dialog.dismiss();
                        if (e instanceof IOException)
                            Toast.makeText(AccountActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                        else
                            Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                    });
            return;
        }
        setData();
    }

    private void setData() {


        docRef.update(register_user)
                .addOnSuccessListener(aVoid -> {
                    loading_dialog.dismiss();
                    Log.d(TAG, "onSuccess: update ");

                    Toast.makeText(this, "Update Profile Success", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    loading_dialog.dismiss();
                    if (e instanceof IOException)
                        Toast.makeText(AccountActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                    else
                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_SELECT_CODE)
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    Toast.makeText(this, "Unable to handle image.", Toast.LENGTH_SHORT).show();
                    image_uri = null;
                    binding.profilePicImageView.setImageResource(R.drawable.defavatar);
                    return;
                }
                image_uri = data.getData();
                Glide.with(this).load(image_uri).into(binding.profilePicImageView);
            } else {
                image_uri = null;
                binding.profilePicImageView.setImageResource(R.drawable.defavatar);
            }
    }

    public void onPickImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_SELECT_CODE);
    }

    private void getProfile() {
        loading_dialog.show();

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            loading_dialog.dismiss();

            if (!documentSnapshot.exists()) {
                Log.d(TAG, "onSuccess: LIST EMPTY");
                return;
            }

            UsersResponse types = documentSnapshot.toObject(UsersResponse.class);
            binding.setModel(types);

            if (types == null) return;
            String AGE = types.getAge();
            String EMAIL = types.getEmail();
            String GENDER = types.getGender();
            String IMAGEURL = types.getImageURL();
            String ID = types.getId();
            String PHONE = types.getPhone();
            String ROLE = types.getRole();
            String USERNAME = types.getUsername();

            SpHelper.makeLogin(AccountActivity.this, AGE,
                    EMAIL,
                    GENDER,
                    IMAGEURL,
                    ID,
                    PHONE,
                    ROLE,
                    USERNAME);

        }).addOnFailureListener(e -> {
            loading_dialog.dismiss();
            Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            Toast.makeText(AccountActivity.this, "error", Toast.LENGTH_SHORT).show();
        });
    }
}
