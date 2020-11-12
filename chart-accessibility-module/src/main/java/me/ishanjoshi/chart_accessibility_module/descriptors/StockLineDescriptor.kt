package me.ishanjoshi.chart_accessibility_module.descriptors

import me.ishanjoshi.chart_accessibility_module.IDescriptor
import org.apache.commons.math3.stat.regression.SimpleRegression
import java.text.SimpleDateFormat
import java.util.*

data class StockDataPoint(
        val timestamp: Long,
        val stockValue: Float
) {
    fun timeDescription(): String {
        val formatter = SimpleDateFormat("dd MMM YYYY hh:mm ss", Locale.getDefault())
        return formatter.format(Date(timestamp)) + " seconds"
    }
}

/**
 * Use this descriptor for stock chart data. It will read out values that are important in understanding stocks.
 */
data class StockLineDescriptor(
        val timeStamps: Array<Long>,
        val stockValues: Array<Float>,
        val stockName: String,
        val valueUnits: String = "USD"
) : IDescriptor {

    val dataPoints: List<StockDataPoint> = timeStamps.zip(stockValues).map {
        StockDataPoint(it.first, it.second)
    }.sortedBy {
        it.timestamp
    }

    private fun trendDescription(): String {
        val reg = SimpleRegression()
        dataPoints.forEach {
            reg.addData(it.timestamp.toDouble(), it.stockValue.toDouble())
        }

        val m = reg.slope

        return when {
            m < 0 -> "downwards"
            m > 0 -> "upwards"
            m == 0.toDouble() -> "stable"
            else -> "Invalid"
        }
    }

    private fun valueDescription(stockDataPoint: StockDataPoint): String {
        return "%.2f $valueUnits".format(stockDataPoint.stockValue)
    }

    private fun timeRange(): String {
        return "${dataPoints.first().timeDescription()} to ${dataPoints.last().timeDescription()}"
    }

    override fun describe(): String {
        if (dataPoints.size < 2) {
            return "Not enough data is provided to describe this stock chart for $stockName"
        }

        val trendDescription = trendDescription()

        val timeRange = timeRange()

        val startingValue = valueDescription(dataPoints.first())
        val closingValue = valueDescription(dataPoints.last())

        val maxEntry = dataPoints.maxBy { it.stockValue }
        val minEntry = dataPoints.minBy { it.stockValue }


        return "The line chart shows information about $stockName, which is trending $trendDescription. " +
                "The chart shows data from $timeRange. The starting value is $startingValue and the closing value is $closingValue. " +
                "The minimum value is ${valueDescription(minEntry!!)} on ${minEntry.timeDescription()}. " +
                "The maximum value is ${valueDescription(maxEntry!!)} on ${maxEntry.timeDescription()}. "
    }

}