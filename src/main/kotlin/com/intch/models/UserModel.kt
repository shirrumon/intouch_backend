package com.intch.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String
)
