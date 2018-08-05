package com.aseanfan.worldcafe.Model;

import java.util.List;

public class AreaModel {
    private int country_id;
    private String country_name;
    private List<CityModel> listcity;

    public AreaModel(int id ,String name , List<CityModel> list)
    {
        this.country_id = id;
        this.country_name =name;
        this.listcity = list;
    }



    public int getid() {
        return this.country_id;
    }

    public void setid(int id) {
        this.country_id = id;
    }

    public String getname() {
        return this.country_name;
    }

    public void setname(String name) {
        this.country_name = name;
    }

    public List<CityModel> getListCity() {
        return this.listcity;
    }

    public void setListCity(List<CityModel> name) {
        this.listcity = name;
    }
}
