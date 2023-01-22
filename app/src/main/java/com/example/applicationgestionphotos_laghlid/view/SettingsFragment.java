package com.example.applicationgestionphotos_laghlid.view;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.applicationgestionphotos_laghlid.R;

/**
 * @author: Ayoub Laghlid
 * @Project: Gestion des Photos en utilsant l'api Flickr
 * 5A ASL
 **/

public class SettingsFragment extends Fragment {
    private Switch themeSwitch;
    private Switch vibrateSwitch;
    private Switch notificationsSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        themeSwitch = view.findViewById(R.id.theme_switch);
        vibrateSwitch = view.findViewById(R.id.vibrate_switch);
        notificationsSwitch = view.findViewById(R.id.notifications_switch);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Settings", MODE_PRIVATE);
        boolean vibrate = sharedPreferences.getBoolean("isVibratorOnFavorites", false);
        boolean dark = sharedPreferences.getBoolean("isDarkActivated", false);
        boolean notifications = sharedPreferences.getBoolean("isNotificationsActivated", false);
        vibrateSwitch.setChecked(vibrate);
        themeSwitch.setChecked(dark);
        themeSwitch.setChecked(notifications);
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("Settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isDarkActivated", isChecked);
                editor.apply();
                if (isChecked)  {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Vibrator vibrate = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrate.vibrate(200);
                }
                else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
        vibrateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("Settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isVibratorOnFavorites", isChecked);
                editor.apply();
                if (isChecked) {
                    Vibrator vibrate = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrate.vibrate(200);
                }
            }
        });
        notificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("Settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isNotificationsActivated", isChecked);
                editor.apply();
                Vibrator vibrate = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrate.vibrate(200);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Settings", MODE_PRIVATE);
        boolean vibrate = sharedPreferences.getBoolean("isVibratorOnFavorites", false);
        boolean dark = sharedPreferences.getBoolean("isDarkActivated", false);
        boolean notifications = sharedPreferences.getBoolean("isNotificationsActivated", false);
        vibrateSwitch.setChecked(vibrate);
        themeSwitch.setChecked(dark);
        notificationsSwitch.setChecked(notifications);
    }
}
