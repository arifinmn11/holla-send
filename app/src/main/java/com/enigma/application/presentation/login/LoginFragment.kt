package com.enigma.application.presentation.login

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enigma.application.R
import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.databinding.FragmentLoginBinding
import com.enigma.application.presentation.activity.ActivityViewModel
import com.enigma.application.utils.Constants
import com.enigma.application.utils.component.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    lateinit var viewModel: LoginViewModel
    lateinit var activityModel: ActivityViewModel
    lateinit var loadingDialog: AlertDialog

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        subscribe()
        loadingDialog = LoadingDialog.build(requireContext())
        binding = FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityModel.setBottomVisibility(false)
        binding.apply {
            signInButton.setOnClickListener {

                val username = etUsername.text.toString()
                val password = etPassword.text.toString()

                viewModel.validation(RequestAuth(username = username, password = password))
            }

            createAccButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {

                    // Handle the back button event
                    activity?.finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return binding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        activityModel = ViewModelProvider(this).get(ActivityViewModel::class.java)
    }

    private fun subscribe() {
        viewModel.getValidation().observe(this) {
            Log.d("STATUS", "$it")

            binding.apply {
                if (!it.username)
                    etUsername.background = resources.getDrawable(R.drawable.radius_edit_text)
                else
                    etUsername.background = resources.getDrawable(R.drawable.error_edit_text)

                if (!it.password)
                    etPassword.background = resources.getDrawable(R.drawable.radius_edit_text)
                else
                    etPassword.background = resources.getDrawable(R.drawable.error_edit_text)

                if (!it.username && !it.password) {
                    loadingDialog.show()

                    val username = etUsername.text.toString()
                    val password = etPassword.text.toString()

                    viewModel.postAuth(RequestAuth(username = username, password = password))
                        .observe(requireActivity()) { response ->
                            loadingDialog.hide()

                            when (response?.code) {
                                200 -> {
                                    if (response.data?.role == "COURIER") {
                                        sharedPref.edit()
                                            .putString(Constants.TOKEN, "${response.data?.token}")
                                            .apply()
                                        viewModel.getProfile().observe(requireActivity()) { res ->
                                            res?.code?.apply {
                                                when (this) {
                                                    200 -> {
                                                        sharedPref.edit()
                                                            .putString(
                                                                Constants.FIRST_NAME,
                                                                "${res?.data?.userDetails?.firstName}"
                                                            )
                                                            .putString(
                                                                Constants.LAST_NAME,
                                                                "${res?.data?.userDetails?.lastName}"
                                                            )
                                                            .putString(
                                                                Constants.USERNAME,
                                                                "${res?.data?.username}"
                                                            )
                                                            .putString(
                                                                Constants.IDENTIFICATION_CATEGORY,
                                                                "${res?.data?.userDetails?.identityCategory}"
                                                            )
                                                            .putString(
                                                                Constants.IDENTIFICATION_NUMBER,
                                                                "${res?.data?.userDetails?.identificationNumber}"
                                                            ).apply()
                                                        findNavController().navigate(R.id.action_global_homeFragment)
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "This application only for courier!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    loadingDialog.hide()
                                }
                                500 -> {
                                    loadingDialog.hide()
                                    Toast.makeText(
                                        requireContext(),
                                        "There isn't an account for this username!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                400 -> {
                                    loadingDialog.hide()
                                    Toast.makeText(
                                        requireContext(),
                                        "Incorrect password.!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    sharedPref.edit()
                                        .putString(Constants.TOKEN, "Bearer ")
                                        .apply()
                                    loadingDialog.hide()
                                    Toast.makeText(
                                        requireContext(),
                                        "Incorrect username and / or password.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                }
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}