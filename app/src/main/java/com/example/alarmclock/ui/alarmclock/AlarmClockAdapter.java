package com.example.alarmclock.ui.alarmclock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarmclock.R;
import com.example.alarmclock.data.AlarmClockModel;

import java.util.List;

public class AlarmClockAdapter extends RecyclerView.Adapter<AlarmClockAdapter.AlarmViewHolder> {

    private List<AlarmClockModel> alarmList;
    private OnItemClickListener clickListener;

    public AlarmClockAdapter(List<AlarmClockModel> alarmList) {
        this.alarmList = alarmList;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_alarm_clock, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        AlarmClockModel alarm = alarmList.get(position);

        // Set alarm time
        holder.alarmTimeTextView.setText(String.format("%02d:%02d", alarm.getHour(), alarm.getMinute()));

        // Set date
        // (For demonstration purposes, we're using a fixed date here. You can replace it with your logic.)
        holder.dateTextView.setText(alarm.getDayOfWeekString());

        // Set the switch state (On/Off)
        holder.activateSwitch.setChecked(alarm.getIsActive() == 1);

        // Set expand/collapse listener
        holder.expandButtonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isExpanded = holder.detailsLayout.getVisibility() == View.VISIBLE;
                holder.detailsLayout.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
                // You can add animation here for a smoother transition.
            }
        });

        // Set days of the week
        holder.t2Button.setSelected(alarm.getT2() == 1);
        holder.t3Button.setSelected(alarm.getT3() == 1);
        holder.t4Button.setSelected(alarm.getT4() == 1);
        holder.t5Button.setSelected(alarm.getT5() == 1);
        holder.t6Button.setSelected(alarm.getT6() == 1);
        holder.t7Button.setSelected(alarm.getT7() == 1);
        holder.cnButton.setSelected(alarm.getCn() == 1);

        // Set button click listeners
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onDeleteClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        private TextView alarmTimeTextView;
        private ImageButton expandButton;
        private RelativeLayout expandButtonContainer;
        private TextView dateTextView;
        private Switch activateSwitch;
        private Button t2Button, t3Button, t4Button, t5Button, t6Button, t7Button, cnButton;
        private LinearLayout detailsLayout;
        private ImageButton deleteButton;

        AlarmViewHolder(View itemView) {
            super(itemView);
            alarmTimeTextView = itemView.findViewById(R.id.alarmTimeTextView);
            expandButton = itemView.findViewById(R.id.deleteButton);
            expandButtonContainer = itemView.findViewById(R.id.expandButtonContainer);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            dateTextView.setTag("date_" + getAdapterPosition());
            activateSwitch = itemView.findViewById(R.id.activateSwitch);
            t2Button = itemView.findViewById(R.id.t2Button);
            t3Button = itemView.findViewById(R.id.t3Button);
            t4Button = itemView.findViewById(R.id.t4Button);
            t5Button = itemView.findViewById(R.id.t5Button);
            t6Button = itemView.findViewById(R.id.t6Button);
            t7Button = itemView.findViewById(R.id.t7Button);
            cnButton = itemView.findViewById(R.id.cnButton);
            detailsLayout = itemView.findViewById(R.id.detailsLayout);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
