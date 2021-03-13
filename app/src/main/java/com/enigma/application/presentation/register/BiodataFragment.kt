package com.enigma.application.presentation.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.enigma.application.R
import com.enigma.application.databinding.FragmentBiodataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BiodataFragment : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var binding: FragmentBiodataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBiodataBinding.inflate(layoutInflater)
        binding.apply {

            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.identity,
                android.R.layout.simple_spinner_dropdown_item
            )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerIdentity.adapter = adapter
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = BiodataFragment()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val text: String = p0?.getItemAtPosition(p2).toString()
        Log.d("text", "$text")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}