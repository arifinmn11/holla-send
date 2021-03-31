package com.enigma.application.presentation.splash

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enigma.application.R
import com.enigma.application.databinding.FragmentSplashBinding
import com.enigma.application.presentation.activity.ActivityViewModel
import com.enigma.application.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {
    lateinit var binding: FragmentSplashBinding
    lateinit var viewModel: SplashViewModel
    lateinit var activityViewModel: ActivityViewModel

    @Inject
    lateinit var sharedPref: SharedPreferences

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
        viewModel.checkToken().observe(this) { res ->
            activityViewModel.setBottomVisibility(false)
            when (res?.code) {
                200 -> {
                    viewModel.getRadius().observe(this) { res ->
                        when(res?.code) {
                            200 -> {
                                res?.data?.let {
                                    sharedPref.edit()
                                        .putInt(Constants.MIN_RADIUS, it)
                                        .apply()
                                    activityViewModel.setBottomVisibility(true)
                                    findNavController().navigate(R.id.action_global_homeFragment)
                                }
                            }
                        }
                    }
                }
                404 -> {
                    sharedPref.edit()
                        .putString(Constants.TOKEN, "")
                        .apply()
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
                401 -> {
                    sharedPref.edit()
                        .putString(Constants.TOKEN, "")
                        .apply()
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
                else -> {
                    Toast.makeText(
                        requireActivity(),
                        "Something wrong with your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SplashFragment()
    }
}