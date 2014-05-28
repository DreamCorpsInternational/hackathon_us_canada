package org.dreamcorps.lms;

import java.util.ArrayList;
import java.util.Random;

import org.dreamcorps.content.Book;
import org.dreamcorps.content.Constants;
import org.dreamcorps.ui.BookListviewActivity;
import org.dreamcorps.ui.ImagePagerActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

//https://github.com/nostra13/Android-Universal-Image-Loader

public class MainActivity extends Activity
{

 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView splashScreen = (ImageView) findViewById(R.id.mainSplashScreen);
        Random random = new Random();
        if(random.nextInt(10) > 5) {
            splashScreen.setImageResource(R.drawable.splashscreen);
        } else {
            splashScreen.setImageResource(R.drawable.yangyu);
        }
       
        
        splashScreen.setOnTouchListener(new View.OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startBookListviewActivity();
                return false;
            }
        });
        initImageLoader(getApplicationContext());

      
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
    
    private void startBookListviewActivity() {
        Intent intent = new Intent(this,BookListviewActivity.class);
        startActivity(intent);
    }

}
