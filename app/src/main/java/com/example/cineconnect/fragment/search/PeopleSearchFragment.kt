package com.example.cineconnect.fragment.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.cineconnect.R
import com.example.cineconnect.adapter.PersonPagingAdapter
import com.example.cineconnect.databinding.FragmentPeopleSearchBinding
import com.example.cineconnect.fragment.detailFragment.PersonDetailFragment
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.onClickInterface.OnPersonClicked
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.PersonViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PeopleSearchFragment(
    private val query: String,
    private val parentId: Int
) : Fragment(), OnPersonClicked {
    private lateinit var fragmentPeopleSearchBinding: FragmentPeopleSearchBinding
    private val personAdapter = PersonPagingAdapter()
    private val personViewModel: PersonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentPeopleSearchBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_people_search, container, false)
        personViewModel.searchPeople(query)
        return fragmentPeopleSearchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personAdapter.setOnPersonClicked(this)
        fragmentPeopleSearchBinding.rvPerson.adapter = personAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            personViewModel.peopleState.collectLatest { state ->
                when (state) {
                    is BaseResponse.Loading -> {
                        showLoading()
                    }

                    is BaseResponse.Success -> {
                        stopLoading()
                        state.data?.let { pagingData ->
                            personAdapter.submitData(pagingData)
                        }
                    }

                    is BaseResponse.Error -> {
                        stopLoading()
                        processError(state.msg)
                    }
                }
            }
        }


    }

    override fun getOnPersonClicked(position: Int, personId: Int) {
        hideKeyboard()
        val bundle = Bundle()
        bundle.putInt(Utils.PERSON_ID, personId)

        val personDetailFragment = PersonDetailFragment().apply {
            arguments = bundle
        }

        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .add(parentId, personDetailFragment)
            .addToBackStack(null)
            .commit()

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

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }


}