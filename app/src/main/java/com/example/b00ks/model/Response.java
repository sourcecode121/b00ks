package com.example.b00ks.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Anand on 14/11/2016.
 */

@Root(name = "GoodreadsResponse", strict = false)
public class Response {

    private List<Review> reviews;

    @ElementList(name = "reviews")
    public List<Review> getReviews() {
        return reviews;
    }

    @ElementList(name = "reviews")
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
