package com.intch.db.facade

import com.intch.entities.user.UserEntity
import com.intch.enums.user.UserLoginMethodsEnum


interface UserFacadeDAO {
    suspend fun allUsers(): List<UserEntity>
    suspend fun user(id: Int): UserEntity?
    suspend fun userByUsername(username: String): UserEntity?
    suspend fun createNewUser(
        username: String,
        password: String,
        profileImage: String? = null,
        email: String?,
        phoneNumber: String?,
        userLoginMethod: UserLoginMethodsEnum,
        birthday: String,
        userProfileStatus: String? = null,
        jwtToken: String? = null
    ): UserEntity?

    suspend fun editUser(id: Int, username: String, password: String): Boolean
    suspend fun deleteUser(id: Int): Boolean
}