package org.dreamcorps.content;

import org.dreamcorps.lms.C;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import ca.dragonflystudios.content.model.Collection;
import ca.dragonflystudios.content.model.Model;
import ca.dragonflystudios.content.service.Service;

public class Book implements Parcelable
{
    private String imageURL;
    private String title;
    private String description;
    private String author;
    private String ISBN13;
    private String pubYear;

    public Book(String title, String description, String imageURL, String author, String ISBN, String pubYear)
    {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.author = author;
        this.ISBN13 = ISBN;
        this.pubYear = pubYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Book(Parcel in)
    {
        String[] data = new String[6];

        in.readStringArray(data);
        this.imageURL = data[0];
        this.title = data[1];
        this.description = data[2];
        this.ISBN13 = data[3];
        this.author = data[4];
        this.pubYear = data[5];
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] { this.imageURL, this.title, this.description, this.ISBN13, this.author, this.pubYear});

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN13() {
        return ISBN13;
    }

    public void setISBN13(String ISBN) {
        this.ISBN13 = ISBN;
    }

    public String getPubYear() {
        return pubYear;
    }

    public void setPubYear(String pubYear) {
        this.pubYear = pubYear;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
                                                       public Book createFromParcel(Parcel in) {
                                                           return new Book(in);
                                                       }

                                                       public Book[] newArray(int size) {
                                                           return new Book[size];
                                                       }
                                                   };
}
