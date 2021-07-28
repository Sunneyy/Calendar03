package com.example.calendar03;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendar03.http.Impl.RequestTask;
import com.example.calendar03.http.util.ThreadPoolUtil;
import com.example.calendar03.util.ConfigUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String fileName= ("urlConfig.properties");
    //private Context mContext;
    //Map<String, String> configMap = new HashMap<String, String> ();
    private DatePicker datePicker;
    private Button btn_choose;
    String dateStr=new String();

    int currentYear;
    int currentMonth;
    int currentDay;
    int lastYear;
    int lastMonth;
    int lastDay;

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if(msg.what==0){
                dateStr=msg.obj.toString();
                Intent intent=new Intent();
                intent.putExtra("date",dateStr);
                intent.setClass(MainActivity.this, DateActivity.class);
                MainActivity.this.startActivity(intent);
                //tv_date.setText(str);
            }
        }

        ;
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                //日期提示
                //Toast.makeText(MainActivity.this,MainActivity.this.currentYear+"年"+MainActivity.this.currentMonth+"月"+MainActivity.this.currentDay+"日",Toast.LENGTH_SHORT).show();
            }
        });




        btn_choose .setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Map<String, String> paramMap = new HashMap<>();
                String url= ConfigUtils.getValueFromFile(MainActivity.this,fileName,"url");
                String appKey=ConfigUtils.getValueFromFile(MainActivity.this,fileName,"appKey");

                //String url="http://v.juhe.cn/calendar/day";
                //String appKey="2d0163d34b5e3f7285e65f3160b9cded";
//                String url=configMap.getOrDefault("url",null);
//                String appKey=configMap.getOrDefault("appKey",null);

                //存储当前查询日期
                mEditor.putInt("year", datePicker.getYear());
                mEditor.putInt("month", datePicker.getMonth()+1);
                mEditor.putInt("day", datePicker.getDayOfMonth());
                mEditor.commit();
                String date = String.format("%d-%d-%d",datePicker.getYear(),datePicker.getMonth()+1,datePicker.getDayOfMonth());

                paramMap.put("date", date);
                paramMap.put("key", appKey);
                ThreadPoolUtil.getInstance().submit(new RequestTask("POST", url, paramMap,handler));

            }
        });



    }

}
