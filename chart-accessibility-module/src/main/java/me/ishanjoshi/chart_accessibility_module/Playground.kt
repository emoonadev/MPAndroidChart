package me.ishanjoshi.chart_accessibility_module


import org.apache.commons.math3.fitting.HarmonicCurveFitter
import org.apache.commons.math3.fitting.PolynomialCurveFitter
import org.apache.commons.math3.fitting.WeightedObservedPoints
import org.apache.commons.math3.stat.regression.SimpleRegression
import java.util.*
import kotlin.math.sin

data class ChartPoint(val x: Double, val y: Double)


fun main() {
//    sineInterpolationCheck()
//    polynomialFitter()
//    linearRegressionFitter()

    val pcbd = PieChartDescriptor.Builder(
            arrayOf("Category1", "Category2"),
            arrayOf(0.3f, 0.5f),
            "Categories"
    ).numValuesToRead(1).readOrder(PieChartDescriptor.ReadOrder.Ascending).build().describe()

    println()
    println(pcbd)

}

fun sineInterpolationCheck() {
    val array = Array(10) { pos -> ChartPoint(pos.toDouble(), Math.random() * 0.1 + sin(pos.toDouble())) }

    val weightedObservedPoints = WeightedObservedPoints()
    array.forEach { chartPoint -> weightedObservedPoints.add(chartPoint.x, chartPoint.y) }

    val harmonicCurveFitter = HarmonicCurveFitter.create()

    val res = harmonicCurveFitter.fit(weightedObservedPoints.toList())

    weightedObservedPoints.toList().forEach { point -> print("[${point.x}, ${point.y}]") }
    println("")
    println("Sine interpolation is ${Arrays.toString(res)}")
}

fun polynomialFitter() {
    val observedPoints = WeightedObservedPoints()

    for (i in 1..10) {
        observedPoints.add(i.toDouble(), i * i.toDouble())
    }

    val polynomialCurveFitter = PolynomialCurveFitter.create(2)
    val res = polynomialCurveFitter.fit(observedPoints.toList())

    println(Arrays.toString(res))

}


fun linearRegressionFitter() {
    val simpleRegression = SimpleRegression(true)
    for (i in 1..100) {
        simpleRegression.addData(i.toDouble(), Math.random() * 5 + i)
    }

    println(simpleRegression)
}


