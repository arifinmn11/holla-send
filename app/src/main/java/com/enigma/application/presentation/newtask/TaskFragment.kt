package com.enigma.application.presentation.newtask

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.enigma.application.R
import com.enigma.application.data.model.newtask.DataItem
import com.enigma.application.data.model.newtask.ResponseNewTaskWaiting
import com.enigma.application.databinding.FragmentTaskBinding
import com.enigma.application.presentation.activity.ActivityViewModel
import com.enigma.application.utils.Constans
import com.enigma.application.utils.component.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_task.*

@AndroidEntryPoint
class TaskFragment : Fragment() {
    lateinit var binding: FragmentTaskBinding
    lateinit var viewModel: TaskViewModel
    lateinit var activityViewModel: ActivityViewModel
    lateinit var rvAdapter: TaskAdapter
    lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        subscribe()
        alertDialog = LoadingDialog.build(requireContext())
        binding = FragmentTaskBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.apply {

            rvAdapter = TaskAdapter(viewModel)
            taskList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = rvAdapter
            }

            // first time opened menu
            viewModel.getNewTasksApi().observe(requireActivity()) {
                alertDialog.show()
                handleGetApi(it)
            }

            // on Refresh
            refreshNewTask.setOnRefreshListener {
                alertDialog.show()
                viewModel.getNewTasksApi().observe(requireActivity()) {
                    handleGetApi(it)
                }
            }


            btnAddTask.setOnClickListener {
//                viewModel.getData()
            }

            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        return binding.root
    }

    fun initViewModel() {
        viewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)
    }

    fun subscribe() {
        activityViewModel.setBottomVisibility(false)
        viewModel.getTaskSelected.observe(this) {
            binding.apply {
                if (it.size > 0) {
                    selectedTask.text = it.size.toString()
                    selectedTask.visibility = View.VISIBLE
                    btnAddTask.visibility = View.VISIBLE
                    refreshNewTask.setPadding(0, 0, 0, 140)
                    refreshNewTask.isEnabled = false
                } else {
                    selectedTask.visibility = View.GONE
                    btnAddTask.visibility = View.GONE
                    refreshNewTask.setPadding(0, 0, 0, 0)
                    refreshNewTask.isEnabled = true
                }
            }
        }

    }

    fun handleGetApi(data: ResponseNewTaskWaiting?) {
        binding.apply {
            data?.code.apply {
                when (this) {
                    200 -> data?.data.apply {
                        Log.d("DATAS", "$this")
                        pageWarning(status = false)
                        refreshNewTask.isRefreshing = false
                        alertDialog.hide()
                        rvAdapter.setView(this as List<DataItem>)
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
        fun newInstance() = TaskFragment
    }
}