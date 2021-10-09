package com.application.brainnforce.ui.fragments.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.brainnforce.R
import com.application.brainnforce.base.adapter.RecyclerViewGenricAdapter
import com.application.brainnforce.databinding.*
import com.application.brainnforce.model.UserDetail
import com.application.swapp.app.BNFBaseFragment

class NotificationFragment : BNFBaseFragment<FragInboxBinding>() {

    private var mAdapter: RecyclerViewGenricAdapter<UserDetail, InboxListBinding>? =
        null
    private var list: ArrayList<UserDetail> = ArrayList()

    override fun configureToolbar(): Any? {
        return null
    }

    override fun isToolbarVisible(): Boolean {
        return false
    }

    override fun isBottomBarVisible(): Boolean {
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.searchCardView.visibility = View.GONE
        viewDataBinding.toolbarTitle.text = getString(R.string.notification)

        list = ArrayList()
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())

        mAdapter = RecyclerViewGenricAdapter(list, R.layout.inbox_list)
        { binder, model, position, itemView ->

            binder.seenIcon.visibility = View.GONE

        }


        val mLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        viewDataBinding.recyclerView.layoutManager = mLayoutManager
        viewDataBinding.recyclerView.adapter = mAdapter


    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_inbox
    }

}
