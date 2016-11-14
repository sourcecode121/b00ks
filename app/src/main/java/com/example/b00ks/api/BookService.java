package com.example.b00ks.api;

import com.example.b00ks.model.Response;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Anand on 14/11/2016.
 */

public interface BookService {

    @GET("review/recent_reviews.xml")
    Observable<Response> getRecentReviews(@Query("key") String key);
}
