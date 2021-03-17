package com.enigma.application.presentation.mytask

import androidx.lifecycle.*
import com.enigma.application.data.model.mytask.ResponseMyTask
import com.enigma.application.data.repository.MyTaskRepository
import com.enigma.application.di.qualifier.GetMyTaskUnFinished
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import javax.inject.Inject


@HiltViewModel
class MyTaskViewModel @Inject constructor(@GetMyTaskUnFinished val repository: MyTaskRepository) :
    ViewModel(), MyTaskOnClickListener {

    private var _doneTask = MutableLiveData<String>()
    private var _unAssignTask = MutableLiveData<String>()

    val doneTask: LiveData<String>
        get() {
            return _doneTask
        }

    val unAssignTask: LiveData<String>
        get() {
            return _unAssignTask
        }


    fun getMyTasksApi() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        withTimeout(5000) {
            var response: ResponseMyTask? = null
            try {
                response = repository.getMyTask()
            } catch (e: Exception) {
                response =
                    ResponseMyTask(
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
            var response: ResponseMyTask? = null
            try {
                response = repository.postTaskToPickUp()
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

    fun unAssignMyTaskApi(id: String) = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        withTimeout(5000) {
            var response: ResponseMyTask? = null
            try {
                response = repository.putTaskToUnAssign(id)
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

    private fun setUnAssign(id: String) {
        _unAssignTask.postValue(id)
    }

    private fun setDone(id: String) {
        _doneTask.postValue(id)
    }

    override fun onClickUnAssign(id: String) {
        setUnAssign(id)
    }

    override fun onClickDetail(id: String) {
        TODO("Not yet implemented")
    }

    override fun onClickDone(id: String) {
        setDone(id)
    }
}