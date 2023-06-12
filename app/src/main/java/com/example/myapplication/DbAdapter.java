package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbAdapter {
    private static final String TAG = "DbAdapter";
    public static final String TABLE = "peopleinfo";
    SQLiteDatabase db;
    Context context;
    DBOpenHelper dbOpenHelper;

    public DbAdapter(Context context) {
        this.context = context;
    }

    public void open(){
        dbOpenHelper = new DBOpenHelper(context, "peopledb", null, 1);
        try {
            db = dbOpenHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbOpenHelper.getReadableDatabase();
        }
    }

    public void close(){
        if(db != null) {
            db.close();
            db = null;
        }
    }

    public void insert(Person p) {
        ContentValues cv = new ContentValues();
        cv.put("name", p.name);
        cv.put("age", p.age);
        cv.put("height", p.height);
        db.insert(TABLE, null , cv);
    }

    public Person[] getAllData() {
        Cursor cursor = db.query(TABLE,
                new String[]{"_id", "name", "age", "height"},
                null, null, null, null, null);
        return convertToPerson(cursor);
    }


    @SuppressLint("Range")
    private Person[] convertToPerson(Cursor cursor) {
        int rowCount = cursor.getCount();
        if(rowCount == 0 || !cursor.moveToFirst()){
            Log.d(TAG, "convertToPerson: rowCount is 0 or can not move to first row");
            return null;
        }
        Person[] ps = new Person[rowCount];
        cursor.moveToFirst();
        for(int i = 0; i < rowCount; i++){
            ps[i] = new Person();
            ps[i].name = cursor.getString(cursor.getColumnIndex("name"));
            ps[i].age = cursor.getInt(cursor.getColumnIndex("age"));
            ps[i].height = cursor.getFloat(cursor.getColumnIndex("height"));
            cursor.moveToNext();
        }
        return ps;
    }

    public Person queryById(int id) {
        Cursor cursor = db.query(TABLE,
                new String[]{"_id", "name", "age", "height"},
                "_id=" + id,
                null,null,null,null);
        Person[] ps = convertToPerson(cursor);
        if(ps != null)
            return ps[0];
        return null;
    }

    private class DBOpenHelper extends SQLiteOpenHelper {
        public DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        private static final String DB_CREATE =
                "create table "+ TABLE + " (" +
                        "_id integer primary key autoincrement," +
                        "name text," +
                        "age integer," +
                        "height float)";

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
