package com.coderlytics.apexblogger.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.coderlytics.apexblogger.databinding.FragmentsHomeBinding;
import com.coderlytics.apexblogger.databinding.FragmentsSearchBinding;

public class SearchFragments extends Fragment {

    FragmentsSearchBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentsSearchBinding.inflate(inflater, container, false);
//        binding.textView25.setText("FragmentsSearchBinding");
        return binding.getRoot();
    }
}
