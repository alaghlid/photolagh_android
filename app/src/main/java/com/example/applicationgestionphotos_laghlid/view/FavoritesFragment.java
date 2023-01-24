package com.example.applicationgestionphotos_laghlid.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationgestionphotos_laghlid.Adapters.FavoritesPhotoAdapter;
import com.example.applicationgestionphotos_laghlid.Database.PhotoDatabaseHelper;
import com.example.applicationgestionphotos_laghlid.Model.Photo;
import com.example.applicationgestionphotos_laghlid.R;

import java.util.List;



/**
 * @author: Ayoub Laghlid
 * @Project: Gestion des Photos en utilsant l'api Flickr
 * 5A ASL
 **/


//  C'est le fragment qui gère l'affichage des photos enregistrées comme favoris par l'utilisateur
//  J'ai utilisé RecyclerView pour afficher les photos en utilisant un adapter personnalisé FavoritesPhotoAdapter (synchronisation en cas de suppression du favoris)
public class FavoritesFragment extends Fragment {
    private RecyclerView recycler_view_favorites;
    private FavoritesPhotoAdapter adapter;
    private List<Photo> photos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState
    ) {
        View view;
        view = inflater.inflate(R.layout.fragment_favorites, container, false);
        recycler_view_favorites = view.findViewById(R.id.recycler_view_favorites);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_favorites.setLayoutManager(layoutManager);
        //  on utilise une instance de PhotoDatabaseHelper pour récupérer les photos enregistrées en tant que favoris à partir de la base de données
        PhotoDatabaseHelper dbHelper = new PhotoDatabaseHelper(getContext());
        photos = dbHelper.getAllPhotos();
        //  On vérifie s'il y a des photos enregistrées en tant que favoris
        if (photos != null){
            //  Si oui, les affiche dans la vue en utilisant l'adapter
            adapter = new FavoritesPhotoAdapter(getActivity(), photos);
            recycler_view_favorites.setAdapter(adapter);
            Toast.makeText(getActivity(), "Voici tes favoris ", Toast.LENGTH_SHORT).show();
        }
        else{
            //  Sinon,  un message Toast indiquant qu'aucune photo n'a été trouvée dans les favoris
            Toast.makeText(getActivity(), "Aucune photo trouvée dans favoris ", Toast.LENGTH_SHORT).show();
        }
        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}


