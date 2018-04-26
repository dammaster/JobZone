package com.appolom.jobzone;

/**
 * Created by daniel on 2018-03-22.
 */

public class Item {
    String name;
    String detailText;
    String money;
    String category;
    String phoneNumber;
    String postalCode;
    String date;
    double lat;
    double lng;



    public Item() {}

    public Item(String name, String category, String detailText, String money, String phoneNumber,double lat,double lng,String postalCode,String date) {
        this.name = name;
        this.category = category;
        this.detailText = detailText;
        this.money = money;
        this.phoneNumber = phoneNumber;
        this.lat = lat;
        this.lng = lng;
        this.postalCode = postalCode;
        this.date = date;


    }


}


