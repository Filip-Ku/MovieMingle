package com.example.moviemingle.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviemingle.R;
import com.example.moviemingle.ui.Film;
import com.example.moviemingle.ui.JsonPlaceholderAPI;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilmInfo extends AppCompatActivity {

   private String title;

   private TextView titleTextView;
   private TextView runtimeTextView;
   private TextView directorTextView;
   private TextView writerTextView;
   private TextView genreTextView;
   private TextView yearTextView;
   private TextView actorsTextView;
   private TextView plotTextView;
   private TextView productionTextView;

   private ImageView posterImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filminfo_layout);
        titleTextView = findViewById(R.id.titleTextView);
        runtimeTextView = findViewById(R.id.runtimeTextView);
        directorTextView = findViewById(R.id.directorTextView);
        writerTextView = findViewById(R.id.writerTextView);
        genreTextView = findViewById(R.id.genreTextView);
        yearTextView = findViewById(R.id.yearTextView);
        actorsTextView = findViewById(R.id.actorsTextView);
        plotTextView = findViewById(R.id.plotTextView);
        productionTextView = findViewById(R.id.productionTextView);
        posterImageView = findViewById(R.id.posterImageView);
        getSupportActionBar().hide();
        title = getIntent().getStringExtra("filmInfo");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceholderAPI jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);
        Call<Film> call = jsonPlaceholderAPI.getFilm("7e8698f5", title);

        call.enqueue(new Callback<Film>() {
            @Override
            public void onResponse(Call<Film> call, Response<Film> response) {
                titleTextView.setText(response.body().getTitle().toString());
                runtimeTextView.setText(response.body().getTime().toString());
                directorTextView.setText(response.body().getDirector().toString());
                writerTextView.setText(response.body().getWriter().toString());
                genreTextView.setText(response.body().getGenre().toString());
                yearTextView.setText(response.body().getYear().toString());
                actorsTextView.setText(response.body().getActors().toString());
                plotTextView.setText(response.body().getPlot().toString());
                productionTextView.setText(response.body().getReleased().toString());
                if(response.body().getPoster().equals("N/A")) {
                    Picasso.get().load("https://i.imgur.com/zYUBMnP.png").into(posterImageView);
                } else {
                    Picasso.get().load(response.body().getPoster()).into(posterImageView);
                }
            }

            @Override
            public void onFailure(Call<Film> call, Throwable t) {
                Log.e("HomeFragment", "Error: " + t.getMessage());
            }
        });
    }

    public void turnOff(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        title="";
    }
}