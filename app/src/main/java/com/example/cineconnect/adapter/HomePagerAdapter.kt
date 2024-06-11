package com.example.cineconnect.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cineconnect.fragment.detailFragment.FollowingHomeFragment
import com.example.cineconnect.fragment.detailFragment.RecommendHomeFragment

class HomePagerAdapter(
    fragmentActivity: FragmentActivity,
    private val parentIds: Int
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RecommendHomeFragment(parentIds)
            else -> FollowingHomeFragment(parentIds)
        }
    }
}