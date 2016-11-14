package com.example.b00ks.model;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import static android.R.attr.author;

/**
 * Created by Anand on 12/11/2016.
 */

@Root(name = "book", strict = false)
@Default(DefaultType.PROPERTY)
public class Book {

    private int id;
    private String title;
    private List<Author> authors;
    private String image_url;

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
        return image_url;
    }

    @Element(name = "image_url")
    public void setImageUrl(String image_url) {
        this.image_url = image_url;
    }
}
