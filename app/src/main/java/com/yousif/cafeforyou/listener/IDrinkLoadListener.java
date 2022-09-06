package com.yousif.cafeforyou.listener;


import com.yousif.cafeforyou.model.DrinkModel;

import java.util.List;

public interface IDrinkLoadListener {
    void onDrinkLoadSuccess(List<DrinkModel>drinkModelList);
    void onDrinkLoadFailure(String message);
}
