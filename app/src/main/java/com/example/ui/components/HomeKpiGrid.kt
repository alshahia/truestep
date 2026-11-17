package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.tokens.appShapes
import com.example.ui.theme.tokens.dimens
import com.example.ui.theme.tokens.spacing
import com.example.util.MetricFormatter

@Composable
fun HomeKpiGrid(
    stepsInHour: Int,
    distanceKm: Double,
    caloriesKcal: Int,
    shareOfDayPercent: Int,
    isScrubbing: Boolean,
    isPaused: Boolean,
    modifier: Modifier = Modifier,
    stepsDeltaInHour: Int = 0,
    activeMinutes: Int = 0
) {
    val stepsLabel = if (isScrubbing) {
        if (stepsDeltaInHour > 0) {
            "${stringResource(R.string.elapsed_so_far)} (+${MetricFormatter.formatSteps(stepsDeltaInHour)})"
        } else {
            stringResource(R.string.elapsed_so_far)
        }
    } else {
        stringResource(R.string.today_steps)
    }

    val progressLabel = if (isScrubbing) {
        stringResource(R.string.share_of_day)
    } else if (activeMinutes > 0) {
        "${stringResource(R.string.goal_progress)} • ${stringResource(R.string.active_time_mins_format, activeMinutes)}"
    } else {
        stringResource(R.string.goal_progress)
    }

    val progressIcon = if (isScrubbing) Icons.Default.Schedule else Icons.Default.Flag

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
    ) {
        // Row 1: Steps & Distance (kpi_steps_hour & kpi_distance)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
        ) {
            KpiTile(
                icon = Icons.AutoMirrored.Filled.DirectionsWalk,
                iconTint = Color(0xFF10E599),
                value = MetricFormatter.formatSteps(stepsInHour),
                label = stepsLabel,
                isPaused = isPaused,
                modifier = Modifier
                    .weight(1f)
                    .testTag("kpi_steps_hour")
            )

            KpiTile(
                icon = Icons.Default.Place,
                iconTint = Color(0xFF00F0FF),
                value = MetricFormatter.formatDistance(distanceKm.toFloat()),
                label = stringResource(R.string.km_unit),
                isPaused = isPaused,
                modifier = Modifier
                    .weight(1f)
                    .testTag("kpi_distance")
            )
        }

        // Row 2: Calories & Progress / Active Time (kpi_calories & kpi_share_of_day)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
        ) {
            KpiTile(
                icon = Icons.Default.LocalFireDepartment,
                iconTint = Color(0xFFFF9500),
                value = MetricFormatter.formatCalories(caloriesKcal),
                label = stringResource(R.string.kcal_unit),
                isPaused = isPaused,
                modifier = Modifier
                    .weight(1f)
                    .testTag("kpi_calories")
            )

            KpiTile(
                icon = progressIcon,
                iconTint = MaterialTheme.colorScheme.primary,
                value = MetricFormatter.formatPercentage(shareOfDayPercent),
                label = progressLabel,
                isPaused = isPaused,
                modifier = Modifier
                    .weight(1f)
                    .testTag("kpi_share_of_day")
            )
        }
    }
}

@Composable
private fun KpiTile(
    icon: ImageVector,
    iconTint: Color,
    value: String,
    label: String,
    isPaused: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.appShapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.dimens.elevationLow)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.sm),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isPaused) MaterialTheme.colorScheme.secondary else iconTint,
                        modifier = Modifier.size(MaterialTheme.dimens.iconMedium)
                    )
                }

                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isPaused) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
