package com.example.alarmclock.receiver;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.alarmclock.MainActivity;
import com.example.alarmclock.R;

public class AlarmReceiver extends BroadcastReceiver {
    private static Ringtone ringtone = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        // Xử lý khi báo thức được kích hoạt, ví dụ: hiển thị thông báo, rung và chơi âm thanh báo thức

        // Tạo PendingIntent để mở ứng dụng khi người dùng nhấn vào thông báo
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        if (ringtone == null) {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(context, alarmSound);
        }
        if (ringtone != null && !ringtone.isPlaying()) {
            ringtone.play();
        }

        Intent stopIntent = new Intent(context, StopAlarmReceiver.class);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE );
        // Tạo thông báo
        String channelId = "alarm_channel";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_alarm) // Icon của thông báo
                .setContentTitle("Báo thức đã được kích hoạt") // Tiêu đề của thông báo
                .setContentText("Nhấn vào để tắt báo thức") // Nội dung của thông báo
                .setContentIntent(pendingIntent) // Intent sẽ thực thi khi thông báo được nhấn vào
                .addAction(R.drawable.ic_stop, "Dừng báo thức", stopPendingIntent)
                .setAutoCancel(true); // Tự động huỷ thông báo khi người dùng nhấn vào
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Kiểm tra phiên bản Android và tạo kênh thông báo nếu cần
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Alarm Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        // Hiển thị thông báo
        int notificationId = 1;
        notificationManager.notify(notificationId, notificationBuilder.build());

    }
    public static void stopRingtone() {
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
            ringtone = null;
        }
    }
}
