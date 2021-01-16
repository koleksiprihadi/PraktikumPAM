package com.krisnaprihadiyanto.tabbedaplication.api

import com.krisnaprihadiyanto.tabbedaplication.api.Constant

/**
 * Created by Avin on 04/09/2018.
 */
object TheSportDBApi {
    fun getEvent(league: String?, event: String?): String{
        return Constant.BASE_URL + "api/v1/json/${Constant.TSDB_API_KEY}" + "/"+event+".php?id="+league
    }

    fun getEventDetail(eventId: String?): String{
        return Constant.BASE_URL + "api/v1/json/${Constant.TSDB_API_KEY}" + "/lookupevent.php?id="+eventId
    }
}