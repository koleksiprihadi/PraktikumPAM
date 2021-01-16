package com.krisnaprihadiyanto.tabbedaplication.event

import android.content.Context
import com.krisnaprihadiyanto.tabbedaplication.model.Event

interface EventView{
    abstract val ctx: Context

    fun showLoading()
    fun hideLoading()
    fun showEventList(data: List<Event>)
    fun <T> startActivity(
        pair: Pair<String, String>,
        pair1: Pair<String, String>,
        pair2: Pair<String, String>
    )
}