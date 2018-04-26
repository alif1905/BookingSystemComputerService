package com.example.aliff.bookingsystemcomputerservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;

import java.util.ArrayList;

public class Invoice extends ArrayAdapter<InvoiceMode> implements View.OnClickListener{

    public ArrayList<InvoiceMode> dataSet;
    Context mContext;

    // View lookup cache
    public static class ViewHolder {
        TextView tvName;
        TextView tvDate;
        TextView tvItem;
        TextView tvService;
        TextView tvRm;

    }

    public Invoice(ArrayList<InvoiceMode> data, Context context) {
        super(context, R.layout.invoice_row, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        InvoiceMode dataModel=(InvoiceMode) object;

//        switch (v.getId())
//        {
//            case R.id.item_info:
//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//                break;
//        }
    }

   public int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        InvoiceMode dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.invoice_row, parent, false);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvItem = (TextView) convertView.findViewById(R.id.tvItem);
            viewHolder.tvService = (TextView) convertView.findViewById(R.id.tvService);
            viewHolder.tvRm = (TextView) convertView.findViewById(R.id.tvRm);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        lastPosition = position;


        viewHolder.tvDate.setText(dataModel.getDate());
        viewHolder.tvName.setText("Dummy Name");
        viewHolder.tvItem.setText((dataModel.getModel()+" "+dataModel.getBrand()));
        viewHolder.tvService.setText(dataModel.getService());
        viewHolder.tvRm.setText("RM 20");

        // Return the completed view to render on screen
        return convertView;
    }
}