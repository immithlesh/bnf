package com.application.brainnforce.ui.fragments.home

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
import com.application.brainnforce.ui.fragments.auth.LoginFragment
import com.application.swapp.app.BNFBaseFragment


class TotalEarningFragment : BNFBaseFragment<FragTotalEarningBinding>() {

    private var mAdapter: RecyclerViewGenricAdapter<UserDetail, TotalItemListBinding>? =
        null
    private var list: ArrayList<UserDetail> = ArrayList()

    override fun configureToolbar(): Any? {
        return null
    }

    override fun isToolbarVisible(): Boolean {
        return false
    }

    override fun isBottomBarVisible(): Boolean {
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewDataBinding.backIcon.setOnClickListener {
            displayIt(DashboardFragment(), DashboardFragment::class.java.canonicalName, false)
        }
        list = ArrayList()
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())


        mAdapter = RecyclerViewGenricAdapter(list, R.layout.total_item_list)
        { binder, model, position, itemView ->


        }


        val mLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        viewDataBinding.marchRecyclerView.layoutManager = mLayoutManager
        viewDataBinding.marchRecyclerView.adapter = mAdapter

        val mLayoutManagers = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        viewDataBinding.aprilRecyclerView.layoutManager = mLayoutManagers
        viewDataBinding.aprilRecyclerView.adapter = mAdapter


    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_total_earning
    }

}
