package com.lihan.gamecenterstore.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lihan.gamecenterstore.TaitoStore
import com.lihan.gamecenterstore.data.GameCenterRepository
import com.lihan.gamecenterstore.domain.model.GameCenterStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: GameCenterRepository
)  : ViewModel(){

    private val _dataStatus = MutableStateFlow<Resource>(Resource.Loading)
    val dataStatus = _dataStatus

    sealed class Resource{
        object Loading : Resource()
        class Success(val data : List<GameCenterStore>) : Resource()
        class Fail(val data: List<GameCenterStore> = arrayListOf() , val message : String ) : Resource()
    }
    init {
        getData()
    }



    private fun getData() {
        _dataStatus.value = Resource.Loading
        viewModelScope.launch {
            repository.getAllGameCenter().collectLatest { stores ->
                if (stores.isEmpty()) {
                    _dataStatus.value = Resource.Fail(listOf(), "Error")
                } else {
                    _dataStatus.value = Resource.Success(stores)
                }
            }

        }
    }

}
