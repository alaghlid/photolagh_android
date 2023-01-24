package com.example.applicationgestionphotos_laghlid;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.applicationgestionphotos_laghlid.Handlers.DownloadPhotoHandler;
import com.example.applicationgestionphotos_laghlid.Model.Photo;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

/**
 * @author: Ayoub Laghlid
 * @Project: Gestion des Photos en utilsant l'api Flickr
 * 5A ASL
 **/

//  C'est l'activité principale de l'application
//  Il a également un dialogue (popup) qui s'ouvre lorsque l'utilisateur clique sur une image pour afficher les détails de l'image
//  Il y a également une option pour télécharger des images et afficher le dossier de téléchargement
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Dialog myDialog;

    @Override
    protected void onStart() {
        super.onStart();
        //  On récupère les préférences de l'utilisateur pour le thème dark, les notifications et les vibrations
        SharedPreferences sh = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean isDarkActivated = sh.getBoolean("isDarkActivated", false);
        if (isDarkActivated) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        // gestion de la navigation entre les différents fragments de l'application via un DrawerLayout et une NavigationView
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_search, R.id.nav_favorites, R.id.nav_settings, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();

        //  Gestion de la navigation
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        myDialog = new Dialog(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // pour le menu en haut à droite (téléchargement/photolagh) (main.xml)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_downloads:
                // Affichez le dossier photolagh qui contient les images téléchargées
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                String directoryPhotolagh = "/photolagh/";
                Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + directoryPhotolagh).getPath());
                intent.setDataAndType(uri, "image/png");
                startActivity(Intent.createChooser(intent, "Ouvrir le dossier de téléchargement"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // cette méthode est ajoutée comme fonction invokée après un clique sur une image (voir dans item_photo.xml : android:onClick="popupImageDetails")
    public void popupImageDetails(View v) {
        myDialog.setContentView(R.layout.popup_image);
        ImageButton download_button = myDialog.findViewById(R.id.download_button);
        Button openDownloadsButton = myDialog.findViewById(R.id.open_downloads_button);
        ImageView imageView = myDialog.findViewById(R.id.image_view);
        ImageButton close_button = myDialog.findViewById(R.id.close_button);
        Photo photo = (Photo) v.findViewById(R.id.image_view).getTag();
        String photoUrl = photo.getPhotoUrl();
        String title = photo.getTitle();
        Picasso.get().load(photoUrl).into(imageView);
        TextView photoTitle = myDialog.findViewById(R.id.title_text_view);
        photoTitle.setText(title.equals("") ? "Sans Titre" : title);

        //  On a une autre bouton dans le popup afficher le dossier de photolagh dans téléchargement
        openDownloadsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On crée une intention pour ouvrir le dossier photolagh qui est dans téléchargement
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                String directoryPhotolagh = "/photolagh/";
                Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + directoryPhotolagh).getPath());
                intent.setDataAndType(uri, "image/png");
                startActivity(Intent.createChooser(intent, "Ouvrir le dossier de téléchargement"));
            }
        });

        //  Pour fermer popup
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        //  On a une autre bouton dans le popup pour télécharger des images
        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadPhotoHandler(v.getContext(), photoUrl, title).execute(photoUrl);
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}


