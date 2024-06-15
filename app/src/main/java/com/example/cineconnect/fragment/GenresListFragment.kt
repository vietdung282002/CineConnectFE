package com.example.cineconnect.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cineconnect.R
import com.example.cineconnect.adapter.GenreListAdapter
import com.example.cineconnect.databinding.FragmentGenresListBinding
import com.example.cineconnect.fragment.detailFragment.MovieListFragment
import com.example.cineconnect.model.Genre
import com.example.cineconnect.onClickInterface.OnGenreClicked
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.utils.Utils.Companion.GENRE_ID
import com.example.cineconnect.utils.Utils.Companion.TITLE
import com.example.cineconnect.utils.Utils.Companion.TYPE

class GenresListFragment() :
    Fragment(), OnGenreClicked {
    private lateinit var fragmentGenresListBinding: FragmentGenresListBinding
    private lateinit var genreListAdapters: GenreListAdapter
    private var genresList: ArrayList<Genre>? = null
    private var parentId: Int = -1

    companion object {

        @JvmStatic
        fun newInstance(parentIds: Int, list: ArrayList<Genre>) =
            GenresListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(Utils.ARG_LIST, list)
                    putInt(Utils.CONTAINER_ID, parentIds)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentGenresListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_genres_list, container, false)
        arguments?.let {
            genresList = BundleCompat.getParcelableArrayList(it, Utils.ARG_LIST, Genre::class.java)
            parentId = it.getInt(Utils.CONTAINER_ID)
        }
        return fragmentGenresListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        genreListAdapters = GenreListAdapter()
        genreListAdapters.setOnGenreClicked(this)
        fragmentGenresListBinding.lifecycleOwner = viewLifecycleOwner

        fragmentGenresListBinding.rvGenresList.adapter = genreListAdapters
        fragmentGenresListBinding.rvGenresList.setHasFixedSize(true)

        genreListAdapters.submitList(genresList?.toList())
    }

    override fun getOnGenreClicked(position: Int, genreId: Int, genreName: String) {
        val bundle = Bundle()
        bundle.putInt(GENRE_ID, genreId)
        bundle.putString(TITLE, genreName)
        bundle.putInt(TYPE, 1)

        val genreDetailFragment = MovieListFragment().apply {
            arguments = bundle
        }

        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .add(parentId, genreDetailFragment)
            .addToBackStack(null)
            .commit()
    }


}