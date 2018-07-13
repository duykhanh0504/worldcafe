package com.aseanfan.worldcafe.Model;

import java.util.List;

public class PostTimelineModel {

    private Long timeline_id;
    private List<String> urlImage;
    private String urlAvatar;
    private String title;
    private String detail;
    private int numberComment;
    private int numberLike;
    private int type;
    private int islike;
    private Long account_id;

    public PostTimelineModel() {
    }

    public Long getEventid() {
        return timeline_id;
    }

    public void setEventid(Long event_id) {
        this.timeline_id = event_id;
    }


    public List<String> getUrlImage() {
        return urlImage;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public String getUrlAvatar() {
        return urlAvatar;
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


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getIslike() {
        return islike;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getAccountid() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

}
