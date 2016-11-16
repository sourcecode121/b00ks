package com.example.b00ks.model;

import org.parceler.Parcel;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Anand on 14/11/2016.
 */

@Parcel(Parcel.Serialization.BEAN)
@Root(name = "author", strict = false)
@Default(DefaultType.PROPERTY)
public class Author {

    private String id;
    private String name;
    private String link;
    private String averageRating;
    private String ratingsCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
