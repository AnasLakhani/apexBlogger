package com.coderlytics.apexblogger.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.coderlytics.apexblogger.databinding.FragmentsCategoriesBinding;
import com.coderlytics.apexblogger.databinding.FragmentsHomeBinding;

public class CategoriesFragments extends Fragment {

    FragmentsCategoriesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentsCategoriesBinding.inflate(inflater, container, false);
        binding.txt.setText("FragmentsBookmarksBinding");
        return binding.getRoot();
    }
}
