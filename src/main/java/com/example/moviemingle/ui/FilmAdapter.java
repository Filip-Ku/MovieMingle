package com.example.moviemingle.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemingle.R;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {
    private List<Film> films;
    public FilmAdapter(List<Film> films) {
        this.films = films;
    }

    private OnPosterClickListener onPosterClickListener;

    private OnItemClickListener onItemClickListener;

    private OnWatchClickListener onWatchClickListener;

    private OnFavouriteClickListener onFavouriteClickListener;


    public FilmAdapter(List<Film> films, Context context) {
        this.films = films;
    }

    private boolean isAddable;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_film, parent, false);
        return new ViewHolder(view);
    }

    public void setOnPosterClickListener(OnPosterClickListener listener) {
        this.onPosterClickListener = listener;
    }

    public void setOnFavouriteClickListener(OnFavouriteClickListener listener){
        this.onFavouriteClickListener = listener;
    }

    public void setAddable(boolean isAddable){
        this.isAddable=isAddable;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnWatchClickListener(OnWatchClickListener listener){
        this.onWatchClickListener=listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnFavouriteClickListener {
        void onFavouriteClick(int position,boolean isAddable);
    }

    public interface OnWatchClickListener {
        void onToWatchClick(int position,boolean isAddable);
    }

    public interface OnPosterClickListener {
        void onPosterClick(int position);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Film film = films.get(position);
        holder.bind(film);

        if (isAddable) {
            holder.toWatch.setImageResource(R.drawable.tv);
            holder.favourite.setImageResource(R.drawable.ic_favourite);
        } else {
            holder.toWatch.setImageResource(android.R.drawable.ic_delete);
            holder.favourite.setVisibility(View.GONE);
        }

        holder.posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPosterClick(position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        holder.toWatch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onWatchClickListener.onToWatchClick(position,isAddable);
            }
        });

        if(holder.favourite.getVisibility()== View.VISIBLE) {
        holder.favourite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                onFavouriteClickListener.onFavouriteClick(position,isAddable);
            }
        });
        }
    }

    public void onPosterClick(int position) {
        if (onPosterClickListener != null) {
            onPosterClickListener.onPosterClick(position);
        }
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
        public ImageView toWatch;

        public ImageView favourite;
        public ImageView posterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            toWatch = itemView.findViewById(R.id.toWatch);
            favourite = itemView.findViewById(R.id.favourite);
            posterImageView = itemView.findViewById(R.id.poster);
            titleTextView = itemView.findViewById(R.id.title);
            yearTextView = itemView.findViewById(R.id.year);
            directorTextView = itemView.findViewById(R.id.director);
            timeTextView = itemView.findViewById(R.id.runtime);

        }

        public void bind(Film film) {
            titleTextView.setText(film.getTitle());
            yearTextView.setText(film.getYear());
            if(film.getTime()!=null){
                timeTextView.setText(film.getTime());
            } else {
                timeTextView.setText("Not found");
            }

            String firstDirector ="";
            if(film.getPoster() == null || film.getPoster().equals("N/A")) {
                Picasso.get().load("https://i.imgur.com/zYUBMnP.png").resize(100, 135).centerCrop().into(posterImageView);
            } else {
                Picasso.get().load(film.getPoster()).into(posterImageView);
            }


            String directors = film.getDirector();
            String writers = film.getWriter();
            if(directors!=null) {
                if (directors.equals("N/A")) {
                    if (writers != null && !writers.isEmpty()) {
                        if (writers.contains(",")) {
                            String[] writerArray = writers.split(",");
                            if (writerArray.length > 0) {
                                firstDirector = writerArray[0].trim();
                            }
                        } else {
                            firstDirector = writers.trim();
                        }
                    }
                } else {
                    if (directors.contains(",")) {
                        String[] directorArray = directors.split(",");
                        if (directorArray.length > 0) {
                            firstDirector = directorArray[0].trim();
                        }
                    } else {
                        firstDirector = directors.trim();
                    }
                }

                directorTextView.setText(firstDirector);
            } else {
                directorTextView.setText("Not found");
            }
    }
}}