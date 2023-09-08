package com.example.firebaseconnection;

import java.util.List;

public class User {

    private String EmailId;
    private String MobileNo;
    private String Password;
    private List<String> selectedItems;
    private String date;
    private String time;



    private double total_bill;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String EmailId, String MobileNo, String Password, List<String> selectedItems, String date, String time, double total_bill) {
        this.EmailId = EmailId;
        this.MobileNo = MobileNo;
        this.Password = Password;
        this.selectedItems = selectedItems;
        this.date = date;
        this.time = time;
        this.total_bill=total_bill;
    }

    public String getEmailId() {
        return EmailId;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public String getPassword() {
        return Password;
    }

    public List<String> getSelectedItems() {
        return selectedItems;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getTotal_bill() {
        return total_bill;
    }


}
