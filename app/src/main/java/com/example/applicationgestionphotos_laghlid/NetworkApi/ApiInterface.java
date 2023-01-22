package com.example.applicationgestionphotos_laghlid.NetworkApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

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

