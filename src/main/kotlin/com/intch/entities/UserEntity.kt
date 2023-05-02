package com.intch.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    @SerialName("username")
    val userName: String,
    @SerialName("password")
    val password: String
)
