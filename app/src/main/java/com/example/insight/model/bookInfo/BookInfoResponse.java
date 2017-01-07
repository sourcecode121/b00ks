package com.example.insight.model.bookInfo;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

/**
 * Created by Anand on 26/12/2016.
 */

@Root(name = "GoodreadsResponse", strict = false)
@Default(DefaultType.PROPERTY)
public class BookInfoResponse {

    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
