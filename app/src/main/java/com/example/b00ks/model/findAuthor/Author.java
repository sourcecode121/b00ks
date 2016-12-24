package com.example.b00ks.model.findAuthor;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

/**
 * Created by Anand on 24/12/2016.
 */

@Root(name = "author", strict = false)
@Default(DefaultType.PROPERTY)
public class Author {

    private String id;

    @Attribute
    public String getId() {
        return id;
    }

    @Attribute
    public void setId(String id) {
        this.id = id;
    }
}
