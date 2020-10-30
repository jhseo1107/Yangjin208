package kr.kro.jhseo1107.controller

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kr.kro.jhseo1107.db.Articles
import kr.kro.jhseo1107.db.Users
import kr.kro.jhseo1107.model.Article
import kr.kro.jhseo1107.model.Article.timeFormatter
import kr.kro.jhseo1107.util.FlashManager
import kr.kro.jhseo1107.util.UserManager
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime



fun Route.articleRoutes() {
    route("/articles") {
        get ("") {
            call.respond(FreeMarkerContent("/articles/index.ftl", mapOf(
                "title" to "게시글 목록",
                "path" to "/articles",
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
        get ("/new") {
            if(UserManager.getWithoutNull(call).id == 0) {
                FlashManager.addFlash("danger", "권한 부족", call)
                call.respondRedirect("/")
                return@get
            }
            call.respond(FreeMarkerContent("/articles/new.ftl", mapOf(
                "title" to "게시글 작성",
                "path" to "/articles/new",
                "userSession" to UserManager.getWithoutNull(call),
                "flash" to FlashManager.getFlash(call),
                "articleWriter" to transaction{Users.select{Users.id eq UserManager.getWithoutNull(call).id}.map{it[Users.name]}.first()}
            )))
        }
        post ("/create") {
            if(UserManager.getWithoutNull(call).id == 0) {
                FlashManager.addFlash("danger", "권한 부족", call)
                call.respondRedirect("/")
                return@post
            }
            val params = call.receiveParameters()
            val articleTitle = params["articleTitle"]!!
            val articleWriter = UserManager.getWithoutNull(call).id
            val articleFixed = !params["articleFixed"].isNullOrEmpty()
            val articleContent = params["articleContent"]!!

            transaction{
                Articles.insert{
                    it[title] = articleTitle
                    it[writer] = articleWriter
                    it[fixed] = articleFixed
                    it[content] = articleContent
                    it[created_at] = LocalDateTime.now()
                    it[updated_at] = LocalDateTime.now()
                }
            }
            call.respondRedirect("/articles/${transaction{Articles.selectAll().count()}}")
        }
        get ("/{id}") {
            val givenId: Int
            try {
                givenId = call.parameters["id"]!!.toInt()
            } catch (e : Exception) {
                FlashManager.addFlash("danger", "잘못된 URL입니다.", call)
                call.respondRedirect("/")
                return@get
            }

            val article = transaction{Articles.select{Articles.id eq givenId}.first()}

            call.respond(FreeMarkerContent("/articles/show.ftl", mapOf(
                "title" to article[Articles.title],
                "path" to "/articles/${givenId}",
                "userSession" to UserManager.getWithoutNull(call),
                "flash" to FlashManager.getFlash(call),
                "articleId" to givenId,
                "articleTitle" to article[Articles.title],
                "articleWriter" to transaction{Users.select{Users.id eq article[Articles.writer]}.first()}[Users.name],
                "articleTime" to article[Articles.created_at].format(timeFormatter),
                "articleIsFixed" to article[Articles.fixed],
                "articleContent" to article[Articles.content].replace("\r\n", "<br>").replace("\n", "<br>")
            )))
        }
        post ("/update/{id}") {
            if(UserManager.getWithoutNull(call).id == 0) {
                FlashManager.addFlash("danger", "권한 부족", call)
                call.respondRedirect("/")
                return@post
            }
            val givenId = call.parameters["id"]!!.toInt()
            val article = transaction{Articles.select{Articles.id eq givenId}.first()}
            transaction{
                Articles.update({Articles.id eq givenId}) {
                    it[fixed] = !article[fixed]
                    it[updated_at] = LocalDateTime.now()
                }
            }
            call.respondRedirect("/articles/${givenId}")
        }
    }
}