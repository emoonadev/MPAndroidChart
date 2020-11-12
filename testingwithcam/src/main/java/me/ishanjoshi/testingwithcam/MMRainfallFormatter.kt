package me.ishanjoshi.mpandroid_accessible

import com.github.mikephil.charting.formatter.ValueFormatter

class MMRainfallFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "%.2f mm".format(value)
    }
}