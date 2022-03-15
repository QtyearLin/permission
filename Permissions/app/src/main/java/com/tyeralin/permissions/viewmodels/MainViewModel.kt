package com.tyeralin.permissions.viewmodels

import androidx.fragment.app.FragmentActivity
import com.tyeralin.permissions.extend.PermissionProxy

class MainViewModel : BaseViewModel() {
    fun requestPermissions(
            vararg permissions: BaseLinkedViewModel, activity: FragmentActivity
    ) {
        //link

        for ((index, value) in permissions.withIndex()) {
            if (index != permissions.size - 1) {
                value.next = permissions[index + 1]
            }
        }
        permissions[0].next()
    }


}