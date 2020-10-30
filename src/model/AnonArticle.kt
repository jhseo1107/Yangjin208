package kr.kro.jhseo1107.model

import kr.kro.jhseo1107.db.AnonArticles
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object AnonArticle {
    val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun <T> getAnonArticles(args: Column<T>): List<T> {
        var returnList: MutableList<T> = mutableListOf()
        transaction {
            returnList = AnonArticles.selectAll().map { it[args] }.toMutableList().also { it.reverse() }
        }
        return returnList.toList()
    }

    fun getAnonArticlesId(): List<Int> {
        var returnList: MutableList<Int> = mutableListOf()
        transaction {
            returnList = AnonArticles.selectAll().map { it[AnonArticles.id].value }.toMutableList().also { it.reverse() }
        }
        return returnList.toList()
    }

    fun formatList(list: List<LocalDateTime>) : List<String> {
        val tmp : MutableList<String> = mutableListOf()
        list.forEach{
            tmp.add(it.format(timeFormatter))
        }
        return tmp.toList()
    }
}