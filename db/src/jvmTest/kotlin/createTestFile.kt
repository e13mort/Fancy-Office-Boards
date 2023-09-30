/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

import java.io.File

/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

actual fun createTestFile(): TestFile {
    return object : TestFile {
        private val testFile = File("test.db")

        override fun name(): String {
            return testFile.path
        }

        override fun remove() {
            testFile.delete()
        }

    }
}