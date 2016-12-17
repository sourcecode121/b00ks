package com.example.b00ks.model.findBook;

import org.parceler.Parcel;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Anand on 09/12/2016.
 */

@Parcel(Parcel.Serialization.BEAN)
@Root(name = "work", strict = false)
@Default(DefaultType.PROPERTY)
public class Work {

    private Book bestBook;
    private String publicationYear;
    private String publicationMonth;
    private String publicationDay;
    private String averageRating;
    private String ratingsCount;
    private String reviewsCount;

    @Element(name = "best_book")
    public Book getBestBook() {
        return bestBook;
    }

    @Element(name = "best_book")
    public void setBestBook(Book bestBook) {
        this.bestBook = bestBook;
    }

    @Element(name = "original_publication_year", required = false)
    public String getPublicationYear() {
        return publicationYear;
    }

    @Element(name = "original_publication_year", required = false)
    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    @Element(name = "original_publication_month", required = false)
    public String getPublicationMonth() {
        return publicationMonth;
    }

    @Element(name = "original_publication_month", required = false)
    public void setPublicationMonth(String publicationMonth) {
        this.publicationMonth = publicationMonth;
    }

    @Element(name = "original_publication_day", required = false)
    public String getPublicationDay() {
        return publicationDay;
    }

    @Element(name = "original_publication_day", required = false)
    public void setPublicationDay(String publicationDay) {
        this.publicationDay = publicationDay;
    }

    @Element(name = "average_rating")
    public String getAverageRating() {
        return averageRating;
    }

    @Element(name = "average_rating")
    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    @Element(name = "ratings_count")
    public String getRatingsCount() {
        return ratingsCount;
    }

    @Element(name = "ratings_count")
    public void setRatingsCount(String ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    @Element(name = "text_reviews_count")
    public String getReviewsCount() {
        return reviewsCount;
    }

    @Element(name = "text_reviews_count")
    public void setReviewsCount(String reviewsCount) {
        this.reviewsCount = reviewsCount;
    }
}
