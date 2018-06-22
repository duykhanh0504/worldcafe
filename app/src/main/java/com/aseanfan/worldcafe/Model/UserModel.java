package com.aseanfan.worldcafe.Model;


public class UserModel {
    private Long account_id;

    private String username;
    private String email;
    private String phonenumber;
    private String avarta;

    public UserModel( String email, String mobile) {
        this.email = email;
        this.phonenumber = mobile;
    }

    public UserModel() {
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

}
