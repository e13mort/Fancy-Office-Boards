/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.util.Properties

actual class DriverFactory(
    private val driver: JdbcSqliteDriver
) {
    companion object {
        @Suppress("unused")
        fun inMemory() = DriverFactory(
            JdbcSqliteDriver(
                JdbcSqliteDriver.IN_MEMORY,
                properties = createProperties()
            )
        )

        fun persistent(fileName: String) = DriverFactory(
            JdbcSqliteDriver(
                url = "jdbc:sqlite:$fileName",
                properties = createProperties()
            )
        )

        private fun createProperties(): Properties =
            Properties().apply { put("foreign_keys", "true") }
    }

    actual fun createDriver(): SqlDriver {
        Database.Schema.create(driver)
        return driver
    }
}