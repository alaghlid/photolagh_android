package com.example.applicationgestionphotos_laghlid.Handlers;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


// Classe pour télécharger une photo à partir d'une URL donnée en utilisant la classe DownloadManager

public class DownloadPhotoHandler extends AsyncTask<String, Void, Bitmap> {
    private Context context;
    String photoTitle;
    String url;

    public DownloadPhotoHandler(Context context, String photoUrl, String photoTitle){
        this.context=context;
        this.photoTitle = photoTitle;
        this.url=photoUrl;

    }

    //  Dans cette méthode, on utilise la classe URL pour créer une URL à partir de l'URL passée en paramètre
    //  ensuite, on ouvre une connexion à cette URL, on lit les données de l'image et on utilise BitmapFactory pour décoder ces données en un bitmap
    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            this.url=urls[0];
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //  Dans cette méthode, on vérifie si le bitmap est non null, et si c'est le cas on utilise la classe DownloadManager pour télécharger la photo
    //  On vérifie en utilisant les sharedPreferences (contient la configuration choisie dans Paramètres : dark, vibreur, notification)
    //  si les notifications sont activées, on met la visibilité de la notification à VISIBILITY_VISIBLE_NOTIFY_COMPLETED pour voir la notiifcation sur le téléphone à la fin de téléchargement
    //  Pour le répértoire de destination pour le téléchargement de la photo c'est photolagh qui est dans le dossier de téléchrgements
    //  (le dosssier photolagh est créé par défaut s'il n'existe pas)
    // Le Toast est pour afficher un message de confirmation que la photo a été téléchargée avec succès.

    @SuppressLint("WrongThread")
    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            FileOutputStream out = null;
            try {
                String directoryPhotolagh = "/photolagh/";
                DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(this.url);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                SharedPreferences sh = context.getSharedPreferences("Settings", MODE_PRIVATE);
                boolean isNotificationsActivated = sh.getBoolean("isNotificationsActivated", false);
                if (isNotificationsActivated) request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, directoryPhotolagh+photoTitle+".png");
                Long reference = downloadManager.enqueue(request);
                Toast.makeText(context, photoTitle + " est téléchargé avec succès ! Veuillez consulter les téléchargements", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
