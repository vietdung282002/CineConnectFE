package com.example.cineconnect.fragment.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cineconnect.R
import com.example.cineconnect.adapter.PersonSearchListAdapter
import com.example.cineconnect.databinding.FragmentPeopleSearchBinding
import com.example.cineconnect.model.PeopleListResponse
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.onClickInterface.OnPersonClicked
import com.example.cineconnect.viewmodel.PersonViewModel

class PeopleSearchFragment(
    private val query: String,
    private val parentId: Int) : Fragment(),OnPersonClicked {

    private lateinit var fragmentPeopleSearchBinding: FragmentPeopleSearchBinding
    private lateinit var personSearchListAdapter: PersonSearchListAdapter
    private val personViewModel: PersonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentPeopleSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_people_search, container, false)
        personViewModel.getSearchPerson(1,query)
        return fragmentPeopleSearchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personSearchListAdapter = PersonSearchListAdapter()
        personSearchListAdapter.setOnPersonClicked(this)
        fragmentPeopleSearchBinding.rvPerson.adapter = personSearchListAdapter

        personViewModel.personListResult.observe(viewLifecycleOwner){
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    updateUI(it.data)
                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                    stopLoading()
                }

                else -> {
                    stopLoading()
                }
            }
        }
    }

    private fun updateUI(response: PeopleListResponse?) {
        personSearchListAdapter.submitList(response?.personLists)
    }

    override fun getOnPersonClicked(position: Int, movieId: Int) {
        TODO("Not yet implemented")
    }

    private fun showLoading() {
        fragmentPeopleSearchBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentPeopleSearchBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


}