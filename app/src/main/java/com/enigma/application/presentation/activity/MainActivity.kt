package com.enigma.application.presentation.activity

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import com.enigma.application.R
import com.enigma.application.databinding.ActivityMainBinding
import com.enigma.application.presentation.activity.NotificationHelper.displayNotification
import com.enigma.application.utils.Constants.Companion.MENU_HISTORY
import com.enigma.application.utils.Constants.Companion.MENU_HOME
import com.enigma.application.utils.Constants.Companion.MENU_PROFILE
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ActivityViewModel

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        initViewModel()
        subscribe()
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.apply {
            bottomNavigation.setOnNavigationItemSelectedListener {
                enableButton()
                when ("$it") {
                    MENU_HOME -> {
                        bottomNavigation.menu.getItem(0).isEnabled = false
                        navHostFragment.findNavController()
                            .navigate(R.id.action_global_homeFragment)
                    }
                    MENU_HISTORY -> {
                        bottomNavigation.menu.getItem(1).isEnabled = false
                        navHostFragment.findNavController()
                            .navigate(R.id.action_global_historyFragment)
                    }
                    MENU_PROFILE -> {
                        bottomNavigation.menu.getItem(2).isEnabled = false
                        navHostFragment.findNavController()
                            .navigate(R.id.action_global_profileFragment)
                    }
                }
                true
            }
        }
        setContentView(binding.root)
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ActivityViewModel::class.java)
    }

    fun enableButton() {
        for (i in 0..2) {
            binding.bottomNavigation.menu.getItem(i).isEnabled = true
        }
    }

    fun subscribe() {
        viewModel.bottomVisibility.observe(this) {
            val maxHeight = binding.layoutActivity.height
            binding.navHostFragment.minimumHeight = maxHeight
            binding.bottomNavigation.visibility = it
        }

        if (checkGooglePlayServices()) {
            // [START retrieve_current_token]
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("ERROR", getString(R.string.token_error), task.exception)
                        return@OnCompleteListener
                    }
                    // Get new Instance ID token
                    val token = task.result?.token
                    // Log and toast
                    val msg = getString(R.string.token_prefix, token)
                    Log.d("TAG", msg)
                })
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            messageReceiver,
            IntentFilter("MyData")
        )
    }


    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            Log.d(
                "MESSAGE RECEIVER",
                intent.extras?.getString("title")!! + " " + intent.extras?.getString("body")!!
            )
            if (context != null) {
                displayNotification(
                    context,
                    intent.extras?.getString("title")!!, intent.extras?.getString("body")!!
                )
            }
        }
    }


    private fun checkGooglePlayServices(): Boolean {
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        return status == ConnectionResult.SUCCESS
    }


    companion object {
        var TAG = "ACTIVITY NOFITICATION"
        const val CHANNEL_ID = "CHANNEL"
        private const val CHANNEL_NAME = "Simplified Coding"
        private const val CHANNEL_DESC = "Android Push Notification Tutorial"
                ;

    }
}