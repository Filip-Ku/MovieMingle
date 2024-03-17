package com.example.moviemingle.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemingle.R;
import com.example.moviemingle.databinding.FragmentHomeBinding;
import com.example.moviemingle.ui.Film;
import com.example.moviemingle.ui.FilmAdapter;
import com.example.moviemingle.ui.JsonPlaceholderAPI;

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

    String[] filmy = {"The+Shawshank+Redemption", "The+Godfather", "The+Godfather:+Part+II", "The+Dark+Knight", "12+Angry+Men", "Schindler's+List", "The+Lord+of+the+Rings:+The+Return+of+the+King", "Pulp+Fiction", "The+Good,+the+Bad+and+the+Ugly", "Fight+Club", "Forrest+Gump", "Inception", "The+Lord+of+the+Rings:+The+Fellowship+of+the+Ring", "Star+Wars:+Episode+V+-+The+Empire+Strikes+Back", "The+Lord+of+the+Rings:+The+Two+Towers", "The+Matrix", "Goodfellas", "One+Flew+Over+the+Cuckoo's+Nest", "Seven+Samurai", "Se7en", "Life+Is+Beautiful", "City+of+God", "The+Silence+of+the+Lambs", "It's+a+Wonderful+Life", "Star+Wars:+Episode+IV+-+A+New+Hope", "Saving+Private+Ryan", "Spirited+Away", "The+Green+Mile", "Interstellar", "Parasite", "Léon:+The+Professional", "The+Usual+Suspects", "Harakiri", "The+Lion+King", "The+Pianist", "Back+to+the+Future", "Terminator+2:+Judgment+Day", "American+History+X", "Modern+Times", "Psycho", "Gladiator", "City+Lights", "The+Departed", "The+Intouchables", "Whiplash", "The+Prestige", "The+Lives+of+Others", "Grave+of+the+Fireflies", "Once+Upon+a+Time+in+the+West", "Cinema+Paradiso", "Alien", "Apocalypse+Now", "Memento", "The+Great+Dictator", "The+Sting", "The+Apartment", "The+Father", "Coco", "Sunset+Boulevard", "Spider-Man:+Into+the+Spider-Verse", "Hamilton", "Django+Unchained", "The+Dark+Knight+Rises", "1917", "The+Shining", "Avengers:+Infinity+War", "Avengers:+Endgame", "The+Hunt", "The+Bridge+on+the+River+Kwai", "Jurassic+Park", "Inglourious+Basterds", "Requiem+for+a+Dream", "Eternal+Sunshine+of+the+Spotless+Mind", "Toy+Story", "Toy+Story+3", "Reservoir+Dogs", "Amélie", "Das+Boot", "Star+Wars:+Episode+VI+-+Return+of+the+Jedi", "Das+Leben+der+Anderen", "Indiana+Jones+and+the+Raiders+of+the+Lost+Ark", "Aliens", "A+Clockwork+Orange", "WALL·E", "The+Treasure+of+the+Sierra+Madre", "A+Beautiful+Mind", "Vertigo", "North+by+Northwest", "Your+Name", "The+Seventh+Seal", "The+Third+Man", "On+the+Waterfront", "The+Gold+Rush", "The+Maltese+Falcon", "The+Bridge+on+the+River+Kwai", "Rashomon", "The+Wizard+of+Oz", "The+Truman+Show", "Amadeus"};
    Set<String> wylosowaneTytuly = new HashSet<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.bestFilms);
        filmList = new ArrayList<>();
        recyclerView.setAdapter(new FilmAdapter(new ArrayList<>()));

        loadFilms();

        return root;
    }

    private void loadFilms() {
        Random rand = new Random();

        while (wylosowaneTytuly.size() < 10) {
            int index = rand.nextInt(filmy.length);
            wylosowaneTytuly.add(filmy[index]);
        }

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
        }
    }

    private void updateRecyclerView() {
        adapter = new FilmAdapter(filmList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            adapter = null;
        }

        if (wylosowaneTytuly != null) {
            wylosowaneTytuly.clear();
        }
    }
}


