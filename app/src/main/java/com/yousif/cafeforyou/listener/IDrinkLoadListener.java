package com.yousif.cafeforyou.listener;


import com.yousif.cafeforyou.model.CartModel;

import java.util.List;

public interface IDrinkLoadListener {
    void onDrinkLoadSuccess(List<CartModel>CartModelList);
    void onDrinkLoadFailure(String message);
}
