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

//  Classe qui se charge de l'affichage des photos dans la liste (utilisé pour l'acceuil et recherche)
//  Utilisation du ListView pour la gestion des photos affichées dans l'acceuil et dans la recherche

public class PhotoAdapter extends ArrayAdapter<Photo> {
    //  photos est la liste des photos trouvées
    private List<Photo> photos;

    //  context est le contexte de l'application
    private Context context;


    public PhotoAdapter(Context context, List<Photo> photos) {
        super(context, R.layout.item_photo, photos);
        this.photos = photos;
        this.context = context;
    }

    // Cette méthode est appelée pour chaque élément (item de la listeView) de photos à afficher.
    // Elle est utilisée pour créer ou réutiliser une vue pour chaque élément
    // position est l'index de la photo dans la liste de photos
    // convertView est la vue à réutiliser, si elle existe déjà
    // parent est le parent de la vue
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        // On vérifie s'il y a une vue existante, sinon en créer une nouvelle
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_photo, parent, false);
        }

        // on récupère la photo dont l'index est position
        Photo photo = photos.get(position);

        // C'est là qu'on doit mettre l'image affichée
        ImageView imageView = view.findViewById(R.id.image_view);

        // On ajoute la photo en tant que tag à l'image view pour une référence future (Popup utilise le tag l'image cliquée (voir MainActivity))
        imageView.setTag(photo);
        TextView titleTextView = view.findViewById(R.id.title_text_view);
        ImageButton favoriteButton = view.findViewById(R.id.favorite_button);

        // appelle la méthode pour vérifier si la photo est déjà en favoris pour changer icon pour voir dans la recherche les photos déjà ajoutés en Favoris
        new FavoritesHandler(favoriteButton, context).handleFavoritesExists(photo);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Avec cette méthode on gère l'ajout/suppression des photos en favoris
                handleFavoriteClick(favoriteButton, photo);
            }
        });
        ImageButton download_button = view.findViewById(R.id.download_button);
        //  on définit un listener sur le bouton de téléchargement des photos
        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadPhotoHandler(context, photo.getPhotoUrl(), photo.getTitle()).execute(photo.getPhotoUrl());
            } });

        // On charge l'image à partir de l'url de la photo en utilisant la librairie Picasso dans l'imageView
        Picasso.get().load(photo.getPhotoUrl()).into(imageView);

        // On définit le titre de la photo ( Si le titre est vide on le remplace par Sans titre pour éviter de l'afficher vide)
        titleTextView.setText(photo.getTitle().equals("") ? "Sans Titre" : photo.getTitle());

        return view;
    }

    //  Méthode utilisée pour gérer le processus d'ajout/suppression d'une photo en favoris lors d'un clique sur favoriteButton
    // photo est la photo à ajouter/supprimer en favoris
    private void handleFavoriteClick(ImageButton favoriteButton, Photo photo) {
        // Méthode pour gérer l'ajout/suppression en favoris
        new FavoritesHandler(favoriteButton, context).handleFavoriteButtonClick(photo);

        // on notifie les changements pour mettre à jour l'affichage
        notifyDataSetChanged();
    }
}