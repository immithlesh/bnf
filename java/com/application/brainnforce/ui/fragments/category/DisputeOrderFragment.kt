package com.application.brainnforce.ui.fragments.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.brainnforce.R
import com.application.brainnforce.base.adapter.RecyclerViewGenricAdapter
import com.application.brainnforce.databinding.*
import com.application.brainnforce.model.UserDetail
import com.application.swapp.app.BNFBaseFragment


class DisputeOrderFragment(var orderStatus: String, var orderFrag: Int) :
    BNFBaseFragment<FragDisputeOrderBinding>() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.backIcon.setOnClickListener {

            if(orderFrag == 0){
                displayIt(MyOrderFragment(), MyOrderFragment::class.java.canonicalName, false)
            }else {
                getContainerActivity().onBackPressed()
            }

        }

        if (orderStatus == "active") {
            viewDataBinding.activeLayout.visibility = View.VISIBLE
            viewDataBinding.pastLayout.visibility = View.GONE
            viewDataBinding.disputedLayout.visibility = View.GONE
            viewDataBinding.ReviewLayout.visibility = View.GONE

            if (linkodesPref.getSaveValue(getContainerActivity(), "Account_Type") == "Buyer") {
                viewDataBinding.RatingLayout.visibility = View.GONE
                viewDataBinding.RatingTextLayout.visibility = View.GONE
                viewDataBinding.completeClick.setOnClickListener {
                    viewDataBinding.giveRatingText.visibility = View.VISIBLE
                }

                viewDataBinding.giveRatingText.setOnClickListener {
                    viewDataBinding.activeLayout.visibility = View.GONE
                    viewDataBinding.RatingLayout.visibility = View.VISIBLE
                    viewDataBinding.RatingTextLayout.visibility = View.GONE
                }

                viewDataBinding.submitBtn.setOnClickListener {
                    viewDataBinding.activeLayout.visibility = View.GONE
                    viewDataBinding.RatingLayout.visibility = View.GONE
                    viewDataBinding.RatingTextLayout.visibility = View.VISIBLE
                }


            } else {

                viewDataBinding.RatingLayout.visibility = View.GONE
                viewDataBinding.RatingTextLayout.visibility = View.GONE

            }
        }
        else if (orderStatus == "past") {
            viewDataBinding.activeLayout.visibility = View.GONE
            viewDataBinding.pastLayout.visibility = View.VISIBLE
            viewDataBinding.disputedLayout.visibility = View.GONE

            viewDataBinding.RatingLayout.visibility = View.GONE
            viewDataBinding.RatingTextLayout.visibility = View.GONE
            viewDataBinding.ReviewLayout.visibility = View.GONE


            viewDataBinding.pastLayout.setOnClickListener {

                viewDataBinding.activeLayout.visibility = View.GONE
                viewDataBinding.pastLayout.visibility = View.GONE
                viewDataBinding.disputedLayout.visibility = View.GONE

                viewDataBinding.RatingLayout.visibility = View.GONE
                viewDataBinding.RatingTextLayout.visibility = View.GONE

                viewDataBinding.ReviewLayout.visibility = View.VISIBLE

            }


        }
        else {
            viewDataBinding.activeLayout.visibility = View.GONE
            viewDataBinding.pastLayout.visibility = View.GONE
            viewDataBinding.disputedLayout.visibility = View.VISIBLE

            viewDataBinding.RatingLayout.visibility = View.GONE
            viewDataBinding.RatingTextLayout.visibility = View.GONE

            viewDataBinding.ReviewLayout.visibility = View.GONE

        }

        list = ArrayList()
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())

        viewDataBinding.crossIcon.setOnClickListener {
            viewDataBinding.activeLayout.visibility = View.GONE
            viewDataBinding.pastLayout.visibility = View.VISIBLE
            viewDataBinding.disputedLayout.visibility = View.GONE

            viewDataBinding.RatingLayout.visibility = View.GONE
            viewDataBinding.RatingTextLayout.visibility = View.GONE
            viewDataBinding.ReviewLayout.visibility = View.GONE
        }

        mAdapter = RecyclerViewGenricAdapter(list, R.layout.past_review_item)
        { binder, model, position, itemView ->


        }

        val activeLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        viewDataBinding.recyclerView.layoutManager = activeLayoutManager
        viewDataBinding.recyclerView.adapter = mAdapter


    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_dispute_order
    }

}
