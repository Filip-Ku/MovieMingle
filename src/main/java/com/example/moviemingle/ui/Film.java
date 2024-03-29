package com.example.moviemingle.ui;

public class Film {
    private String Title;
    private String Year;
    private String Runtime;
    private String Director;

    private String Response;

    private String Poster;

    private String Writer;
    private Film(String Title,String Year,String Time,String Director,String Response,String Poster,String Writer){
        this.Director=Director;
        this.Runtime=Time;
        this.Year=Year;
        this.Title=Title;
        this.Response=Response;
        this.Poster=Poster;
        this.Writer=Writer;
    }

    public String getDirector() {
        return Director;
    }

    public String getTime() {
        return Runtime;
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
}
