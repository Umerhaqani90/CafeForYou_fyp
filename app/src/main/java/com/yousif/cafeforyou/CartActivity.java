package com.yousif.cafeforyou;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
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
import com.yousif.cafeforyou.User.BookingActivity;
import com.yousif.cafeforyou.adaptor.MyCartAdapter;
import com.yousif.cafeforyou.adaptor.eventbus.MyUpdateCartEvent;
import com.yousif.cafeforyou.listener.ICartLoadListener;
import com.yousif.cafeforyou.model.CartModel;
import com.yousif.cafeforyou.model.UserModel;
import com.yousif.cafeforyou.notification.APIClient;
import com.yousif.cafeforyou.notification.NotificationAPI;
import com.yousif.cafeforyou.notification.NotificationData;
import com.yousif.cafeforyou.notification.PushNotification;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yousif.cafeforyou.R;
import yousif.cafeforyou.databinding.ActivityBookingBinding;
import yousif.cafeforyou.databinding.ActivityCartBinding;

public class CartActivity extends AppCompatActivity implements ICartLoadListener {
    @BindView(R.id.recycler_cart)
    RecyclerView recyclerList;

    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.txt_Total)
    TextView txtTotal;

    @BindView(R.id.btnPurchase)
    Button btnPurchase;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.txtPrice)
    TextView price;

    @BindView(R.id.Photo)
    ImageView Photo;


    ICartLoadListener cartLoadListener;

    ActivityCartBinding binding;
    CartModel cartModel;
    DatabaseReference myRef;
    DatabaseReference currentUserRef, referenceCatList;
    ArrayList<UserModel> arrayList;
    Object prevBalance;
    String name;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if(EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event)
    {
        loadCartItemFromFirebase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_cart);

        title=findViewById(R.id.title);
        Photo=findViewById(R.id.Photo);

        arrayList = new ArrayList<>();
        referenceCatList = FirebaseDatabase.getInstance().getReference().child("AllUsers");
        myRef = FirebaseDatabase.getInstance().getReference("CafeForYou").child("PurchaseRequests");
        currentUserRef = FirebaseDatabase.getInstance().getReference().child("User").child("UserDetail");
//        cartModel = new CartModel();
//        cartModel = (CartModel) getIntent().getSerializableExtra("SINGLEITEM");
//
//       title.setText("" + cartModel.getProductName());
//       price.setText("" + cartModel.getProductPrice());
//        Glide.with(getApplicationContext()).load(cartModel.getImageUrl()).into(Photo);

        uploadHotelDataClick();
        //Allusers
        getFBData();
        //current User
        getCurrentUserBalance();

        Init();
        loadCartItemFromFirebase();
    }

    private void getCurrentUserBalance() {
        currentUserRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    name = (String) map.get("UserName");
                    try
                    {
                        prevBalance = (int) map.get("Balance");
                    }
                    catch (Exception e)
                    {

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
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    UserModel categoryListModel = dataSnapshot.getValue(UserModel.class);
                    arrayList.add(categoryListModel);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadHotelDataClick() {
        btnPurchase.setOnClickListener(view -> {
            HashMap<String, String> map = new HashMap<>();
            String key = myRef.push().getKey();
//            map.put("ProductName", cartModel.getProductName());
//            map.put("ProductPrice", cartModel.getProductPrice());
//            map.put("key", key);
            myRef.child(key).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    showDialogs();
                }
            });
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
        Button confirmPurchaseBtn = (Button) dialog.findViewById(R.id.btnPurchase);

        generateQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String rechargeET = dataEdt.getText().toString();
//                referenceCatList.child(arrayList.get(pos).getPrivate()).child("/Balance").setValue(rechargeET);
                generateQrBtn.setVisibility(View.GONE);
                confirmPurchaseBtn.setVisibility(View.VISIBLE);

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
        confirmPurchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prevBalance==null){
                    prevBalance=5000;
                }
                int Blnc = Integer.parseInt(cartModel.getProductPrice());
                int totalBlnc;
                totalBlnc = (int)prevBalance - Blnc;

                if(totalBlnc <500){
                    Toast.makeText(confirmPurchaseBtn.getContext(), "Please Recharge Your Balance between 500 - 5000", Toast.LENGTH_SHORT).show();
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

    private void loadCartItemFromFirebase() {
        List<CartModel>cartModels=new ArrayList<>();

        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot cartSnapshot:snapshot.getChildren())
                            {
                                CartModel cartModel=cartSnapshot.getValue(CartModel.class);
                                cartModel.setKey(cartSnapshot.getKey());
                                cartModels.add(cartModel);
                            }
                            cartLoadListener.onCartLoadSuccess(cartModels);
                        }
                        else
                        {
                           cartLoadListener.onCartLoadFailure("Cart Empty");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailure(error.getMessage());

                    }
                });

    }

    private void Init() {
        ButterKnife.bind(this);
        cartLoadListener=this;

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerList.setLayoutManager(linearLayoutManager);
        recyclerList.addItemDecoration(new DividerItemDecoration(this,linearLayoutManager.getOrientation()));

       btnBack.setOnClickListener(view -> finish());

    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        double sum=0;
        for (CartModel cartModel:cartModelList)
        {
            sum += cartModel.getTotalPrice();
        }
        txtTotal.setText(new StringBuilder(new StringBuilder("$").append(sum)));
        MyCartAdapter myCartAdapter=new MyCartAdapter(this,cartModelList);
        recyclerList.setAdapter(myCartAdapter);

    }


    @Override
    public void onCartLoadFailure(String message) {

        Snackbar.make(mainLayout,message,Snackbar.LENGTH_SHORT).show();

    }
}