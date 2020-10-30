package kr.kro.jhseo1107.model

import kr.kro.jhseo1107.db.Articles
import kr.kro.jhseo1107.db.Articles.fixed
import kr.kro.jhseo1107.db.Users
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Article {
    val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun <T> getArticles(args: Column<T>, isFixed: Boolean): List<T> {
        var returnList: MutableList<T> = mutableListOf()
        transaction {
            returnList = Articles.select { fixed eq isFixed }.map { it[args] }.toMutableList().also{ it.reverse() }
        }
        return returnList.toList()
    }

    fun getArticlesId(isFixed: Boolean): List<Int> {
        var returnList: MutableList<Int> = mutableListOf()
        transaction {
            returnList = Articles.select { fixed eq isFixed }.map { it[Articles.id].value }.toMutableList().also{ it.reverse() }
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

    fun writerNameList(list: List<Int>) : List<String> {
        val tmp : MutableList<String> = mutableListOf()
        list.forEach {
            val givenId = it
            tmp.add(transaction {
                Users.select{Users.id eq givenId}.map {it[Users.name]}.first()
            })
        }
        return tmp.toList()
    }
}