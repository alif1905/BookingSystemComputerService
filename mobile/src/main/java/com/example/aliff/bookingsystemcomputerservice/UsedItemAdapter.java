package com.example.aliff.bookingsystemcomputerservice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aliff on 21/5/2018.
 */

public class UsedItemAdapter extends RecyclerView.Adapter<UsedItemAdapter.ViewHolder> {
    private List<InvoiceModalInventory> items;
    private int itemLayout;
    private Context c;
    private int minteger = 0;

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

        holder.itemName.setText(item.getItemName());
        holder.itemQuantity.setText(item.getItemQuantity());
        holder.count.setText(String.valueOf(minteger));
//        holder.supplier.setText(item.getDate());
//        holder.count.setVisibility(View.GONE);
        holder.itemView.setTag(item);



       holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            minteger=minteger+1;
                holder.count.setText(String.valueOf(minteger));

            }
        });

        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minteger=minteger-1;
                holder.count.setText(String.valueOf(minteger));


            }
        });

    }


    public void add(InvoiceModalInventory item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

      public TextView itemName,itemQuantity,count;
      public Button addBtn,minusBtn;
        public ViewHolder(View itemView) {
            super(itemView);

            count=(TextView)itemView.findViewById(R.id.tvQuantityInNeed);
           itemName =(TextView)itemView.findViewById(R.id.tvItemName);
           itemQuantity=(TextView)itemView.findViewById(R.id.tvItemQuantity);
           addBtn=(Button)itemView.findViewById(R.id.btnAddQuantity);
           minusBtn=(Button)itemView.findViewById(R.id.btnSubtractQuantity);


        }
    }}

