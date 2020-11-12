package me.ishanjoshi.chart_accessibility_module

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import org.apache.commons.math3.stat.regression.SimpleRegression
import kotlin.math.abs


class LineChartDescriptor {

    fun getDescription(title: String, x: List<Float>, y: List<Float>, xUnit: String, yUnit: String): String {
        val xDoubled = x.map { value -> value.toDouble() }
        val yDoubled = y.map { value -> value.toDouble() }

        // Linear regression
        val simpleRegression = SimpleRegression(true)
        val stats = DescriptiveStatistics()

        xDoubled.forEachIndexed { index, d ->
            run {
                simpleRegression.addData(d, yDoubled[index])
                stats.addValue(yDoubled[index])
            }
        }

        val trendPosNeg = getTrendDescription(simpleRegression.r)
        val riseOrFall = if (simpleRegression.r > 0) "Rise" else "Fall"
        val riseFallValue = simpleRegression.slope

        return "This is a line chart!! It has ${x.size} entries. The trend is $trendPosNeg. There is an observed $riseOrFall in the data by ${String.format("%.2f",riseFallValue)} every $xUnit"

    }

    private fun getTrendDescription(r: Double): String {
        val threshold = 0.5
        return when {
            r > threshold -> "Positive"
            r < -1 * threshold -> "Negative"
            abs(r) < threshold -> "Not calculated found"
            else -> "Invalid entry"
        }
    }

}