package com.example.e_commercedemoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.RadioButton;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics.Event;

public class PaymentMethod extends AppCompatActivity {
    private AppCompatImageView homeIcon;
    String paymentMethod;
    private AppCompatTextView totalpayPrice;
    private AppCompatButton makePayment;
    FirebaseAnalytics firebaseAnalytics;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        getSupportActionBar().hide();
        init();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ProductPage.class));
            }
        });

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        totalpayPrice.setText(sharedPreferences.getString("totalPrice", ""));

        AppEventsLogger logger = AppEventsLogger.newLogger(this);


        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("paymentMethod", paymentMethod);
                editor.apply();
                editor.commit();

                Bundle addPaymentParams = new Bundle();
                addPaymentParams.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                addPaymentParams.putDouble(FirebaseAnalytics.Param.VALUE, Double.parseDouble(totalpayPrice.getText().toString()));
                addPaymentParams.putString(FirebaseAnalytics.Param.COUPON, "TATVIC50");
                addPaymentParams.putString(FirebaseAnalytics.Param.PAYMENT_TYPE, paymentMethod);
                addPaymentParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                        AddToCart.checkoutItems.toArray(new Parcelable[0]));

                //firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, addPaymentParams);

                //GA3 Implementation
                Bundle ecommerceBundle = new Bundle();
                ecommerceBundle.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                ecommerceBundle.putDouble(FirebaseAnalytics.Param.VALUE, Double.parseDouble(totalpayPrice.getText().toString()));
                //ecommerceBundle.putString(FirebaseAnalytics.Param.CHECKOUT_OPTION, paymentMethod);
                ecommerceBundle.putParcelableArrayList("items", AddToCart.checkoutItems_ga3);

                //firebaseAnalytics.logEvent(Event.SET_CHECKOUT_OPTION, ecommerceBundle);

                //FB PIXEL IMPLEMENTATION
                Bundle params = new Bundle();
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE,"product_group"); //value will be a product_group
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID,"SKU_123"); //Passing product or content identifier
                params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY,"INR");
                params.putString(AppEventsConstants.EVENT_PARAM_PAYMENT_INFO_AVAILABLE,paymentMethod);
                params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS,2); //Number Of Items
                logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_PAYMENT_INFO, 7998, params);  // In value - pass value of the product

                startActivity(new Intent(getApplicationContext(), ReviewScreen.class));
            }
        });
    }

    public void init(){
        homeIcon = findViewById(R.id.homeIcon);
        makePayment = findViewById(R.id.makepaymentbtn);
        totalpayPrice = findViewById(R.id.totalpayprice);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.netBanking:
                if (checked)
                    paymentMethod = "Net Banking";
                    break;
            case R.id.visa:
                if (checked)
                    paymentMethod = "Visa";
                    break;
            case R.id.upi:
                if (checked)
                    paymentMethod = "UPI";
                    break;
            case R.id.wallet:
                if (checked)
                    paymentMethod = "Wallet";
                break;
            case R.id.cod:
                if (checked)
                    paymentMethod = "Cash on Delivery";
                break;
        }
    }
}