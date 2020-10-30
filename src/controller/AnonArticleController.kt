package kr.kro.jhseo1107.controller

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kr.kro.jhseo1107.db.AnonArticles
import kr.kro.jhseo1107.db.Articles
import kr.kro.jhseo1107.db.Users
import kr.kro.jhseo1107.model.AnonArticle
import kr.kro.jhseo1107.model.Article
import kr.kro.jhseo1107.util.FlashManager
import kr.kro.jhseo1107.util.UserManager
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

fun Route.anonArticleRoutes() {
    route("/anon_articles") {
        get ("") {
            if(UserManager.getWithoutNull(call).id == 0) {
                FlashManager.addFlash("danger", "권한 부족", call)
                call.respondRedirect("/")
                return@get
            }

            call.respond(FreeMarkerContent("/anon_articles/index.ftl", mapOf(
                "title" to "익명 게시글 목록",
                "path" to "/anon_articles",
                "userSession" to UserManager.getWithoutNull(call),
                "flash" to FlashManager.getFlash(call),
                "anonArticleId" to AnonArticle.getAnonArticlesId(),
                "anonArticleTitle" to AnonArticle.getAnonArticles(AnonArticles.title),
                "anonArticleTopic" to AnonArticle.getAnonArticles(AnonArticles.topic),
                "anonArticleTime" to AnonArticle.formatList(AnonArticle.getAnonArticles(AnonArticles.created_at))
            )))
        }
        get ("/new") {
            call.respond(FreeMarkerContent("/anon_articles/new.ftl", mapOf(
                "title" to "새 익명 게시글 작성",
                "path" to "/anon_articles/new",
                "userSession" to UserManager.getWithoutNull(call),
                "flash" to FlashManager.getFlash(call)
            )))
        }
        post ("/create") {
            val params = call.receiveParameters()
            val articleTitle = params["anonArticleTitle"]!!
            val articleTopic = params["anonArticleTopic"]!!
            val articleContent = params["anonArticleContent"]!!

            transaction {
                AnonArticles.insert{
                    it[title] = articleTitle
                    it[topic] = articleTopic
                    it[content] = articleContent
                    it[created_at] = LocalDateTime.now()
                    it[updated_at] = LocalDateTime.now()
                }
            }
            FlashManager.addFlash("primary", "작성에 성공했습니다!", call)
            call.respondRedirect("/")
        }
        get ("/{id}") {
            if(UserManager.getWithoutNull(call).id == 0) {
                FlashManager.addFlash("danger", "권한 부족", call)
                call.respondRedirect("/")
                return@get
            }
            val givenId: Int
            try {
                givenId = call.parameters["id"]!!.toInt()
            } catch (e : Exception) {
                FlashManager.addFlash("danger", "잘못된 URL입니다.", call)
                call.respondRedirect("/")
                return@get
            }

            val anonArticle = transaction{ AnonArticles.select{ AnonArticles.id eq givenId}.first()}

            call.respond(FreeMarkerContent("/anon_articles/show.ftl", mapOf(
                "title" to anonArticle[AnonArticles.title],
                "path" to "/anon_articles/${givenId}",
                "userSession" to UserManager.getWithoutNull(call),
                "flash" to FlashManager.getFlash(call),
                "anonArticleId" to givenId,
                "anonArticleTitle" to anonArticle[AnonArticles.title],
                "anonArticleTopic" to anonArticle[AnonArticles.topic],
                "anonArticleTime" to anonArticle[AnonArticles.created_at].format(Article.timeFormatter),
                "anonArticleContent" to anonArticle[AnonArticles.content].replace("\r\n", "<br>").replace("\n", "<br>")
            ))
            )
        }
    }
}