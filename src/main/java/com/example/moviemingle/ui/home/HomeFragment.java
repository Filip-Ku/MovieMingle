package com.example.moviemingle.ui.home;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemingle.ui.FilmInfo;
import com.example.moviemingle.R;
import com.example.moviemingle.databinding.FragmentHomeBinding;
import com.example.moviemingle.ui.Film;
import com.example.moviemingle.ui.FilmAdapter;
import com.example.moviemingle.ui.InternetConnectionChecker;
import com.example.moviemingle.ui.JsonPlaceholderAPI;
import com.example.moviemingle.ui.Poster;
import com.example.moviemingle.ui.SearchResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private RecyclerView recyclerView;

    private List<Film> filmList;

    private FilmAdapter adapter;

    private EditText finder;

    private ImageView search;

    private SharedPreferences sharedPref;

    private Set<String> Titles = new HashSet<>();

    private Set<String> favouriteTitles = new HashSet<>();

    String[] filmy = {"The+Shawshank+Redemption", "The+Godfather", "The+Dark+Knight", "12+Angry+Men", "Schindler's+List", "The+Lord+of+the+Rings:+The+Return+of+the+King",
            "Pulp+Fiction", "The+Good,+the+Bad+and+the+Ugly", "Fight+Club", "Forrest+Gump", "Inception", "The+Lord+of+the+Rings:+The+Fellowship+of+the+Ring",
            "Star+Wars:+Episode+V+-+The+Empire+Strikes+Back", "The+Lord+of+the+Rings:+The+Two+Towers", "The+Matrix", "Goodfellas", "One+Flew+Over+the+Cuckoo's+Nest",
            "Seven+Samurai", "Se7en", "Life+Is+Beautiful", "City+of+God", "The+Silence+of+the+Lambs", "It's+a+Wonderful+Life", "Star+Wars:+Episode+IV+-+A+New+Hope",
            "Saving+Private+Ryan", "Spirited+Away", "The+Green+Mile", "Interstellar", "Parasite", "Léon:+The+Professional", "The+Usual+Suspects", "Harakiri", "The+Lion+King",
            "The+Pianist", "Back+to+the+Future", "Terminator+2:+Judgment+Day", "American+History+X", "Modern+Times", "Psycho", "Gladiator", "City+Lights", "The+Departed",
            "The+Intouchables", "Whiplash", "The+Prestige", "The+Lives+of+Others", "Grave+of+the+Fireflies", "Once+Upon+a+Time+in+the+West", "Cinema+Paradiso", "Alien",
            "Apocalypse+Now", "Memento", "The+Great+Dictator", "The+Sting", "The+Apartment", "The+Father", "Coco", "Sunset+Boulevard", "Spider-Man:+Into+the+Spider-Verse",
            "Hamilton", "Django+Unchained", "The+Dark+Knight+Rises", "1917", "The+Shining", "Avengers:+Infinity+War", "Avengers:+Endgame", "The+Hunt", "The+Bridge+on+the+River+Kwai",
            "Jurassic+Park", "Inglourious+Basterds", "Requiem+for+a+Dream", "Eternal+Sunshine+of+the+Spotless+Mind", "Toy+Story", "Toy+Story+3", "Reservoir+Dogs", "Amélie",
            "Das+Boot", "Star+Wars:+Episode+VI+-+Return+of+the+Jedi", "Shrek", "Indiana+Jones", "Aliens", "A+Clockwork+Orange",
            "WALL·E", "The+Treasure+of+the+Sierra+Madre", "A+Beautiful+Mind", "Vertigo", "North+by+Northwest", "Your+Name", "The+Seventh+Seal", "The+Third+Man", "On+the+Waterfront",
            "The+Gold+Rush", "The+Maltese+Falcon", "The+Bridge+on+the+River+Kwai", "Rashomon", "The+Wizard+of+Oz", "The+Truman+Show", "Amadeus"};
    Set<String> wylosowaneTytuly = new HashSet<>();

    private InternetConnectionChecker internetConnectionChecker;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPref = getContext().getSharedPreferences("MySharedPref",MODE_PRIVATE);
        recyclerView = root.findViewById(R.id.bestFilms);
        finder = root.findViewById(R.id.finder);
        filmList = new ArrayList<>();
        recyclerView.setAdapter(new FilmAdapter(new ArrayList<>()));
        search = (ImageView) root.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchProduction();
            }
        });
        loadToWatch();
        loadFavourites();
        loadFilms();

        return root;
    }

    private void loadToWatch(){
        Titles = sharedPref.getStringSet("toWatchList", new HashSet<>());
    }

    private void loadFavourites(){
        favouriteTitles = sharedPref.getStringSet("favouriteList", new HashSet<>());
    }

    private void searchProduction() {
        String phrase;
        filmList.clear();
        phrase = finder.getText().toString();
        if(internetConnectionChecker.isInternetAvailable(getContext())){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceholderAPI jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);
        Call<SearchResult> call = jsonPlaceholderAPI.searchProduction("7e8698f5", phrase, "movie", 10, "imdbRating", "desc");
        Log.d("URL LIST", "URL: " + call.request().url().toString());
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.isSuccessful()) {
                    SearchResult result = response.body();
                    Log.e("HomeFragment", response.body().toString());
                    if (result != null && result.getSearch() != null) {
                        for (Film film : result.getSearch()) {
                            searchExactInfo(film.getTitle());
                            Log.d("Title",film.getTitle());
                        }
                    } else {
                        Log.e("HomeFragment", "No series found for: " + phrase);
                    }
                } else {
                    Log.e("HomeFragment", "Failed to fetch series: " + phrase);
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.e("HomeFragment", "Error: " + t.getMessage());
            }
        });
        } else{
            Toast.makeText(getContext(), "Brak dostępu do internetu.", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchExactInfo(String tytul) {
        if(internetConnectionChecker.isInternetAvailable(getContext())){
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
                    if (filmList.size() <= 10) {
                        updateRecyclerView();
                    }
                } else {
                    Log.e("HomeFragment", "Film not found: " + tytul);
                }
            }

            @Override
            public void onFailure(Call<Film> call, Throwable t) {
                Log.e("HomeFragment", "Error: " + t.getMessage());
            }


        });}else{
            Toast.makeText(getContext(), "Brak dostępu do internetu.", Toast.LENGTH_SHORT).show();
        }
    }



    private void loadFilms() {
        Random rand = new Random();

        while (wylosowaneTytuly.size() < 10) {
            int index = rand.nextInt(filmy.length);
            wylosowaneTytuly.add(filmy[index]);
        }
        if(internetConnectionChecker.isInternetAvailable(getContext())){
        for (String tytul : wylosowaneTytuly) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.omdbapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceholderAPI jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);
            Call<Film> call = jsonPlaceholderAPI.getFilm("7e8698f5", tytul);
            Log.d("URL", "URL: " + call.request().url().toString());
            call.enqueue(new Callback<Film>() {
                @Override
                public void onResponse(Call<Film> call, Response<Film> response) {
                    if (response.isSuccessful()) {
                        Film film = response.body();
                        filmList.add(film);
                        if (filmList.size() == 10) {
                            updateRecyclerView();
                        }
                    } else {
                        Log.e("HomeFragment", "Film not found: " + tytul);
                    }
                }

                @Override
                public void onFailure(Call<Film> call, Throwable t) {
                    Log.e("HomeFragment", "Error: " + t.getMessage());
                }
            });
        }} else{
            Toast.makeText(getContext(), "Brak dostępu do internetu.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateRecyclerView() {
        adapter = new FilmAdapter(filmList);
        adapter.setAddable(true);

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
                if (internetConnectionChecker.isInternetAvailable(getContext())) {
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
                if (isAddable) {
                    Film clickedFilm = filmList.get(position);
                    Titles.add(clickedFilm.getTitle());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove("toWatchList");
                    editor.apply();
                    editor.putStringSet("toWatchList", Titles);
                    editor.commit();
                    Toast.makeText(getContext(), "Added "+clickedFilm.getTitle()+" to watch.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapter.setOnFavouriteClickListener(new FilmAdapter.OnFavouriteClickListener() {
            @Override
            public void onFavouriteClick(int position, boolean isAddable) {
                if (isAddable){
                    Film clickedFilm = filmList.get(position);
                    favouriteTitles.add(clickedFilm.getTitle());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove("favouriteList");
                    editor.apply();
                    editor.putStringSet("favouriteList", favouriteTitles);
                    editor.commit();
                    Toast.makeText(getContext(), "Added "+clickedFilm.getTitle()+" to favourite list.", Toast.LENGTH_SHORT).show();
                }}
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (wylosowaneTytuly != null) {
            wylosowaneTytuly.clear();
        }
    }
}


