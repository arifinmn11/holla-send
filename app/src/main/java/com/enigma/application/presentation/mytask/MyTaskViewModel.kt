package com.enigma.application.presentation.mytask

import android.util.Log
import androidx.lifecycle.*
import com.enigma.application.data.model.mytask.DataItem
import com.enigma.application.data.model.mytask.ResponseMyTask
import com.enigma.application.data.model.mytask.ResponseMyTasks
import com.enigma.application.data.repository.MyTaskRepository
import com.enigma.application.di.qualifier.GetMyTaskUnFinished
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import javax.inject.Inject


@HiltViewModel
class MyTaskViewModel @Inject constructor(@GetMyTaskUnFinished val repository: MyTaskRepository) :
    ViewModel(), MyTaskOnClickListener {

    private var _doneTask = MutableLiveData<DataItem>()
    private var _unAssignTask = MutableLiveData<DataItem>()

    val doneTask: LiveData<DataItem>
        get() {
            return _doneTask
        }

    val unAssignTask: LiveData<DataItem>
        get() {
            return _unAssignTask
        }


    fun getMyTasksApi() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        withTimeout(5000) {
            var response: ResponseMyTasks? = null
            try {
                response = repository.getMyTask()
            } catch (e: Exception) {
                Log.d("EXCEPTION", "$e")
                response =
                    ResponseMyTasks(
                        code = 400,
                        data = null,
                        message = "Something wrong with your connection!",
                    )
            } finally {
                emit(response)
            }
        }
    }

    fun startToPickUpApi() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        withTimeout(5000) {
            var response: ResponseMyTasks? = null
            try {
                response = repository.postTaskToPickUp()
            } catch (e: Exception) {
                response =
                    ResponseMyTasks(
                        code = 500,
                        data = null,
                        message = "Something wrong with your connection!",
                    )
            } finally {
                emit(response)
            }
        }
    }

    fun unAssignMyTaskApi(id: String) = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        withTimeout(5000) {
            var response: ResponseMyTask? = null
            try {
                response = repository.putTaskToUnAssign(id)
            } catch (e: Exception) {
                Log.d("EXCEPTION", "$e")
                response =
                    ResponseMyTask(
                        code = 500,
                        data = null,
                        message = "Something wrong with your connection!",
                    )
            } finally {
                emit(response)
            }
        }
    }

    fun doneTaskApi(id: String) = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        withTimeout(5000) {
            var response: ResponseMyTask? = null
            try {
                response = repository.putTaskToDone(id)
            } catch (e: Exception) {
                response =
                    ResponseMyTask(
                        code = 500,
                        data = null,
                        message = "Something wrong with your connection!",
                    )
            } finally {
                emit(response)
            }
        }
    }

    private fun setUnAssign(data: DataItem) {
        _unAssignTask.postValue(data)
    }

    private fun setDone(data: DataItem) {
        _doneTask.postValue(data)
    }

    override fun onClickUnAssign(data: DataItem) {
        setUnAssign(data)
    }

    override fun onClickDetail(data: DataItem) {
        TODO("Not yet implemented")
    }

    override fun onClickDone(data: DataItem) {
        setDone(data)
    }
}