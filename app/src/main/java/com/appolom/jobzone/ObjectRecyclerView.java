package com.appolom.jobzone;

/**
 * Created by daniel on 2018-03-27.
 */

public class ObjectRecyclerView {
    String detailText;
    String money;
    String name;
    String category;
    String phoneNumber;
    String date;
    double lat;
    double lng;


    public ObjectRecyclerView(){ }
    public ObjectRecyclerView(String category, String detailText, String money, String phoneNumber,double lat,double lng,String date,String name) {
        this.category = category;
        this.detailText = detailText;
        this.money = money;
        this.phoneNumber = phoneNumber;
        this.lat = lat;
        this.lng = lng;
        this.date = date;
        this.name = name;

    }



    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getDetailText() {
        return detailText;
    }
    public void setDetailText(String detailText) {
        this.detailText = detailText;
    }

    public String getMoney() {
        return money;
    }
    public void setMoney(String money) {
        this.money = money;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getdate() {
        return date;
    }
    public void setdate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


}





