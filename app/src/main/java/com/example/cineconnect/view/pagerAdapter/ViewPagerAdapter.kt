package com.example.cineconnect.view.pagerAdapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cineconnect.model.entities.CastList
import com.example.cineconnect.model.entities.Genre
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.view.fragment.detailFragment.CastListFragment
import com.example.cineconnect.view.fragment.detailFragment.GenresListFragment

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