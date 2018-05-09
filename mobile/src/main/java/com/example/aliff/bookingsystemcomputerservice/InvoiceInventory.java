package com.example.aliff.bookingsystemcomputerservice;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.example.aliff.bookingsystemcomputerservice.InvoiceModalInventory;
//
//import java.util.ArrayList;
//
///**
// * Created by Aliff on 26/4/2018.
// */
//
//public class InvoiceInventory extends ArrayAdapter<InvoiceModalInventory> implements View.OnClickListener{
public class InvoiceInventory{

//    public ArrayList<InvoiceModalInventory> dataSet;
//    Context mContext;
//
//    // View lookup cache
//    public static class ViewHolder {
//        TextView tvItemName;
//        TextView tvItemBrand;
//        TextView tvItemPrice;
//        TextView tvItemQuantity;
//        TextView tvItemAvailable;
//
//    }
//
//    public InvoiceInventory(ArrayList<InvoiceModalInventory> data, Context context) {
//        super(context, R.layout.invoiceinventory_row, data);
//        this.dataSet = data;
//        this.mContext=context;
//
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        int position=(Integer) v.getTag();
//        Object object= getItem(position);
//        InvoiceModalInventory dataModel=(InvoiceModalInventory) object;
//
////        switch (v.getId())
////        {
////            case R.id.item_info:
////                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
////                        .setAction("No action", null).show();
////                break;
////        }
//    }
//
//    public int lastPosition = -1;
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the data item for this position
//        InvoiceModalInventory dataModel = getItem(position);
//        // Check if an existing view is being reused, otherwise inflate the view
//        ViewHolder viewHolder; // view lookup cache stored in tag
//
//        final View result;
//
//        if (convertView == null) {
//
//            viewHolder = new ViewHolder();
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.invoiceinventory_row, parent, false);
//
//            viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
//            viewHolder.tvItemBrand = (TextView) convertView.findViewById(R.id.tvItemBrand);
//            viewHolder.tvItemPrice = (TextView) convertView.findViewById(R.id.tvItemPrice);
//            viewHolder.tvItemQuantity = (TextView) convertView.findViewById(R.id.tvItemQuantity);
//            viewHolder.tvItemAvailable = (TextView) convertView.findViewById(R.id.tvItemAvailable);
//
//
//            result=convertView;
//            convertView.setTag(viewHolder);
//        } else {
//
//            viewHolder = (InvoiceInventory.ViewHolder) convertView.getTag();
//
//            result=convertView;
//        }
//        lastPosition = position;
//
//
//
//        viewHolder.tvItemName.setText("Dummy Name");
//        viewHolder.tvItemBrand.setText("Dummy Brand");
//        viewHolder.tvItemPrice.setText("Dummy Price");
//        viewHolder.tvItemQuantity.setText("Dummy Quantity");
//        viewHolder.tvItemAvailable.setText("Dummy Quantity Available");
//        // Return the completed view to render on screen
//        return result;
//    }
}