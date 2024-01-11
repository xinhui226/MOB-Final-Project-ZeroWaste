package com.xinhui.mobfinalproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.core.utils.Constants
import com.xinhui.mobfinalproject.core.utils.NotificationUtil

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.navHostFragment)

        NotificationUtil.createNotificationChannel(this,Constants.expiryNotificationName, Constants.expiryNotificationChannelId)
    }
}