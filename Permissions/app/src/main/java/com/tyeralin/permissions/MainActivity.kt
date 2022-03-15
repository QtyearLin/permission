package com.tyeralin.permissions

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tyeralin.permissions.extend.PermissionProxy
import com.tyeralin.permissions.viewmodels.LoginViewModel
import com.tyeralin.permissions.viewmodels.MainViewModel
import com.tyeralin.permissions.viewmodels.PermissionCameraLinkedViewModel
import com.tyeralin.permissions.viewmodels.PermissionRecordLinkedViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        PermissionProxy.init(this)
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            userRequestPermissions();
        }

    }

    private fun userRequestPermissions() {
        val permissionCameraLinkedViewModel =
                PermissionCameraLinkedViewModel()
        val permissionRecordLinkedViewModel =
                PermissionRecordLinkedViewModel()
        val loginViewModel = LoginViewModel()

        loginViewModel.loginSuccess.observe(this, Observer {
            if (it) {
                Toast.makeText(baseContext, "登录成功", Toast.LENGTH_SHORT).show()
            }

        })
        viewModel.requestPermissions(permissionCameraLinkedViewModel, permissionRecordLinkedViewModel, loginViewModel, activity = this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}