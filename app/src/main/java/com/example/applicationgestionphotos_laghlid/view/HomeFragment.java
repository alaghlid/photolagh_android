package com.example.applicationgestionphotos_laghlid.view;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.applicationgestionphotos_laghlid.Adapters.PhotoAdapter;
import com.example.applicationgestionphotos_laghlid.Model.Photo;
import com.example.applicationgestionphotos_laghlid.NetworkApi.PerformSearchTask;
import com.example.applicationgestionphotos_laghlid.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;



/**
 * @author: Ayoub Laghlid
 * @Project: Gestion des Photos en utilsant l'api Flickr
 * 5A ASL
 **/

//  Ce fragment est utilisé pour afficher les images récentes publiées sur Flickr
//  Il utilise PerformSearchTask pour effectuer une recherche à l'aide de l'API Flickr, puis affiche les résultats dans une ListView à l'aide d'un adaptateur PhotoAdapter
public class HomeFragment extends Fragment {
    private ListView listView;
    private PhotoAdapter adapter;
    private List<Photo> photos;
    private FloatingActionButton refreshHome;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listView = view.findViewById(R.id.list_view);
        refreshHome = view.findViewById(R.id.refresh);
        photos = new ArrayList<>();
        adapter = new PhotoAdapter(getActivity(), photos);
        listView.setAdapter(adapter);
        performSearch("");

        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //  Le bouton flottant est pour but de recharger les images récentes
        //  lorsqu'on clique sur ce bouton de rafraîchissement le téléphone peut vibrer en fonction des paramètres de l'application
        //  (on peut désactiver la vibration sur le fragment Paramètres)
        refreshHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch("");
                Toast.makeText(getActivity(), "Recharger les images récentes publiées", Toast.LENGTH_SHORT).show();
                SharedPreferences sh = getContext().getSharedPreferences("Settings", MODE_PRIVATE);
                boolean isVibratorOnFavorites = sh.getBoolean("isVibratorOnFavorites", false);
                if (isVibratorOnFavorites) {
                    Vibrator vibrate = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrate.vibrate(200);
                }
            }
        });
    }
    private void performSearch(String query) {
        new PerformSearchTask(photos, adapter).execute(query);
    }

    @Override
    public void onResume() {
        super.onResume();
        performSearch("");
        Toast.makeText(getContext(), "Voici les images récentes en attendant que vous effectuez une recherche par tag", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
