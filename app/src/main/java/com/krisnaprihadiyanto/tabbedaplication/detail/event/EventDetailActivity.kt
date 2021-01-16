package com.krisnaprihadiyanto.tabbedaplication.detail.event


import android.os.Bundle
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.krisnaprihadiyanto.tabbedaplication.R
import com.krisnaprihadiyanto.tabbedaplication.api.ApiRepository
import com.krisnaprihadiyanto.tabbedaplication.model.EventDetail
import com.krisnaprihadiyanto.tabbedaplication.util.invisible
import com.krisnaprihadiyanto.tabbedaplication.util.visible
import com.squareup.picasso.Picasso
import okhttp3.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.onRefresh
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import
import kotlinx.android.synthetic.main.activity_event_detail.*


class EventDetailActivity : AppCompatActivity(), EventDetailView {
    private lateinit var presenter: EventDetailPresenter
    private lateinit var idEvent: String
    private var idHome: String = ""
    private var idAway: String = ""
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        supportActionBar?.title = "Match Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progressBar = find(R.id.progress_bar_detail)
        swipeRefresh = find(R.id.swipe_refresh_detail)

        val intent = intent
        idEvent = intent.getStringExtra("id")
        idHome = intent.getStringExtra("idhome")
        idAway = intent.getStringExtra("idaway")

        val request = ApiRepository()
        val gson = Gson()
        presenter = EventDetailPresenter(this, request, gson)
        presenter.getEventDetail(idEvent)
        swipeRefresh.onRefresh {
            presenter.getEventDetail(idEvent)
        }
        val logo = arrayOf(idHome, idAway)
        getBadge(logo)
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showEventDetail(data: List<EventDetail>) {
        swipeRefresh.isRefreshing = false
        val tanggal = SimpleDateFormat("EEE, d MMM yyyy")
            .format(toGMTFormat(data[0].eventDate, data[0].eventTime))
        val waktu = SimpleDateFormat("HH:mm")
            .format(toGMTFormat(data[0].eventDate, data[0].eventTime))
        tv_date_detail.text = tanggal
        tv_time_detail.text = waktu
        tv_home_detail.text = data[0].teamHome
        tv_away_detail.text = data[0].teamAway
        if(data[0].scoreHome.isNullOrEmpty() && data[0].scoreAway.isNullOrEmpty()){
            tv_skor_detail.text = "0 vs 0"
        }else{
            tv_skor_detail.text = data[0].scoreHome+"  vs  "+data[0].scoreAway
        }
        tv_home_formation.text = data[0].formationHome
        tv_away_formation.text = data[0].formationAway
        tv_home_goals.text = data[0].goalHome?.replace(";", "\n")
        tv_away_goals.text = data[0].goalAway?.replace(";", "\n")
        tv_home_shots.text = data[0].shotHome
        tv_away_shots.text = data[0].shotAway
        tv_home_gk.text = biarRapi(data[0].gkHome)
        tv_away_gk.text = biarRapi(data[0].gkAway)
        tv_home_def.text = biarRapi(data[0].defHome)
        tv_away_def.text = biarRapi(data[0].defAway)
        tv_home_mid.text = biarRapi(data[0].midHome)
        tv_away_mid.text = biarRapi(data[0].midAway)
        tv_home_forward.text = biarRapi(data[0].forwHome)
        tv_away_forward.text = biarRapi(data[0].forwAway)
    }

    private fun getBadge(logo: Array<String>) {
        for(i in 0..1){
            val request = Request.Builder()
                .url("https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id="+logo[i])
                .build()
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    var a = response.body()?.string()
                    runOnUiThread {
                        run(){
                            var json = JSONObject(a)
                            var badge = json.getJSONArray("teams").getJSONObject(0).getString("strTeamBadge")
                            if(i == 0) {
                                Picasso.with(ctx).load(badge).into(img_home)
                            }else{
                                Picasso.with(ctx).load(badge).into(img_away)
                            }
                        }
                    }
                }
            })
        }
    }

    private fun toGMTFormat(date: String?, time: String?): Date? {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val dateTime = "$date $time"
        return formatter.parse(dateTime)
    }

    //merapikan string (mengganti <semicolon> dengan <enter>)
    private fun biarRapi(pemain: String?): String? {
        return pemain?.replace("; ", "\n")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

private fun SwipeRefreshLayout.onRefresh(l: () -> Unit) {

}
