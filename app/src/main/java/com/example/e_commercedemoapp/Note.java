package com.example.e_commercedemoapp;

public class Note {
    private String pname;
    private int pprice;
    private String pImg;
    private String pid;

    public Note(){

    }

    public Note(String pname, int pprice) {
        this.pname = pname;
        this.pprice = pprice;
    }

    public Note(String pname, int pprice, String pImg, String pid) {
        this.pname = pname;
        this.pprice = pprice;
        this.pImg = pImg;
        this.pid = pid;
    }

    public Note(String pname, int pprice, String pImg) {
        this.pname = pname;
        this.pprice = pprice;
        this.pImg = pImg;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getpImg() {
        return pImg;
    }

    public void setpImg(String pImg) {
        this.pImg = pImg;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getPprice() {
        return pprice;
    }

    public void setPprice(int pprice) {
        this.pprice = pprice;
    }
}
