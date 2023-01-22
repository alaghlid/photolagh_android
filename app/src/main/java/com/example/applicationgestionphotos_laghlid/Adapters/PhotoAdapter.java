package com.example.applicationgestionphotos_laghlid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.applicationgestionphotos_laghlid.Handlers.DownloadPhotoHandler;
import com.example.applicationgestionphotos_laghlid.Handlers.FavoritesHandler;
import com.example.applicationgestionphotos_laghlid.R;
import com.example.applicationgestionphotos_laghlid.Model.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author: Ayoub Laghlid
 * @Project: Gestion des Photos en utilsant l'api Flickr
 * 5A ASL
 **/

// Classe qui se charge de l'affichage des photos dans la liste (utilisé pour l'acceuil et recherche)

public class PhotoAdapter extends ArrayAdapter<Photo> {
    private List<Photo> photos;
    private Context context;
    private ImageButton download_button;

    /**
     * Constructeur de l'adapter
     *
     * @param context le contexte de l'application
     * @param photos la liste des photos trouvées
     */
    public PhotoAdapter(Context context, List<Photo> photos) {
        super(context, R.layout.item_photo, photos);
        this.photos = photos;
        this.context = context;
    }

    /**
     * Cette méthode est appelée pour chaque élément de la liste de photos à afficher. Elle est utilisée pour créer ou réutiliser une vue pour chaque élément.
     *
     * @param position l'index de l'élément dans la liste de photos
     * @param convertView la vue à réutiliser, si elle existe déjà
     * @param parent le parent de la vue
     * @return la vue créée ou réutilisée pour afficher l'élément de la liste
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        // On vérifie s'il y a une vue existante, sinon en créer une nouvelle
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_photo, parent, false);
        }

        Photo photo = photos.get(position);

        ImageView imageView = view.findViewById(R.id.image_view);

        // On ajoute la photo en tant que tag à l'image view pour une référence future (popup en utilisant le tag l'image cliquée)
        imageView.setTag(photo);
        TextView titleTextView = view.findViewById(R.id.title_text_view);
        ImageButton favoriteButton = view.findViewById(R.id.favorite_button);

        // appelle la méthode pour vérifier si la photo est déjà en favoris pour changer icon
        new FavoritesHandler(favoriteButton, context).handleFavoritesExists(photo);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appeler une méthode pour gérer l'ajout/suppression en favoris
                handleFavoriteClick(favoriteButton, photo);
            }
        });
        download_button = view.findViewById(R.id.download_button);
        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadPhotoHandler(context, photo.getPhotoUrl(), photo.getTitle()).execute(photo.getPhotoUrl());
            } });

        // on utilise Picasso pour charger l'image à partir de l'URL
        Picasso.get().load(photo.getPhotoUrl()).into(imageView);

        // On définit le titre de la photo ( Si le titre est vide on le remplace par Sans titre au lieu de le laisser vide)
        titleTextView.setText(photo.getTitle().equals("") ? "Sans Titre" : photo.getTitle());

        return view;
    }

    /**
     * Cette méthode est utilisée pour gérer le processus d'ajout/suppression d'une photo en favoris
     *
     * @param favoriteButton le bouton favori à gérer
     * @param photo la photo à ajouter/supprimer en favoris
     */

    private void handleFavoriteClick(ImageButton favoriteButton, Photo photo) {
        // Méthode pour gérer l'ajout/suppression en favoris
        new FavoritesHandler(favoriteButton, context).handleFavoriteButtonClick(photo);

        // on notifie les changements pour mettre à jour l'affichage
        notifyDataSetChanged();
    }
}