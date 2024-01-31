package com.example.projectv11

data class LoginRequest(val username: String, val password: String)


data class LoginResponse(val success: Boolean, val sessionToken: String?)