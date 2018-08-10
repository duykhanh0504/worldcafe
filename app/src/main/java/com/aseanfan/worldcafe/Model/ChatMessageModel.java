package com.aseanfan.worldcafe.Model;


import com.aseanfan.worldcafe.Utils.Utils;

public class ChatMessageModel {

    private Long message_id;
    private String message;
    private String received_accounts;
    private Long send_account;
    private String create_date;
    private Long receiver_accounts;
    private Long room_id;
    private int type;


    public ChatMessageModel() {
        // TODO Auto-generated constructor stub
    }


    public Long getSend_account() {
        return send_account;
    }

    public void setGroupid(Long groupid) {
        this.room_id = groupid;
    }

    public Long getGroupid() {
        return room_id;
    }

    public void setSend_account(Long send_account) {
        this.send_account = send_account;
    }

    public void setMessageText(String messageText) {
        this.message = messageText;
    }


    public String getMessageText() {

        return message;
    }


    public String getCreate_day() {
        return create_date;
    }


    public void setCreate_day(String create_day) {
        this.create_date = create_day;
    }


    public Long getMessage_id() {
        return message_id;
    }


    public void setMessage_id(Long message_id) {
        this.message_id = message_id;
    }


    public Long getReceiver() {
        return receiver_accounts;
    }


    public void setReceiver(Long receiver_accounts) {
        this.receiver_accounts = receiver_accounts;
    }


    public String getReceived() {
        return received_accounts;
    }


    public void setReceived(String received_accounts) {
        this.received_accounts = received_accounts;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}