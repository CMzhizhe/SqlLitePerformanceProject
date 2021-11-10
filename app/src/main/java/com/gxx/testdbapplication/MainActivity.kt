package com.gxx.testdbapplication

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gxx.testdbapplication.db.OrderDBHelper.Companion.ID
import com.gxx.testdbapplication.db.OrderDBHelper.Companion.MESSAGEID
import com.gxx.testdbapplication.db.OrderDBHelper.Companion.TABLENAME
import com.gxx.testdbapplication.db.OrderDao
import java.util.*


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
        findViewById<View>(R.id.insert_100_w_data).setOnClickListener(this)
        findViewById<View>(R.id.insert_200_w_data).setOnClickListener(this)
        findViewById<View>(R.id.insert_300_w_data).setOnClickListener(this)
        findViewById<View>(R.id.insert_one_delete_zhangsan).setOnClickListener(this)
        findViewById<View>(R.id.insert_one_like_zhangsan).setOnClickListener(this)
        findViewById<View>(R.id.insert_one_data).setOnClickListener(this)
        findViewById<View>(R.id.insert_find_last_data).setOnClickListener(this)
        findViewById<View>(R.id.insert_find_update_last_data).setOnClickListener(this)
        findViewById<View>(R.id.insert_one_like_zhujain).setOnClickListener(this);
        findViewById<View>(R.id.insert_one_suoyin).setOnClickListener(this)
        findViewById<View>(R.id.insert_100_w_order_by_data).setOnClickListener(this)
        findViewById<View>(R.id.insert_200_w_order_by_data).setOnClickListener(this)
        findViewById<View>(R.id.insert_300_w_order_by_data).setOnClickListener(this)
        findViewById<View>(R.id.insert_one_create_suoyin).setOnClickListener(this)
    }



    override fun onClick(view: View) {
        when (view.getId()) {
            R.id.insert_one_create_suoyin->{
                //创建索引
                val db: SQLiteDatabase = ordersDao.orderDBHelper.getReadableDatabase()
                val sql2 = "CREATE UNIQUE INDEX index_name on " + TABLENAME + "(" + MESSAGEID + ")";
                db.execSQL(sql2)
                db.close()

            }

            R.id.insert_100_w_order_by_data -> {
                findDataOrderByRandumNumber()
            }

            R.id.insert_200_w_order_by_data -> {
                findDataOrderByRandumNumber()
            }

            R.id.insert_300_w_order_by_data -> {
                findDataOrderByRandumNumber()
            }


            R.id.insert_one_suoyin -> {
                Thread(object : Runnable {
                    override fun run() {
                        val startTime = System.currentTimeMillis()
                        val db: SQLiteDatabase = ordersDao.orderDBHelper.getReadableDatabase();
                        val cursor = db.rawQuery("select * from " + TABLENAME, null)
                        cursor.moveToLast()
                        val messageUID = cursor.getString(cursor.getColumnIndex(MESSAGEID))
                        cursor.moveToFirst();
                        db.rawQuery(
                            "select * from " + TABLENAME + " where messageUId = ? ",
                            arrayOf(messageUID)
                        )
                        val disTime = System.currentTimeMillis() - startTime
                        runOnUiThread { showSQLMsg.setText("查询出来的messageUID = " + messageUID + " \n " + "毫秒 = " + disTime) }
                        cursor.close();
                        db.close()
                    }
                }).start()
            }


            R.id.insert_one_like_zhangsan -> {
                Thread(object : Runnable {
                    override fun run() {
                        val startTime = System.currentTimeMillis()
                        val db: SQLiteDatabase = ordersDao.orderDBHelper.getReadableDatabase()
                        val cursor = db.rawQuery(
                            "select * from " + TABLENAME + " where customName like '%张三%'",
                            null
                        )
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
                }).start();
            }

            R.id.insert_one_delete_zhangsan -> {
                Thread(object : Runnable {
                    override fun run() {
                        val startTime = System.currentTimeMillis()
                        val db: SQLiteDatabase = ordersDao.orderDBHelper.getWritableDatabase()
                        db.execSQL(
                            "delete from " + TABLENAME + " where customName=?",
                            arrayOf("张三")
                        );
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
                        val cursor = db.rawQuery(
                            "select * from " + TABLENAME + " where customName = ? ",
                            arrayOf("张三")
                        )
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
                        db.execSQL(
                            "update " + TABLENAME + " set customName= '李四'  where customName=?",
                            arrayOf("张三")
                        )
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
                db.execSQL("insert into " + TABLENAME + " (customName, orderPrice, country,randumNumber) values ('张三', 'test', 'China','" + getRandom() + "')")
                val disTime = System.currentTimeMillis() - startTime
                runOnUiThread { showSQLMsg.setText("毫秒 =" + disTime) }
                db.setTransactionSuccessful()
                db.endTransaction()
                db.close()
            }

            R.id.insert_one_like_zhujain -> {
                Thread(object : Runnable {
                    override fun run() {
                        val startTime = System.currentTimeMillis()
                        val db: SQLiteDatabase = ordersDao.orderDBHelper.getReadableDatabase()
                        val cursor = db.rawQuery(
                            "select * from " + TABLENAME + " where  _id =? ",
                            arrayOf("2000018")
                        )
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
                }).start();
            }

            R.id.insert_100_w_data -> {
                dowork(100)
            }

            R.id.insert_200_w_data -> {
                dowork(200)
            }

            R.id.insert_300_w_data -> {
                dowork(300)
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
                    db.execSQL(
                        "insert into " + TABLENAME + " ( customName,messageUId, orderPrice, country,randumNumber) values ('Arc', '" + UUID.randomUUID()
                            .toString() + "', 'test', 'China','" + getRandom() + "')"
                    )
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


    private val random = Random(100000);

    /**
     * @date 创建时间:2021/11/10 0010
     * @auther gaoxiaoxiong
     * @Descriptiion 获取随机数
     **/
    private fun getRandom(): Int {
        return random.nextInt();
    }

    private fun findDataOrderByRandumNumber(){
        Thread(object : Runnable {
            override fun run() {
                val startTime = System.currentTimeMillis()
                val db: SQLiteDatabase = ordersDao.orderDBHelper.getReadableDatabase();
                val cursor = db.rawQuery("select * from " + TABLENAME + " order by randumNumber asc", null)
                val disTime = System.currentTimeMillis() - startTime
                runOnUiThread { showSQLMsg.setText("毫秒 = " + disTime) }
                cursor.close();
                db.close()
            }
        }).start()
    }

}