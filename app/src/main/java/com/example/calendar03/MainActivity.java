package com.example.calendar03;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.calendar03.util.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private DatePicker datePicker;
    private Button btn_choose;

    int currentYear;
    int currentMonth;
    int currentDay;

    String lastTime;

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        btn_choose .setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //存储当前查询日期
                mEditor=mSharedPreferences.edit();
                int year=datePicker.getYear();
                int month=datePicker.getMonth()+1;
                int day=datePicker.getDayOfMonth();
                mEditor.putString("time",year+"-"+month+"-"+day);
                mEditor.commit();
                String date1 = String.format("%d-%d-%d",datePicker.getYear(),datePicker.getMonth()+1,datePicker.getDayOfMonth());
                Intent intent=new Intent(MainActivity.this,DateActivity.class);
                intent.putExtra("date",date1);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        datePicker = findViewById(R.id.dp);
        btn_choose=findViewById(R.id.btn_choose);

        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String time=format.format(calendar.getTime());

        //获取上次查找的日期
        mSharedPreferences=getSharedPreferences("lastDate",MODE_PRIVATE);
        lastTime=mSharedPreferences.getString("time",null);
        if(lastTime==null){
            mEditor=mSharedPreferences.edit();
            mEditor.putString("time",time);
            mEditor.commit();
        }else{
            Date date=new Date();
            try {
                date=format.parse(lastTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar1=Calendar.getInstance();
            calendar1.setTime(date);
            currentYear=calendar1.get(Calendar.YEAR);
            currentMonth=calendar1.get(Calendar.MONTH)+1;
            currentDay=calendar1.get(Calendar.DAY_OF_MONTH);
        }
        //初始化日期选择器
        datePicker.init(currentYear, currentMonth - 1, currentDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        });
    }


}
