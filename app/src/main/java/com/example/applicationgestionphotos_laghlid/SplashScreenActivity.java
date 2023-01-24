package com.example.applicationgestionphotos_laghlid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * @author: Ayoub Laghlid
 * @Project: Gestion des Photos en utilsant l'api Flickr
 * 5A ASL
 **/


//  Ce fragment est un écran de chargement (splash screen) qui est utilisé lorsque l'application est lancée
//  on utilise un handler pour afficher l'écran pendant une seconde avant de passer à l'activité principale (MainActivity)
//  on récupère les paramètres de thème de l'utilisateur (Shared preferences)
//  @SuppressLint("CustomSplashScreen") est utilisée pour supprimer tout avertissement de performance lié à l'utilisation d'un écran de chargement personnalisé
@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sh =  getSharedPreferences("Settings", MODE_PRIVATE);
        boolean isDarkActivated =  sh.getBoolean("isDarkActivated", false);
        if (isDarkActivated)  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.splash_screen);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
