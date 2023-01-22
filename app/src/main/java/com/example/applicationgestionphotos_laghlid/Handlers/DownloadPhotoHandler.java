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

public class DownloadPhotoHandler extends AsyncTask<String, Void, Bitmap> {
    private Context context;
    String photoTitle;
    String url;

    
    public DownloadPhotoHandler(Context context, String photoUrl, String photoTitle){
        this.context=context;
        this.photoTitle = photoTitle;
        this.url=photoUrl;

    }
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
