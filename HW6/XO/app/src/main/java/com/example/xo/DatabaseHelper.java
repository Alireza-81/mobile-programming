package com.example.xo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TicTacToe.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_GAME_STATE = "game_state";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_STATE = "state";

    private static final String TABLE_GAME_RESULTS = "game_results";
    private static final String COLUMN_RESULT = "result";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    private static final String CREATE_TABLE_GAME_STATE = "CREATE TABLE " + TABLE_GAME_STATE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_STATE + " TEXT);";

    private static final String CREATE_TABLE_GAME_RESULTS = "CREATE TABLE " + TABLE_GAME_RESULTS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_RESULT + " TEXT, " +
            COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GAME_STATE);
        db.execSQL(CREATE_TABLE_GAME_RESULTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME_STATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME_RESULTS);
        onCreate(db);
    }

    public void saveGameState(String gameState, boolean playerIsX) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, 1);
        values.put(COLUMN_STATE, gameState + "," + (playerIsX ? "1" : "0"));

        db.replace(TABLE_GAME_STATE, null, values);
        db.close();
    }

    public String loadGameState() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GAME_STATE, new String[]{COLUMN_STATE}, COLUMN_ID + "=?", new String[]{"1"}, null, null, null);
        String state = null;
        boolean playerIsX = true;
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String fullState = cursor.getString(cursor.getColumnIndex(COLUMN_STATE));
            cursor.close();
            db.close();

            int lastIndex = fullState.lastIndexOf(',');
            if (lastIndex != -1) {
                state = fullState.substring(0, lastIndex);
                playerIsX = "1".equals(fullState.substring(lastIndex + 1));
            }
            return state + "," + playerIsX;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    public void saveGameResult(String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESULT, result);

        db.insert(TABLE_GAME_RESULTS, null, values);
        db.close();
    }

    public List<String> loadGameResults() {
        List<String> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GAME_RESULTS, new String[]{COLUMN_RESULT, COLUMN_TIMESTAMP}, null, null, null, null, COLUMN_TIMESTAMP + " DESC");

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String result = cursor.getString(cursor.getColumnIndex(COLUMN_RESULT));
                @SuppressLint("Range") String timestamp = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));
                results.add(result + " at " + timestamp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return results;
    }
}
