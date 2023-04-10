package com.example.e_commercedemoapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), ProductPage.class);

//                try {
//                    PackageInfo info = getPackageManager().getPackageInfo(
//                            "com.example.e_commercedemoapp",
//                            PackageManager.GET_SIGNATURES);
//                    for (Signature signature : info.signatures) {
//                        MessageDigest md = MessageDigest.getInstance("SHA");
//                        md.update(signature.toByteArray());
//                        Log.d("KeyHash", "KeyHash:" + Base64.encodeToString(md.digest(),
//                                Base64.DEFAULT));
//                        Toast.makeText(getApplicationContext(), Base64.encodeToString(md.digest(),
//                                Base64.DEFAULT), Toast.LENGTH_LONG).show();
//                    }
//                } catch (Exception e) {
//                    Log.d("KeyHash", e.toString());
//                }

                startActivity(intent);
                finish();
            }
        }, 3000);







    }
}