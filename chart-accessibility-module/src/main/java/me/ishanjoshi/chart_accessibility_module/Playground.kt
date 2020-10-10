package me.ishanjoshi.chart_accessibility_module


import me.ishanjoshi.chart_accessibility_module.descriptors.BarChartDescriptor
import me.ishanjoshi.chart_accessibility_module.descriptors.PieChartDescriptor
import me.ishanjoshi.chart_accessibility_module.descriptors.StockLineDescriptor
import me.ishanjoshi.chart_accessibility_module.descriptors.WeatherColumnDescriptor
import org.apache.commons.math3.fitting.HarmonicCurveFitter
import org.apache.commons.math3.fitting.PolynomialCurveFitter
import org.apache.commons.math3.fitting.WeightedObservedPoints
import org.apache.commons.math3.stat.regression.SimpleRegression
import java.util.*
import kotlin.math.sin



fun main() {

    val pcbd = PieChartDescriptor(
            arrayOf("iOS", "Android", "Windows", "Symbian"),
            arrayOf(0.51f, 0.45f, 0.03f, 0.01f, 0.02f),
            "Operating system",
            numValuesToRead = 2
    ).describe()

    val pcbds = PieChartDescriptor(
            "Maruti,Hyundai,Mahindra,Tata,Honda,Toyota,Renault,Ford".split(",").toTypedArray(),
            arrayOf(0.52f, 0.17f, 0.08f, 0.07f, 0.05f, 0.04f, 0.03f, 0.04f),
            "Automobile Companies"
    ).describe()

    println(pcbd)
    println(pcbds)

    val barChartDescriptorDescription = BarChartDescriptor(
            arrayOf("Jazz", "City", "Accord", "HRV"),
            arrayOf(333f, 3223f, 234f, 342f),
            "Car model",
            "sale count",
            "Honda Car model sales count for 2020"
    ).describe()

    println(barChartDescriptorDescription)

    val dayInMS = 24 * 60 * 60 * 1000
    val wrcd = WeatherColumnDescriptor(
            arrayOf(Date().time, Date().time + dayInMS),
            arrayOf(0f, 12f),
            "Clayton"
    ).describe()
    println(wrcd)

    val ti = 60 * 1000 // 60 seconds
    val stcd = StockLineDescriptor(
            arrayOf(Date().time, Date().time + ti, Date().time + ti + ti),
            arrayOf(773f, 3243.324f, 773f),
            "Amazon (AMZN)",
            "US Dollars"
    ).describe()
    println(stcd)

}
