package com.example.applicationgestionphotos_laghlid.NetworkApi;

import android.os.AsyncTask;

import com.example.applicationgestionphotos_laghlid.Adapters.PhotoAdapter;
import com.example.applicationgestionphotos_laghlid.Model.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//  Cette Classe est une tâche AsyncTask qui permet de rechercher des photos à partir de l'API Flickr en utilisant Retrofit
//  Lorsque l'utilisateur effectue une recherche, une requête HTTP est envoyée à l'API Flickr en utilisant les méthodes de l'interface ApiInterface
//  Les résultats de la recherche sont ensuite convertis en objets Java à l'aide de la bibliothèque GSON
//  La tâche AsyncTask met à jour la liste de photos de l'application en utilisant la classe PhotoAdapter et notifie le changement de données pour afficher les résultats de la recherche à l'utilisateur
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
        //  Si la query est vide on utilise getRecent pour obtenir les photos récentes
        //  Sinon on effectue une recherche en utilisant la méthode search
        Call<FlickrResponse> call = query.equals("") ? apiService.getRecentPhotos(methodGetRecent, format, apiKey, 1) : apiService.searchPhotos(methodSearch, format, apiKey, 1, query);
        call.enqueue(new Callback<FlickrResponse>() {

            @Override
            public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                // On ajoute les photos si et seulement si la réponse est en succès et que l'obtient un objet de réponse qui n'est pas vide
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
}


