package com.yousif.cafeforyou.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yousif.cafeforyou.CatAdapter;
import com.yousif.cafeforyou.CategoryListModel;

import java.util.ArrayList;

import yousif.cafeforyou.databinding.ActivityViewItemsBinding;

public class ViewItems extends AppCompatActivity {
    ActivityViewItemsBinding binding;
    ArrayList<CategoryListModel> arrayList;
    DatabaseReference referenceCatList;
    String purpose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        purpose =intent.getStringExtra("Purpose");

        arrayList = new ArrayList<>();
        referenceCatList = FirebaseDatabase.getInstance().getReference("CafeForYou").child("Products");
        getFBData();
    }

    private void getFBData() {
        referenceCatList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CategoryListModel categoryListModel = dataSnapshot.getValue(CategoryListModel.class);
                    arrayList.add(categoryListModel);
                }
                setCatAdapter(arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setCatAdapter(ArrayList<CategoryListModel> arrayList) {
        Toast.makeText(this, ""+arrayList.size(), Toast.LENGTH_SHORT).show();
        binding.itemRV.setAdapter(new CatAdapter(arrayList, getApplicationContext(), referenceCatList,purpose));
    }
}