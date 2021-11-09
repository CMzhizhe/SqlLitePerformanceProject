package com.gxx.testdbapplication.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.gxx.testdbapplication.db.OrderDBHelper.Companion.DBNAME
import com.gxx.testdbapplication.db.OrderDBHelper.Companion.TABLENAME

class OrderDao constructor(context: Context) {
    lateinit var orderDBHelper: OrderDBHelper;
    init {
        orderDBHelper = OrderDBHelper(context);
    }



}