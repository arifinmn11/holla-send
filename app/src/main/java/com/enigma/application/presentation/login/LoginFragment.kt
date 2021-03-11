package com.enigma.application.presentation.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enigma.application.R
import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.databinding.FragmentLoginBinding
import com.enigma.application.presentation.activity.ActivityViewModel
import com.enigma.application.presentation.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    lateinit var viewModel: LoginViewModel
    lateinit var activityModel: ActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        subscribe()
        binding = FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.apply {
            signInButton.setOnClickListener {

                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                viewModel.validation(RequestAuth(email = email, password = password))

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
                if (!it.email)
                    etEmail.background = resources.getDrawable(R.drawable.radius_edit_text)
                else
                    etEmail.background = resources.getDrawable(R.drawable.error_edit_text)

                if (!it.password)
                    etPassword.background = resources.getDrawable(R.drawable.radius_edit_text)
                else
                    etPassword.background = resources.getDrawable(R.drawable.error_edit_text)

                if (!it.email && !it.password) {
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()

                    viewModel.postAuth(RequestAuth(email = email, password = password))
                        .observe(requireActivity()) {
                            when (it?.code) {
                                200 -> {
                                    activityModel.setBottomVisibility(true)
                                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                                }
                                401 -> {
                                    Toast.makeText(
                                        requireContext(),
                                        "Password or Email invalid!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                400 -> Toast.makeText(
                                    requireContext(),
                                    "Password or Email invalid!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                else -> Toast.makeText(
                                    requireContext(),
                                    "Password or Email invalid!",
                                    Toast.LENGTH_SHORT
                                ).show()
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