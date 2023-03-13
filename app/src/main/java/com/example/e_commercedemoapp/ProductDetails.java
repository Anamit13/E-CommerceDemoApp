package com.example.e_commercedemoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import static com.example.e_commercedemoapp.AddToCart.arrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ProductDetails extends AppCompatActivity {

    private AppCompatTextView pname, price;
    private AppCompatImageView backIcon, productImage;
    private AppCompatButton addToCartBtn, buyNowBtn;
    private FirebaseFirestore db;
    private String fpname, fpprice, fpimg;
    private Spinner shoesizespinner;
    private String sizeSelected;
    private Spinner shoeqtySpinner;
    public static String qtySelected;
    CartModel cartModel;

    FirebaseAnalytics firebaseAnalytics;

    Bundle product = new Bundle();

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        getSupportActionBar().hide();
        db = FirebaseFirestore.getInstance();
        init();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = getIntent().getExtras();
        String getPId= bundle.getString("pid");
        fpname = bundle.getString("pname");
        pname.setText(fpname);
        fpprice = bundle.getString("pprice");
        price.setText(fpprice);
        fpimg = bundle.getString("pimg");

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
//
//        Log.d("YOUR_VLAUE", "onCreate: "+"namw " +fpname +"pricve" +fpprice +" dfimg "+fpimg);

        Glide.with(getApplicationContext()).load(fpimg).into(productImage);

        product.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(getPId));
        //Toast.makeText(getApplicationContext(),document.getString("name"), Toast.LENGTH_SHORT).show();
        product.putString(FirebaseAnalytics.Param.ITEM_NAME, fpname);
        product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Shoe");
        product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, bundle.getString("variant", ""));
        product.putString(FirebaseAnalytics.Param.ITEM_BRAND, "Nike");
        product.putString(FirebaseAnalytics.Param.PRICE, fpprice);

        Bundle viewItemParams = new Bundle();
        viewItemParams.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
        viewItemParams.putString(FirebaseAnalytics.Param.VALUE, "viewproduct");
        viewItemParams.putDouble(FirebaseAnalytics.Param.VALUE, Double.parseDouble(fpprice));
        viewItemParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                new Parcelable[]{product});

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, viewItemParams);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.shoe_sizes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        shoesizespinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.shoe_qty, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        shoeqtySpinner.setAdapter(adapter1);

        shoesizespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sizeSelected = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        shoeqtySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                qtySelected = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProductPage.class));
                finish();
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), AddToCart.class));

                if(sizeSelected != null && shoesizespinner.getSelectedItem() != null){
                    if(qtySelected != null && shoeqtySpinner.getSelectedItem() != null){
                        Toast.makeText(getApplicationContext(), "Inside if", Toast.LENGTH_SHORT).show();
                        cartModel = new CartModel(fpname, Integer.parseInt(fpprice), fpimg, sizeSelected, Integer.parseInt(qtySelected));
                        arrayList.add(cartModel);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("proname", fpname);
                        editor.putString("proprice", fpprice);
                        editor.putString("prosize", sizeSelected);
                        editor.putString("proqty", qtySelected);
                        editor.putString("proimg", fpimg);
                        editor.apply();
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "Product added to cart", Toast.LENGTH_SHORT).show();

                        Bundle itemaddToCart = new Bundle(product);
                        itemaddToCart.putLong(FirebaseAnalytics.Param.QUANTITY, Long.parseLong(qtySelected));

                        Bundle addToWishlistParams = new Bundle();
                        addToWishlistParams.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                        addToWishlistParams.putDouble(FirebaseAnalytics.Param.VALUE, Double.parseDouble(qtySelected) * Double.parseDouble(fpprice));
                        addToWishlistParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                                new Parcelable[]{itemaddToCart});

                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, addToWishlistParams);

                        startActivity(new Intent(getApplicationContext(), AddToCart.class));

                    } else {
                        Toast.makeText(getApplicationContext(),"Please select quantity!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please select size!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void init(){
        pname = findViewById(R.id.pname);
        price = findViewById(R.id.pprice);
        backIcon = findViewById(R.id.backicon);
        productImage = findViewById(R.id.productimage);
        addToCartBtn = findViewById(R.id.addtocartBtn);
        shoesizespinner = findViewById(R.id.shoe_size_spinner);
        shoeqtySpinner = findViewById(R.id.quantitySpinner);
    }
}