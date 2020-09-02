package com.sawadevelopers.gofix_app;

public class spare {
    private double spareid;
    private String sparename;
    private double spareprice;
    private String spareimage;
    private double userid;
    private String sparedescription;
    private String phone,address;

    public spare(double spare_id,String spare_name,double spare_price,String spare_image,double user_id,String spare_desc,String phone, String address){
        this.spareid = spare_id;
        this.sparename = spare_name;
        this.spareprice = spare_price;
        this.spareimage = spare_image;
        this.userid = user_id;
        this.sparedescription = spare_desc;
        this.phone = phone;
        this.address = address;
    }

    public double getSpareid() {
        return spareid;
    }

    public void setSpareid(double spareid) {
        this.spareid = spareid;
    }

    public String getSparename() {
        return sparename;
    }

    public void setSparename(String sparename) {
        this.sparename = sparename;
    }

    public double getSpareprice() {
        return spareprice;
    }

    public void setSpareprice(double spareprice) {
        this.spareprice = spareprice;
    }

    public String getSpareimage() {
        return spareimage;
    }

    public void setSpareimage(String spareimage) {
        this.spareimage = spareimage;
    }

    public double getUserid() {
        return userid;
    }

    public void setUserid(double userid) {
        this.userid = userid;
    }

    public String getSparedescription() {
        return sparedescription;
    }

    public void setSparedescription(String sparedescription) {
        this.sparedescription = sparedescription;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
