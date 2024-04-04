package com.example.moviemingle.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
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

   private RatingBar MyRating;
   private RatingBar usersRating;

   private TextView userMark;

   private SharedPreferences sharedPreferences;

   private TextView yourMark;

   private Float yourMarkF;
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
        MyRating = findViewById(R.id.MyRating);
        usersRating = findViewById(R.id.usersRating);
        userMark = findViewById(R.id.userMark);
        yourMark=findViewById(R.id.yourMark);

        getSupportActionBar().hide();
        title = getIntent().getStringExtra("filmInfo");
        sharedPreferences = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        yourMarkF=sharedPreferences.getFloat(title, 0.0f);

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
                yearTextView.setText(response.body().getAwards().toString());
                actorsTextView.setText(response.body().getActors().toString());
                plotTextView.setText(response.body().getPlot().toString());
                productionTextView.setText(response.body().getReleased().toString());
                convertPercent(response.body().getRottenTomatoesRating());
                if(yourMarkF!=null){
                    yourMark.setText("Twoja ocena: " + yourMarkF.toString());
                    MyRating.setRating(yourMarkF);
                }
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

    public void convertPercent(String percent) {
        if(percent!=null) {
            String cleanedPercent = percent.replaceAll("[^0-9]", "");
            double percentValue = Double.parseDouble(cleanedPercent);
            double rating = (percentValue / 100) * 4.5 + 0.5;
            usersRating.setRating((float) rating);
            String formattedRating = String.format("%.2f", rating);
            userMark.setText("Ocena użytkowników: " + formattedRating);
        } else {
            userMark.setText("Brak ocen użytkowników");
        }
    }

    public void turnOff(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        yourMarkF=MyRating.getRating();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(title);
        editor.apply();
        if(yourMarkF!=null) {
            editor.putFloat(title, yourMarkF);
            editor.commit();
        }
        title="";
    }
}