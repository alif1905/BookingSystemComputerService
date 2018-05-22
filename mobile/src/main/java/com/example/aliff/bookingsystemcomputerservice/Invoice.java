package com.example.aliff.bookingsystemcomputerservice;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;

import java.util.ArrayList;

public class Invoice extends ArrayAdapter<InvoiceMode> implements View.OnClickListener{

    public ArrayList<InvoiceMode> dataSet;
    Context mContext;

    // View lookup cache
    public static class ViewHolder {
        TextView tvStatus;
        TextView tvDate;
        TextView tvItem;
        TextView tvService;
        TextView tvRm;
        Button invoice;
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
        final InvoiceMode dataModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

         View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.invoice_row, parent, false);

            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvItem = (TextView) convertView.findViewById(R.id.tvItem);
            viewHolder.tvService = (TextView) convertView.findViewById(R.id.tvService);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            viewHolder.tvRm = (TextView) convertView.findViewById(R.id.tvRm);
            viewHolder.invoice=(Button)convertView.findViewById(R.id.btnViewInvoice) ;
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Invoice.ViewHolder) convertView.getTag();
            result=convertView;

        }
        lastPosition = position;


        viewHolder.tvDate.setText(dataModel.getDate());
        viewHolder.tvItem.setText((dataModel.getModel()+" "+dataModel.getBrand()));
        viewHolder.tvService.setText(dataModel.getService());
        viewHolder.tvRm.setText("RM"+dataModel.getChargeRm());

        viewHolder.tvStatus.setText(dataModel.getStatus());
        viewHolder.invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof CustomerInvoice) {
                    ((CustomerInvoice)mContext).print(
                            dataModel.getBrand()+" "+
                                    dataModel.getModel(),
                            dataModel.getService(),
                            dataModel.getPhoneNo(),
                            dataModel.getChargeRm(),""

                    );
                }
            }
        });

        // Return the completed view to render on screen
        return result;
    }

}