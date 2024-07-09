package com.example.cineconnect.view.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.cineconnect.R
import com.example.cineconnect.databinding.ActivityMainBinding
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.view.fragment.mainFragment.DiscoverFragment
import com.example.cineconnect.view.fragment.mainFragment.GuessProfileFragment
import com.example.cineconnect.view.fragment.mainFragment.HomeFragment
import com.example.cineconnect.view.fragment.mainFragment.ProfileFragment
import com.example.cineconnect.view.fragment.mainFragment.SearchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var activeFragment: Fragment
    private val homeFragment = HomeFragment()
    private val discoverFragment = DiscoverFragment()
    private val searchFragment = SearchFragment()
    private val profileFragment = ProfileFragment()
    private val guessProfileFragment = GuessProfileFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(activityMainBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.paddingBottom)
            insets
        }


        val bottomNavigationView = activityMainBinding.bottomNavigationView

        if (SessionManager.getToken(this) == null) {
            activityMainBinding.homeFragment.visibility = View.GONE
            activityMainBinding.discoverFragment.visibility = View.VISIBLE
            activityMainBinding.profileFragment.visibility = View.GONE
            bottomNavigationView.menu.clear()
            bottomNavigationView.inflateMenu(R.menu.guess_menu)
            activeFragment = DiscoverFragment()

            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {

                    R.id.fragmentDiscover -> {
                        activityMainBinding.homeFragment.visibility = View.GONE
                        activityMainBinding.discoverFragment.visibility = View.VISIBLE
                        activityMainBinding.searchFragment.visibility = View.GONE
                        activityMainBinding.guessProfileFragment.visibility = View.GONE

                        activeFragment = discoverFragment

                        true
                    }

                    R.id.fragmentSearch -> {
                        activityMainBinding.homeFragment.visibility = View.GONE
                        activityMainBinding.discoverFragment.visibility = View.GONE
                        activityMainBinding.searchFragment.visibility = View.VISIBLE
                        activityMainBinding.guessProfileFragment.visibility = View.GONE

                        activeFragment = searchFragment
                        true
                    }

                    R.id.fragmentProfile -> {
                        activityMainBinding.homeFragment.visibility = View.GONE
                        activityMainBinding.discoverFragment.visibility = View.GONE
                        activityMainBinding.searchFragment.visibility = View.GONE
                        activityMainBinding.guessProfileFragment.visibility = View.VISIBLE

                        activeFragment = guessProfileFragment
                        true
                    }

                    else -> false
                }
            }
        } else {
            activeFragment = HomeFragment()
            activityMainBinding.guessProfileFragment.visibility = View.GONE
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

    fun nearhs(view: View?) {
    }
}