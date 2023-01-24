package com.example.applicationgestionphotos_laghlid.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.applicationgestionphotos_laghlid.Adapters.PhotoAdapter;
import com.example.applicationgestionphotos_laghlid.Model.Photo;
import com.example.applicationgestionphotos_laghlid.NetworkApi.PerformSearchTask;
import com.example.applicationgestionphotos_laghlid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Ayoub Laghlid
 * @Project: Gestion des Photos en utilsant l'api Flickr
 * 5A ASL
 **/

//  Ce fragment est utilisé pour la recherche de photos à partir de l'API Flickr
//  l'dapter utilisé est PhotoAdapter
//  listView est utilisée pour afficher les résultats de la recherche
//  la barre de recherche est utilisée pour saisir les tags de recherche
//  performSearch est une tâche asynchrone pour effectuer la recherche en arrière-plan pour éviter de bloquer l'interface utilisateur
public class SearchFragment extends Fragment {
    private ListView listView;
    private SearchView searchView;
    private PhotoAdapter adapter;
    private List<Photo> photos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        listView = view.findViewById(R.id.list_view);
        searchView = view.findViewById(R.id.search_view);
        photos = new ArrayList<>();
        adapter = new PhotoAdapter(getActivity(), photos);
        listView.setAdapter(adapter);
        performSearch("");

        // Listners sur la barre de recherche pour la saisie et pour l'affichage des résultats de la recherche
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                performSearch(query);
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photos.clear();
                adapter.notifyDataSetChanged();
                searchView.clearFocus();
            }
        });
        return view;
    }

    private void performSearch(String query) {
        new PerformSearchTask(photos, adapter).execute(query);
        if (query.equals(""))   Toast.makeText(getContext(), "Voici les images récentes en attendant que vous effectuez une recherche par tag", Toast.LENGTH_SHORT).show();
        else Toast.makeText(getContext(), "Recherche effectuée !", Toast.LENGTH_SHORT).show();
    }

    //  Nettoyer les résultats de la recherche lorsque le fragment est détruit ou lorsque l'utilisateur revient à ce fragment
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        photos.clear();
        adapter.notifyDataSetChanged();
        searchView.setQuery("", false);
        searchView.clearFocus();
    }
    @Override
    public void onResume() {
        super.onResume();
        searchView.setQuery("", false);
        searchView.clearFocus();
        Toast.makeText(getContext(), "Une nouvelle recherche ?", Toast.LENGTH_SHORT).show();
    }
}
