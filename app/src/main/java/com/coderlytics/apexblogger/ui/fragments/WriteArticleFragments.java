package com.coderlytics.apexblogger.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.coderlytics.apexblogger.R;
import com.coderlytics.apexblogger.adapters.SpinnerCategoryAdapter;
import com.coderlytics.apexblogger.databinding.FragmentsWriteArticlesBinding;
import com.coderlytics.apexblogger.model.BlogCategoriesResponse;
import com.coderlytics.apexblogger.utils.MyUtils;
import com.coderlytics.apexblogger.utils.SpHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class WriteArticleFragments extends Fragment {

    FragmentsWriteArticlesBinding binding;

    public static final int IMAGE_SELECT_CODE = 1001;

    private static final String TAG = "WriteArticleFragments";

    private Uri image_uri;

    private int GALLERY = 1;

    private String title, content, user,cat_id;

    private androidx.appcompat.app.AlertDialog loading_dialog;

    private FirebaseFirestore db;

    private StorageReference mStorageRef;

    Activity activity;

    Context context;

    CollectionReference blogsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentsWriteArticlesBinding.inflate(inflater, container, false);

        context = binding.getRoot().getContext();
        activity = (Activity) context;
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        blogsRef = db.collection("categories");

        loading_dialog = MyUtils.getLoadingDialog(activity);

//        binding.toolbar.setNavigationOnClickListener(view -> finish());

        user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding.imageButton.setOnClickListener(view -> imageVideo());

        binding.addArticles.setOnClickListener(view -> addArticles());

//        binding.spinnerAreas.setAdapter(new SpinnerCategoryAdapter());

        populateRecyclerView();

        return binding.getRoot();
    }

    private void populateRecyclerView() {
        blogsRef.orderBy("created_at", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<BlogCategoriesResponse> list = task.getResult().toObjects(BlogCategoriesResponse.class);
                    binding.spinnerAreas.setAdapter(new SpinnerCategoryAdapter(list));
//
                }
            }
        });
    }

    private void addArticles() {

        MyUtils.hideKeyboard(requireActivity());

//        Toast.makeText(requireActivity(), "All Done", Toast.LENGTH_SHORT).show();

        addfeeds();

    }

    private void addfeeds() {

        BlogCategoriesResponse response = (BlogCategoriesResponse) binding.spinnerAreas.getSelectedItem();

        title = String.valueOf(binding.etTitle.getText());
        content = String.valueOf(binding.etArticles.getText());
        cat_id = String.valueOf(response.getId());

        if (TextUtils.isEmpty(title)) {
            binding.etTitleParent.setError("Enter a title");
            binding.etTitle.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(content)) {
            binding.etArticlesParent.setError("Enter a content");
            binding.etTitle.requestFocus();
            return;
        }

        if (cat_id.isEmpty()) {
            Toast.makeText(context, "Select Category", Toast.LENGTH_SHORT).show();
            return;
        }

        loading_dialog.show();

        //IF USER DONT ENTER IMAGE
        if (image_uri == null) {
            setdata(null);
            return;
        }

        mStorageRef.child("blogs/" + UUID.randomUUID()).putFile(image_uri)
                .addOnSuccessListener(taskSnapshot -> {
                    Log.d(TAG, "onViewClicked: " + "upload");

                    Objects.requireNonNull(Objects
                                    .requireNonNull(taskSnapshot.getMetadata())
                                    .getReference()).getDownloadUrl()
                            .addOnSuccessListener(uri -> setdata(uri.toString()))

                            .addOnFailureListener(e -> {
                                loading_dialog.dismiss();
                                if (e instanceof IOException)
                                    Toast.makeText(context, "internet connection error", Toast.LENGTH_SHORT).show();
                                else
                                    Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    loading_dialog.dismiss();
                    if (e instanceof IOException)
                        Toast.makeText(context, "internet connection error", Toast.LENGTH_SHORT).show();
                    else
                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                });


    }

    private void setdata(String links) {

        String random_id = String.valueOf(UUID.randomUUID());

        Map<String, Object> blog = new HashMap<>();
        blog.put("id", random_id);
        blog.put("title", title);
        blog.put("content", content);
        blog.put("vote", 0);
        blog.put("cat_id", cat_id);
        blog.put("image", links);
        blog.put("created_at", new Date());
        blog.put("updated_at", new Date());
        blog.put("create_by", user);
        blog.put("username", SpHelper.getValue(context,SpHelper.USERNAME));
        blog.put("updated_by", user);

        Log.d(TAG, "Title: " + title + "\n" + "Content: " + content + "links" + links);

        db.collection("blogs").document(random_id).set(blog)
                .addOnSuccessListener(documentReference -> {
                    loading_dialog.dismiss();
//                    Log.d(TAG, "onSuccess: get id " + documentReference);
                    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();

                    Navigation.findNavController(binding.getRoot()).popBackStack();
//                    startActivity(new Intent(this, AdminBlogs.class));
//                    finish();


                })
                .addOnFailureListener(e -> {
                    loading_dialog.dismiss();
                    if (e instanceof IOException) {
                        Toast.makeText(context, "internet connection error", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "onFailure: failure " + e.getLocalizedMessage());
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void imageVideo() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(context);
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
                    Toast.makeText(context, "Unable to handle image.", Toast.LENGTH_SHORT).show();
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
