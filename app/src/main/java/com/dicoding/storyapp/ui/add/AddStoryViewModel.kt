package com.dicoding.storyapp.ui.add

import UserRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.pref.UserModel
import com.dicoding.storyapp.service.response.AddStoryResponse
import com.dicoding.storyapp.utils.Event
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {
    val uploadResponse: LiveData<AddStoryResponse> = repository.uploadResponse
    val isLoading: LiveData<Boolean> = repository.isLoading
    val toastText: LiveData<Event<String>> = repository.toastText

    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            repository.uploadStory(token, file, description)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession()
    }
}