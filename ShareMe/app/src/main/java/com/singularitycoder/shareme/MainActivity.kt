package com.singularitycoder.shareme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
// Maybe do a linkedin login and sync all skills


// Before accepting a time requst
// Call, Sign contract with docu-sign, start work

// Should services be offered only in hours?

// Since time is too vague
// 5 min - i can achieve x
// __Develop full website__ IN __2 hrs__
// I can acheive x in y mins
// 5 min, 30 min, 1 hr time goals

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val tabNamesList = listOf(
        Tab.HOME.value,
        Tab.TIME_REQUESTS.value,
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
        binding.setupUI()
        setUpViewPager()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewpagerHome.unregisterOnPageChangeCallback(viewPager2PageChangeListener)
    }

    private fun ActivityMainBinding.setupUI() {
        val user = Person(
            name = "Hithesh Vurjana",
            rating = 1f,
            ratingCount = 3333203,
            iCanAfford = "24 hrs",
            tempImageDrawable = R.drawable.hithesh,
            profession = "Software Engineer",
            hourlyWorth = 999.99
        )
        ivImage.setImageDrawable(drawable(user.tempImageDrawable))
        ivImage.setOnClickListener {
            PersonDetailBottomSheetFragment.newInstance(
                adapterPosition = 0,
                person = user
            ).show(supportFragmentManager, TAG_PERSON_DETAIL_MODAL_BOTTOM_SHEET)
        }
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
        override fun createFragment(position: Int): Fragment = ShareFragment.newInstance(tab = tabNamesList[position])
    }
}