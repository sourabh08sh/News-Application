package com.lucifer.newsapplication.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs


class VerticalViewPager : ViewPager {

    companion object {
        private const val MIN_SCALE = 0.75f
    }

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }

    private fun init() {
        // The majority of the magic happens here
        setPageTransformer(true, VerticalPageTransformer())
        // The easiest way to get rid of the over scroll drawing that happens on the left and right
        overScrollMode = OVER_SCROLL_NEVER
    }

    private inner class VerticalPageTransformer : PageTransformer {
        override fun transformPage(view: View, position: Float) {
            // In this we are checking positions....
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.alpha = 0F
                }
                position <= 0 -> { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    view.alpha = 1F
                    // Counteract the default slide transition
                    view.translationX = view.width * -position

                    //set Y position to swipe in from top
                    val yPosition: Float = position * view.height
                    view.translationY = yPosition
                    view.scaleX = 1F
                    view.scaleY = 1F
                }
                position <= 1 -> { // [0,1]
                    view.alpha = 1F

                    // Counteract the default slide transition
                    view.translationX = view.width * -position


                    // Scale the page down (between MIN_SCALE and 1)
                    val scaleFactor = (0.75f + (1 - 0.75f) * (1 - abs(position)))
                    view.scaleX = scaleFactor
                    view.scaleY = scaleFactor
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.alpha = 0F
                }
            }
        }
    }

    private fun swapXY(ev: MotionEvent): MotionEvent {
        // swap x and y coordinates
        val width = width.toFloat()
        val height = height.toFloat()
        val newX = ev.y / height * width
        val newY = ev.x / width * height
        ev.setLocation(newX, newY)
        return ev
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val intercepted = super.onInterceptTouchEvent(swapXY(ev))
        swapXY(ev) // return touch coordinates
        return intercepted
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return super.onTouchEvent(swapXY(ev))
    }
}