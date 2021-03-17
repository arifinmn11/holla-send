package com.enigma.application.presentation.mytask

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.enigma.application.data.model.mytask.DataItem
import com.enigma.application.data.model.mytask.ResponseMyTasks
import com.enigma.application.databinding.FragmentMyTaskBinding
import com.enigma.application.presentation.activity.ActivityViewModel
import com.enigma.application.utils.component.LoadingDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*
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
        alertDialog = LoadingDialog.build(requireContext())
        binding = FragmentMyTaskBinding.inflate(layoutInflater)
        initViewModel()
        subscribe()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        alertDialog.show()

        binding.apply {
            rvAdapter = MyTaskAdapter(viewModel)
            myTaskList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = rvAdapter
            }

            // on Refresh
            refreshNewTask.setOnRefreshListener {
                alertDialog.show()
                viewModel.getMyTasksApi().observe(requireActivity()) { data ->
                    handleGetApi(data)
                }
            }

            // on Start and change status to PICK UP
            buttonStart.setOnClickListener {
                viewModel.startToPickUpApi().observe(requireActivity()) { data ->
                    handleUpdateApi(data)
                }
            }

            buttonStop.setOnClickListener {

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

        // Live Data on Click Cancel
        viewModel.unAssignTask.observe(this) { data ->
            val dialogView =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.bottom_sheet_dialog, null, false)
            val dialogBuilder = BottomSheetDialog(requireContext())
            dialogBuilder.setContentView(dialogView)
            dialogBuilder.show()
            dialogView.apply {
                title_dialog.text = "Are you sure drop this task?"

                data.destination?.apply {
                    text_receiver.text = this?.name ?: ""
                    text_location.text = this?.address ?: ""
                }
                data.requestBy?.userDetails.apply {
                    text_user.text = "${this?.firstName} ${this?.lastName}"
                }
                data.createDate.apply {
                    text_date.text = this?.substring(0, 10)
                }

                button_positive.setOnClickListener {
                    data?.id?.let { id ->
                        dialogBuilder.dismiss()
                        viewModel.unAssignMyTaskApi(id).observe(this@MyTaskFragment) { res ->
                            when (res?.code) {
                                200 -> {
                                    Toast.makeText(
                                        requireContext(),
                                        "Data has been drop!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    viewModel.getMyTasksApi().observe(this@MyTaskFragment) { data ->
                                        handleGetApi(data)
                                    }
                                }
                                else -> {
                                    viewModel.getMyTasksApi().observe(this@MyTaskFragment) { data ->
                                        handleGetApi(data)
                                    }
                                    Toast.makeText(
                                        requireContext(),
                                        "Something wrong, try again!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
                button_negative.setOnClickListener {
                    dialogBuilder.dismiss()
                }
            }
        }
        // Live Data on Click Done
        viewModel.doneTask.observe(this) { dataItem ->
            val dialogView =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.bottom_sheet_dialog, null, false)
            val dialogBuilder = BottomSheetDialog(requireContext())
            dialogBuilder.setContentView(dialogView)
            dialogBuilder.show()
            dialogView.apply {
                title_dialog.text = "Are you sure to change status to delivered??"

                dataItem.destination?.apply {
                    text_receiver.text = this?.name ?: ""
                    text_location.text = this?.address ?: ""
                }
                dataItem.requestBy?.userDetails.apply {
                    text_user.text = "${this?.firstName} ${this?.lastName}"
                }
                dataItem.createDate.apply {
                    text_date.text = this?.substring(0, 10)
                }

                button_positive.setOnClickListener {
                    dataItem?.id?.let { id ->
                        dialogBuilder.dismiss()
                        viewModel.doneTaskApi(id).observe(this@MyTaskFragment) { res ->
                            Toast.makeText(
                                requireContext(),
                                "$res",
                                Toast.LENGTH_SHORT
                            ).show()
                            when (res?.code) {
                                200 -> {
                                    viewModel.getMyTasksApi().observe(this@MyTaskFragment) { data ->
                                        Toast.makeText(
                                            requireContext(),
                                            "Data has been change!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        handleGetApi(data)
                                    }
                                }
                                else -> {
                                    viewModel.getMyTasksApi().observe(this@MyTaskFragment) { data ->
                                        Toast.makeText(
                                            requireContext(),
                                            "Something wrong, try again!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        handleGetApi(data)
                                    }

                                }
                            }
                        }
                    }

                }
                button_negative.setOnClickListener {
                    dialogBuilder.dismiss()
                }
            }
        }

        // Check Activity ID
        viewModel.getCheckActivityApi().observe(this) { res ->
            res?.code.apply {
                when (this) {
                    200 -> {
                        res?.data?.id?.let {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                            viewModel.setActivityId(it)
                            binding.apply {
                                buttonStart.visibility = View.GONE
                                buttonStop.visibility = View.VISIBLE
                            }
                        }
                    }
                    else -> {
                        binding.apply {
                            buttonStop.visibility = View.GONE
                            buttonStart.visibility = View.VISIBLE
                        }

                    }
                }
            }

        }

        // Get My Task
        viewModel.getMyTasksApi().observe(this) { data ->
            handleGetApi(data)
        }

    }


    fun handleGetApi(data: ResponseMyTasks?) {
        binding.apply {
            data?.code.apply {
                when (this) {
                    200 -> {
                        if (data?.data?.isEmpty() == true) {
                            pageWarning(status = false)
                            refreshNewTask.isRefreshing = false
                            alertDialog.hide()
                            pageWarning(true, 200)
                        } else {

                            data?.data?.apply {
//                            this[0]?.courierActivity?.id?.apply {
//                                btnStartStop.isEnabled = false
//                                btnStartStop.setBackgroundColor(Color.parseColor("#FAFAFA"))
//                                sharedPref.edit()
//                                    .putString(Constans.ACTIVITY_ID, this)
//                                    .apply()
//                            }

                                pageWarning(status = false)
                                refreshNewTask.isRefreshing = false
                                alertDialog.hide()
                                rvAdapter.setView(this as List<DataItem>)
                                totalTask.text = this.size.toString()

                                if (this.isEmpty())
                                    pageWarning(true, 200)
                            }
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

    fun handleUpdateApi(data: ResponseMyTasks?) {
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