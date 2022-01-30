package com.example.hydrationapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val context: Context?,
                    private val dates_arr: ArrayList<String>,
                    private val intakes_arr: ArrayList<String>
                    ) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    private var goal = 0
    private var units = "ml"

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date_tv = itemView.findViewById<TextView>(R.id.date)
        val goal_tv = itemView.findViewById<TextView>(R.id.goal)
        val percentage_tv = itemView.findViewById<TextView>(R.id.percentage)
        val check_mark = itemView.findViewById<ImageView>(R.id.check_mark)
        val intake_tv = itemView.findViewById<TextView>(R.id.intake) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.row_layout, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        set_goals_units()
        val date = get_date(dates_arr[position])
        holder.date_tv.text = date
        holder.intake_tv.text = intakes_arr[position] + " " + units
        val percentage = (intakes_arr[position].toInt() * 100)/goal
        holder.percentage_tv.text = percentage.toString() + "%"
        holder.goal_tv.text = "out of " + goal + " " + units + " Goal"
        if (percentage < 100)
        {
            holder.check_mark.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return dates_arr.size
    }

    fun get_date(str_date: String): String
    {
        val list_date = str_date.split(":")
        val str_day_week = list_date[0]
        val day = list_date[1]
        val str_month = list_date[2]
        var day_week = ""
        var month = ""
        var date = ""

        when (str_day_week) {
            "2" -> day_week = "Monday"
            "3" -> day_week = "Tuesday"
            "4" -> day_week = "Wednesday"
            "5" -> day_week = "Thursday"
            "6" -> day_week = "Friday"
            "7" -> day_week = "Saturday"
            "1" -> day_week = "Sunday"
        }

        when (str_month) {
            "0" -> month = "January"
            "1" -> month = "February"
            "2" -> month = "March"
            "3" -> month = "April"
            "4" -> month = "May"
            "5" -> month = "June"
            "6" -> month = "July"
            "7" -> month = "August"
            "8" -> month = "September"
            "9" -> month = "October"
            "10" -> month = "November"
            "11" -> month = "December"
        }

        date = "$day_week, $day $month"
        return  date
    }

    fun set_goals_units()
    {
        val database = Database(context)
        val cursor = database.get_settings()

        if (cursor != null && cursor.count != 0)
        {
            cursor.moveToNext()
            units = cursor.getString(1)
            goal = cursor.getString(2).toInt()
        }
    }
}