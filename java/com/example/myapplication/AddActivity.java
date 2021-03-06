package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    ImageView imageView;
    Button setImg, save;
    EditText name, type, des, alertStart, alertDelay, time, when;
    RadioGroup radioGroup;
    RadioButton in, out;
    String mName, mType, mDes, mWhere, mAlertStart, mAlertDelay, mTime, mWhen;
    Bitmap img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_add);

        name = findViewById(R.id.name);
        type = findViewById(R.id.kind);
        des = findViewById(R.id.intro);
        radioGroup = findViewById(R.id.radio);
        in = findViewById(R.id.in);
        out = findViewById(R.id.out);
        imageView = findViewById(R.id.imageView);
        save = findViewById(R.id.save);
        alertDelay = findViewById(R.id.period_et);
        alertStart = findViewById(R.id.alarmDate_et);
        time = findViewById(R.id.alarmTime_et);
        when = findViewById(R.id.adoptionDate_et);

        setImg = findViewById(R.id.imgAddButton);
        setImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        //????????? ?????? ????????? ????????????
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        //?????? ?????? ????????? ??? ???????????? ??? ????????? ??????
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = name.getText().toString();
                mType = type.getText().toString();
                mDes = des.getText().toString();
                mWhen = when.getText().toString();
                mAlertStart = alertStart.getText().toString();
                mAlertDelay = alertDelay.getText().toString();
                mTime = time.getText().toString();
                if (!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mType) &&
                        !TextUtils.isEmpty(mDes) && !TextUtils.isEmpty(mWhere) && !TextUtils.isEmpty(mAlertStart)
                        && !TextUtils.isEmpty(mAlertDelay) && !TextUtils.isEmpty(mTime) && !TextUtils.isEmpty(mWhen)) {
                    startNoti(Integer.parseInt(mAlertStart),Integer.parseInt(mTime));
                    saveData();
                    Toast.makeText(getBaseContext(), "????????????", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "?????? ??????????????????", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    //???????????????
    private void startNoti(int day , int time){
        Intent serviceIntent = new Intent(AddActivity.this,NotiService.class);
        serviceIntent.putExtra("day",day);
        serviceIntent.putExtra("time",time);
        startService(serviceIntent);
    }

    //????????? preference??? ??????
    public void saveData() {
        if (PreferenceManager.getInt(getBaseContext(), "size") != -1) {
            int size = PreferenceManager.getInt(getBaseContext(), "size");
            ArrayList<String> arrayList = new ArrayList<>();
            Log.e("sizefff", String.valueOf(size));
            PreferenceManager.setInt(getBaseContext(), "size", ++size);
            PreferenceManager.setString(getBaseContext(), "name" + size, mName);
            PreferenceManager.setString(getBaseContext(), "type" + size, mType);
            PreferenceManager.setString(getBaseContext(), "des" + size, mDes);
            PreferenceManager.setString(getBaseContext(), "when" + size, mWhen);
            PreferenceManager.setString(getBaseContext(), "alertStart" + size, mAlertStart);
            PreferenceManager.setString(getBaseContext(), "alertDelay" + size, mAlertDelay);
            PreferenceManager.setString(getBaseContext(), "time" + size, mTime);
            PreferenceManager.setString(getBaseContext(), "where" + size, mWhere);
            PreferenceManager.setBoolean(getBaseContext(), "ok" + size, true);
            if (img != null) {
                PreferenceManager.setString(getBaseContext(), "img" + size, bitmapToString(img));
            }
            PreferenceManager.setInt(getBaseContext(), "baseData" + size, size);
            int i = size - 1;
            Log.e("asdfff",
                    PreferenceManager.getString(getBaseContext(), "name" + i));
            Log.e("sizefff", String.valueOf(size));
        } else {
            PreferenceManager.setInt(getBaseContext(), "size", 0);
            PreferenceManager.setString(getBaseContext(), "name0", mName);
            PreferenceManager.setString(getBaseContext(), "type0", mType);
            PreferenceManager.setString(getBaseContext(), "des0", mDes);
            PreferenceManager.setString(getBaseContext(), "when0", mWhen);
            PreferenceManager.setString(getBaseContext(), "alertStart0", mAlertStart);
            PreferenceManager.setString(getBaseContext(), "alertDelay0", mAlertDelay);
            PreferenceManager.setString(getBaseContext(), "time0", mTime);
            PreferenceManager.setString(getBaseContext(), "where0", mWhere);
            PreferenceManager.setBoolean(getBaseContext(), "ok0", true);
            PreferenceManager.setInt(getBaseContext(), "baseData0", 0);
            if (img != null) {
                PreferenceManager.setString(getBaseContext(), "img0", bitmapToString(img));
            }
            Log.e("asdf", "asdfasdf");
        }
    }

    //????????? ???????????? ??? ??????
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (i == R.id.in) {
                mWhere = "??????";
            } else if (i == R.id.out) {
                mWhere = "??????";
            }
        }
    };

    //???????????? string??????
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }


    //????????? ????????? ????????????(???????????? ??????????????? result?????? ??????)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    img = BitmapFactory.decodeStream(in);
                    in.close();

                    imageView.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getBaseContext(), "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
            }
        }
    }
}