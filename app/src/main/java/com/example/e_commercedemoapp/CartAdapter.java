package com.example.e_commercedemoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.e_commercedemoapp.AddToCart.arrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<CartModel> cartModelArrayList;
    private FirebaseAnalytics firebaseAnalytics;
    Bundle global;

    static int flag = -1;

    public CartAdapter(Context context, ArrayList<CartModel> cartModelArrayList) {
        this.context = context;
        this.cartModelArrayList = cartModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());

        CartModel cartModel = cartModelArrayList.get(position);
        holder.cartPname.setText(cartModel.getProname());
        holder.cartPprice.setText(String.valueOf(cartModel.getProprice()));
        Glide.with(context).load(cartModel.getProimg()).into(holder.cartProimg);
        holder.proQty.setText(String.valueOf(cartModel.getProQty()));
        holder.proSize.setText(cartModel.getProsize());

        ((AddToCart) context).totalPrice.setText(String.valueOf(getTotalAmount()));



        holder.deletePro.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {

                switch (cartModel.getProname()){
                    case "Nike Air 1.5":
                        global = ProductPage.NikeAir;
                        break;
                    case "Nike jordan":
                        global = ProductPage.NikeJordan;
                        break;
                    case "Nike light 1.5":
                        global = ProductPage.NikeLight;
                        break;
                    case "Nike running":
                        global = ProductPage.NikeRunning;
                        break;
                    default:
                        global = new Bundle();
                        break;
                }

                Bundle removeCartParam = new Bundle();
                removeCartParam.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                removeCartParam.putDouble(FirebaseAnalytics.Param.VALUE, Double.parseDouble(String.valueOf(cartModel.getProQty() * cartModel.getProprice())));
                removeCartParam.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                        new Parcelable[]{global});

                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART, removeCartParam);

                //GA3 Implementation
                Bundle GA3_removeCart = new Bundle();
                GA3_removeCart.putString("checkGA", "GA3");
                GA3_removeCart.putBundle("items", global);
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART, GA3_removeCart);

                //FB PIXEL IMPLEMENTATION
                AppEventsLogger logger = AppEventsLogger.newLogger(context.getApplicationContext());
                Bundle params = new Bundle();

                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE,"product"); //value will be a product as in the UI single product is getting removed

                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID,"SKU_123"); //Passing product or content identifier
                params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY,"INR");
                params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS,2);//Number Of Items
                logger.logEvent("remove_from_cart", 7998, params); //value - pass value of the product


                arrayList.remove(cartModel);
                notifyDataSetChanged();
                ((AddToCart) context).totalPrice.setText(String.valueOf(getTotalAmount()));
            }
        });

        holder.addqty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = cartModel.getProQty();
                cartModel.setProQty(qty+1);
                notifyDataSetChanged();
                ((AddToCart) context).totalPrice.setText(String.valueOf(getTotalAmount()));
                flag = 1;
            }
        });

        holder.removeqty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = cartModel.getProQty();
                if(qty>1){
                    cartModel.setProQty(qty-1);
                    notifyDataSetChanged();
                    ((AddToCart) context).totalPrice.setText(String.valueOf( getTotalAmount()));
                    flag = 0;
                }
            }
        });
    }

    public double getTotalAmount() {
        double total = 0;
        for (CartModel model : cartModelArrayList) {
            total += model.getProQty() * model.getProprice();
        }
        return total;
    }

    @Override
    public int getItemCount() {
        return cartModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView cartPname, cartPprice, proQty, proSize;
        private AppCompatImageView cartProimg, addqty, removeqty, deletePro;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cartPname = itemView.findViewById(R.id.cartProname);
            cartPprice = itemView.findViewById(R.id.cartProprice);
            proQty = itemView.findViewById(R.id.proqty);
            cartProimg = itemView.findViewById(R.id.cartProimg);
            addqty = itemView.findViewById(R.id.addprobtn);
            removeqty = itemView.findViewById(R.id.removeprobtn);
            deletePro = itemView.findViewById(R.id.deletepro);
            proSize = itemView.findViewById(R.id.proSize);

        }
    }
}
