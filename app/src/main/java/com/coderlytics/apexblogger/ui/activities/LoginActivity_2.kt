package com.coderlytics.apexblogger.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.coderlytics.apexblogger.databinding.ActivityLoginBinding

class LoginActivity_2 : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater);
        setContentView(binding.root);


    }

}