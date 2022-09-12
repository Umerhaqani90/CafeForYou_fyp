package com.yousif.cafeforyou;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yousif.cafeforyou.activity.MainActivity;

import yousif.cafeforyou.R;
import yousif.cafeforyou.databinding.FragmentEditBinding;
import yousif.cafeforyou.databinding.FragmentProductBinding;


public class ProductFragment extends Fragment {
    CardView cardView;
   // FragmentProductBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_product, container, false);
       // binding=FragmentProductBinding.inflate(getLayoutInflater());
        View myView = inflater.inflate(R.layout.fragment_product, container, false);
        cardView = (CardView) myView.findViewById(R.id.CVSetting);


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).openDrawer();
            }
        });
        return myView;

    }

}