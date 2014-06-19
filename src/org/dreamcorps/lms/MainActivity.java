package org.dreamcorps.lms;

import java.util.Random;

import org.dreamcorps.ui.BookListActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

//https://github.com/nostra13/Android-Universal-Image-Loader

public class MainActivity extends Activity
{
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_main);

        ImageView splashScreen = (ImageView) findViewById(R.id.mainSplashScreen);
        splashScreen.setBackgroundColor(Color.BLACK);
        Random random = new Random();
        if (random.nextInt(10) > 3) {
            splashScreen.setImageResource(R.drawable.splashscreen);
        } else {
            splashScreen.setImageResource(R.drawable.yangyu);
        }

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                startBookListviewActivity();
            }
        }, SPLASH_TIME_OUT);

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

    private void startBookListviewActivity() {
        Intent intent = new Intent(this, BookListActivity.class);
        startActivity(intent);
    }

}
