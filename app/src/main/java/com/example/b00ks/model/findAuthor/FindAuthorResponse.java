package com.example.b00ks.model.findAuthor;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

/**
 * Created by Anand on 24/12/2016.
 */

@Root(name = "GoodreadsResponse", strict = false)
@Default(DefaultType.PROPERTY)
public class FindAuthorResponse {

    private Author author;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
