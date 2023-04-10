package com.example.e_commercedemoapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PurchaseSuccessful extends AppCompatActivity {
    private AppCompatImageView homeIcon;
    private AppCompatTextView dd, oid;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private AppCompatButton csBtn, rpBtn;
    FirebaseAnalytics firebaseAnalytics;
    String orderid;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";

    FirebaseFirestore db;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_successful);
        getSupportActionBar().hide();

        init();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        db = FirebaseFirestore.getInstance();
        Map<String, String> product = new HashMap<>();

        AppEventsLogger logger = AppEventsLogger.newLogger(this);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProductPage.class));
            }
        });


        Date today = new Date();
        long ltime = today.getTime()+5*(24*60*60*1000);
        Date today8 = new Date(ltime);
        SimpleDateFormat dateFormat;
        String date;
        dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
        date = dateFormat.format(today8);

        dd.setText(date);

        orderid = (String.valueOf((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L));
        oid.setText(orderid);

        product.put("delivery date", date);
        product.put("total price", sharedPreferences.getString("totalPrice", ""));
        product.put("shipping tier", sharedPreferences.getString("shippingtier", ""));
        product.put("product name", sharedPreferences.getString("proname", ""));
        product.put("product size", sharedPreferences.getString("prosize", ""));
        product.put("payment method", sharedPreferences.getString("paymentMethod", ""));
        product.put("product quantity", sharedPreferences.getString("proqty", ""));
        product.put("product image", sharedPreferences.getString("proimg", ""));


        db.collection("orders").document(orderid).set(product)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Document snapshot successful");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "error in writing",e);
                    }
                });

        csBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProductPage.class));
            }
        });

        rpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ReturnProduct.class));
            }
        });

        Bundle purchaseParams = new Bundle();
        purchaseParams.putString(FirebaseAnalytics.Param.TRANSACTION_ID, orderid);
        purchaseParams.putString(FirebaseAnalytics.Param.AFFILIATION, "Tatvic Store");
        purchaseParams.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
        purchaseParams.putDouble(FirebaseAnalytics.Param.VALUE, Double.parseDouble(sharedPreferences.getString("totalPrice", "")));
        purchaseParams.putDouble(FirebaseAnalytics.Param.TAX, 58);
        purchaseParams.putDouble(FirebaseAnalytics.Param.SHIPPING, 150);
        purchaseParams.putString(FirebaseAnalytics.Param.COUPON, "TATVIC50");
//        purchaseParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
//                AddToCart.checkoutItems.toArray(new Parcelable[AddToCart.checkoutItems.size()]));
//
        //firebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, purchaseParams);
        purchaseParams.putParcelableArrayList("items", AddToCart.checkoutItems_ga3);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, purchaseParams);

        //FB PIXEL IMPLEMENTATION
        Bundle params = new Bundle();

        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE,"product_group"); //value will be a product_group
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID,"SKU_123"); //Passing product or content identifier
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY,"INR");
        params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS,2); //Number Of Items
        params.putString(AppEventsConstants.EVENT_PARAM_ORDER_ID, orderid); //value will be unique OrderID of product
        logger.logEvent(AppEventsConstants.EVENT_NAME_PURCHASED, 7998, params); // In value - pass value of the product

        Bundle rewadz = new Bundle();
        rewadz.putString(FirebaseAnalytics.Param.TRANSACTION_ID, orderid);
        rewadz.putString("Reward_type", "Purchase reward");
        rewadz.putDouble("Reward_count", Double.parseDouble(sharedPreferences.getString("totalPrice", ""))/80);
        firebaseAnalytics.logEvent("Rewardz", rewadz);
    }

    private void init(){
        homeIcon = findViewById(R.id.homeIcon);
        dd = findViewById(R.id.dd);
        csBtn = findViewById(R.id.csbtn);
        rpBtn = findViewById(R.id.returnbtn);
        oid = findViewById(R.id.oid);
    }
}