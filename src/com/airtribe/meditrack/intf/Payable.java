package com.airtribe.meditrack.intf;

public interface Payable {

    double calculateTotal();

    default void paymentStatus(){
        System.out.println("Amount Due: " + calculateTotal());
    }
}
