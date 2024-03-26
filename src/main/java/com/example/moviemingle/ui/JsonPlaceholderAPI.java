    package com.example.moviemingle.ui;

    import com.example.moviemingle.ui.Film;

    import java.util.List;

    import retrofit2.Call;
    import retrofit2.http.GET;
    import retrofit2.http.Query;

    public interface JsonPlaceholderAPI {
        @GET("/")
        Call<Film> getFilm(@Query("apikey") String apiKey, @Query("t") String title);

        @GET("/")
        Call<com.example.moviemingle.ui.SearchResult> searchProduction(
                @Query("apikey") String apiKey,
                @Query("s") String searchQuery,
                @Query("type") String type,
                @Query("r") int resultsPerPage,
                @Query("sort") String sortBy,
                @Query("order") String order
        );
    }
