package com.example.applicationgestionphotos_laghlid.Model;

/**
 * @author: Ayoub Laghlid
 * @Project: Gestion des Photos en utilsant l'api Flickr
 * 5A ASL
 **/


//  La classe Photo représente un objet photo qui est utilisé pour stocker les détails d'une photo téléchargée à partir de l'API Flickr
public class Photo {
    //  Les détails nécessaire de la photo : le titre de la photo, le numéro de farm, le numéro de serveur, l'identifiant unique de la photo (id) et le secret de la photo
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

    //  Méthode pour génèree l'URL de la photo en utilisant ces détails pour qu'on peut soit :
    //      Télécharger la photo en local
    //      Afficher la photo dans l'imageView en utilisant Picasso
    public String getPhotoUrl() {
        return "https://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret + ".jpg";
    }
}

