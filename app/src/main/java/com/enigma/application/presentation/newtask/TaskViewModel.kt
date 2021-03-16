package com.enigma.application.presentation.newtask

import android.util.Log
import androidx.lifecycle.*
import com.enigma.application.data.model.newtask.ResponseNewTaskWaiting
import com.enigma.application.data.model.newtask.put.ResponseAddTaskToMyTask
import com.enigma.application.data.repository.NewTaskRepository
import com.enigma.application.di.qualifier.PutAddToMyTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    @PutAddToMyTask
    val repository: NewTaskRepository
) :
    ViewModel(), TaskOnClickListener {
    private var listSelected = mutableListOf<String>()

    private var _selectedTasks = MutableLiveData<List<String>>()

    private var _listenAdd = MutableLiveData<String>()

    val getTaskSelected: LiveData<List<String>>
        get() {
            return _selectedTasks
        }

    val putListenAdd: LiveData<String>
        get() {
            return _listenAdd
        }

    val getTaskAdd: LiveData<String>
        get() {
            return _listenAdd
        }

    private fun addSelectedTask(data: String) {
        listSelected.add(data)
        _selectedTasks.postValue(listSelected)
    }

    private fun setPutTask(data: String) {
        _listenAdd.postValue(data)
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

    //     handle click : send task
    fun sendToMyTaskApi(id: String) =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            withTimeout(10000) {
                var response: ResponseAddTaskToMyTask? = null
                try {
                    response = repository.addTaskToMyTask(id)
                } catch (e: Exception) {
                    response =
                        ResponseAddTaskToMyTask(
                            code = 400,
                            data = null,
                            message = "Something wrong with your connection!",
                        )
                } finally {
                    emit(response)
                }
            }
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

    override fun onClick(id: String) {
        Log.d("THIS IS ON CLICK!", id)
        setPutTask(id)
    }

}