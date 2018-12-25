package com.example.duyphuong.playermp3.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.example.duyphuong.playermp3.R;

public class IntroduceActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 1500;
    private static boolean splashLoaded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (splashLoaded == false) {
            setContentView(R.layout.activity_introduce);
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(IntroduceActivity.this,MainActivity.class);
                    startActivity(mainIntent);
                    splashLoaded = true;
                }
            }, SPLASH_DISPLAY_LENGTH);
        } else  {
            Intent goToMainActivity = new Intent(IntroduceActivity.this, MainActivity.class);
            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToMainActivity);
        }
    }
}
