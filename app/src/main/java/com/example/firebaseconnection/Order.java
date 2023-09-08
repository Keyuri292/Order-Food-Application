package com.example.firebaseconnection;

import java.util.List;

public class Order {
    private String orderId;
    private List<String> selectedItems;

    // Empty constructor for Firebase
    public Order() {
    }

    public Order(String orderId, List<String> selectedItems) {
        this.orderId = orderId;
        this.selectedItems = selectedItems;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<String> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<String> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
