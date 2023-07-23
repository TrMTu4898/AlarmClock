package com.example.alarmclock.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.example.alarmclock.data.AlarmClockModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "alarm_database";
    private static final int DATABASE_VERSION = 10;

    private static final String TABLE_NAME = "alarms";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_HOUR = "hour";
    private static final String COLUMN_MINUTE = "minute";
    private static final String COLUMN_IS_ACTIVE = "is_active";
    private static final String COLUMN_IS_VIBRATE = "is_vibrate";
    private static final String COLUMN_T2 = "t2";
    private static final String COLUMN_T3 = "t3";
    private static final String COLUMN_T4 = "t4";
    private static final String COLUMN_T5 = "t5";
    private static final String COLUMN_T6 = "t6";
    private static final String COLUMN_T7 = "t7";
    private static final String COLUMN_CN = "cn";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HOUR + " INTEGER, " +
                COLUMN_MINUTE + " INTEGER, " +
                COLUMN_IS_ACTIVE + " INTEGER, " +
                COLUMN_IS_VIBRATE + " INTEGER, " +
                COLUMN_T2 + " INTEGER, " +
                COLUMN_T3 + " INTEGER, " +
                COLUMN_T4 + " INTEGER, " +
                COLUMN_T5 + " INTEGER, " +
                COLUMN_T6 + " INTEGER, " +
                COLUMN_T7 + " INTEGER, " +
                COLUMN_CN + " INTEGER)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ nếu tồn tại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Tạo lại bảng
        onCreate(db);
    }

    // Phương thức thêm mới báo thức vào cơ sở dữ liệu
    public int addAlarm(AlarmClockModel alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOUR, alarm.getHour());
        values.put(COLUMN_MINUTE, alarm.getMinute());
        values.put(COLUMN_IS_ACTIVE, alarm.getIsActive());
        values.put(COLUMN_IS_VIBRATE, alarm.getIsVibrate());
        values.put(COLUMN_T2, alarm.getT2());
        values.put(COLUMN_T3, alarm.getT3());
        values.put(COLUMN_T4, alarm.getT4());
        values.put(COLUMN_T5, alarm.getT5());
        values.put(COLUMN_T6, alarm.getT6());
        values.put(COLUMN_T7, alarm.getT7());
        values.put(COLUMN_CN, alarm.getCn());

        int id = (int) db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    // Phương thức lấy danh sách tất cả các báo thức từ cơ sở dữ liệu
    public List<AlarmClockModel> getAllAlarms() {
        List<AlarmClockModel> alarmList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                int hour = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HOUR));
                int minute = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MINUTE));
                int isActive = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ACTIVE));
                int isVibrate = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_VIBRATE));
                int t2 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_T2));
                int t3 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_T3));
                int t4 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_T4));
                int t5 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_T5));
                int t6 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_T6));
                int t7 = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_T7));
                int cn = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CN));

                AlarmClockModel alarm = new AlarmClockModel(hour,id,minute,isActive,isVibrate,t2,t3,t4,t5,t6,t7,cn);
                alarmList.add(alarm);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return alarmList;
    }

    // Phương thức cập nhật thông tin báo thức trong cơ sở dữ liệu
    public int updateAlarm(int id, AlarmClockModel alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOUR, alarm.getHour());
        values.put(COLUMN_MINUTE, alarm.getMinute());
        values.put(COLUMN_IS_ACTIVE, alarm.getIsActive());
        values.put(COLUMN_IS_VIBRATE, alarm.getIsVibrate());
        values.put(COLUMN_T2, alarm.getT2());
        values.put(COLUMN_T3, alarm.getT3());
        values.put(COLUMN_T4, alarm.getT4());
        values.put(COLUMN_T5, alarm.getT5());
        values.put(COLUMN_T6, alarm.getT6());
        values.put(COLUMN_T7, alarm.getT7());
        values.put(COLUMN_CN, alarm.getCn());

        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    // Phương thức xóa báo thức khỏi cơ sở dữ liệu
    public boolean deleteAlarm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public int getLastInsertID() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid() AS last_id", null);
        int lastInsertId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            lastInsertId = (int) cursor.getLong(cursor.getColumnIndexOrThrow("last_id"));
            cursor.close();
        }
        db.close();
        return lastInsertId;
    }
}
