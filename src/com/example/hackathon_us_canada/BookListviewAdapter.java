package com.example.hackathon_us_canada;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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
		}
		
		return convertView;
	}
}
