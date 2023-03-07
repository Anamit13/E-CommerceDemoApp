package com.example.e_commercedemoapp;

public class CartModel {
    private String proid;
    private String proname;
    private int proprice;
    private String proimg;
    private String prosize;
    private int proQty;

    public CartModel() {}

    public CartModel(String proname, int proprice, String proimg) {
        this.proname = proname;
        this.proprice = proprice;
        this.proimg = proimg;
    }

    public CartModel(String proid, String proname, int proprice, String proimg) {
        this.proid = proid;
        this.proname = proname;
        this.proprice = proprice;
        this.proimg = proimg;
    }

    public CartModel(String proid, String proname, int proprice, String proimg, String prosize, int proQty) {
        this.proid = proid;
        this.proname = proname;
        this.proprice = proprice;
        this.proimg = proimg;
        this.prosize = prosize;
        this.proQty = proQty;
    }

    public CartModel(String proname, int proprice, String proimg, String prosize, int proQty) {
        this.proname = proname;
        this.proprice = proprice;
        this.proimg = proimg;
        this.prosize = prosize;
        this.proQty = proQty;
    }

    public String getProsize() {
        return prosize;
    }

    public void setProsize(String prosize) {
        this.prosize = prosize;
    }

    public int getProQty() {
        return proQty;
    }

    public void setProQty(int proQty) {
        this.proQty = proQty;
    }

    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public int getProprice() {
        return proprice;
    }

    public void setProprice(int proprice) {
        this.proprice = proprice;
    }

    public String getProimg() {
        return proimg;
    }

    public void setProimg(String proimg) {
        this.proimg = proimg;
    }
}
