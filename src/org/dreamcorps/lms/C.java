package org.dreamcorps.lms;

/**
 * Created by jun on 2014-04-26.
 */

public class C
{
    public final static String DREAMCORPS_MODEL_NAME = "Dream Corps Books";
    public final static String DREAMCORPS_AUTHORITY = "org.dreamcorps.content"; // R.string.dreamcorps_authority
    public final static String COLLECTION_NAME_BOOKS = "books";

    public final static class field
    {
        public final static String id = "id";
        public final static String title = "title";
        public final static String author = "author";
        public final static String isbn = "isbn";
        public final static String summary = "summary";
        public final static String imageSmall = "imageSmall";
        public final static String imageLarge = "imageLarge";
    }

    public final static String[] bookFields = { field.id, field.title, field.author, field.isbn, field.summary, field.imageSmall, field.imageLarge };
}
