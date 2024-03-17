package com.example.moviemingle.ui.series;

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

public class SeriesFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;

    private List<Film> serialList;

    private FilmAdapter adapter;

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
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.bestFilms);
        serialList = new ArrayList<>();
        recyclerView.setAdapter(new FilmAdapter(new ArrayList<>()));

        loadSeries();

        return root;
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
            Log.d("URL", "URL: " + call.request().url().toString());
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