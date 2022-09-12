package com.yousif.cafeforyou;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.yousif.cafeforyou.model.UserModel;

import java.util.ArrayList;

import yousif.cafeforyou.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    ArrayList<UserModel> arrayList;
    Context context;
    DatabaseReference referenceCatList, currentUserRef;


    public UserListAdapter(ArrayList<UserModel> arrayList, Context context, DatabaseReference referenceCatList, DatabaseReference currentUserRef) {
        this.arrayList = arrayList;
        this.context = context;
        this.referenceCatList = referenceCatList;
        this.currentUserRef = currentUserRef;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
        holder.userName.setText(arrayList.get(position).getUserName());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                referenceCatList.child(arrayList.get(position).getPrivate()).removeValue();
                currentUserRef.child(arrayList.get(position).getPrivate()).removeValue();
            }
        });
        holder.recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        Button recharge, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            recharge = itemView.findViewById(R.id.recharge);
            delete = itemView.findViewById(R.id.delete);

        }
    }

    public void showDialog(int pos) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        EditText text = (EditText) dialog.findViewById(R.id.txt_dia);
        text.setText(arrayList.get(pos).getBalance());

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_yes);
        Button dialogButtonNo = (Button) dialog.findViewById(R.id.btn_no);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rechargeET = text.getText().toString();
                int rechargeEtInt = Integer.parseInt(rechargeET);

                if(rechargeEtInt <500 || rechargeEtInt > 5000){
                    Toast.makeText(context, "Please use values between 500 - 5000", Toast.LENGTH_SHORT).show();
                    return;
                }

                referenceCatList.child(arrayList.get(pos).getPrivate()).child("/Balance").setValue(rechargeET);
                currentUserRef.child(arrayList.get(pos).getUID()).child("/Balance").setValue(rechargeET);
                Toast.makeText(context, "***Account Recharged***", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


}
