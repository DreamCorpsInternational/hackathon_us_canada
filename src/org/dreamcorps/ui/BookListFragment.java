package org.dreamcorps.ui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.dreamcorps.content.CheckISBN13;
import org.dreamcorps.content.Constants;
import org.dreamcorps.content.ISBNList;
import org.dreamcorps.lms.C;
import org.dreamcorps.lms.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import ca.dragonflystudios.content.model.Collection;
import ca.dragonflystudios.content.model.Model;
import ca.dragonflystudios.content.service.Service;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class BookListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private SimpleCursorAdapter  mBooksAdapter;
    private ImageLoader          mImageLoader;
    private DisplayImageOptions  mOptions;
    private ImageLoadingListener mAnimateFirstListener = new AnimateFirstDisplayListener();

    private final static String  FIRST_START_KEY       = "is first start";
    
    OnBookSelectedListener mCallback;
    
    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnBookSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onBookSelected(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (prefs.getBoolean(FIRST_START_KEY, true)) {
            prefs.edit().putBoolean(FIRST_START_KEY, false);
            for (String isbn : ISBNList.isbnArray)
                requestBookInfo(this, isbn);
        }


        initImageLoader(getActivity());
        mImageLoader = ImageLoader.getInstance();
        mOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(20)).build();

        getLoaderManager().initLoader(BOOKINFO_LOADER_ID, null, this);
        mBooksAdapter = new SimpleCursorAdapter(getActivity(), R.layout.book_row_layout, null, new String[] { C.field.title, C.field.isbn,
                C.field.imageSmall, C.field.author, C.field.publisher, C.field.pubDate }, new int[] { R.id.title, R.id.isbn,
                R.id.thumbnail, R.id.author, R.id.publisher, R.id.pubdate }, 0) {
            public void setViewImage(ImageView v, String value) {
                mImageLoader.displayImage(value, v, mOptions, mAnimateFirstListener);
            }
        };

//        ListView bookListView = (ListView) getView().findViewById(R.id.bookListview);
//        if(bookListView == null) {
//            Log.w("FEVER","NULLLL");
//        }
//        bookListView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startImagePagerActivity(position);
//            }
//        });
        View rootView =  inflater.inflate(R.layout.book_listview, container, false);
        ListView bookListView = (ListView) rootView.findViewById(R.id.bookListview);
        bookListView.setOnItemClickListener(new OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  mCallback.onBookSelected(position);
              }
          });
        bookListView.setAdapter(mBooksAdapter);
        return rootView;
    }
    
    

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnBookSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnBookSelectedListener");
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("图书录入");
            alert.setMessage("请输入ISBN");

            final EditText input = new EditText(getActivity());
            alert.setView(input);

            alert.setPositiveButton("确认", null);
            alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });

            final AlertDialog alertDialog = alert.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String isbn = input.getEditableText().toString();
                            CheckISBN13 code1 = new CheckISBN13(isbn);
                            if (!code1.isValid()) {
                                alertDialog.setMessage("ISBN输入验证失败! 请输入有效的ISBN");
                            } else {
                                BookListActivity.requestBookInfo(getActivity(), isbn);
                                alertDialog.dismiss();
                            }
                        }
                    });
                }
            });

            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static final int BOOKINFO_LOADER_ID = 1;

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch (i) {
        case BOOKINFO_LOADER_ID:
            Collection collection = Model.getModelByAuthority(C.DREAMCORPS_AUTHORITY).getCollectionByName(C.COLLECTION_NAME_BOOKS);
            return new CursorLoader(this.getActivity(), collection.getUri(), collection.itemFieldNamesWithId, null, null, null);
        default:
            throw new RuntimeException("Invalid loader id: " + i);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        switch (cursorLoader.getId()) {
        case BOOKINFO_LOADER_ID:
            mBooksAdapter.swapCursor(cursor);
            return;
        default:
            throw new RuntimeException("Invalid loader id: " + cursorLoader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        switch (cursorLoader.getId()) {
        case BOOKINFO_LOADER_ID:
            mBooksAdapter.swapCursor(null);
            return;
        default:
            throw new RuntimeException("Invalid loader id: " + cursorLoader.getId());
        }
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this); method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
                                                                                 // for
                                                                                 // release
                                                                                 // app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public static void requestBookInfo(BookListFragment bookListFragment, String isbn) {
        Collection collection = Model.getModelByAuthority(C.DREAMCORPS_AUTHORITY).getCollectionByName(C.COLLECTION_NAME_BOOKS);
        final Intent intent = new Intent(bookListFragment.getActivity() , Service.class);
        intent.putExtra(Service.KEY_COLLECTION_ID, collection.getId());
        intent.putExtra(Service.KEY_SELECTION_ARGS, new String[] { isbn });
        bookListFragment.getActivity().startService(intent);
    }

    private void startImagePagerActivity(int position) {
        Intent intent = new Intent(this.getActivity() , ImagePagerActivity.class);
        intent.putExtra(Constants.position, position);
        startActivity(intent);
    }
    
    
    

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener
    {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
