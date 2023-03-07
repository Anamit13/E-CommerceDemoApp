package com.example.e_commercedemoapp;

public class OrderModel {
    private String orderId, deliveryDate, paymentMethod, pImg, pName, pQty, pSize, sTier, tPrice;

    public OrderModel(){}

    public OrderModel(String orderId, String deliveryDate, String paymentMethod, String pImg, String pName, String pQty, String pSize, String sTier, String tPrice) {
        this.orderId = orderId;
        this.deliveryDate = deliveryDate;
        this.paymentMethod = paymentMethod;
        this.pImg = pImg;
        this.pName = pName;
        this.pQty = pQty;
        this.pSize = pSize;
        this.sTier = sTier;
        this.tPrice = tPrice;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getpImg() {
        return pImg;
    }

    public void setpImg(String pImg) {
        this.pImg = pImg;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpQty() {
        return pQty;
    }

    public void setpQty(String pQty) {
        this.pQty = pQty;
    }

    public String getpSize() {
        return pSize;
    }

    public void setpSize(String pSize) {
        this.pSize = pSize;
    }

    public String getsTier() {
        return sTier;
    }

    public void setsTier(String sTier) {
        this.sTier = sTier;
    }

    public String gettPrice() {
        return tPrice;
    }

    public void settPrice(String tPrice) {
        this.tPrice = tPrice;
    }
}
