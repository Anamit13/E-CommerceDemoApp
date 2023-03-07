package com.example.e_commercedemoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    private Context context;
    private ArrayList<OrderModel> arrayList;
    private static OrderAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OrderAdapter.OnItemClickListener listener) {
        OrderAdapter.listener = (OrderAdapter.OnItemClickListener) listener;
    }

    public OrderAdapter(){}

    public OrderAdapter(Context context, ArrayList<OrderModel> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);
        return new OrderAdapter.OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        OrderModel orderModel = arrayList.get(position);
        holder.orderProname.setText(orderModel.getpName());
        holder.orderprosize.setText(orderModel.getpSize());
        holder.orderProQty.setText(orderModel.getpQty());
        holder.orderPrice.setText(orderModel.gettPrice());
        holder.orderPayment.setText(orderModel.getPaymentMethod());
        holder.orderTier.setText(orderModel.getsTier());
        holder.orderDelivery.setText(orderModel.getDeliveryDate());
        holder.orderId.setText(orderModel.getOrderId());

        Glide.with(context).load(orderModel.getpImg()).into(holder.orderProimg);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class OrderHolder extends RecyclerView.ViewHolder{

        private AppCompatImageView orderProimg;
        private AppCompatTextView orderProname, orderprosize, orderProQty, orderPrice, orderPayment, orderTier, orderDelivery, orderId;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            orderProimg = itemView.findViewById(R.id.orderProimg);
            orderProname = itemView.findViewById(R.id.orderProname);
            orderprosize = itemView.findViewById(R.id.orderProsize);
            orderProQty = itemView.findViewById(R.id.orderQty);
            orderPrice = itemView.findViewById(R.id.orderTotal);
            orderPayment = itemView.findViewById(R.id.orderPay);
            orderTier = itemView.findViewById(R.id.ordertier);
            orderDelivery = itemView.findViewById(R.id.orderDelivery);
            orderId = itemView.findViewById(R.id.orderId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
