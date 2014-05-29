package org.dreamcorps.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.dreamcorps.content.Book;
import org.dreamcorps.content.Constants;
import org.dreamcorps.lms.C;
import org.dreamcorps.lms.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

public class BookListviewActivity extends Activity
{

    private ArrayList<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_listview);

        initImageLoader(getApplicationContext());

        bookList = getBookList();
        BookListviewAdapter adapter = new BookListviewAdapter(this, R.layout.book_row_layout, bookList);
        ListView bookListView = (ListView) findViewById(R.id.bookListview);
        bookListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startImagePagerActivity(position);
            }
        });
        bookListView.setAdapter(adapter);
    }

    private ArrayList<Book> getBookList() {
        String[] bookNames = new String[] { "1", "2" };
        String[] bookDescp = new String[] { "desciption1111", "description2222" };

        ArrayList<Book> bookList = new ArrayList<Book>();
        for (int i = 0; i < bookNames.length; i++) {
            bookList.add((new Book(bookNames[i], bookDescp[i],
                    "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg")));
        }
        return bookList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("图书录入");
            alert.setMessage("请输入ISBN");

            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String isbn = input.getEditableText().toString();
                    BookListviewActivity.requestBookInfo(BookListviewActivity.this, isbn);
                }
            });
            alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
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

    public static void requestBookInfo(Context context, String isbn) {
        Collection collection = Model.getModelByAuthority(C.DREAMÇORPS_AUTHORITY).getCollectionByName(C.COLLECTION_NAME_BOOKS);
        final Intent intent = new Intent(context, Service.class);
        intent.putExtra(Service.KEY_COLLECTION_ID, collection.getId());
        intent.putExtra(Service.KEY_SELECTION_ARGS, new String[] { isbn });
        context.startService(intent);
    }

    private void startImagePagerActivity(int position) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        intent.putExtra(Constants.bookList, bookList);
        intent.putExtra(Constants.position, position);
        startActivity(intent);
    }

    class BookListviewAdapter extends ArrayAdapter<Book>
    {

        private Context              context;
        private LayoutInflater       inflater;
        private ArrayList<Book>      bookList;

        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private ImageLoader          imageLoader          = ImageLoader.getInstance();
        DisplayImageOptions          options;

        private class ViewHolder
        {
            public ImageView img;
            public TextView  name;
            public TextView  desc;

        }

        public BookListviewAdapter(Context ctx, int layoutResourceID, ArrayList<Book> bookList)
        {
            super(ctx, layoutResourceID, bookList);
            this.context = ctx;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.bookList = bookList;

            setUpImageOptions();

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;

            if (convertView == null) {
                // inflate the layout
                convertView = inflater.inflate(R.layout.book_row_layout, null);

                viewHolder = new ViewHolder();
                viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
                viewHolder.desc = (TextView) convertView.findViewById(R.id.desc);
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Book book = getItem(position);
            if (book != null) {
                viewHolder.name.setText(book.getName());
                viewHolder.desc.setText(book.getDescription());
                imageLoader.displayImage(book.getImageURL(), viewHolder.img, options, animateFirstListener);
            }

            return convertView;
        }

        private void setUpImageOptions() {
            this.options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
                    .cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(20)).build();
        }
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
