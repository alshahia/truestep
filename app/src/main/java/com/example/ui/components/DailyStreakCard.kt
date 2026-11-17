package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.tokens.appShapes
import com.example.ui.theme.tokens.dimens
import com.example.ui.theme.tokens.spacing
import java.time.LocalDate

@Composable
fun DailyStreakCard(
    streakDays: Int,
    isGoalMetToday: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val weekDays = listOf(
        R.string.weekday_m,
        R.string.weekday_t,
        R.string.weekday_w,
        R.string.weekday_th,
        R.string.weekday_f,
        R.string.weekday_sa,
        R.string.weekday_su
    )

    // Current day of week (1 = Monday, 7 = Sunday)
    val currentDayOfWeek = remember { LocalDate.now().dayOfWeek.value }

    Card(
        shape = MaterialTheme.appShapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.dimens.elevationLow),
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .testTag("home_daily_streak_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
        ) {
            Text(
                text = stringResource(R.string.daily_progress_streak),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Flame Streak Pill
                Row(
                    modifier = Modifier
                        .clip(MaterialTheme.appShapes.pill)
                        .background(Color(0xFF00F0FF).copy(alpha = 0.12f))
                        .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color(0xFFFF9500), // Vibrant amber/fire flame
                        modifier = Modifier.size(MaterialTheme.dimens.iconMedium)
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.xs))
                    Text(
                        text = stringResource(R.string.streak_pill_days_format, streakDays),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00F0FF) // Hyper Cyan
                    )
                }

                // 7 Weekly Dots (M T W T F S S)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    weekDays.forEachIndexed { index, dayRes ->
                        val dayIndex1Based = index + 1
                        val isDayPassedOrToday = dayIndex1Based <= currentDayOfWeek
                        val isDayActive = when {
                            dayIndex1Based == currentDayOfWeek -> isGoalMetToday || streakDays > 0
                            dayIndex1Based < currentDayOfWeek -> streakDays >= (currentDayOfWeek - dayIndex1Based)
                            else -> false
                        }

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isDayActive -> Color(0xFF10E599) // Emerald Mint for active/completed days
                                        dayIndex1Based == currentDayOfWeek -> MaterialTheme.colorScheme.primaryContainer
                                        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(dayRes),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = if (isDayActive) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = when {
                                    isDayActive -> Color.Black
                                    dayIndex1Based == currentDayOfWeek -> MaterialTheme.colorScheme.onPrimaryContainer
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
