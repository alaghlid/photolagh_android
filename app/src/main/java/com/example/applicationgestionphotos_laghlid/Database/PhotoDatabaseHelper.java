package com.example.applicationgestionphotos_laghlid.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.applicationgestionphotos_laghlid.Model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Ayoub Laghlid
 * @Project: Gestion des Photos en utilsant l'api Flickr
 * 5A ASL
 **/

//  Classe utilisée pour gérer la base de données SQLite pour les photos favorites (Ajout et suppression)

public class PhotoDatabaseHelper extends SQLiteOpenHelper {
    // Les constantes nécessaires pour la création et la mise à jour de la table de photos favoris
    // nom et la version de la base de données
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "photos.db";

    // Les colonnes de la photo
    private static final String TABLE_NAME = "favorites";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_FARM = "farm";
    private static final String COLUMN_SERVER = "server";
    private static final String COLUMN_SECRET = "secret";


    public PhotoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //  Méthode pour créer la table des favoris dans la base de données
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_FARM + " TEXT, " +
                COLUMN_SERVER + " TEXT, " +
                COLUMN_SECRET + " TEXT" + ")";
        db.execSQL(createTable);
    }

    //  Méthode pour mettre à jour la base de données en supprimant la table des favoris existante et en créant une nouvelle table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //  Méthode pour ajouter une photo à la table de photos favoris en utilisant les informations de la photo passée en paramètre
    public boolean addPhoto(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, photo.getId());
        contentValues.put(COLUMN_URL, photo.getPhotoUrl());
        contentValues.put(COLUMN_TITLE, photo.getTitle());
        contentValues.put(COLUMN_FARM, photo.getFarm());
        contentValues.put(COLUMN_SERVER, photo.getServer());
        contentValues.put(COLUMN_SECRET, photo.getSecret());
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    //  Méthode pour supprimer une photo de la table de photos favoris en utilisant l'ID de la photo passée en paramètre
    public void deletePhoto(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(photo.getId()) });
        db.close();
    }

    //  Méthode pour vérifier si une photo existe déjà dans la table de photos favoris en utilisant l'ID de la photo passée en paramètre
    public boolean isExistsPhoto(Photo photo) {
                SQLiteDatabase db = this.getWritableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[] { String.valueOf(photo.getId()) });
        boolean exists = (cursor.getCount() > 0);
                cursor.close();
        return exists;
    }

    //  Méthode pour récupèrer toutes les photos de la table de photos favoris et les retourner sous forme de liste de photos
    //  Cette liste de photos est utilisé par les adapters pour affichées les éléments
    //  Soit dans RecyclerView pour les favoris ou dans la listeView pour l'acceuil et la recherche
    public List<Photo> getAllPhotos() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        List<Photo> photos = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String url = cursor.getString(cursor.getColumnIndex(COLUMN_URL));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                @SuppressLint("Range") String farm = cursor.getString(cursor.getColumnIndex(COLUMN_FARM));
                @SuppressLint("Range") String server = cursor.getString(cursor.getColumnIndex(COLUMN_SERVER));
                @SuppressLint("Range") String secret = cursor.getString(cursor.getColumnIndex(COLUMN_SECRET));

                // Ajout de tous les objets photo dans photos
                photos.add(new Photo(title, farm, server, id, secret));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return photos;
    }
}

