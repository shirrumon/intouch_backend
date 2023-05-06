package com.intch.services.user

import com.intch.db.facadeImplementation.UserFacadeImplementation
import com.intch.entities.user.UserEntity
import com.intch.models.user.UserModel
import com.intch.models.user.UserRegisterModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class UserAuthService {
    suspend fun checkUserWhenLogin(user: UserModel): UserEntity? {
        val requestedUser = UserFacadeImplementation().userByUsername(user.username)

        if (requestedUser != null && BCrypt.checkpw(user.password, requestedUser.password)) {
            return requestedUser
        }

        return null
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun registerUser(userRegisterModel: UserRegisterModel) {
        GlobalScope.launch {
            UserFacadeImplementation().createNewUser(
                username = userRegisterModel.username,
                password = userRegisterModel.password,
                email = userRegisterModel.email,
                phoneNumber = userRegisterModel.phoneNumber,
                userLoginMethod = userRegisterModel.userLoginMethod,
                birthday = userRegisterModel.birthday
            )
        }
    }
}