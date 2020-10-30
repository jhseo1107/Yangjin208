package kr.kro.jhseo1107.util

import io.ktor.application.*
import io.ktor.sessions.*
import kr.kro.jhseo1107.FlashSession

object FlashManager {
    fun addFlash(tag: String, content: String, call: ApplicationCall) {
        val tagList = (call.sessions.get<FlashSession>()?.tag ?: mutableListOf()).also { it.add(tag) }
        val contentList = (call.sessions.get<FlashSession>()?.content ?: mutableListOf()).also { it.add(content) }

        call.sessions.set(FlashSession(tagList, contentList))
    }
    fun removeFlash(call: ApplicationCall) {
        call.sessions.set(FlashSession(mutableListOf(), mutableListOf()))
    }
    fun getFlash(call : ApplicationCall) : FlashSession {
        val flashListCopy = call.sessions.get<FlashSession>() ?: FlashSession()
        removeFlash(call)
        return flashListCopy
    }
}