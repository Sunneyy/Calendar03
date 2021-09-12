package com.example.calendar03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendar03.gson.BaseResult;
import com.example.calendar03.gson.CalendarData;
import com.example.calendar03.http.RequestTask;
import com.example.calendar03.http.ResponseListener;
import com.example.calendar03.http.util.ThreadPoolUtil;
import com.example.calendar03.util.ConfigUtils;
import com.example.calendar03.util.DateUtils;

import java.util.HashMap;
import java.util.Map;

public class DateActivity extends AppCompatActivity {
    String fileName= ("urlConfig.properties");//配置文件
    private TextView greg_calendar;
    private TextView lunar_calendar;
    private TextView suit;
    private TextView avoid;

    Map<String, String> paramMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        Intent intent=getIntent();
        String date=intent.getStringExtra("date");
        String url= ConfigUtils.getValueFromFile(DateActivity.this,fileName,"url");
        String appKey=ConfigUtils.getValueFromFile(DateActivity.this,fileName,"appKey");
        paramMap.put("date", date);
        paramMap.put("key", appKey);
        RequestTask<CalendarData> task = new RequestTask<>("POST", url, paramMap);
        task.setResponseListener(new ResponseListener<CalendarData>(){
            @Override
            public void onSuccess(CalendarData result) {
                greg_calendar.setText(DateUtils.DateConvert(result.getData().getDate())+"  "+result.getData().getWeekday());
                lunar_calendar.setText(result.getData().getLunarYear()+result.getData().getLunar());
                suit.setText(result.getData().getSuit().replace(".","、"));
                avoid.setText(result.getData().getAvoid().replace(".","、"));
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(DateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        ThreadPoolUtil.getInstance().request(task);

    }

    private void initView() {
        setContentView(R.layout.activity_date);
        greg_calendar=findViewById(R.id.greg_calendar);
        lunar_calendar=findViewById(R.id.lunar_calendar);
        suit=findViewById(R.id.suit);
        avoid=findViewById(R.id.avoid);
    }
}
