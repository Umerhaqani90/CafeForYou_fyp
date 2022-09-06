package com.yousif.cafeforyou;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yousif.cafeforyou.adaptor.MyCartAdapter;
import com.yousif.cafeforyou.adaptor.eventbus.MyUpdateCartEvent;
import com.yousif.cafeforyou.listener.ICartLoadListener;
import com.yousif.cafeforyou.model.CartModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yousif.cafeforyou.R;

public class CartActivity extends AppCompatActivity implements ICartLoadListener {
    @BindView(R.id.recycler_cart)
    RecyclerView recyclerList;

    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.txt_Total)
    TextView txtTotal;

    ICartLoadListener cartLoadListener;

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
        setContentView(R.layout.activity_cart);
        
        Init();
        loadCartItemFromFirebase();
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