package com.example.moviemingle.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviemingle.R;
import com.squareup.picasso.Picasso;

public class Poster extends AppCompatActivity {

    private String url;

    private ImageView posterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poster_layout);

        url = getIntent().getStringExtra("posterUrl");

        getSupportActionBar().hide();

        posterView = findViewById(R.id.bigPoster);
        if(url.equals("N/A")) {
            Picasso.get().load("https://i.imgur.com/zYUBMnP.png").resize(600, 800)  .centerCrop().into(posterView);
        } else {
            Picasso.get().load(url).into(posterView);
        }
    }

    public void turnOff(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        url="N/A";
    }
}