package com.yousif.cafeforyou;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import yousif.cafeforyou.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    private AdminProfileModel adminProfileModel;
    DatabaseReference reference;
    String key;
    ArrayList<AdminProfileModel> arrayList;
    private DatabaseReference reference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        arrayList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("CafeForYou").child("AdminProfile").child("1");
//        String id = reference.push().getKey();
        binding.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<>();
                map.put("adminName", binding.adminName.getText().toString());
                map.put("adminLoc", binding.location.getText().toString());
                map.put("adminPhone", binding.Number.getText().toString());
//                map.put("PrivateKey", id);
                reference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ProfileActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        getAdminDetail();
    }

    private void getAdminDetail() {
        reference1 = FirebaseDatabase.getInstance().getReference("CafeForYou").child("AdminProfile").child("1");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    String adminName = (String) map.get("adminName");
                    binding.adminName.setText("" + adminName);
                    String adminLoc = (String) map.get("adminLoc");
                    binding.location.setText("" + adminLoc);
                    String adminPhone = (String) map.get("adminPhone");
                    binding.Number.setText("" + adminPhone);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}