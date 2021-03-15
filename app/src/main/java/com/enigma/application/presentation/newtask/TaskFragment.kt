package com.enigma.application.presentation.newtask

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.enigma.application.R
import com.enigma.application.databinding.FragmentTaskBinding
import com.enigma.application.presentation.activity.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_task.*

@AndroidEntryPoint
class TaskFragment : Fragment() {
    lateinit var binding: FragmentTaskBinding
    lateinit var viewModel: TaskViewModel
    lateinit var activityViewModel: ActivityViewModel
    lateinit var rvAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        subscribe()
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
            val datas: List<String> =
                arrayListOf("Test1", "Test2", "Test3", "Test4", "Test5", "Test6")
            rvAdapter.setView(datas)

            btnAddTask.setOnClickListener {
                viewModel.getData()
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
            if (it.size > 0) {
                selected_task.text = it.size.toString()
                selected_task.visibility = View.VISIBLE
                btnAddTask.visibility = View.VISIBLE
                refresh_home.setPadding(0, 0, 0, 140)
            } else {
                selected_task.visibility = View.GONE
                btnAddTask.visibility = View.GONE
                refresh_home.setPadding(0, 0, 0, 0)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = TaskFragment
    }
}