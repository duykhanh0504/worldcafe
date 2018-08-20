package com.aseanfan.worldcafe.Model;

public class AccountModel {
    private Long account_id;
    private String username;
    private String phonenumber;
    private String avarta;
    private int sex;
    private String email;

    public AccountModel() {
    }


    public Long getId() {
        return account_id;
    }

    public void setId(Long account_id) {
        this.account_id = account_id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String username) {
        this.phonenumber = phonenumber;
    }


    public String getAvarta() {
        return avarta;
    }

    public void setAvarta(String avarta) {
        this.avarta = avarta;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

}
