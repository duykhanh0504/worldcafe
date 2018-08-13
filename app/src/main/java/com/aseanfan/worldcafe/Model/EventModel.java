package com.aseanfan.worldcafe.Model;

import java.sql.Timestamp;
import java.util.List;

public class EventModel {

    private Long event_id;
    private List<String> urlImage;
    private String username;
    private String urlAvatar;
    private String title;
    private Long price;
    private String content;
    private int genre;
    private int numberComment;
    private int numberLike;
    private int islike;
    private Long account_id;
    private String start_time;
    private String city_name;


    public EventModel() {
    }

    public String getStarttime() {
        return start_time;
    }

    public void setStarttime(String start_time) {
        this.start_time = start_time;
    }


    public String getCityname() {
        return city_name;
    }

    public void setCityname(String city_name) {
        this.city_name = city_name;
    }

    public Long getEventid() {
        return event_id;
    }

    public void setEventid(Long event_id) {
        this.event_id = event_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }




    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public List<String> getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(List<String> urlImage) {
        this.urlImage = urlImage;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return genre;
    }

    public void setType(int genre) {
        this.genre = genre;
    }

    public int getNumberComment() {
        return numberComment;
    }

    public void setNumberComment(int numberComment) {
        this.numberComment = numberComment;
    }

    public int getNumberLike() {
        return numberLike;
    }

    public void setNumberLike(int numberLike) {
        this.numberLike = numberLike;
    }

    public int getIslike() {
        return islike;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }


    public Long getAccountid() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

}
