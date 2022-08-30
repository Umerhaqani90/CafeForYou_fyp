package com.yousif.cafeforyou;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import yousif.cafeforyou.R;

public class OpenForm extends AppCompatActivity {
    String CATEGORY;
    Uri imageuri = null;
    StorageReference filepaths;
    EditText priceEdt, productname;
    ProgressDialog progressDialog;
    Button uploadData;
    ImageView img;
    public static final int PERMISSION_CODE = 1001;
    private static final int IMAGE_PICKER_CODE = 101;
    Uri filePath;
    ScrollView otherScrollForm;
    CategoryListModel categoryListModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_form);

        progressDialog = new ProgressDialog(this);
        uploadData = findViewById(R.id.uploadData);
        otherScrollForm = findViewById(R.id.otherScrollForm);
        img = findViewById(R.id.Photo);
        productname = findViewById(R.id.producttitle);
        priceEdt = findViewById(R.id.price);
        categoryListModel = new CategoryListModel();


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();
                    }
                }

            }
        });

        uploadDataClick();

    }


    private void uploadDataClick() {
        uploadData.setOnClickListener(v -> {

            if (imageuri == null) {
                Toast.makeText(this, "Please Fill detail", Toast.LENGTH_SHORT).show();
            } else if (productname.getText().toString() == null) {
                Toast.makeText(this, "Please Fill detail", Toast.LENGTH_SHORT).show();
            } else if (priceEdt.getText().toString() == null) {
                Toast.makeText(this, "Please Fill detail", Toast.LENGTH_SHORT).show();
            } else {
                addToFirebase();
            }
        });


    }

    private void addToFirebase() {
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        final String timestamp = "" + System.currentTimeMillis();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        filepaths = storageReference.child("product" + timestamp);
        filepaths.putFile(imageuri).addOnSuccessListener(taskSnapshot -> {

            Task<Uri> task1 = taskSnapshot.getMetadata().getReference().getDownloadUrl();
            task1.addOnSuccessListener(Uri -> {
                task1.addOnSuccessListener(uri2 -> {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CafeForYou").child("Products");
                    String key = reference.push().getKey();
                    categoryListModel = new CategoryListModel(productname.getText().toString(),priceEdt.getText().toString(),uri2.toString(),key);
                    reference.child(key).setValue(categoryListModel).addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        imageuri = filePath = null;
                        Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(OpenForm.this, AdminMenu.class));
                    });
                });
            });


        });

    }


    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICKER_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICKER_CODE) {
                imageuri = data.getData();

                Glide.with(getApplicationContext()).load(imageuri).into(img);

            }
        }
    }

}