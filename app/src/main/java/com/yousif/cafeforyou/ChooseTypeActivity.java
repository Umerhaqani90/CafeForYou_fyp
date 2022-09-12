package com.yousif.cafeforyou;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.yousif.cafeforyou.activity.SignInActivity;

import yousif.cafeforyou.databinding.ActivityChooseTypeBinding;


public class ChooseTypeActivity extends AppCompatActivity {

    ActivityChooseTypeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.openAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseTypeActivity.this, AdminLogin.class);
                intent.putExtra("Type", "ADMIN");
                startActivity(intent);
            }
        });


        binding.openUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseTypeActivity.this, SignInActivity.class);
                intent.putExtra("Type", "USER");
                startActivity(intent);
               // finish();
            }
        });


    }
}