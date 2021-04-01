package com.enigma.application.presentation.mytask

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.enigma.application.R
import com.enigma.application.data.model.mytask.DataItem
import com.enigma.application.data.model.mytask.ResponseMyTasks
import com.enigma.application.databinding.FragmentMyTaskBinding
import com.enigma.application.presentation.activity.ActivityViewModel
import com.enigma.application.utils.Constants
import com.enigma.application.utils.component.GpsUtils
import com.enigma.application.utils.component.LoadingDialog
import com.google.android.gms.location.*
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
    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private var locationManager: LocationManager? = null

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var isContinue = true
    private var isGPS = false

    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alertDialog = LoadingDialog.build(requireContext())
        binding = FragmentMyTaskBinding.inflate(layoutInflater)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        locationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(10000L)
        locationRequest.setFastestInterval(5000L)

        initViewModel()
        subscribe()
        subscribeButton()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        alertDialog.show()

        // call function to active GPS
        buildAlertMessageNoGps()
        updateLocation()

        binding.apply {
            getActivityApi()

            rvAdapter = MyTaskAdapter(viewModel)
            myTaskList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = rvAdapter
                alertDialog.hide()
            }

            // on Refresh
            refreshNewTask.setOnRefreshListener {
                alertDialog.show()
                getActivityApi()
            }

            val callback: OnBackPressedCallback =
                object : OnBackPressedCallback(true /* enabled by default */) {
                    override fun handleOnBackPressed() {
                        // Handle the back button event
                        isContinue = false
                        findNavController().navigate(R.id.action_global_homeFragment)
                    }
                }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

            // on Start and change status to PICK UP
            buttonStart.setOnClickListener {
                alertDialog.show()
                val dialogView =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.bottom_sheet_done_confirmation, null, false)
                val dialogBuilder = BottomSheetDialog(requireContext())
                dialogBuilder.setContentView(dialogView)
                dialogBuilder.show()
                dialogView.apply {
                    button_positive.setOnClickListener {
                        alertDialog.show()
                        viewModel.startToPickUpApi().observe(requireActivity()) { res ->
                            res?.code.apply {
                                when (this) {
                                    200 -> {
                                        Toast.makeText(
                                            requireContext(),
                                            "Change status to pick up!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        alertDialog.hide()
                                        subscribe()
                                    }
                                    else -> {
                                        Toast.makeText(
                                            requireContext(),
                                            "Sorry something error!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        alertDialog.hide()
                                    }
                                }
                            }

                        }
                        dialogBuilder.dismiss()
                    }

                    button_negative.setOnClickListener {
                        alertDialog.hide()
                        dialogBuilder.dismiss()
                    }
                }

            }

            // Stop activity!
            buttonStop.setOnClickListener {
                val dialogView =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.bottom_sheet_done_confirmation, null, false)
                val dialogBuilder = BottomSheetDialog(requireContext())
                dialogBuilder.setContentView(dialogView)
                dialogBuilder.show()
                dialogView.apply {
                    button_positive.setOnClickListener {
                        alertDialog.show()
                        viewModel.putDoneActivityApi().observe(requireActivity()) { res ->
                            res?.code.apply {
                                when (this) {
                                    200 -> {
                                        Toast.makeText(
                                            requireContext(),
                                            "My task has been delivered!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        subscribe()
                                        alertDialog.hide()
                                    }
                                    500 -> {
                                        Toast.makeText(
                                            requireContext(),
                                            "Remember, you have a task!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        buttonStart.visibility = View.VISIBLE
                                        subscribe()
                                        alertDialog.hide()
                                    }
                                    else -> {
                                        Toast.makeText(
                                            requireContext(),
                                            "${res?.code}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Toast.makeText(
                                            requireContext(),
                                            "Sorry something error!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        alertDialog.hide()
                                    }
                                }
                            }

                        }

                        dialogBuilder.dismiss()
                    }

                    button_negative.setOnClickListener {
                        dialogBuilder.dismiss()

                    }
                }

            }

            // Back to home
            buttonBack.setOnClickListener {
                findNavController().navigate(R.id.action_global_homeFragment)
            }
        }
        return binding.root
    }

    fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MyTaskViewModel::class.java)
        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)
    }

    private fun subscribe() {
        activityViewModel.setBottomVisibility(false)

        // Get My Task
        fun getMyTask() = viewModel.getMyTasksApi().observe(requireActivity()) { data ->
            handleGetApi(data)
        }
        // Check Activity ID
        viewModel.getCheckActivityApi().observe(requireActivity()) { res ->
            res?.code.apply {
                when (this) {
                    200 -> {
                        res?.data?.id?.let {
                            viewModel.setActivityId(it)
                            binding.apply {
                                buttonStart.visibility = View.GONE
                                buttonStop.visibility = View.VISIBLE
                                getMyTask()
                            }
                        }
                    }
                    else -> {
                        binding.apply {
                            buttonStop.visibility = View.GONE
                            buttonStart.visibility = View.VISIBLE
                            getMyTask()
                        }

                    }
                }
            }

        }

        //Get Map Location Nav
        viewModel.getLocationMap.observe(requireActivity()) {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=" + it.destination?.lat + "," + it.destination?.lon + "&mode=l")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    private fun subscribeButton() {
        // Live Data on Click Cancel
        viewModel.unAssignTask.observe(requireActivity()) { data ->
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
                        viewModel.unAssignMyTaskApi(id).observe(requireActivity()) { res ->
                            when (res?.code) {
                                200 -> {
                                    Toast.makeText(
                                        requireContext(),
                                        "Data has been drop!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    viewModel.getMyTasksApi().observe(requireActivity()) { data ->
                                        handleGetApi(data)
                                    }
                                    dialogBuilder.dismiss()
                                }
                                else -> {
                                    viewModel.getMyTasksApi().observe(requireActivity()) { data ->
                                        handleGetApi(data)
                                    }
                                    Toast.makeText(
                                        requireContext(),
                                        "Something wrong, try again!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dialogBuilder.dismiss()
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
        viewModel.doneTask.observe(requireParentFragment()) { dataItem ->
            alertDialog.hide()
            getCurrentLocation()

            if (isLocationEnabled()) {
                val dialogView =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.bottom_sheet_dialog, null, false)
                val dialogBuilder = BottomSheetDialog(requireContext())
                dialogBuilder.setContentView(dialogView)
                dialogBuilder.show()
                dialogView.apply {
                    val destination = Location("${dataItem.destination?.name}")
                    destination.latitude = dataItem?.destination?.lat!!
                    destination.longitude = dataItem?.destination?.lon!!
                    button_positive.isEnabled = false
                    button_positive.text = "Loading..."
                    button_positive.setBackgroundColor(resources.getColor(R.color.hintColor))

                    viewModel.getLocation.observe(requireActivity()) {
                        if (it.distanceTo(destination) < sharedPref.getInt(
                                Constants.MIN_RADIUS,
                                100
                            )
                        ) {
                            button_positive.isEnabled = true
                            button_positive.text = it.distanceTo(destination).toString()
                            button_positive.backgroundTintList =
                                ColorStateList.valueOf(resources.getColor(R.color.primary_700))
                            button_positive.text = "Done"
                            validation_location.text = "In Radius"

                        } else {
                            validation_location.text =
                                "Out of Radius (Distance to location %.2f km)".format(
                                    it.distanceTo(
                                        destination
                                    ) / 1000
                                )
                            button_positive.isEnabled = false
                            button_positive.text = "Out of Radius"
                            button_positive.backgroundTintList =
                                ColorStateList.valueOf(resources.getColor(R.color.hintColor))
                        }
                    }

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
                            viewModel.doneTaskApi(id).observe(requireActivity()) { res ->
                                when (res?.code) {
                                    200 -> {
                                        viewModel.getMyTasksApi()
                                            .observe(requireActivity()) { data ->
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Data has been change!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                handleGetApi(data)
                                                dialogBuilder.dismiss()
                                            }
                                    }
                                    else -> {
                                        viewModel.getMyTasksApi()
                                            .observe(requireActivity()) { data ->
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Something wrong, try again!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                handleGetApi(data)
                                                dialogBuilder.dismiss()
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

        }
    }

    private fun handleGetApi(data: ResponseMyTasks?) {
        binding.apply {
            data?.code.apply {
                when (this) {
                    200 -> {
                        if (data?.data?.isEmpty() == true) {
                            refreshNewTask.isRefreshing = false
                            alertDialog.hide()
                            rvAdapter.setView(arrayListOf())
                            pageWarning(true, 200)

                        } else {
                            data?.data?.apply {
                                refreshNewTask.isRefreshing = false
                                alertDialog.hide()
                                pageWarning(status = false)

                                rvAdapter.setView(this as List<DataItem>)
                                totalTask.text = this.size.toString()

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

    private fun getActivityApi() =
        viewModel.getCheckActivityApi().observe(requireActivity()) { res ->
            binding.apply {
                alertDialog.hide()
                when (res?.code) {
                    200 -> {
                        alertDialog.hide()
                        buttonStart.visibility = View.GONE
                        buttonStop.visibility = View.VISIBLE
                        viewModel.getMyTasksApi().observe(requireActivity()) { data ->
                            handleGetApi(data)
                        }
                    }
                    404 -> {
                        alertDialog.hide()
                        buttonStart.visibility = View.VISIBLE
                        buttonStop.visibility = View.GONE
                        viewModel.getMyTasksApi().observe(requireActivity()) { data ->
                            handleGetApi(data)
                        }
                    }
                    else -> {
                        alertDialog.hide()
                        Log.d("SOMETHING WRONG!", "$res")
                    }
                }
            }
        }

    private fun pageWarning(status: Boolean, error: Int? = 0) {
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
                    messageAlert.text = "My Task not available!"
                    buttonStart.visibility = View.GONE
                }


                else -> {
                    rvAdapter.setView(clearData)
                    logoAlert.setImageResource(R.drawable.ic_undraw_notify_re_65on)
                    messageAlert.text = "No Internet Connection!"
                }
            }

        }
    }

    // Get location now!
    private fun getCurrentLocation() {
        // checking location permission
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // request permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE
            );
            return
        }

        try {
            if (isLocationEnabled()) {
                if (isContinue) {
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        null
                    )
                } else {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location ->

                            // getting the last known or current location
                            if (location != null) {
                                latitude = location.latitude
                                longitude = location.longitude
                            }

                            Toast.makeText(
                                requireContext(), "$latitude | $longitude",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                requireContext(), "Failed on getting current location",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            } else {
                buildAlertMessageNoGps()
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireActivity(),
                "Something wrong, make sure your Gps is active!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Handle GPS
    protected fun isLocationEnabled(): Boolean {
        locationManager =
            (requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager)!!
        if (!locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            alertDialog.hide()


        return locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // Handle permission for active gps location
    private fun buildAlertMessageNoGps() {
        GpsUtils(requireActivity()).turnGPSOn(object : GpsUtils.onGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                // turn on GPS
                isGPS = isGPSEnable
            }
        })
    }

    // Handle update location gps
    private fun updateLocation() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {
                        if (isContinue) {
                            viewModel.setLocationGps(location)
                        }
                        if (!isContinue && fusedLocationClient != null) {
                            fusedLocationClient.removeLocationUpdates(locationCallback)
                        }
                    }
                }
            }
        }

    }

    // Handle permission Location
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission granted
                } else {
                    // permission denied
                    Toast.makeText(
                        requireContext(), "You need to grant permission to access location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyTaskFragment()
    }
}