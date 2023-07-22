package com.example.alarmclock.data;

import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class AlarmClockModel {
    private int hour;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AlarmClockModel(int hour, int minute, int isActive, int isVibrate, int t2, int t3, int t4, int t5, int t6, int t7, int cn) {
        this.hour = hour;
        this.minute = minute;
        this.isActive = isActive;
        this.isVibrate = isVibrate;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
        this.t5 = t5;
        this.t6 = t6;
        this.t7 = t7;
        this.cn = cn;
    }
    private View binding;
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsVibrate() {
        return isVibrate;
    }

    public void setIsVibrate(int isVibrate) {
        this.isVibrate = isVibrate;
    }

    public int getT2() {
        return t2;
    }

    public void setT2(int t2) {
        this.t2 = t2;
    }

    public int getT3() {
        return t3;
    }

    public void setT3(int t3) {
        this.t3 = t3;
    }

    public int getT4() {
        return t4;
    }

    public void setT4(int t4) {
        this.t4 = t4;
    }

    public int getT5() {
        return t5;
    }

    public void setT5(int t5) {
        this.t5 = t5;
    }

    public int getT6() {
        return t6;
    }

    public void setT6(int t6) {
        this.t6 = t6;
    }

    public int getT7() {
        return t7;
    }

    public void setT7(int t7) {
        this.t7 = t7;
    }

    public int getCn() {
        return cn;
    }

    public void setCn(int cn) {
        this.cn = cn;
    }

    private int minute;
    private int isActive;
    private int isVibrate;
    private int t2;
    private int t3;
    private int t4;
    private int t5;
    private int t6;
    private int t7;
    private int cn;

    public String getDayOfWeekString() {
        List<String> dayOfWeeks = new ArrayList<>();
        if (t2 == 1) dayOfWeeks.add("T2");
        if (t3 == 1) dayOfWeeks.add("T3");
        if (t4 == 1) dayOfWeeks.add("T4");
        if (t5 == 1) dayOfWeeks.add("T5");
        if (t6 == 1) dayOfWeeks.add("T6");
        if (t7 == 1) dayOfWeeks.add("T7");
        if (cn == 1) dayOfWeeks.add("CN");

        return TextUtils.join(", ", dayOfWeeks);
    }
    public void setBinding(View binding) {
        this.binding = binding;
    }

    public View getBinding() {
        return binding;
    }
}
