package com.application.brainnforce.ui.fragments.payment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.application.brainnforce.R
import com.application.brainnforce.databinding.*
import com.application.swapp.app.BNFBaseFragment

class PaymentSettingFragment : BNFBaseFragment<FragPaymentSettingBinding>() {

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

        viewDataBinding.buyerClick.setOnClickListener {

            viewDataBinding.buyerOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.blue_outline)
            viewDataBinding.buyerImg.setImageResource(R.drawable.select_round_add)
            viewDataBinding.buyerTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.purple
                )
            )
            viewDataBinding.buyerSelected.visibility = View.VISIBLE

            // unselected
            viewDataBinding.sellerOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline)
            viewDataBinding.sellerTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.sellerSelected.visibility = View.INVISIBLE

            viewDataBinding.cardLayout.visibility = View.VISIBLE
            viewDataBinding.buttonlayout.visibility = View.VISIBLE
            viewDataBinding.cardPaypalLayout.visibility = View.GONE

        }

        viewDataBinding.sellerClick.setOnClickListener {

            viewDataBinding.sellerOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.blue_outline)
            viewDataBinding.sellerTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.purple
                )
            )
            viewDataBinding.sellerSelected.visibility = View.VISIBLE

            // unselected
            viewDataBinding.buyerOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline)
            viewDataBinding.buyerTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.buyerImg.setImageResource(R.drawable.unselect_round_add)
            viewDataBinding.buyerSelected.visibility = View.INVISIBLE

            viewDataBinding.cardPaypalLayout.visibility = View.VISIBLE
            viewDataBinding.cardLayout.visibility = View.GONE
            viewDataBinding.buttonlayout.visibility = View.GONE

        }

        viewDataBinding.logoIcon.setOnClickListener {
            getContainerActivity().onBackPressed()
        }


    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_payment_setting
    }

}
