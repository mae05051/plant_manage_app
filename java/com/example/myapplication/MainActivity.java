package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    NotificationManager notificationManager;

    PendingIntent intent;

    Button add, all, in, out;
    ListView listView;
    boolean isOk;
    String mName, mType, mDes, mWhere, mAlertStart, mAlertDelay, mTime, mWhen;
    ArrayList<PlantData> dataAll = new ArrayList<>();
    ArrayList<PlantData> dataIn = new ArrayList<>();
    ArrayList<PlantData> dataOut = new ArrayList<>();
    MyAdapter myAdapterAll, myAdapterIn, myAdapterOut;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) //옵션메뉴 만들기
    {
        MenuInflater inflater = getMenuInflater();

        ((MenuInflater) inflater).inflate(R.menu.main_menu, menu);//팽창

        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)//메뉴 선택 오버라이드
    {
        Toast toast = Toast.makeText(getApplicationContext(),"", Toast.LENGTH_LONG);

        switch(item.getItemId())
        {
            case R.id.menu1:
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                break;
            case R.id.menu2:
                Intent settingIntent = new Intent(this, SettingActivity.class);
                startActivity(settingIntent);
                break;
        }

        toast.show();

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //뷰 연결
        add = findViewById(R.id.add);
        all = findViewById(R.id.all);
        in = findViewById(R.id.in);
        out = findViewById(R.id.out);
        listView = findViewById(R.id.plantList);
        intent = PendingIntent.getActivity(this, 0,
                new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        //기본은 all로 set
        listView.setAdapter(myAdapterAll);

        //모두보여주기
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listView.setAdapter(myAdapterAll);
                myAdapterAll.notifyDataSetChanged();
            }
        });
        //실내만 보여주기
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setAdapter(myAdapterIn);
                myAdapterIn.notifyDataSetChanged();

            }
        });
        //실외만 보여주기
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setAdapter(myAdapterOut);
                myAdapterOut.notifyDataSetChanged();

            }
        });
        //더하기 액티비티로 intent
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AddActivity.class);
                startActivity(intent);
            }
        });

        //아이템 클릭했을 때
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ShowActivity.class);
                intent.putExtra("chose", position);
                startActivity(intent);
            }
        });

        //길게 눌렀을 때
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("알림");
                builder.setMessage("삭제하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                PreferenceManager.setBoolean(getApplicationContext(), "ok" + position, false);
                                Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                getData();
                                setAdapter();
                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "삭제 취소", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();
                return false;
            }
        });

    }

    //어댑터 만들어주고 초기화해주기
    private void setAdapter() {
        myAdapterAll = new MyAdapter(getBaseContext(), dataAll);
        myAdapterIn = new MyAdapter(getBaseContext(), dataIn);
        myAdapterOut = new MyAdapter(getBaseContext(), dataOut);
        myAdapterOut.notifyDataSetChanged();
        myAdapterAll.notifyDataSetChanged();
        myAdapterIn.notifyDataSetChanged();

    }

    //preference에서 해당되는 값 만큼 받아오기
    private void getData() {
        dataOut = new ArrayList<>();
        dataIn = new ArrayList<>();
        dataAll = new ArrayList<>();
        if (PreferenceManager.getInt(getBaseContext(), "size") != -1) {
            int size = PreferenceManager.getInt(getBaseContext(), "size");
            for (int i = 0; i <= size; i++) {
                Log.e("asdf", String.valueOf(i));
                mName = PreferenceManager.getString(getBaseContext(), "name" + i);
                mType = PreferenceManager.getString(getBaseContext(), "type" + i);
                mDes = PreferenceManager.getString(getBaseContext(), "des" + i);
                mWhen = PreferenceManager.getString(getBaseContext(), "when" + i);
                mAlertStart = PreferenceManager.getString(getBaseContext(), "alertStart" + i);
                mAlertDelay = PreferenceManager.getString(getBaseContext(), "alertDelay" + i);
                mTime = PreferenceManager.getString(getBaseContext(), "time" + i);
                mWhere = PreferenceManager.getString(getBaseContext(), "where" + i);
                Log.e("name" + i, mName);

                long now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                // 시간을 나타냇 포맷을 정한다
                SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss");
                // nowDate 변수에 값을 저장한다.
                String formatDate = sdfNow.format(date);
                if(formatDate.equals(mTime)){
                }

                if (PreferenceManager.getBoolean(getBaseContext(), "ok" + i)) {
                    dataAll.add(new PlantData(mName, mType, mDes, mWhen, mAlertStart, mAlertDelay, mTime, mWhere, i));
                    if (mWhere.equals("실내")) {
                        dataIn.add(new PlantData(mName, mType, mDes, mWhen, mAlertStart, mAlertDelay, mTime, mWhere, i));
                    } else {
                        dataOut.add(new PlantData(mName, mType, mDes, mWhen, mAlertStart, mAlertDelay, mTime, mWhere, i));
                    }
                }
            }
        }
    }

    //생명주기 컴포넌트 이용해서 바로 생길 때 set
    @Override
    protected void onResume() {
        super.onResume();
        getData();
        setAdapter();
    }


}