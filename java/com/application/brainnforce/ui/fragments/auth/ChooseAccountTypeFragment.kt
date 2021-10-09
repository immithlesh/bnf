package com.application.brainnforce.ui.fragments.auth

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.application.brainnforce.R
import com.application.brainnforce.databinding.FragChooseAccTypeBinding
import com.application.brainnforce.utils.ConstantStr
import com.application.swapp.app.BNFBaseFragment
import pl.droidsonroids.gif.GifDrawable
import java.io.IOException

class ChooseAccountTypeFragment(var gif: Int) : BNFBaseFragment<FragChooseAccTypeBinding>() {

    var gifDrawable: GifDrawable? = null

    override fun configureToolbar(): Any? {
        return null
    }

    override fun isToolbarVisible(): Boolean {
        return false
    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun isBottomBarVisible(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_choose_acc_type
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getContainerActivity().viewDataBinding.bottomBar.visibility = View.GONE

        if (gif == 0) {
            try {
                gifDrawable = GifDrawable(resources, R.drawable.bnf)
                gifDrawable!!.loopCount = 1
            } catch (e: IOException) {
                e.printStackTrace()
            }
            viewDataBinding.bnfLogo.setImageDrawable(gifDrawable)
            viewDataBinding.bnfLogo1.visibility = View.GONE
        } else {
            viewDataBinding.bnfLogo1.visibility = View.VISIBLE
            setGlide(R.drawable.gif_image, viewDataBinding.bnfLogo1)
            viewDataBinding.bnfLogo.visibility = View.GONE
        }

        viewDataBinding.buyerClick.setOnClickListener {


            linkodesPref.setSaveValue(getContainerActivity(), "Account_Type", "Buyer")

            viewDataBinding.buyerOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.sky_blue_outline)
            viewDataBinding.buyerTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.red
                )
            )
            viewDataBinding.buyerImg.setImageResource(R.drawable.select_buyer_type)
//            viewDataBinding.buyerSelected.visibility = View.VISIBLE

            // unselected
            viewDataBinding.sellerOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline)
            viewDataBinding.sellerTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.sellerImg.setImageResource(R.drawable.unselect_complete_order)
//            viewDataBinding.sellerSelected.visibility = View.INVISIBLE

            displayIt(
                LoginFragment.newInstance(ConstantStr.BUYER),
                LoginFragment::class.java.canonicalName,
                true
            )


        }

        viewDataBinding.sellerClick.setOnClickListener {
            linkodesPref.setSaveValue(getContainerActivity(), "Account_Type", "Seller")
            viewDataBinding.sellerOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.sky_blue_outline)
            viewDataBinding.sellerTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.red
                )
            )
            viewDataBinding.sellerImg.setImageResource(R.drawable.select_seller_type)
//            viewDataBinding.sellerSelected.visibility = View.VISIBLE

            // unselected
            viewDataBinding.buyerOutline.background =
                ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline)
            viewDataBinding.buyerTxt.setTextColor(
                ContextCompat.getColor(
                    getContainerActivity(),
                    R.color.dark_grey
                )
            )
            viewDataBinding.buyerImg.setImageResource(R.drawable.unselect_buyer_type)
            displayIt(
                LoginFragment.newInstance(ConstantStr.SELER),
                LoginFragment::class.java.canonicalName,
                true
            )
        }

    }
}