package com.yousif.cafeforyou.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yousif.cafeforyou.ForgetPassword;
import com.yousif.cafeforyou.User.UserMainMenu;

import yousif.cafeforyou.databinding.ActivitySignInBinding;


public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String type = intent.getStringExtra("Type");


        binding.MoveOnSignUp.setOnClickListener(view -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            finish();

        });

        binding.LoginNow.setOnClickListener(view -> {
            if (binding.Email.getText().toString().isEmpty()) {
                binding.Email.setError("Detail in Empty*");
            } else if (binding.Password.getText().toString().isEmpty()) {
                binding.Password.setError("Detail in Empty*");
            } else {
                CheckAccountIsActive();
            }
        });

        binding.ForgetPassword.setOnClickListener(view -> {
            startActivity(new Intent(SignInActivity.this, ForgetPassword.class));

        });
    }

    private void CheckAccountIsActive() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Login....");
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User").child("UserDetail");

        reference.orderByChild("Email").equalTo(binding.Email.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        String Email = binding.Email.getText().toString();
                        String Pass = binding.Password.getText().toString();

                        firebaseAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(SignInActivity.this, UserMainMenu.class));
                                finish();
                            }
                        });
                    }
                    progressDialog.dismiss();

                } else {
                    progressDialog.dismiss();
                    Snackbar.make(binding.LoginPage, "First Register Your Self", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(SignInActivity.this, UserMainMenu.class));
            finish();
        }
    }
}