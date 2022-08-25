package com.yousif.cafeforyou;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import yousif.cafeforyou.databinding.FragmentManageUsersBinding;

public class ManageUsersFragment extends Fragment {
    FragmentManageUsersBinding binding;
    ArrayList<UserModel> arrayList;
    DatabaseReference referenceCatList, currentUserRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageUsersBinding.inflate(getLayoutInflater());

        arrayList = new ArrayList<>();
        currentUserRef = FirebaseDatabase.getInstance().getReference().child("User").child("UserDetail");
        referenceCatList = FirebaseDatabase.getInstance().getReference().child("AllUsers");
        getFBData();
        return binding.getRoot();
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
                setCatAdapter(arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setCatAdapter(ArrayList<UserModel> arrayList) {
        binding.recyclerView.setAdapter(new UserListAdapter(arrayList, getContext(), referenceCatList, currentUserRef));
    }
}