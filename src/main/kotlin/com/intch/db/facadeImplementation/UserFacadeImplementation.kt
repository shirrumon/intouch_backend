package com.intch.db.facadeImplementation

import com.intch.db.facade.UserFacadeDAO
import com.intch.entities.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import com.intch.db.dao.DataBaseDAO.dbQuery
import org.mindrot.jbcrypt.BCrypt

class UserFacadeImplementation : UserFacadeDAO {
    private fun resultRowToUser(row: ResultRow) = UserEntity(
        id = row[UserEntitySchema.id],
        username = row[UserEntitySchema.username],
        password = row[UserEntitySchema.password],
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

    override suspend fun createNewUser(username: String, password: String): UserEntity? = dbQuery {
        val insertStatement = UserEntitySchema.insert {
            it[UserEntitySchema.username] = username
            it[UserEntitySchema.password] = BCrypt.hashpw(password, BCrypt.gensalt())
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