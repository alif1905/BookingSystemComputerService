package com.example.aliff.bookingsystemcomputerservice;



public class InvoiceMode {
    public String Date;
    public String Address;
    public String Model;
    public String PhoneNo;
    public String PickupTime;
    public String Service;
    public String Brand;
    public String Reason;
    public String Status;

    public boolean isAccepted;
    public boolean isUpdated;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getPickupTime() {
        return PickupTime;
    }

    public void setPickupTime(String pickupTime) {
        PickupTime = pickupTime;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public InvoiceMode() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public InvoiceMode(String status, String address, String model, String phoneNo,
                       String pickupTime, String service, String brand, String date,
                       String reason, boolean isAccepted, boolean isUpdated) {

        this.Status = status;
        this.Date = date;
        this.Address = address;
        this.Model = model;
        this.PhoneNo = phoneNo;
        this.PickupTime = pickupTime;
        this.Service = service;
        this.Brand = brand;
        this.Reason = reason;
        this.isAccepted = isAccepted;
        this.isUpdated = isUpdated;

    }
}
