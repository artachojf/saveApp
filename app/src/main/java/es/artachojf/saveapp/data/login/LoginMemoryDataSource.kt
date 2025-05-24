package es.artachojf.saveapp.data.login

import io.github.jan.supabase.gotrue.user.UserInfo

class LoginMemoryDataSource {

    private var loggedUser: UserInfo? = null

    fun getLoggedUser(): UserInfo? {
        return loggedUser
    }

    fun setLoggedUser(newUser: UserInfo?) {
        loggedUser = newUser
    }
}