package com.quizflix.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import com.quizflix.R;
import com.quizflix.Util.Util;


/**
 * Created by kavasthi on 12/13/2016.
 */

public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String regid = "";
    private SharedPreferences mSharedPreferences;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        mTextView=(TextView)findViewById(R.id.timer);
        mSharedPreferences = Util.getSharedPreferences(this);
        final boolean isLogIn = mSharedPreferences.getBoolean("isLogin", false);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(isLogIn)
                {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
