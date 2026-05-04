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
    private static final int DATABASE_VERSION = 2; // Increment version for schema change

    public static final String TABLE_TOOLS = "tools";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_USAGE = "usage_count";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_IMAGE = "image_path"; // New column

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
                + COLUMN_STATUS + " TEXT,"
                + COLUMN_IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
        
        // Initial dummy data
        db.execSQL("INSERT INTO " + TABLE_TOOLS + " (name, type, usage_count, status, image_path) VALUES ('Sonde Dental', 'Diagnostic', 5, 'Available', null)");
        db.execSQL("INSERT INTO " + TABLE_TOOLS + " (name, type, usage_count, status, image_path) VALUES ('Eskavator', 'Conservative', 12, 'Need Maintenance', null)");
        db.execSQL("INSERT INTO " + TABLE_TOOLS + " (name, type, usage_count, status, image_path) VALUES ('Tang Ekstraksi', 'Oral Surgery', 2, 'Available', null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_TOOLS + " ADD COLUMN " + COLUMN_IMAGE + " TEXT");
        }
    }

    // CREATE
    public long insertTool(ToolModel tool) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, tool.getName());
        values.put(COLUMN_TYPE, tool.getType());
        values.put(COLUMN_USAGE, tool.getUsageCount());
        values.put(COLUMN_STATUS, tool.getStatus());
        values.put(COLUMN_IMAGE, tool.getImagePath());
        return db.insert(TABLE_TOOLS, null, values);
    }

    // READ
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
                        cursor.getString(4),
                        cursor.getString(5)); // Read image path
                toolList.add(tool);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return toolList;
    }

    // UPDATE
    public int updateTool(ToolModel tool) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, tool.getName());
        values.put(COLUMN_TYPE, tool.getType());
        values.put(COLUMN_USAGE, tool.getUsageCount());
        values.put(COLUMN_IMAGE, tool.getImagePath());

        if (tool.getUsageCount() > 10) {
            values.put(COLUMN_STATUS, "Need Maintenance");
        } else {
            values.put(COLUMN_STATUS, tool.getStatus());
        }

        return db.update(TABLE_TOOLS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(tool.getId())});
    }

    // INCREMENT USAGE
    public void incrementUsage(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_TOOLS, new String[]{COLUMN_USAGE}, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            int currentUsage = cursor.getInt(0);
            int newUsage = currentUsage + 1;
            
            ContentValues values = new ContentValues();
            values.put(COLUMN_USAGE, newUsage);
            
            if (newUsage > 10) {
                values.put(COLUMN_STATUS, "Need Maintenance");
            }
            
            db.update(TABLE_TOOLS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        }
        cursor.close();
    }

    // DELETE
    public boolean deleteTool(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_TOOLS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // RESET MAINTENANCE
    public void resetMaintenance(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USAGE, 0);
        values.put(COLUMN_STATUS, "Available");
        db.update(TABLE_TOOLS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}