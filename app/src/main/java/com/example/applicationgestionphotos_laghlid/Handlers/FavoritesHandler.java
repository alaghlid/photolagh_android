package com.example.applicationgestionphotos_laghlid.Handlers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.widget.ImageView;

import com.example.applicationgestionphotos_laghlid.R;
import com.example.applicationgestionphotos_laghlid.Database.PhotoDatabaseHelper;
import com.example.applicationgestionphotos_laghlid.Model.Photo;

/**
 * @author: Ayoub Laghlid
 * @Project: Gestion des Photos en utilsant l'api Flickr
 * 5A ASL
 **/

public class FavoritesHandler {
    private Context context;
    private SQLiteDatabase db;
    private ImageView favoriteButton;

    public FavoritesHandler(ImageView favouriteButton,
                            Context context) {
        this.favoriteButton = favouriteButton;
        this.context = context;
    }

    public void handleFavoriteButtonClick(Photo photo) {
        PhotoDatabaseHelper dbHelper = new PhotoDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        SharedPreferences sh = context.getSharedPreferences("Settings", MODE_PRIVATE);
        boolean isVibratorOnFavorites = sh.getBoolean("isVibratorOnFavorites", false);
        if (isVibratorOnFavorites) {
            Vibrator vibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrate.vibrate(200);
        }
        // On vérifie si la photo existe déjà
        if (!dbHelper.isExistsPhoto(photo)) {
            // La photo n'est pas enregistrée comme favorite, il faut l'ajouter à la table
            this.favoriteButton.setImageResource(R.drawable.favori_32p);
            if (dbHelper.addPhoto(photo)) {
                // Insertion réussie
                System.out.println("Insertion réussie ");
            } else {
                // Echec de l'insertion
                System.out.println("Echec de l'insertion ");
            }
        } else {
            // La photo est déjà enregistrée comme favorite, il faut la supprimer de la table
            favoriteButton.setImageResource(R.drawable.favori_32v);
            dbHelper.deletePhoto(photo);
            if (!dbHelper.isExistsPhoto(photo)) System.out.println("Supprimée avec succès ");
        }
        db.close();
    }

    public void handleFavoritesExists(Photo photo) {
        PhotoDatabaseHelper dbHelper = new PhotoDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        if (dbHelper.isExistsPhoto(photo)) this.favoriteButton.setImageResource(R.drawable.favori_32p);
        else this.favoriteButton.setImageResource(R.drawable.favori_32v);
    }
}
