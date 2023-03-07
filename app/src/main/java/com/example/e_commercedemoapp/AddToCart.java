package com.example.e_commercedemoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

public class AddToCart extends AppCompatActivity {
    private CartAdapter adapter;
    static ArrayList<CartModel> arrayList = new ArrayList<>();
    CartModel cartModel;
    private RecyclerView cart_recyclerview;
    AppCompatTextView totalPrice;
    private AppCompatButton checkoutBtn;
    private AppCompatImageView homeBtn;

    private Bundle glo;
    private Bundle glo1;
    static Bundle checkoutCartParams;
    static List<Bundle> cartItems;

    static List<Bundle> checkoutItems;

    int tprice = 0;

    FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        getSupportActionBar().hide();

        init();

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        cartItems = new ArrayList<>();

        for(CartModel item : arrayList) {
            switch (item.getProname()){
                case "Nike Air 1.5":
                    glo = ProductPage.NikeAir;
                    break;
                case "Nike jordan":
                    glo = ProductPage.NikeJordan;
                    break;
                case "Nike light 1.5":
                    glo = ProductPage.NikeLight;
                    break;
                case "Nike running":
                    glo = ProductPage.NikeRunning;
                    break;
                default:
                    glo = new Bundle();
                    break;
            }
            Bundle view_cart = new Bundle(glo);
            view_cart.putInt(FirebaseAnalytics.Param.QUANTITY, item.getProQty());
//            Toast.makeText(this, Integer.parseInt(item.getProQty()), Toast.LENGTH_SHORT).show();

            tprice += Integer.parseInt(glo.getString("price", "")) * Integer.parseInt(String.valueOf(item.getProQty()));

//            Toast.makeText(this, String.valueOf(tprice), Toast.LENGTH_SHORT).show();

            cartItems.add(view_cart);
        }

        Bundle viewCartParams = new Bundle();
        viewCartParams.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
        viewCartParams.putString(FirebaseAnalytics.Param.VALUE, String.valueOf(tprice));
        viewCartParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                cartItems.toArray(new Parcelable[cartItems.size()]));

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_CART, viewCartParams);

        adapter = new CartAdapter(this,arrayList);
        cart_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        cart_recyclerview.setAdapter(adapter);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProductPage.class));
                finish();
            }
        });

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShippingInfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("tprice", totalPrice.getText().toString());
                intent.putExtras(bundle);

                checkoutItems = new ArrayList<>();

                for (CartModel item1 : arrayList) {
                    switch (item1.getProname()) {
                        case "Nike Air 1.5":
                            glo1 = ProductPage.NikeAir;
                            break;
                        case "Nike jordan":
                            glo1 = ProductPage.NikeJordan;
                            break;
                        case "Nike light 1.5":
                            glo1 = ProductPage.NikeLight;
                            break;
                        case "Nike running":
                            glo1 = ProductPage.NikeRunning;
                            break;
                        default:
                            glo1 = new Bundle();
                            break;
                    }
                    Bundle checkOut = new Bundle(glo1);
                    checkOut.putInt(FirebaseAnalytics.Param.QUANTITY, item1.getProQty());


                    checkoutItems.add(checkOut);

                    Bundle beginCheckoutparams = new Bundle();
                    beginCheckoutparams.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                    beginCheckoutparams.putString(FirebaseAnalytics.Param.COUPON, "TATVIC50");
                    beginCheckoutparams.putDouble(FirebaseAnalytics.Param.VALUE, Double.parseDouble(totalPrice.getText().toString()));
                    beginCheckoutparams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                            checkoutItems.toArray(new Parcelable[checkoutItems.size()]));

                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, beginCheckoutparams);

                    startActivity(intent);

                }
            }
        });


    }

    private void init(){
        totalPrice = findViewById(R.id.totalPrice);
        checkoutBtn = findViewById(R.id.checkOutbtn);
        cart_recyclerview = findViewById(R.id.cartRecyclerview);
        homeBtn = findViewById(R.id.homeIcon);
    }
}