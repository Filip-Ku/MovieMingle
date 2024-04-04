package com.example.moviemingle.ui;

import java.util.List;

public class Film {
    private String Title;
    private String Year;
    private String Runtime;
    private String Director;

    private String Response;

    private String Poster;

    private String Writer;

    private String Released;
    private String Genre;

    private String Actors;
    private String Plot;

    private String Awards;

    private List<Rating> Ratings;

    public List<Rating> getRatings() {
        return Ratings;
    }

    public void setRatings(List<Rating> ratings) {
        Ratings = ratings;
    }


    private Film(String Title, String Year, String Time, String Director, String Response, String Poster, String Writer, String Released, String Genre, String Actors, String Plot,String Awards,List<Rating> Ratings){
        this.Director=Director;
        this.Runtime=Time;
        this.Year=Year;
        this.Title=Title;
        this.Response=Response;
        this.Poster=Poster;
        this.Writer=Writer;
        this.Released=Released;
        this.Genre=Genre;
        this.Actors=Actors;
        this.Plot=Plot;
        this.Awards=Awards;
        this.Ratings=Ratings;
    }

    public String getReleased() {
        return Released;
    }

    public String getGenre() {
        return Genre;
    }

    public String getActors() {
        return Actors;
    }

    public String getPlot() {
        return Plot;
    }

    public String getDirector() {
        return Director;
    }

    public String getTime() {
        return Runtime;
    }

    public String getAwards(){
        return Awards;
    }

    public String getYear() {
        return Year;
    }

    public String getTitle() {
        return Title;
    }

    public String getPoster(){ return Poster; }

    public String getWriter(){return Writer;}

    public String getResponse(){return Response;}

    public String getRottenTomatoesRating() {
        for (Rating rating : Ratings) {
            if ("Rotten Tomatoes".equals(rating.getSource())) {
                return rating.getValue();
            }
        }
        return null;
    }
}
class Rating {
    private String Source;
    private String Value;

    public Rating(String source, String value) {
        this.Source = source;
        this.Value = value;
    }

    public String getSource() {
        return Source;
    }

    public String getValue() {
        return Value;
    }

    public void setSource(String source) {
        Source = source;
    }

    public void setValue(String value) {
        Value = value;
    }

}
