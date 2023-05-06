package com.intch.enums.user

enum class UserLoginMethodsEnum(val method: String = "username") {
    PHONE("phoneNumber"),
    USERNAME("username"),
    EMAIL("email")
}