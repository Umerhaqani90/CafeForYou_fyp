package com.yousif.cafeforyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;
import com.yousif.cafeforyou.CartActivity;
import com.yousif.cafeforyou.adaptor.MyDrinkAdaptor;
import com.yousif.cafeforyou.adaptor.eventbus.MyUpdateCartEvent;
import com.yousif.cafeforyou.listener.ICartLoadListener;
import com.yousif.cafeforyou.listener.IDrinkLoadListener;
import com.yousif.cafeforyou.model.CartModel;
import com.yousif.cafeforyou.model.CartModel;
import com.yousif.cafeforyou.utilis.SpaceItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yousif.cafeforyou.R;

//Main activity where user can add items to cart .....
public class MainItemActivity extends AppCompatActivity implements IDrinkLoadListener, ICartLoadListener {
    @BindView(R.id.recycler_list)
    RecyclerView recyclerList;

    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;

    @BindView(R.id.badge)
    NotificationBadge badge;

    @BindView(R.id.btn_Cart)
    FrameLayout btnCart;

    IDrinkLoadListener drinkLoadListener;
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
        CountCartItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_item);

        Init();
        LoadDrinkFromFirebase();
        CountCartItem();
    }

    private void LoadDrinkFromFirebase() {
        List<CartModel>CartModelist=new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("CafeForYou").child("Products")
//                .getReference("Drink")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot drinkSnapshot:snapshot.getChildren())
                            {
                                CartModel CartModel=drinkSnapshot.getValue(CartModel.class);
                                CartModel.setKey(drinkSnapshot.getKey());
                                CartModelist.add(CartModel);
                            }
                            drinkLoadListener.onDrinkLoadSuccess(CartModelist);
                        }
                        else{
                            drinkLoadListener.onDrinkLoadFailure("Can't find drink");
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        drinkLoadListener.onDrinkLoadFailure(error.getMessage());

                    }
                });

    }

    private void Init() {
        ButterKnife.bind(this);
        drinkLoadListener=this;
        cartLoadListener=this;

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recyclerList.setLayoutManager(gridLayoutManager);
        recyclerList.addItemDecoration(new SpaceItemDecoration());

        btnCart.setOnClickListener(view -> startActivity(new Intent(this, CartActivity.class)));
    }
    @Override
    public void onDrinkLoadSuccess(List<CartModel> CartModelList) {
        MyDrinkAdaptor adaptor=new MyDrinkAdaptor(this,CartModelList,cartLoadListener);
        recyclerList.setAdapter(adaptor);


    }

    @Override
    public void onDrinkLoadFailure(String message) {
        Snackbar.make(mainLayout,message,Snackbar.LENGTH_SHORT).show();


    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        int cartSum = 0;
        for (CartModel cartModel:cartModelList)
            cartSum+=cartModel.getQuantity();
        badge.setNumber(cartSum);

    }

    @Override
    public void onCartLoadFailure(String message) {
        Snackbar.make(mainLayout,message,Snackbar.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        CountCartItem();
    }

    private void CountCartItem() {
        List<CartModel>cartModels=new ArrayList<>();

        FirebaseDatabase
                .getInstance().getReference("Cart")
                .child("UNIQUE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot cartSnapshot:snapshot.getChildren())
                        {
                            CartModel cartModel=cartSnapshot.getValue(CartModel.class);
                            cartModel.setKey(cartSnapshot.getKey());
                            cartModels.add(cartModel);
                        }
                        cartLoadListener.onCartLoadSuccess(cartModels);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailure(error.getMessage());

                    }
                });
    }
}