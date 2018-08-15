package com.aseanfan.worldcafe.Model;


public class UserModel {
    private Long account_id;

    private String username;
    private String email;
    private String phonenumber;
    private String avarta;
    private String cover;
    private int sex;
    private String birthday;
    private String address;
    private String district;
    private int city;
    private int country;
    private String company;
    private String school;
    private String introduction;
    private int status;
    private String facebook_id;
    private int v_followed;
    private int v_follower;



    public UserModel( String email, String mobile) {
        this.email = email;
        this.phonenumber = mobile;
    }

    public UserModel() {
    }

    public String getFacebookid() {
        return facebook_id;
    }

    public void setFacebookid(String facebookid) {
        this.facebook_id = facebookid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getFollowed() {
        return v_followed;
    }

    public void setFollowed(int v_followed) {
        this.v_followed = v_followed;
    }

    public int getFollower() {
        return v_follower;
    }

    public void setFollower(int v_follower) {
        this.v_follower = v_follower;
    }



}
