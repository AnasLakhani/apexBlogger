package com.coderlytics.apexblogger.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.coderlytics.apexblogger.databinding.CustomSpinnerItemsBinding;
import com.coderlytics.apexblogger.model.BlogCategoriesResponse;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.Objects;

public class SpinnerCategoryAdapter extends BaseAdapter {

    private final List<BlogCategoriesResponse> carts;

    public SpinnerCategoryAdapter(List<BlogCategoriesResponse> carts) {
        this.carts = carts;
    }

    @Override
    public int getCount() {
        return carts.size();
    }

    @Override
    public Object getItem(int i) {
        return carts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (viewGroup.getContext() != null) {
            CustomSpinnerItemsBinding binding = CustomSpinnerItemsBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
            binding.setModel(carts.get(i));
            return binding.getRoot();
        }
        return null;
    }
}