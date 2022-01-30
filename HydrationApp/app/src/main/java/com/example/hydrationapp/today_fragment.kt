package com.example.hydrationapp

import android.os.Bundle
import android.service.media.MediaBrowserService
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.awaitAll
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class today_fragment : Fragment() {
    private var current_intake = 0
    private var percentage = 0
    private var goal = 0
    private var unit = " ml"
    private var c1_size = 0
    private var c2_size = 0
    private var c3_size = 0
    private var root1: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_today, container, false)
        root1 = root

        get_settings()
        set_variables(root)
        set_records(root)
        set_buttons(root)

        return root
    }

    override fun onResume() {
        super.onResume()

        get_settings()
        root1?.let { set_variables(it) }
        root1?.let { set_percentage(it) }
    }

    fun get_settings()
    {
        val database = Database(this.context)
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

    fun set_variables(root: View)
    {
        val goal_tv = root.findViewById<TextView>(R.id.goal)
        val str_goal = "of " + goal.toString() + unit + " Goal"
        goal_tv.text = str_goal

        val intake_tv = root.findViewById<TextView>(R.id.intake)
        val str_intake = current_intake.toString() + unit
        intake_tv.text = str_intake

        val button1 = root.findViewById<Button>(R.id.add_1)
        val str_button1 = c1_size.toString() + unit
        button1.text = str_button1

        val button2 = root.findViewById<Button>(R.id.add_2)
        val str_button2 = c2_size.toString() + unit
        button2.text = str_button2

        val button3 = root.findViewById<Button>(R.id.add_3)
        val str_button3 = c3_size.toString() + unit
        button3.text = str_button3
    }

    fun set_records(root: View)
    {
        val calendar = Calendar.getInstance()
        val day_week = calendar.get(Calendar.DAY_OF_WEEK)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val date = "$day_week:$day:$month:$year"

        val database = Database(this.context)
        val cursor = database.read_record(date)


        if (cursor != null && cursor.count != 0)
        {
            cursor.moveToNext()
            current_intake = cursor.getString(2).toInt()
            set_percentage(root)
        }
        else
        {
            this.context?.let { database.add_record(it, date, current_intake) }
        }
    }

    fun set_buttons(root: View) {
        val intake_tv = root.findViewById<TextView>(R.id.intake)
        val add_1_tv = root.findViewById<Button>(R.id.add_1)
        val add_2_tv = root.findViewById<Button>(R.id.add_2)
        val add_3_tv = root.findViewById<Button>(R.id.add_3)


        add_1_tv.setOnClickListener()
        {
            val added = (add_1_tv.text.split(" ")[0]).toInt()
            val str_intake = (current_intake + added).toString() + unit
            intake_tv.text = str_intake
            current_intake += added
            set_percentage(root)
        }

        add_2_tv.setOnClickListener()
        {
            val added = (add_2_tv.text.split(" ")[0]).toInt()
            val str_intake = (current_intake + added).toString() + unit
            intake_tv.text = str_intake
            current_intake += added
            set_percentage(root)
        }

        add_3_tv.setOnClickListener()
        {
            val added = (add_3_tv.text.split(" ")[0]).toInt()
            val str_intake = (current_intake + added).toString() + unit
            intake_tv.text = str_intake
            current_intake += added
            set_percentage(root)
        }
    }


    fun set_percentage(root: View) {
        val intake_tv = root.findViewById<TextView>(R.id.intake)
        intake_tv.text = current_intake.toString() + unit

        val percentage_tv = root.findViewById<TextView>(R.id.percentage)
        val goal = root.findViewById<TextView>(R.id.goal).text.split(" ")[1].toInt()

        percentage = (current_intake * 100) / goal
        val str_percentage = percentage.toString() + "%"
        percentage_tv.text = str_percentage

        val glass_tv = root.findViewById<ImageView>(R.id.transparent_glass)

        when {
            percentage >= 100 -> glass_tv.setImageResource(R.drawable.glass_1)
            percentage >= 90 -> glass_tv.setImageResource(R.drawable.glass_2)
            percentage >= 80 -> glass_tv.setImageResource(R.drawable.glass_3)
            percentage >= 70 -> glass_tv.setImageResource(R.drawable.glass_4)
            percentage >= 60 -> glass_tv.setImageResource(R.drawable.glass_5)
            percentage >= 50 -> glass_tv.setImageResource(R.drawable.glass_6)
            percentage >= 40 -> glass_tv.setImageResource(R.drawable.glass_7)
            percentage >= 30 -> glass_tv.setImageResource(R.drawable.glass_8)
            percentage >= 20 -> glass_tv.setImageResource(R.drawable.glass_9)
            percentage >= 10 -> glass_tv.setImageResource(R.drawable.glass_10)
            percentage >= 1 -> glass_tv.setImageResource(R.drawable.glass_11)
        }

        val calendar = Calendar.getInstance()
        val day_week = calendar.get(Calendar.DAY_OF_WEEK)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val date = "$day_week:$day:$month:$year"
        val database = Database(context)
        context?.let { database.update_record(it, date, current_intake) }
    }
}