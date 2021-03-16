package com.enigma.application.presentation.mytask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
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
    fun getMyTaskApi() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        withTimeout(10000) {
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

    override fun onClickAction(id: String) {
        TODO("Not yet implemented")
    }

    override fun onClickDetail(id: String) {
        TODO("Not yet implemented")
    }
}