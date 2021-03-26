package com.enigma.application.presentation.mytask

import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.enigma.application.data.model.courier_activity.get.ResponsePickUp
import com.enigma.application.data.model.courier_activty.ResponseCourierActivity
import com.enigma.application.data.model.mytask.DataItem
import com.enigma.application.data.model.mytask.ResponseMyTask
import com.enigma.application.data.model.mytask.ResponseMyTasks
import com.enigma.application.data.repository.CourierActivityRepository
import com.enigma.application.data.repository.MyTaskRepository
import com.enigma.application.di.qualifier.CourierActivity
import com.enigma.application.di.qualifier.GetMyTaskUnFinished
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import javax.inject.Inject


@HiltViewModel
class MyTaskViewModel @Inject constructor(
    @GetMyTaskUnFinished val repository: MyTaskRepository,
    @CourierActivity val repoCourier: CourierActivityRepository
) :
    ViewModel(), MyTaskOnClickListener {

    private var _doneTask = MutableLiveData<DataItem>()
    private var _unAssignTask = MutableLiveData<DataItem>()
    private var _activityId = MutableLiveData<String>()
    private var _locationGps = MutableLiveData<Location>()

    val activityId: LiveData<String>
        get() {
            return _activityId
        }

    val getLocation: LiveData<Location>
        get() {
            return _locationGps
        }

    val doneTask: LiveData<DataItem>
        get() {
            return _doneTask
        }

    val unAssignTask: LiveData<DataItem>
        get() {
            return _unAssignTask
        }

    fun getCheckActivityApi() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        withTimeout(5000) {
            var response: ResponseCourierActivity? = null
            try {
                response = repoCourier.getActivity()
            } catch (e: Exception) {
                Log.d("EXCEPTION", "$e")
                response =
                    ResponseCourierActivity(
                        code = 400,
                        data = null,
                        message = "Something wrong with your connection!",
                    )
            } finally {
                emit(response)
            }
        }
    }

    fun putDoneActivityApi() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        withTimeout(5000) {
            var response: ResponseCourierActivity? = null
            try {
                response = repoCourier.putActivity()
            } catch (e: Exception) {
                response =
                    ResponseCourierActivity(
                        code = 400,
                        data = null,
                        message = "Something wrong with your connection!",
                    )
            } finally {
                response?.message?.let { Log.d("ERROR REPO", it) }
                emit(response)
            }
        }
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
            var response: ResponsePickUp? = null
            try {
                response = repository.postTaskToPickUp()
            } catch (e: Exception) {
                Log.d("ERROR PICKUP", e.toString())
                response =
                    ResponsePickUp(
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

    fun setLocationGps(location: Location) {
        _locationGps.postValue(location)
    }


    fun setActivityId(id: String) {
        _activityId.postValue(id)
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