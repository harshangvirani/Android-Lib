package com.example.android.libs.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.libs.domain.model.Course
import com.example.android.libs.domain.repository.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val repository: CourseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CourseUiState>(CourseUiState.Loading)
    val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    init {
        observeCourses()
        refreshCourses()
    }

    /**
     * Observes the cached list of courses from the database.
     * Launches a coroutine to collect updates from the repository's Flow,
     * translating database updates directly into UI state updates.
     */
    private fun observeCourses() {
        viewModelScope.launch {
            repository.getCoursesFlow()
                .catch { exception ->
                    _uiState.value = CourseUiState.Error(exception.message ?: "Unknown local database error")
                }
                .collect { courses ->
                    _uiState.value = CourseUiState.Success(courses)
                }
        }
    }

    /**
     * Refreshes courses by triggering remote network API synchronization.
     * Launches a coroutine to call the repository's recursive suspending sync function.
     */
    fun refreshCourses() {
        viewModelScope.launch {
            val currentVal = _uiState.value
            if (currentVal !is CourseUiState.Success || currentVal.courses.isEmpty()) {
                _uiState.value = CourseUiState.Loading
            }

            // Start recursive fetch from page 1
            repository.fetchAndSaveCoursesRecursive(page = 1)
                .onSuccess { courses ->
                    _uiState.value = CourseUiState.Success(courses)
                }
                .onFailure { exception ->
                    val currentState = _uiState.value
                    if (currentState !is CourseUiState.Success || currentState.courses.isEmpty()) {
                        _uiState.value = CourseUiState.Error(
                            exception.message ?: "Failed to fetch courses recursively from API"
                        )
                    }
                }
        }
    }
}

sealed interface CourseUiState {
    object Loading : CourseUiState
    data class Success(val courses: List<Course>) : CourseUiState
    data class Error(val message: String) : CourseUiState
}
