package com.enigma.application.presentation.history

import android.util.Log
import androidx.lifecycle.*
import com.enigma.application.data.model.history.ResponseHistory
import com.enigma.application.data.repository.HistoryRepository
import com.enigma.application.di.qualifier.History
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(@History val repository: HistoryRepository) :
    ViewModel(), HistoryOnClickListener {

    private var _pageLiveData = MutableLiveData<Int>(1)

    val pageLiveData: LiveData<Int>
        get() {
            return _pageLiveData
        }

    fun onNextPage() {
        _pageLiveData.postValue(_pageLiveData.value?.toInt()?.plus(1))
    }

    fun onPrevPage() {
        if (_pageLiveData.value?.toInt()!! > 1)
            _pageLiveData.postValue(_pageLiveData.value?.toInt()?.minus(1))
    }

    fun getHistoryApi(page: Int, limit: Int) =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            withTimeout(5000) {
                var response: ResponseHistory? = null
                try {
                    response = repository.getHistory(page, limit)
                } catch (e: Exception) {
                    Log.d("EXCEPTION", "$e")
                    response =
                        ResponseHistory(
                            code = 400,
                            data = null,
                            message = "Something wrong with your connection!",
                        )
                } finally {
                    emit(response)
                }
            }
        }

}