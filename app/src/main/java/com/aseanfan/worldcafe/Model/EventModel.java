package com.aseanfan.worldcafe.Model;

public class EventModel {

    private Long event_id;
    private String urlImage;
    private String title;
    private Long price;
    private String detail;
    private int type;

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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
