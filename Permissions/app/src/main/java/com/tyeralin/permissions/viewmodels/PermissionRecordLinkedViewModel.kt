package com.tyeralin.permissions.viewmodels

import android.Manifest

class PermissionRecordLinkedViewModel : BaseRequestPermissionLinkedViewModel() {
    override fun bindPermissions(): String = Manifest.permission.RECORD_AUDIO


}