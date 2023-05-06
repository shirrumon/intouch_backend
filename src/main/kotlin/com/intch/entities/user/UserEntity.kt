package com.intch.entities.user

import com.intch.enums.user.UserLoginMethodsEnum
import org.jetbrains.exposed.sql.*

data class UserEntity(
    val id: Int,
    val userUuid: String,
    val username: String,
    val password: String,
    val profileImages: String?,
    val email: String?,
    val phoneNumber: String?,
    val userLoginMethod: UserLoginMethodsEnum,
    val birthday: String,
    val userProfileStatus: String?,
    val jwtToken: String?
)

object UserEntitySchema : Table() {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 128).uniqueIndex()
    val userUuid = varchar("user_uuid", 128).uniqueIndex()
    val password = varchar("password", 1024)
    val profileImages = text("profile_images").nullable()
    val email = varchar("email", 255)
        .uniqueIndex()
        .nullable()
    val phoneNumber = varchar("phone_number", 255)
        .uniqueIndex()
        .nullable()
    val userLoginMethod = enumeration<UserLoginMethodsEnum>("user_login_method")
    val birthday = varchar("birthday", 255)
    val userProfileStatus = varchar("user_profile_status", 255).nullable()
    val jwtToken = varchar("jwt_token", 1024).nullable()

    override val primaryKey = PrimaryKey(id)
}