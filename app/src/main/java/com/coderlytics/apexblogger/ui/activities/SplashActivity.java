package com.coderlytics.apexblogger.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.coderlytics.apexblogger.BuildConfig;
import com.coderlytics.apexblogger.R;
import com.coderlytics.apexblogger.databinding.ActivitySplashBinding;
import com.coderlytics.apexblogger.model.SettingsModel;
import com.coderlytics.apexblogger.utils.MyUtils;
import com.coderlytics.apexblogger.utils.SpHelper;

import java.text.MessageFormat;

public class SplashActivity extends AppCompatActivity {

    SpHelper spHelper;

    boolean isFirstTime = false;

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        spHelper = new SpHelper(this);


        binding.next.setOnClickListener(view -> getBaseUrl());

        binding.next.performClick();
    }

    private void getBaseUrl() {
        if (isFirstTime) {
            spHelper.setFirstTimeLaunch(false);
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
            return;
        }
        binding.txt.setMovementMethod(LinkMovementMethod.getInstance());

        binding.next.setVisibility(View.GONE);
        binding.txt.setVisibility(View.GONE);
        MyUtils.getFirebase().get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                Log.d("Firebase", "onSuccess: LIST EMPTY");
                return;
            }
            SettingsModel types = documentSnapshot.toObject(SettingsModel.class);
            if (types != null) {

                binding.loitte.setVisibility(View.GONE);

//                if (types.isMaintance_mode()) {
                if (types.isMaintainance_mode_yasir()) {
                    binding.next.setVisibility(View.VISIBLE);
                    binding.txt.setVisibility(View.VISIBLE);
                    binding.txt.setText(types.getMaintenance_message());
                    return;
                }

                int versionCode = (BuildConfig.VERSION_CODE);
                String versionName = BuildConfig.VERSION_NAME;

                if (versionCode < Integer.parseInt(types.getVersion_code())) {
                    binding.next.setVisibility(View.VISIBLE);
                    binding.txt.setVisibility(View.VISIBLE);
                    binding.txt.setText(MessageFormat.format("{0}\nUpdated Version is {1}\nYour Version is {2}", types.getUpdate_msg(), types.getVersion_name(), versionName));
                    return;
                }


                String PRIVACY_POLICY_URL = types.getPrivacy_policy_url();
                String TERM_AND_CONDITION_URL = types.getTerm_and_condition_url();


                //PRIVACY POLICY URL
                SpHelper.setKey(SplashActivity.this, SpHelper.PRIVACY_POLICY_URL, PRIVACY_POLICY_URL);

                //TERM AND CONDITION URL
                SpHelper.setKey(SplashActivity.this, SpHelper.TERM_AND_CONDITION_URL, TERM_AND_CONDITION_URL);


                //Not Now
                if (spHelper.isFirstTimeLaunch() && types.isFirst_time_enabled()) {
                    isFirstTime = true;
                    binding.next.setText(getString(R.string.next));
                    binding.next.setVisibility(View.VISIBLE);
                    binding.txt.setVisibility(View.VISIBLE);
                    binding.txt.setText(types.getFirst_time_start_app_message());
                    return;
                }

                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }

        }).addOnFailureListener(e -> {
            binding.next.setText(getString(R.string.retry));
            binding.next.setVisibility(View.VISIBLE);
            binding.txt.setVisibility(View.VISIBLE);
            binding.txt.setText(e.getLocalizedMessage());
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        });
    }
}