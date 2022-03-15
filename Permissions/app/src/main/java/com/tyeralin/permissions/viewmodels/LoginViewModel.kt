package com.tyeralin.permissions.viewmodels

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class LoginViewModel : BaseLinkedViewModel() {
     val loginSuccess = MutableLiveData<Boolean>()
    override fun next() {
        loginSuccess.value = true
    }
}