package com.example.cineconnect.pagerAdapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cineconnect.fragment.detailFragment.FollowingHomeFragment
import com.example.cineconnect.fragment.detailFragment.RecommendHomeFragment
import com.example.cineconnect.utils.Utils

class HomePagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val parentIds: Int
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putInt(Utils.CONTAINER_ID, parentIds)

        RecommendHomeFragment().apply {
            arguments = bundle
        }

        FollowingHomeFragment().apply {
            arguments = bundle
        }
        return when (position) {

            0 -> RecommendHomeFragment()
            else -> FollowingHomeFragment()
        }
    }
}