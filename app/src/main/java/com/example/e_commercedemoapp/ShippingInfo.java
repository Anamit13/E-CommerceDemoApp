package com.example.e_commercedemoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

public class ShippingInfo extends AppCompatActivity {

    private AppCompatEditText oname, oaddress, omobile, ocity, opin;
    private AppCompatTextView Tprice, totalSprice;
    private AppCompatButton nextBtn;
    private AppCompatImageView homeBtn;
    String name, address, mobile, city, pin, shippingTier;

    static ArrayList<CartModel> arrayList = new ArrayList<>();

    FirebaseAnalytics firebaseAnalytics;

    Bundle gloShip;

    static List<Bundle> shipItems;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_TEXT= "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_info);
        getSupportActionBar().hide();
        init();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        Bundle bundle = getIntent().getExtras();
        totalSprice.setText(bundle.getString("tprice", ""));

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProductPage.class));
            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = oname.getText().toString().trim();
                Log.d("AAAAA", "bahar: name " + name);
                address = oaddress.getText().toString();
                mobile = omobile.getText().toString();
                city = ocity.getText().toString();
                pin = opin.getText().toString();

                Log.d("AAAAA", "onClick: name " + name);

                if (name.equals("")) {
                    oname.setError("Please fill your Name");
                }
                if(TextUtils.isEmpty(address))
                {
                    oaddress.setError("Please fill your Address");
                }
                if(TextUtils.isEmpty(mobile)){
                    omobile.setError("Please fill your contact number");
                }
                if(TextUtils.isEmpty(city)){
                    ocity.setError("Please enter your city name");
                }
                if(TextUtils.isEmpty(pin)){
                    opin.setError("Please fill your city pin");
                }
                else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("oname", name);
                    editor.putString("oaddress", address);
                    editor.putString("omobile", mobile);
                    editor.putString("ocity", city);
                    editor.putString("opin", pin);
                    editor.putString("shippingtier", shippingTier);
                    editor.putString("totalPrice", totalSprice.getText().toString());
                    editor.apply();
                    editor.commit();


                    Bundle addShippingParams = new Bundle();
                    addShippingParams.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                    addShippingParams.putDouble(FirebaseAnalytics.Param.VALUE, Double.parseDouble(totalSprice.getText().toString()));
                    addShippingParams.putString(FirebaseAnalytics.Param.COUPON, "TATVIC50");
                    addShippingParams.putString(FirebaseAnalytics.Param.SHIPPING_TIER, shippingTier);

                    addShippingParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                           AddToCart.checkoutItems.toArray(new Parcelable[AddToCart.checkoutItems.size()]));

                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_SHIPPING_INFO, addShippingParams);
                    startActivity(new Intent(getApplicationContext(), PaymentMethod.class));
                }
            }
        });

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.air:
                if (checked)
                    Tprice.setText("300");
                    shippingTier = "Airways";
                    break;
            case R.id.seaways:
                if (checked)
                    Tprice.setText("100");
                    shippingTier = "Roadways";
                    break;
            case R.id.road:
                if (checked)
                    Tprice.setText("200");
                    shippingTier = "Roadways";
                    break;
        }
    }

    private void init() {
        oname = findViewById(R.id.oname);
        oaddress = findViewById(R.id.oaddress);
        omobile = findViewById(R.id.omobile);
        ocity = findViewById(R.id.ocity);
        opin = findViewById(R.id.opin);
        Tprice = findViewById(R.id.TPrice);
        totalSprice = findViewById(R.id.totalsPrice);
        nextBtn = findViewById(R.id.nextBtn);
        homeBtn = findViewById(R.id.homeIcon);
    }
}