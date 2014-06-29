package org.dreamcorps.lms;

import java.util.Random;

import org.dreamcorps.content.Constants;
import org.dreamcorps.ui.BookListActivity;
import org.dreamcorps.ui.BookListFragment;
import org.dreamcorps.ui.ImagePagerFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

//https://github.com/nostra13/Android-Universal-Image-Loader

public class MainActivity extends FragmentActivity
                          implements BookListFragment.OnBookSelectedListener 
{

    private BookListFragment listFrag;
    private ImagePagerFragment pagerFrag;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //getActionBar().hide();
        setContentView(R.layout.activity_main);
        

        listFrag  = new BookListFragment();
        pagerFrag = new ImagePagerFragment();
        
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, listFrag).commit();

        initSplashScreen();
        initImageLoader(getApplicationContext());

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

    
    public void onBookSelected(int position) {
        ImagePagerFragment imgPagerFrag = (ImagePagerFragment)getFragmentManager().findFragmentById(R.id.ImagePagerFragment);
        if(imgPagerFrag != null) {
            // In two panel layout, most likely a large screen
            Log.w("Fever","1");
        } else {
            // In one panel layout
            switchToImagePagerFragment(position);
        }
    }
    
    private void initSplashScreen() {
        ImageView splashScreen = (ImageView) findViewById(R.id.mainSplashScreen);
        splashScreen.setBackgroundColor(Color.BLACK);
        Random random = new Random();
        if (random.nextInt(10) > 3) {
            splashScreen.setImageResource(R.drawable.splashscreen);
        } else {
            splashScreen.setImageResource(R.drawable.yangyu);
        }
        
        splashScreen.setVisibility(View.GONE); 

//        splashScreen.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//               // startBookListviewActivity();
//               // fragmentTransaction.replace(android.R.id.content, bookListFragment);
//               // splashScreen.setVisibility(View.GONE); 
//                return false;
//            }
//        });
    }
    
    private void switchToImagePagerFragment(int position) {
        
        Bundle args = new Bundle();
        args.putInt(Constants.position, position);

        ImagePagerFragment newFrag = new ImagePagerFragment();
        newFrag.setArguments(args);
        
     
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(listFrag);
        transaction.replace(R.id.container, newFrag);
        transaction.addToBackStack(null);
        
        transaction.commit();
    }

}
