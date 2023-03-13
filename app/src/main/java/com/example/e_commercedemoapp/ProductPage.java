package com.example.e_commercedemoapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Objects;

public class ProductPage extends AppCompatActivity implements NoteAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {


    private AppCompatImageView menuIc, sp1;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private ArrayList<Note> arrayList;

    private FirebaseFirestore db;

    private FirebaseAnalytics firebaseAnalytics;

//    Bundle NikeAir, NikeJordan, NikeLight, NikeRunning;
    static Bundle NikeAir = new Bundle();
    static Bundle NikeJordan = new Bundle();
    static Bundle NikeLight = new Bundle();
    static Bundle NikeRunning = new Bundle();
    static Bundle NikeComfort = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        getSupportActionBar().hide();
        init();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.prorecyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setHasFixedSize(true);
        arrayList = new ArrayList<>();

        fetchData();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        menuIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });

        //VIEW PROMOTION EVENT - GA4 IMPLEMENTATION

        Bundle promoParams = new Bundle();
        promoParams.putString(FirebaseAnalytics.Param.PROMOTION_ID, "SUMMER_FUN");
        promoParams.putString(FirebaseAnalytics.Param.PROMOTION_NAME, "Summer Sale");
        promoParams.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "summer2020_promo.jpg");
        promoParams.putString(FirebaseAnalytics.Param.CREATIVE_SLOT, "featured_app_1");
        promoParams.putString(FirebaseAnalytics.Param.LOCATION_ID, "HERO_BANNER");
        promoParams.putString(FirebaseAnalytics.Param.VALUE, "viewpromotion");
        promoParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                new Parcelable[]{NikeAir});

// Promotion displayed
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_PROMOTION, promoParams);

        //GA3 implementation view promotion
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, promoParams);


        sp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_PROMOTION, promoParams);

                //GA3 select promotion
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, promoParams);

                startActivity(new Intent(getApplicationContext(), PromotionSale.class));
// Promotion selected


            }
        });

    }



    private void fetchData(){
        db.collection("products4.4")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().isEmpty())
                            {
                                recyclerView.setVisibility(View.GONE);
                            }
                            else
                            {
                                recyclerView.setVisibility(View.VISIBLE);
                                for (QueryDocumentSnapshot document: task.getResult())
                                {
                                    Log.d(TAG, "Data came");

                                    Note note = new Note(document.getString("name"), Math.toIntExact(document.getLong("price")), document.getString("pro_img"), document.getId());
                                    arrayList.add(note);



                                    if(document.getId().equals("product1")){
                                        NikeAir.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(document.getLong("id")));
                                        //Toast.makeText(getApplicationContext(),document.getString("name"), Toast.LENGTH_SHORT).show();
                                        NikeAir.putString(FirebaseAnalytics.Param.ITEM_NAME, document.getString("name"));
                                        NikeAir.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Shoe");
                                        NikeAir.putString(FirebaseAnalytics.Param.ITEM_VARIANT, "White-blue");
                                        NikeAir.putString(FirebaseAnalytics.Param.ITEM_BRAND, "Nike");
                                        NikeAir.putString(FirebaseAnalytics.Param.PRICE, String.valueOf(document.getLong("price")));
                                    }
                                    else if(document.getId().equals("product2"))
                                    {
                                        NikeJordan.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(document.getLong("id")));
                                        NikeJordan.putString(FirebaseAnalytics.Param.ITEM_NAME, document.getString("name"));
                                        //Toast.makeText(getApplicationContext(),document.getString("name"), Toast.LENGTH_SHORT).show();
                                        NikeJordan.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Shoe");
                                        NikeJordan.putString(FirebaseAnalytics.Param.ITEM_VARIANT, "white-pink");
                                        NikeJordan.putString(FirebaseAnalytics.Param.ITEM_BRAND, "Nike");
                                        NikeJordan.putString(FirebaseAnalytics.Param.PRICE, String.valueOf(document.getLong("price")));
                                    }
                                    else if(document.getId().equals("product3"))
                                    {
                                        NikeLight.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(document.getLong("id")));
                                        NikeLight.putString(FirebaseAnalytics.Param.ITEM_NAME, document.getString("name"));
                                        //Toast.makeText(getApplicationContext(),document.getString("name"), Toast.LENGTH_SHORT).show();
                                        NikeLight.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Shoe");
                                        NikeLight.putString(FirebaseAnalytics.Param.ITEM_VARIANT, "Yellow");
                                        NikeLight.putString(FirebaseAnalytics.Param.ITEM_BRAND, "Nike");
                                        NikeLight.putString(FirebaseAnalytics.Param.PRICE, String.valueOf(document.getLong("price")));
                                    }
                                    else if(document.getId().equals("product4"))
                                    {
                                        NikeRunning.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(document.getLong("id")));
                                        NikeRunning.putString(FirebaseAnalytics.Param.ITEM_NAME, document.getString("name"));
                                        //Toast.makeText(getApplicationContext(),document.getString("name"), Toast.LENGTH_SHORT).show();
                                        NikeRunning.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Shoe");
                                        NikeRunning.putString(FirebaseAnalytics.Param.ITEM_VARIANT, "Green");
                                        NikeRunning.putString(FirebaseAnalytics.Param.ITEM_BRAND, "Nike");
                                        NikeRunning.putString(FirebaseAnalytics.Param.PRICE, String.valueOf(document.getLong("price")));
                                    }
                                      //GA4 IMPLEMENTATION
                                }
                                Bundle NikeAirWithIndex = new Bundle(NikeAir);
                                NikeAirWithIndex.putLong(FirebaseAnalytics.Param.INDEX, 1);

                                Bundle NikeJordanWithIndex = new Bundle(NikeJordan);
                                NikeJordanWithIndex.putLong(FirebaseAnalytics.Param.INDEX, 2);

                                Bundle NikeLightWithIndex = new Bundle(NikeLight);
                                NikeLightWithIndex.putLong(FirebaseAnalytics.Param.INDEX, 3);

                                Bundle NikeRunningWithIndex = new Bundle(NikeRunning);
                                NikeRunningWithIndex.putLong(FirebaseAnalytics.Param.INDEX, 4);


                                Bundle viewItemListParams = new Bundle();
                                viewItemListParams.putString(FirebaseAnalytics.Param.ITEM_LIST_ID, "L001");
                                viewItemListParams.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Related products");
                                viewItemListParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                                        new Parcelable[]{NikeAirWithIndex, NikeJordanWithIndex, NikeLightWithIndex, NikeRunningWithIndex});
                                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, viewItemListParams);

                                //GA3 IMPLEMENTATION
                                adapter = new NoteAdapter(getApplicationContext(), arrayList);
                                recyclerView.setAdapter(adapter);
                                adapter.setOnItemClickListener(ProductPage.this::onItemClick);
                            }

                        }
                        else
                        {
                            Log.d(TAG, "Error getting Documents: ", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Error", "Failed to query data");
                    }
                });
    }


    private void init(){

        menuIc = findViewById(R.id.menuicon);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        sp1 = findViewById(R.id.sp1);

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), ProductDetails.class);
        Bundle bundle = new Bundle();
        Note note = arrayList.get(position);

        bundle.putString("pid", note.getPid());
        bundle.putString("pname", note.getPname());
        bundle.putString("pprice", String.valueOf(note.getPprice()));
        bundle.putString("pimg", note.getpImg());

