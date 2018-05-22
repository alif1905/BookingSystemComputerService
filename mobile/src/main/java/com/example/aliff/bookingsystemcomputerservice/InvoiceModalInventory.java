package com.example.aliff.bookingsystemcomputerservice;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Aliff on 26/4/2018.
 */
@IgnoreExtraProperties
public class InvoiceModalInventory {

    public String itemName;
    public String itemBrand;
    public String itemPrice;
    public String itemQuantity;




    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }



    public InvoiceModalInventory( ) {
    }


    public InvoiceModalInventory(String itemName, String itemBrand, String itemPrice, String itemQuantity
                                 ) {


        this.itemName = itemName;
        this.itemBrand = itemBrand;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;

    }
}