package kr.kro.jhseo1107.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object Users : IntIdTable(name = "users") {
    val name = text(name = "name")
    val mail = text(name = "mail")
    val password = text(name = "password")
    val created_at = datetime(name = "created_at")
    val updated_at = datetime(name = "updated_at")
}

object AnonArticles : IntIdTable(name = "anon_articles") {
    val title = text(name = "title")
    val topic = text(name = "topic")
    val content = text(name = "content")
    val created_at = datetime(name = "created_at")
    val updated_at = datetime(name = "updated_at")
}

object Articles : IntIdTable(name = "articles") {
    val title = text(name = "title")
    val writer = integer(name = "writer").references(Users.id)
    val content = text(name = "content")
    val fixed = bool(name = "fixed")
    val created_at = datetime(name = "created_at")
    val updated_at = datetime(name = "updated_at")
}