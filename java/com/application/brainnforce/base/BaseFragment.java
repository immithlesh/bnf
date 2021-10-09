package com.application.brainnforce.base;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.application.brainnforce.local.PrefUtils;
import com.application.brainnforce.network.ApiService;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import io.reactivex.disposables.CompositeDisposable;


// by :- Deepak Kumar
// at :- Netset Software
// in :- Java

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {

    private BaseActivity mActivity;
    private T mViewDataBinding;
    private View mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mViewDataBinding == null) {
            mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
            mRootView = mViewDataBinding.getRoot();
        }

        return mRootView;
    }


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    public BaseActivity getBaseActivity(){
        return mActivity == null ? (BaseActivity) getActivity() : mActivity;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

//    public BaseActivity getBaseActivity() {
//        return mActivity;
//    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

//    public boolean isNetworkConnected() {
//        return mActivity != null && mActivity.isNetworkConnected();
//    }

//    public void openActivityOnTokenExpire() {
//        if (mActivity != null) {
//            mActivity.openActivityOnTokenExpire();
//        }
//    }


    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    //    public void vibrate(){
//        AppUtils.vibrate(mActivity);
//    }
//

    public void showLoading() {
        getBaseActivity().showLoading();
    }

    public void hideLoading() {
        getBaseActivity().hideLoading();
    }

//    public void showResponseDialog(String message){
//        mActivity.showResponseDialog(message);
//    }


    public CompositeDisposable getDisposable() {
        return getBaseActivity().getDisposable();
    }

    public ApiService getApiService() {
        return getBaseActivity().getApiService();
    }

    public PrefUtils getLinkodesPref() {
        return new PrefUtils();
    }

    public void displayIt(final Fragment mFragment, final String tag, final boolean isBack) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                if (getBaseActivity() == null){
                    return;
                }

                getBaseActivity().displayIt(mFragment, tag, isBack);
            }
        }, 1000);
    }

    public Fragment setArguments(final Fragment mFragment, Bundle mBundle) {
        return getBaseActivity().setArguments(mFragment, mBundle);
    }

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    public void showToast(String msg){
        Toast.makeText(getBaseActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void setGlide(int url, ImageView img) {
        Glide.with(getBaseActivity()).load(url).into(img);

    }

    public void setGlideString(String string, ImageView img) {
        Glide.with(mActivity).load(string).into(img);

    }


    public void setGlideFile(File file, ImageView img) {
        Glide.with(mActivity).load(file).into(img);

    }

    public void setGlide(Uri uri, ImageView img) {
        Glide.with(mActivity).load(uri).into(img);
    }


}
