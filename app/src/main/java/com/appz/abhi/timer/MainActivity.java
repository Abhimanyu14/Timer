package com.appz.abhi.timer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NumberPicker.OnScrollListener, View.OnClickListener {

    //  UI components
    static Button start_timer_btn;
    TextView timer_tv;
    NumberPicker hour_np, min_np, sec_np;

    //  Data variables
    CountDownTimer countDownTimer;
    int milli_seconds;
    String s;

    //  Alarm Manager
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Linking UI components
        start_timer_btn = findViewById(R.id.start_timer_btn_id);
        timer_tv = findViewById(R.id.timer_tv_id);

        hour_np = findViewById(R.id.hour_np_id);
        min_np = findViewById(R.id.min_np_id);
        sec_np = findViewById(R.id.sec_np_id);

        //  Populate Number Picker
        String[] hour_data = new String[24];
        for (int i = 0; i < 24; i++) {
            hour_data[i] = String.format(Locale.getDefault(), "%02d", i);
        }
        String[] minute_data = new String[60];
        for (int i = 0; i < 60; i++) {
            minute_data[i] = String.format(Locale.getDefault(), "%02d", i);
        }

        hour_np.setMinValue(0);
        hour_np.setMaxValue(hour_data.length - 1);
        hour_np.setDisplayedValues(hour_data);

        min_np.setMinValue(0);
        min_np.setMaxValue(minute_data.length - 1);
        min_np.setDisplayedValues(minute_data);

        sec_np.setMinValue(0);
        sec_np.setMaxValue(minute_data.length - 1);
        sec_np.setDisplayedValues(minute_data);

        //  Add Listeners
        hour_np.setOnScrollListener(this);
        min_np.setOnScrollListener(this);
        sec_np.setOnScrollListener(this);

        start_timer_btn.setOnClickListener(this);
    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {
        int id = view.getId();

        String hour_str = timer_tv.getText().toString().substring(0, 2);
        String min_str = timer_tv.getText().toString().substring(3, 5);
        String sec_str = timer_tv.getText().toString().substring(6, 8);

        int hour = Integer.parseInt(hour_str);
        int min = Integer.parseInt(min_str);
        int sec = Integer.parseInt(sec_str);

        String hourStr = String.format(Locale.getDefault(), "%02d", (hour));
        String minStr = String.format(Locale.getDefault(), "%02d", (min));
        String secStr = String.format(Locale.getDefault(), "%02d", (sec));

        switch (id) {
            case R.id.hour_np_id:
                s = String.format(Locale.getDefault(), "%02d", (hour_np.getValue()))
                        + ":" + minStr + ":" + secStr;
                timer_tv.setText(s);
                milli_seconds = (((hour_np.getValue() * 60) + min) * 60 + sec) * 1000;
                break;

            case R.id.min_np_id:
                s = hourStr + ":" + String.format(Locale.getDefault(),
                        "%02d", (min_np.getValue())) + ":" + secStr;
                timer_tv.setText(s);
                milli_seconds = (((hour * 60) + min_np.getValue()) * 60 + sec) * 1000;
                break;

            case R.id.sec_np_id:
                s = hourStr + ":" + minStr + ":" + String.format(Locale.getDefault(),
                        "%02d", (sec_np.getValue()));
                timer_tv.setText(s);
                milli_seconds = (((hour * 60) + min) * 60 + sec_np.getValue()) * 1000;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (start_timer_btn.getText().toString()) {
            case "START": {
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent
                        .getBroadcast(getApplicationContext(), 234324243, intent, 0);
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + milli_seconds, pendingIntent);
                countDownTimer = new CountDownTimer(milli_seconds + 1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        int hour_rem = (int) (millisUntilFinished / (1000 * 60 * 60));
                        int min_rem = (int) ((millisUntilFinished / (1000 * 60)) - (hour_rem * 60));
                        int sec_rem = (int) ((millisUntilFinished / 1000) - (hour_rem * 60 * 60) - (min_rem * 60));
                        String hourStr = String.format(Locale.getDefault(), "%02d", (hour_rem));
                        String minStr = String.format(Locale.getDefault(), "%02d", (min_rem));
                        String secStr = String.format(Locale.getDefault(), "%02d", (sec_rem));
                        s = hourStr + ":" + minStr + ":" + secStr;
                        timer_tv.setText(s);
                    }

                    public void onFinish() {
                        start_timer_btn.setText(getResources().getText(R.string.reset));
                    }
                };
                start_timer_btn.setText(getResources().getText(R.string.stop));
                hour_np.setEnabled(false);
                min_np.setEnabled(false);
                sec_np.setEnabled(false);
                countDownTimer.start();
                break;
            }
            case "STOP": {
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent
                        .getBroadcast(getApplicationContext(), 234324243, intent, 0);
                alarmManager.cancel(pendingIntent);
                countDownTimer.cancel();
                start_timer_btn.setText(getResources().getText(R.string.reset));
                break;
            }
            case "RESET": {
                start_timer_btn.setText(getResources().getText(R.string.start));
                hour_np.setValue(0);
                min_np.setValue(0);
                sec_np.setValue(0);
                hour_np.setEnabled(true);
                min_np.setEnabled(true);
                sec_np.setEnabled(true);
                timer_tv.setText(getResources().getText(R.string._00_00_00));
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
