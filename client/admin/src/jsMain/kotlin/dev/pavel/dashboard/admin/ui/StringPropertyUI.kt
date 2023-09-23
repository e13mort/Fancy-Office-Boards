package dev.pavel.dashboard.admin.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import dev.pavel.dashboard.admin.properties.StringProperty
import dev.petuska.kmdc.textfield.MDCTextArea
import dev.petuska.kmdc.textfield.MDCTextField
import dev.petuska.kmdc.textfield.MDCTextFieldType
import dev.petuska.kmdc.typography.MDCBody1
import dev.petuska.kmdc.typography.MDCCaption
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
@Composable
fun StringProperty.RenderFormfield(
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
        val currentNameValue = flow.collectAsState().value
        val lastError = errors.collectAsState().value
        if (lastError != null) {
            Div {
                MDCCaption(lastError)
            }
        }
        MDCTextField(
            value = currentNameValue,
            label = label,
            disabled = !isActive,
            type = MDCTextFieldType.Outlined,
            attrs = {
                onInput { syntheticInputEvent ->
                    value = syntheticInputEvent.value
                }
            }
        )
    }
}

@Composable
fun StringProperty.RenderAsDescription(
    isActive: Boolean = false,
    label: String = "Description"
) {
    val content = flow.collectAsState().value
    MDCTextArea(
        value = content,
        label = label,
        disabled = !isActive,
        type = MDCTextFieldType.Outlined,
        rows = 2u,
        attrs = {
            onInput {
                if (isActive) {
                    value = it.value
                }
            }
        }
    )
}