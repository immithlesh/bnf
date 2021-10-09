package com.application.brainnforce.ui.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.application.brainnforce.R
import com.application.brainnforce.databinding.FragDashboardBinding
import com.application.brainnforce.ui.fragments.category.OrderListFragment
import com.application.swapp.app.BNFBaseFragment

class DashboardFragment : BNFBaseFragment<FragDashboardBinding>() {

    override fun configureToolbar(): Any? {
        return null
    }

    override fun isToolbarVisible(): Boolean {
        return false
    }

    override fun isBottomBarVisible(): Boolean {
        return true
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getContainerActivity().viewDataBinding.bottomNavigationView.getMenu().getItem(2)
            .setChecked(true)
        getContainerActivity().viewDataBinding?.addPostClick2?.visibility = View.VISIBLE
        getContainerActivity().viewDataBinding?.addPostClick?.visibility = View.INVISIBLE
        getContainerActivity().SellerSide()

        viewDataBinding.totalClick.setOnClickListener {

            viewDataBinding.totalOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.blue_outline_white)
            viewDataBinding.totalTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.purple
                )
            )
            viewDataBinding.totalImg.setImageResource(R.drawable.select_total_earning)
            viewDataBinding.totalSelected.visibility = View.VISIBLE

            // unselected
            viewDataBinding.activeOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline_white)
            viewDataBinding.activeTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.activeImg.setImageResource(R.drawable.unselect_active_order)
            viewDataBinding.activeSelected.visibility = View.INVISIBLE

            viewDataBinding.completeOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline_white)
            viewDataBinding.completeTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.completeImg.setImageResource(R.drawable.unselect_complete_order)
            viewDataBinding.completeSelected.visibility = View.INVISIBLE

            viewDataBinding.disputeOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline_white)
            viewDataBinding.disputeTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.disputeImg.setImageResource(R.drawable.unselected_disputed)
            viewDataBinding.disputeSelected.visibility = View.INVISIBLE

            displayIt(TotalEarningFragment(), TotalEarningFragment::class.java.canonicalName, true)

        }

        viewDataBinding.activeClick.setOnClickListener {

            viewDataBinding.activeOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.blue_outline_white)
            viewDataBinding.activeTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.purple
                )
            )
            viewDataBinding.activeImg.setImageResource(R.drawable.select_active_order)
            viewDataBinding.activeSelected.visibility = View.VISIBLE

            // unselected
            viewDataBinding.totalOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline_white)
            viewDataBinding.totalTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.totalImg.setImageResource(R.drawable.unselect_total_earning)
            viewDataBinding.totalSelected.visibility = View.INVISIBLE

            viewDataBinding.completeOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline_white)
            viewDataBinding.completeTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.completeImg.setImageResource(R.drawable.unselect_complete_order)
            viewDataBinding.completeSelected.visibility = View.INVISIBLE


            viewDataBinding.disputeOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline_white)
            viewDataBinding.disputeTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.disputeImg.setImageResource(R.drawable.unselected_disputed)
            viewDataBinding.disputeSelected.visibility = View.INVISIBLE

            displayIt(
                OrderListFragment(
                    "active",
                    1
                ), OrderListFragment::class.java.canonicalName, true
            )


        }

        viewDataBinding.completeClick.setOnClickListener {

            viewDataBinding.completeOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.blue_outline_white)
            viewDataBinding.completeTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.purple
                )
            )
            viewDataBinding.completeImg.setImageResource(R.drawable.select_complete_order)
            viewDataBinding.completeSelected.visibility = View.VISIBLE

            // unselected
            viewDataBinding.totalOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline_white)
            viewDataBinding.totalTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.totalImg.setImageResource(R.drawable.unselect_total_earning)
            viewDataBinding.totalSelected.visibility = View.INVISIBLE

            viewDataBinding.activeOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline_white)
            viewDataBinding.activeTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.activeImg.setImageResource(R.drawable.unselect_active_order)
            viewDataBinding.activeSelected.visibility = View.INVISIBLE


            viewDataBinding.disputeOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline_white)
            viewDataBinding.disputeTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.disputeImg.setImageResource(R.drawable.unselected_disputed)
            viewDataBinding.disputeSelected.visibility = View.INVISIBLE

            displayIt(
                OrderListFragment(
                    "past",
                    1
                ), OrderListFragment::class.java.canonicalName, true
            )
        }

        viewDataBinding.disputeClick.setOnClickListener {

            viewDataBinding.disputeOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.blue_outline_white)
            viewDataBinding.disputeTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.purple
                )
            )
            viewDataBinding.disputeImg.setImageResource(R.drawable.select_disputed)
            viewDataBinding.disputeSelected.visibility = View.VISIBLE

            // unselected
            viewDataBinding.totalOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline_white)
            viewDataBinding.totalTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.totalImg.setImageResource(R.drawable.unselect_total_earning)
            viewDataBinding.totalSelected.visibility = View.INVISIBLE

            viewDataBinding.completeOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline_white)
            viewDataBinding.completeTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.completeImg.setImageResource(R.drawable.unselect_complete_order)
            viewDataBinding.completeSelected.visibility = View.INVISIBLE


            viewDataBinding.activeOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline_white)
            viewDataBinding.activeTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.activeImg.setImageResource(R.drawable.unselect_active_order)
            viewDataBinding.activeSelected.visibility = View.INVISIBLE

            displayIt(
                OrderListFragment(
                    "disputed",
                    1
                ), OrderListFragment::class.java.canonicalName, true
            )
        }

    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_dashboard
    }

}
