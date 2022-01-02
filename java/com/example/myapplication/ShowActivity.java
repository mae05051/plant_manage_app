package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowActivity extends AppCompatActivity {

    ImageView imageView;
    TextView name, type, des, alertStart, alertDelay, time, when, where;
    Bitmap img;
    int chose = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent intent = getIntent();
        chose = intent.getIntExtra("chose", -1);

        imageView = findViewById(R.id.imageView);
        name = findViewById(R.id.nameTextView);
        type = findViewById(R.id.kindTextView);
        des = findViewById(R.id.introView);
        when = findViewById(R.id.dateView);
        where = findViewById(R.id.textView);
        alertStart = findViewById(R.id.alarmStartDate);
        alertDelay = findViewById(R.id.textAlarmPeriod);
        time = findViewById(R.id.textAlarmTime);

        //가져온 데이터 넣어주기
        name.setText(name.getText().toString() +": "+PreferenceManager.getString(getBaseContext(), "name"+chose));
        type.setText(type.getText().toString() +": "+PreferenceManager.getString(getBaseContext(), "type"+chose));
        des.setText(des.getText().toString() +": "+PreferenceManager.getString(getBaseContext(), "des"+chose));
        when.setText(when.getText().toString() +": "+PreferenceManager.getString(getBaseContext(), "when"+chose));
        where.setText(where.getText().toString() +": "+PreferenceManager.getString(getBaseContext(), "where"+chose));
        alertStart.setText(alertStart.getText().toString() +": "+PreferenceManager.getString(getBaseContext(), "alertStart"+chose));
        alertDelay.setText(alertDelay.getText().toString() +": "+PreferenceManager.getString(getBaseContext(), "alertDelay"+chose)+"일 마다");
        time.setText(time.getText().toString() +": "+PreferenceManager.getString(getBaseContext(), "time"+chose)+"시");
        img = stringToBitmap(PreferenceManager.getString(getBaseContext(), "img"+chose));
        imageView.setImageBitmap(img);
    }

    //string을 비트맵으로
    public static Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}