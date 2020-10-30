package kr.kro.jhseo1107

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import freemarker.cache.*
import io.ktor.freemarker.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.sessions.*
import io.ktor.features.*
import kr.kro.jhseo1107.controller.anonArticleRoutes
import kr.kro.jhseo1107.controller.articleRoutes
import kr.kro.jhseo1107.controller.userRoutes
import kr.kro.jhseo1107.db.Articles
import kr.kro.jhseo1107.db.DatabaseInit
import kr.kro.jhseo1107.model.Article
import kr.kro.jhseo1107.model.Article.timeFormatter
import kr.kro.jhseo1107.util.FlashManager
import kr.kro.jhseo1107.util.UserManager

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    DatabaseInit.initDB()

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Locations) {
    }

    install(Sessions) {
        cookie<UserSession>("USER_SESSION", SessionStorageMemory())
        cookie<FlashSession>("FLASH_SESSION", SessionStorageMemory())
    }

    routing {
        get("/") {
            FlashManager.addFlash("primary", "Internet Explorer에서는 사이트가 정상적으로 그려지지 않을 수 있습니다.", call)
            call.respond(FreeMarkerContent("index.ftl", mapOf(
                    "title" to "홈",
                    "path" to "/",
                    "userSession" to UserManager.getWithoutNull(call),
                    "flash" to FlashManager.getFlash(call),
                    "fixedArticleId" to Article.getArticlesId(true),
                    "fixedArticleTitle" to Article.getArticles(Articles.title, true),
                    "fixedArticleWriter" to Article.writerNameList(Article.getArticles(Articles.writer, true)),
                    "fixedArticleTime" to Article.formatList(Article.getArticles(Articles.created_at, true)),
                    "articleId" to Article.getArticlesId(false),
                    "articleTitle" to Article.getArticles(Articles.title, false),
                    "articleWriter" to Article.writerNameList(Article.getArticles(Articles.writer, false)),
                    "articleTime" to Article.formatList(Article.getArticles(Articles.created_at, false))
            )))
        }

        anonArticleRoutes()
        articleRoutes()
        userRoutes()

        static("/static") {
            resources("static")
        }

        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }
        }
    }
}

data class UserSession(var id: Int = 0)
data class FlashSession(var tag: MutableList<String> = mutableListOf(), var content : MutableList<String> = mutableListOf())

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

