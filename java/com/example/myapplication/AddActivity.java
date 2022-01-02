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
        //라디오 그룹 리스너 달아주기
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        //저장 버튼 눌렀을 시 텍스트가 다 있으면 저장
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
                    Toast.makeText(getBaseContext(), "저장완료", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "모두 입력해주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    //알림서비스
    private void startNoti(int day , int time){
        Intent serviceIntent = new Intent(AddActivity.this,NotiService.class);
        serviceIntent.putExtra("day",day);
        serviceIntent.putExtra("time",time);
        startService(serviceIntent);
    }

    //데이터 preference에 저장
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

    //아이디 구분으로 값 설정
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (i == R.id.in) {
                mWhere = "실내";
            } else if (i == R.id.out) {
                mWhere = "실외";
            }
        }
    };

    //비트맵을 string으로
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }


    //갤러리 이미지 받아오기(생명주기 컴포넌트로 result에서 처리)
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
                Toast.makeText(getBaseContext(), "사진 선택 취소", Toast.LENGTH_SHORT).show();
            }
        }
    }
}