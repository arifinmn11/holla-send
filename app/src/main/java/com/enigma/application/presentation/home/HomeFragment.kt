package com.enigma.application.presentation.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enigma.application.R
import com.enigma.application.databinding.FragmentHomeBinding
import com.enigma.application.presentation.activity.ActivityViewModel
import com.enigma.application.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    lateinit var activityViewModel: ActivityViewModel

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        subscribe()
        binding = FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    activity?.finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.apply {
            activityViewModel.setBottomVisibility(true)

            refreshHome.setOnRefreshListener {
                subscribe()
            }

            cvMyTask.setOnClickListener {
                findNavController().navigate(R.id.action_global_myTaskFragment)
            }

            cvNewTask.setOnClickListener {
                findNavController().navigate(R.id.action_global_taskFragment)
            }

        }
        return binding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)
    }

    private fun subscribe() {
        viewModel.getDashboard().observe(requireActivity()) { res ->
            res?.code.apply {
                when (this) {
                    200 -> {
                        binding.apply {
                            refreshHome.isRefreshing = false
                            assignedCount.text = res?.data?.assigned.toString()
                            pickupCount.text = res?.data?.pickedUp.toString()
                            deliveredCount.text = res?.data?.delivered.toString()
                            newTaskCount.text = res?.data?.waiting.toString()
                        }
                    }
                    401 -> {
                        binding.apply {
                            refreshHome.isRefreshing = false
                            sharedPref.edit()
                                .putString(Constants.TOKEN, "")
                                .apply()
                            findNavController().navigate(R.id.action_global_loginFragment)
                        }
                    }
                    else -> {
                        binding.refreshHome.isRefreshing = false
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}