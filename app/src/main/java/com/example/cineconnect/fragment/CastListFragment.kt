package com.example.cineconnect.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cineconnect.R
import com.example.cineconnect.adapter.CastListAdapter
import com.example.cineconnect.databinding.FragmentCastListBinding
import com.example.cineconnect.fragment.detailFragment.PersonDetailFragment
import com.example.cineconnect.model.CastList
import com.example.cineconnect.onClickInterface.OnPersonClicked
import com.example.cineconnect.utils.Utils.Companion.PERSON_ID

class CastListFragment(private val castList: List<CastList>, private val parentId: Int) :
    Fragment(), OnPersonClicked {
    private lateinit var fragmentCastListBinding: FragmentCastListBinding
    private lateinit var castListAdapter: CastListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentCastListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cast_list, container, false)

        return fragmentCastListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        castListAdapter = CastListAdapter()
        castListAdapter.setOnPersonClicked(this)
        fragmentCastListBinding.lifecycleOwner = viewLifecycleOwner

        fragmentCastListBinding.rvCastList.adapter = castListAdapter
        fragmentCastListBinding.rvCastList.setHasFixedSize(true)

        castListAdapter.submitList(castList)
    }

    override fun getOnPersonClicked(position: Int, personId: Int) {
        val bundle = Bundle()
        bundle.putInt(PERSON_ID, personId)

        val personDetailFragment = PersonDetailFragment().apply {
            arguments = bundle
        }

        val fragmentManager = requireActivity().supportFragmentManager
        if (parentId != null) {
            fragmentManager.beginTransaction()
                .add(parentId, personDetailFragment)
                .addToBackStack(null)
                .commit()
        }
    }

}