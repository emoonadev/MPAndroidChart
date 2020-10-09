package me.ishanjoshi.chart_accessibility_module

import kotlin.math.abs

fun Float.approxEqualWithin(another: Float, error: Float = 0.003f): Boolean {
    val diff = abs(this - another)
    return diff <= error
}