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

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Dialog myDialog;

    @Override
    protected void onStart() {
        super.onStart();
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_search, R.id.nav_favorites, R.id.nav_settings, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_downloads:
                //Affichez le fragment de l'accueil
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

        // Ajouter des listners de clic sur les boutons
        openDownloadsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Créer une intention pour ouvrir le dossier de téléchargement
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                String directoryPhotolagh = "/photolagh/";
                Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + directoryPhotolagh).getPath());
                intent.setDataAndType(uri, "image/png");
                startActivity(Intent.createChooser(intent, "Ouvrir le dossier de téléchargement"));
            }
        });

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

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