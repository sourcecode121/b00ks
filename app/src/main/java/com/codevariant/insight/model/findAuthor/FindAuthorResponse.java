package com.codevariant.insight.model.findAuthor;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Anand on 24/12/2016.
 */

@Root(name = "GoodreadsResponse", strict = false)
@Default(DefaultType.PROPERTY)
public class FindAuthorResponse {

    private Author author;

    @Element(required = false)
    public Author getAuthor() {
        return author;
    }

    @Element(required = false)
    public void setAuthor(Author author) {
        this.author = author;
    }
}
