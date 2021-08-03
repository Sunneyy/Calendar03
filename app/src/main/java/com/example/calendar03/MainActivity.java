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
    int lastYear;
    int lastMonth;
    int lastDay;

    String lastTime;

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        datePicker = findViewById(R.id.dp);
        btn_choose=findViewById(R.id.btn_choose);

        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        //获取上次查找日期
        mSharedPreferences=getSharedPreferences("lastDate",MODE_PRIVATE);
        mEditor=mSharedPreferences.edit();
        lastYear=mSharedPreferences.getInt("year",0);
        lastMonth=mSharedPreferences.getInt("month",0);
        lastDay=mSharedPreferences.getInt("day",0);
        if(lastYear!=0){
            currentYear=lastYear;
            currentMonth=lastMonth-1;
            currentDay=lastDay;
        }

        //初始化日期选择器并设置日期改变监听器
        datePicker.init(currentYear, currentMonth, currentDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //获取选中的年月日
                MainActivity.this.currentYear = year;
                //月份是从0开始的
                MainActivity.this.currentMonth = (monthOfYear+1);
                MainActivity.this.currentDay = dayOfMonth;
            }
        });

        btn_choose .setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //存储当前查询日期
                mEditor.putInt("year", datePicker.getYear());
                mEditor.putInt("month", datePicker.getMonth()+1);
                mEditor.putInt("day", datePicker.getDayOfMonth());
                mEditor.commit();
                String date = String.format("%d-%d-%d",datePicker.getYear(),datePicker.getMonth()+1,datePicker.getDayOfMonth());
                Intent intent=new Intent(MainActivity.this,DateActivity.class);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });



    }

    private void initView() {

        datePicker = findViewById(R.id.dp);
        btn_choose = findViewById(R.id.btn_choose);
    }


}
