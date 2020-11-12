package me.ishanjoshi.chart_accessibility_module.utils

/**
 * Stores the X and the Y coordinate.
 */
data class DataPoint(val category: Any, val proportion: Float) : Comparable<DataPoint> {
    override fun compareTo(other: DataPoint): Int {
        if (proportion < other.proportion) {
            return -1
        } else if (proportion > other.proportion) {
            return 1
        }
        return 0
    }
}