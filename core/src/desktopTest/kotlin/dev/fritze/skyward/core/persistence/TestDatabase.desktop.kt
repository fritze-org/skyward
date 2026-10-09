package dev.fritze.skyward.core.persistence

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

actual fun inMemorySkywardDatabase(): SkywardDatabase {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    SkywardDatabase.Schema.create(driver)
    return SkywardDatabase(driver)
}
