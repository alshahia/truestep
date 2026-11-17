package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.UserGoalSettings
import com.example.ui.viewmodel.StepUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoalsScreen(
    uiState: StepUiState,
    onUpdateGoal: (Int) -> Unit,
    onUpdateDetailedGoals: (min: Int, target: Int, stretch: Int) -> Unit,
    onToggleAdaptiveGoals: (Boolean) -> Unit,
    onToggleLockGoals: (Boolean) -> Unit,
    onToggleRecoveryMode: (Boolean) -> Unit,
    onRecalculateAdaptiveGoal: () -> Unit,
    onUpdateGoalSettings: (UserGoalSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = uiState.userGoalSettings ?: UserGoalSettings()

    var targetGoal by remember(uiState.goalSteps) { mutableIntStateOf(uiState.goalSteps) }
    var minGoal by remember(uiState.minGoal) { mutableIntStateOf(uiState.minGoal) }
    var stretchGoal by remember(uiState.stretchGoal) { mutableIntStateOf(uiState.stretchGoal) }

    var weekendEnabled by remember(settings.weekendAdjustmentEnabled) {
        mutableStateOf(settings.weekendAdjustmentEnabled)
    }
    var weekendGoal by remember(settings.weekendTargetGoal) {
        mutableIntStateOf(settings.weekendTargetGoal)
    }

    val daysOfWeek = listOf(
        "Monday" to stringResource(R.string.day_monday),
        "Tuesday" to stringResource(R.string.day_tuesday),
        "Wednesday" to stringResource(R.string.day_wednesday),
        "Thursday" to stringResource(R.string.day_thursday),
        "Friday" to stringResource(R.string.day_friday),
        "Saturday" to stringResource(R.string.day_saturday),
        "Sunday" to stringResource(R.string.day_sunday)
    )
    var selectedRestDays by remember(settings.restDays) {
        mutableStateOf(settings.restDays.split(",").map { it.trim() }.filter { it.isNotBlank() }.toSet())
    }

    var recoveryDays by remember { mutableIntStateOf(7) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.goals_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.goals_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 1. Three-Tier Goal Overview Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)),
            modifier = Modifier.fillMaxWidth().testTag("goals_tier_overview_card")
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Flag, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.three_daily_tiers_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    GoalTierBadge(stringResource(R.string.tier_minimum_title), minGoal, stringResource(R.string.tier_minimum_sub))
                    GoalTierBadge(stringResource(R.string.tier_target_title), targetGoal, stringResource(R.string.tier_target_sub))
                    GoalTierBadge(stringResource(R.string.tier_stretch_title), stretchGoal, stringResource(R.string.tier_stretch_sub))
                }

                Text(
                    text = stringResource(R.string.three_tiers_explanation),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 2. Target Goal Adjuster
        ElevatedCard(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("target_goal_adjuster_card")
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.daily_target_goal_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (targetGoal > 2000) {
                                targetGoal -= 500
                                minGoal = (targetGoal * 0.6).toInt()
                                stretchGoal = (targetGoal * 1.2).toInt()
                                onUpdateDetailedGoals(minGoal, targetGoal, stretchGoal)
                            }
                        },
                        modifier = Modifier.testTag("target_goal_decrement")
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = stringResource(R.string.decrease_goal))
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "%,d".format(targetGoal),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = stringResource(R.string.steps_unit),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = {
                            if (targetGoal < 40000) {
                                targetGoal += 500
                                minGoal = (targetGoal * 0.6).toInt()
                                stretchGoal = (targetGoal * 1.2).toInt()
                                onUpdateDetailedGoals(minGoal, targetGoal, stretchGoal)
                            }
                        },
                        modifier = Modifier.testTag("target_goal_increment")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.increase_goal))
                    }
                }

                Slider(
                    value = targetGoal.toFloat(),
                    onValueChange = { value ->
                        val rounded = ((value / 250).toInt()) * 250
                        targetGoal = rounded
                        minGoal = (rounded * 0.6).toInt()
                        stretchGoal = (rounded * 1.2).toInt()
                    },
                    onValueChangeFinished = {
                        onUpdateDetailedGoals(minGoal, targetGoal, stretchGoal)
                    },
                    valueRange = 2000f..30000f,
                    modifier = Modifier.fillMaxWidth().testTag("target_goal_slider")
                )

                // Quick Presets
                Text(
                    text = stringResource(R.string.quick_presets),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val presets = listOf(
                        4000 to stringResource(R.string.preset_4k),
                        7500 to stringResource(R.string.preset_7_5k),
                        10000 to stringResource(R.string.preset_10k),
                        12500 to stringResource(R.string.preset_12_5k)
                    )
                    presets.forEach { (presetSteps, label) ->
                        FilterChip(
                            selected = targetGoal == presetSteps,
                            onClick = {
                                targetGoal = presetSteps
                                minGoal = (presetSteps * 0.6).toInt()
                                stretchGoal = (presetSteps * 1.2).toInt()
                                onUpdateDetailedGoals(minGoal, targetGoal, stretchGoal)
                            },
                            label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // 3. Adaptive Smart Goals Card
        ElevatedCard(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("adaptive_goals_card")
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.adaptive_goals),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.adaptive_goals_adjust_sub),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = settings.adaptiveGoalsEnabled,
                        onCheckedChange = { onToggleAdaptiveGoals(it) },
                        modifier = Modifier.testTag("adaptive_goals_switch")
                    )
                }

                Text(
                    text = stringResource(R.string.adaptive_goals_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FilledTonalButton(
                    onClick = onRecalculateAdaptiveGoal,
                    modifier = Modifier.fillMaxWidth().testTag("recalculate_baseline_button")
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.recalculate_from_baseline_btn))
                }

                // Lock Goals Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = if (settings.lockedGoalEnabled) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.lock_goals),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = stringResource(R.string.lock_goals_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = settings.lockedGoalEnabled,
                        onCheckedChange = { onToggleLockGoals(it) },
                        modifier = Modifier.testTag("lock_goals_switch")
                    )
                }
            }
        }

        // 4. Weekday vs Weekend Goals Card
        ElevatedCard(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("weekend_goals_card")
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(imageVector = Icons.Default.Weekend, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.weekend_target_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.weekend_target_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = weekendEnabled,
                        onCheckedChange = {
                            weekendEnabled = it
                            onUpdateGoalSettings(settings.copy(weekendAdjustmentEnabled = it, weekendTargetGoal = weekendGoal))
                        },
                        modifier = Modifier.testTag("weekend_goal_switch")
                    )
                }

                AnimatedVisibility(visible = weekendEnabled) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(stringResource(R.string.weekend_target_label), style = MaterialTheme.typography.bodyMedium)
                            Text(stringResource(R.string.steps_count_format, weekendGoal), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Slider(
                            value = weekendGoal.toFloat(),
                            onValueChange = {
                                weekendGoal = ((it / 250).toInt()) * 250
                            },
                            onValueChangeFinished = {
                                onUpdateGoalSettings(settings.copy(weekendAdjustmentEnabled = true, weekendTargetGoal = weekendGoal))
                            },
                            valueRange = 2000f..25000f,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // 5. Rest Days Selector
        ElevatedCard(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("rest_days_card")
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Bedtime, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.rest_days_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = stringResource(R.string.rest_days_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    daysOfWeek.forEach { (dayKey, dayLabel) ->
                        val isSelected = selectedRestDays.contains(dayKey)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                val updated = if (isSelected) selectedRestDays - dayKey else selectedRestDays + dayKey
                                selectedRestDays = updated
                                val restDaysString = updated.joinToString(",")
                                onUpdateGoalSettings(settings.copy(restDays = restDaysString))
                            },
                            label = { Text(dayLabel) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    }
                }
            }
        }

        // 6. Recovery Mode Card
        ElevatedCard(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("recovery_mode_card")
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(imageVector = Icons.Default.Healing, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.recovery_mode),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.recovery_mode_lower_sub),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = settings.recoveryModeEnabled,
                        onCheckedChange = { onToggleRecoveryMode(it) },
                        modifier = Modifier.testTag("recovery_mode_switch")
                    )
                }

                Text(
                    text = stringResource(R.string.recovery_mode_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                AnimatedVisibility(visible = settings.recoveryModeEnabled) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            3 to stringResource(R.string.days_count_format, 3),
                            7 to stringResource(R.string.days_count_format, 7),
                            14 to stringResource(R.string.days_count_format, 14)
                        ).forEach { (days, label) ->
                            FilterChip(
                                selected = recoveryDays == days,
                                onClick = { recoveryDays = days },
                                label = { Text(label) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // 7. Reset Default Goals Button
        OutlinedButton(
            onClick = {
                targetGoal = 10000
                minGoal = 6000
                stretchGoal = 12000
                weekendEnabled = false
                selectedRestDays = setOf("Sunday")
                onUpdateDetailedGoals(6000, 10000, 12000)
                onUpdateGoalSettings(
                    settings.copy(
                        targetGoal = 10000,
                        minimumGoal = 6000,
                        stretchGoal = 12000,
                        adaptiveGoalsEnabled = true,
                        lockedGoalEnabled = false,
                        restDays = "Sunday",
                        recoveryModeEnabled = false,
                        weekendAdjustmentEnabled = false
                    )
                )
            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("reset_default_goals_button")
        ) {
            Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.restore_default_goals))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun GoalTierBadge(title: String, steps: Int, subtitle: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = "%,d".format(steps),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary, fontSize = 10.sp)
    }
}
