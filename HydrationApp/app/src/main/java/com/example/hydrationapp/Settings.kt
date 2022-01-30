package com.example.hydrationapp

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class Settings : AppCompatActivity() {
    private var goal = 0
    private var unit = " ml"
    private var c1_size = 0
    private var c2_size = 0
    private var c3_size = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        get_settings()
        set_variables()
        set_actions()
    }

    override fun onRestart() {
        //here i call the things from onCreate method to update the
        // screen when i come back from changing any settings
        super.onRestart()

        get_settings()
        set_variables()
    }

    fun get_settings()
    {
        //here i get the current settings from the database
        val database = Database(this)
        database.set_default_settings()
        val cursor = database.get_settings()

        if(cursor!=null && cursor.count != 0)
        {
            cursor.moveToNext()
            unit = " " + cursor.getString(1)
            goal = cursor.getString(2).toInt()
            c1_size = cursor.getString(3).toInt()
            c2_size = cursor.getString(4).toInt()
            c3_size = cursor.getString(5).toInt()
        }
    }

    fun set_variables()
    {
        //here i set all the view on the screen that are related to the settings
        val goal_tv = findViewById<TextView>(R.id.goal)
        val str_goal = goal.toString()
        goal_tv.text = str_goal

        val c1_size_tv = findViewById<TextView>(R.id.c1_size)
        val str_c1 = c1_size.toString()
        c1_size_tv.text = str_c1

        val c2_size_tv = findViewById<TextView>(R.id.c2_size)
        val str_c2 = c2_size.toString()
        c2_size_tv.text = str_c2

        val c3_size_tv = findViewById<TextView>(R.id.c3_size)
        val str_c3 = c3_size.toString()
        c3_size_tv.text = str_c3

        var unit_tv = findViewById<TextView>(R.id.unit1)
        unit_tv.text = unit

        unit_tv = findViewById<TextView>(R.id.unit2)
        unit_tv.text = unit

        unit_tv = findViewById<TextView>(R.id.unit3)
        unit_tv.text = unit

        unit_tv = findViewById<TextView>(R.id.unit4)
        unit_tv.text = unit

        unit_tv = findViewById<TextView>(R.id.unit5)
        unit_tv.text = unit
    }

    fun set_actions()
    {
        //here i set the actions for clicking the views
        val units_layout = findViewById<RelativeLayout>(R.id.units_layout)
        val goal_layout = findViewById<RelativeLayout>(R.id.goal_layout)
        val c1_layout = findViewById<RelativeLayout>(R.id.c1_layout)
        val c2_layout = findViewById<RelativeLayout>(R.id.c2_layout)
        val c3_layout = findViewById<RelativeLayout>(R.id.c3_layout)
        val back_tv = findViewById<ImageView>(R.id.back)

        back_tv.setOnClickListener()
        {
            finish()
        }

        units_layout.setOnClickListener()
        {
            val intent = Intent(this, UnitsSettings::class.java)
            startActivity(intent)
        }

        goal_layout.setOnClickListener()
        {
            val intent = Intent(this, SizeSettings::class.java)
            intent.putExtra("type", "goal")
            startActivity(intent)
        }

        c1_layout.setOnClickListener()
        {
            val intent = Intent(this, SizeSettings::class.java)
            intent.putExtra("type", "c1")
            startActivity(intent)
        }

        c2_layout.setOnClickListener()
        {
            val intent = Intent(this, SizeSettings::class.java)
            intent.putExtra("type", "c2")
            startActivity(intent)
        }

        c3_layout.setOnClickListener()
        {
            val intent = Intent(this, SizeSettings::class.java)
            intent.putExtra("type", "c3")
            startActivity(intent)
        }
    }
}