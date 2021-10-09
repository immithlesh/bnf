package com.application.brainnforce.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import com.application.brainnforce.R
import com.application.brainnforce.databinding.FragSplashScreenBinding
import com.application.brainnforce.ui.fragments.auth.ChooseAccountTypeFragment
import com.application.brainnforce.ui.fragments.home.DashboardFragment
import com.application.brainnforce.ui.fragments.home.HomeFragment
import com.application.swapp.app.BNFBaseFragment

class SplashFragment : BNFBaseFragment<FragSplashScreenBinding>() {

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

        getContainerActivity().viewDataBinding.bottomBar.visibility = View.GONE

        setGlide(R.drawable.bnf, viewDataBinding.logogif)

        Handler(Looper.getMainLooper()).postDelayed({

            if (linkodesPref.getUserLoggedInStatus(getContainerActivity())!!) {
                if (linkodesPref.getSaveValue(
                        getContainerActivity(),
                        "Account_Type"
                    ) == "Buyer"
                ) {
                    displayIt(
                        HomeFragment(),
                        HomeFragment::class.java.canonicalName, true
                    )
                } else {
                    displayIt(
                        DashboardFragment(),
                        DashboardFragment::class.java.canonicalName,
                        true
                    )
                }

            } else {
                displayIt(
                    ChooseAccountTypeFragment(0),
                    ChooseAccountTypeFragment::class.java.canonicalName,
                    true
                )

            }


        }, 2000)

    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_splash_screen
    }

}