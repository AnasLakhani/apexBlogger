package com.coderlytics.apexblogger.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.coderlytics.apexblogger.adapters.CategoriesAdapter;
import com.coderlytics.apexblogger.databinding.FragmentsCategoriesBinding;
import com.coderlytics.apexblogger.databinding.FragmentsHomeBinding;
import com.coderlytics.apexblogger.interfaces.OnItemClickListener;
import com.coderlytics.apexblogger.model.BlogCategoriesResponse;
import com.coderlytics.apexblogger.ui.activities.AddCategory;
import com.coderlytics.apexblogger.utils.MyUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class CategoriesFragments extends Fragment implements OnItemClickListener {

    FragmentsCategoriesBinding binding;

    Context context;

    private CategoriesAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference blogsRef = db.collection("categories");

    private static final String TAG = "AdminBlogs";

    private AlertDialog loading_dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentsCategoriesBinding.inflate(inflater, container, false);
        context = binding.getRoot().getContext();
        Activity activity = (Activity) context;
        loading_dialog = MyUtils.getLoadingDialog(activity);

        boolean isUsman = FirebaseAuth.getInstance().getCurrentUser().getUid().equals("xz8RxBB9dih8WfhAsmcg5gFX5pO2");

        if (!isUsman) {
           // binding.button.setVisibility(View.GONE);
        }

        binding.button.setOnClickListener(view -> startActivity(new Intent(context, AddCategory.class)));

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.startListening();
            binding.swipeRefreshLayout.setRefreshing(false);
        });

        setUpRecyclerView();

        return binding.getRoot();
    }

    private void setUpRecyclerView() {
        Query query = blogsRef.orderBy("created_at", Query.Direction.ASCENDING);

        adapter = new CategoriesAdapter(query, this) {

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
        //Toast.makeText(context, documentSnapshot, Toast.LENGTH_SHORT).show();
        BlogCategoriesResponse model = documentSnapshot.toObject(BlogCategoriesResponse.class);


    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, View view) {

    }
}
