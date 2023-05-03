package com.intch.services.user

import com.intch.db.facadeImplementation.UserFacadeImplementation
import com.intch.entities.UserEntity
import com.intch.models.UserModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class UserAuthService {
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun checkUserWhenLogin(user: UserModel): UserEntity? {
        val requestedUser = UserFacadeImplementation().userByUsername(user.username)

        if(requestedUser != null && BCrypt.checkpw(user.password, requestedUser.password)) {
            return requestedUser
        }

        return null
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun registerUser(user: UserModel) {
        GlobalScope.launch {
            UserFacadeImplementation().createNewUser(
                user.username,
                user.password
            )
        }
    }
}