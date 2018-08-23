package com.aseanfan.worldcafe.Model;

public class RequestUserModel {
    private Long account_id;

    private String username;
    private String avarta;
    private int id;
    private int status;


    public RequestUserModel()
    {
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




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getAvarta() {
        return avarta;
    }

    public void setAvarta(String avarta) {
        this.avarta = avarta;
    }



    public int getRequestId() {
        return id;
    }

    public void setRequestId(int id) {
        this.id = id;
    }




}
