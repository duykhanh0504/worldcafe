package com.aseanfan.worldcafe.Model;

public class CommentModel {

    private Long _account_id;
    private String _avarta;
    private String _username;
    private String _content;
    private String _createtime;
    private String time_diff;


    public CommentModel() {
    }

    public String getTimeDiff() {
        return time_diff;
    }

    public void setTimeDiff(String time_diff) {
        this.time_diff = time_diff;
    }

    public Long getAccount_id() {
        return _account_id;
    }

    public void setAccount_id(Long _account_id) {
        this._account_id = _account_id;
    }


    public String getAvarta() {
        return _avarta;
    }

    public void setAvarta(String _avarta) {
        this._avarta = _avarta;
    }


    public String getUsername() {
        return _username;
    }

    public void setUsername(String _username) {
        this._username = _username;
    }


    public String getContent() {
        return _content;
    }

    public void setContent(String _content) {
        this._content = _content;
    }


    public String getCreatetime() {
        return _createtime;
    }

    public void setCreatetime(String _createtime) {
        this._createtime = _createtime;
    }


}
