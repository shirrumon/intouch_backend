package com.intch.services.user

import com.intch.db.facadeImplementation.UserFacadeImplementation
import com.intch.models.UserModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class UserAuthService {
    @OptIn(DelicateCoroutinesApi::class)
    fun checkUserWhenLogin(user: UserModel): MutableStateFlow<Boolean> {
        val isUserExistAndCorrect = MutableStateFlow(false)
        GlobalScope.launch {
            val requestedUser = UserFacadeImplementation().userByUsername(user.username)

            if(requestedUser != null && BCrypt.checkpw(user.password, requestedUser.password)) {
                isUserExistAndCorrect.value = true
            }
        }

        return isUserExistAndCorrect
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