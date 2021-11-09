package com.gxx.testdbapplication

import android.R.id
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gxx.testdbapplication.db.OrderDBHelper.Companion.ID
import com.gxx.testdbapplication.db.OrderDBHelper.Companion.TABLENAME
import com.gxx.testdbapplication.db.OrderDao


class MainActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val TAG = "MainActivity";
    }

    lateinit var ordersDao: OrderDao;
    lateinit var showSQLMsg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ordersDao = OrderDao(this);
        ordersDao.orderDBHelper.writableDatabase

        showSQLMsg = findViewById<View>(R.id.showSQLMsg) as TextView
        findViewById<View>(R.id.insert_10_w_data).setOnClickListener(this)
        findViewById<View>(R.id.insert_100_w_data).setOnClickListener(this)
        findViewById<View>(R.id.insert_500_w_data).setOnClickListener(this)
        findViewById<View>(R.id.insert_800_w_data).setOnClickListener(this)
        findViewById<View>(R.id.insert_one_delete_zhangsan).setOnClickListener(this)
        findViewById<View>(R.id.insert_one_like_zhangsan).setOnClickListener(this)
        findViewById<View>(R.id.insert_one_data).setOnClickListener(this)
        findViewById<View>(R.id.insert_find_last_data).setOnClickListener(this)
        findViewById<View>(R.id.insert_find_update_last_data).setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.getId()) {
            R.id.insert_one_like_zhangsan->{
                Thread(object : Runnable{
                    override fun run() {
                        val startTime = System.currentTimeMillis()
                        val db: SQLiteDatabase = ordersDao.orderDBHelper.getReadableDatabase()
                        db.execSQL("select * from " + TABLENAME + " where customName like ? ", arrayOf("%张三%"));

                        val disTime = System.currentTimeMillis() - startTime
                        runOnUiThread { showSQLMsg.setText("毫秒 = " + disTime) }
                        db.close();
                    }
                }).start();
            }

            R.id.insert_one_delete_zhangsan->{
                Thread(object : Runnable{
                    override fun run() {
                        val startTime = System.currentTimeMillis()
                        val db: SQLiteDatabase = ordersDao.orderDBHelper.getWritableDatabase()
                        db.execSQL("delete from " + TABLENAME + " where customName=?", arrayOf("张三"));
                        val disTime = System.currentTimeMillis() - startTime
                        runOnUiThread { showSQLMsg.setText("毫秒 = " + disTime) }
                        db.close();
                    }
                }).start();
            }

            R.id.insert_find_last_data -> {
                Thread(object : Runnable {
                    override fun run() {
                        val startTime = System.currentTimeMillis()
                        val db: SQLiteDatabase = ordersDao.orderDBHelper.getReadableDatabase();
                        val cursor = db.rawQuery("select * from " + TABLENAME + " where customName = ? ", arrayOf("张三"))
                        val list = mutableListOf<String>()
                        while (cursor.moveToNext()) {
                            val _id = cursor.getInt(cursor.getColumnIndex(ID))
                            list.add(_id.toString());
                        }
                        val disTime = System.currentTimeMillis() - startTime
                        runOnUiThread { showSQLMsg.setText("查询出来的ID = " + list.toString() + " \n " + "毫秒 = " + disTime) }
                        cursor.close();
                        db.close()
                    }
                }).start()
            }

            R.id.insert_find_update_last_data -> {
                Thread(object : Runnable {
                    override fun run() {
                        val startTime = System.currentTimeMillis()
                        val db: SQLiteDatabase = ordersDao.orderDBHelper.getReadableDatabase();
                        db.execSQL("update " + TABLENAME + " set customName= '李四'  where customName=?", arrayOf("张三"))
                        val disTime = System.currentTimeMillis() - startTime
                        runOnUiThread { showSQLMsg.setText("毫秒 =" + disTime) }
                        db.close()
                    }
                }).start()
            }


            R.id.insert_one_data -> {
                val startTime = System.currentTimeMillis()
                val db: SQLiteDatabase = ordersDao.orderDBHelper.getWritableDatabase()
                db.beginTransaction()
                db.execSQL("insert into " + TABLENAME + " (customName, orderPrice, country) values ('张三', 'test', 'China')")
                val disTime = System.currentTimeMillis() - startTime
                runOnUiThread { showSQLMsg.setText("毫秒 =" + disTime) }
                db.setTransactionSuccessful()
                db.endTransaction()
                db.close()
            }

            R.id.insert_500_w_data->{
                dowork(500)
            }

            R.id.insert_10_w_data -> {
                dowork(10)
            }


            R.id.insert_100_w_data -> {
                dowork(100)
            }
        }
    }


    private fun dowork(count: Int) {
        Thread {
            val startTime = System.currentTimeMillis()
            val db: SQLiteDatabase = ordersDao.orderDBHelper.getWritableDatabase()
            Log.i(TAG, "开始执行")
            try {
                db.beginTransaction()
                for (i in 0 until count * 10000) {
                    db.execSQL("insert into " + TABLENAME + " ( customName, orderPrice, country) values ('Arc', 'test', 'China')")
                    Log.i(TAG, "第" + i + "条")
                }
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                Log.e(TAG, "", e)
            } finally {
                if (db != null) {
                    db.endTransaction()
                    db.close()
                }
                Toast.makeText(this@MainActivity, "执行完成", Toast.LENGTH_SHORT).show()
                val disTime = System.currentTimeMillis() - startTime
                runOnUiThread { showSQLMsg.setText("注入" + count + "w条耗时(秒)：" + disTime / 1000 + " \n " + "毫秒 = " + disTime) }
            }
        }.start()
    }
}