package com.enigma.application.presentation.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.enigma.application.R
import com.enigma.application.databinding.FragmentHistoryBinding
import com.enigma.application.presentation.activity.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    lateinit var viewModel: HistoryViewModel
    lateinit var activityViewModel: ActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()

        binding = FragmentHistoryBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding.apply {
            activityViewModel.setBottomVisibility(false)

            buttonNext.setOnClickListener {

            }

            buttonForward.setOnClickListener {

            }


        }

        return binding.root
    }

    fun initViewModel() {
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)
    }

    companion object {

        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}