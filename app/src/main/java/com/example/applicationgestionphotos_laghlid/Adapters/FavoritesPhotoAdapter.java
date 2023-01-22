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

public class FavoritesPhotoAdapter extends RecyclerView.Adapter<FavoritesPhotoAdapter.FavoritesViewHolder> {
    private List<Photo> favoritesPhotos;
    private static Context context;

    /**
     * Constructeur de l'adapter
     *
     * @param context le contexte de l'application
     * @param favoritesPhotos la liste des photos favoris
     */
    public FavoritesPhotoAdapter(Context context, List<Photo> favoritesPhotos) {
        this.context = context;
        this.favoritesPhotos = favoritesPhotos;
    }

    /**
     * Méthode appelée pour créer un nouveau ViewHolder pour un élément de la liste
     *
     * @param parent le parent qui contiendra le ViewHolder créé
     * @param viewType le type de vue (non utilisé dans ce cas)
     * @return un nouveau ViewHolder pour un élément de la liste
     */
    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Création de la vue pour chaque item de la liste
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);

        return new FavoritesViewHolder(view, favoritesPhotos, this);
    }

    /**
     * Méthode appelée pour remplir les données d'un ViewHolder pour un élément de la liste
     *
     * @param holder le ViewHolder à remplir
     * @param position la position de l'élément dans la liste
     */
    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        // Bind les données pour chaque item de la liste
        Photo photo = favoritesPhotos.get(position);
        holder.bind(photo);
    }

    /**
     * Méthode qui retourne le nombre d'éléments dans la liste
     *
     * @return le nombre d'éléments dans la liste
     */
    @Override
    public int getItemCount() {
        return favoritesPhotos.size();
    }

    /**
     * Classe interne pour le ViewHolder des éléments de la liste
     *
     */
    static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        private ImageView photoPhoto;
        private ImageButton favoriteButton;
        private List<Photo> favoritesPhotos;
        private TextView titlePhoto;
        private FavoritesPhotoAdapter adapter;
        private ImageButton download_button;

        /**
         * Constructeur pour le ViewHolder
         * @param itemView la vue de l'élément de la liste
         * @param favouritesPhotos la liste des photos favoris
         * @param adapter l'adapter de la liste
         */
        public FavoritesViewHolder(@NonNull View itemView,List<Photo> favouritesPhotos, FavoritesPhotoAdapter adapter) {
            super(itemView);
            // Initialisation des vues pour chaque item
            photoPhoto = itemView.findViewById(R.id.image_view);
            favoriteButton = itemView.findViewById(R.id.favorite_button);
            download_button = itemView.findViewById(R.id.download_button);
            titlePhoto = itemView.findViewById(R.id.title_text_view);
            this.adapter = adapter;
            this.favoritesPhotos = favouritesPhotos;
        }

        public void bind(final Photo photo) {
            // On charge l'image à partir de l'url de la photo
            Picasso.get().load(photo.getPhotoUrl()).into(photoPhoto);

            // On ajoute la photo en tant que tag à l'image view pour une référence future (popup en utilisant le tag l'image cliquée)
            photoPhoto.setTag(photo);

            // On définit le titre de la photo ( Si le titre est vide on le remplace par Sans titre au lieu de le laisser vide)
            titlePhoto.setText(photo.getTitle().equals("") ? "Sans Titre" : photo.getTitle());

            // On définit l'icône de favori pour l'image
            favoriteButton.setImageResource(R.drawable.favori_24p);

            // On ajoute un listener pour le bouton favori
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // on récupère la position de l'item
                    int position = getAdapterPosition();

                    // On récupère les préférences de l'application pour savoir si la vibration est activée lors de l'ajout aux favoris
                    SharedPreferences sh = context.getSharedPreferences("Settings", MODE_PRIVATE);
                    boolean isVibratorOnFavorites = sh.getBoolean("isVibratorOnFavorites", false);
                    if (isVibratorOnFavorites) {
                        Vibrator vibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrate.vibrate(200);
                    }
                    //  ici, on utilise la classe PhotoDatabaseHelper pour supprimer la photo de la base de données
                    PhotoDatabaseHelper dbHelper = new PhotoDatabaseHelper(context);
                    
                    //  on récupère une instance de la base de données en écriture
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    
                    //  on utilise la méthode deletePhoto pour supprimer la photo de la base de données
                    dbHelper.deletePhoto(photo);
                    
                    //  on ferme la connexion à la base de données
                    db.close();

                    //  on supprime la photo de la liste des photos favorites
                    favoritesPhotos.remove(position);

                    //  on utilise notifyItemRemoved pour notifier le RecyclerView qu'un élément a été supprimé
                    adapter.notifyItemRemoved(position);

                    //  on utilise notifyItemRangeChanged pour notifier le RecyclerView qu'il y a eu un changement sur tous les éléments à partir de la position de l'élément supprimé
                    adapter.notifyItemRangeChanged(position, favoritesPhotos.size());
                }
            });
            //  on définit un listener sur le bouton de téléchargement
            download_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  pour lancer la tâche asynchrone de téléchargement de la photo en utilisant la classe DownloadPhotoHandler
                    new DownloadPhotoHandler(context, photo.getPhotoUrl(), photo.getTitle()).execute(photo.getPhotoUrl());
                } });
        }
    }
}
