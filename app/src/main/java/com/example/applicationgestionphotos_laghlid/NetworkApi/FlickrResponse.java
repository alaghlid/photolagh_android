package com.example.applicationgestionphotos_laghlid.NetworkApi;

import com.example.applicationgestionphotos_laghlid.Model.Photo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FlickrResponse {

    @SerializedName("photos")
    private Photos photos;

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public class Photos {

        @SerializedName("photo")
        private List<Photo> photoList;

        public List<Photo> getPhotoList() {
            return photoList;
        }
    }
}



