/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.admin.properties.validation

object PositiveIntValidator : Validator<String> {
    override fun validate(item: String) {
        val value = item.toIntOrNull()
        if (value == null || value < 1) {
            throw Validator.ValidationException("Value must be positive int number")
        }
    }

}