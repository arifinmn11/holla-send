package com.enigma.application.presentation.activity

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.enigma.application.R
import com.enigma.application.databinding.ActivityMainBinding
import com.enigma.application.utils.Constants.Companion.MENU_HISTORY
import com.enigma.application.utils.Constants.Companion.MENU_HOME
import com.enigma.application.utils.Constants.Companion.MENU_PROFILE
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
    }


    override fun onDestroy() {
        super.onDestroy()
    }


}