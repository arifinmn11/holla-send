package com.enigma.application.presentation.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.enigma.application.R
import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.data.repository.AuthRepositoryImpl
import com.enigma.application.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    lateinit var binding: FragmentSplashBinding
    lateinit var viewModel: SplashViewModel

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
//        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                val getRepo = AuthRepositoryImpl()
//                return SplashViewModel(getRepo) as T
//            }
//
//        }).get(SplashViewModel::class.java)
    }

    fun subscribe() {
        viewModel.postAuth().observe(this) {
            when (it?.status) {
                200 -> findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                401 -> findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                400 -> findNavController().navigate(R.id.action_splashFragment_to_welcomeFragment)
                else -> findNavController().navigate(R.id.action_splashFragment_to_welcomeFragment)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SplashFragment()
    }
}