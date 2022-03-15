package com.tyeralin.permissions.viewmodels

import com.tyeralin.permissions.extend.PermissionProxy

abstract class BaseRequestPermissionLinkedViewModel : BasePermissionLinkedViewModel() {
    abstract fun bindPermissions(): String

    override fun requestPermission(
    ) {

        PermissionProxy.registerRequetPermissionCallback(permission = bindPermissions(), this)
        PermissionProxy.requestPermission(bindPermissions());


    }

    override fun onCleared() {
        PermissionProxy.removeRequetPermissionCallback(permission = bindPermissions())
        super.onCleared()
    }

    override fun next() {
        requestPermission()
    }

    override fun onNext(permission: String, granted: Boolean, shouldShowRequestPermissionRationale: Boolean) {
        if (granted) {
             next.next()
        } else{
            //已拒绝
            if(shouldShowRequestPermissionRationale) {

            } else{

            }
        }

    }
}