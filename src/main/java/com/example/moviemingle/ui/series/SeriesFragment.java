package com.example.moviemingle.ui.series;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemingle.R;
import com.example.moviemingle.databinding.FragmentHomeBinding;
import com.example.moviemingle.ui.Film;
import com.example.moviemingle.ui.FilmAdapter;
import com.example.moviemingle.ui.FilmInfo;
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

public class SeriesFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;

    private List<Film> serialList;

    private FilmAdapter adapter;

    private EditText finder;

    private ImageView search;


    private  SharedPreferences sharedPref;

    String[] seriale = {
            "The+Wire","Mad+Men", "Breaking+Bad","Fleabag","Game+of+Thrones","I+May+Destroy+You","The+Leftovers","The+Americans","Succession", "BoJack+Horseman",
            "Six+Feet+Under","Twin+Peaks", "Atlanta","Chernobyl","The+Crown", "30+Rock","Deadwood", "Lost", "The+Thick+of+It","Curb+Your+Enthusiasm", "Black+Mirror",
            "Better+Call+Saul", "Veep","Sherlock","Watchmen","Line+of+Duty", "Friday+Night+Lights", "Parks+and+Recreation", "Girls","True+Detective","Arrested+Development",
            "The+Good+Wife","The+Bridge","Fargo", "Downton+Abbey", "Band+of+Brothers", "The+Handmaid’s+Tale","Borgen","Schitt’s+Creek", "Peep+Show", "Money+Heist",  "Community",
            "The+Good+Fight", "Homeland","Grey’s+Anatomy", "Inside+No+9", "The+Bureau","Halt+and+Catch+Fire", "Small+Axe", "Call+My+Agent!",
            "Happy+Valley", "The+Shield", "The+Big+Bang+Theory","The+Young+Pope","Dark","The+Underground+Railroad", "House+of+Cards", "Avatar:+The+Last+Airbender",
            "The+Good+Place","Pose","Detectorists", "Orange+is+the+New+Black","Mare+of+Easttown", "RuPaul’s+Drag+Race", "Stranger+Things", "24", "Battlestar+Galactica",
            "Enlightened", "Gilmore+Girls", "Planet+Earth","Babylon+Berlin", "Rick+and+Morty", "American+Crime+Story","Mindhunter","House", "OJ:+Made+in+America",
            "Big+Little+Lies", "Insecure", "Normal+People", "Narcos", "How+I+Met+Your+Mother", "The+Comeback","The+OA","Dexter","It’s+Always+Sunny+in+Philadelphia",
            "Westworld", "Show+Me+a+Hero", "Treme", "Louie", "Luther","Catastrophe","Hannibal","Crazy+Ex-Girlfriend","Steven+Universe","The+Queen’s+Gambit"
    };
    Set<String> wylosowaneTytuly = new HashSet<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = inflater.inflate(R.layout.fragment_series, container, false);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        recyclerView = root.findViewById(R.id.bestSeries);
        serialList = new ArrayList<>();
        finder = root.findViewById(R.id.finder);
        recyclerView.setAdapter(new FilmAdapter(new ArrayList<>()));
        search = (ImageView) root.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchSeries();
            }
        });
        loadSeries();

        return root;
    }

    private void searchSeries() {
        String phrase;
        serialList.clear();
        phrase = finder.getText().toString();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceholderAPI jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);
        Call<SearchResult> call = jsonPlaceholderAPI.searchProduction("7e8698f5", phrase, "series", 10, "imdbRating", "desc");
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.isSuccessful()) {
                    SearchResult result = response.body();
                    Log.e("HomeFragment", response.body().toString());
                    if (result != null && result.getSearch() != null) {
                        for (Film film : result.getSearch()) {
                            searchExactInfoSeries(film.getTitle());
                        }
                        updateRecyclerView();
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
    }

    private void searchExactInfoSeries(String tytul) {
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
                    serialList.add(film);
                    if (serialList.size() <= 10) {
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

    private void loadSeries() {
        Random rand = new Random();

        while (wylosowaneTytuly.size() < 10) {
            int index = rand.nextInt(seriale.length);
            wylosowaneTytuly.add(seriale[index]);
        }

        for (String tytul : wylosowaneTytuly) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.omdbapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceholderAPI jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);
            Call<Film> call = jsonPlaceholderAPI.getFilm("7e8698f5", tytul);
            Log.d("URL SERIES", "URL: " + call.request().url().toString());
            call.enqueue(new Callback<Film>() {
                @Override
                public void onResponse(Call<Film> call, Response<Film> response) {
                    if (response.isSuccessful()) {
                        Film film = response.body();
                        serialList.add(film);
                        if (serialList.size() == 10) {
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
        adapter = new FilmAdapter(serialList);
        adapter.setAddable(true);
        adapter.setOnPosterClickListener(new FilmAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Film clickedFilm = serialList.get(position);
                Intent intent = new Intent(getContext(), Poster.class);
                intent.putExtra("posterUrl", clickedFilm.getPoster());
                startActivity(intent);
            }
        });

        adapter.setOnItemClickListener(new FilmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Film clickedFilm = serialList.get(position);
                Intent intent = new Intent(getContext(), FilmInfo.class);
                intent.putExtra("filmInfo", clickedFilm.getTitle());
                startActivity(intent);
            }
        });

        adapter.setOnWatchClickListener(new FilmAdapter.OnWatchClickListener() {
            @Override
            public void onToWatchClick(int position,boolean isAddable) {
                if (isAddable) {
                    Film clickedFilm = serialList.get(position);
                    adapter.addToWatchList(clickedFilm.getTitle().toString());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putStringSet("toWatchList", adapter.getWatchList());
                    editor.apply();
                    Toast.makeText(getContext(), "Added "+clickedFilm.getTitle().toString() +" to watch", Toast.LENGTH_SHORT).show();
                }}
        });

        adapter.setOnFavouriteClickListener(new FilmAdapter.OnFavouriteClickListener() {
            @Override
            public void onFavouriteClick(int position, boolean isAddable) {
                if (isAddable){
                    Film clickedFilm = serialList.get(position);
                    adapter.addFavouriteList(clickedFilm.getTitle().toString());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putStringSet("favouriteList", adapter.getFavouriteList());
                    editor.apply();
                    Toast.makeText(getContext(), "Added "+clickedFilm.getTitle().toString() +" to favourite list", Toast.LENGTH_SHORT).show();
                }}
        });

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