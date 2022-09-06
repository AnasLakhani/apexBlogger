package com.coderlytics.apexblogger.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.coderlytics.apexblogger.adapters.BlogAdapter;
import com.coderlytics.apexblogger.adapters.CategoriesAdapter;
import com.coderlytics.apexblogger.databinding.FragmentsBlogsBinding;
import com.coderlytics.apexblogger.model.BlogsResponse;
import com.coderlytics.apexblogger.ui.activities.HireActivity;
import com.coderlytics.apexblogger.utils.MyUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class BlogsFragments extends Fragment implements BlogAdapter.OnItemClickListener {

    FragmentsBlogsBinding binding;

    Context context;

    private BlogAdapter adapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference blogsRef = db.collection("blogs");

    private static final String TAG = "AdminBlogs";

    private AlertDialog loading_dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentsBlogsBinding.inflate(inflater, container, false);
        context = binding.getRoot().getContext();
        Activity activity = (Activity) context;
        loading_dialog = MyUtils.getLoadingDialog(activity);

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            setUpRecyclerView();
            adapter.startListening();
            binding.swipeRefreshLayout.setRefreshing(false);
        });

        setUpRecyclerView();

        return binding.getRoot();
    }
    @Override
    public void onHireClick(DocumentSnapshot documentSnapshot) {
        startActivity(new Intent(requireActivity(), HireActivity.class));
    }
    private void setUpRecyclerView() {

        Query query = blogsRef.orderBy("updated_at", Query.Direction.DESCENDING);

        adapter = new BlogAdapter(query, BlogsFragments.this,false) {

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
        Log.d(TAG, "onStart: ");
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

    }

    @Override
    public void onDeleteClick(DocumentSnapshot documentSnapshot) {

    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot) {

    }
}
