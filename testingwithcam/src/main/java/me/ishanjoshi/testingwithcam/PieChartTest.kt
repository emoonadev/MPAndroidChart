package me.ishanjoshi.testingwithcam

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.activity_pie_chart_test.*
import java.util.*

/**
 * NOTE: The default PieChart has an implementation which works fine in most cases,
 * this can be overriden to accomodate more complex use cases.
 */
private class CustomPieChart(context: Context) : PieChart(context) {

    override fun getAccessibilityDescription(): String {

        val pieData = data
        val dataSet = pieData.getDataSetByIndex(0)
        val categoryTitleDataset = dataSet.label

        val entryCount = pieData.entryCount

        val labels = arrayOfNulls<Any>(entryCount)
        val proportions = arrayOfNulls<Float>(entryCount)

        for (i in 0 until entryCount) {
            val entry = dataSet.getEntryForIndex(i)
            val percentage = entry.value / pieData.yValueSum
            labels[i] = entry.label
            proportions[i] = percentage
        }

        // TODO: use the CAM Module to generate the description.
        return "this will be spoken out"
    }
}


class PieChartTest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart_test)


        setupChart()

        //TODO: check and add accessibility to this pie chart so it reads a description

    }

    private fun setupChart() {
        val chart = pieChart

        chart.categoryTitle

        chart.setUsePercentValues(true)
        chart.setExtraOffsets(5f, 10f, 5f, 5f)
        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f

        chart.setDrawCenterText(true)

        chart.rotationAngle = 0f


        val entries = arrayListOf<PieEntry>(
                PieEntry(0.05f, "BMW"),
                PieEntry(0.21f, "Suzuki"),
                PieEntry(0.02f, "Jeep"),
                PieEntry(0.12f, "Hyundai"),
                PieEntry(0.33f, "Toyota"),
                PieEntry(0.27f, "Honda")
        )


        val dataSet = PieDataSet(entries, "Automotive market share")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors


        // add a lot of colors
        val colors = ArrayList<Int>()

        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(chart))
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        chart.data = data

        // undo all highlights
        chart.highlightValues(null)

        chart.notifyDataSetChanged()
        chart.data.notifyDataChanged()
        chart.invalidate()
    }

}