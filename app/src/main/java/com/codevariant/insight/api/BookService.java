package com.codevariant.insight.api;

import com.codevariant.insight.model.bookInfo.BookInfoResponse;
import com.codevariant.insight.model.findAuthor.FindAuthorResponse;
import com.codevariant.insight.model.findBook.FindBookResponse;
import com.codevariant.insight.model.recentReview.RecentReviewResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Anand on 14/11/2016.
 */

public interface BookService {

    @GET("review/recent_reviews.xml")
    Observable<RecentReviewResponse> getRecentReviews(@Query("key") String key);
    @GET("search/index.xml")
    Observable<FindBookResponse> findBooks(@Query("q") String q,
                                           @Query("page") String page,
                                           @Query("key") String key,
                                           @Query("search[field]") String field);
    @GET("api/author_url/{name}")
    Observable<FindAuthorResponse> findAuthor(@Path("name") String name,
                                              @Query("key") String key);
    @GET("author/show.xml")
    Observable<FindAuthorResponse> getAuthorInfo(@Query("id") String id,
                                                 @Query("key") String key);
    @GET("book/show.xml")
    Observable<BookInfoResponse> getBookInfo(@Query("id") String id,
                                             @Query("key") String key);
}
