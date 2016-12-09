package com.example.b00ks.model.findBook;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Anand on 09/12/2016.
 */

@Root(name = "work", strict = false)
@Default(DefaultType.PROPERTY)
public class Work {

    private Book bestBook;

    @Element(name = "best_book")
    public Book getBestBook() {
        return bestBook;
    }

    @Element(name = "best_book")
    public void setBestBook(Book bestBook) {
        this.bestBook = bestBook;
    }
}
