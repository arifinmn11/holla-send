package com.enigma.application.presentation.newtask

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enigma.application.data.repository.NewTaskRepository
import com.enigma.application.di.qualifier.GetNewTaskWaiting
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(@GetNewTaskWaiting val repository: NewTaskRepository) :
    ViewModel(), TaskOnClickListener {
    private var listSelected = mutableListOf<String>()

    private var _selectedTasks = MutableLiveData<List<String>>()

    val getTaskSelected: LiveData<List<String>>
        get() {
            return _selectedTasks
        }

    private fun addSelectedTask(data: String) {
        listSelected.add(data)
        _selectedTasks.postValue(listSelected)
        getData()
    }

    fun getData() {
        getTaskSelected.value?.let { Log.d("DATA : ", "$it") }
    }

    private fun unSelectedTask(data: String) {
        val index = listSelected.indexOf(data)
        if (index != null) {
            listSelected.removeAt(index)
            _selectedTasks.postValue(listSelected)
            getData()
        }
    }

    override fun onSelected(data: String) {
        addSelectedTask(data)
    }

    override fun onUnSelected(data: String) {
        unSelectedTask(data)
    }

}