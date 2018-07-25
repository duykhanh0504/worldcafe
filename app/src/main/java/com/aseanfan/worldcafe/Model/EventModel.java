package com.aseanfan.worldcafe.Model;

public class EventModel {

    private Long event_id;
    private String urlImage;
    private String urlAvatar;
    private String title;
    private Long price;
    private String content;
    private int genre;

    public EventModel() {
    }


    public Long getEventid() {
        return event_id;
    }

    public void setEventid(Long event_id) {
        this.event_id = event_id;
    }


    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlImage(String urlImage) {
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

}
