package com.enigma.application.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.enigma.application.R
import com.enigma.application.databinding.ActivityMainBinding
import com.enigma.application.utils.Constans.Companion.MENU_HISTORY
import com.enigma.application.utils.Constans.Companion.MENU_HOME
import com.enigma.application.utils.Constans.Companion.MENU_MAP
import com.enigma.application.utils.Constans.Companion.MENU_TASK
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        initViewModel()
        subscribe()
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.apply {

            bottomNavigation.setOnNavigationItemSelectedListener {
                when ("$it") {
                    MENU_HISTORY -> navHostFragment.findNavController()
                        .navigate(R.id.action_global_historyFragment)
                    MENU_MAP -> navHostFragment.findNavController()
                        .navigate(R.id.action_global_mapFragment)
                    MENU_HOME -> navHostFragment.findNavController()
                        .navigate(R.id.action_global_homeFragment)
                    MENU_TASK -> navHostFragment.findNavController()
                        .navigate(R.id.action_global_taskFragment)
                }
                true
            }
        }


        setContentView(binding.root)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ActivityViewModel::class.java)
    }


    fun subscribe() {
        viewModel.bottomVisibility.observe(this) {
            val maxHeight = binding.layoutActivity.height
            binding.navHostFragment.minimumHeight = maxHeight
            binding.bottomNavigation.visibility = it
        }
    }
}