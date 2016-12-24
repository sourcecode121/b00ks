package com.example.b00ks.model.findAuthor;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import static android.R.attr.required;

/**
 * Created by Anand on 24/12/2016.
 */

@Root(name = "author", strict = false)
@Default(DefaultType.PROPERTY)
public class Author {

    private String id;
    private String name;
    private String imageUrl;
    private String about;
    private String worksCount;
    private String gender;
    private String hometown;
    private String bornAt;
    private String diedAt;

    @Attribute(required = false)
    public String getId() {
        return id;
    }

    @Attribute(required = false)
    public void setId(String id) {
        this.id = id;
    }

    @Element(required = false)
    public String getName() {
        return name;
    }

    @Element(required = false)
    public void setName(String name) {
        this.name = name;
    }

    @Element(name = "image_url", required = false)
    public String getImageUrl() {
        return imageUrl;
    }

    @Element(name = "image_url", required = false)
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Element(required = false)
    public String getAbout() {
        return about;
    }

    @Element(required = false)
    public void setAbout(String about) {
        this.about = about;
    }

    @Element(name = "works_count", required = false)
    public String getWorksCount() {
        return worksCount;
    }

    @Element(name = "works_count", required = false)
    public void setWorksCount(String worksCount) {
        this.worksCount = worksCount;
    }

    @Element(required = false)
    public String getGender() {
        return gender;
    }

    @Element(required = false)
    public void setGender(String gender) {
        this.gender = gender;
    }

    @Element(required = false)
    public String getHometown() {
        return hometown;
    }

    @Element(required = false)
    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    @Element(name = "born_at", required = false)
    public String getBornAt() {
        return bornAt;
    }

    @Element(name = "born_at", required = false)
    public void setBornAt(String bornAt) {
        this.bornAt = bornAt;
    }

    @Element(name = "died_at", required = false)
    public String getDiedAt() {
        return diedAt;
    }

    @Element(name = "died_at", required = false)
    public void setDiedAt(String diedAt) {
        this.diedAt = diedAt;
    }
}
