package me.ishanjoshi.mpandroid_accessible

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class WeekdayFormatter(val now: Date = Date()) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val sdf = SimpleDateFormat("E", Locale.getDefault())
        // float values will be 1,2,3,....
        val dayInMS = 24 * 60 * 60 * 1000
        val dateMS: Long = (value * dayInMS).toLong() + now.time
        return sdf.format(Date(dateMS))
    }
}