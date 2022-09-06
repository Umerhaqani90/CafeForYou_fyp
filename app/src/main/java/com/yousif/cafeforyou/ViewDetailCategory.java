package com.yousif.cafeforyou;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yousif.cafeforyou.activity.AddItemActivity;
import com.yousif.cafeforyou.model.CartModel;


import yousif.cafeforyou.databinding.ActivityViewDetailCategoryBinding;

public class ViewDetailCategory extends AppCompatActivity {
    CartModel cartModel;
    ActivityViewDetailCategoryBinding binding;
//    String CATEGORY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewDetailCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        CATEGORY = getIntent().getStringExtra("CATEGORY");
        cartModel = new CartModel();
        cartModel = (CartModel) getIntent().getSerializableExtra("SINGLEITEM");

//        binding.imgUrl.setText("" + categoryListModel.getProductImage());
        binding.title.setText("" + cartModel.getProductName());

            binding.price.setText("" + cartModel.getProductPrice());
            Glide.with(getApplicationContext()).load(cartModel.getImageUrl()).into(binding.Photo);
            otherCategoryClick();



    }


    private void otherCategoryClick() {
        binding.updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CafeForYou").child("Products");
                String key = cartModel.getKey();
                cartModel = new CartModel(binding.title.getText().toString(),binding.price.getText().toString(), cartModel.getImageUrl(), cartModel.getKey());

                reference.child(key).setValue(cartModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getApplicationContext(), AddItemActivity.class));
                                Toast.makeText(ViewDetailCategory.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }, 1000);
                    }
                });
            }
        });
    }

}