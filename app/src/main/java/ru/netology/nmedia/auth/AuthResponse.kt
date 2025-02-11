package ru.netology.nmedia.auth

data class AuthResponse(
        val id: Long,
        val token: String,
        val error: String? = null // Поле для хранения сообщений об ошибках
    )
