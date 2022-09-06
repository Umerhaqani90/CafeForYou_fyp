package com.yousif.cafeforyou.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yousif.cafeforyou.AdminLogin;
import com.yousif.cafeforyou.CatAdapter;
import com.yousif.cafeforyou.CategoryListModel;
import com.yousif.cafeforyou.MainActivity;
import com.yousif.cafeforyou.MainItemActivity;
import com.yousif.cafeforyou.MainItemActivity_ViewBinding;
import com.yousif.cafeforyou.SignIn;

import java.util.ArrayList;

import yousif.cafeforyou.R;
import yousif.cafeforyou.databinding.ActivityViewItemsBinding;

public class ViewItems extends AppCompatActivity {
   // ActivityViewItemsBinding binding;
    ArrayList<CategoryListModel> arrayList;
    DatabaseReference referenceCatList;
    RecyclerView itemRV;
    String purpose;

     Button btnAddToCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
       // binding = ActivityViewItemsBinding.inflate(getLayoutInflater());

        btnAddToCart=findViewById(R.id.btnAddToCart);
        itemRV=findViewById(R.id.itemRV);

        Intent intent=getIntent();
        purpose =intent.getStringExtra("Purpose");

        arrayList = new ArrayList<>();
        referenceCatList = FirebaseDatabase.getInstance().getReference("CafeForYou").child("Products");
        getFBData();

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewItems.this, MainItemActivity.class);
                //startActivity(intent);
                finish();
            }
        });
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
        itemRV.setAdapter(new CatAdapter(arrayList, getApplicationContext(), referenceCatList,purpose));
    }
}