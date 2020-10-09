package me.ishanjoshi.chart_accessibility_module


/**
 * Descriptor that takes in pie chart data and generates a description.
 * @property categories the labels shown in the chart legend
 * @property counts floating values representing percentage of slice taken
 * @property categoriesTitle a grouping for all the categories. e.g. Automobile company (Honda, 20%), (Toyota, 30%), ...
 * @property title an optional string that generates contextual introduction
 * @property numValuesToRead limit to reading the top `n` values
 * @property readOrder ascending or descending
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




