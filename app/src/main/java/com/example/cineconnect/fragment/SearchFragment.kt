package com.example.cineconnect.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.cineconnect.R
import com.example.cineconnect.adapter.SearchViewPagerAdapter
import com.example.cineconnect.databinding.FragmentSearchBinding
import com.example.cineconnect.viewmodel.MovieViewModel


class SearchFragment : Fragment() {
    private lateinit var fragmentSearchBinding: FragmentSearchBinding
    private lateinit var searchViewPagerAdapter: SearchViewPagerAdapter
    private val movieViewModel: MovieViewModel by viewModels()
    private var containerId = -1
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentSearchBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return fragmentSearchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        containerId = (view.parent as? ViewGroup)?.id!!

        fragmentSearchBinding.cancelBtn.setOnClickListener {
            hideKeyboard()
            fragmentSearchBinding.searchTextInput.apply {
                clearFocus()
                text?.clear()
            }
        }

        fragmentSearchBinding.apply {
            viewModel = movieViewModel
            searchTextInput.onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        cancelBtn.visibility = View.VISIBLE
                        tabBar.visibility = View.VISIBLE
                        viewPager.visibility = View.VISIBLE
                        movieViewModel.searchQuery.observe(viewLifecycleOwner) {
                            updateAdapter(it)
                        }
                    } else {
                        cancelBtn.visibility = View.GONE
                        tabBar.visibility = View.GONE
                        viewPager.visibility = View.GONE
                    }
                }
        }
        viewPager = fragmentSearchBinding.viewPager
        searchViewPagerAdapter = activity?.let { SearchViewPagerAdapter(it) }!!
        viewPager.adapter = searchViewPagerAdapter
        val tabBar = fragmentSearchBinding.tabBar
        tabBar.attachTo(viewPager)
    }

    private fun updateAdapter(query: String) {

        searchViewPagerAdapter.updateQuery(query, containerId)

    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }


}