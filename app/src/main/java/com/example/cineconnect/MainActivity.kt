package com.example.cineconnect

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.cineconnect.databinding.ActivityMainBinding
import com.example.cineconnect.fragment.mainFragment.DiscoverFragment
import com.example.cineconnect.fragment.mainFragment.HomeFragment
import com.example.cineconnect.fragment.mainFragment.ProfileFragment
import com.example.cineconnect.fragment.mainFragment.SearchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var activeFragment: Fragment
    private val homeFragment = HomeFragment()
    private val discoverFragment = DiscoverFragment()
    private val searchFragment = SearchFragment()
    private val profileFragment = ProfileFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(activityMainBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        activeFragment = HomeFragment()


        val bottomNavigationView = activityMainBinding.bottomNavigationView

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragmentHome -> {
                    activityMainBinding.homeFragment.visibility = View.VISIBLE
                    activityMainBinding.discoverFragment.visibility = View.GONE
                    activityMainBinding.searchFragment.visibility = View.GONE
                    activityMainBinding.profileFragment.visibility = View.GONE

                    activeFragment = homeFragment
                    true
                }

                R.id.fragmentDiscover -> {
                    activityMainBinding.homeFragment.visibility = View.GONE
                    activityMainBinding.discoverFragment.visibility = View.VISIBLE
                    activityMainBinding.searchFragment.visibility = View.GONE
                    activityMainBinding.profileFragment.visibility = View.GONE

                    activeFragment = discoverFragment

                    true
                }

                R.id.fragmentSearch -> {
                    activityMainBinding.homeFragment.visibility = View.GONE
                    activityMainBinding.discoverFragment.visibility = View.GONE
                    activityMainBinding.searchFragment.visibility = View.VISIBLE
                    activityMainBinding.profileFragment.visibility = View.GONE

                    activeFragment = searchFragment
                    true
                }

                R.id.fragmentProfile -> {
                    activityMainBinding.homeFragment.visibility = View.GONE
                    activityMainBinding.discoverFragment.visibility = View.GONE
                    activityMainBinding.searchFragment.visibility = View.GONE
                    activityMainBinding.profileFragment.visibility = View.VISIBLE

                    activeFragment = profileFragment
                    true
                }

                else -> false
            }
        }
    }
}