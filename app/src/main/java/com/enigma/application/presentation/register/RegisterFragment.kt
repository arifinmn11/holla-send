package com.enigma.application.presentation.register

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enigma.application.R
import com.enigma.application.data.model.register.RequestRegister
import com.enigma.application.data.model.register.User
import com.enigma.application.data.model.register.UserDetails
import com.enigma.application.databinding.FragmentRegisterBinding
import com.enigma.application.utils.Constants.Companion.VALIDATION_ADDRESS
import com.enigma.application.utils.Constants.Companion.VALIDATION_CONFIRM_PASSWORD
import com.enigma.application.utils.Constants.Companion.VALIDATION_EMAIL
import com.enigma.application.utils.Constants.Companion.VALIDATION_FIRSTNAME
import com.enigma.application.utils.Constants.Companion.VALIDATION_IDENTIFICATION
import com.enigma.application.utils.Constants.Companion.VALIDATION_LASTNAME
import com.enigma.application.utils.Constants.Companion.VALIDATION_NO_IDENTIFICATION
import com.enigma.application.utils.Constants.Companion.VALIDATION_PASSWORD
import com.enigma.application.utils.Constants.Companion.VALIDATION_SUCCESS
import com.enigma.application.utils.Constants.Companion.VALIDATION_USERNAME
import com.enigma.application.utils.component.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*

@AndroidEntryPoint
class RegisterFragment : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var binding: FragmentRegisterBinding
    lateinit var viewModel: RegisterViewModel
    lateinit var loadingDialog: AlertDialog

    private var identity: String = "KTP"
    private var registration: RequestRegister? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        subscribe()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        loadingDialog = LoadingDialog.build(requireContext())
        binding = FragmentRegisterBinding.inflate(layoutInflater)


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    findNavController().navigate(R.id.action_global_loginFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.apply {
            loginText.setOnClickListener {
                findNavController().navigate(R.id.action_global_loginFragment)
            }

            signUpButton.setOnClickListener {
                val email = etEmail.text.toString()
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()
                val passwordConfirm = etPasswordConfirm.text.toString()
                val firstName = etFirstName.text.toString()
                val lastName = etLastName.text.toString()
                val address = etContact.text.toString()
                val contactNumber = etIdentificationNumber.text.toString()

                registration = RequestRegister(
                    User(password, email, username),
                    UserDetails(firstName, lastName, identity, address, contactNumber)
                )

                viewModel.checkValidation(
                    email,
                    username,
                    password,
                    passwordConfirm,
                    firstName,
                    lastName,
                    identity,
                    contactNumber,
                    address
                )
            }

            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.identity,
                android.R.layout.simple_spinner_dropdown_item
            )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerIdentity.adapter = adapter
            spinnerIdentity.onItemSelectedListener = this@RegisterFragment

        }
        return binding.root
    }

    fun initViewModel() {
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun subscribe() {
        viewModel.getValidation().observe(this) { res ->
            etUsername.background = resources.getDrawable(R.drawable.radius_edit_text)
            etEmail.background = resources.getDrawable(R.drawable.radius_edit_text)
            etPassword.background = resources.getDrawable(R.drawable.radius_edit_text)
            etPasswordConfirm.background = resources.getDrawable(R.drawable.radius_edit_text)
            etFirstName.background = resources.getDrawable(R.drawable.radius_edit_text)
            etLastName.background = resources.getDrawable(R.drawable.radius_edit_text)
            etIdentificationNumber.background = resources.getDrawable(R.drawable.radius_edit_text)
            etContact.background = resources.getDrawable(R.drawable.radius_edit_text)

            when (res.status) {
                VALIDATION_SUCCESS -> {
                    loadingDialog.show()
                    registration?.let { register ->
                        viewModel.postRegister(register).observe(this) {
                            when (it?.code) {
                                200 -> {
                                    loadingDialog.hide()
                                    dialog()
                                }
                                500 -> {
                                    loadingDialog.hide()
                                    Toast.makeText(
                                        requireContext(),
                                        "Password or Username invalid!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                else -> {
                                    loadingDialog.hide()
                                    Toast.makeText(
                                        requireContext(),
                                        "Error, Something wrong!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }

                VALIDATION_USERNAME -> {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    etUsername.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                VALIDATION_EMAIL -> {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    etEmail.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                VALIDATION_PASSWORD -> {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    etPassword.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                VALIDATION_CONFIRM_PASSWORD -> {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    etPasswordConfirm.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                VALIDATION_FIRSTNAME -> {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    etFirstName.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                VALIDATION_LASTNAME -> {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    etLastName.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                VALIDATION_IDENTIFICATION -> {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    spinnerIdentity.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                VALIDATION_NO_IDENTIFICATION -> {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    etIdentificationNumber.background =
                        resources.getDrawable(R.drawable.error_edit_text)
                }
                VALIDATION_ADDRESS -> {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    etContact.background = resources.getDrawable(R.drawable.error_edit_text)
                }
            }
        }
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }

    // alert dialog
    private fun dialog() {
        AlertDialog.Builder(requireContext())
            // Title
            .setTitle("Account has been created!!")
            .setNeutralButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                findNavController().navigate(R.id.action_global_loginFragment)
            })
            .setCancelable(false)
            .show()
    }

    // Spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        identity = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}