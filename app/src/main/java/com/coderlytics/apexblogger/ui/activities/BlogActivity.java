package com.coderlytics.apexblogger.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.coderlytics.apexblogger.adapters.BlogAdapter;
import com.coderlytics.apexblogger.databinding.FragmentsBlogsBinding;
import com.coderlytics.apexblogger.model.BlogsResponse;
import com.coderlytics.apexblogger.utils.MyUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class BlogActivity extends AppCompatActivity implements BlogAdapter.OnItemClickListener {

    FragmentsBlogsBinding binding;

    String id;
    String title;

    Context context;

    private BlogAdapter adapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference blogsRef = db.collection("blogs");

    private static final String TAG = "AdminBlogs";

    private AlertDialog loading_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentsBlogsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");

        context = binding.getRoot().getContext();
        Activity activity = (Activity) context;
        loading_dialog = MyUtils.getLoadingDialog(activity);

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.startListening();
            binding.swipeRefreshLayout.setRefreshing(false);
        });

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {

        Query query = blogsRef.orderBy("created_at", Query.Direction.DESCENDING).whereEqualTo("cat_id", id);

        adapter = new BlogAdapter(query, BlogActivity.this,false) {

            @Override
            protected void onDataChanged() {
                Log.d(TAG, "onDataChanged: ");

                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.noData.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.noData.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {

                Snackbar.make(binding.getRoot(),
                        "Error: check logs for info." + e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        };


        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setAdapter(adapter);

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
        MyUtils.share(binding.getRoot().getContext(),model.getTitle(),model.getContent());

    }

    @Override
    public void onEditClick(DocumentSnapshot documentSnapshot) {
        Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(DocumentSnapshot documentSnapshot) {
        Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot) {
        Toast.makeText(context, "Item Click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHireClick(DocumentSnapshot documentSnapshot) {
        startActivity(new Intent(this,HireActivity.class));
    }
}