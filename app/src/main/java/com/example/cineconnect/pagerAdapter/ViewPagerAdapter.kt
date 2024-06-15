package com.example.cineconnect.pagerAdapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cineconnect.fragment.CastListFragment
import com.example.cineconnect.fragment.GenresListFragment
import com.example.cineconnect.model.CastList
import com.example.cineconnect.model.Genre
import com.example.cineconnect.utils.Utils

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val castList: List<CastList>,
    private val genresList: List<Genre>,
    private val parentIds: Int
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putInt(Utils.CONTAINER_ID, parentIds)

        val newCastList = ArrayList(castList)
        val newGenresList = ArrayList(genresList)

        val castListFragment = CastListFragment.newInstance(parentIds, newCastList)
        val genresListFragment = GenresListFragment.newInstance(parentIds, newGenresList)
        return when (position) {
            0 -> castListFragment
            else -> genresListFragment
        }
    }
}