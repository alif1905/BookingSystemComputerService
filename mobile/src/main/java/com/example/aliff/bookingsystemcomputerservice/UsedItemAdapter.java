package com.example.aliff.bookingsystemcomputerservice;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aliff on 21/5/2018.
 */

public class UsedItemAdapter extends RecyclerView.Adapter<UsedItemAdapter.ViewHolder> {
    private List<InvoiceModalInventory> items;
    private int itemLayout;
    private Context c;
    private int minteger = 0;
    private int sum;
    private int totalprice=0;
    private DatabaseReference myRef;
    private List<Double> itemsPrice = new ArrayList<>();
    private List<Double> itemsQuantity= new ArrayList<>();
    private String itemName;
    private String itemBrand;
    private String itemPrice;
    private String itemQuantity;

    public UsedItemAdapter(Context context, List<InvoiceModalInventory> items, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;
        this.c =context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }


    @Override public void onBindViewHolder(final ViewHolder holder, final int position) {
        final InvoiceModalInventory item = items.get(position);
        final int totalQuantity=Integer.parseInt(item.getItemQuantity());
        final int price=Integer.parseInt(item.getItemPrice());
        sum=totalQuantity;

        holder.itemName.setText(item.getItemName());
        holder.itemQuantity.setText(String.valueOf(sum));
        holder.count.setText(String.valueOf(minteger));
        holder.itemPrice.setText(String.valueOf(totalprice));
//        holder.supplier.setText(item.getDate());
//        holder.count.setVisibility(View.GONE);
        holder.itemView.setTag(item);



       holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            minteger=minteger+1;
            sum=totalQuantity-minteger;
            totalprice=price*minteger;

                holder.itemPrice.setText(String.valueOf(totalprice));
                holder.count.setText(String.valueOf(minteger));
                holder.itemQuantity.setText(String.valueOf(sum));
            }
        });

        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minteger=minteger-1;
                sum=totalQuantity-minteger;
                totalprice=price*minteger;
                holder.itemPrice.setText(String.valueOf(totalprice));
                holder.count.setText(String.valueOf(minteger));
                holder.itemQuantity.setText(String.valueOf(sum));

            }
        });


    }


    public void add(InvoiceModalInventory item, int position) {
        if(!items.contains(item)){
            itemsPrice.add(Double.parseDouble(item.getItemPrice()));
            items.add(position, item);
            notifyItemInserted(position);
        }
    }



    public void remove(InvoiceModalInventory item) {
        int position = items.indexOf(item);
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public int getTotalPrice(){
        int totalPrice = 0;

        for(int i=0; i<items.size(); i++){
      //      totalPrice = Integer.parseInt(items.get(i).itemPrice)*(Integer.parseInt(   items.get(i).String.valueOf(minteger)));
        }

        return totalPrice;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

      public TextView itemName,itemQuantity,count,itemPrice,itemPriceService,itemPriceTotal;
      public Button addBtn,minusBtn;
      public ImageButton delete;
        public ViewHolder(View itemView) {
            super(itemView);

            delete=(ImageButton) itemView.findViewById(R.id.deleteBtn);
            itemPrice=(TextView)itemView.findViewById(R.id.tvItemPriceCharge);
            count=(TextView)itemView.findViewById(R.id.tvQuantityInNeed);
           itemName =(TextView)itemView.findViewById(R.id.tvItemName);
           itemQuantity=(TextView)itemView.findViewById(R.id.tvItemQuantity);
           addBtn=(Button)itemView.findViewById(R.id.btnAddQuantity);
           minusBtn=(Button)itemView.findViewById(R.id.btnSubtractQuantity);


        }
    }

//
//    public void SubmitData() {
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//        if (accesslevel.equals("USER")) {
//            myRef = database.getReference().child("Bookings").child(userid).child(value);
//        } else {
//
//            myRef = database.getReference().child("Bookings").child(CustId).child(value);
//        }
//
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//
//
//
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//
//        });
//
//    }
}

