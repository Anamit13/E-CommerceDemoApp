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
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Objects;

public class ProductPage extends AppCompatActivity implements NoteAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {


    private AppCompatImageView menuIc, sp1, profileIcon, sp2, sp3;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private AppCompatTextView head;
    private ArrayList<Note> arrayList;

    private FirebaseFirestore db;
    String utmSource = "chk";

    private FirebaseAnalytics firebaseAnalytics;
    String id;

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
        dynamicLink();
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        boolean x = mFirebaseRemoteConfig.getBoolean("update1");

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();

                            if(updated)
                            {

                            }
                            Toast.makeText(ProductPage.this, "Fetch and activate succeeded" +
                                            updated + x,
                                    Toast.LENGTH_SHORT).show();
                            if((x || updated) && (Objects.equals(utmSource, "google")))
                            {
                                sp2.setVisibility(View.GONE);
                                sp3.setVisibility(View.VISIBLE);
                                head.setText("49% Discounted products");
                            }

                        } else {
                            Toast.makeText(ProductPage.this, "Fetch failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        mFirebaseRemoteConfig.addOnConfigUpdateListener(new ConfigUpdateListener() {
            @Override
            public void onUpdate(ConfigUpdate configUpdate) {
                Log.d(TAG, "Updated keys: " + configUpdate.getUpdatedKeys());

                mFirebaseRemoteConfig.activate().addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                    }
                });
            }

            @Override
            public void onError(FirebaseRemoteConfigException error) {
                Log.w(TAG, "Config update error with code: " + error.getCode(), error);
            }
        });

        db = FirebaseFirestore.getInstance();
//        FacebookSdk.sdkInitialize(this);

        recyclerView = findViewById(R.id.prorecyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setHasFixedSize(true);
        arrayList = new ArrayList<>();

        fetchData();



        AppEventsLogger logger = AppEventsLogger.newLogger(this);

        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);



        logger.logEvent(AppEventsConstants.EVENT_NAME_ACTIVATED_APP);

        logger.logEvent("App Activated... Chill", 58);

        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE,"product_group"); //value will be a product_group
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID,"SKU_123"); //Passing product or content unique identifier
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY,"INR");  //value will be "INR"

        logger.logEvent(AppEventsConstants.EVENT_NAME_SEARCHED, 3999, params);


        profileIcon = findViewById(R.id.profileicon);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAnalytics.getAppInstanceId().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                id = s;
                Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
            }
        });

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

