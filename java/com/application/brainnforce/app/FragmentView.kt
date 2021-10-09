package com.application.brainnforce.app

import androidx.fragment.app.Fragment

interface FragmentView {

     fun getCurrentFragment(): Fragment

     fun isToolbarVisible(): Boolean

     fun isBottomBarVisible(): Boolean

     fun configureToolbar() : Any?

}