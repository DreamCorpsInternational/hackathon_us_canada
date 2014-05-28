package com.example.hackathon_us_canada;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookListviewAdapter extends ArrayAdapter<Book> {

	private Context context;
	private LayoutInflater inflater;
	private ArrayList<Book> bookList;
	
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	private static class ViewHolder {
		public ImageView img;
		public TextView name;
		public TextView desc;

	}
	
	
	public BookListviewAdapter(Context ctx, int layoutResourceID, ArrayList<Book> bookList) {
		super(ctx, layoutResourceID, bookList);
		this.context = ctx;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.bookList = bookList;
		
		
		setUpImageOptions();

	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder;
		
		if(convertView == null) {
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
		if(book != null) {
			viewHolder.name.setText(book.getName());
			viewHolder.desc.setText(book.getDescription());
			imageLoader.displayImage(book.getImageURL(), viewHolder.img, options, animateFirstListener);
		}
		
		return convertView;
	}
	
	private void setUpImageOptions() {
		this.options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
	}
}

class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

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
