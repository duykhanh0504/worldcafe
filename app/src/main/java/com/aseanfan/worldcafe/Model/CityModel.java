package com.aseanfan.worldcafe.Model;

public class CityModel {
    private int city_id;
    private String city_name;

    public CityModel(int id ,String name)
    {
        this.city_id = id;
        this.city_name =name;
    }


    public int getid() {
        return this.city_id;
    }

    public void setid(int id) {
        this.city_id = id;
    }

    public String getname() {
        return this.city_name;
    }

    public void setname(String name) {
        this.city_name = name;
    }
}
