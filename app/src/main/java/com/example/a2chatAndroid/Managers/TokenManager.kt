package com.example.a2chatAndroid.Managers


class TokenStorage {
    private var token: String? = null

    //getters and setters
    fun getToken(): String? {
        return token
    }

    fun setToken(token: String) {
        this.token = token
    }
}

val TokenManager = TokenStorage()
