package com.application.brainnforce.ui.fragments.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.application.brainnforce.R
import com.application.brainnforce.databinding.FragSettingBinding
import com.application.brainnforce.ui.fragments.auth.LoginFragment
import com.application.brainnforce.ui.fragments.category.DisputesFragment
import com.application.brainnforce.ui.fragments.conversation.VideoSubscriptionFragment
import com.application.brainnforce.ui.fragments.home.DashboardFragment
import com.application.brainnforce.ui.fragments.notification.BlockUserFragment
import com.application.brainnforce.ui.fragments.packages.MyPackageFragment
import com.application.brainnforce.ui.fragments.payment.PaymentSettingFragment
import com.application.brainnforce.ui.fragments.profile.SellerProfileFragment
import com.application.swapp.app.BNFBaseFragment

class SettingFragment : BNFBaseFragment<FragSettingBinding>() {

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

        viewDataBinding.packageTxt.setOnClickListener {
            displayIt(MyPackageFragment(), MyPackageFragment::class.java.canonicalName, true)
        }


        viewDataBinding.videoTxt.setOnClickListener {
            displayIt(
                VideoSubscriptionFragment(),
                VideoSubscriptionFragment::class.java.canonicalName,
                true
            )
        }

        viewDataBinding.disputeTxt.setOnClickListener {
            displayIt(DisputesFragment(), DisputesFragment::class.java.canonicalName, true)
        }

        viewDataBinding.changePasswordText.setOnClickListener {
            displayIt(
                ChangePasswordFragment(),
                ChangePasswordFragment::class.java.canonicalName,
                true
            )
        }

        viewDataBinding.paymentTxt.setOnClickListener {
            displayIt(
                PaymentSettingFragment(),
                PaymentSettingFragment::class.java.canonicalName,
                true
            )
        }
        viewDataBinding.blockTxt.setOnClickListener {
            displayIt(BlockUserFragment(), BlockUserFragment::class.java.canonicalName, true)
        }

        viewDataBinding.LogTxt.setOnClickListener {

            getContainerActivity().supportFragmentManager.popBackStack(DashboardFragment::class.java.canonicalName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            displayIt(LoginFragment(), LoginFragment::class.java.canonicalName, false)
        }

    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_setting
    }

}
