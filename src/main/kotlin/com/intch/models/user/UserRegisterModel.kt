package com.intch.models.user

import com.intch.enums.user.UserLoginMethodsEnum

data class UserRegisterModel(
    val username: String,
    val password: String,
    val profileImages: String?,
    val email: String?,
    val phoneNumber: String?,
    val userLoginMethod: UserLoginMethodsEnum,
    val birthday: String
)
