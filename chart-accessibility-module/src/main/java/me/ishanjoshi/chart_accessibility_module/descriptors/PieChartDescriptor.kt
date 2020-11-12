package me.ishanjoshi.chart_accessibility_module.descriptors

import me.ishanjoshi.chart_accessibility_module.IDescriptor
import me.ishanjoshi.chart_accessibility_module.extensions.approxEqualWithin
import me.ishanjoshi.chart_accessibility_module.utils.DataPoint
import me.ishanjoshi.chart_accessibility_module.utils.warn


/**
 * Maximum values that can be retained by an average person
 */
private const val MILLERS_MEMORY_LIMIT = 7

/**
 * Determines how the values are read
 */
enum class ReadOrder {
    Ascending,
    Descending
}



/**
 * Descriptor that takes in pie chart data and generates a description.
 * @property categories the labels shown in the chart legend
 * @property proportions floating values representing percentage of slice taken
 * @property categoriesTitle a grouping for all the categories. e.g. Automobile company (Honda, 20%), (Toyota, 30%), ...
 * @property title an optional string that generates contextual introduction
 * @property numValuesToRead limit to reading the top `n` values
 * @property readOrder ascending or descending
 * Best used for pie chart like data that describe proportions.
 */
data class PieChartDescriptor @JvmOverloads constructor(
        private var categories: Array<Any>,
        private var proportions: Array<Float>,
        private var categoriesTitle: String,
        private var title: String? = null,
        private var contextDescription: String? = null,
        private var numValuesToRead: Int = MILLERS_MEMORY_LIMIT,
        private var readOrder: ReadOrder = ReadOrder.Descending
) : IDescriptor {

    /**
     * Converted format to contain both X and Y values in one class
     */
    private val dataDataPoint: List<DataPoint>

    init {
        // Cannot read more than the items available
        numValuesToRead = proportions.size.coerceAtMost(numValuesToRead)

        // Ensure that if the sizes of x and y are inconsistent, they become same size.
        dataDataPoint = categories.zip(proportions).map { DataPoint(it.first, it.second) }

        if (categories.size != proportions.size) {
            warn("Data list sizes do not match! Some entries may be omitted")
        }

        if (proportions.sum() != 1f) {
            warn("Proportions do not add up to the full 100% of the pie.")
        }

    }

    private fun percentDescription(float: Float): String {
        // https://www.ieltsbuddy.com/ielts-pie-chart.html
        return when {
            float < 0.03 -> return "a minority"
            float == 0.1f -> return "one in 10"
            float == 0.2f -> return "one in 5"
            float == 0.3f -> return "three in 10"
            float.approxEqualWithin((1 / 3f), 0.02f) -> return "approximately a third"
            float == 0.4f -> return "four in 10"
            float == 0.5f -> return "half"
            float.approxEqualWithin(0.5f, 0.02f) -> "approximately half"
            float == 0.75f -> return "third"
            float.approxEqualWithin(0.75f, 0.02f) -> "almost a third"
            float.approxEqualWithin(0.8f, 0.03f) -> "four fifths"
            float >= 0.98 -> "almost all"
            else -> "%.2f percent".format(float * 100)
        }
    }

    private fun title(): String {
        return title?.also { t -> return t } ?: kotlin.run {
            return "proportion of $categoriesTitle"
        }
    }

    private fun descForDataPoint(dataPoint: DataPoint): String {
        val fillerWords = arrayOf("fills", "takes")
        return "${dataPoint.category} ${fillerWords.random()} up ${percentDescription(dataPoint.proportion)} of $categoriesTitle"
    }


    private fun dataDescription(): String {

        // Sort it in sort order
        var dataMerged: List<DataPoint> = dataDataPoint.sortedDescending()

        if (readOrder == ReadOrder.Ascending) {
            dataMerged = dataMerged.reversed()
        }

        val valuesToRead = numValuesToRead
        val valuesIgnored = dataMerged.size - valuesToRead


        val dataDesc = dataMerged.take(valuesToRead).map {
            descForDataPoint(it)
        }.joinToString(",")


        val ignoredValuesMessage = if (valuesIgnored == 0) "" else "$valuesIgnored values were ignored in the description"

        val others = dataMerged.subList(valuesToRead, dataMerged.size)
        val othersSum = others.sumByDouble { it.proportion.toDouble() }
        val othersMessage = "Others take up ${percentDescription(othersSum.toFloat())} proportion"

        return "There are ${dataMerged.size} data points available. $dataDesc. $ignoredValuesMessage. $othersMessage"
    }

    override fun describe(): String {
        val chartTitle = title()
        val dataDescription = dataDescription()

        return "The pie chart describes $chartTitle. $dataDescription"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PieChartDescriptor

        if (!categories.contentEquals(other.categories)) return false
        if (!proportions.contentEquals(other.proportions)) return false
        if (categoriesTitle != other.categoriesTitle) return false
        if (title != other.title) return false
        if (contextDescription != other.contextDescription) return false
        if (numValuesToRead != other.numValuesToRead) return false
        if (readOrder != other.readOrder) return false
        if (dataDataPoint != other.dataDataPoint) return false

        return true
    }

    override fun hashCode(): Int {
        var result = categories.contentHashCode()
        result = 31 * result + proportions.contentHashCode()
        result = 31 * result + categoriesTitle.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (contextDescription?.hashCode() ?: 0)
        result = 31 * result + numValuesToRead
        result = 31 * result + readOrder.hashCode()
        result = 31 * result + dataDataPoint.hashCode()
        return result
    }

}