//        Bundle promoParams = new Bundle();
//        promoParams.putString(FirebaseAnalytics.Param.PROMOTION_ID, "SUMMER_FUN");
//        promoParams.putString(FirebaseAnalytics.Param.PROMOTION_NAME, "Summer Sale");
//        promoParams.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "summer2020_promo.jpg");
//        promoParams.putString(FirebaseAnalytics.Param.CREATIVE_SLOT, "featured_app_1");
//        promoParams.putString(FirebaseAnalytics.Param.LOCATION_ID, "HERO_BANNER");
//        promoParams.putString(FirebaseAnalytics.Param.VALUE, "viewpromotion");
//        promoParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
//                new Parcelable[]{NikeAir});

        // Promotion displayed
        //GA4 Implementation
        //firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_PROMOTION, promoParams);

        //GA3 implementation view promotion
        Bundle promoParams = new Bundle();
        promoParams.putString(Param.ITEM_ID, "SUMMER_FUN");
        promoParams.putString(Param.ITEM_NAME, "Summer Sale");
        promoParams.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "summer2020_promo.jpg");
        promoParams.putString("promoparam", "viewPromotion");
        promoParams.putString(FirebaseAnalytics.Param.CREATIVE_SLOT, "featured_app_1");

        ArrayList <Bundle>promotions = new ArrayList<Bundle>();
        promotions.add(promoParams);

        Bundle ecommerceBundle = new Bundle();
        ecommerceBundle.putParcelableArrayList("promotions", promotions );

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, ecommerceBundle );

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);


        sp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GA4 Implementation
                //firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_PROMOTION, promoParams);

                //GA3 select promotion
                ecommerceBundle.putString( Param.CONTENT_TYPE, "Internal Promotions" );
                ecommerceBundle.putString( Param.ITEM_ID, "SUMMER_FUN" );
                ecommerceBundle.putString("promoparam", "viewPromotion");
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, ecommerceBundle);

                startActivity(new Intent(getApplicationContext(), PromotionSale.class));


            }
        });
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = token;
                        Log.d(TAG, msg);
                        Toast.makeText(ProductPage.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void dynamicLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        Toast.makeText(getApplicationContext(), "Get link" + deepLink, Toast.LENGTH_SHORT).show();

                        Log.d("HERE_IS_DATA", "pendingData: "+pendingDynamicLinkData);

                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            utmSource = deepLink.getQueryParameter("utm_source");
                            Toast.makeText(getApplicationContext(), String.valueOf(deepLink), Toast.LENGTH_SHORT).show();

                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("HERE_IS_DATA", "addOnFailureListener: "+e.getMessage());

                        Toast.makeText(getApplicationContext(), "No Link", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "getDynamicLink:onFailure", e);
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

                                        //for GA3 implementation
                                        NikeAir.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                                        NikeAir.putLong(FirebaseAnalytics.Param.INDEX, 1);
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

                                        //for GA3 implementation
                                        NikeJordan.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                                        NikeJordan.putLong(FirebaseAnalytics.Param.INDEX, 2);
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

                                        //for GA3 implementation
                                        NikeLight.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                                        NikeLight.putLong(FirebaseAnalytics.Param.INDEX, 3);
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

                                        //for GA3 implementation
                                        NikeRunning.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                                        NikeRunning.putLong(FirebaseAnalytics.Param.INDEX, 4);
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

                                //GA3 implementation
                                ArrayList<Bundle> items = new ArrayList<Bundle>();

                                items.add(NikeAir);
                                items.add(NikeJordan);
                                items.add(NikeLight);
                                items.add(NikeRunning);

                                Bundle ecommerceBundle = new Bundle();
                                ecommerceBundle.putString("checkGA", "GA3");
                                ecommerceBundle.putParcelableArrayList( "items", items );

                                // Log view_search_results or view_item_list event with ecommerce bundle

                                firebaseAnalytics.logEvent( FirebaseAnalytics.Event.VIEW_ITEM_LIST, ecommerceBundle );

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
        sp2 = findViewById(R.id.sp2);
        sp3 = findViewById(R.id.sp3);
        head = findViewById(R.id.ourpopular);

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
                //GA4 Implementation
            Bundle selectItemParams = new Bundle();
            selectItemParams.putString(FirebaseAnalytics.Param.ITEM_LIST_ID, "L001");
            selectItemParams.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Related products");
            selectItemParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                    new Parcelable[]{NikeAir});
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, selectItemParams);

            //GA3 implementation
            Bundle ecommerceBundle = new Bundle();
            ecommerceBundle.putString("chkProduct", "selectProduct");
            ecommerceBundle.putBundle("items", NikeAir);

            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, ecommerceBundle);
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

            //GA3 implementation
            Bundle ecommerceBundle = new Bundle();
            ecommerceBundle.putString("chkProduct", "selectProduct");
            ecommerceBundle.putBundle("items", NikeJordan);

            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, ecommerceBundle);
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

            //GA3 implementation
            Bundle ecommerceBundle = new Bundle();
            ecommerceBundle.putString("chkProduct", "selectProduct");
            ecommerceBundle.putBundle("items", NikeLight);

            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, ecommerceBundle);
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

            //GA3 implementation
            Bundle ecommerceBundle = new Bundle();
            ecommerceBundle.putString("chkProduct", "selectProduct");
            ecommerceBundle.putBundle("items", NikeRunning);

            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, ecommerceBundle);
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