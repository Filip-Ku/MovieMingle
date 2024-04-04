package com.example.moviemingle.ui.slideshow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemingle.R;
import com.example.moviemingle.databinding.FragmentHomeBinding;
import com.example.moviemingle.ui.Film;
import com.example.moviemingle.ui.FilmAdapter;
import com.example.moviemingle.ui.FilmInfo;
import com.example.moviemingle.ui.InternetConnectionChecker;
import com.example.moviemingle.ui.JsonPlaceholderAPI;
import com.example.moviemingle.ui.Poster;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SlideShowFragment extends Fragment {

    private FragmentHomeBinding binding;

    private RecyclerView recyclerView;

    private List<Film> filmList;

    private FilmAdapter adapter;

    private SharedPreferences sharedPref;

    Set<String> Titles = new HashSet<>();

    private InternetConnectionChecker internetConnectionChecker;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sharedPref = getActivity().getSharedPreferences("MySharedPref",Context.MODE_PRIVATE);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        recyclerView = root.findViewById(R.id.favouriteFilms);
        recyclerView.setAdapter(new FilmAdapter(new ArrayList<>()));
        filmList = new ArrayList<>();

        loadFilms();
        return root;
    }
    private void loadFilms() {
        Titles = sharedPref.getStringSet("favouriteList",new HashSet<>());
        if(internetConnectionChecker.isInternetAvailable(getContext())){
        for (String tytul : Titles) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.omdbapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceholderAPI jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);
            Call<Film> call = jsonPlaceholderAPI.getFilm("7e8698f5", tytul);
            call.enqueue(new Callback<Film>() {
                @Override
                public void onResponse(Call<Film> call, Response<Film> response) {
                    if (response.isSuccessful()) {
                        Film film = response.body();
                        filmList.add(film);
                        updateRecyclerView();
                    } else {
                        Log.e("Slideshow", "Film not found: " + tytul);
                    }
                }

                @Override
                public void onFailure(Call<Film> call, Throwable t) {
                    Log.e("Slideshow", "Error: " + t.getMessage());
                }
            });
        }} else{
            Toast.makeText(getContext(), "Brak dostępu do internetu.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateRecyclerView() {
        adapter = new FilmAdapter(filmList);
        adapter.setAddable(false);

        adapter.setOnPosterClickListener(new FilmAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Film clickedFilm = filmList.get(position);
                Intent intent = new Intent(getContext(), Poster.class);
                intent.putExtra("posterUrl", clickedFilm.getPoster());
                startActivity(intent);
            }
        });

        adapter.setOnItemClickListener(new FilmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(internetConnectionChecker.isInternetAvailable(getContext())){
                Film clickedFilm = filmList.get(position);
                Intent intent = new Intent(getContext(), FilmInfo.class);
                intent.putExtra("filmInfo", clickedFilm.getTitle());
                startActivity(intent);
            } else{
                Toast.makeText(getContext(), "Brak dostępu do internetu.", Toast.LENGTH_SHORT).show();
            }
        }});

        adapter.setOnWatchClickListener(new FilmAdapter.OnWatchClickListener() {
            @Override
            public void onToWatchClick(int position, boolean isAddable) {
                if (!isAddable){
                    Film clickedFilm = filmList.get(position);
                    Titles.remove(clickedFilm.getTitle());
                    saveTitlesState(clickedFilm);
                    filmList.remove(position);
                    adapter.notifyDataSetChanged();
                }}
        });

        recyclerView.setAdapter(adapter);
    }

    private void saveTitlesState(Film clickedFilm) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("favouriteList");
        editor.apply();
        editor.putStringSet("favouriteList", Titles);
        editor.commit();
        Toast.makeText(getContext(), "Removed "+ clickedFilm.getTitle()+" from favourite list.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}