package com.coderlytics.apexblogger.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coderlytics.apexblogger.databinding.LayoutCateogriesBinding;
import com.coderlytics.apexblogger.interfaces.OnItemClickListener;
import com.coderlytics.apexblogger.model.BlogCategoriesResponse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;


public class CategoriesAdapter extends FirestoreAdapter<CategoriesAdapter.MyRecyclerViewHolder> {

    private OnItemClickListener listener;

    protected CategoriesAdapter(Query query, OnItemClickListener listener) {
        super(query);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoriesAdapter.MyRecyclerViewHolder(LayoutCateogriesBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        holder.bind(getSnapshot(position), listener);

    }

    static class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        LayoutCateogriesBinding binding;

        MyRecyclerViewHolder(@NonNull LayoutCateogriesBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }

        void bind(final DocumentSnapshot snapshot,
                  final OnItemClickListener listener) {

            BlogCategoriesResponse model = snapshot.toObject(BlogCategoriesResponse.class);
            binding.setModel(model);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null) {
                        listener.onReadClick(snapshot);
                    }
                }
            });

        }
    }


}