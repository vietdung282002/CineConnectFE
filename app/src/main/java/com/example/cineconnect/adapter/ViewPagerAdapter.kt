package com.example.cineconnect.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cineconnect.fragment.CastListFragment
import com.example.cineconnect.fragment.GenresListFragment
import com.example.cineconnect.model.CastList
import com.example.cineconnect.model.Genre

class ViewPagerAdapter (
    fragmentActivity: FragmentActivity,
    private val castList: List<CastList>,
    private val genresList: List<Genre>,
    private val parentIds: Int
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CastListFragment(castList,parentIds)
            else -> GenresListFragment(genresList,parentIds)
        }
    }
}