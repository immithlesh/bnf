package com.application.brainnforce.ui.fragments.conversation

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.application.brainnforce.R
import com.application.brainnforce.databinding.*
import com.application.swapp.app.BNFBaseFragment

class VideoSubscriptionFragment : BNFBaseFragment<FragVideoSubscriptionBinding>() {

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
            getContainerActivity().onBackPressed()
        }

        viewDataBinding.monthlyClick.setOnClickListener {

            viewDataBinding.monthlyOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.blue_outline)
            viewDataBinding.monthlyText.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.purple
                )
            )
            viewDataBinding.monthlyImg.setImageResource(R.drawable.select_monthly)
            viewDataBinding.monthlySelected.visibility = View.VISIBLE

            // unselected
            viewDataBinding.yearlyOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline)
            viewDataBinding.yearlyText.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.yearlyImg.setImageResource(R.drawable.unselect_total_earning)
            viewDataBinding.yearlySelected.visibility = View.INVISIBLE


        }

        viewDataBinding.yearlyClick.setOnClickListener {

            viewDataBinding.yearlyOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.blue_outline)
            viewDataBinding.yearlyText.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.purple
                )
            )
            viewDataBinding.yearlyImg.setImageResource(R.drawable.select_total_earning)
            viewDataBinding.yearlySelected.visibility = View.VISIBLE

            // unselected
            viewDataBinding.monthlyOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline)
            viewDataBinding.monthlyText.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.monthlyImg.setImageResource(R.drawable.unselect_monthly)
            viewDataBinding.monthlySelected.visibility = View.INVISIBLE


        }

    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_video_subscription
    }

}
