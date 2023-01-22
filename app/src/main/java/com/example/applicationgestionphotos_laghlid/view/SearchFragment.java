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
