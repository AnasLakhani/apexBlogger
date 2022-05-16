package com.coderlytics.apexblogger.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.coderlytics.apexblogger.R;
import com.coderlytics.apexblogger.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

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

        binding.navView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(m -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this,LoginActivity.class));
            finish();
            return true;
        });

    }
}