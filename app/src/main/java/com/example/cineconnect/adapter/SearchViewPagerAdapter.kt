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
) : FragmentStateAdapter(fragmentActivity) {

    var query: String = ""
    var parentIds: Int = -1

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MovieSearchFragment(query, parentIds)
            1 -> PeopleSearchFragment(query, parentIds)
            2 -> UserSearchFragment()
            else -> ReviewSearchFragment()
        }
    }

    fun updateQuery(newQuery: String, newParentIds: Int) {
        query = newQuery
        parentIds = newParentIds
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        // Return a unique ID for each fragment based on position and data
        return (position + query.hashCode() + parentIds).toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        // Check if the item ID matches the current data
        return (itemId - query.hashCode() - parentIds).toInt() in 0 until itemCount
    }
}