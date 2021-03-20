package com.enigma.application.presentation.activity

import android.content.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import com.enigma.application.R
import com.enigma.application.databinding.ActivityMainBinding
import com.enigma.application.utils.Constants.Companion.MENU_HISTORY
import com.enigma.application.utils.Constants.Companion.MENU_HOME
import com.enigma.application.utils.Constants.Companion.MENU_PROFILE
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
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

        if (intent.extras != null) {
            for(key in intent.extras!!.keySet()) {
               Log.d(TAG, intent.extras!!.getString(key).toString())
            }
        }

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
                    Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
                })
            // [END retrieve_current_token]
        } else {
            //You won't be able to send notifications to this device
            Log.w("TAG", "Device doesn't have google play services")
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            messageReceiver,
            IntentFilter("MyData")
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            intent.extras?.getString("message")?.let { Log.d("KEY", it) }
        }
    }

    private fun checkGooglePlayServices(): Boolean {
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        return if (status != ConnectionResult.SUCCESS) {
            Log.e("ERROR", "Error")
            // ask user to update google play services.
            false
        } else {
            Log.i("Etest", "Google play services updated")
            true
        }
    }

    companion object {
        var TAG = "ACTIVITY NOFITICATION"
    }
}