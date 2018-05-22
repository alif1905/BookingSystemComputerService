package com.example.aliff.bookingsystemcomputerservice;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aliff on 21/5/2018.
 */

public class UsedItemAdapter extends ArrayAdapter<InvoiceModalInventory> {
    String userid;

    public ArrayList<InvoiceModalInventory> dataSet;
    Context mContext;
    String accesslevel;

    ArrayList<String> rootValues = new ArrayList<String>();
    // View lookup cache
    public static class ViewHolder {
        TextView tvItem;
        TextView tvQuantity;
        TextView tvQuantityInNeed;

    }




    public UsedItemAdapter(ArrayList<InvoiceModalInventory> data, Context context) {
        super(context, R.layout.activity_diagnose_adapter, data);
        this.dataSet = data;
        this.mContext = context;
    }

    public int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final InvoiceModalInventory dataModel = getItem(position);
        UsedItemAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new UsedItemAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_diagnose, parent, false);

            viewHolder.tvItem = (TextView) convertView.findViewById(R.id.tvItemName);
            viewHolder.tvQuantity = (TextView) convertView.findViewById(R.id.tvItemQuantity);


            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UsedItemAdapter.ViewHolder) convertView.getTag();
            result = convertView;

        }
        lastPosition = position;

        viewHolder.tvItem.setText(dataModel.getItemName());
        viewHolder.tvQuantity.setText(dataModel.getItemQuantity());



        // Return the completed view to render on screen
        return result;


    }

}
