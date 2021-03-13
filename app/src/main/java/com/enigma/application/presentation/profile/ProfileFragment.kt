package com.enigma.application.presentation.profile

import android.Manifest
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.enigma.application.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding

    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.apply {
            permissionGps()
            gpsButton.setOnClickListener {

            }
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


    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }


}