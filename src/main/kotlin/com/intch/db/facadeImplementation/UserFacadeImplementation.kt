package com.intch.db.facadeImplementation

import com.intch.db.facade.UserFacadeDAO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import com.intch.db.dao.DataBaseDAO.dbQuery
import com.intch.entities.user.UserEntity
import com.intch.entities.user.UserEntitySchema
import com.intch.enums.user.UserLoginMethodsEnum
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class UserFacadeImplementation : UserFacadeDAO {
    private fun resultRowToUser(row: ResultRow) = UserEntity(
        id = row[UserEntitySchema.id],
        userUuid = row[UserEntitySchema.userUuid],
        username = row[UserEntitySchema.username],
        password = row[UserEntitySchema.password],
        profileImages = row[UserEntitySchema.profileImages],
        birthday = row[UserEntitySchema.birthday],
        email = row[UserEntitySchema.email],
        jwtToken = row[UserEntitySchema.jwtToken],
        phoneNumber = row[UserEntitySchema.phoneNumber],
        userLoginMethod = row[UserEntitySchema.userLoginMethod],
        userProfileStatus = row[UserEntitySchema.userProfileStatus]
    )

    override suspend fun allUsers(): List<UserEntity> {
        return UserEntitySchema.selectAll().map(::resultRowToUser)
    }

    override suspend fun user(id: Int): UserEntity? = dbQuery {
        UserEntitySchema
            .select { UserEntitySchema.id eq id }
            .map(::resultRowToUser)
            .firstOrNull()
    }

    override suspend fun userByUsername(username: String): UserEntity? = dbQuery {
        UserEntitySchema
            .select { UserEntitySchema.username eq username }
            .map(::resultRowToUser)
            .firstOrNull()
    }

    override suspend fun createNewUser(
        username: String,
        password: String,
        profileImage: String?,
        email: String?,
        phoneNumber: String?,
        userLoginMethod: UserLoginMethodsEnum,
        birthday: String,
        userProfileStatus: String?,
        jwtToken: String?
    ): UserEntity? = dbQuery {
        val insertStatement = UserEntitySchema.insert { insertStatement ->
            insertStatement[UserEntitySchema.username] = username
            insertStatement[userUuid] = UUID.randomUUID().toString()
            insertStatement[profileImages] = profileImage
            insertStatement[UserEntitySchema.email] = email
            insertStatement[UserEntitySchema.phoneNumber] = phoneNumber
            insertStatement[UserEntitySchema.userLoginMethod] = userLoginMethod
            insertStatement[UserEntitySchema.birthday] = birthday
            insertStatement[UserEntitySchema.userProfileStatus] = userProfileStatus
            insertStatement[UserEntitySchema.jwtToken] = jwtToken
            insertStatement[UserEntitySchema.password] = BCrypt.hashpw(password, BCrypt.gensalt())
        }

        insertStatement.resultedValues?.firstOrNull()?.let(::resultRowToUser)
    }

    override suspend fun editUser(id: Int, username: String, password: String): Boolean = dbQuery {
        UserEntitySchema.update({ UserEntitySchema.id eq id }) {
            it[UserEntitySchema.username] = username
            it[UserEntitySchema.password] = BCrypt.hashpw(password, BCrypt.gensalt())
        } > 0
    }

    override suspend fun deleteUser(id: Int): Boolean = dbQuery {
        UserEntitySchema.deleteWhere { UserEntitySchema.id eq id } > 0
    }
}