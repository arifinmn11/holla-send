package com.enigma.application

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.enigma.application.utils.Constans

class MainActivity : AppCompatActivity() {

    lateinit var sharedpreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        sharedpreferences = getSharedPreferences(Constans.PREREFERENCES, Context.MODE_PRIVATE);

    }
}