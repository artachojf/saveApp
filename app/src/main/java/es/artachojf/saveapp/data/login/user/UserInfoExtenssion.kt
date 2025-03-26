package es.artachojf.saveapp.data.login.user

import es.artachojf.saveapp.domain.login.user.model.UserBusiness
import io.github.jan.supabase.gotrue.user.UserInfo

fun UserInfo.toDomain(): UserBusiness {
    return UserBusiness(
        id = id,
        email = email
    )
}