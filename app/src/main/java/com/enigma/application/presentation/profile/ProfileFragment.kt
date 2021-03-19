package com.enigma.application.presentation.profile

import android.Manifest
import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enigma.application.R
import com.enigma.application.databinding.FragmentProfileBinding
import com.enigma.application.presentation.activity.ActivityViewModel
import com.enigma.application.utils.Constants
import com.enigma.application.utils.component.LoadingDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var viewModel: ViewModelProfile
    lateinit var activityViewModel: ActivityViewModel
    lateinit var alertDialog: AlertDialog


    private var firstName: String = ""
    private var lastname: String = ""
    private var username: String = ""

    private var locationManager: LocationManager? = null

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()

        firstName = sharedPreferences.getString(Constants.FIRST_NAME, "").toString()
        lastname = sharedPreferences.getString(Constants.LAST_NAME, "").toString()
        username = sharedPreferences.getString(Constants.USERNAME, "").toString()

        alertDialog = LoadingDialog.build(requireContext())
        binding = FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.apply {
            permissionGps()

            refreshProfile.setOnRefreshListener {
                viewModel.profileApi().observe(requireActivity()) { res ->
                    alertDialog.show()
                    res?.code.apply {
                        when (this) {
                            200 -> {
                                refreshProfile.isRefreshing = false
                                alertDialog.hide()
                                nameProfile.text =
                                    "${res?.data?.userDetails?.firstName} ${res?.data?.userDetails?.lastName}"
                                usernameProfile.text =
                                    "@${res?.data?.username}"
                            }

                            401 -> {
                                refreshProfile.isRefreshing = false
                                alertDialog.hide()
                                Toast.makeText(
                                    requireContext(),
                                    "Your token is invalid!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                sharedPreferences.edit()
                                    .putString(Constants.TOKEN, "")
                                    .apply()
                                findNavController().navigate(R.id.action_global_loginFragment)
                            }
                            400 -> {
                                refreshProfile.isRefreshing = false
                                alertDialog.hide()
                                Toast.makeText(
                                    requireContext(),
                                    "${res?.data}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            else -> {
                                refreshProfile.isRefreshing = false
                                Toast.makeText(
                                    requireContext(),
                                    "Something wrong try again!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                alertDialog.hide()
                            }
                        }
                    }
                }
            }

            buttonLogout.setOnClickListener {
                val dialogView =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.bottom_sheet_logout, null, false)
                val dialogBuilder = BottomSheetDialog(requireContext())
                dialogBuilder.setContentView(dialogView)
                dialogBuilder.show()
                dialogView.apply {
                    button_positive.setOnClickListener {
                        sharedPreferences.edit()
                            .putString(Constants.TOKEN, "")
                            .apply()
                        activityViewModel.setBottomVisibility(false)
                        findNavController().navigate(R.id.action_global_loginFragment)
                        dialogBuilder.dismiss()
                    }

                    button_negative.setOnClickListener {
                        dialogBuilder.dismiss()
                    }

                }

            }

            nameProfile.text = "$firstName $lastname"
            usernameProfile.text = "@$username"
        }

        return binding.root
    }

    fun permissionGps() {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) GlobalScope.launch(Dispatchers.IO) {
            } else {
                Log.d("RECONFIG", "CPONFIG")
            }
        }.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ViewModelProfile::class.java)
        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }


}