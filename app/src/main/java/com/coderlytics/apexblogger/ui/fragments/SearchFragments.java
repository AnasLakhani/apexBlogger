package com.coderlytics.apexblogger.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.coderlytics.apexblogger.adapters.BlogAdapter;
import com.coderlytics.apexblogger.databinding.FragmentsSearchBinding;
import com.coderlytics.apexblogger.model.BlogsResponse;
import com.coderlytics.apexblogger.ui.activities.HireActivity;
import com.coderlytics.apexblogger.utils.MyUtils;
import com.coderlytics.apexblogger.utils.SpHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class SearchFragments extends Fragment implements BlogAdapter.OnItemClickListener {

    FragmentsSearchBinding binding;

    Context context;

    private BlogAdapter adapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference blogsRef = db.collection("blogs");

    private static final String TAG = "AdminBlogs";

    private AlertDialog loading_dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentsSearchBinding.inflate(inflater, container, false);

        binding.materialAutoCompleteTextView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.equals("")) {
                    return false;
                }
                MyUtils.hideKeyboard(requireActivity());
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String searchView) {

//                if (searchView.length() > 3) {
//                    fetchdata(searchView);
//                    return true;
//                }

                return false;
            }
        });

        context = binding.getRoot().getContext();
        Activity activity = (Activity) context;
        loading_dialog = MyUtils.getLoadingDialog(activity);

        binding.blogs.swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.startListening();
            binding.blogs.swipeRefreshLayout.setRefreshing(false);
        });


        return binding.getRoot();
    }

    private void search(String query) {
        setUpRecyclerView(query);
        adapter.startListening();
    }

    @Override
    public void onHireClick(DocumentSnapshot documentSnapshot) {
        startActivity(new Intent(requireActivity(), HireActivity.class));
    }

    private void setUpRecyclerView(String q) {

        Query query = blogsRef.orderBy("created_at", Query.Direction.DESCENDING).whereEqualTo("title", q);

        adapter = new BlogAdapter(query, SearchFragments.this,false) {

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
    public void onStop() {
        super.onStop();
        if (adapter != null)
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
