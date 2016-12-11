package com.example.b00ks.model.recentReview;

import org.parceler.Parcel;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Anand on 14/11/2016.
 */

@Parcel(Parcel.Serialization.BEAN)
@Root(name = "GoodreadsResponse", strict = false)
public class RecentReviewResponse {

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
