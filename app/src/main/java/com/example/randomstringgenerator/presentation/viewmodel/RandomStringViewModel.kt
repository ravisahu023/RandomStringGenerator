package com.example.randomstringgenerator.presentation.viewmodel

import android.util.Log
import androidx.compose.ui.util.fastFilter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomstringgenerator.data.repository.RandomStringRepository
import com.example.randomstringgenerator.domain.model.RandomStringItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomStringViewModel @Inject constructor(
    private val repository: RandomStringRepository
) : ViewModel() {

    private var _randomStrings = MutableStateFlow<List<RandomStringItem>>(emptyList())
    val randomStrings: StateFlow<List<RandomStringItem>> = _randomStrings

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentJob: Job? = null

    fun generateString(length: Int) {

        // Cancel any ongoing request
        currentJob?.cancel()

        currentJob = viewModelScope.launch {
            _isLoading.value = true
            try {
                val item = repository.safeQueryWithRetry(length, 2)
                item?.let {
                    _randomStrings.value += it
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Failed to fetch: ${e.localizedMessage}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteItem(item: RandomStringItem) {
        _randomStrings.value -= item
    }

    fun clearAll() {
        _randomStrings.value = emptyList()
    }

    fun filterByFav() {
        _randomStrings.value = randomStrings.value.filter {
            it.isFavourite
        }
        _randomStrings.update {
            _randomStrings.value.filter { it.isFavourite }
        }
    }

    fun markFavourite(item: RandomStringItem) {
        randomStrings.value.forEach {
            if (it.value == item.value) {
                //found that item
                it.isFavourite = !item.isFavourite
            }
        }
    }


}