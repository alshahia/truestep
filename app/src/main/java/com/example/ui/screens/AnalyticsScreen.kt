package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.content.AchievementRegistry
import com.example.domain.content.DestinationRegistry
import com.example.domain.model.Achievement
import com.example.ui.viewmodel.StepUiState
import com.example.util.LocalizationUtil

@Composable
fun AnalyticsScreen(
    uiState: StepUiState,
    modifier: Modifier = Modifier
) {
    val history = uiState.allHistory
    val totalStepsLifetime = remember(history) { history.sumOf { it.totalSteps } }
    val totalKmLifetime = remember(history) { history.sumOf { it.cumulativeKm } }
    val bestDay = remember(history) { history.maxByOrNull { it.totalSteps } }
    val activeDaysCount = remember(history) { history.size.coerceAtLeast(1) }
    val metGoalCount = remember(history) { history.count { it.totalSteps >= it.minGoal } }
    val consistencyPercent = remember(history) {
        if (history.isEmpty()) 100 else ((metGoalCount.toFloat() / history.size) * 100).toInt()
    }

    val nextDestination = remember(totalKmLifetime) {
        DestinationRegistry.getNextDestination(totalKmLifetime)
    }
    val completedDestinations = remember(totalKmLifetime) {
        DestinationRegistry.getCompletedDestinations(totalKmLifetime)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.analytics_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Consistency Banner
        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().testTag("consistency_banner")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.consistency_score),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$consistencyPercent%",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.consistency_desc, metGoalCount, activeDaysCount),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(38.dp)
                )
            }
        }

        // Virtual Walk Destination Landmark Card
        if (nextDestination != null) {
            val prevKm = completedDestinations.lastOrNull()?.kmRequired ?: 0.0
            val destProgress = ((totalKmLifetime - prevKm) / (nextDestination.kmRequired - prevKm))
                .coerceIn(0.0, 1.0).toFloat()
            val remainingKm = (nextDestination.kmRequired - totalKmLifetime).coerceAtLeast(0.0)

            Card(
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth().testTag("next_destination_card")
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(imageVector = Icons.Default.Flag, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                            Text(text = stringResource(R.string.next_destination), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = stringResource(R.string.km_left_format, remainingKm),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = nextDestination.emoji, fontSize = 24.sp)
                        Column {
                            Text(text = LocalizationUtil.getDestinationName(nextDestination), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(text = LocalizationUtil.getDestinationDescription(nextDestination), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(destProgress)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
        }

        // Personal Records section
        Text(
            text = stringResource(R.string.personal_records_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RecordTile(
                icon = Icons.Default.Star,
                title = stringResource(R.string.all_time_best_day),
                value = if (bestDay != null) "%,d".format(bestDay.totalSteps) else "0",
                subtitle = bestDay?.date ?: "—",
                tint = Color(0xFFF59E0B),
                modifier = Modifier.weight(1f)
            )
            RecordTile(
                icon = Icons.Default.EmojiEvents,
                title = stringResource(R.string.longest_streak_record),
                value = "${uiState.bestStreak}",
                subtitle = stringResource(R.string.consecutive_days),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RecordTile(
                icon = Icons.Default.Timeline,
                title = stringResource(R.string.total_lifetime_steps),
                value = "%,d".format(totalStepsLifetime),
                subtitle = stringResource(R.string.all_time_total),
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
            RecordTile(
                icon = Icons.Default.Speed,
                title = stringResource(R.string.total_lifetime_km),
                value = "%.1f km".format(totalKmLifetime),
                subtitle = stringResource(R.string.distance_traveled),
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            )
        }

        // Real Hourly rhythm breakdown connected to database
        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = stringResource(R.string.hourly_distribution), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = stringResource(R.string.time_of_day_rhythm_desc), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                // 12 sampled hours: 6 to 22 (intervals of ~1.5h)
                val hoursToDisplay = listOf(6, 7, 8, 9, 11, 12, 14, 15, 17, 18, 20, 22)
                val maxHourlySteps = hoursToDisplay.maxOfOrNull { uiState.hourlySteps.getOrElse(it) { 0 } }?.coerceAtLeast(100) ?: 100

                Row(
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    hoursToDisplay.forEach { h ->
                        val count = uiState.hourlySteps.getOrElse(h) { 0 }
                        val fraction = (count.toFloat() / maxHourlySteps).coerceIn(0.08f, 1f)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height((54 * fraction).dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (count > 0) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f + (fraction * 0.6f))
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                        )
                    }
                }
            }
        }

        // Milestones & Achievement Badges Section (Connected to AchievementDao & Registry)
        Text(
            text = stringResource(R.string.milestones_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AchievementRegistry.allAchievements.forEach { achievement ->
                val isUnlocked = uiState.unlockedAchievementIds.contains(achievement.id)
                AchievementRowCard(
                    achievement = achievement,
                    isUnlocked = isUnlocked
                )
            }
        }
    }
}

@Composable
private fun AchievementRowCard(
    achievement: Achievement,
    isUnlocked: Boolean
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 1.dp else 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("achievement_${achievement.id}")
            .border(
                width = 1.dp,
                color = if (isUnlocked) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isUnlocked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = achievement.iconEmoji, fontSize = 20.sp)
                }

                Column {
                    Text(
                        text = LocalizationUtil.getAchievementTitle(achievement),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = LocalizationUtil.getAchievementRequirement(achievement),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isUnlocked) Color(0xFFDCFCE7) else MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (isUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (isUnlocked) Color(0xFF15803D) else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = stringResource(if (isUnlocked) R.string.unlocked_badge else R.string.locked_badge),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) Color(0xFF15803D) else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
private fun RecordTile(
    icon: ImageVector,
    title: String,
    value: String,
    subtitle: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
