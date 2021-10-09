package com.application.brainnforce.app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import com.application.brainnforce.R
import com.application.brainnforce.base.BaseActivity
import com.application.brainnforce.databinding.ActivityBrainnforceBinding
import com.application.brainnforce.ui.fragments.SplashFragment
import com.application.brainnforce.ui.fragments.category.EditCategoryFragment
import com.application.brainnforce.ui.fragments.category.MyOrderFragment
import com.application.brainnforce.ui.fragments.home.DashboardFragment
import com.application.brainnforce.ui.fragments.home.HomeFragment
import com.application.brainnforce.ui.fragments.notification.InboxFragment
import com.application.brainnforce.ui.fragments.notification.NotificationFragment
import com.application.brainnforce.ui.fragments.profile.ProfileFragment
import com.application.brainnforce.ui.fragments.profile.SellerProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BrainNForceActivity : BaseActivity<ActivityBrainnforceBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_brainnforce
    }

    override fun setContainerLayout(): Int {
        return viewDataBinding.frameContainer.id
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        displayIt(SplashFragment(), SplashFragment::class.java.canonicalName, false)

        val menu: Menu = viewDataBinding.bottomNavigationView.getMenu()
        viewDataBinding.bottomNavigationView.getMenu().getItem(2).setChecked(true)
        viewDataBinding.bottomNavigationView1.getMenu().getItem(2).setChecked(true)

        if (linkodesPref.getSaveValue(applicationContext, "Account_Type") == "Buyer") {
            viewDataBinding?.addPostClick?.visibility = View.VISIBLE
            viewDataBinding?.addPostClick2?.visibility = View.INVISIBLE
            BuyerSide()
        }

        if (linkodesPref.getSaveValue(applicationContext, "Account_Type") == "Seller") {
            viewDataBinding?.addPostClick2?.visibility = View.VISIBLE
            viewDataBinding?.addPostClick?.visibility = View.INVISIBLE
            SellerSide()
        }

        viewDataBinding.addPostClick.setOnClickListener {
                viewDataBinding.addPostClick.setImageResource(R.drawable.select_home)
                displayIt(HomeFragment(), HomeFragment::class.java.canonicalName, false)
                viewDataBinding.bottomNavigationView.getMenu().getItem(2).setChecked(true)

        }

        viewDataBinding.addPostClick2.setOnClickListener {

                viewDataBinding.addPostClick2.setImageResource(R.drawable.select_home)
                displayIt(DashboardFragment(), DashboardFragment::class.java.canonicalName, false)
                viewDataBinding.bottomNavigationView1.getMenu().getItem(2).setChecked(true)

        }

        onNewIntent(intent)

    }

    fun BuyerSide() {
        viewDataBinding?.bottomNavigationView?.visibility = View.VISIBLE
        viewDataBinding?.bottomNavigationView1?.visibility = View.INVISIBLE
        viewDataBinding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navChatMenuId -> {
                    viewDataBinding.addPostClick.setImageResource(R.drawable.unselected_home)
                    displayIt(InboxFragment(), InboxFragment::class.java.canonicalName, false)
                    true
                }
                R.id.navNotificationMenuId -> {
                    viewDataBinding.addPostClick.setImageResource(R.drawable.unselected_home)
                    displayIt(
                        NotificationFragment(),
                        NotificationFragment::class.java.canonicalName,
                        false
                    )
                    true
                }
                R.id.navOrderMenuId -> {
                    viewDataBinding.addPostClick.setImageResource(R.drawable.unselected_home)
                    displayIt(
                        MyOrderFragment(),
                        MyOrderFragment::class.java.canonicalName,
                        false
                    )
                    true
                }

                R.id.navProfileMenuId -> {
                    viewDataBinding.addPostClick.setImageResource(R.drawable.unselected_home)
                    displayIt(
                        ProfileFragment(),
                        ProfileFragment::class.java.canonicalName,
                        false
                    )
                    true
                }
                else -> false
            }
        }
    }

    fun SellerSide() {
        viewDataBinding?.bottomNavigationView1?.visibility = View.VISIBLE
        viewDataBinding?.bottomNavigationView?.visibility = View.INVISIBLE
        viewDataBinding?.bottomNavigationView1?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navChatMenuId1 -> {
                    viewDataBinding.addPostClick2.setImageResource(R.drawable.unselected_home)
                    displayIt(InboxFragment(), InboxFragment::class.java.canonicalName, false)
                    true
                }
                R.id.navNotificationMenuId1 -> {
                    viewDataBinding.addPostClick2.setImageResource(R.drawable.unselected_home)
//                    linkodesPref.clear(this, "Category")
//                    linkodesPref.clear(this, "Category2")
                    displayIt(
                        EditCategoryFragment(1),
                        EditCategoryFragment::class.java.canonicalName,
                        false
                    )
                    true
                }
                R.id.navOrderMenuId1 -> {
                    viewDataBinding.addPostClick2.setImageResource(R.drawable.unselected_home)
                    displayIt(
                        MyOrderFragment(),
                        MyOrderFragment::class.java.canonicalName,
                        false
                    )
                    true
                }

                R.id.navProfileMenuId1 -> {
                    viewDataBinding.addPostClick2.setImageResource(R.drawable.unselected_home)
                    displayIt(
                        SellerProfileFragment(),
                        SellerProfileFragment::class.java.canonicalName,
                        false
                    )
                    true
                }
                else -> false
            }
        }
    }


    fun isToolbarVisible(isVisible: Boolean) {

    }

    fun isBottomBarVisible(isVisible: Boolean) {

        if (isVisible)
            viewDataBinding.bottomBar.visibility = View.VISIBLE
        else
            viewDataBinding.bottomBar.visibility = View.GONE

    }

    fun configureToolbar(toolbarModel: Any?) {

    }

    fun getCurrentFragment(fragment: Fragment) {
        currentFragment = fragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        currentFragment!!.onActivityResult(requestCode, resultCode, data)
    }

}
