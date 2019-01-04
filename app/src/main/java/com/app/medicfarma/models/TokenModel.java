package com.app.medicfarma.models;

public class TokenModel {

    private String outh_name;
    private String sender_id;
    private String refresh_token;

    public String getOuth_name() {
        return outh_name;
    }

    public void setOuth_name(String outh_name) {
        this.outh_name = outh_name;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getRefresh_token() { return refresh_token; }

    public void setRefresh_token(String refresh_token) { this.refresh_token = refresh_token; }

}
