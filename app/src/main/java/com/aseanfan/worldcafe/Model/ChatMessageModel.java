package com.aseanfan.worldcafe.Model;


import com.aseanfan.worldcafe.Utils.Utils;

public class ChatMessageModel {

    private String messageText;
    private int receive_id;
    private String usernameReceive;
    private String message_id;
    private String create_time;
    private int numnerline;
    private int type;// text,image,record
    private int relationship = 0;


    public ChatMessageModel() {
        // TODO Auto-generated constructor stub
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }


    public String getMessageText() {

        return messageText;
    }


    public String getUsernameReceive() {
        return usernameReceive;
    }

    public void setUsernameReceive(String username) {
        this.usernameReceive = username;
    }


    public String getCreate_day() {
        if (create_time == null)
            create_time = "";
        else {
            if (create_time.contains("GMT")) {
                return Utils.convertStringToLocalTime(create_time);
            }

        }

        return create_time;
    }


    public void setCreate_day(String create_day) {
        this.create_time = create_day;
    }


    public String getMessage_id() {
        return message_id;
    }


    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }


    public int getNumnerline() {
        return numnerline;
    }


    public void setNumnerline(int numnerline) {
        this.numnerline = numnerline;
    }

    /**
     * @return the receive_id
     */
    public int getReceive_id() {
        return receive_id;
    }

    /**
     * @param receive_id the receive_id to set
     */
    public void setReceive_id(int receive_id) {
        this.receive_id = receive_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public int getRelationship() {
        return relationship;
    }

    public void setRelationship(int relationship) {
        this.relationship = relationship;
    }
}