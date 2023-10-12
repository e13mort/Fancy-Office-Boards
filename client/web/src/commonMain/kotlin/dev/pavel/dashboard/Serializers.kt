/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard

import dev.pavel.dashboard.pm.DisplayListPM
import dev.pavel.dashboard.pm.MainWebClientPM
import dev.pavel.dashboard.pm.WebPagesDashboardPM
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import me.dmdev.premo.PmDescription

object Serializers {
    private val module = SerializersModule {
        polymorphic(PmDescription::class) { registerPmDescriptionSubclasses() }
    }

    val json = Json {
        serializersModule = module
    }

    private fun PolymorphicModuleBuilder<PmDescription>.registerPmDescriptionSubclasses() {
        subclass(DisplayListPM.Description::class)
        subclass(MainWebClientPM.Description::class)
        subclass(WebPagesDashboardPM.Description::class)
    }
}