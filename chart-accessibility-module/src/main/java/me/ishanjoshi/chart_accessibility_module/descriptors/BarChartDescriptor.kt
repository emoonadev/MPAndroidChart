package me.ishanjoshi.chart_accessibility_module.descriptors

import me.ishanjoshi.chart_accessibility_module.IDescriptor
import me.ishanjoshi.chart_accessibility_module.utils.DataPoint
import me.ishanjoshi.chart_accessibility_module.utils.warn


/**
 * Bar chart descriptor should be used in situations where there is no temporal data,
 * this reads out individual values and counts one at a time in descending order.
 * No trend information like data should be used with this.
 *
 * **Sample description:**
 * The bar chart describes Honda Car model sales count for 2020. There are 4 data points available. 3223.0 sale count for City,342.0 sale count for HRV,333.0 sale count for Jazz,234.0 sale count for Accord.
 */
data class BarChartDescriptor @JvmOverloads constructor(
        private var categories: Array<Any>,
        private var counts: Array<Float>,
        private var categoriesTitle: String,
        private var countsTitle: String,
        private var title: String? = null,
        private var contextDescription: String? = null,
        private var readOrder: ReadOrder = ReadOrder.Descending
) : IDescriptor {

    /**
     * Converted format to contain both X and Y values in one class
     */
    private val dataDataPoint: List<DataPoint>

    init {
        // Ensure that if the sizes of x and y are inconsistent, they become same size.
        dataDataPoint = categories.zip(counts).map { DataPoint(it.first, it.second) }

        if (categories.size != counts.size) {
            warn("Data list sizes do not match! Some entries may be omitted")
        }

    }

    private fun title(): String {
        return title?.also { t -> return t } ?: kotlin.run {
            return "proportion of $categoriesTitle"
        }
    }

    private fun descForDataPoint(dataPoint: DataPoint): String {
        return "${dataPoint.proportion} $countsTitle for ${dataPoint.category}"
    }


    private fun dataDescription(): String {

        // Sort it in sort order
        var dataMerged: List<DataPoint> = dataDataPoint.sortedDescending()

        if (readOrder == ReadOrder.Ascending) {
            dataMerged = dataMerged.reversed()
        }


        val dataDesc = dataMerged.map {
            descForDataPoint(it)
        }.joinToString(",")


        return "There are ${dataMerged.size} data points available. $dataDesc."
    }

    override fun describe(): String {
        val chartTitle = title()
        val dataDescription = dataDescription()

        return "The bar chart describes $chartTitle. $dataDescription"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BarChartDescriptor

        if (!categories.contentEquals(other.categories)) return false
        if (!counts.contentEquals(other.counts)) return false
        if (categoriesTitle != other.categoriesTitle) return false
        if (title != other.title) return false
        if (contextDescription != other.contextDescription) return false
        if (readOrder != other.readOrder) return false
        if (dataDataPoint != other.dataDataPoint) return false

        return true
    }

    override fun hashCode(): Int {
        var result = categories.contentHashCode()
        result = 31 * result + counts.contentHashCode()
        result = 31 * result + categoriesTitle.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (contextDescription?.hashCode() ?: 0)
        result = 31 * result + readOrder.hashCode()
        result = 31 * result + dataDataPoint.hashCode()
        return result
    }

}




