package com.coderlytics.apexblogger.ui.fragments;

import android.app.Activity;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.coderlytics.apexblogger.R;
import com.coderlytics.apexblogger.adapters.BlogAdapter;
import com.coderlytics.apexblogger.databinding.FragmentsProfileBinding;
import com.coderlytics.apexblogger.databinding.FragmentsWriteArticlesBinding;
import com.coderlytics.apexblogger.model.BlogsResponse;
import com.coderlytics.apexblogger.utils.MyUtils;
import com.coderlytics.apexblogger.utils.SpHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ProfileFragments extends Fragment implements BlogAdapter.OnItemClickListener {

    FragmentsProfileBinding binding;

    public static final int IMAGE_SELECT_CODE = 1001;

    private Uri image_uri;

    private int GALLERY = 1;

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    Context context;

    private BlogAdapter adapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference blogsRef = db.collection("blogs");

    private static final String TAG = "AdminBlogs";

    private AlertDialog loading_dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentsProfileBinding.inflate(inflater, container, false);
        context = binding.getRoot().getContext();
        bottomSheetDialog = new BottomSheetDialog(context);
        MyUtils.requestOptions(binding.profileImg, SpHelper.getValue(context, SpHelper.IMAGEURL));
        binding.nameText.setText(SpHelper.getValue(context, SpHelper.USERNAME));

        Activity activity = (Activity) context;
        loading_dialog = MyUtils.getLoadingDialog(activity);

        binding.blogs.swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.startListening();
            binding.blogs.swipeRefreshLayout.setRefreshing(false);
        });

        setUpRecyclerView();

//        binding.txt.setText("FragmentsProfileBinding");
        return binding.getRoot();
    }

    private void setUpRecyclerView() {

        Query query = blogsRef.orderBy("created_at", Query.Direction.DESCENDING).whereEqualTo("updated_by", SpHelper.getValue(context, SpHelper.ID));

        adapter = new BlogAdapter(query, ProfileFragments.this,true) {

            @Override
            protected void onDataChanged() {
                Log.d(TAG, "onDataChanged: ");

                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    binding.blogs.recyclerView.setVisibility(View.GONE);
                    binding.blogs.noData.setVisibility(View.VISIBLE);
                } else {
                    binding.blogs.recyclerView.setVisibility(View.VISIBLE);
                    binding.blogs.noData.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {

                Snackbar.make(binding.blogs.getRoot(),
                        "Error: check logs for info." + e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        };


        binding.blogs.recyclerView.setHasFixedSize(true);
        binding.blogs.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.blogs.recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public void onShareClick(DocumentSnapshot documentSnapshot) {
        BlogsResponse model = documentSnapshot.toObject(BlogsResponse.class);
        MyUtils.share(binding.getRoot().getContext(), model.getTitle(), model.getContent());
    }

    BottomSheetDialog bottomSheetDialog;

    private String title, content, user;

    FragmentsWriteArticlesBinding bindings;

    DocumentReference blogRef;

    @Override
    public void onEditClick(DocumentSnapshot documentSnapshot) {

        blogRef = db.collection("blogs")
                .document(documentSnapshot.getId());

        BlogsResponse model = documentSnapshot.toObject(BlogsResponse.class);

        bindings = FragmentsWriteArticlesBinding.inflate(getLayoutInflater());

        bindings.spinnerAreas.setVisibility(View.GONE);

        bindings.imageButton.setOnClickListener(view -> imageVideo());

        bindings.addArticles.setText("Update");
        bindings.textView25.setText("Update Article");
        bindings.etTitle.setText(model.getTitle());
        bindings.etArticles.setText(model.getContent());

        if (model.getImage() != null) {
            MyUtils.requestOptions(bindings.imageButton, model.getImage());
        }

        bindings.addArticles.setOnClickListener(view -> {

            title = String.valueOf(bindings.etTitle.getText());
            content = String.valueOf(bindings.etArticles.getText());


            if (TextUtils.isEmpty(title)) {
                bindings.etTitleParent.setError("Enter a title");
                bindings.etTitle.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(content)) {
                bindings.etArticlesParent.setError("Enter a content");
                bindings.etTitle.requestFocus();
                return;
            }

//        if (cat_id.isEmpty()) {
//            Toast.makeText(context, "Select Category", Toast.LENGTH_SHORT).show();
//            return;
//        }

            loading_dialog.show();

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


        });


        bottomSheetDialog.setContentView(bindings.getRoot());
        bottomSheetDialog.show();
        bottomSheetDialog.getBehavior().setPeekHeight(5000);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setCancelable(false);
    }

    @Override
    public void onDeleteClick(DocumentSnapshot documentSnapshot) {
        DocumentReference reference = documentSnapshot.getReference();

        BlogsResponse model = documentSnapshot.toObject(BlogsResponse.class);

        reference.delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "onSuccess: item delete");
                    setUpRecyclerView();
                    adapter.startListening();
                })
                .addOnFailureListener(e -> Log.d(TAG, "onItemClick: " + e.getLocalizedMessage()));

        Snackbar.make(binding.getRoot(), "Item Deleted", Snackbar.LENGTH_LONG)
                .setAction("undo", v -> {
                    assert model != null;
                    reference.set(model);
                    setUpRecyclerView();
                    adapter.startListening();
                })
                .show();
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_SELECT_CODE || requestCode == GALLERY)
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    Toast.makeText(context, "Unable to handle image.", Toast.LENGTH_SHORT).show();
                    image_uri = null;
                    bindings.imageButton.setImageResource(R.drawable.image_selection_placeholder);
                    return;
                }
                image_uri = data.getData();
                Glide.with(this).load(image_uri).into(bindings.imageButton);
            } else {
                image_uri = null;
                bindings.imageButton.setImageResource(R.drawable.image_selection_placeholder);
            }
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_SELECT_CODE);
    }

    private void imageVideo() {
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(context);
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

    private void setdata(String links) {

        String random_id = String.valueOf(UUID.randomUUID());

        Map<String, Object> blog = new HashMap<>();
//        blog.put("id", random_id);
        blog.put("title", title);
        blog.put("content", content);
//        blog.put("vote", 0);
//        blog.put("cat_id", cat_id);
        if (links != null)
            blog.put("image", links);
//        blog.put("created_at", new Date());
        blog.put("updated_at", new Date());
//        blog.put("create_by", user);
        blog.put("username", SpHelper.getValue(context, SpHelper.USERNAME));
        blog.put("updated_by", SpHelper.getValue(context,SpHelper.ID));

        Log.d(TAG, "Title: " + title + "\n" + "Content: " + content + "links" + links);

        blogRef.update(blog)
                .addOnSuccessListener(documentReference -> {
                    loading_dialog.dismiss();
                    bottomSheetDialog.dismiss();
//                    Log.d(TAG, "onSuccess: get id " + documentReference);
                    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                    setUpRecyclerView();
                    adapter.startListening();
//                    Navigation.findNavController(binding.getRoot()).popBackStack();
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
}
