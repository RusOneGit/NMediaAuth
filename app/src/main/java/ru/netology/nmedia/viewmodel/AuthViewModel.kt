package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.api.PostsApi
import retrofit2.HttpException

class AuthViewModel : ViewModel() {
    val data: LiveData<AuthState> = AppAuth.getInstance()
        .authStateFlow
        .asLiveData(Dispatchers.Default)

    val authenticated: Boolean
        get() = AppAuth.getInstance().authStateFlow.value.id != 0L

    fun authenticate(login: String, password: String, onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = PostsApi.service.authenticate(login, password)
                if (response.isSuccessful) {
                    response.body()?.let { authResponse ->
                        AppAuth.getInstance().setAuth(authResponse.id, authResponse.token)
                        onResult(true)
                    } ?: onResult(false)
                } else {
                    onResult(false)
                }
            } catch (e: HttpException) {
                onResult(false)
            }
        }
    }

    // Метод для выхода из системы
    fun logout() {
        AppAuth.getInstance().removeAuth()
    }
}
