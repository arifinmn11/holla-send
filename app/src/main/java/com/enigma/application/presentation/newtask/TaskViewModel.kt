package com.enigma.application.presentation.newtask

import android.util.Log
import androidx.lifecycle.*
import com.enigma.application.data.model.newtask.ResponseNewTaskWaiting
import com.enigma.application.data.model.register.RequestRegister
import com.enigma.application.data.model.register.ResponseRegister
import com.enigma.application.data.repository.NewTaskRepository
import com.enigma.application.di.qualifier.GetNewTaskWaiting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
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
    }

    fun getData() {
        getTaskSelected.value?.let { Log.d("DATA : ", "$it") }
    }

    private fun unSelectedTask(data: String) {
        val index = listSelected.indexOf(data)
        if (index != null) {
            listSelected.removeAt(index)
            _selectedTasks.postValue(listSelected)
        }
    }

    // handle refresh : clear selected item
    fun refreshHandle() {
        listSelected = mutableListOf<String>()
    }

    // Get New Tasks
    fun getNewTasksApi() =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            withTimeout(10000) {
                var response: ResponseNewTaskWaiting? = null
                try {
                    response = repository.getAllTaskWaiting()
                } catch (e: Exception) {
                    Log.d("exception api", "$e")
                    response =
                        ResponseNewTaskWaiting(
                            code = 400,
                            data = null,
                            message = "Something wrong with your connection!",
                        )
                } finally {
                    emit(response)
                }

            }
        }

    override fun onSelected(data: String) {
        addSelectedTask(data)
    }

    override fun onUnSelected(data: String) {
        unSelectedTask(data)
    }

    override fun onClick(id: Int) {
    }

}