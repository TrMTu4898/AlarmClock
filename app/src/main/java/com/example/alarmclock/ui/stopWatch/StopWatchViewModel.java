package com.example.alarmclock.ui.stopWatch;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StopWatchViewModel extends ViewModel {

    private Handler handler = new Handler();
    private long startTimeInMillis;
    private MutableLiveData<Long> elapsedTimeInMillis = new MutableLiveData<>();
    private boolean isRunning = false;

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                long currentTimeInMillis = System.currentTimeMillis();
                long elapsedTime = currentTimeInMillis - startTimeInMillis;
                elapsedTimeInMillis.setValue(elapsedTime);
                handler.postDelayed(this, 1000);
            }
        }
    };

    public LiveData<Long> getElapsedTimeInMillis() {
        return elapsedTimeInMillis;
    }

    public void startTimer() {
        if (!isRunning) {
            isRunning = true;
            startTimeInMillis = System.currentTimeMillis();
            handler.post(timerRunnable);
        }
    }

    public void stopTimer() {
        isRunning = false;
        handler.removeCallbacks(timerRunnable);
    }

    public void resetTimer() {
        isRunning = false;
        elapsedTimeInMillis.setValue(0L);
        handler.removeCallbacks(timerRunnable);
    }

}

