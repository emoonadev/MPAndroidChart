package me.ishanjoshi.chart_accessibility_module

/**
 * Pie charts can be described using this descriptor. They contain the following important pieces of information.
 * * **Category name**
 * * **Proportion**
 * * Title
 * * Context
 * * Read Count
 * * Read order
 *
 *
 */

private data class XY(val category: Any, val proportion: Float) : Comparable<XY> {
    override fun compareTo(other: XY): Int {
        if (proportion < other.proportion) {
            return -1
        } else if (proportion > other.proportion) {
            return 1
        }
        return 0
    }
}

class PieChartDescriptor(
        val categories: Array<Any>,
        val proportions: Array<Float>,
        val categoriesTitle: String,
        var title: String?,
        var contextDescription: String?,
        var numValuesToRead: Int,
        var readOrder: ReadOrder
) : IDescriptor {

    enum class ReadOrder {
        Ascending,
        Descending
    }


    data class Builder(
            private var categories: Array<Any>,
            private var proportions: Array<Float>,
            private var categoriesTitle: String,
            private var title: String? = null,
            private var contextDescription: String? = null,
            private var numValuesToRead: Int = Math.min(categories.size, 0),
            private var readOrder: ReadOrder = ReadOrder.Descending
    ) {
        fun categories(data: Array<Any>) = apply { categories = data }
        fun proportions(data: Array<Float>) = apply { proportions = data }
        fun categoriesTitle(title: String) = apply { categoriesTitle = title }
        fun title(title: String?) = apply { this.title = title }
        fun contextDescription(description: String?) = apply { contextDescription = description }
        fun numValuesToRead(readCount: Int) = apply { numValuesToRead = readCount }
        fun readOrder(readOrder: ReadOrder) = apply { this.readOrder = readOrder }

        fun build() = PieChartDescriptor(
                categories,
                proportions,
                categoriesTitle,
                title,
                contextDescription,
                numValuesToRead,
                readOrder
        )
    }

    /**
     * Array of objects implementing `.toString()` which will be used as the label.
     */
//    lateinit var categories: Array<Any>

    /**
     * A floating array of proportions. Ideally this should add up to 100.
     * If it does not, it will be counted as unknown.
     */
//    lateinit var proportions: Array<Float>


//    var title: String? = null
//
//    var contextDescription: String? = null
//
//    var numValuesToRead: Int = 0
//
//    var readOrder = ReadOrder.Descending


    private fun title(): String {
        return title?.also { t -> return t } ?: kotlin.run {
            return "proportion of $categoriesTitle"
        }
    }

    private fun dataDescription(): String {

        // Sort it in sort order
        var dataMerged: List<XY> = categories.zip(proportions).map {
            XY(it.first, it.second)
        }.sortedDescending()

        if (readOrder == ReadOrder.Ascending) {
            dataMerged = dataMerged.reversed()
        }

        print(dataMerged)


        return "There are ${dataMerged.size} data points available."
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
        if (title != other.title) return false
        if (contextDescription != other.contextDescription) return false
        if (numValuesToRead != other.numValuesToRead) return false
        if (readOrder != other.readOrder) return false

        return true
    }

    override fun hashCode(): Int {
        var result = categories.contentHashCode()
        result = 31 * result + proportions.contentHashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (contextDescription?.hashCode() ?: 0)
        result = 31 * result + numValuesToRead
        result = 31 * result + readOrder.hashCode()
        return result
    }

}




