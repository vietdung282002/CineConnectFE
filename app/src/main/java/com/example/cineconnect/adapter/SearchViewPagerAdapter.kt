package com.example.cineconnect.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cineconnect.fragment.search.MovieSearchFragment
import com.example.cineconnect.fragment.search.PeopleSearchFragment
import com.example.cineconnect.fragment.search.ReviewSearchFragment
import com.example.cineconnect.fragment.search.UserSearchFragment

class SearchViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val query: String,
    private val parentIds: Int
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MovieSearchFragment(query, parentIds)
            1 -> PeopleSearchFragment()
            2 -> UserSearchFragment()
            else -> ReviewSearchFragment()
        }
    }
}