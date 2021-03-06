package com.codevariant.insight.model.bookInfo;

import org.parceler.Parcel;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Anand on 26/12/2016.
 */

@Parcel(Parcel.Serialization.BEAN)
@Root(name = "book", strict = false)
@Default(DefaultType.PROPERTY)
public class Book {

    private String description;
    private String link;
    private String numPages;

    @Element(required = false)
    public String getDescription() {
        return description;
    }

    @Element(required = false)
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Element(name = "num_pages", required = false)
    public String getNumPages() {
        return numPages;
    }

    @Element(name = "num_pages", required = false)
    public void setNumPages(String numPages) {
        this.numPages = numPages;
    }
}
