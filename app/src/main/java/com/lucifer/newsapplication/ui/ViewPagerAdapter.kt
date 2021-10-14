package com.lucifer.newsapplication.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.lucifer.newsapplication.R
import com.lucifer.newsapplication.models.Article
import java.text.SimpleDateFormat
import java.util.*


class ViewPagerAdapter(
    private val context: Context,
    private val list: List<Article>,
    private val verticalViewPager: VerticalViewPager
) : PagerAdapter() {

    private var mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var newPosition: Int = 0
    private var x1: Float = 0F
    private var x2: Float = 0F

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View = mLayoutInflater.inflate(R.layout.item_container, container, false)
        val imageView: ImageView = itemView.findViewById(R.id.imageView) as ImageView
        val headline: TextView = itemView.findViewById(R.id.headline) as TextView
        val desc: TextView = itemView.findViewById(R.id.desc) as TextView
        val date: TextView = itemView.findViewById(R.id.date) as TextView

        headline.text = list[position].title
        desc.text = list[position].description

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        parser.timeZone = TimeZone.getTimeZone("IST")
        val zoneDt = parser.parse(list[position].publishedAt)
        val created = parser.format(zoneDt)

        val formatter = SimpleDateFormat("dd-MM-yyyy KK:mm aaa")
        val output: String = formatter.format(parser.parse(created))
        date.text = output

        Glide.with(context)
            .load(list[position].urlToImage)
            .centerCrop()
            .into(imageView)

        verticalViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                newPosition = position

            }

        })

        verticalViewPager.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
                when (motionEvent!!.action)
                {
                    MotionEvent.ACTION_DOWN -> { x1 = motionEvent.x
                    }
                    MotionEvent.ACTION_UP -> {x2 = motionEvent.x
                        val deltaX = x1-x2
                        if (deltaX > 300){
                            val intent = Intent(context, NewsDetailActivity::class.java)
                            if (position == 1){
                                intent.putExtra("url", list[0].url)
                                context.startActivity(intent)
                            }else{
                                intent.putExtra("url", list[newPosition].url)
                                context.startActivity(intent)
                            }
                        }
                    }
                }
                return false
            }
        })

        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}