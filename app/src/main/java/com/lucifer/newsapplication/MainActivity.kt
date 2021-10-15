package com.lucifer.newsapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.lucifer.newsapplication.background.AlarmReceiver
import com.lucifer.newsapplication.ui.VerticalViewPager
import com.lucifer.newsapplication.ui.ViewPagerAdapter
import com.lucifer.newsapplication.utils.Coroutines
import com.lucifer.newsapplication.viewmodel.NewsViewModel
import dagger.android.AndroidInjection
import kotlinx.coroutines.DelicateCoroutinesApi
import javax.inject.Inject


@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    @Inject lateinit var newsViewModel: NewsViewModel

    private lateinit var verticalViewPager: VerticalViewPager
    private lateinit var pgBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // it will prepare broadcast receiver.
        val alarm = Intent(this, AlarmReceiver::class.java)
        val alarmRunning =
            PendingIntent.getBroadcast(this, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null
        if (!alarmRunning) {
            val pendingIntent = PendingIntent.getBroadcast(this, 0, alarm, 0)
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                300000, // 5 min in milliseconds
                pendingIntent
            )
        }

        verticalViewPager = findViewById(R.id.verticalViewPager)
        pgBar = findViewById(R.id.pg_bar)

        bindUI()
    }

    private fun bindUI() = Coroutines.main {
        pgBar.visibility = View.VISIBLE
        newsViewModel.news.await().observe(this, Observer {
            pgBar.visibility = View.GONE
            Log.d("NEWSDATA", it.toString())
            //Toast.makeText(this@MainActivity, it.size.toString(), Toast.LENGTH_SHORT).show()
            verticalViewPager.adapter = ViewPagerAdapter(this, it, verticalViewPager)

        })
    }
}