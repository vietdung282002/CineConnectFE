package com.example.cineconnect.pagerAdapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cineconnect.fragment.search.MovieSearchFragment
import com.example.cineconnect.fragment.search.PeopleSearchFragment
import com.example.cineconnect.fragment.search.ReviewSearchFragment
import com.example.cineconnect.fragment.search.UserSearchFragment
import com.example.cineconnect.utils.Utils

class SearchViewPagerAdapter(
    fragmentManager: FragmentManager,
//    fragmentActivity: FragmentActivity,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    var query: String = ""
    private var parentIds: Int = -1

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putInt(Utils.CONTAINER_ID, parentIds)
        bundle.putString(Utils.QUERY, query)
        val movieSearchFragment = MovieSearchFragment().apply {
            arguments = bundle
        }
        val peopleSearchFragment = PeopleSearchFragment().apply {
            arguments = bundle
        }
        val userSearchFragment = UserSearchFragment().apply {
            arguments = bundle
        }
        val reviewSearchFragment = ReviewSearchFragment().apply {
            arguments = bundle
        }
        return when (position) {
            0 -> movieSearchFragment
            1 -> peopleSearchFragment
            2 -> userSearchFragment
            3 -> reviewSearchFragment
            else -> throw IllegalArgumentException("Invalid position: $position")
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