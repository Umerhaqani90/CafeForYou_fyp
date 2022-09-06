package com.yousif.cafeforyou;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.yousif.cafeforyou.User.BookingActivity;
import com.yousif.cafeforyou.model.CartModel;

import java.util.ArrayList;

import yousif.cafeforyou.R;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.HolderClass> {
    ArrayList<CartModel> arrayList;
    Context context;
    DatabaseReference myRef;
    String purpose;

    public CatAdapter(ArrayList<CartModel> arrayList, Context context, DatabaseReference myRef, String purpose) {
        this.arrayList = arrayList;
        this.context = context;
        this.myRef = myRef;
        this.purpose = purpose;

    }

    @NonNull
    @Override
    public HolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.place_view, parent, false);
        return new HolderClass(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HolderClass holder, int position) {
        if (purpose.equals("PurchaseItem")) {
            holder.deletebtn.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(view -> {
                CartModel cartModel = arrayList.get(position);
                Intent intent = new Intent(context, BookingActivity.class);
                intent.putExtra("SINGLEITEM", cartModel);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
        } else {
            holder.itemView.setOnClickListener(view -> {
                CartModel cartModel = arrayList.get(position);
                Intent intent = new Intent(context, ViewDetailCategory.class);
                intent.putExtra("SINGLEITEM", cartModel);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
        }
        holder.productName.setText("" + arrayList.get(position).getProductName());
        Glide.with(context).load(arrayList.get(position).getImageUrl()).into(holder.imageView);
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(arrayList.get(position).getKey()).removeValue();

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class HolderClass extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView productName;
        ImageButton deletebtn;

        public HolderClass(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            imageView = itemView.findViewById(R.id.productImg);
            deletebtn = itemView.findViewById(R.id.deletebtn);
        }
    }
}
