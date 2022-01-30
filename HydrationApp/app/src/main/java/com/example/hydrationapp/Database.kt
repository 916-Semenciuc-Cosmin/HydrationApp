package com.example.hydrationapp

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.database.Cursor
import android.widget.Toast

class Database(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Hydration"
        private const val DATABASE_VERSION = 1

        private const val RECORDS = "Records"
        private const val ID = "Id"
        private const val DATE = "Date"
        private const val INTAKE = "Intake"

        private const val SETTINGS = "Settings"
        private const val S_ID = "Id"
        private const val UNITS = "Units"
        private const val GOAL = "Goal"
        private const val C1 = "C1Size"
        private const val C2 = "C2Size"
        private const val C3 = "C3Size"

        private var count = 0
    }

    private val context: Context? = null

    override fun onCreate(database: SQLiteDatabase) {

        val create_records =
            "CREATE TABLE " + RECORDS + "(" + ID + " INTEGER PRIMARY KEY, " +
                    DATE + " VARCHAR(20), " + INTAKE + " INTEGER);"

        val create_settings =
            "CREATE TABLE " + SETTINGS + "(" + S_ID + " INTEGER PRIMARY KEY, " + UNITS +
                    " VARCHAR(10), " + GOAL + " INTEGER, " + C1 + " INTEGER, " + C2 + " INTEGER, " + C3 + " INTEGER);"

        database.execSQL(create_records)
        database.execSQL(create_settings)
    }

    override fun onUpgrade(database: SQLiteDatabase, i: Int, i1: Int) {
        database.execSQL("DROP TABLE IF EXISTS $RECORDS")
        database.execSQL("DROP TABLE IF EXISTS $SETTINGS")
        onCreate(database)
    }

    fun set_default_settings() {
        val cursor = get_settings()
        if (cursor != null && cursor.count == 0) {
            val database = writableDatabase
            val content_values = ContentValues()

            content_values.put(S_ID, 0)
            content_values.put(UNITS, "ml")
            content_values.put(GOAL, 2000)
            content_values.put(C1, 200)
            content_values.put(C2, 400)
            content_values.put(C3, 500)

            database.insert(SETTINGS, null, content_values)
        }
    }

    fun set_count()
    {
        val database = readableDatabase
        val query = "SELECT COUNT($ID) FROM $RECORDS"
        val cursor = database.rawQuery(query, null)
        cursor.moveToNext()
        count = cursor.getString(0).toInt()
    }

    fun get_settings(): Cursor {
        val id = 0
        val select = "SELECT * FROM $SETTINGS WHERE $S_ID = $id "
        val database = this.readableDatabase
        lateinit var cursor: Cursor
        if (database != null) {
            cursor = database.rawQuery(select, null)
        }

        return cursor
    }

    fun set_units(units: String) {
        val database = this.writableDatabase
        val query = "UPDATE " + SETTINGS + " SET " + UNITS + " = " + "'" + units + "'" + " WHERE " +
                S_ID + " = " + "0"
        database.execSQL(query)
    }

    fun set_goal(goal: Int) {
        val database = this.writableDatabase
        val query = "UPDATE " + SETTINGS + " SET " + GOAL + " = " + goal + " WHERE " +
                S_ID + " = " + "0"
        database.execSQL(query)
    }

    fun set_c1_size(size: Int) {
        val database = this.writableDatabase
        val query = "UPDATE " + SETTINGS + " SET " + C1 + " = " + size + " WHERE " +
                S_ID + " = " + "0"
        database.execSQL(query)
    }

    fun set_c2_size(size: Int) {
        val database = this.writableDatabase
        val query = "UPDATE " + SETTINGS + " SET " + C2 + " = " + size + " WHERE " +
                S_ID + " = " + "0"
        database.execSQL(query)
    }

    fun set_c3_size(size: Int) {
        val database = this.writableDatabase
        val query = "UPDATE " + SETTINGS + " SET " + C3 + " = " + size + " WHERE " +
                S_ID + " = " + "0"
        database.execSQL(query)
    }

    fun add_record(context: Context, date: String, intake: Int) {
        set_count()
        val database = this.writableDatabase
        val content_values = ContentValues()

        if (count == 30) {
            var query = "DELETE FROM $RECORDS WHERE $ID = 1"
            database.execSQL(query)
            query = "UPDATE $RECORDS SET $ID = $ID - 1 WHERE $ID > 0"
            database.execSQL(query)
        }

        set_count()
        content_values.put(ID, count+1)
        content_values.put(DATE, date)
        content_values.put(INTAKE, intake)

        database.insert(RECORDS, null, content_values)
    }

    fun get_first_last(): Cursor {
        set_count()
        val select = "SELECT * FROM $RECORDS WHERE $ID = 1 or $ID = $count"
        val database = this.readableDatabase
        lateinit var cursor: Cursor
        if (database != null) {
            cursor = database.rawQuery(select, null)
        }

        return cursor
    }

    fun update_record(context: Context, date: String, intake: Int) {
        val database = this.writableDatabase
        val query = "UPDATE " + RECORDS + " SET " + INTAKE + " = " + intake.toString() + " WHERE " +
                DATE + " = " + "'" + date + "'"
        database.execSQL(query)
    }

    fun read_record(date: String): Cursor {
        val str_date = "'$date'"
        val select = "SELECT * FROM $RECORDS WHERE $DATE = $str_date"
        val database = this.readableDatabase
        lateinit var cursor: Cursor
        if (database != null) {
            cursor = database.rawQuery(select, null)
        }

        return cursor
    }

    fun read_records(): Cursor {
        val select = "SELECT * FROM $RECORDS"
        val database = this.readableDatabase
        lateinit var cursor: Cursor
        if (database != null) {
            cursor = database.rawQuery(select, null)
        }

        return cursor
    }

}