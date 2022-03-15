package com.tyeralin.permissions.viewmodels

import androidx.fragment.app.FragmentActivity

abstract class BaseLinkedViewModel : BaseViewModel() {
   lateinit var next:BaseLinkedViewModel;
   abstract  fun next()
}