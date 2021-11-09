package com.gxx.testdbapplication.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class OrderDBHelper : SQLiteOpenHelper {
    companion object {
        const val DBNAME = "myTest.db";
        const val TABLENAME = "Orders";

        const val ID = "_id";
        const val MESSAGEID = "messageUId"
        const val CUSTOMNAME = "customName"
        const val ORDERPRICE = "orderPrice"
        const val COUNTRY = "country";
    }

    constructor(context: Context) : super(context, DBNAME, null, 1) {}

    override fun onCreate(db: SQLiteDatabase) {
        val sql = "CREATE TABLE IF NOT EXISTS " + TABLENAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MESSAGEID + " TEXT, " +
                CUSTOMNAME + " TEXT, " +
                ORDERPRICE + " TEXT, " +
                COUNTRY + " TEXT " +
                ");"
        db.execSQL(sql)


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql = "DROP TABLE IF EXISTS " + TABLENAME
        db.execSQL(sql)
        onCreate(db)
    }

}