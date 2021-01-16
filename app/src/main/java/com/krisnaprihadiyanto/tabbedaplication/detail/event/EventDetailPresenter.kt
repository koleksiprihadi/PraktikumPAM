package com.krisnaprihadiyanto.tabbedaplication.detail.event

import com.google.gson.Gson
import com.krisnaprihadiyanto.tabbedaplication.api.ApiRepository
import com.krisnaprihadiyanto.tabbedaplication.api.TheSportDBApi
import com.krisnaprihadiyanto.tabbedaplication.model.EventDetailResponse
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Avin on 06/09/2018.
 */
class EventDetailPresenter(private val view: EventDetailView,
                           private val apiRepository: ApiRepository,
                           private val gson: Gson){

    fun getEventDetail(eventId: String?){
        view.showLoading()
        doAsync {
            val data = gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getEventDetail(eventId)),
                EventDetailResponse::class.java
            )

            uiThread {
                view.hideLoading()
                view.showEventDetail(data.events)
            }
        }
    }
}