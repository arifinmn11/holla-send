package com.enigma.application.presentation.newtask

import android.view.View

interface TaskOnClickListener {
    fun onSelected(id: String)
    fun onUnSelected(id: String)
    fun onClick(id: String)
}