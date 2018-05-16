package com.appz.abhi.timer;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class TimeUpActivity extends AppCompatActivity {

    //  UI components
    Button ok_btn;

    //  Data variables
    Boolean vibration_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_up);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        vibration_flag = prefs.getBoolean("key_vibrate", true);

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //  Linking UI components
        RelativeLayout relativeLayout = findViewById(R.id.time_up_layout_id);
        relativeLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        ok_btn = findViewById(R.id.ok_btn_id);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = AlarmReceiver.mediaPlayer;
                mediaPlayer.stop();
                if (vibration_flag) {
                    if (vibrator != null) {
                        vibrator.cancel();
                    }
                }
                MainActivity.start_timer_btn.performClick();
                finish();
            }
        });

        if (vibration_flag) {
            if (vibrator != null) {
                long[] pattern = {0, 1000, 200};
                //  0 to start now, 200 to vibrate 200 ms, 0 to sleep for 0 ms.
                vibrator.vibrate(pattern, 0);
            }
        }
    }
}
