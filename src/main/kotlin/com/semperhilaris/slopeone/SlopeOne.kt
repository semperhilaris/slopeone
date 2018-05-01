package com.semperhilaris.slopeone

import java.util.HashMap

/**
 * Slope One collaborative filtering for rated resources.
 */
class SlopeOne {

    private val items = HashMap<String, Item>()
    private val diffs = HashMap<Item, HashMap<Item, Double>>()
    private val freqs = HashMap<Item, HashMap<Item, Int>>()

    /**
     * Reports instance status.
     */
    fun status(): String {
        return "OK"
    }

    /**
     * Reset the instance.
     */
    fun clear() {
        diffs.clear()
        freqs.clear()
    }

    /**
     * Update matrices with user preference data.
     */
    fun update(updateRequest: UpdateRequest) {
        for (entry in updateRequest.entries) {
            for ((key1, value1) in entry) {
                val item1 = getItem(key1)
                if (!freqs.containsKey(item1)) { freqs[item1] = HashMap() }
                if (!diffs.containsKey(item1)) { diffs[item1] = HashMap() }
                for ((key2, value2) in entry) {
                    val item2 = getItem(key2)
                    if (!freqs[item1]!!.containsKey(item2)) { freqs[item1]!![item2] = 0 }
                    if (!diffs[item1]!!.containsKey(item2)) { diffs[item1]!![item2] = 0.0 }
                    freqs[item1]!![item2] = freqs[item1]!![item2]!! + 1
                    diffs[item1]!![item2] = diffs[item1]!![item2]!! + (value1 - value2)
                }
            }
        }
        for ((item1, value1) in diffs) {
            for ((item2, value2) in value1) {
                diffs[item1]!![item2] = diffs[item1]!![item2]!! / freqs[item1]!![item2]!!.toDouble()
            }
        }
    }

    /**
     * Recommend new items given known item ratings.
     */
    fun predict(predictRequest: PredictRequest): PredictResponse {
        val predictions = HashMap<Item, Double>()
        val frequencies = HashMap<Item, Int>()
        val predictResponse = PredictResponse(HashMap())

        for ((key1, value1) in predictRequest.entries) {
            val item1 = getItem(key1)
            for ((diffItem, diffValue) in diffs) {
                if (freqs.containsKey(diffItem) && freqs[diffItem]!!.containsKey(item1)) {
                    val frequency = freqs[diffItem]!![item1]
                    if (!predictions.containsKey(diffItem)) { predictions[diffItem] = 0.0 }
                    if (!frequencies.containsKey(diffItem)) { frequencies[diffItem] = 0 }
                    predictions[diffItem] = predictions[diffItem]!! + frequency!!.times(diffValue[item1]!! + value1)
                    frequencies[diffItem] = frequencies[diffItem]!! + frequency
                }
            }
        }
        for ((key1, value1) in predictions) {
            if (!predictRequest.entries.containsKey(key1.label) && frequencies[key1]!! > 0) {
                predictResponse.predictions[key1.label] = value1/frequencies[key1]!!.toDouble()
            }
        }
        return predictResponse
    }

    /**
     * Returns the item corresponding to a label.
     */
    private fun getItem(label: String): Item {
        val item: Item
        if (items.containsKey(label)) {
            item = items[label]!!
        } else {
            item = Item(label)
            items[label] = item
        }
        return item
    }
}