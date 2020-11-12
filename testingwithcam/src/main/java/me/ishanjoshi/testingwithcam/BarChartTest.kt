package me.ishanjoshi.testingwithcam

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.activity_bar_chart_test.*
import me.ishanjoshi.mpandroid_accessible.MMRainfallFormatter
import me.ishanjoshi.mpandroid_accessible.WeekdayFormatter
import java.util.*

class BarChartTest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart_test)


        setupChartAndData()

        //TODO: add accessibility to the barChart
        /**
         * Any thing can be changed here.
         * To get the data from the chart itself, you can use the following code snippet commented below
         */

        /*val dataSet = barChart.barData.getDataSetByIndex(0)
        for (i in 0 until dataSet.entryCount) {
            // Get each entry
            val entry = dataSet.getEntryForIndex(i)
            val date = entry.data as? Date
            Log.d(this.javaClass.simpleName, "$date: ${entry.x} ${entry.y}")
        }*/

    }

    private fun setupChartAndData() {
        val chart = barChart

        chart.setDrawValueAboveBar(true)
        chart.setMaxVisibleValueCount(60)

        val xAxisFormatter: ValueFormatter = WeekdayFormatter()

        val xAxis: XAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.labelCount = 7
        xAxis.valueFormatter = xAxisFormatter


        val custom: ValueFormatter = MMRainfallFormatter()

        val leftAxis = chart.axisLeft
        leftAxis.setLabelCount(8, false)
        leftAxis.valueFormatter = custom
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f


        val rawData: ArrayList<BarEntry> = arrayListOf()
        val now = Date()
        val dayInMilliSeconds = 24 * 60 * 60 * 1000
        val mmVals = arrayOf(3f, 5f, 6.5f, 0f, 0f, 0f, 29f)
        for (i in mmVals.indices) {
            // Adds a day to the date
            val updatedDate = Date(now.time + (i * dayInMilliSeconds))
            val addDate = (updatedDate.time - now.time)
            Log.d("DATE", addDate.toString())
            rawData.add(BarEntry(i.toFloat(), mmVals[i], updatedDate))
        }

        val barDataSet = BarDataSet(rawData, "mm_weather")
        barDataSet.color = Color.RED

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(barDataSet)

        val barData = BarData(dataSets)

        barData.setValueTextSize(10f)
        barData.barWidth = 0.9f

        chart.data = barData



        chart.data.notifyDataChanged()
        chart.notifyDataSetChanged()
    }
}