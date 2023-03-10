package com.coderlytics.apexblogger.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.coderlytics.apexblogger.OnboardingViewPagerAdapter4
import com.coderlytics.apexblogger.R
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingExample4Activity : AppCompatActivity() {

    private lateinit var mViewPager: ViewPager2
    private lateinit var btnBack: Button
    private lateinit var btnNext: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_example4)
        mViewPager = findViewById(R.id.viewPager)
        mViewPager.adapter = OnboardingViewPagerAdapter4(this, this)
        mViewPager.offscreenPageLimit = 1
        btnBack = findViewById(R.id.btn_previous_step)
        btnNext = findViewById(R.id.btn_next_step)
        mViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    btnNext.text = getText(R.string.finish)
                } else {
                    btnNext.text = getText(R.string.next)
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })
        TabLayoutMediator(findViewById(R.id.pageIndicator), mViewPager) { _, _ -> }.attach()

        btnNext.setOnClickListener {
            if (getItem() > mViewPager.childCount) {
                startActivity(Intent(this
                ,LoginActivity::class.java))
                finish()
            } else {
                mViewPager.setCurrentItem(getItem() + 1, true)
            }
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this
                ,LoginActivity::class.java))
            finish()
        }
    }

    private fun getItem(): Int {
        return mViewPager.currentItem
    }
}
