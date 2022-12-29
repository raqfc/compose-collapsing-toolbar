package br.com.justworks.volan2.common.presentation.composables.animations

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.unit.IntOffset

object Transitions {

    fun slideInFromBottom(): EnterTransition = slideInVertically(
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            visibilityThreshold = IntOffset.VisibilityThreshold
        ), initialOffsetY = { it }
    )

    fun slideOutToBottom(): ExitTransition = slideOutVertically(
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            visibilityThreshold = IntOffset.VisibilityThreshold
        ),
        targetOffsetY = { it }
    )
}