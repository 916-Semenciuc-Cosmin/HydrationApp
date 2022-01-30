package com.example.hydrationapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;


class history_fragment : Fragment() {

    var custom_adapter: CustomAdapter? = null
    private var database: Database? = null
    private var dates_arr = ArrayList<String>()
    private var intakes_arr = ArrayList<String>()
    private var root1: View? = null
    private var goal = 0
    private var start_date = ""
    private var end_date = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_history, container, false)
        root1 = root

        database = Database(context)
        store_records()
        get_dates()
        set_chart(root)

        custom_adapter = CustomAdapter(this.context, dates_arr, intakes_arr)
        val recycler1 = root.findViewById<RecyclerView>(R.id.records_view)
        recycler1.adapter = null

        recycler1.adapter = custom_adapter
        recycler1.layoutManager = LinearLayoutManager(this.context)

        val start_date_tv = root.findViewById<TextView>(R.id.start_date)
        val end_date_tv = root.findViewById<TextView>(R.id.end_date)
        start_date_tv.text = start_date
        end_date_tv.text = end_date

        return root
    }

    fun get_dates() {
        val cursor = database?.get_first_last()
        if (cursor != null) {
            cursor.moveToNext()
            val str_date = cursor.getString(1).split(":")
            val day = str_date[1].toInt()
            val month = str_date[2].toInt() + 1
            var str_day = day.toString()
            var str_month = month.toString()
            if (day < 10)
                str_day = "0" + str_day
            if (month < 10)
                str_month = "0" + str_month

            start_date = str_day + "." + str_month
            end_date = start_date

            if (cursor.count == 2) {
                cursor.moveToNext()
                val str_date = cursor.getString(1).split(":")
                val day = str_date[1].toInt()
                val month = str_date[2].toInt() + 1
                var str_day = day.toString()
                var str_month = month.toString()
                if (day < 10)
                    str_day = "0" + str_day
                if (month < 10)
                    str_month = "0" + str_month

                end_date = str_day + "." + str_month
            }
        }
    }

    fun set_chart(root: View) {
        var bar_entries = ArrayList<BarEntry>()
        var bar_data_set: BarDataSet
        var bar_data: BarData? = null

        for (index in 0 until intakes_arr.size) {
            bar_entries = ArrayList<BarEntry>()
            bar_entries.add(BarEntry(index.toFloat(), intakes_arr[index].toFloat()))
            bar_data_set = BarDataSet(bar_entries, "")
            bar_data_set.setDrawValues(false)

            if (intakes_arr[index].toInt() < goal)
                bar_data_set.color = resources.getColor(R.color.yellow)
            else
                bar_data_set.color = resources.getColor(R.color.green)

            bar_data_set.barShadowColor = resources.getColor(R.color.gray_2)

            if (bar_data == null) {
                bar_data = BarData(bar_data_set)
            } else {
                bar_data.addDataSet(bar_data_set)
            }
        }

        for (index in intakes_arr.size until 30) {
            bar_entries = ArrayList<BarEntry>()
            bar_entries.add(BarEntry(index.toFloat(), 0F))
            bar_data_set = BarDataSet(bar_entries, "")
            bar_data_set.setDrawValues(false)
            bar_data_set.barShadowColor = resources.getColor(R.color.gray_2)
            bar_data?.addDataSet(bar_data_set)
        }

        val bar_chart_tv = root.findViewById<BarChart>(R.id.bar_chart)
        bar_data?.barWidth = 0.5F
        bar_chart_tv.data = bar_data
        bar_chart_tv.setDrawBarShadow(true)
        bar_chart_tv.setTouchEnabled(false)

        val left_axis = bar_chart_tv.axisLeft
        left_axis.textColor = resources.getColor(R.color.white)
        bar_chart_tv.axisRight.isEnabled = false
        bar_chart_tv.xAxis.isEnabled = false
        bar_chart_tv.axisLeft.setDrawAxisLine(false)
        bar_chart_tv.description.isEnabled = false
        bar_chart_tv.axisLeft.axisMaximum = (goal + 200).toFloat()
        bar_chart_tv.axisLeft.axisMinimum = 0F

        val legend = bar_chart_tv.legend
        legend.xEntrySpace = 0.3F
        legend.xOffset = 2F
        legend.formSize = 6.45F
        legend.textColor = resources.getColor(R.color.white)
        val legend_entries = legend.entries
        for (entry in legend_entries) {
            entry.formColor = resources.getColor(R.color.gray_2)
        }
        legend_entries[0].formColor = resources.getColor(R.color.white)
        legend_entries[29].formColor = resources.getColor(R.color.white)
        legend.setCustom(legend_entries)
    }

    override fun onResume() {
        super.onResume()
        database = Database(context)
        dates_arr.clear()
        intakes_arr.clear()

        store_records()
        get_dates()
        root1?.let { set_chart(it) }

        custom_adapter = CustomAdapter(this.context, dates_arr, intakes_arr)
        val recycler1 = root1?.findViewById<RecyclerView>(R.id.records_view)
        if (recycler1 != null) {
            recycler1.adapter = null
            recycler1.adapter = custom_adapter
            recycler1.layoutManager = LinearLayoutManager(this.context)
        }
    }

    fun store_records() {
        var cursor = database?.read_records()

        if (cursor != null && cursor.count != 0) {
            while (cursor.moveToNext()) {
                dates_arr.add(cursor.getString(1))
                intakes_arr.add(cursor.getString(2))
            }
        }

        cursor = database?.get_settings()

        if (cursor != null && cursor.count != 0) {
            cursor.moveToNext()
            goal = cursor.getString(2).toInt()
        }
    }
}