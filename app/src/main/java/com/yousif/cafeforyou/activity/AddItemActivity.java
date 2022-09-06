package com.yousif.cafeforyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yousif.cafeforyou.CatAdapter;
import com.yousif.cafeforyou.OpenForm;
import com.yousif.cafeforyou.model.CartModel;

import java.util.ArrayList;

import yousif.cafeforyou.R;

public class AddItemActivity extends AppCompatActivity {
    String CATEGORY;
    RecyclerView recyclerView;
    ArrayList<CartModel> arrayList;
    DatabaseReference referenceCatList;
    ProgressBar simpleProgressBar;
    FloatingActionButton add_newCat;
    private String purpose="AddItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        referenceCatList = FirebaseDatabase.getInstance().getReference("CafeForYou").child("Products");
        recyclerView = findViewById(R.id.recyclerView);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        add_newCat = findViewById(R.id.add_newCat);
        arrayList = new ArrayList<>();

        simpleProgressBar.setVisibility(View.VISIBLE);
        getFBData();
        addCategory_inList();
    }

    private void addCategory_inList() {
        add_newCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OpenForm.class);
                startActivity(intent);
            }
        });
    }

    private void getFBData() {
        referenceCatList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                simpleProgressBar.setVisibility(View.GONE);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartModel cartModel = dataSnapshot.getValue(CartModel.class);
                    arrayList.add(cartModel);
                }
                setCatAdapter(arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setCatAdapter(ArrayList<CartModel> arrayList) {
        recyclerView.setAdapter(new CatAdapter(arrayList, getApplicationContext(), referenceCatList, purpose));
    }
}