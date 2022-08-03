package com.coderlytics.apexblogger.ui.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.coderlytics.apexblogger.databinding.FragmentsSettingsBinding;
import com.coderlytics.apexblogger.ui.activities.AccountActivity;
import com.coderlytics.apexblogger.utils.SpHelper;

public class SettingsFragments extends Fragment {

    FragmentsSettingsBinding binding;

    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentsSettingsBinding.inflate(inflater, container, false);

        context = binding.getRoot().getContext();

        binding.accountText.setOnClickListener(view -> startActivity(new Intent(context, AccountActivity.class)));

        binding.aboutText.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(SpHelper.getValue(context, SpHelper.ABOUT_US_URL)));
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // can't start activity
                e.printStackTrace();
            }
        });

        binding.privacyText.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(SpHelper.getValue(context, SpHelper.PRIVACY_POLICY_URL)));
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // can't start activity
                e.printStackTrace();
            }
        });


        return binding.getRoot();

    }
}
