package com.example.calendar03.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class GsonTest {
    public static void main(String[] args) {
        Gson gson=new Gson();
        String json="{\"reason\":\"Success\",\"result\":{\"data\":{\"holiday\":\"元旦\",\"avoid\":\"破土.安葬.行丧.开生坟.\",\"animalsYear\":\"马\",\"desc\":\"1月1日至3日放假调休，共3天。1月4日（星期日）上班。\",\"weekday\":\"星期四\",\"suit\":\"订盟.纳采.造车器.祭祀.祈福.出行.安香.修造.动土.上梁.开市.交易.立券.移徙.入宅.会亲友.安机械.栽种.纳畜.造屋.起基.安床.造畜椆栖.\",\"lunarYear\":\"甲午年\",\"lunar\":\"十一月十一\",\"year-month\":\"2015-1\",\"date\":\"2015-1-1\"}},\"error_code\":0}";
        Type responseType=new TypeToken<BaseResult<CalendarData>>(){}.getType();
        BaseResult<CalendarData> data=gson.fromJson(json,responseType);
    }


}
