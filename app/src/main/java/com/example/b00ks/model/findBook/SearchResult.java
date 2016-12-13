package com.example.b00ks.model.findBook;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Anand on 13/12/2016.
 */

@Parcel(Parcel.Serialization.BEAN)
public class SearchResult {

    private List<Work> results;

    public List<Work> getResults() {
        return results;
    }

    public void setResults(List<Work> results) {
        this.results = results;
    }
}
