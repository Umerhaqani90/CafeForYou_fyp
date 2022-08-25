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




import yousif.cafeforyou.databinding.ActivityViewDetailCategoryBinding;

public class ViewDetailCategory extends AppCompatActivity {
    CategoryListModel categoryListModel;
    ActivityViewDetailCategoryBinding binding;
//    String CATEGORY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewDetailCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        CATEGORY = getIntent().getStringExtra("CATEGORY");
        categoryListModel = new CategoryListModel();
        categoryListModel = (CategoryListModel) getIntent().getSerializableExtra("SINGLEITEM");

            binding.title.setText("" + categoryListModel.getProductName());
            binding.price.setText("" + categoryListModel.getProductPrice());
            Glide.with(getApplicationContext()).load(categoryListModel.getImageUrl()).into(binding.Photo);
            otherCategoryClick();



    }


    private void otherCategoryClick() {
        binding.updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CafeForYou").child("Products");
                String key = categoryListModel.getKey();
                categoryListModel = new CategoryListModel(binding.title.getText().toString(),binding.price.getText().toString(),categoryListModel.getImageUrl(),categoryListModel.getKey());

                reference.child(key).setValue(categoryListModel).addOnSuccessListener(new OnSuccessListener<Void>() {
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