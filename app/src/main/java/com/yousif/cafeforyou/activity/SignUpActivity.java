package com.yousif.cafeforyou.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;

import yousif.cafeforyou.R;
import yousif.cafeforyou.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();


        binding.MoveOnSignIn.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            finish();
        });


        binding.RegisterNow.setOnClickListener(view -> {

            if (binding.Name.getText().toString().isEmpty()) {
                binding.Name.setError("Detail in Empty*");
            }
            else if (binding.phoneNum.getText().toString().isEmpty()) {
                binding.Email.setError("Detail in Empty*");
            }
            else if (binding.Email.getText().toString().isEmpty()) {
                binding.Email.setError("Detail in Empty*");
            } else if (binding.Password.getText().toString().isEmpty()) {
                binding.Password.setError("Detail in Empty*");
            } else {
                SendRequestForRegistration();
            }


        });


    }

    private void SendRequestForRegistration() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Creating Account....");
        progressDialog.show();


        RelativeLayout LayoutShow;
        LayoutShow = findViewById(R.id.LayoutShow);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User").child("UserDetail");

        DatabaseReference Allreference = FirebaseDatabase.getInstance().getReference().child("AllUsers");

        firebaseAuth.createUserWithEmailAndPassword(binding.Email.getText().toString(), binding.Password.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String Private = reference.push().getKey();


                HashMap<String, Object> map = new HashMap<>();
                map.put("UserName", binding.Name.getText().toString());
                map.put("Email", binding.Email.getText().toString());
                map.put("Password", binding.Password.getText().toString());
                map.put("PhoneNumber", binding.phoneNum.getText().toString());
                map.put("Balance","0");
                map.put("Private", Private);
                map.put("UID", UID);

                Allreference.child(Private).setValue(map);
                reference.child(UID).setValue(map).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(this, "Registration Request Send", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                        finish();
                        progressDialog.dismiss();
                    } else {
                        Snackbar.make(LayoutShow, "Check Your Connection", Snackbar.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }

                });

            } else {
                Snackbar.make(LayoutShow, "Registration Request Sending Fail", Snackbar.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });


    }
}