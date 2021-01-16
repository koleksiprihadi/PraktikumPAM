package com.krisnaprihadiyanto.tabbedaplication.detail.event

import com.krisnaprihadiyanto.tabbedaplication.model.EventDetail


interface EventDetailView{
    fun showLoading()
    fun hideLoading()
    fun showEventDetail(data: List<EventDetail>)
}