package com.example.applicationgestionphotos_laghlid.NetworkApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//  Classe utilisée pour la configuration de l'API Flickr
//  Elle contient les informations de base nécessaires pour se connecter à l'API, comme l'URL de base, la clé d'API et les méthodes :
//      Recherche et de récupération des photos récentes
public class ApiClient {
    private static final String BASE_URL = "https://www.flickr.com/";
    //  Attention la clé de l'api Flickr est personnel (je l'ai obtenue après avoir créer un compte dévelopeur)
    private static final String FLICKR_API_KEY = "cf67085354b1da9cf52323e49a42e574";
    private static final String METHOD_SEARCH = "flickr.photos.search";
    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    private static final String FORMAT = "json";
    private static Retrofit retrofit = null;

    //  La bibliothèque Retrofit est utilisée pour gérer les appels de l'API et la conversion de données JSON en objets Java
    //  getClient() est utilisée pour créer une instance de Retrofit avec les paramètres de configuration de base
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static String getApiKey() {
        return FLICKR_API_KEY;
    }

    public static String getMethodSearch() { return METHOD_SEARCH;}

    public static String getMethodGetRecent() { return METHOD_GET_RECENT; }

    public static String getFormat() { return FORMAT; }
}