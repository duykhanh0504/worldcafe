package com.aseanfan.worldcafe.Model;

public class ChatModel {
    private Long account_id;
    private String username;
    private String avarta;
    private String time_diff;
    private String last_message;


    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long _account_id) {
        this.account_id = _account_id;
    }

    public String getTimediff() {
        return time_diff;
    }

    public void setTime_diff(String time_diff) {
        this.time_diff = time_diff;
    }

    public String getAvarta() {
        return avarta;
    }

    public void setAvarta(String _avarta) {
        this.avarta = _avarta;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String _username) {
        this.username = _username;
    }

    public String getLastMessage() {
        return last_message;
    }

    public void setLastMessage(String last_message) {
        this.last_message = last_message;
    }
}
