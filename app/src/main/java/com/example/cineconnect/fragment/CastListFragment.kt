package com.example.cineconnect.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cineconnect.R
import com.example.cineconnect.adapter.CastListAdapter
import com.example.cineconnect.databinding.FragmentCastListBinding
import com.example.cineconnect.fragment.detailFragment.PersonDetailFragment
import com.example.cineconnect.model.CastList
import com.example.cineconnect.onClickInterface.OnPersonClicked
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.utils.Utils.Companion.ARG_LIST
import com.example.cineconnect.utils.Utils.Companion.PERSON_ID

class CastListFragment() :
    Fragment(), OnPersonClicked {
    private lateinit var fragmentCastListBinding: FragmentCastListBinding
    private lateinit var castListAdapter: CastListAdapter
    private var castList: ArrayList<CastList>? = null
    private var parentId: Int = -1

    companion object {

        @JvmStatic
        fun newInstance(parentIds: Int, list: ArrayList<CastList>) =
            CastListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_LIST, list)
                    putInt(Utils.CONTAINER_ID, parentIds)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            castList = BundleCompat.getParcelableArrayList(it, ARG_LIST, CastList::class.java)
            parentId = it.getInt(Utils.CONTAINER_ID)
        }
    }
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

        castListAdapter.submitList(castList?.toList())
    }

    override fun getOnPersonClicked(position: Int, personId: Int) {
        val bundle = Bundle()
        bundle.putInt(PERSON_ID, personId)

        val personDetailFragment = PersonDetailFragment().apply {
            arguments = bundle
        }

        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .add(parentId, personDetailFragment)
            .addToBackStack(null)
            .commit()
    }

}