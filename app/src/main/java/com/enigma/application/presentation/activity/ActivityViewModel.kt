package com.enigma.application.presentation.activity

import android.location.Location
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor() : ViewModel() {
    private val _bottomVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)

    val bottomVisibility: LiveData<Int> = _bottomVisibility

    fun setBottomVisibility(boolean: Boolean) {
        if (!boolean) {
            _bottomVisibility.postValue(View.GONE)
        } else {
            _bottomVisibility.postValue(View.VISIBLE)
        }
    }
}