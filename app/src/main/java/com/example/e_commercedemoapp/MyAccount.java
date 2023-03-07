package com.example.e_commercedemoapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyAccount extends AppCompatActivity implements OrderAdapter.OnItemClickListener{

    private AppCompatImageView backIcon;
    private RecyclerView orderrv;
    private OrderAdapter adapter;
    private ArrayList<OrderModel> arrayList;

    private FirebaseFirestore db;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        init();

        orderrv.setLayoutManager(new LinearLayoutManager(this));
        orderrv.setHasFixedSize(true);
        arrayList = new ArrayList<>();

        fetchData();
    }

    private void fetchData(){
        db.collection("orders")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().isEmpty())
                            {
                                orderrv.setVisibility(View.GONE);
                            }
                            else
                            {
                                orderrv.setVisibility(View.VISIBLE);
                                for (QueryDocumentSnapshot document: task.getResult())
                                {
                                    Log.d(TAG, "Data came");

                                    OrderModel orderModel = new OrderModel(document.getId(), document.getString("delivery date"), document.getString("payment method"),
                                            document.getString("product image"), document.getString("product name"), document.getString("product quantity"),
                                            document.getString("product size"), document.getString("shipping tier"), document.getString("total price"));
                                    arrayList.add(orderModel);


                                }
                                adapter = new OrderAdapter(getApplicationContext(), arrayList);
                                orderrv.setAdapter(adapter);
                                adapter.setOnItemClickListener(MyAccount.this::onItemClick);
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
        backIcon = findViewById(R.id.backIcon);
        orderrv = findViewById(R.id.orderrv);
    }

    @Override
    public void onItemClick(int position) {

    }
}