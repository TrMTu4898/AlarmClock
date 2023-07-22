package com.example.alarmclock.ui.stopWatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.alarmclock.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StopWatchFragment extends Fragment {

    private StopWatchViewModel viewModel;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stop_clock, container, false);
        textView = view.findViewById(R.id.textView);

        // Lấy tham chiếu đến các FloatingActionButton bằng id
        FloatingActionButton playButton = view.findViewById(R.id.playButton);
        FloatingActionButton stopButton = view.findViewById(R.id.stopButton);
        FloatingActionButton resetButton = view.findViewById(R.id.resetButton);

        // Gán các OnClickListener cho từng FloatingActionButton
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlay(view);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStop(view);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReset(view);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo StopWatchViewModel
        viewModel = new ViewModelProvider(this).get(StopWatchViewModel.class);

        // Liên kết LiveData trong StopWatchViewModel với TextView để hiển thị thời gian
        viewModel.getElapsedTimeInMillis().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long elapsedTimeInMillis) {
                updateTimerText(elapsedTimeInMillis);
            }
        });
    }

    private void updateTimerText(long elapsedTimeInMillis) {
        int seconds = (int) (elapsedTimeInMillis / 1000);
        int minutes = seconds / 60;
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;
        int remainingSeconds = seconds % 60;

        String time = String.format("%d:%02d:%02d", hours, remainingMinutes, remainingSeconds);
        textView.setText(time);
    }

    public void onPlay(View view) {
        viewModel.startTimer();
    }

    public void onStop(View view) {
        viewModel.stopTimer();
    }

    public void onReset(View view) {
        viewModel.resetTimer();
    }
}
