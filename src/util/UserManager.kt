package kr.kro.jhseo1107.util

import io.ktor.application.*
import io.ktor.sessions.*
import kr.kro.jhseo1107.UserSession

object UserManager {
    fun getWithoutNull(call: ApplicationCall) : UserSession {
        return call.sessions.get<UserSession>() ?: UserSession()
    }
}