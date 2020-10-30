package kr.kro.jhseo1107.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.mariadb.jdbc.Driver

object DatabaseConfig {
    val url = "jdbc:mysql://localhost:3306/yangjin208?serverTimeZone=Asia/Seoul&characterEncoding=utf8&useUnicode=true"
    val driver = Driver::class.java.name
    val user = "dockhyub"
    val password = System.getenv("DBPASSWORD")
}

object DatabaseInit {
    fun initDB() {
        Database.connect(url = DatabaseConfig.url, driver = DatabaseConfig.driver, user = DatabaseConfig.user, password = DatabaseConfig.password)

        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.let {
                it.create(Users)
                it.create(AnonArticles)
                it.create(Articles)
            }
        }
    }
}
