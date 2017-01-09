package com.codevariant.insight.model.findBook;

import org.parceler.Parcel;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Anand on 09/12/2016.
 */

@Parcel(Parcel.Serialization.BEAN)
@Root(name = "best_book", strict = false)
@Default(DefaultType.PROPERTY)
public class Book {

    private String id;
    private String title;
    private Author author;
    private String imageUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Element(name = "image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @Element(name = "image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
