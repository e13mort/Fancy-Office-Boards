package dev.pavel.dashboard.admin.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import dev.pavel.dashboard.admin.properties.StringProperty
import dev.petuska.kmdc.textfield.MDCTextField
import dev.petuska.kmdc.typography.MDCBody1
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div

@Composable
fun StringProperty.RenderAsTitle(
    isActive: Boolean = false,
    label: String = "Title"
) {
    Div(
        attrs = {
            style {
                padding(4.px)
            }
        }
    ) {
        when (isActive) {
            true -> {
                val currentNameValue = flow.collectAsState().value
                MDCTextField(
                    value = currentNameValue,
                    label = label,
                    attrs = {
                        onInput { syntheticInputEvent ->
                            value = syntheticInputEvent.value
                        }
                    }
                )
            }

            false -> MDCBody1(value)
        }
    }
}