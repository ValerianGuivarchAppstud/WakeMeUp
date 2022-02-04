package com.vguivarc.wakemeup.util.widget

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.FloatingActionButtonElevation
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun LoadableButton(loading : Boolean = false,
                   text: @Composable () -> Unit,
                   onClick: () -> Unit,
                   modifier: Modifier = Modifier,
                   icon: @Composable (() -> Unit)? = null,
                   interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
                   shape: Shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
                   backgroundColor: Color = MaterialTheme.colors.secondary,
                   contentColor: Color = contentColorFor(backgroundColor),
                   elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation()
) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .padding(start = 16.dp, end = 12.dp),
        contentAlignment = Alignment.Center
    ) {
    ExtendedFloatingActionButton(text = text, onClick = onClick, modifier = modifier, icon = icon, interactionSource = interactionSource,
    shape = shape, backgroundColor = backgroundColor, contentColor = contentColor, elevation = elevation)
       if(loading){
           CircularProgressIndicator()
       }
    }
}