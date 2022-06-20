package com.coderlytics.apexblogger.ui.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.coderlytics.apexblogger.R;
import com.coderlytics.apexblogger.databinding.ActivityHomeBinding;
import com.coderlytics.apexblogger.utils.KeyboardUtils;
import com.coderlytics.apexblogger.utils.MyUtils;
import com.coderlytics.apexblogger.utils.SpHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.text.MessageFormat;

public class HomeActivity extends AppCompatActivity {

    NavController navController;

    ActivityHomeBinding binding;

    public final BroadcastReceiver mMasage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("aa") != null) {
                MyUtils.requestOptions(imageView,SpHelper.getValue(HomeActivity.this,SpHelper.IMAGEURL));
            }
        }
    };

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMasage, new IntentFilter("mymsg"));

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

        TextView textView = binding.navView.getHeaderView(0).findViewById(R.id.tvTitle);
        imageView = binding.navView.getHeaderView(0).findViewById(R.id.imgLogo);
        MyUtils.requestOptions(imageView,SpHelper.getValue(this,SpHelper.IMAGEURL));
        textView.setText(SpHelper.getValue(this,SpHelper.USERNAME));

        KeyboardUtils.addKeyboardToggleListener(this, isVisible -> binding.aaa.bottom.setVisibility(isVisible ? GONE : VISIBLE));



    }
}