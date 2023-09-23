/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.admin.properties

import dev.pavel.dashboard.admin.properties.validation.Validator

class StringProperty(initialValue: String, validators: List<Validator<String>> = emptyList()) :
    AbstractViewProperty<String>(initialValue, validators)

fun StringProperty.copy() : StringProperty {
    // don't copy errors
    return StringProperty(
        initialValue = value,
        validators = validators
    )
}