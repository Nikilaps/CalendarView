package com.codehelp.calendarviewexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.codehelp.calendarview.CustomCalendarView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        val dtStart = "2019-05-01"
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var date: Date? = null
        try {
            date = format.parse(dtStart)
            date = DateUtil.addDays(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val cv = findViewById<CustomCalendarView>(R.id.calendar_view)
        cv.loadComponents(this, calendar.time, date, CustomCalendarView.SelectionMode.RANGE)
        cv.setHeaderBackground(R.color.colorAccent)
        cv.setOnDateSelectedListener(object : CustomCalendarView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                Toast.makeText(this@MainActivity, sdf.format(date), Toast.LENGTH_SHORT).show()
            }

            override fun onDateUnSelected(date: Date) {

            }
        })
    }

    object DateUtil {
        internal fun addDays(date: Date?): Date {
            val cal = Calendar.getInstance()
            cal.time = date!!
            cal.add(Calendar.DATE, 1)
            return cal.time
        }
    }
}
