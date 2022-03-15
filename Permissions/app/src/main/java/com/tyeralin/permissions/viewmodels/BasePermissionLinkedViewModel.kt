package com.tyeralin.permissions.viewmodels

import com.tyeralin.permissions.extend.PermissionProxy

abstract class BasePermissionLinkedViewModel : BaseLinkedViewModel(),PermissionProxy.PermissionCallback {

    abstract fun requestPermission()
}