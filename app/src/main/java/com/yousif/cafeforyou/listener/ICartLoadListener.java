package com.yousif.cafeforyou.listener;

import com.yousif.cafeforyou.model.CartModel;

import java.util.List;

public interface ICartLoadListener {
    void onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartLoadFailure(String message);
}
