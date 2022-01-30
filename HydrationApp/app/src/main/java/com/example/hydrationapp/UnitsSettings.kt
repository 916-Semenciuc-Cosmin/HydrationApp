package com.example.hydrationapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class UnitsSettings : AppCompatActivity() {

    private var unit = "ml"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_units_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        get_settings()
        set_tv()
        set_actions()
    }

    fun set_actions()
    {
        val database = Database(this)
        val mililiters_l = findViewById<RelativeLayout>(R.id.mililiters_layout)
        val ounces_l = findViewById<RelativeLayout>(R.id.ounces_layout)
        val back_tv = findViewById<ImageView>(R.id.back)

        back_tv.setOnClickListener()
        {
            finish()
        }

        mililiters_l.setOnClickListener()
        {
            val mililiters_tv = findViewById<TextView>(R.id.mililiters_tv)
            mililiters_tv.setTextColor(resources.getColor(R.color.green))
            val ounces_tv = findViewById<TextView>(R.id.ounces_tv)
            ounces_tv.setTextColor(resources.getColor(R.color.white))
            database.set_units("ml")
        }

        ounces_l.setOnClickListener()
        {
            val ounces_tv = findViewById<TextView>(R.id.ounces_tv)
            ounces_tv.setTextColor(resources.getColor(R.color.green))
            val mililiters_tv = findViewById<TextView>(R.id.mililiters_tv)
            mililiters_tv.setTextColor(resources.getColor(R.color.white))
            database.set_units("oz")
        }
    }

    fun get_settings()
    {
        val database = Database(this)
        database.set_default_settings()
        val cursor = database.get_settings()

        if(cursor!=null && cursor.count != 0)
        {
            cursor.moveToNext()
            unit = cursor.getString(1)
        }
    }

    fun set_tv()
    {
        if (unit == "ml")
        {
            val mililiters_tv = findViewById<TextView>(R.id.mililiters_tv)
            mililiters_tv.setTextColor(resources.getColor(R.color.green))
            val ounces_tv = findViewById<TextView>(R.id.ounces_tv)
            ounces_tv.setTextColor(resources.getColor(R.color.white))
        }
        else
        {
            val ounces_tv = findViewById<TextView>(R.id.ounces_tv)
            ounces_tv.setTextColor(resources.getColor(R.color.green))
            val mililiters_tv = findViewById<TextView>(R.id.mililiters_tv)
            mililiters_tv.setTextColor(resources.getColor(R.color.white))
        }
    }
}