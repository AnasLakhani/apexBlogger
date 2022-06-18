package com.coderlytics.apexblogger.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.coderlytics.apexblogger.R;
import com.coderlytics.apexblogger.databinding.ActivityAddCategoryBinding;
import com.coderlytics.apexblogger.utils.MyUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AddCategoryActivity extends AppCompatActivity {

    public static final int IMAGE_SELECT_CODE = 1001;

    ActivityAddCategoryBinding binding;

    private Uri image_uri;

    private int GALLERY = 1;

    private String title, content, user;

    private androidx.appcompat.app.AlertDialog loading_dialog;

    private FirebaseFirestore db;

    private StorageReference mStorageRef;

    private static final String TAG = "AddCategory";

    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        loading_dialog = MyUtils.getLoadingDialog(this);

        binding.toolbar.setNavigationOnClickListener(view -> finish());

        user = FirebaseAuth.getInstance().getCurrentUser().getUid();


        binding.imageButton.setOnClickListener(view -> imageVideo());
        binding.addButton.setOnClickListener(v -> addfeeds());

    }

    private void addfeeds() {
        if (image_uri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        title = String.valueOf(binding.etTitle.getText());


        if (TextUtils.isEmpty(title)) {
            binding.etTitle.setError("Enter a title");
            binding.etTitle.requestFocus();
            return;
        }

        loading_dialog.show();

        mStorageRef.child("categories/" + UUID.randomUUID()).putFile(image_uri)
                .addOnSuccessListener(taskSnapshot -> {
                    Log.d(TAG, "onViewClicked: " + "upload");

                    Objects.requireNonNull(Objects
                                    .requireNonNull(taskSnapshot.getMetadata())
                                    .getReference()).getDownloadUrl()
                            .addOnSuccessListener(uri -> setdata(uri.toString()))

                            .addOnFailureListener(e -> {
                                loading_dialog.dismiss();
                                if (e instanceof IOException)
                                    Toast.makeText(AddCategoryActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                                else
                                    Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    loading_dialog.dismiss();
                    if (e instanceof IOException)
                        Toast.makeText(AddCategoryActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                    else
                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                });


    }

    private void setdata(String links) {

        String random_id = String.valueOf(UUID.randomUUID());

        String user_id = auth.getCurrentUser().getUid();

        Map<String, Object> blog = new HashMap<>();
        blog.put("id", random_id);
        blog.put("title", title);
        blog.put("image", links);
        blog.put("created_at", new Date());
        blog.put("create_by", user_id);
        blog.put("updated_by", user_id);
        blog.put("updated_at", new Date());

        Log.d(TAG, "Title: " + title + "\n" + "Content: " + content + "links" + links);

        db.collection("categories").document(random_id).set(blog)
                .addOnSuccessListener(documentReference -> {
                    loading_dialog.dismiss();
                    Toast.makeText(AddCategoryActivity.this, "Success!", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(this, AdminBlogs.class));
//                    finish();


                })
                .addOnFailureListener(e -> {
                    loading_dialog.dismiss();
                    if (e instanceof IOException) {
                        Toast.makeText(this, "internet connection error", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "onFailure: failure " + e.getLocalizedMessage());
                        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void imageVideo() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select image from gallery"
//                , "Select video from gallery"
        };
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            pickImage();
                            break;
//                        case 1:
//                            pickVideo();
//                            break;
                    }
                });
        pictureDialog.show();
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_SELECT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_SELECT_CODE || requestCode == GALLERY)
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    Toast.makeText(this, "Unable to handle image.", Toast.LENGTH_SHORT).show();
                    image_uri = null;
                    binding.imageButton.setImageResource(R.drawable.image_selection_placeholder);
                    return;
                }
                image_uri = data.getData();
                Glide.with(this).load(image_uri).into(binding.imageButton);
            } else {
                image_uri = null;
                 binding.imageButton.setImageResource(R.drawable.image_selection_placeholder);
            }
    }
}
