package com.enigma.application.presentation.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.enigma.application.R
import com.enigma.application.databinding.ActivityMainBinding
import com.enigma.application.utils.Constants.Companion.MENU_HISTORY
import com.enigma.application.utils.Constants.Companion.MENU_HOME
import com.enigma.application.utils.Constants.Companion.MENU_PROFILE
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
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
        firebaseMessaging()
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.apply {
            bottomNavigation.setOnNavigationItemSelectedListener {
                enableButton()
                handleBottomMenu(it.toString())
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

    private fun handleBottomMenu(it: String) {
        binding.apply {
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
                else -> {

                }
            }
        }
    }


    fun subscribe() {
        viewModel.bottomVisibility.observe(this) {
            val maxHeight = binding.layoutActivity.height
            binding.navHostFragment.minimumHeight = maxHeight
            binding.bottomNavigation.visibility = it
        }

        viewModel.bottomNav.observe(this) {
            binding.apply {
                enableButton()
                when (it) {
                    MENU_HOME -> {
                        bottomNavigation.menu.getItem(0)
                            .isEnabled = false
                        bottomNavigation.menu.getItem(0)
                            .isChecked = false
                    }
                    MENU_HISTORY -> {
                        bottomNavigation.menu.getItem(1).isEnabled = false

                        bottomNavigation.menu.getItem(1)
                            .isChecked = false
                    }
                    MENU_PROFILE -> {
                        bottomNavigation.menu.getItem(2).isEnabled = false

                        bottomNavigation.menu.getItem(2)
                            .isChecked = false
                    }
                    else -> {

                    }
                }
            }
        }
    }


    fun firebaseMessaging() {
        FirebaseMessaging.getInstance().subscribeToTopic("NewTask")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Something wrong!"
                }
                Log.d("TAG", msg)
            }
    }
}