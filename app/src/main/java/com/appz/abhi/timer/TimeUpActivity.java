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

public class TimeUpActivity extends AppCompatActivity {

    Button ok_btn;
    Boolean vibration_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_up);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        vibration_flag = prefs.getBoolean("notifications_new_message_vibrate", true);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
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
                finish();
            }
        });
        if (vibration_flag) {
            long[] pattern = {0, 1000, 200}; //0 to start now, 200 to vibrate 200 ms, 0 to sleep for 0 ms.
            if (vibrator != null) {
                vibrator.vibrate(pattern, 0);
            }
        }
    }
}
