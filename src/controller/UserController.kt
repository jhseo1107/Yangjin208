package kr.kro.jhseo1107.controller

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kr.kro.jhseo1107.EncryptBuilder1107
import kr.kro.jhseo1107.EncryptMethod
import kr.kro.jhseo1107.UserSession
import kr.kro.jhseo1107.db.Users
import kr.kro.jhseo1107.db.Users.id
import kr.kro.jhseo1107.db.Users.mail
import kr.kro.jhseo1107.db.Users.name
import kr.kro.jhseo1107.db.Users.password
import kr.kro.jhseo1107.util.FlashManager
import kr.kro.jhseo1107.util.UserManager
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime

fun Route.userRoutes() {
    route("/users") {
        get("") {
            if (UserManager.getWithoutNull(call).id != 1) {
                FlashManager.addFlash("danger", "권한 부족", call)
                call.respondRedirect("/")
                return@get
            }

            var userId : List<Int> = emptyList()
            var userName : List<String> = emptyList()
            var userMail : List<String> = emptyList()

            transaction {
                with(Users.selectAll()) {
                    userId = this.map { it[Users.id].value }
                    userName = this.map { it[name] }
                    userMail = this.map { it[mail] }
                }
            }

            call.respond(FreeMarkerContent("/users/index.ftl", mapOf(
                "title" to "유저 관리",
                "path" to "/users",
                "userSession" to UserManager.getWithoutNull(call),
                "flash" to FlashManager.getFlash(call),
                "userId" to userId,
                "userName" to userName,
                "userMail" to userMail
            )))
        }
        get("/new") {
            if (UserManager.getWithoutNull(call).id != 1) {
                FlashManager.addFlash("danger", "권한 부족", call)
                call.respondRedirect("/")
                return@get
            }
            call.respond(FreeMarkerContent("/users/new.ftl", mapOf(
                "title" to "유저 추가",
                "path" to "/users/new",
                "userSession" to UserManager.getWithoutNull(call),
                "flash" to FlashManager.getFlash(call)
            )))
        }
        post("/create") {
            val params = call.receiveParameters()
            val userName = params["userName"]!!
            val userMail = params["userMail"]!!
            val userPw =
                EncryptBuilder1107().setPlainText(params["userPw"]!!).setEncryptMethod(EncryptMethod.SHA_256)
                    .build()

            if (transaction { Users.select { mail eq userMail }.count() } != 0.toLong()) {
                FlashManager.addFlash("danger", "이미 회원가입된 이메일입니다.", call)
                call.respondRedirect("/users/new")
                return@post
            }

            transaction { Users.insert {
                it[name] = userName
                it[mail] = userMail
                it[password] = userPw
                it[created_at] = LocalDateTime.now()
                it[updated_at] = LocalDateTime.now()
            } }
            FlashManager.addFlash("primary", "성공", call)
            call.respondRedirect("/users/${transaction{Users.selectAll().count()}}")
        }
        get("/{id}") {
            val givenId: Int
            try {
                givenId = call.parameters["id"]!!.toInt()
            } catch (e : Exception) {
                FlashManager.addFlash("danger", "잘못된 URL입니다.", call)
                call.respondRedirect("/")
                return@get
            }

            if(UserManager.getWithoutNull(call).id != 1 && UserManager.getWithoutNull(call).id != givenId) {
                FlashManager.addFlash("danger", "권한 부족", call)
                call.respondRedirect("/")
                return@get
            }

            val user =  transaction { Users.select{Users.id eq givenId}.first() }
            call.respond(FreeMarkerContent("/users/show.ftl", mapOf(
                "title" to user[name],
                "path" to "/users/${givenId}",
                "userSession" to UserManager.getWithoutNull(call),
                "flash" to FlashManager.getFlash(call),
                "userName" to user[name],
                "userMail" to user[mail],
                "userId" to user[id]
            )))
        }
        post("/update/{id}") {
            val givenId = call.parameters["id"]!!.toInt()
            val params = call.receiveParameters()

            if(UserManager.getWithoutNull(call).id != 1 && UserManager.getWithoutNull(call).id != givenId) {
                FlashManager.addFlash("danger", "권한 부족", call)
                call.respondRedirect("/")
                return@post
            }

            val givenName = params["userName"]!!
            val givenPw = EncryptBuilder1107().setPlainText(params["userPw"]!!).setEncryptMethod(EncryptMethod.SHA_256).build()

            val user = transaction { Users.select {Users.id eq givenId}.first() }

            if(user[password] != givenPw) {
                FlashManager.addFlash("warning", "비밀번호가 일치하지 않습니다.", call)
                call.respondRedirect("/users/${givenId}")
                return@post
            }
            val givenNewPw : String? = params["userNewPw"]
            if(givenNewPw.isNullOrEmpty()) {
                transaction { Users.update({Users.id eq givenId}) {
                    it[name] = givenName
                    it[updated_at] = LocalDateTime.now()
                } }
            } else {
                transaction { Users.update({Users.id eq givenId}) {
                    it[name] = givenName
                    it[password] = EncryptBuilder1107().setPlainText(givenNewPw).setEncryptMethod(EncryptMethod.SHA_256).build()
                    it[updated_at] = LocalDateTime.now()
                } }
            }
            FlashManager.addFlash("primary", "성공", call)
            call.respondRedirect("/users/${givenId}")
        }
        post("/match") {
            val params = call.receiveParameters()
            val userMail = params["userMail"]!!
            val userPw = EncryptBuilder1107().setPlainText(params["userPw"]!!).setEncryptMethod(EncryptMethod.SHA_256).build()

            if (transaction { Users.selectAll().count() } == 0.toLong() && userMail == "seojanghyeob@gmail.com") { // The Dirty Part
                transaction { Users.insert {
                    it[name] = "관리자"
                    it[mail] = userMail
                    it[password] = userPw
                    it[created_at] = LocalDateTime.now()
                    it[updated_at] = LocalDateTime.now()
                } }
                call.sessions.set(UserSession(id = 1))
                call.respondRedirect("/users/1")
                return@post
            }

            if(transaction { Users.select{mail eq userMail}.count() } == 0.toLong()) {
                FlashManager.addFlash("warning", "해당 이메일로 등록된 계정이 존재하지 않습니다.", call)
                call.respondRedirect("/users/login")
                return@post
            }

            val savedUser = transaction { Users.select {mail eq userMail}.first() }

            if( savedUser[password] != userPw ) {
                FlashManager.addFlash("warning", "비밀번호가 잘못되었습니다.", call)
                call.respondRedirect("/users/login")
                return@post
            }

            call.sessions.set(UserSession(id = savedUser[id].value))
            FlashManager.addFlash("primary", "로그인에 성공했습니다.", call)
            call.respondRedirect("/")
        }
        get("/login") {
            call.respond(FreeMarkerContent("/users/login.ftl", mapOf(
                "title" to "로그인",
                "path" to "/users/login",
                "userSession" to UserManager.getWithoutNull(call),
                "flash" to FlashManager.getFlash(call)
            )))
        }
        get("/logout") {
            if(UserManager.getWithoutNull(call).id != 0) {
                call.sessions.set(UserSession(id = 0))
            }
            call.respondRedirect("/")
        }
    }
}