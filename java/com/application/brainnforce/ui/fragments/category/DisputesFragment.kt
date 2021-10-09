package com.application.brainnforce.ui.fragments.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.brainnforce.R
import com.application.brainnforce.base.adapter.RecyclerViewGenricAdapter
import com.application.brainnforce.databinding.*
import com.application.brainnforce.model.UserDetail
import com.application.swapp.app.BNFBaseFragment


class DisputesFragment : BNFBaseFragment<FragDisputesBinding>() {

    private var mAdapter: RecyclerViewGenricAdapter<UserDetail, DisputesItemListBinding>? =
        null
    private var list: ArrayList<UserDetail> = ArrayList()

    override fun configureToolbar(): Any? {
        return null
    }

    override fun isToolbarVisible(): Boolean {
        return false
    }

    override fun isBottomBarVisible(): Boolean {
        return  false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewDataBinding.logoIcon.setOnClickListener {
            getContainerActivity().onBackPressed()
        }

        list = ArrayList()
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())



        mAdapter = RecyclerViewGenricAdapter(list, R.layout.disputes_item_list)
        { binder, model, position, itemView ->

            binder.mainLayout.setOnClickListener {
                displayIt(DisputedDetailListFragment(), DisputedDetailListFragment::class.java.canonicalName, true)
            }

        }


        val mLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        viewDataBinding.recyclerView.layoutManager = mLayoutManager
        viewDataBinding.recyclerView.adapter = mAdapter


    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_disputes
    }

}
