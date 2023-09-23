/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard

import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.lang
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.script
import kotlinx.html.title

fun HTML.mainHTMLContent(pageTitle: String, scriptName: String) {
    lang = "en"
    head {
        meta {
            charset = "UTF-8"
        }
        title {
            +pageTitle
        }
        link("/static/styles.css", rel = "stylesheet")
    }
    body {
        div("container") {
            div {
                id = "root"
            }
        }
        script(src = "/scripts/${scriptName}") {}
    }
}