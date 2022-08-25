package com.yousif.cafeforyou;

import java.io.Serializable;

public class CategoryListModel implements Serializable {
    String productName,productPrice,ImageUrl,key;

    public CategoryListModel() {
    }

    public CategoryListModel(String productName, String productPrice, String imageUrl, String key) {
        this.productName = productName;
        this.productPrice = productPrice;
        ImageUrl = imageUrl;
        this.key = key;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
