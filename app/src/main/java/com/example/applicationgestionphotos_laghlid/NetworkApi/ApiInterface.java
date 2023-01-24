package com.example.applicationgestionphotos_laghlid.NetworkApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//  Classe définit l'interface ApiInterface qui contient les méthodes pour effectuer des requêtes HTTP vers l'API Flickr
//  Les méthodes searchPhotos et getRecentPhotos utilisent les annotations GET pour indiquer qu'il s'agit d'une requête GET et @Query pour passer les paramètres nécessaires pour la requête
//  Les méthodes retournent un objet Call de type FlickrResponse qui sera utilisé pour traiter la réponse de l'API
public interface ApiInterface {
    @GET("services/rest/")
    Call<FlickrResponse> searchPhotos(@Query("method") String method,
                                      @Query("format") String format,
                                      @Query("api_key") String apiKey,
                                      @Query("nojsoncallback") int callback,
                                      @Query("text") String tags);
    @GET("services/rest/")
    Call<FlickrResponse> getRecentPhotos(@Query("method") String method,
                                      @Query("format") String format,
                                      @Query("api_key") String apiKey,
                                      @Query("nojsoncallback") int callback);

}

