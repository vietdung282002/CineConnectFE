package com.example.cineconnect.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.cineconnect.utils.DataSource
import com.example.cineconnect.R
import com.example.cineconnect.adapter.GenreListAdapter
import com.example.cineconnect.databinding.FragmentGenresListBinding
import com.example.cineconnect.model.Genre
import com.example.cineconnect.onClickInterface.OnGenreClicked
import com.example.cineconnect.utils.Utils.Companion.GENRE_ID
import com.example.cineconnect.utils.Utils.Companion.GENRE_NAME
import com.example.cineconnect.utils.Utils.Companion.LOG_TAG

class GenresListFragment(private val genresList: List<Genre>, private  val parentId: Int) : Fragment(),OnGenreClicked {
    private lateinit var fragmentGenresListBinding: FragmentGenresListBinding
    private lateinit var genreListAdapters: GenreListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentGenresListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_genres_list, container, false)
        return fragmentGenresListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataset = DataSource().loadGenre()

        genreListAdapters = GenreListAdapter()
        genreListAdapters.setOnGenreClicked(this)
        fragmentGenresListBinding.lifecycleOwner = viewLifecycleOwner

        fragmentGenresListBinding.rvGenresList.adapter = genreListAdapters
        fragmentGenresListBinding.rvGenresList.setHasFixedSize(true)

        genreListAdapters.submitList(genresList)
    }

    override fun getOnGenreClicked(position: Int, genreId: Int, genreName: String) {
        val bundle = Bundle()
        bundle.putInt(GENRE_ID, genreId)
        bundle.putString(GENRE_NAME,genreName)

        val genreDetailFragment = GenreDetailFragment().apply {
            arguments = bundle
        }

        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .add(parentId, genreDetailFragment)
            .addToBackStack(null)
            .commit()
    }


}