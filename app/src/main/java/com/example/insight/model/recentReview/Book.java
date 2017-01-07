package com.example.insight.model.recentReview;

import org.parceler.Parcel;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Anand on 12/11/2016.
 */

@Parcel(Parcel.Serialization.BEAN)
@Root(name = "book", strict = false)
@Default(DefaultType.PROPERTY)
public class Book {

    private int id;
    private String title;
    private List<Author> authors;
    private String imageUrl;
    private String link;
    private String numPages;
    private String format;
    private String publisher;
    private String publicationYear;
    private String averageRating;
    private String ratingsCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ElementList(name = "authors")
    public List<Author> getAuthors() {
        return authors;
    }

    @ElementList(name = "authors")
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @Element(name = "image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @Element(name = "image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Element(name = "num_pages", required = false)
    public String getNumPages() {
        return numPages;
    }

    @Element(name = "num_pages", required = false)
    public void setNumPages(String numPages) {
        this.numPages = numPages;
    }

    @Element(required = false)
    public String getFormat() {
        return format;
    }

    @Element(required = false)
    public void setFormat(String format) {
        this.format = format;
    }

    @Element(required = false)
    public String getPublisher() {
        return publisher;
    }

    @Element(required = false)
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Element(name = "publication_year", required = false)
    public String getPublicationYear() {
        return publicationYear;
    }

    @Element(name = "publication_year", required = false)
    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
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
}
