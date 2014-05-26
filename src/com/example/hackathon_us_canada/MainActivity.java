package com.example.hackathon_us_canada;


import java.util.ArrayList;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.os.Build;

public class MainActivity extends Activity {
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       
        
      
        ArrayList<Book> bookList = getBookList();
        BookListviewAdapter adapter = new BookListviewAdapter(this, R.layout.book_row_layout,bookList);
        ListView bookListView = (ListView) findViewById(R.id.bookListview);
        bookListView.setAdapter(adapter);
    }

    private ArrayList<Book> getBookList() {
    	 String[] bookNames = new String[]{"1","2"};
         String[] bookDescp = new String[]{"desciption1111","description2222"};
         
         ArrayList<Book> bookList = new ArrayList<Book>();
         for(int i = 0; i < bookNames.length; i++) {
         	bookList.add((new Book(bookNames[i],bookDescp[i],"http://icons.iconarchive.com/icons/robinweatherall/library/256/book-open-icon.png")));
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    

}
