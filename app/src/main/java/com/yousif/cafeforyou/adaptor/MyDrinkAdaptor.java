package com.yousif.cafeforyou.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yousif.cafeforyou.adaptor.eventbus.MyUpdateCartEvent;
import com.yousif.cafeforyou.listener.ICartLoadListener;
import com.yousif.cafeforyou.listener.IRecyclerViewClickListener;
import com.yousif.cafeforyou.model.CartModel;
import com.yousif.cafeforyou.model.CartModel;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import yousif.cafeforyou.R;

public class MyDrinkAdaptor extends RecyclerView.Adapter<MyDrinkAdaptor.MyDrinkViewHolder> {
    private Context context;
    private List<CartModel> CartModelList;
    private ICartLoadListener iCartLoadListener;

    public MyDrinkAdaptor(Context context, List<CartModel> CartModelList, ICartLoadListener iCartLoadListener) {
        this.context = context;
        this.CartModelList = CartModelList;
        this.iCartLoadListener = iCartLoadListener;

    }

//    public MyDrinkAdaptor (Context context,List<CartModel>CartModelList)
//    {
//        this.context=context;
//        this.CartModelList=CartModelList;
//    }


    @NonNull
    @Override
    public MyDrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyDrinkViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_drink_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyDrinkViewHolder holder, int position) {
        Glide.with(context)
                .load(CartModelList.get(position).getImageUrl())
                .into(holder.imageView);
        holder.txtPrice.setText(new StringBuilder("$").append(CartModelList.get(position).getProductPrice()));
        holder.txtName.setText(new StringBuilder().append(CartModelList.get(position).getProductName()));

        holder.setClickListener((view, adapterPosition) -> {
            addToCart(CartModelList.get(position));

        });
    }

    private void addToCart(CartModel CartModel) {
        DatabaseReference userCart= FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID");
        userCart.child(CartModel.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) // if user already have item in Cart
                        {
                            //just update quantity and total price
                            CartModel cartModel=snapshot.getValue(CartModel.class);

                            cartModel.setQuantity(cartModel.getQuantity()+1);
                            Map<String,Object> updateData=new HashMap<>();
                            updateData.put("Quantity",cartModel.getQuantity());
                            updateData.put("Total Price",cartModel.getQuantity()*Float.parseFloat(cartModel.getProductPrice()));

                            userCart.child(CartModel.getKey())
                                    .updateChildren(updateData)
                                    .addOnSuccessListener(unused -> iCartLoadListener.onCartLoadFailure("Add to Cart Success")).addOnFailureListener(e -> iCartLoadListener.onCartLoadFailure(e.getMessage()));

                        }
                        else // if item not have in cart, add new
                        {
                            CartModel cartModel=new CartModel();
                            cartModel.setProductName(CartModel.getProductName());
                            cartModel.setImageUrl(CartModel.getImageUrl());
                            cartModel.setKey(CartModel.getKey());
                            cartModel.setProductPrice(CartModel.getProductPrice());
                            cartModel.setQuantity(1);
                            cartModel.setTotalPrice(Float.parseFloat(CartModel.getProductPrice()));

                            userCart.child(CartModel.getKey())
                                    .setValue(cartModel)
                                    .addOnSuccessListener(unused -> iCartLoadListener.onCartLoadFailure("Add to Cart Success")).addOnFailureListener(e -> iCartLoadListener.onCartLoadFailure(e.getMessage()));

                        }

                        EventBus.getDefault().postSticky(new MyUpdateCartEvent());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iCartLoadListener.onCartLoadFailure(error.getMessage());

                    }
                });
    }

    @Override
    public int getItemCount() {
        return CartModelList.size();
    }

    public class MyDrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.txtPrice)
        TextView txtPrice;
        @BindView(R.id.txtName)
        TextView txtName;

        IRecyclerViewClickListener clickListener;

        public void setClickListener(IRecyclerViewClickListener clickListener) {
            this.clickListener = clickListener;
        }

        private Unbinder unbinder;
        public MyDrinkViewHolder(@NonNull View itemView) {
            super(itemView);

            unbinder= ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.OnRecyclerClick(view,getAdapterPosition());
        }
    }
}
