package com.enigma.application.presentation.history

import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.enigma.application.R
import com.enigma.application.data.model.history.ListItem
import com.enigma.application.databinding.FragmentHistoryBinding
import com.enigma.application.presentation.activity.ActivityViewModel
import com.enigma.application.presentation.mytask.MyTaskAdapter
import com.enigma.application.utils.Constants
import com.enigma.application.utils.component.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    lateinit var viewModel: HistoryViewModel
    lateinit var activityViewModel: ActivityViewModel
    lateinit var rvAdapter: HistoryAdapter
    lateinit var alertDialog: AlertDialog

    @Inject
    lateinit var sharedPref: SharedPreferences

    private var page = 1;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        subscribe()

        alertDialog = LoadingDialog.build(requireContext())
        binding = FragmentHistoryBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding.apply {

            historySubscribe(page)

            refreshNewTask.setOnRefreshListener {
                historySubscribe(page)
            }

//            activityViewModel.setBottomVisibility(false)
            rvAdapter = HistoryAdapter(viewModel)
            myHistoryList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = rvAdapter
            }


            buttonNext.setOnClickListener {
                viewModel.onNextPage()
            }

            buttonForward.setOnClickListener {
                viewModel.onPrevPage()
            }

        }

        return binding.root
    }

    fun initViewModel() {
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)
    }

    fun subscribe() {
        viewModel.pageLiveData.observe(this) {
            page = it
            historySubscribe(page)
        }
    }

    fun historySubscribe(page: Int) {
        alertDialog.show()
        viewModel.getHistoryApi(page - 1, 5).observe(requireActivity()) { res ->

            res?.code.apply {
                when (this) {
                    200 -> {
                        res?.data?.let {
                            alertDialog.hide()
                            rvAdapter.setView(it.list as List<ListItem>)

                            val total = it.total!!
                            val size = (page * 5)
                            val limit = (total.toFloat() / 5).roundToInt()

                            binding.apply {
                                refreshNewTask.isRefreshing = false
                                pageText.text = "Page $page of ${(limit ?: 0)}"
                                totalText.text = "Total : ($total items)"

                                if (size < total) {
                                    buttonNext.isEnabled = true
                                    buttonNext.setBackgroundTintList(
                                        ColorStateList.valueOf(
                                            resources.getColor(R.color.white)
                                        )
                                    );

                                } else {
                                    buttonNext.isEnabled = false
                                    buttonNext.setBackgroundTintList(
                                        ColorStateList.valueOf(
                                            resources.getColor(R.color.hintColor)
                                        )
                                    );
                                }

                                if (page > 1) {
                                    buttonForward.isEnabled = true
                                    buttonForward.setBackgroundTintList(
                                        ColorStateList.valueOf(
                                            resources.getColor(R.color.white)
                                        )
                                    )
                                } else {
                                    buttonForward.isEnabled = false
                                    buttonForward.setBackgroundTintList(
                                        ColorStateList.valueOf(
                                            resources.getColor(R.color.hintColor)
                                        )
                                    )
                                }
                            }
                        }
                    }
                    401 -> {
                        binding.apply {
                            refreshNewTask.isRefreshing = false
                            sharedPref.edit()
                                .putString(Constants.TOKEN, "")
                                .apply()
                            findNavController().navigate(R.id.action_global_loginFragment)
                        }
                    }
                    else -> {
                        binding.apply {
                            refreshNewTask.isRefreshing = false
                            sharedPref.edit()
                                .putString(Constants.TOKEN, "")
                                .apply()
                            findNavController().navigate(R.id.action_global_loginFragment)
                        }

                    }
                }
            }

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}