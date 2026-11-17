package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.local.DailySteps
import com.example.ui.components.DayDetailSheet
import com.example.ui.viewmodel.StepUiState

enum class HistoryFilter {
    ALL,
    GOALS_MET,
    WITH_NOTES
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    uiState: StepUiState,
    onSaveNotes: (date: String, notes: String) -> Unit,
    onNavigateToAnalytics: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var activeFilter by remember { mutableStateOf(HistoryFilter.ALL) }
    var selectedDayForDetail by remember { mutableStateOf<DailySteps?>(null) }
    val sheetState = rememberModalBottomSheetState()

    val allDays = uiState.allHistory

    val filteredDays = remember(allDays, activeFilter) {
        when (activeFilter) {
            HistoryFilter.ALL -> allDays
            HistoryFilter.GOALS_MET -> allDays.filter { it.totalSteps >= it.goalSteps }
            HistoryFilter.WITH_NOTES -> allDays.filter { it.notes.isNotBlank() }
        }
    }

    val averageSteps = remember(allDays) {
        if (allDays.isEmpty()) 0 else allDays.sumOf { it.totalSteps } / allDays.size
    }

    val bestDay = remember(allDays) {
        allDays.maxByOrNull { it.totalSteps }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Screen Title & Summary Strip
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.daily_records_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            FilledTonalButton(
                onClick = onNavigateToAnalytics,
                modifier = Modifier.testTag("history_analytics_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Insights,
                    contentDescription = stringResource(R.string.analytics_title),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = stringResource(R.string.nav_analytics))
            }
        }

        // Summary Hero Card (Lifetime Average & Best Day)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.weekly_average),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "%,d".format(averageSteps),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (bestDay != null) {
                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.height(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(R.string.best_day),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "%,d".format(bestDay.totalSteps),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Filter chips (All, Goals Met, Notes)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = activeFilter == HistoryFilter.ALL,
                onClick = { activeFilter = HistoryFilter.ALL },
                label = { Text(stringResource(R.string.all_filter, allDays.size), style = MaterialTheme.typography.labelSmall) },
                modifier = Modifier.testTag("filter_all_chip")
            )
            FilterChip(
                selected = activeFilter == HistoryFilter.GOALS_MET,
                onClick = { activeFilter = HistoryFilter.GOALS_MET },
                label = { Text(stringResource(R.string.goals_met_filter), style = MaterialTheme.typography.labelSmall) },
                modifier = Modifier.testTag("filter_goals_met_chip")
            )
            FilterChip(
                selected = activeFilter == HistoryFilter.WITH_NOTES,
                onClick = { activeFilter = HistoryFilter.WITH_NOTES },
                label = { Text(stringResource(R.string.with_notes_filter), style = MaterialTheme.typography.labelSmall) },
                modifier = Modifier.testTag("filter_with_notes_chip")
            )
        }

        // History Daily Records List
        if (filteredDays.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_history_yet),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredDays, key = { it.date }) { day ->
                    DayRowItem(
                        day = day,
                        onClick = { selectedDayForDetail = day }
                    )
                }
            }
        }
    }

    if (selectedDayForDetail != null) {
        DayDetailSheet(
            day = selectedDayForDetail,
            sheetState = sheetState,
            onDismiss = { selectedDayForDetail = null },
            onSaveNotes = onSaveNotes
        )
    }
}

@Composable
private fun DayRowItem(
    day: DailySteps,
    onClick: () -> Unit
) {
    val isAchieved = day.totalSteps >= day.goalSteps
    val progress = (day.totalSteps.toFloat() / day.goalSteps).coerceIn(0f, 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("day_row_${day.date}"),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = day.date,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (day.isRestDay) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.rest_badge),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                    if (day.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = stringResource(R.string.has_note),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.height(16.dp)
                        )
                    }
                }

                if (day.notes.isNotBlank()) {
                    Text(
                        text = day.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isAchieved) Color(0xFF10B981) else MaterialTheme.colorScheme.primary)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "%,d".format(day.totalSteps),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isAchieved) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "%.1f km".format(day.cumulativeKm),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
