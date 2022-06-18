package com.coderlytics.apexblogger.ui.fragments;

import android.app.Activity;
import android.content.Context;
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
import com.coderlytics.apexblogger.databinding.FragmentsHomeBinding;
import com.coderlytics.apexblogger.databinding.FragmentsProfileBinding;
import com.coderlytics.apexblogger.ui.activities.BlogActivity;
import com.coderlytics.apexblogger.utils.MyUtils;
import com.coderlytics.apexblogger.utils.SpHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class ProfileFragments extends Fragment implements BlogAdapter.OnItemClickListener{

    FragmentsProfileBinding binding;

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
        MyUtils.requestOptions(binding.profileImg, SpHelper.getValue(context,SpHelper.IMAGEURL));
        binding.nameText.setText(SpHelper.getValue(context,SpHelper.USERNAME));

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

        Query query = blogsRef.orderBy("created_at", Query.Direction.DESCENDING).whereEqualTo("updated_by", SpHelper.getValue(context,SpHelper.ID));

        adapter = new BlogAdapter(query, ProfileFragments.this) {

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
    public void onReadClick(DocumentSnapshot documentSnapshot) {

    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, View view) {

    }
}
