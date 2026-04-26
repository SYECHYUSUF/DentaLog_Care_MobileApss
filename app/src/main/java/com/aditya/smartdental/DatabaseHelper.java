package com.aditya.smartdental;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SmartDental.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TOOLS = "tools";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_USAGE = "usage_count";
    public static final String COLUMN_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_TOOLS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_USAGE + " INTEGER,"
                + COLUMN_STATUS + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
        
        db.execSQL("INSERT INTO " + TABLE_TOOLS + " (name, type, usage_count, status) VALUES ('Sonde Dental', 'Diagnostic', 5, 'Available')");
        db.execSQL("INSERT INTO " + TABLE_TOOLS + " (name, type, usage_count, status) VALUES ('Eskavator', 'Conservative', 12, 'Need Maintenance')");
        db.execSQL("INSERT INTO " + TABLE_TOOLS + " (name, type, usage_count, status) VALUES ('Tang Ekstraksi', 'Surgery', 2, 'Available')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOOLS);
        onCreate(db);
    }

    public long insertTool(ToolModel tool) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, tool.getName());
        values.put(COLUMN_TYPE, tool.getType());
        values.put(COLUMN_USAGE, tool.getUsageCount());
        values.put(COLUMN_STATUS, tool.getStatus());
        return db.insert(TABLE_TOOLS, null, values);
    }

    public List<ToolModel> getAllTools() {
        return getFilteredTools(null);
    }

    public List<ToolModel> getMaintenanceTools() {
        return getFilteredTools("status = 'Need Maintenance'");
    }

    private List<ToolModel> getFilteredTools(String selection) {
        List<ToolModel> toolList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TOOLS, null, selection, null, null, null, COLUMN_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                ToolModel tool = new ToolModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4));
                toolList.add(tool);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return toolList;
    }

    public int updateTool(ToolModel tool) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, tool.getName());
        values.put(COLUMN_TYPE, tool.getType());
        values.put(COLUMN_USAGE, tool.getUsageCount());

        if (tool.getUsageCount() > 10) {
            values.put(COLUMN_STATUS, "Need Maintenance");
        } else {
            values.put(COLUMN_STATUS, tool.getStatus());
        }

        return db.update(TABLE_TOOLS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(tool.getId())});
    }

    public void deleteTool(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TOOLS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void resetMaintenance(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USAGE, 0);
        values.put(COLUMN_STATUS, "Available");
        db.update(TABLE_TOOLS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}