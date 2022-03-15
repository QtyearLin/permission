package com.tyeralin.permissions.viewmodels

import android.Manifest

class PermissionCameraLinkedViewModel : BaseRequestPermissionLinkedViewModel() {

    override fun bindPermissions(): String = Manifest.permission.CAMERA


}


