package com.codevariant.insight.model.recentReview;

import org.parceler.Parcel;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Anand on 14/11/2016.
 */

@Parcel(Parcel.Serialization.BEAN)
@Root(name = "user", strict = false)
@Default(DefaultType.PROPERTY)
public class User {

    private String id;
    private String displayName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Element(name = "display_name")
    public String getDisplayName() {
        return displayName;
    }

    @Element(name = "display_name")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
