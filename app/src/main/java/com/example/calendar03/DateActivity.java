package com.example.calendar03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.calendar03.gson.BaseResult;
import com.example.calendar03.gson.CalendarData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateActivity extends AppCompatActivity {
    private TextView greg_calendar;
    private TextView lunar_calendar;
    private TextView suit;
    private TextView avoid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        initView();
        Intent intent=getIntent();
        String dateStr=intent.getStringExtra("date");
        //tv_date.setText(dateStr);
        Gson gson=new Gson();
        //String json="{\"reason\":\"Success\",\"result\":{\"data\":{\"holiday\":\"元旦\",\"avoid\":\"破土.安葬.行丧.开生坟.\",\"animalsYear\":\"马\",\"desc\":\"1月1日至3日放假调休，共3天。1月4日（星期日）上班。\",\"weekday\":\"星期四\",\"suit\":\"订盟.纳采.造车器.祭祀.祈福.出行.安香.修造.动土.上梁.开市.交易.立券.移徙.入宅.会亲友.安机械.栽种.纳畜.造屋.起基.安床.造畜椆栖.\",\"lunarYear\":\"甲午年\",\"lunar\":\"十一月十一\",\"year-month\":\"2015-1\",\"date\":\"2015-1-1\"}},\"error_code\":0}";
        Type responseType=new TypeToken<BaseResult<CalendarData>>(){}.getType();
        BaseResult<CalendarData> data=gson.fromJson(dateStr,responseType);
        //公历日期
        greg_calendar.setText(DateConvert(data.getResult().getData().getDate())+"  "+data.getResult().getData().getWeekday());
        //农历日期
        lunar_calendar.setText(data.getResult().getData().getLunarYear()+data.getResult().getData().getLunar());
        //宜
        suit.setText(data.getResult().getData().getSuit().replace(".","、"));
        avoid.setText(data.getResult().getData().getAvoid().replace(".","、"));

    }

    private void initView() {
        greg_calendar=findViewById(R.id.greg_calendar);
        lunar_calendar=findViewById(R.id.lunar_calendar);
        suit=findViewById(R.id.suit);
        avoid=findViewById(R.id.avoid);
    }

    private String DateConvert(String str) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy年MM月dd日").format(date);
    }
}
