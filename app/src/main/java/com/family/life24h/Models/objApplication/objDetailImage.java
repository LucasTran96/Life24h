package com.family.life24h.Models.objApplication;

/*
 *  Date created: 12/16/2019
 *  Last updated: 12/16/2019
 *  Name project: Life24h
 *  Description:
 *  Auth: James Ryan
 */

public class objDetailImage {

    private String username;
    private String urlImage;
    private long timeCreate;

    public objDetailImage() {
    }

    public objDetailImage(String username, String urlImage, long timeCreate) {
        this.username = username;
        this.urlImage = urlImage;
        this.timeCreate = timeCreate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public long getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(long timeCreate) {
        this.timeCreate = timeCreate;
    }

}
