package com.example.alarmclock.ui.alarmclock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarmclock.R;
import com.example.alarmclock.data.AlarmClockModel;
import com.example.alarmclock.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AlarmClockFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<AlarmClockModel> alarmList;
    private AlarmClockAdapter alarmAdapter;
    private ToggleButton t2ButtonAdd, t3ButtonAdd, t4ButtonAdd, t5ButtonAdd, t6ButtonAdd, t7ButtonAdd, cnButtonAdd;
    private DatabaseHelper databaseHelper;
    public AlarmClockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_alarm, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmList = new ArrayList<>();
        alarmAdapter = new AlarmClockAdapter(alarmList);
        recyclerView.setAdapter(alarmAdapter);
        loadAlarmData(); // Load data from the database or other sources if necessary
        databaseHelper = new DatabaseHelper(getContext());
        // Set click listener for buttonAddAlarm
        View buttonAddAlarm = view.findViewById(R.id.buttonAddAlarm);
        buttonAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the custom dialog containing the "add_alarm_clock" layout
                showAddAlarmDialog();
            }
        });

        return view;
    }

    private void loadAlarmData() {
        // Replace this with your logic to load alarm data from the database or other sources
        // For demonstration purposes, I'm adding dummy data here
        alarmList.clear();
        databaseHelper = new DatabaseHelper(getContext());
        alarmList.addAll(databaseHelper.getAllAlarms());
        alarmAdapter.notifyDataSetChanged();
        for (AlarmClockModel alarm : alarmList) {
            TextView dateTextView = recyclerView.findViewWithTag("date_" + alarm.getId());
            if (dateTextView != null) {
                dateTextView.setText(alarm.getDayOfWeekString());
            }
        }

    }

    private void showAddAlarmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_alarm_clock, null);
        builder.setView(dialogView);
        Dialog dialog = builder.create();

        // Find views inside the custom dialog layout
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);
        TextView okButton = dialogView.findViewById(R.id.okButton);
        t2ButtonAdd = dialogView.findViewById(R.id.t2ButtonAdd);
        t3ButtonAdd = dialogView.findViewById(R.id.t3ButtonAdd);
        t4ButtonAdd = dialogView.findViewById(R.id.t4ButtonAdd);
        t5ButtonAdd = dialogView.findViewById(R.id.t5ButtonAdd);
        t6ButtonAdd = dialogView.findViewById(R.id.t6ButtonAdd);
        t7ButtonAdd = dialogView.findViewById(R.id.t7ButtonAdd);
        cnButtonAdd = dialogView.findViewById(R.id.cnButtonAdd);
        // Add click listeners to the buttons
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Dismiss the dialog when Cancel is clicked
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process the selected time from the TimePicker here
                TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                int t2Value = t2ButtonAdd.isChecked() ? 1 : 0;
                int t3Value = t3ButtonAdd.isChecked() ? 1 : 0;
                int t4Value = t4ButtonAdd.isChecked() ? 1 : 0;
                int t5Value = t5ButtonAdd.isChecked() ? 1 : 0;
                int t6Value = t6ButtonAdd.isChecked() ? 1 : 0;
                int t7Value = t7ButtonAdd.isChecked() ? 1 : 0;
                int cnValue = cnButtonAdd.isChecked() ? 1 : 0;
                int isActive = 1;
                int isVibrate = 1;
                AlarmClockModel alarm = new AlarmClockModel(hour, minute, isActive, isVibrate, t2Value, t3Value, t4Value, t5Value, t6Value, t7Value, cnValue);
                long insertedId = databaseHelper.addAlarm(alarm);
                if (insertedId != -1) {
                    Toast.makeText(getContext(), "Đã lưu báo thức thành công!", Toast.LENGTH_SHORT).show();
                    alarm.setId(databaseHelper.getLastInsertID());
                    loadAlarmData();
                } else {
                    Toast.makeText(getContext(), "Lỗi khi lưu báo thức. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
                alarmAdapter.notifyDataSetChanged();
                dialog.dismiss(); // Dismiss the dialog when OK is clicked
            }
        });

        dialog.show();
    }
}
