@file:Suppress("DEPRECATION")

package com.application.brainnforce.ui.fragments.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import com.application.brainnforce.R
import com.application.brainnforce.databinding.FragMyOrderBinding
import com.application.swapp.app.BNFBaseFragment

class MyOrderFragment : BNFBaseFragment<FragMyOrderBinding>() {
    override fun configureToolbar(): Any? {
        return null
    }

    override fun isToolbarVisible(): Boolean {
        return false
    }

    override fun isBottomBarVisible(): Boolean {
        return true
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter =
            ViewPagerAdapter(
                childFragmentManager
            )


        viewDataBinding.favouriteIcon.setOnClickListener {
            if (!viewDataBinding.searchCardViewInnerInbox.isVisible) {
                viewDataBinding.searchCardViewInnerInbox.visibility = View.VISIBLE
            } else {
                viewDataBinding.searchCardViewInnerInbox.visibility = View.GONE

            }
        }

        adapter.addFragment(
            OrderListFragment(
                "active",
                0
            ), "Active"
        )
        adapter.addFragment(
            OrderListFragment(
                "past",
                0
            ), "Completed"
        )
        adapter.addFragment(
            OrderListFragment(
                "disputed",
                0
            ), "Disputed"
        )


        viewDataBinding.viewPager.adapter = adapter
        viewDataBinding.tabs.setupWithViewPager(viewDataBinding.viewPager)

    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_my_order
    }

}

class ViewPagerAdapter(supportFragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(supportFragmentManager) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        try {
            super.restoreState(state, loader)
        } catch (e: Exception) {
            Log.e("TAG", "Error Restore State of Fragment : " + e.message, e)
        }
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
}
