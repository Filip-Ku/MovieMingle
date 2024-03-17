package com.example.moviemingle.ui;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemingle.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {
    private List<Film> films;
    public FilmAdapter(List<Film> films) {
        this.films = films;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_film, parent, false);
        return new ViewHolder(view);
    }

    public void updateData(List<Film> newMessages) {
        films.clear();
        films.addAll(newMessages);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Film film = films.get(position);
        Log.d("FilmAdapter", "Binding message: " + film.getTitle());
        holder.bind(film);
    }



    @Override
    public int getItemCount() {
        return films.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView yearTextView;
        public TextView directorTextView;
        public TextView timeTextView;

        public ImageView posterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.poster);
            titleTextView = itemView.findViewById(R.id.title);
            yearTextView = itemView.findViewById(R.id.year);
            directorTextView = itemView.findViewById(R.id.director);
            timeTextView = itemView.findViewById(R.id.runtime);
        }

        public void bind(Film film) {
            Log.d("MessagesAdapter", "Binding message: " + film.getTitle());
            titleTextView.setText(film.getTitle());
            yearTextView.setText(film.getYear());
            timeTextView.setText(film.getTime());

            String firstDirector ="";
            Picasso.get().load(film.getPoster()).into(posterImageView);

            String directors = film.getDirector();
            if(directors!= null && directors.contains(",")){
                String[] directorArray = directors.split(",");


                if (directorArray.length > 0) {
                    firstDirector = directorArray[0].trim();
                }
            }else if(directors!= null && directors.contains(",")==false){
                firstDirector=directors;
            } else {
                firstDirector="";
            }


            directorTextView.setText(firstDirector);
        }
    }
}