package com.xinhui.mobfinalproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.core.utils.Constants
import com.xinhui.mobfinalproject.core.utils.NotificationUtil
import com.xinhui.mobfinalproject.core.service.AuthService
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    @Inject
    lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.navHostFragment)

        NotificationUtil.createNotificationChannel(this,Constants.expiryNotificationName, Constants.expiryNotificationChannelId)
        lifecycleScope.launch {
            if (authService.getCurrUser() != null){
                navController.navigate(R.id.toHome)
            }
        }
    }
}