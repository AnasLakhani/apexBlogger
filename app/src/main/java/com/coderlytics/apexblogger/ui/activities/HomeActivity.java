package com.coderlytics.apexblogger.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.coderlytics.apexblogger.R;
import com.coderlytics.apexblogger.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    NavController navController;

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.aaa.bottom, navController);

        binding.aaa.bottom.getMenu().findItem(R.id.menu).setOnMenuItemClickListener(menuItem -> {
            binding.drawerLayout.open();
            return true;
        });

    }
}