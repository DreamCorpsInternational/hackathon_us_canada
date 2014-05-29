package org.dreamcorps.content;

import org.dreamcorps.lms.C;

import ca.dragonflystudios.content.model.Collection;
import ca.dragonflystudios.content.model.Model;
import ca.dragonflystudios.content.provider.Provider;
import ca.dragonflystudios.content.processor.json.JsonPath;

/**
 * Created by jun on 2014-04-19.
 */

public class LmsContentProvider extends Provider
{
    static {
        // Do app-specific static initialization in this app-specific class
        // Note that the constructors for Collection and Model self-register the instances created
        final Collection DoubanBooks = new Collection(C.COLLECTION_NAME_BOOKS, C.bookFields,
                "https://api.douban.com/v2/book/isbn/%s", null,
                new JsonPath[]{new JsonPath("id"), new JsonPath("title"), new JsonPath("author"), new JsonPath("isbn13"), new JsonPath("summary"), new JsonPath("images", "small"), new JsonPath("images", "large") })        {
            @Override
            public String getUrl(String[] selectionArgs)
            {
                return String.format(url, (Object[])selectionArgs);
            }
        };

        final Model DoubanModel = new Model(C.DOUBAN_MODEL_NAME, C.DOUBAN_AUTHORITY, 1, new Collection[]{ DoubanBooks });
    }
}
