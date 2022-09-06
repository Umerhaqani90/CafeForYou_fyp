package com.yousif.cafeforyou.User;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.yousif.cafeforyou.model.UserModel;
import com.yousif.cafeforyou.model.CartModel;
import com.yousif.cafeforyou.notification.APIClient;
import com.yousif.cafeforyou.notification.NotificationAPI;
import com.yousif.cafeforyou.notification.NotificationData;
import com.yousif.cafeforyou.notification.PushNotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yousif.cafeforyou.R;
import yousif.cafeforyou.databinding.ActivityBookingBinding;


public class BookingActivity extends AppCompatActivity {
    ActivityBookingBinding binding;
    CartModel cartModel;
    DatabaseReference myRef;
    DatabaseReference currentUserRef, referenceCatList;
    ArrayList<UserModel> arrayList;
    Object prevBalance;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        arrayList = new ArrayList<>();
        referenceCatList = FirebaseDatabase.getInstance().getReference().child("AllUsers");
        myRef = FirebaseDatabase.getInstance().getReference("CafeForYou").child("PurchaseRequests");
        currentUserRef = FirebaseDatabase.getInstance().getReference().child("User").child("UserDetail");
        cartModel = new CartModel();
        cartModel = (CartModel) getIntent().getSerializableExtra("SINGLEITEM");

        binding.title.setText("" + cartModel.getProductName());
        binding.price.setText("" + cartModel.getProductPrice());
        Glide.with(getApplicationContext()).load(cartModel.getImageUrl()).into(binding.Photo);
        uploadHotelDataClick();
        //Allusers
        getFBData();
        //current User
        getCurrentUserBalance();
    }

    private void getCurrentUserBalance() {
        currentUserRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    name = (String) map.get("UserName");
                    try {
                        prevBalance = (int) map.get("Balance");
                    }catch (Exception e){

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void getFBData() {
        referenceCatList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModel categoryListModel = dataSnapshot.getValue(UserModel.class);
                    arrayList.add(categoryListModel);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void showDialogs() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.qrcode_dialog);

        EditText dataEdt = (EditText) dialog.findViewById(R.id.idEdt);
        ImageView qrCodeIV = (ImageView) dialog.findViewById(R.id.idIVQrcode);
        dataEdt.setText(name);
        Button generateQrBtn = (Button) dialog.findViewById(R.id.idBtnGenerateQR);
        Button confrmPurchaseBtn = (Button) dialog.findViewById(R.id.confirmbtn_click);

        generateQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String rechargeET = dataEdt.getText().toString();
//                referenceCatList.child(arrayList.get(pos).getPrivate()).child("/Balance").setValue(rechargeET);
                generateQrBtn.setVisibility(View.GONE);
                confrmPurchaseBtn.setVisibility(View.VISIBLE);

                MultiFormatWriter mWriter = new MultiFormatWriter();
                try {
                    //BitMatrix class to encode entered text and set Width & Height
                    BitMatrix mMatrix = mWriter.encode(name, BarcodeFormat.QR_CODE, 400, 400);
                    BarcodeEncoder mEncoder = new BarcodeEncoder();
                    Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
                    qrCodeIV.setImageBitmap(mBitmap);//Setting generated QR code to imageView
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(dataEdt.getApplicationWindowToken(), 0);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
        confrmPurchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prevBalance==null){
                    prevBalance=5000;
                }
                int Blnc = Integer.parseInt(cartModel.getProductPrice());
                int totalBlnc;
                totalBlnc = (int)prevBalance - Blnc;

                if(totalBlnc <500){
                    Toast.makeText(confrmPurchaseBtn.getContext(), "Please Recharge Your Balance between 500 - 5000", Toast.LENGTH_SHORT).show();
                    return;
                }


                currentUserRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Balance").setValue(totalBlnc);
                Toast.makeText(getApplicationContext(), "***AMOUNT deducted***", Toast.LENGTH_SHORT).show();
                getAdminFcmToken(cartModel.getProductName());
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void uploadHotelDataClick() {
        binding.BookNow.setOnClickListener(view -> {
            HashMap<String, String> map = new HashMap<>();
            String key = myRef.push().getKey();
            map.put("ProductName", cartModel.getProductName());
            map.put("ProductPrice", cartModel.getProductPrice());
            map.put("key", key);
            myRef.child(key).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    showDialogs();
                }
            });
        });

    }
    private void getAdminFcmToken(String CATEGORY) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User").child("UserDetail");
        reference.child("7MbgxVsvNKQB9TPvPFjlS09w4SI2").child("fcmToken")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String fcm = snapshot.getValue(String.class);
                        sendNotification(fcm, CATEGORY, "Purchased a product " + CATEGORY);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void sendNotification(String token, String title, String body) {
        PushNotification rootModel = new PushNotification(new NotificationData(title, body), token);
        NotificationAPI apiService = APIClient.getClient().create(NotificationAPI.class);
        Call<ResponseBody> responseBodyCall = apiService.sendNotification(rootModel);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("NOTIFICATION_SEND", response.body() + "     1");
                } else {
                    Log.d("NOTIFICATION_SEND", response.code() + "     2");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("NOTIFICATION_SEND", t.getMessage());
            }
        });
    }
}