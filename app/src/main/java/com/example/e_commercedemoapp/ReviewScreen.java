package com.example.e_commercedemoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;

public class ReviewScreen extends AppCompatActivity {

    private AppCompatImageView homeIcon, reviewProimg;
    private AppCompatTextView reviewProname, reviewProprice, proSize, proQty1, reviewoname, reviewoadddress, reviewomobile,
    reviewocity, reviewopin, reviewtier, reviewPayment, totalreviewPrice;
    private AppCompatButton confirmBtn;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_screen);
        getSupportActionBar().hide();

        init();

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        reviewProname.setText(sharedPreferences.getString("proname", ""));
        reviewProprice.setText(sharedPreferences.getString("proprice", ""));
        proSize.setText(sharedPreferences.getString("prosize", ""));
        proQty1.setText(sharedPreferences.getString("proqty",""));
        Glide.with(getApplicationContext()).load(sharedPreferences.getString("proimg", "")).into(reviewProimg);
        reviewoname.setText(sharedPreferences.getString("oname",""));
        reviewoadddress.setText(sharedPreferences.getString("oaddress", ""));
        reviewomobile.setText(sharedPreferences.getString("omobile",""));
        reviewocity.setText(sharedPreferences.getString("ocity", ""));
        reviewopin.setText(sharedPreferences.getString("opin", ""));
        reviewtier.setText(sharedPreferences.getString("shippingtier", ""));
        totalreviewPrice.setText(sharedPreferences.getString("totalPrice", ""));
        reviewPayment.setText(sharedPreferences.getString("paymentMethod", ""));

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProductPage.class));
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PurchaseSuccessful.class));
            }
        });

    }

    private void init(){
        homeIcon = findViewById(R.id.homeIcon);
        reviewProname = findViewById(R.id.reviewProname);
        reviewProprice = findViewById(R.id.reviewProprice);
        proSize = findViewById(R.id.proSize);
        proQty1 = findViewById(R.id.proQty);
        reviewoname = findViewById(R.id.reviewoname);
        reviewoadddress = findViewById(R.id.reviewoaddress);
        reviewomobile = findViewById(R.id.reviewomobile);
        reviewocity = findViewById(R.id.reviewocity);
        reviewopin = findViewById(R.id.reviewopin);
        reviewtier = findViewById(R.id.reviewtier);
        reviewPayment = findViewById(R.id.reviewPayment);
        totalreviewPrice = findViewById(R.id.totalreviewprice);
        confirmBtn = findViewById(R.id.confirmBtn);
        reviewProimg = findViewById(R.id.reviewProimg);
    }
}