package com.codevariant.insight.model.findBook;

import org.parceler.Parcel;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

/**
 * Created by Anand on 09/12/2016.
 */

@Parcel(Parcel.Serialization.BEAN)
@Root(name = "author", strict = false)
@Default(DefaultType.PROPERTY)
public class Author {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
