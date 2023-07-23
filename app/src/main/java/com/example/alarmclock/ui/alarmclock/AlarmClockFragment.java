package com.example.alarmclock.ui.alarmclock;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarmclock.R;
import com.example.alarmclock.data.AlarmClockModel;
import com.example.alarmclock.data.DatabaseHelper;
import com.example.alarmclock.receiver.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmClockFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<AlarmClockModel> alarmList;
    private AlarmClockAdapter alarmAdapter;
    private ToggleButton t2ButtonAdd, t3ButtonAdd, t4ButtonAdd, t5ButtonAdd, t6ButtonAdd, t7ButtonAdd, cnButtonAdd;
    private DatabaseHelper databaseHelper;
    private boolean[] selectedDays = new boolean[7];

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
        loadAlarmData();
        databaseHelper = new DatabaseHelper(getContext());

        // Set click listener for buttonAddAlarm
        View buttonAddAlarm = view.findViewById(R.id.buttonAddAlarm);
        buttonAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAlarmDialog();
            }
        });
        alarmAdapter.setOnItemClickListener(new AlarmClockAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                if (position >= 0 && position < alarmList.size()) {
                    AlarmClockModel alarmToDelete = alarmList.get(position);
                    int alarmId = alarmToDelete.getId();
                    System.out.println(alarmId);
                    // Delete the alarm from the database
                    databaseHelper.deleteAlarm(alarmToDelete.getId());

                    // Remove the alarm from the list
                    alarmList.remove(position);

                    // Notify the adapter that an item has been removed
                    alarmAdapter.notifyItemRemoved(position);
                    alarmAdapter.notifyItemRangeChanged(position, alarmList.size()); // Refresh the view after removing the item

                    Toast.makeText(getContext(), "Báo thức đã được xóa!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Set click listener for RecyclerView items


        return view;
    }

    private void loadAlarmData() {
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
                dialog.dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                selectedDays[0] = t2ButtonAdd.isChecked();
                selectedDays[1] = t3ButtonAdd.isChecked();
                selectedDays[2] = t4ButtonAdd.isChecked();
                selectedDays[3] = t5ButtonAdd.isChecked();
                selectedDays[4] = t6ButtonAdd.isChecked();
                selectedDays[5] = t7ButtonAdd.isChecked();
                selectedDays[6] = cnButtonAdd.isChecked();

                int isActive = 1;
                int isVibrate = 1;
                AlarmClockModel alarm = new AlarmClockModel(hour, minute, isActive, isVibrate, t2Value, t3Value, t4Value, t5Value, t6Value, t7Value, cnValue);
                int insertedId = databaseHelper.addAlarm(alarm);
                if (insertedId != -1) {
                    // Gán giá trị insertedId vào trường id của đối tượng alarm
                    alarm.setId(insertedId);
                    Toast.makeText(getContext(), "Đã lưu báo thức thành công!", Toast.LENGTH_SHORT).show();
                    loadAlarmData();

                } else {
                    Toast.makeText(getContext(), "Lỗi khi lưu báo thức. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
                if (t2Value == 1) {
                    setRepeatingAlarm(getContext(), alarm, Calendar.MONDAY);
                }
                if (t3Value == 1) {
                    setRepeatingAlarm(getContext(), alarm, Calendar.TUESDAY);
                }
                if (t4Value == 1) {
                    setRepeatingAlarm(getContext(), alarm, Calendar.WEDNESDAY);
                }
                if (t5Value == 1) {
                    setRepeatingAlarm(getContext(), alarm, Calendar.THURSDAY);
                }
                if (t6Value == 1) {
                    setRepeatingAlarm(getContext(), alarm, Calendar.FRIDAY);
                }
                if (t7Value == 1) {
                    setRepeatingAlarm(getContext(), alarm, Calendar.SATURDAY);
                }
                if (cnValue == 1) {
                    setRepeatingAlarm(getContext(), alarm, Calendar.SUNDAY);
                }

                alarmAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


        dialog.show();
    }
    private void setRepeatingAlarm(Context context, AlarmClockModel alarm, int dayOfWeek) {
        // Tạo một Calendar object để lấy ngày và giờ hiện tại
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Tính toán số ngày cần thêm vào currentDayOfWeek để đạt được ngày thứ dayOfWeek trong tuần
        int daysToAdd = (dayOfWeek - currentDayOfWeek + 7) % 7;
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);

        // Lấy giờ và phút từ đối tượng alarm
        int hour = alarm.getHour();
        int minute = alarm.getMinute();

        // Đặt giờ và phút cho calendar
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        // Tạo Intent để gửi đến AlarmReceiver khi báo thức được kích hoạt
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("com.example.alarmclock.ACTION_TRIGGER_ALARM");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);

        // Đặt báo thức lặp lại cho các ngày đã chọn
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void setAlarm() {
        // Get the selected time from TimePicker
        TimePicker timePicker = getView().findViewById(R.id.timePicker);
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        // Create a Calendar instance with the selected time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        // Create an Intent that will be delivered to the AlarmReceiver
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.setAction("com.example.alarmclock.ACTION_TRIGGER_ALARM");
        intent.putExtra("selectedDays", selectedDays);
        // Create a PendingIntent to be triggered when the alarm goes off
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_MUTABLE);

        // Get the AlarmManager service
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        // Set the alarm using AlarmManager
        // Note: In a real-world scenario, you may want to add a unique ID to PendingIntent
        // to distinguish different alarms.
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(getContext(), "Alarm set for " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
    }

}
