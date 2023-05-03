package com.intch.db.facade

import com.intch.entities.UserEntity

interface UserFacadeDAO {
    suspend fun allUsers(): List<UserEntity>
    suspend fun user(id: Int): UserEntity?
    suspend fun userByUsername(username: String): UserEntity?
    suspend fun createNewUser(username: String, password: String): UserEntity?
    suspend fun editUser(id: Int, username: String, password: String): Boolean
    suspend fun deleteUser(id: Int): Boolean
}