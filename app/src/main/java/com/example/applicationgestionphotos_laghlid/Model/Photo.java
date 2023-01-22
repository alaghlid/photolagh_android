package com.example.applicationgestionphotos_laghlid.Model;

/**
 * @author: Ayoub Laghlid
 * @Project: Gestion des Photos en utilsant l'api Flickr
 * 5A ASL
 **/

public class Photo {
    private String title;
    private String farm;
    private String server;
    private String id;
    private String secret;

    public Photo(String title, String farm, String server, String id, String secret) {
        this.title = title;
        this.farm = farm;
        this.server = server;
        this.id = id;
        this.secret = secret;
    }


    public String getTitle() {
        return title;
    }


    public String getFarm() {
        return farm;
    }


    public String getServer() {
        return server;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }


    public String getPhotoUrl() {
        return "https://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret + ".jpg";
    }

}

