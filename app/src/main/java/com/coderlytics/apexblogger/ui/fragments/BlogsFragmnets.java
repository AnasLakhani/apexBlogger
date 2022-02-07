package com.coderlytics.apexblogger.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.coderlytics.apexblogger.databinding.FragmentsBlogsBinding;

public class BlogsFragmnets extends Fragment {

    FragmentsBlogsBinding binding;

    String s;

    public BlogsFragmnets(String s) {
        this.s = s;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentsBlogsBinding.inflate(inflater, container, false);
//        binding.txt.setText(s);
        return binding.getRoot();
    }
}
