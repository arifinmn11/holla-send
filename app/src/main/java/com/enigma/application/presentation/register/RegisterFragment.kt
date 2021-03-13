package com.enigma.application.presentation.register

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enigma.application.R
import com.enigma.application.data.model.register.RequestRegister
import com.enigma.application.data.model.register.User
import com.enigma.application.data.model.register.UserDetails
import com.enigma.application.databinding.FragmentRegisterBinding
import com.enigma.application.utils.Constans
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
        binding.apply {


            signUpButton.setOnClickListener {
                val email = etEmail.text.toString()
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()
                val passwordConfirm = etPasswordConfirm.text.toString()
                val firstName = etFirstName.text.toString()
                val lastName = etLastName.text.toString()
                val address = etAddress.text.toString()
                val noIdentification = etIdentificationNumber.text.toString()

                registration = RequestRegister(
                    User(password, email, username),
                    UserDetails(firstName, lastName, identity, address, noIdentification)
                )

                viewModel.checkValidation(
                    email,
                    username,
                    password,
                    passwordConfirm,
                    firstName,
                    lastName,
                    identity,
                    noIdentification,
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

    fun subscribe() {
        viewModel.getValidation().observe(this) {
            etUsername.background = resources.getDrawable(R.drawable.radius_edit_text)
            etEmail.background = resources.getDrawable(R.drawable.radius_edit_text)
            etPassword.background = resources.getDrawable(R.drawable.radius_edit_text)
            etPasswordConfirm.background = resources.getDrawable(R.drawable.radius_edit_text)
            etFirstName.background = resources.getDrawable(R.drawable.radius_edit_text)
            etLastName.background = resources.getDrawable(R.drawable.radius_edit_text)
            etIdentificationNumber.background = resources.getDrawable(R.drawable.radius_edit_text)
            etAddress.background = resources.getDrawable(R.drawable.radius_edit_text)

            when (it.status) {
                Constans.VALIDATION_SUCCESS -> {
                    registration?.let { register ->
                        viewModel.postRegister(register).observe(this) {
                            loadingDialog.show()

                            when (it?.code) {
                                200 -> {
                                    loadingDialog.hide()
                                    dialog()
                                }
                                500 -> {
                                    loadingDialog.hide()
                                    Toast.makeText(
                                        requireContext(),
                                        "Password or Usernameis Invalid!",
                                        Toast.LENGTH_SHORT
                                    ).show()

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
                Constans.VALIDATION_USERNAME -> {
                    etUsername.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_EMAIL -> {
                    etEmail.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_PASSWORD -> {
                    etPassword.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_CONFIRM_PASSWORD -> {
                    etPasswordConfirm.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_FIRSTNAME -> {
                    etFirstName.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_LASTNAME -> {
                    etLastName.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_IDENTIFICATION -> {
                    spinnerIdentity.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_NO_IDENTIFICATION -> {
                    etIdentificationNumber.background =
                        resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_ADDRESS -> {
                    etAddress.background = resources.getDrawable(R.drawable.error_edit_text)
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
            .setNeutralButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                findNavController().navigate(R.id.action_global_loginFragment)
            })
            .show()
    }


    // Spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        identity = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}