package com.singularitycoder.shareme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.singularitycoder.shareme.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

// The basic idea is time sharing
// Estimate the worth of a person using hourly wage
// This is meant to be striclty time sharing and no money must be involved although u cant cotrol that anyway
// Technically this should be similar to torrent seeding or socket comm
// Since time is too vague, it will be something like "In 5 min i can achieve X". For 30 mins, 1 hr, 3 hr, 6 hr, 12 hr - This is how u set time goals
// Its like how much of him/her can u afford?
// No need of chat n stuff - direct them to sms and whatsapp
// Get lat long for address and show in google maps
// To verify if the hourly wage is accurate user must add Linkedin profile  and any other website link
// First time user opens app he fills the profile
// Thsi probably makees sense to be a D-App hosted on blockchain
// Mayeb create an online docu-sign contract and only then accept offers


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val tabNamesList = listOf(
        Tab.SHARE.value,
        Tab.SHARED.value,
        Tab.SAVED.value,
    )

    private val viewPager2PageChangeListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            println("viewpager2: onPageScrollStateChanged")
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            println("viewpager2: onPageSelected")
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            println("viewpager2: onPageScrolled")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewPager()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewpagerHome.unregisterOnPageChangeCallback(viewPager2PageChangeListener)
    }

    private fun setUpViewPager() {
        binding.viewpagerHome.apply {
            adapter = HomeViewPagerAdapter(fragmentManager = supportFragmentManager, lifecycle = lifecycle)
            registerOnPageChangeCallback(viewPager2PageChangeListener)
        }
        binding.tabLayoutHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) = Unit
            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
        TabLayoutMediator(binding.tabLayoutHome, binding.viewpagerHome) { tab, position ->
            tab.text = tabNamesList[position]
        }.attach()
    }

    inner class HomeViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int = tabNamesList.size
        override fun createFragment(position: Int): Fragment = ShareFragment.newInstance(shareState = tabNamesList[position])
    }
}