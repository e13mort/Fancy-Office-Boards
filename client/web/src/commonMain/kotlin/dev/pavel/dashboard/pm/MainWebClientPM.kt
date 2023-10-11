/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.pm

import kotlinx.serialization.Serializable
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel
import me.dmdev.premo.navigation.StackNavigation
import me.dmdev.premo.navigation.StackNavigator
import me.dmdev.premo.navigation.push
import me.dmdev.premo.onMessage

class MainWebClientPM(pmParams: PmParams) : PresentationModel(pmParams) {
    @Serializable
    object Description : PmDescription

    private val navigator = StackNavigator(DisplayListPM.Description)
    val navigation: StackNavigation = navigator

    init {
        messageHandler.onMessage<DisplayListPM.DisplaySelectedMessage> {
            navigator.push(Child(WebPagesDashboardPM.Description(it.dashboardId)))
        }
    }
}