package com.example.e_commercedemoapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.helper.widget.MotionEffect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReturnProduct extends AppCompatActivity {
    private AppCompatImageView homeIcon;
    private AppCompatTextView enterproQty;
    private AppCompatEditText pid, returnQty;
    private AppCompatButton crbtn;
    FirebaseFirestore db;
    FirebaseAnalytics firebaseAnalytics;
    String returnType;
    String returnqty;
    String itemId;
    int initQty = 0;
    int finalQty = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_product);
        getSupportActionBar().hide();

        init();

        db = FirebaseFirestore.getInstance();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProductPage.class));
            }
        });



        crbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = pid.getText().toString();
                Toast.makeText(getApplicationContext(),id, Toast.LENGTH_SHORT).show();

                if(returnType.equals("full"))
                {
                    Bundle refundParams = new Bundle();
                    refundParams.putString(FirebaseAnalytics.Param.TRANSACTION_ID, id);
                    refundParams.putString(FirebaseAnalytics.Param.AFFILIATION, "Tatvic store");
                    refundParams.putString(FirebaseAnalytics.Param.CURRENCY, "INR");

                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.REFUND, refundParams);
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE_REFUND, refundParams);

                    db.collection("orders").document(id)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });
                }

                else if(returnType.equals("partial"))
                {
                    returnqty = returnQty.getText().toString();
                    db.collection("orders").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Toast.makeText(getApplicationContext(), documentSnapshot.getString("product name"), Toast.LENGTH_SHORT).show();
                            Bundle refundParams = new Bundle();
                            refundParams.putString(FirebaseAnalytics.Param.TRANSACTION_ID, id);
                            refundParams.putString(FirebaseAnalytics.Param.AFFILIATION, "Tatvic store");
                            refundParams.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                            refundParams.putString(FirebaseAnalytics.Param.ITEM_NAME, documentSnapshot.getString("product name"));

                            initQty = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("product quantity")));

                            db.collection("products4.4")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                if(task.getResult().isEmpty())
                                                {
                                                }
                                                else
                                                {
                                                    for (QueryDocumentSnapshot document: task.getResult())
                                                    {
                                                        Log.d(TAG, "Data came");
                                                        if(Objects.equals(document.getString("name"), documentSnapshot.getString("product name")))
                                                        {
                                                            itemId = document.getId();
                                                            Toast.makeText(getApplicationContext(),
                                                                    itemId, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
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

                            refundParams.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
                            refundParams.putLong(FirebaseAnalytics.Param.QUANTITY, Long.parseLong(returnqty));

                            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.REFUND, refundParams);

                            finalQty = initQty - Integer.parseInt(returnqty);

                            Map<String, String> qty = new HashMap<>();

                            qty.put("product quantity", String.valueOf(finalQty));


                            db.collection("orders").document(id).set(qty, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(MotionEffect.TAG, "Document snapshot successful");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(MotionEffect.TAG, "error in writing",e);
                                        }
                                    });

                        }
                    });

                }

            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.full:
                if (checked)
                    returnType = "full";
                    enterproQty.setVisibility(View.INVISIBLE);
                    returnQty.setVisibility(View.INVISIBLE);
                break;
            case R.id.partiall:
                if (checked)
                    returnType = "partial";
                    enterproQty.setVisibility(View.VISIBLE);
                    returnQty.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void init(){
        homeIcon = findViewById(R.id.homeIcon);
        pid = findViewById(R.id.pid);
        crbtn = findViewById(R.id.crbtn);
        enterproQty = findViewById(R.id.enterQtytxt);
        returnQty = findViewById(R.id.returnProQty);
    }
}