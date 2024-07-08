package com.example.b22_4sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyHelper(context: Context):SQLiteOpenHelper(context,"tuhoc",null,1 ) {
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE TUHOC (_id INTEGER PRIMARY KEY autoincrement, user TEXT, email TEXT)")
        p0?.execSQL("INSERT INTO TUHOC (user, email) VALUES ('Admin','admin@gmail.com')")
        p0?.execSQL("INSERT INTO TUHOC (user, email) VALUES ('tien','tien@gmail.com')")
        p0?.execSQL("INSERT INTO TUHOC (user, email) VALUES ('hieu','hieu@gmail.com')")
        p0?.execSQL("INSERT INTO TUHOC (user, email) VALUES ('van','van@gmail.com')")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}