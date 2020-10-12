package com.sawadevelopers.gofix_app;

public class Car {
    private String car_id;
    private String title;
    private String engine;
    private String tank;
    private String image1;
    private String image2;
    private String image3;
    private Double daily;
    private Double monthly;
    private Double user;
    private double rent_id;
    private String phone,address,hourp;

    public Car(String car_id,String title, String engine, String tank, String image1, String image2, String image3, Double daily, Double monthly, Double user,Double rent_id,String phone,String address,String hourp){

        this.car_id =car_id;
        this.title = title;
        this.engine = engine;
        this.tank = tank;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.daily = daily;
        this.monthly = monthly;
        this.user = user;
        this.rent_id = rent_id;
        this.phone = phone;
        this.address = address;
        this.hourp = hourp;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getTitle() {
        return title;
    }

    public String getEngine() {
        return engine;
    }

    public Double getUser() {
        return user;
    }

    public double getRent_id() {
        return rent_id;
    }

    public void setRent_id(double rent_id) {
        this.rent_id = rent_id;
    }

    public void setUser(Double user) {
        this.user = user;
    }

    public String getTank() {
        return tank;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getImage3() {
        return image3;
    }

    public Double getDaily() {
        return daily;
    }

    public Double getMonthly() {
        return monthly;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public void setTank(String tank) {
        this.tank = tank;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public void setDaily(Double daily) {
        this.daily = daily;
    }

    public void setMonthly(Double monthly) {
        this.monthly = monthly;
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

    public String getHourp() {
        return hourp;
    }

    public void setHourp(String hourp) {
        this.hourp = hourp;
    }
}
