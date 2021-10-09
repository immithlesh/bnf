package com.application.brainnforce.ui.fragments.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.brainnforce.R
import com.application.brainnforce.base.adapter.RecyclerViewGenricAdapter
import com.application.brainnforce.databinding.*
import com.application.brainnforce.model.UserDetail
import com.application.brainnforce.ui.fragments.home.HomeFragment
import com.application.brainnforce.ui.fragments.profile.SellerProfileFragment
import com.application.swapp.app.BNFBaseFragment

class FavoritesFragment : BNFBaseFragment<FragFavoritesBinding>() {

    private var mAdapter: RecyclerViewGenricAdapter<UserDetail, FavoritesItemListBinding>? =
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

        viewDataBinding.favouriteIcon.setOnClickListener {
            displayIt(HomeFragment(), HomeFragment::class.java.canonicalName, false)
        }

        list = ArrayList()
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())

        mAdapter = RecyclerViewGenricAdapter(list, R.layout.favorites_item_list)
        { binder, model, position, itemView ->

            binder.mainLayout.setOnClickListener {
                displayIt(SellerProfileFragment(), SellerProfileFragment::class.java.canonicalName,true)
            }

        }


        val mLayoutManager = GridLayoutManager(activity, 2,RecyclerView.VERTICAL, false)
        viewDataBinding.recyclerView.layoutManager = mLayoutManager
        viewDataBinding.recyclerView.adapter = mAdapter


    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_favorites
    }

}
