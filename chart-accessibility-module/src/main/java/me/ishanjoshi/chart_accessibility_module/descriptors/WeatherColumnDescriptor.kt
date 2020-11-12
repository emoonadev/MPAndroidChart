package me.ishanjoshi.chart_accessibility_module.descriptors

import me.ishanjoshi.chart_accessibility_module.IDescriptor
import me.ishanjoshi.chart_accessibility_module.utils.warn
import java.text.SimpleDateFormat
import java.util.*

data class RainfallDataPoint(
        val dateStamp: Date,
        val rainfall: Float
) {
    fun dayDescription(): String {
        val formatter = SimpleDateFormat("E dd MMM", Locale.getDefault())
        return formatter.format(dateStamp)
    }

    fun rainTextDescription(): String {
        val rfValue = "%.2f millimeters".format(rainfall)
        return when (rainfall) {
            0f -> "none"
            in 0f..2f -> "drizzle of $rfValue"
            in 2f..4f -> "light rain of $rfValue"
            in 5f..6f -> "moderate rain of $rfValue"
            in 8f..15f -> "moderate strong rain of $rfValue"
            in 15f..20f -> "strong rain of $rfValue"
            in 30f..700f -> "heavy rainfall of $rfValue"
            else -> rfValue
        }
    }
}

data class WeatherColumnDescriptor @JvmOverloads constructor(
        val epochTimeInWeek: Array<Long>,
        val rainfallMM: Array<Float>,
        val location: String? = null
) : IDescriptor {

    private val dataItems: List<RainfallDataPoint> = epochTimeInWeek.zip(rainfallMM).map {
        RainfallDataPoint(Date(it.first), it.second)
    }.filter { it.rainfall >= 0f } // only 0mm or more, cannot have negative rainfall.

    init {

        if (epochTimeInWeek.size != rainfallMM.size) {
            warn("Corresponding entries cannot be found, some entries may be omitted")
        } else if (dataItems.size != epochTimeInWeek.size) {
            warn("Cannot have negative rainfall")
        }

    }

    override fun describe(): String {

        val forLocation = if (location == null) "" else "for $location"

        val contextSetting = "This column chart describes the forecasted rainfall $forLocation in the upcoming week. It has ${dataItems.size} entries"

        val eachDayDescription = dataItems.map {
            return@map "On ${it.dayDescription()}, ${it.rainTextDescription()} is forecasted"
        }.joinToString(",")

        return "$contextSetting. $eachDayDescription"
    }

}