package com.application.brainnforce.ui.fragments.conversation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.brainnforce.R
import com.application.brainnforce.base.adapter.RecyclerViewGenricAdapter
import com.application.brainnforce.databinding.*
import com.application.brainnforce.model.UserDetail
import com.application.brainnforce.ui.fragments.profile.SellerProfileFragment
import com.application.swapp.app.BNFBaseFragment

class ChatFragment : BNFBaseFragment<FragChatBinding>() {

    private var mAdapter: RecyclerViewGenricAdapter<UserDetail, ChatItemBinding>? =
        null
    private var list: ArrayList<UserDetail> = ArrayList()

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

        viewDataBinding.menuIcon.setOnClickListener {
            callVideoScheduledDialog(
                object : OnDialogListeners {

                    override fun onYesClick(s: String) {
                        if (s == "video") {
                            callVideoConferenceScheduledDialog(
                                object : OnDialogListener {

                                    override fun onYesClick() {


                                    }

                                })
                        }
                        else if (s == "report") {
                             callReportDialog(
                                object : OnDialogListener {
                                    override fun onYesClick() {
                                    }

                                })
                        }

                    }

                })
        }

        viewDataBinding.logoIcon.setOnClickListener {
            getContainerActivity().onBackPressed()
        }

        viewDataBinding.image.setOnClickListener {
            displayIt(SellerProfileFragment(),SellerProfileFragment::class.java.canonicalName, true)
        }

        list = ArrayList()
        list.add(UserDetail())

        mAdapter = RecyclerViewGenricAdapter(list, R.layout.chat_item)
        { binder, model, position, itemView ->


        }

        val mLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        viewDataBinding.recyclerView.layoutManager = mLayoutManager
        viewDataBinding.recyclerView.adapter = mAdapter

    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_chat
    }

}
