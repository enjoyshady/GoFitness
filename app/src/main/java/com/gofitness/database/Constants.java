package com.gofitness.database;

import android.util.ArrayMap;

import com.google.gson.internal.LinkedTreeMap;

import java.util.LinkedHashMap;
import java.util.Map;

public class Constants {

    // MuscleGroup 集合
    public static final String AllExercises = "All Exercises";


    /**
     * 是否第一次进入
     */
    public static final String IS_INITIALIZATION_USER_INFORMATION = "IS_INITIALIZATION_USER_INFORMATION";

    /**
     * 是否初始化内置数据库数据
     */
    public static final String IS_INITIALIZATION_DATABASE = "IS_INITIALIZATION_DATABASE";

    // toolbar title
    public static final String TITLE = "title";

    // 练习时间 : 前面显示，后面具体时间，可根据具体情况修改
    private static final Map<String, Integer> EXERCISES_TIME = new LinkedHashMap<>();

    public static synchronized Map<String, Integer> getExercisesTime() {
        if (EXERCISES_TIME.size() == 0) {
            EXERCISES_TIME.put("10 sec", 10);
            EXERCISES_TIME.put("20 sec", 20);
            EXERCISES_TIME.put("30 sec - 0.5 min", 30);
            EXERCISES_TIME.put("40 sec", 40);
            EXERCISES_TIME.put("50 sec", 50);
            EXERCISES_TIME.put("60 sec - 1 min", 60);
            EXERCISES_TIME.put("75 sec", 75);
            EXERCISES_TIME.put("90 sec - 1.5 min", 90);
            EXERCISES_TIME.put("105 sec", 105);
            EXERCISES_TIME.put("120 sec - 2 min", 120);
            EXERCISES_TIME.put("135 sec", 135);
            EXERCISES_TIME.put("150 sec - 2.5 min", 150);
            EXERCISES_TIME.put("165 sec", 165);
            EXERCISES_TIME.put("180 sec - 3 min", 180);
            EXERCISES_TIME.put("195 sec", 195);
            EXERCISES_TIME.put("210 sec - 3.5 min", 210);
            EXERCISES_TIME.put("225 sec", 225);
            EXERCISES_TIME.put("240 sec - 4 min", 240);
            EXERCISES_TIME.put("255 sec", 255);
            EXERCISES_TIME.put("270 sec - 4.5 min", 270);
            EXERCISES_TIME.put("285 sec", 285);
            EXERCISES_TIME.put("300 sec - 5 min", 300);
            EXERCISES_TIME.put("315 sec", 315);
            EXERCISES_TIME.put("330 sec - 5.5 min", 330);
            EXERCISES_TIME.put("345 sec", 345);
            EXERCISES_TIME.put("360 sec - 6 min", 360);
            EXERCISES_TIME.put("375 sec", 375);
            EXERCISES_TIME.put("390 sec - 6.5 min", 390);
            EXERCISES_TIME.put("405 sec", 405);
            EXERCISES_TIME.put("420 sec - 7 min", 420);
            EXERCISES_TIME.put("435 sec", 435);
            EXERCISES_TIME.put("450 sec - 7.5 min", 450);
            EXERCISES_TIME.put("465 sec", 465);
            EXERCISES_TIME.put("480 sec - 8 min", 480);
            EXERCISES_TIME.put("495 sec", 495);
            EXERCISES_TIME.put("510 sec - 8.5 min", 510);
            EXERCISES_TIME.put("525 sec", 525);
            EXERCISES_TIME.put("540 sec - 9 min", 540);
            EXERCISES_TIME.put("555 sec", 555);
            EXERCISES_TIME.put("570 sec - 9.5 min", 570);
            EXERCISES_TIME.put("585 sec", 585);
            EXERCISES_TIME.put("600 sec - 10 min", 600);
        }
        return EXERCISES_TIME;
    }
}
