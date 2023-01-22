package com.example.applicationgestionphotos_laghlid.NetworkApi;

import android.os.AsyncTask;

import com.example.applicationgestionphotos_laghlid.Adapters.PhotoAdapter;
import com.example.applicationgestionphotos_laghlid.Model.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerformSearchTask extends AsyncTask<String, Void, List<Photo>> {
    private ApiInterface apiService;
    private String apiKey;
    private String methodSearch;
    private String methodGetRecent;
    private String format;
    private List<Photo> photos;
    private PhotoAdapter adapter;

    public PerformSearchTask(List<Photo> photos, PhotoAdapter adapter) {
        this.photos = photos;
        this.adapter = adapter;
        apiService = ApiClient.getClient().create(ApiInterface.class);
        apiKey = ApiClient.getApiKey();
        methodSearch = ApiClient.getMethodSearch();
        methodGetRecent = ApiClient.getMethodGetRecent();
        format = ApiClient.getFormat();
    }

    @Override
    protected List<Photo> doInBackground(String... strings) {
        String query = strings[0];
        Call<FlickrResponse> call = query.equals("") ? apiService.getRecentPhotos(methodGetRecent, format, apiKey, 1) : apiService.searchPhotos(methodSearch, format, apiKey, 1, query);
        call.enqueue(new Callback<FlickrResponse>() {

            @Override
            public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {

                if (response.isSuccessful() && response.body() != null && response.body().getPhotos() != null) {
                    photos.clear();
                    photos.addAll(response.body().getPhotos().getPhotoList());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<FlickrResponse> call, Throwable t) {
                // Handle failure
            }
        });
        return photos;
    }

// Supprimer la méthode onPostExecute car elle n'est plus nécessaire maintenant
// que les mises à jour de l'interface utilisateur se font directement dans la méthode onResponse.

}


