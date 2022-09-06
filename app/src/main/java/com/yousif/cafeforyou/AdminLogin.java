package com.yousif.cafeforyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yousif.cafeforyou.activity.MainActivity;

import yousif.cafeforyou.databinding.ActivityAdminLoginBinding;

public class AdminLogin extends AppCompatActivity {
    ActivityAdminLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.adminLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.Email.getText().toString().equals("admin@gmail.com") && binding.Password.getText().toString().equals("121212")) {
                    startActivity(new Intent(AdminLogin.this, MainActivity.class));
                    finish();
                }
            }
        });


    }
}