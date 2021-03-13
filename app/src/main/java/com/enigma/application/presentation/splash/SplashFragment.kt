package com.enigma.application.presentation.splash

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enigma.application.R
import com.enigma.application.databinding.FragmentSplashBinding
import com.enigma.application.presentation.activity.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    lateinit var binding: FragmentSplashBinding
    lateinit var viewModel: SplashViewModel
    lateinit var activityViewModel: ActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        subscribe()
        binding = FragmentSplashBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)
    }

    fun subscribe() {
        viewModel.checkToken().observe(this) {
            Log.d("MASUK", "$it")
            activityViewModel.setBottomVisibility(false)
            when (it?.code) {
                200 -> {
                    activityViewModel.setBottomVisibility(true)
                    findNavController().navigate(R.id.action_global_homeFragment)
                }
                401 -> findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                400 -> findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                else -> findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SplashFragment()
    }
}