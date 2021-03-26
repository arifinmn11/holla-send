package com.enigma.application.presentation.activity

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enigma.application.utils.Constants.Companion.MENU_HOME
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor() : ViewModel() {
    private val _bottomVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)
    private val _bottomNav: MutableLiveData<String> = MutableLiveData(MENU_HOME)

    val bottomVisibility: LiveData<Int> = _bottomVisibility
    val bottomNav: MutableLiveData<String> = _bottomNav

    fun setBottomVisibility(boolean: Boolean) {
        if (!boolean) {
            _bottomVisibility.postValue(View.GONE)
        } else {
            _bottomVisibility.postValue(View.VISIBLE)
        }
    }

    fun setBottomNav(menu: String) {
        _bottomNav.postValue(menu)
    }
}