package com.coderlytics.apexblogger.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.coderlytics.apexblogger.databinding.CustomTab2Binding;
import com.coderlytics.apexblogger.model.CategoriesResponse;
import com.coderlytics.apexblogger.ui.fragments.BlogsFragmnets;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryTopAdapter extends FragmentPagerAdapter {

    final List<CategoriesResponse> subCategoriesResponses;
    final Context context;
    boolean isPending;

    public CategoryTopAdapter(@NonNull FragmentManager fm, List<CategoriesResponse> subCategoriesResponses, Context context, boolean isPending) {
        super(fm);
        this.subCategoriesResponses = subCategoriesResponses;
        this.context = context;
        this.isPending = isPending;
    }

    @Override
    public int getCount() {
        return subCategoriesResponses.size();
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new BlogsFragmnets("Newest");
                break;
            case 1:
                fragment = new BlogsFragmnets("Popular");
                break;
            case 2:
                fragment = new BlogsFragmnets("Trending");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return subCategoriesResponses.get(position).getCategoryName();
    }

    public View getTabView(int position) {
        CustomTab2Binding binding = CustomTab2Binding.inflate(LayoutInflater.from(context));
        binding.text1.setText(subCategoriesResponses.get(position).getCategoryName());
        return binding.getRoot();
    }

}
