package com.application.brainnforce.ui.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.brainnforce.R
import com.application.brainnforce.base.adapter.RecyclerViewGenricAdapter
import com.application.brainnforce.databinding.*
import com.application.brainnforce.model.UserDetail
import com.application.brainnforce.ui.fragments.payment.PaymentFragment
import com.application.swapp.app.BNFBaseFragment
import com.willy.ratingbar.BaseRatingBar
import com.willy.ratingbar.BaseRatingBar.OnRatingChangeListener

class ServiceDetailListFragment() :
    BNFBaseFragment<FragDescriptionBinding>() {

    private var mAdapter: RecyclerViewGenricAdapter<UserDetail, PastReviewItemBinding>? =
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.backIcon.setOnClickListener {
            getContainerActivity().onBackPressed()
        }

        viewDataBinding.payBtn.setOnClickListener {
            displayIt(PaymentFragment(), PaymentFragment::class.java.canonicalName, true)
        }


        viewDataBinding.ContactFreelancerNBtn.setOnClickListener {
            callVideoDialog(object : OnDialogListener {

                override fun onYesClick() {

                }

            })
        }

        list = ArrayList()
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())

        mAdapter = RecyclerViewGenricAdapter(list, R.layout.past_review_item)
        { binder, model, position, itemView ->


        }
        viewDataBinding.crossIcon.setOnClickListener {
            viewDataBinding.ReviewLayout.visibility = View.GONE
        }

        val activeLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        viewDataBinding.recyclerView.layoutManager = activeLayoutManager
        viewDataBinding.recyclerView.adapter = mAdapter
        viewDataBinding.simpleRatingBar.rating=4.0f
        viewDataBinding.simpleRatingBar.setOnClickListener {

            viewDataBinding.simpleRatingBar.setClearRatingEnabled(false)
            viewDataBinding.simpleRatingBar.setOnRatingChangeListener(OnRatingChangeListener { ratingBar: BaseRatingBar?, rating: Float, fromUser: Boolean ->
                Log
                    .d("rating",
                        "BaseRatingBar onRatingChange: $rating"
                    )
            })
            viewDataBinding.ReviewLayout.visibility = View.VISIBLE
        }

    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_description
    }

}

