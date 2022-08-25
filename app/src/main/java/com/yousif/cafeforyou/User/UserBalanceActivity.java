package com.yousif.cafeforyou.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import yousif.cafeforyou.databinding.ActivityUserBalanceBinding;

public class UserBalanceActivity extends AppCompatActivity {
    DatabaseReference currentUserRef;
    Object prevBalance;
    ActivityUserBalanceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBalanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currentUserRef = FirebaseDatabase.getInstance().getReference().child("User").child("UserDetail");
        getCurrentUserBalance();
    }


    private void getCurrentUserBalance() {
        currentUserRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();

                    try {
                        prevBalance = map.get("Balance");
                        binding.balance.setText("" + prevBalance);

                    } catch (Exception e) {

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

}