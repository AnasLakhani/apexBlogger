package com.coderlytics.apexblogger.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.coderlytics.apexblogger.adapters.CategoryTopAdapter;
import com.coderlytics.apexblogger.databinding.FragmentsHomeBinding;
import com.coderlytics.apexblogger.model.CategoriesResponse;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomeFragments extends Fragment {

    FragmentsHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentsHomeBinding.inflate(inflater, container, false);


        ArrayList<CategoriesResponse> responses = new ArrayList<>();

        responses.add(new CategoriesResponse("0","Blog Feed"));
//        responses.add(new CategoriesResponse("1","Popular"));
//        responses.add(new CategoriesResponse("2","Trending"));

        CategoryTopAdapter pagerAdapter =
                new CategoryTopAdapter(getChildFragmentManager(), responses, requireContext(),true);
        binding.viewpager.setAdapter(pagerAdapter);
        binding.viewpager.setOffscreenPageLimit(responses.size());

        binding.tabLayout.setupWithViewPager(binding.viewpager);

//        binding.tabLayout.setVisibility(View.GONE);
        // Iterate over all tabs and set the custom view
        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = binding.tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(pagerAdapter.getTabView(i));
        }


        return binding.getRoot();
    }
}
