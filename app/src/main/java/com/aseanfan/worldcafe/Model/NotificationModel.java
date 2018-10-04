package com.aseanfan.worldcafe.Model;

public class NotificationModel {
    private int _notifyid;
    private String _avarta;
    private String _title;
    private String _message;
    private String _createtime;
    private int _status;
    private Long _fromid;
    private int _type;
    private Long objectId;



    public NotificationModel() {
    }


    public int getNotifyid() {
        return _notifyid;
    }

    public void setNotifyid(int notifyid) {
        this._notifyid = notifyid;
    }


    public String getAvarta() {
        return _avarta;
    }

    public void setAvarta(String _avarta) {
        this._avarta = _avarta;
    }


    public String getTitle() {
        return _title;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }


    public String getMessage() {
        return _message;
    }

    public void setMessage(String _message) {
        this._message = _message;
    }


    public String getCreatetime() {
        return _createtime;
    }

    public void setCreatetime(String _createtime) {
        this._createtime = _createtime;
    }

    public int getStatus() {
        return _status;
    }

    public void setStatus(int _status) {
        this._status = _status;
    }

    public Long getFromid() {
        return _fromid;
    }

    public void setFromid(Long _status) {
        this._fromid = _fromid;
    }
    public int getType() {
        return _type;
    }

    public void setType(int _type) {
        this._type = _type;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }
}
