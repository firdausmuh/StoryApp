package com.dicoding.storyapp.utils

import UserRepository
import android.content.Context
import com.dicoding.storyapp.pref.UserPreference
import com.dicoding.storyapp.pref.dataStore
import com.dicoding.storyapp.service.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
}