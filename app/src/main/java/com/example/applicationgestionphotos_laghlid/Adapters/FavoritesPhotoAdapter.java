package com.example.applicationgestionphotos_laghlid.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationgestionphotos_laghlid.Handlers.DownloadPhotoHandler;
import com.example.applicationgestionphotos_laghlid.R;
import com.example.applicationgestionphotos_laghlid.Database.PhotoDatabaseHelper;
import com.example.applicationgestionphotos_laghlid.Model.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author: Ayoub Laghlid
 * @Project: Gestion des Photos en utilsant l'api Flickr
 * 5A ASL
 **/

    //  Classe pour l'adapter de la liste des photos favoris (utilisé pour favoris)
    //  Utilisation du RecyclerView pour la gestion des photos affichées dans favoris

public class FavoritesPhotoAdapter extends RecyclerView.Adapter<FavoritesPhotoAdapter.FavoritesViewHolder> {
    private List<Photo> favoritesPhotos;
    private static Context context;


    public FavoritesPhotoAdapter(Context context, List<Photo> favoritesPhotos) {
        // context le contexte de l'application
        this.context = context;

        // la liste des photos favoris
        this.favoritesPhotos = favoritesPhotos;
    }


    // Méthode appelée pour créer un nouveau ViewHolder pour un élément de la liste
    // parent est le parent qui contiendra le ViewHolder créé
    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Création de la vue pour chaque élément de la liste
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);

        return new FavoritesViewHolder(view, favoritesPhotos, this);
    }


    // Méthode appelée pour remplir les données d'un ViewHolder pour un élément de la liste
    // holder est le ViewHolder à remplir
    // position la position de l'élément dans la liste
    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        Photo photo = favoritesPhotos.get(position);
        // Bind les données pour chaque élément de la liste
        holder.bind(photo);
    }

    // Méthode qui retourne le nombre d'éléments dans la liste
    @Override
    public int getItemCount() {
        return favoritesPhotos.size();
    }

    // Classe interne pour le ViewHolder des éléments de la liste
    static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        private ImageView photoPhoto;
        private ImageButton favoriteButton;
        private List<Photo> favoritesPhotos;
        private TextView titlePhoto;
        private FavoritesPhotoAdapter adapter;
        private ImageButton download_button;

        // itemView est la vue de l'élément de la liste
        // favouritesPhotos est la liste des photos favoris
        // adapter est l'adapter de la liste
        public FavoritesViewHolder(@NonNull View itemView,List<Photo> favouritesPhotos, FavoritesPhotoAdapter adapter) {
            super(itemView);
            // Initialisation des vues pour chaque élément
            photoPhoto = itemView.findViewById(R.id.image_view);
            favoriteButton = itemView.findViewById(R.id.favorite_button);
            download_button = itemView.findViewById(R.id.download_button);
            titlePhoto = itemView.findViewById(R.id.title_text_view);
            this.adapter = adapter;
            this.favoritesPhotos = favouritesPhotos;
        }

        public void bind(final Photo photo) {
            // On charge l'image à partir de l'url de la photo en utilisant la librairie Picasso
            Picasso.get().load(photo.getPhotoUrl()).into(photoPhoto);

            // On ajoute la photo en tant que tag à l'image view pour une référence future (Popup utilise le tag l'image cliquée (voir MainActivity))
            photoPhoto.setTag(photo);

            // On définit le titre de la photo ( Si le titre est vide on le remplace par Sans titre au lieu de le laisser vide)
            titlePhoto.setText(photo.getTitle().equals("") ? "Sans Titre" : photo.getTitle());

            // La photo est en favoris donc on doit utilisé l'icon de favoris pleine
            favoriteButton.setImageResource(R.drawable.favori_24p);

            // On ajoute un listener pour le bouton favoris pour intéragir avec la suppression des images de favoris
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // on récupère la position de l'élément
                    int position = getAdapterPosition();

                    // On récupère les préférences de l'application pour savoir si la vibration est activée ou non (pour le clique sur le boutton des favoris des photos)
                    SharedPreferences sh = context.getSharedPreferences("Settings", MODE_PRIVATE);
                    boolean isVibratorOnFavorites = sh.getBoolean("isVibratorOnFavorites", false);
                    if (isVibratorOnFavorites) {
                        Vibrator vibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrate.vibrate(200);
                    }
                    //  ici, on utilise la classe PhotoDatabaseHelper pour supprimer la photo de la base de données
                    //  on récupère une instance de la base de données en écriture
                    //  on utilise la méthode deletePhoto pour supprimer la photo de la base de données
                    PhotoDatabaseHelper dbHelper = new PhotoDatabaseHelper(context);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    dbHelper.deletePhoto(photo);
                    
                    //  on ferme la connexion à la base de données
                    db.close();

                    //  on supprime la photo de la liste des photos favorites
                    favoritesPhotos.remove(position);

                    //  on notifie le RecyclerView qu'un élément a été supprimé
                    adapter.notifyItemRemoved(position);

                    //  on notifie le RecyclerView qu'il y a eu un changement sur tous les éléments à partir de la position de l'élément supprimé
                    adapter.notifyItemRangeChanged(position, favoritesPhotos.size());
                }
            });
            //  on définit un listener sur le bouton de téléchargement des photos
            download_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  pour lancer la tâche asynchrone de téléchargement de la photo en utilisant la classe DownloadPhotoHandler
                    new DownloadPhotoHandler(context, photo.getPhotoUrl(), photo.getTitle()).execute(photo.getPhotoUrl());
                } });
        }
    }
}
