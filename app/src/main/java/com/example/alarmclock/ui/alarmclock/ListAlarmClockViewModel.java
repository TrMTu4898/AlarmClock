package com.example.alarmclock.ui.alarmclock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListAlarmClockViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ListAlarmClockViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}