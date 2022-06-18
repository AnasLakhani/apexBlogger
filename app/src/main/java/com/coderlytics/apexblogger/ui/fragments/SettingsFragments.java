package com.coderlytics.apexblogger.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.coderlytics.apexblogger.databinding.FragmentsHomeBinding;
import com.coderlytics.apexblogger.databinding.FragmentsSettingsBinding;
import com.coderlytics.apexblogger.ui.activities.AccountActivity;

public class SettingsFragments extends Fragment {

    FragmentsSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentsSettingsBinding.inflate(inflater, container, false);

        binding.accountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), AccountActivity.class));
            }
        });



        return binding.getRoot();

    }
}
