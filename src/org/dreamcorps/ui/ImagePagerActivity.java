package org.dreamcorps.ui;

import java.util.ArrayList;

import org.dreamcorps.content.Book;
import org.dreamcorps.content.Constants;
import org.dreamcorps.lms.C;
import org.dreamcorps.lms.R;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import ca.dragonflystudios.content.model.Collection;
import ca.dragonflystudios.content.model.Model;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ImagePagerActivity extends Activity
{

    private static final String STATE_POSITION = "STATE_POSITION";

    DisplayImageOptions         options;

    ViewPager                   pager;
    ArrayList<Book>             bookList;
    boolean mDidSetInitialPosition;
    int mInitialPosition;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ImagePagerAdapter pagerAdapter = new ImagePagerAdapter();
        getLoaderManager().initLoader(ImagePagerAdapter.BOOKDETAIL_LOADER_ID, null, pagerAdapter);
        mDidSetInitialPosition = false;

        setContentView(R.layout.bookcover_pager);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        bookList = (ArrayList<Book>) bundle.getSerializable(Constants.bookList);
        mInitialPosition = bundle.getInt(Constants.position, 0);

        if (savedInstanceState != null) {
            mInitialPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
    }

    private class ImagePagerAdapter extends PagerAdapter implements LoaderManager.LoaderCallbacks<Cursor>
    {

        // private String[] images;
        private LayoutInflater inflater;
        private ImageLoader    imageLoader = ImageLoader.getInstance();

        private Cursor mCursor;
        protected static final int BOOKDETAIL_LOADER_ID = 2;

        ImagePagerAdapter()
        {
            // this.images = images;
            inflater = getLayoutInflater();
        }

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
        {
            switch (i) {
                case BOOKDETAIL_LOADER_ID:
                    Collection collection = Model.getModelByAuthority(C.DREAMCORPS_AUTHORITY).getCollectionByName(C.COLLECTION_NAME_BOOKS);
                    return new CursorLoader(ImagePagerActivity.this, collection.getUri(), collection.itemFieldNamesWithId, null, null, null);
                default:
                    throw new RuntimeException("Invalid loader id: " + i);
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
        {
            switch (cursorLoader.getId()) {
                case BOOKDETAIL_LOADER_ID:
                    mCursor = cursor;
                    if (!mDidSetInitialPosition) {
                        mDidSetInitialPosition = true;
                        notifyDataSetChanged();
                        pager.setCurrentItem(mInitialPosition);
                    }
                    return;
                default:
                    throw new RuntimeException("Invalid loader id: " + cursorLoader.getId());
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader)
        {
            switch (cursorLoader.getId()) {
                case BOOKDETAIL_LOADER_ID:
                    mCursor = null;
                    notifyDataSetChanged();
                    return;
                default:
                    throw new RuntimeException("Invalid loader id: " + cursorLoader.getId());
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.pager_image);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            final TextView titleView = (TextView) imageLayout.findViewById(R.id.title);
            final TextView summaryView = (TextView) imageLayout.findViewById(R.id.summary);

            mCursor.moveToPosition(position);
            final String imageUrl = mCursor.getString(mCursor.getColumnIndex(C.field.imageLarge));
            imageLoader.displayImage(imageUrl, imageView, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "Input/Output error";
                        break;
                    case DECODING_ERROR:
                        message = "Image can't be decoded";
                        break;
                    case NETWORK_DENIED:
                        message = "Downloads are denied";
                        break;
                    case OUT_OF_MEMORY:
                        message = "Out Of Memory error";
                        break;
                    case UNKNOWN:
                        message = "Unknown error";
                        break;
                    }
                    Toast.makeText(ImagePagerActivity.this, message, Toast.LENGTH_SHORT).show();

                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                }
            });
            final String title = mCursor.getString(mCursor.getColumnIndex(C.field.title));
            final String summary = mCursor.getString(mCursor.getColumnIndex(C.field.summary));
            titleView.setText(title);
            summaryView.setText(summary);

            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public int getCount() {
            if (null != mCursor)
                return mCursor.getCount();
            
            return 0;
        }
    }
}
