package com.yousif.cafeforyou.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yousif.cafeforyou.SignIn;

import yousif.cafeforyou.databinding.ActivityUserMainMenuBinding;

public class UserMainMenu extends AppCompatActivity {
    ActivityUserMainMenuBinding binding;
   // Button button;

    Object prevBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        button=(Button)findViewById(R.id.btnLogout);



        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainMenu.this, SignIn.class);
                intent.putExtra("Purpose", "Logout Successfully");
                //startActivity(intent);
                finish();
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainMenu.this, SignIn.class);
                Toast.makeText(UserMainMenu.this, "Log out Successfully", Toast.LENGTH_SHORT).show();
               // startActivity(intent);
                finish();
            }
        });

        binding.selectitemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainMenu.this, ViewItems.class);
                intent.putExtra("Purpose", "PurchaseItem");
                startActivity(new Intent(intent));

            }
        });


        binding.viewbalanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserMainMenu.this, UserBalanceActivity.class));
            }
        });


    }

}