package com.coderlytics.apexblogger.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.coderlytics.apexblogger.databinding.HireBinding;

public class HireActivity extends AppCompatActivity {

    HireBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HireBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void onGotIt(View view) {
        onBackPressed();
    }
}