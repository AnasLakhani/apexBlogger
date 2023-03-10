package com.coderlytics.apexblogger.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.coderlytics.apexblogger.adapters.CategoriesAdapter;
import com.coderlytics.apexblogger.databinding.FragmentsCategoriesBinding;
import com.coderlytics.apexblogger.model.BlogCategoriesResponse;
import com.coderlytics.apexblogger.model.BlogsResponse;
import com.coderlytics.apexblogger.model.CategoriesResponse;
import com.coderlytics.apexblogger.ui.activities.AddCategoryActivity;
import com.coderlytics.apexblogger.ui.activities.BlogActivity;
import com.coderlytics.apexblogger.utils.MyUtils;
import com.coderlytics.apexblogger.utils.SpHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class CategoriesFragments extends Fragment implements CategoriesAdapter.OnItemClickListener {

    FragmentsCategoriesBinding binding;

    Context context;

    private CategoriesAdapter adapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference blogsRef = db.collection("categories");

    private static final String TAG = "AdminBlogs";

    private AlertDialog loading_dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentsCategoriesBinding.inflate(inflater, container, false);
        context = binding.getRoot().getContext();
        Activity activity = (Activity) context;
        loading_dialog = MyUtils.getLoadingDialog(activity);

        boolean isUsman = SpHelper.getValue(context, SpHelper.ROLE).equals("admin");

        if (!isUsman) {
            binding.button.setVisibility(View.GONE);
        }

        binding.button.setOnClickListener(view -> startActivity(new Intent(context, AddCategoryActivity.class)));

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.startListening();
            binding.swipeRefreshLayout.setRefreshing(false);
        });

        setUpRecyclerView();

        return binding.getRoot();
    }

    private void setUpRecyclerView() {

        Query query = blogsRef.orderBy("created_at", Query.Direction.DESCENDING);

        adapter = new CategoriesAdapter(query, CategoriesFragments.this) {

            @Override
            protected void onDataChanged() {

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
    public void onReadClick(DocumentSnapshot documentSnapshot) {

        BlogCategoriesResponse model = documentSnapshot.toObject(BlogCategoriesResponse.class);

        Intent intent = new Intent(context, BlogActivity.class);
        intent.putExtra("id",model.getId());
        intent.putExtra("title",model.getTitle());
        startActivity(intent);

    }
}
