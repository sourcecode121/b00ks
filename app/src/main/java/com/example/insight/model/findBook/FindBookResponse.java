package com.example.insight.model.findBook;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

/**
 * Created by Anand on 09/12/2016.
 */

@Root(name = "GoodreadsResponse", strict = false)
@Default(DefaultType.PROPERTY)
public class FindBookResponse {

    private Search search;

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }
}
