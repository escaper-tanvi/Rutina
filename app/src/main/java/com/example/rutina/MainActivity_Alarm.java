package com.example.rutina;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class MainActivity_Alarm extends AppCompatActivity{

    private MaterialTimePicker picker;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    Button selectTimebtn, setAlarmbtn, cancelAlarmBtn;
    private Uri alarmsound;
    private TextView selectedTime;
    protected Bundle savedInstanceState;
    public View view;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alarm);
        setAlarmbtn = findViewById(R.id.setAlarmbt);
        selectTimebtn = findViewById(R.id.s);
        cancelAlarmBtn = findViewById(R.id.cancelAlarmBt);
        selectedTime = findViewById(R.id.selectedTim);
        setAlarmbtn.setBackgroundColor(Color.BLACK);
        selectTimebtn.setBackgroundColor(Color.BLACK);
        cancelAlarmBtn.setBackgroundColor(Color.BLACK);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        createNotificationChannel();


        selectTimebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showTimePicker();
            }
        });

        setAlarmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setAlarm();
            }
        });

        cancelAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cancelAlarm();
            }
        });
    }

    private void cancelAlarm() {

        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        if (alarmManager == null) {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show();
    }

    private void setAlarm() {

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show();

    }

    private void showTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();

        picker.show(getSupportFragmentManager(), "anaisha");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onClick(View v) {

                if (picker.getHour() > 12)
                    selectedTime.setText(
                            String.format("%02d", (picker.getHour() - 12)) + " : " + String.format("%02d", picker.getMinute()) + " PM "
                    );
                else {
                    selectedTime.setText(picker.getHour() + " : " + picker.getMinute() + " AM ");
                }
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

            }
        });

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name="Anaisha reminder app";
            String description ="Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("anaisha",name,importance);
            channel.setDescription(description);
            NotificationCompat.Builder mbuilder= new NotificationCompat.Builder(MainActivity_Alarm.this);
            NotificationManager notificationManager= getSystemService(NotificationManager.class);
            mbuilder.setSound(alarmsound);
            notificationManager.createNotificationChannel(channel);

        }
    }
}
