package com.tyeralin.permissions.extend

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object PermissionProxy : Fragment() {

    private val TAG: String = "PermissionProxy"
    var dialog: AlertDialog? = null


    private var permissionCallbacks: MutableMap<String, PermissionCallback> = mutableMapOf<String, PermissionCallback>()


    fun registerRequetPermissionCallback(
            permission: String,
            callback: PermissionCallback
    ) {
        if (!permissionCallbacks.contains(callback))
            permissionCallbacks.put(permission, callback)
    }

    fun removeRequetPermissionCallback(
            permission: String
    ) = permissionCallbacks.remove(permission)


    interface PermissionCallback {
        fun onNext(permission: String, granted: Boolean, shouldShowRequestPermissionRationale: Boolean)
    }

    fun isGranted(permission: String?): Boolean {
        val fragmentActivity = this.activity
        return if (fragmentActivity == null) {
            throw IllegalStateException("This fragment must be attached to an activity.")
        } else {
            fragmentActivity.checkSelfPermission(permission!!) == 0
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 199) {
            val shouldShowRequestPermissionRationale = BooleanArray(permissions.size)
            for (i in permissions.indices) {
                shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i])
            }
            this.onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale)

        }


    }


    fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray, shouldShowRequestPermissionRationale: BooleanArray) {

        for (permission in permissions) {
            val permissionCallback: PermissionCallback? =
                    permissionCallbacks.get(permission);
            permissionCallbacks.remove(permission)
            val index = permissions.indexOf(permission)
            val grant = grantResults[index] == 0
            permissionCallback?.onNext(permissions[index], grant, shouldShowRequestPermissionRationale[index])
            //内部回调 showGlobalDialog
            if (index == 0) {
                singlePermissionCallback.onNext(permissions[0], grant, shouldShowRequestPermissionRationale[index])
            }
        }

    }

    fun requestPermission(permission: String) {
        val granted = isGranted(permission)
        if (granted) {
            val permissionCallback: PermissionCallback? =
                    permissionCallbacks.get(permission);
            permissionCallbacks.remove(permission)
            permissionCallback?.onNext(permission, true, false)
        } else {
            //是否已拒绝过 已拒绝禁止再次弹出请求权限
            val refused = shouldShowRequestPermissionRationale(permission)
            val permissionCallback: PermissionCallback? =
                    permissionCallbacks.get(permission);
//            permissionCallbacks.remove(permission)
            permissionCallback?.onNext(permission, false, refused)
            showGlobalDialog(refused, permission)
        }

    }

    private val singlePermissionCallback = object : PermissionCallback {
        override fun onNext(permission: String, granted: Boolean, shouldShowRequestPermissionRationale: Boolean) {
            if (!granted && !shouldShowRequestPermissionRationale) {
                openSettingPage()
            }
        }
    }

    private fun showGlobalDialog(refused: Boolean, permission: String) {
//        val builder: AlertDialog.Builder = AlertDialog.Builder(activity, "无法使用$msgBar", "使用该功能需要$msgBar,请前往系统设置开启权限")
        if (dialog != null) {
            dialog!!.dismiss()
            dialog = null;
        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("提示").setMessage("使用该功能需要 $permission,请前往系统设置开启权限")
                .setNegativeButton("取消") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                .setPositiveButton("前往设置") { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                    if (refused) {
                        //true 则
                        openSettingPage()
                        //成功后 还需要回调 TODO
                    } else {
                        requestPermissions(arrayOf(permission), 199)
                    }
                }
        dialog = builder.create()
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    override fun onDetach() {
        if (null != dialog) {
            dialog!!.dismiss()
            dialog = null
        }
        super.onDetach()
    }

    private fun openSettingPage() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", activity?.packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    fun init(activity: FragmentActivity) {
        val fragment = activity.supportFragmentManager.findFragmentByTag(TAG)
        if (fragment == null) {
            activity.supportFragmentManager.beginTransaction().add(this, TAG).commitAllowingStateLoss()
        }
    }

    fun release(activity: FragmentActivity) {
        val fragment = activity.supportFragmentManager.findFragmentByTag(TAG)
        if (fragment != null) {
            activity.supportFragmentManager.beginTransaction().detach(this).commitAllowingStateLoss()
        }
    }

}