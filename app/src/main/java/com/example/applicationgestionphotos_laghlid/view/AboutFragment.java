package com.example.applicationgestionphotos_laghlid.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import com.example.applicationgestionphotos_laghlid.R;

/**
 * @author: Ayoub Laghlid
 * @Project: Gestion des Photos en utilsant l'api Flickr
 * 5A ASL
 **/

//  C'est le fragment qui est utilisé pour afficher des informations de moi et de l'application
//  La vue pour ce fragment est définie dans le fichier XML fragment_about (elle contient l'image des informations que j'ai fait)
//  La méthode onCreateView est utilisée pour inflater la vue à partir de ce fichier XML et la retourner pour être affichée dans l'interface utilisateur
public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        return view;
    }

}

