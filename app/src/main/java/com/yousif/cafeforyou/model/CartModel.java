package com.yousif.cafeforyou.model;

import java.io.Serializable;

public class CartModel implements Serializable {
    private String key, productName, ImageUrl, productPrice;
    private int quantity;
    private float totalPrice;

    public CartModel()
    {

    }

    public CartModel(String productName, String productPrice, String imageUrl, String key) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.ImageUrl = imageUrl;
        this.key = key;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
