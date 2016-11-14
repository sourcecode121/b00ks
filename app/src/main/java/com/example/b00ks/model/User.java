package com.example.b00ks.model;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Anand on 14/11/2016.
 */

@Root(name = "user", strict = false)
@Default(DefaultType.PROPERTY)
public class User {

    private String id;
    private String display_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Element(name = "display_name")
    public String getDisplayName() {
        return display_name;
    }

    @Element(name = "display_name")
    public void setDisplayName(String display_name) {
        this.display_name = display_name;
    }
}
