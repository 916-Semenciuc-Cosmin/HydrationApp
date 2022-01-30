package com.example.hydrationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import org.w3c.dom.Text

class SizeSettings : AppCompatActivity() {
    private var type = "goal"
    private var goal = 0
    private var unit = " ml"
    private var c1_size = 0
    private var c2_size = 0
    private var c3_size = 0
    private var value = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_size_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val extras = intent.extras
        if (extras != null) {
            type = extras.getString("type").toString()
        }

        get_settings()
        set_text()
        edit_text()
        set_cancel_save()
        set_back()
    }

    fun set_back()
    {
        //here i set the back arrow to end this activity
        val back_tv = findViewById<ImageView>(R.id.back)

        back_tv.setOnClickListener()
        {
            finish()
        }
    }

    fun get_settings() {
        //here i get the settings from the database
        val database = Database(this)
        database.set_default_settings()
        val cursor = database.get_settings()

        if (cursor != null && cursor.count != 0) {
            cursor.moveToNext()
            unit = cursor.getString(1)
            goal = cursor.getString(2).toInt()
            c1_size = cursor.getString(3).toInt()
            c2_size = cursor.getString(4).toInt()
            c3_size = cursor.getString(5).toInt()
        }
    }

    fun set_text() {
        //here is where i set the text of the vies on the screen because
        // i use the same activity for the settings of the goal and for the three containers
        val toolbar_title = findViewById<TextView>(R.id.toolbar_title)
        val description = findViewById<TextView>(R.id.description)
        val units = findViewById<TextView>(R.id.units)
        val editText = findViewById<EditText>(R.id.value)

        if (unit == "ml")
            units.text = "mililiters (ml)"
        else
            units.text = "ounces (oz)"

        if (type == "goal") {
            editText.setText(goal.toString())
            toolbar_title.text = resources.getString(R.string.daily_goal)
            description.text = resources.getString(R.string.goal_settings_text)
            value = goal
        } else {
            description.text = resources.getString(R.string.container_setting_text)
        }

        when {
            type == "c1" -> {
                toolbar_title.text = resources.getString(R.string.container_1)
                editText.setText(c1_size.toString())
                value = c1_size
            }
            type == "c2" -> {
                toolbar_title.text = resources.getString(R.string.container_2)
                editText.setText(c2_size.toString())
                value = c2_size
            }
            type == "c3" -> {
                toolbar_title.text = resources.getString(R.string.container_3)
                editText.setText(c3_size.toString())
                value = c3_size
            }
        }
    }

    fun edit_text()
    {
        //here i set up the text edit view on the screen
        val edit_text = findViewById<EditText>(R.id.value)
        val cancel_tv = findViewById<TextView>(R.id.cancel)
        val save_tv = findViewById<TextView>(R.id.save)
        val back_tv = findViewById<ImageView>(R.id.back)

        edit_text.setOnFocusChangeListener(View.OnFocusChangeListener { _, hasFocus ->
            if(hasFocus)
            {
                back_tv.visibility = View.INVISIBLE
                cancel_tv.isClickable = true
                cancel_tv.visibility = View.VISIBLE
                save_tv.isClickable = true
                save_tv.visibility = View.VISIBLE
            }
        })

    }

    fun set_cancel_save()
    {
        //here i set the actions of the cancel and save buttons
        val edit_text = findViewById<EditText>(R.id.value)
        val cancel_tv = findViewById<TextView>(R.id.cancel)
        val save_tv = findViewById<TextView>(R.id.save)
        val back_tv = findViewById<ImageView>(R.id.back)

        cancel_tv.setOnClickListener()
        {
            edit_text.isEnabled = false
            edit_text.isEnabled = true
            back_tv.visibility = View.VISIBLE
            cancel_tv.isClickable = false
            cancel_tv.visibility = View.INVISIBLE
            save_tv.isClickable = false
            save_tv.visibility = View.INVISIBLE
            edit_text.setText(value.toString())
        }

        save_tv.setOnClickListener()
        {
            edit_text.isEnabled = false
            edit_text.isEnabled = true
            back_tv.visibility = View.VISIBLE
            cancel_tv.isClickable = false
            cancel_tv.visibility = View.INVISIBLE
            save_tv.isClickable = false
            save_tv.visibility = View.INVISIBLE
            value = edit_text.text.toString().toInt()
            update_database()
        }
    }

    fun update_database()
    {
        // here i update the database records if the save button is pushed
        val database = Database(this)
        when {
            type == "goal" -> {
                database.set_goal(value)
            }
            type == "c1" -> {
                database.set_c1_size(value)
            }
            type == "c2" -> {
                database.set_c2_size(value)
            }
            type == "c3" -> {
                database.set_c3_size(value)
            }
        }
    }
}