package com.appz.abhi.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AlarmReceiver extends BroadcastReceiver {

    static MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String alarms = prefs.getString("key_ringtone", "");
        Uri alarmRingtoneUri = Uri.parse(alarms);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(context, alarmRingtoneUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent1 = new Intent(context, TimeUpActivity.class);
        intent1.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
