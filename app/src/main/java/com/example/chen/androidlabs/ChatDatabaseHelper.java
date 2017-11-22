package com.example.chen.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
        protected static String DATABASE_NAME = "Messages1.db";
        protected static int VERSION_NUM = 2;
        public final static String Key_ID = "_id";
        public final static String KEY_MESSAGE = "message";
        public static final String name = "MyTable";

        public ChatDatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, VERSION_NUM);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + name + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, message text);");
            Log.i("ChatDatabaseHelper", "Calling onCreate");
        }

        @Override
        public void onOpen(SQLiteDatabase db){
            Log.i("ChatDatabaseHelper", "Calling onOpen");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + name);
            onCreate(db);
            Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
        }
    }