//        fetchData()

        if(note.getPid().equals("product1"))
        {
            bundle.putString("variant", "White-blue");

            Bundle selectItemParams = new Bundle();
            selectItemParams.putString(FirebaseAnalytics.Param.ITEM_LIST_ID, "L001");
            selectItemParams.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Related products");
            selectItemParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                    new Parcelable[]{NikeAir});
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, selectItemParams);
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, selectItemParams);
        }
        else if(note.getPid().equals("product2"))
        {
            bundle.putString("variant", "white-pink");

            Bundle selectItemParams = new Bundle();
            selectItemParams.putString(FirebaseAnalytics.Param.ITEM_LIST_ID, "L001");
            selectItemParams.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Related products");
            selectItemParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                    new Parcelable[]{NikeJordan});
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, selectItemParams);
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, selectItemParams);
        }
        else if(note.getPid().equals("product3"))
        {
            bundle.putString("variant", "Yellow");

            Bundle selectItemParams = new Bundle();
            selectItemParams.putString(FirebaseAnalytics.Param.ITEM_LIST_ID, "L001");
            selectItemParams.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Related products");
            selectItemParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                    new Parcelable[]{NikeLight});
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, selectItemParams);
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, selectItemParams);
        }
        else if(note.getPid().equals("product4"))
        {
            bundle.putString("variant", "Green");

            Bundle selectItemParams = new Bundle();
            selectItemParams.putString(FirebaseAnalytics.Param.ITEM_LIST_ID, "L001");
            selectItemParams.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Related products");
            selectItemParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                    new Parcelable[]{NikeRunning});
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, selectItemParams);
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, selectItemParams);
        }

        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.mycart:
                startActivity(new Intent(getApplicationContext(), AddToCart.class));
                return true;
            case R.id.returnp:
                startActivity(new Intent(getApplicationContext(), ReturnProduct.class));
                return true;
            case R.id.myaccount:
                startActivity(new Intent(getApplicationContext(), MyAccount.class));
                return true;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}