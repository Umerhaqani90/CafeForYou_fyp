package com.yousif.cafeforyou;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.yousif.cafeforyou.activity.SignInActivity;

import yousif.cafeforyou.databinding.ActivityForgetPasswordBinding;


public class ForgetPassword extends AppCompatActivity {

    ActivityForgetPasswordBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        Objects.requireNonNull(getSupportActionBar()).hide();
        auth = FirebaseAuth.getInstance();
        binding.ForgetNow.setOnClickListener(view -> {

            String email = binding.Email.getText().toString().trim();

            auth.sendPasswordResetEmail(email)

                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(ForgetPassword.this, SignInActivity.class));
                                finish();
                                Toast.makeText(ForgetPassword.this, "Waiting For Mail", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgetPassword.this, "Failed to send Reset Password!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        });


        binding.MoveOnBack.setOnClickListener(view ->
                this.onBackPressed());

    }
}