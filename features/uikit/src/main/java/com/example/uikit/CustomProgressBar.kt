@file:Suppress("SpacingBetweenDeclarationsWithAnnotations")

package com.example.uikit

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val HALF_CIRCLE_ANGLE = 180f
private const val FULL_CIRCLE_ANGLE = 360f

@Suppress("LongParameterList")
@Composable
fun CustomProgressBar(
    modifier: Modifier,
    isVisible: Boolean,
    color: Color,
    secondColor: Color? = color,
    tailLength: Float = 140f,
    smoothTransition: Boolean = true,
    strokeStyle: StrokeStyle = StrokeStyle(),
    cycleDuration: Int = 1400,
) {
    val tailToDisplay = remember { Animatable(0f) }

    LaunchedEffect(isVisible) {
        val targetTail = if (isVisible) tailLength else 0f
        when {
            smoothTransition -> tailToDisplay.animateTo(
                targetValue = targetTail,
                animationSpec = tween(cycleDuration, easing = LinearEasing)
            )

            else -> tailToDisplay.snapTo(targetTail)
        }
    }

    val transition = rememberInfiniteTransition()

    val spinAngel = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = cycleDuration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "",
    )

    Canvas(
        modifier
            .rotate(spinAngel.value)
            .aspectRatio(1f)
    ) {
        listOfNotNull(color, secondColor).forEachIndexed { index, color ->
            rotate(if (index == 0) 0f else HALF_CIRCLE_ANGLE) {
                val brush = Brush.sweepGradient(
                    0f to Color.Transparent,
                    tailToDisplay.value / FULL_CIRCLE_ANGLE to color,
                    1f to Color.Transparent
                )
                val paint = setupPaint(strokeStyle, brush)

                drawIntoCanvas { canvas ->
                    canvas.drawArc(
                        rect = size.toRect(),
                        startAngle = 0f,
                        sweepAngle = tailToDisplay.value,
                        useCenter = false,
                        paint = paint
                    )
                }
            }
        }
    }
}

private fun DrawScope.setupPaint(style: StrokeStyle, brush: Brush): Paint {
    val paint = Paint().apply paint@{
        this@paint.isAntiAlias = true
        this@paint.style = PaintingStyle.Stroke
        this@paint.strokeWidth = style.width.toPx()
        this@paint.strokeCap = style.strokeCap
        brush.applyTo(size, this@paint, 1f)
    }

    style.glowRadius?.let { radius ->
        paint.asFrameworkPaint().setShadowLayer(
            radius.toPx(),
            0f,
            0f,
            android.graphics.Color.WHITE
        )
    }
    return paint
}

data class StrokeStyle(
    val width: Dp = 4.dp,
    val strokeCap: StrokeCap = StrokeCap.Round,
    val glowRadius: Dp? = 4.dp
)
