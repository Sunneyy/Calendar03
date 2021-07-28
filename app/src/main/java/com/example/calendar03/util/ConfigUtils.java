package com.example.calendar03.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ConfigUtils {
/*    public static Map<String,String> configResult(String filePath) throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        BufferedReader in = null;
        in = new BufferedReader(new FileReader(new File(filePath)));
        String s = null;
        while ((s = in.readLine()) != null) {
            String str1 = "";
            String str2 = "";
            if (filePath.contains(".properties")) {
                str1 = s.substring(0, s.indexOf("="));
                str2 = s.substring(str1.length() + 1, s.length());
            }
            if (filePath.contains(".mf")) {
                str1 = s.substring(0, s.indexOf(":"));
                str2 = s.substring(str1.length() + 2, s.length());
            }
            map.put(str1, str2);
        }
        return map;
    }*/

    public static String getValueFromFile(Context context, String filePath, String key) {
        AssetManager assetManager = context.getAssets();
        Map<String, String> map = new HashMap<String, String>();
        try {
            InputStream is = assetManager.open(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                parseFile(filePath, line,map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map.get(key);

    }

    private static void parseFile(String filePath, String line,Map<String, String> map) {
        if (TextUtils.isEmpty(line)) {
            return;
        }
        if (filePath.endsWith(".properties")) {
            String[] split = line.split("=");
            if (split.length != 2) {
                return;
            }
            map.put(split[0], split[1]);
        } else if (filePath.contains(".mf")) {
            String[] split = line.split(":");
            if (split.length != 2) {
                return;
            }
            map.put(split[0], split[1]);
        } else {
            return;
        }
    }

}
