package com.sawadevelopers.gofix_app;

public class Driver {
    private String driver_id;
    private  String driver_name;
    private  String idnumber;
    private  String dlnumber;
    private  String phone;
    private  String category;
    private  String adress;
    private  String created_at;
    private String image;

    public Driver(String driver_id, String driver_name, String idnumber, String dlnumber, String phone, String category, String adress, String created_at, String image) {
        this.driver_id = driver_id;
        this.driver_name = driver_name;
        this.idnumber = idnumber;
        this.dlnumber = dlnumber;
        this.phone = phone;
        this.category = category;
        this.adress = adress;
        this.created_at = created_at;
        this.image = image;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getDlnumber() {
        return dlnumber;
    }

    public void setDlnumber(String dlnumber) {
        this.dlnumber = dlnumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
