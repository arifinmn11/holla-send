package com.enigma.application.presentation.mytask

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.enigma.application.R
import com.enigma.application.data.model.mytask.ResponseMyTask
import com.enigma.application.data.model.mytask.DataItem
import com.enigma.application.databinding.FragmentMyTaskBinding
import com.enigma.application.presentation.activity.ActivityViewModel
import com.enigma.application.utils.Constans
import com.enigma.application.utils.component.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyTaskFragment : Fragment() {
    private lateinit var binding: FragmentMyTaskBinding
    lateinit var viewModel: MyTaskViewModel
    lateinit var activityViewModel: ActivityViewModel
    lateinit var alertDialog: AlertDialog
    lateinit var rvAdapter: MyTaskAdapter

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        subscribe()
        alertDialog = LoadingDialog.build(requireContext())
        binding = FragmentMyTaskBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.apply {
            alertDialog.show()
            rvAdapter = MyTaskAdapter(viewModel)

            myTaskList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = rvAdapter
            }

            // on Refresh
            refreshNewTask.setOnRefreshListener {
                alertDialog.show()
                viewModel.getMyTasksApi().observe(requireActivity()) {
                    handleGetApi(it)
                }
            }

            // on Start and change status to PICK UP
            btnStartStop.setOnClickListener {
                viewModel.startToPickUpApi().observe(requireActivity()) {
                    handleUpdateApi(it)
                }
            }

            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        return binding.root
    }

    fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MyTaskViewModel::class.java)
        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)
    }

    fun subscribe() {
        activityViewModel.setBottomVisibility(false)
        viewModel.getMyTasksApi().observe(this) {
            handleGetApi(it)
        }
        viewModel.unAssignTask.observe(this) {
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
        }
        viewModel.doneTask.observe(this) {
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
        }
    }



    fun handleGetApi(data: ResponseMyTask?) {
        binding.apply {
            data?.code.apply {
                when (this) {
                    200 -> {
                        if (data?.data?.isEmpty() == true) {

                        }

                        data?.data?.apply {
                            this[0]?.courierActivity?.id?.apply {
                                btnStartStop.isEnabled = false
                                btnStartStop.setBackgroundColor(Color.parseColor("#FAFAFA"))
                                sharedPref.edit()
                                    .putString(Constans.ACTIVITY_ID, this)
                                    .apply()
                            }

                            pageWarning(status = false)
                            refreshNewTask.isRefreshing = false
                            alertDialog.hide()
                            rvAdapter.setView(this as List<DataItem>)
                            totalTask.text = this.size.toString()

                            if (this.isEmpty())
                                pageWarning(true, 200)
                        }
                    }

                    404 -> {
                        refreshNewTask.isRefreshing = false
                        pageWarning(true, 404)
                        alertDialog.hide()
                        Toast.makeText(
                            requireContext(),
                            "Your token is expired!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        refreshNewTask.isRefreshing = false
                        pageWarning(true, 500)
                        alertDialog.hide()
                        Toast.makeText(
                            requireContext(),
                            "Something wrong with your connection!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    fun handleUpdateApi(data: ResponseMyTask?) {
        data?.code.apply {
            when (this) {
                200 -> data?.data.apply {

                }
                400 -> {
                    pageWarning(true, 404)
                    alertDialog.hide()
                    Toast.makeText(
                        requireContext(),
                        "Your token is expired!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    pageWarning(true, 500)
                    alertDialog.hide()
                    Toast.makeText(
                        requireContext(),
                        "Something wrong with your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    fun pageWarning(status: Boolean, error: Int? = 0) {
        binding.apply {
            notificationAlert.visibility = View.GONE
            messageAlert.visibility = View.GONE
            logoAlert.visibility = View.GONE
            if (status) {
                notificationAlert.visibility = View.VISIBLE
                messageAlert.visibility = View.VISIBLE
                logoAlert.visibility = View.VISIBLE
            }

            val clearData = arrayListOf<DataItem>()
            when (error) {
                404 -> {
                    rvAdapter.setView(clearData)
                    logoAlert.setImageResource(R.drawable.ic_undraw_authentication_fsn5)
                    messageAlert.text = "Please relogin, your token is expired!"
                }

                200 -> {
                    logoAlert.setImageResource(R.drawable.ic_undraw_no_data_re_kwbl)
                    messageAlert.text = "Task not available!"
                }

                else -> {
                    rvAdapter.setView(clearData)
                    logoAlert.setImageResource(R.drawable.ic_undraw_notify_re_65on)
                    messageAlert.text = "No Internet Connection!"
                }
            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyTaskFragment()
    }
}