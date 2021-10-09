package com.application.brainnforce.ui.fragments.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.brainnforce.R
import com.application.brainnforce.base.adapter.RecyclerViewGenricAdapter
import com.application.brainnforce.databinding.*
import com.application.brainnforce.model.UserDetail
import com.application.brainnforce.ui.fragments.home.DashboardFragment
import com.application.swapp.app.BNFBaseFragment

class OrderListFragment(var orderStatus: String, var orderFrag: Int) :
    BNFBaseFragment<FragOrderListBinding>() {

    private var mAdapter: RecyclerViewGenricAdapter<UserDetail, OrderItemListBinding>? =
        null

    private var list: ArrayList<UserDetail> = ArrayList()


    override fun configureToolbar(): Any? {
        return null
    }

    override fun isToolbarVisible(): Boolean {
        return false
    }

    override fun isBottomBarVisible(): Boolean {
        return orderFrag != 1
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (orderFrag == 1) {
            viewDataBinding.mToolbar.visibility = View.VISIBLE
        } else {
            viewDataBinding.mToolbar.visibility = View.GONE
        }

        if (orderStatus == "active") {
            viewDataBinding.toolbarTitle.text = getString(R.string.active_work_orders)
        } else if (orderStatus == "past") {
            viewDataBinding.toolbarTitle.text = getString(R.string.complete_work_orders)
        } else {
            viewDataBinding.toolbarTitle.text = getString(R.string.disputed_work_orders)
        }

        viewDataBinding.backIcon.setOnClickListener {
            displayIt(DashboardFragment(), DashboardFragment::class.java.canonicalName, false)
        }


        list = ArrayList()
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())

        mAdapter = RecyclerViewGenricAdapter(list, R.layout.order_item_list)
        { binder, model, position, itemView ->

            if (orderStatus == "active") {

                binder.dateText.visibility = View.VISIBLE
                binder.rateText.visibility = View.GONE
                binder.rateText1.visibility = View.GONE
                binder.numText.visibility = View.VISIBLE
                binder.favText.visibility = View.GONE
            }
            else if (orderStatus == "past") {
                binder.dateText.visibility = View.GONE
                binder.rateText.visibility = View.VISIBLE
                binder.rateText1.visibility = View.VISIBLE
                binder.numText.visibility = View.VISIBLE
                binder.favText.visibility = View.GONE
            }
            else {
                binder.dateText.visibility = View.VISIBLE
                binder.rateText.visibility = View.GONE
                binder.rateText1.visibility = View.GONE
                binder.numText.visibility = View.GONE
                binder.favText.visibility = View.VISIBLE
            }

            binder.mainLayout.setOnClickListener {

                displayIt(
                    DisputeOrderFragment(
                        orderStatus,
                        orderFrag
                    ),
                    DisputeOrderFragment::class.java.canonicalName,
                    true
                )


            }


        }

        val activeLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        viewDataBinding.recyclerView.layoutManager = activeLayoutManager
        viewDataBinding.recyclerView.adapter = mAdapter


    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_order_list
    }

}

