package com.example.moviemingle.ui;

import android.util.Log;

import com.example.moviemingle.ui.Film;

import java.util.List;

public class SearchResult {
    private List<Film> Search;
    private int totalResults;
    private boolean Response;

    public List<Film> getSearch() {
        return Search;
    }

    public void setSearch(List<Film> search) {
        this.Search = search;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public boolean isResponse() {
        return Response;
    }

    public void setResponse(boolean response) {
        Response = response;
    }
}