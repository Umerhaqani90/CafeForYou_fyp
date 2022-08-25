package com.yousif.cafeforyou;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import yousif.cafeforyou.R;


public class ProductFragment extends Fragment {
    CardView cardView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_product, container, false);

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