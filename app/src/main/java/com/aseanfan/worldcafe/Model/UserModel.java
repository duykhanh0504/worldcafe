package com.aseanfan.worldcafe.Model;


public class UserModel {
    private Long id;
    private String identifier;
    private String email;
    private String mobile;
    private String gender;

    public UserModel(String identifier, String email, String mobile, String gender) {
        this.identifier = identifier;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender;
    }

    public UserModel() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
