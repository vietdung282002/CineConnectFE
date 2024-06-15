package com.example.cineconnect.fragment.mainFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentHomeBinding
import com.example.cineconnect.pagerAdapter.HomePagerAdapter


class HomeFragment : Fragment() {
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var homePagerAdapter: HomePagerAdapter
    private var container = -1
    private lateinit var viewPager: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        container = (view.parent as ViewGroup).id
        viewPager = fragmentHomeBinding.viewPager
        homePagerAdapter =
            activity?.let { HomePagerAdapter(childFragmentManager, lifecycle, container) }!!
        viewPager.adapter = homePagerAdapter
        val tabBar = fragmentHomeBinding.tabBar
        tabBar.attachTo(viewPager)
    }

}