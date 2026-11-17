package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.tokens.appShapes
import com.example.ui.theme.tokens.dimens
import com.example.ui.theme.tokens.spacing
import com.example.util.MetricFormatter

@Composable
fun HeroStepRing(
    steps: Int,
    goal: Int,
    percent: Int,
    isPaused: Boolean,
    onTogglePause: () -> Unit,
    modifier: Modifier = Modifier,
    onEditGoal: (() -> Unit)? = null
) {
    val progress = if (goal > 0) (steps.toFloat() / goal).coerceIn(0f, 1.5f) else 0f
    val isGoalMet = steps >= goal && goal > 0
    val remainingSteps = maxOf(0, goal - steps)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "ring_progress"
    )

    val activeColor = when {
        isPaused -> MaterialTheme.colorScheme.secondary
        isGoalMet -> Color(0xFF10E599) // Emerald Mint
        else -> MaterialTheme.colorScheme.primary
    }

    val secondaryGradientColor = when {
        isPaused -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
        isGoalMet -> Color(0xFF00F0FF) // Hyper Cyan
        else -> Color(0xFF00D2FF)
    }

    val animatedArcColor by animateColorAsState(
        targetValue = activeColor,
        animationSpec = tween(durationMillis = 400),
        label = "ring_color"
    )

    val trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)

    val cd = "${steps} ${stringResource(R.string.steps_unit)}, $percent% ${stringResource(R.string.of_goal)}"

    val captionText = when {
        isGoalMet -> stringResource(R.string.goal_achieved_caption)
        remainingSteps > 0 -> stringResource(R.string.remaining_steps_to_goal_format, MetricFormatter.formatSteps(remainingSteps))
        else -> stringResource(R.string.supportive_habit)
    }

    Card(
        modifier = modifier.testTag("hero_step_ring_card"),
        shape = MaterialTheme.appShapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.dimens.elevationLow)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            // Dedicated pause/resume icon button in top-right corner (48dp touch target)
            IconButton(
                onClick = onTogglePause,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(MaterialTheme.dimens.minTouchTarget)
                    .testTag("hero_ring_toggle")
            ) {
                Icon(
                    imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = stringResource(if (isPaused) R.string.resume_tracking_desc else R.string.pause_tracking_desc),
                    tint = if (isPaused) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Central Telemetry Ring & Caption
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape)
                        .semantics { contentDescription = cd },
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        val strokeWidth = 12.dp.toPx()
                        drawCircle(
                            color = trackColor,
                            style = Stroke(width = strokeWidth)
                        )

                        val sweepAngle = (animatedProgress * 360f).coerceAtMost(360f)
                        val arcBrush = Brush.sweepGradient(
                            colors = listOf(animatedArcColor, secondaryGradientColor, animatedArcColor)
                        )
                        drawArc(
                            brush = arcBrush,
                            startAngle = -90f,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = "%,d".format(steps),
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.testTag("ring_step_count_text")
                        )

                        Text(
                            text = stringResource(R.string.steps_unit),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        // Target Indicator (tappable to edit goal if callback provided)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clip(MaterialTheme.appShapes.small)
                                .then(if (onEditGoal != null) Modifier.clickable(onClick = onEditGoal) else Modifier)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "$percent% ${stringResource(R.string.of_goal)}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = animatedArcColor
                            )
                            if (onEditGoal != null) {
                                Spacer(modifier = Modifier.width(3.dp))
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = stringResource(R.string.edit_goal),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = captionText,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    fontWeight = FontWeight.Medium,
                    color = if (isGoalMet) Color(0xFF10E599) else MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.xs)
                )
            }
        }
    }
}
