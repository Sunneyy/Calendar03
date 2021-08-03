package com.example.calendar03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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
        setContentView(R.layout.activity_date);
        initView();
        Intent intent=getIntent();
        String date=intent.getStringExtra("date");
        String url= ConfigUtils.getValueFromFile(DateActivity.this,fileName,"url");
        String appKey=ConfigUtils.getValueFromFile(DateActivity.this,fileName,"appKey");
        paramMap.put("date", date);
        paramMap.put("key", appKey);
        ThreadPoolUtil.getInstance().request(new RequestTask<CalendarData>("POST", url, paramMap),new ResponseListener<CalendarData>(){
            @Override
            public void onSuccess(BaseResult<CalendarData> result) {
                greg_calendar.setText(DateUtils.DateConvert(result.getResult().getData().getDate())+"  "+result.getResult().getData().getWeekday());
                lunar_calendar.setText(result.getResult().getData().getLunarYear()+result.getResult().getData().getLunar());
                suit.setText(result.getResult().getData().getSuit().replace(".","、"));
                avoid.setText(result.getResult().getData().getAvoid().replace(".","、"));
            }
            @Override
            public void onFailure(Exception e) {

            }
        });

    }

    private void initView() {
        greg_calendar=findViewById(R.id.greg_calendar);
        lunar_calendar=findViewById(R.id.lunar_calendar);
        suit=findViewById(R.id.suit);
        avoid=findViewById(R.id.avoid);
    }
}
