package com.example.applicationgestionphotos_laghlid.NetworkApi;

import com.example.applicationgestionphotos_laghlid.Model.Photo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//  Classe pour stocker la réponse obtenue lors d'une requête à l'API Flickr
//  Elle contient des informations sur les photos renvoyées par l'API, telles que la liste de photos, le nombre de pages et le nombre total de résultats
//  J'ai utilisé l'annotation SerializedName pour spécifier comment les données doivent être mappées à partir de la réponse JSON obtenue à partir de l'API
public class FlickrResponse {

    @SerializedName("photos")
    private Photos photos;

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    //  La classe interne Photos contient la liste de photos qui est récupérée à partir de la réponse JSON
    public class Photos {

        @SerializedName("photo")
        private List<Photo> photoList;

        public List<Photo> getPhotoList() {
            return photoList;
        }
    }
}



