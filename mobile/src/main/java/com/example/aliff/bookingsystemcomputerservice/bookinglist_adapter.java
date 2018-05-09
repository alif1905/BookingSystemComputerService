package com.example.aliff.bookingsystemcomputerservice;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Aliff on 5/5/2018.
 */

public class bookinglist_adapter extends ArrayAdapter<InvoiceMode>  {
     String userid;

    public ArrayList<InvoiceMode> dataSet;
    Context mContext;
    String accesslevel;
    String CustId;

    ArrayList<String> rootValues = new ArrayList<String>();
    // View lookup cache
    public static class ViewHolder {
        TextView tvStatus;
        TextView tvBrandModel;


    }




    public bookinglist_adapter(ArrayList<InvoiceMode> data, Context context, String accesslevel, String CustId, String userid) {
            super(context, R.layout.bookinglist_row, data);
        this.dataSet = data;
        this.mContext = context;
        this.accesslevel=accesslevel;
        this.CustId=CustId;
        this.userid=userid;

    }
//
//       @Override
//     public void onClick(View v) {
//
//            int position=(Integer) v.getTag();
//            Object object= getItem(position);
//            InvoiceMode dataModel=(InvoiceMode) object;
//
//            switch (v.getId()) {
//
//               case R.id.tvBookingList:
//                // ListView Clicked item index
//                int itemPosition = position;
//
//
//                // ListView Clicked item value
//              //  String itemValue = (String) listView.getItemAtPosition(position);
//
//                // Show Alert
////                Toast.makeText(getApplicationContext(),
////                        "Position :" + itemPosition + "  ListItem : " + rootValues.get(itemPosition).toString(), Toast.LENGTH_LONG)
////                        .show();
//                   mContext.startActivity(new Intent(mContext, displayBooking.class).putExtra());
//
//
//                i.putExtra("CustID", rootValues.get(itemPosition));
//                i.putExtra("Value", itemValue);
//                i.putExtra("ACCESSLEVEL", accesslevel);
//                startActivity(i);
//                break;
//
//            }
////
////            case R.id.item_info:
////                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
////                        .setAction("No action", null).show();
////                break;
////        }
//       }

    public int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final InvoiceMode dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        com.example.aliff.bookingsystemcomputerservice.bookinglist_adapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new com.example.aliff.bookingsystemcomputerservice.bookinglist_adapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.bookinglist_row, parent, false);

            viewHolder.tvBrandModel = (TextView) convertView.findViewById(R.id.tvBookingList);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);


            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (com.example.aliff.bookingsystemcomputerservice.bookinglist_adapter.ViewHolder) convertView.getTag();
            result = convertView;

        }
        lastPosition = position;

        viewHolder.tvStatus.setText(dataModel.getStatus());
        viewHolder.tvBrandModel.setText((dataModel.getBrand() + " " + dataModel.getModel()));
        viewHolder.tvBrandModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int itemPosition = position;


                Intent i = new Intent(mContext, displayBooking.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("name", dataModel.getName());
                i.putExtra("address", dataModel.getAddress());
                i.putExtra("charge", dataModel.getChargeRm());
                i.putExtra("brand", dataModel.getBrand());
                i.putExtra("model", dataModel.getModel());
                i.putExtra("phone", dataModel.getPhoneNo());
                i.putExtra("service", dataModel.getService());
                i.putExtra("reason", dataModel.getReason());
                i.putExtra("repairedtype", dataModel.getRepairedType());
                i.putExtra("date", dataModel.getDate());
                i.putExtra("Value", dataModel.getBrand() + " " + dataModel.getModel());
            //    i.putExtra("CustID", CustId);
                i.putExtra("userid",userid);
                i.putExtra("ACCESSLEVEL", accesslevel);
                i.putExtra("CustID",  rootValues.get(itemPosition).toString());
                mContext.startActivity(i);
            }
        });

        // Return the completed view to render on screen
        return result;


    }

    }