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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.WalkingSession
import com.example.ui.components.CelebrationDialog
import com.example.ui.components.DailyStreakCard
import com.example.ui.components.HeroStepRing
import com.example.ui.components.HomeKpiGrid
import com.example.ui.components.HourlyScrubRibbon
import com.example.ui.components.PausedBanner
import com.example.ui.components.RatingReviewDialog
import com.example.ui.components.WalkingSessionSheet
import com.example.ui.components.WorkoutSummarySheet
import com.example.ui.model.HistoryChartRange
import com.example.ui.theme.tokens.appShapes
import com.example.ui.theme.tokens.dimens
import com.example.ui.theme.tokens.spacing
import com.example.ui.viewmodel.StepUiState
import com.example.util.MetricFormatter
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: StepUiState,
    onTogglePause: () -> Unit,
    onUpdateGoal: (Int) -> Unit,
    onScrubHour: (Int?) -> Unit,
    onSelectRange: (HistoryChartRange) -> Unit = {},
    onNavigateToDestination: () -> Unit = {},
    onNavigateToJourney: () -> Unit = {},
    onNavigateToGoals: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onSaveWalkingSession: (WalkingSession) -> Unit = {},
    onDeleteWalkingSession: (Long) -> Unit = {},
    onOpenTrustCenter: () -> Unit = {},
    onDismissCelebration: () -> Unit = {},
    onShareCelebration: (android.content.Context) -> Unit = {},
    onDismissReviewPrompt: (Boolean) -> Unit = {},
    onRateOnPlayStore: (android.content.Context) -> Unit = {},
    onSendFeedback: (android.content.Context) -> Unit = {},
    onRateApp: (android.content.Context) -> Unit = {},
    onClaimRaidRewards: (Int, Int, Int) -> Unit = { _, _, _ -> },
    onDismissWorkoutSummary: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showGoalDialog by remember { mutableStateOf(false) }
    var showWalkingSessionSheet by remember { mutableStateOf(false) }
    val walkingSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val timeFormatter = remember { DateTimeFormatter.ofPattern("h:mm a").withZone(ZoneId.systemDefault()) }
    val isStale = remember(uiState.lastUpdated) {
        val ageMillis = System.currentTimeMillis() - uiState.lastUpdated
        ageMillis > 3_600_000L && uiState.currentSteps > 0
    }
    val syncTimeFormatted = remember(uiState.lastUpdated) {
        timeFormatter.format(Instant.ofEpochMilli(uiState.lastUpdated))
    }

    val currentHour = remember { LocalTime.now().hour }
    val greetingText = when {
        currentHour in 5..11 -> stringResource(R.string.greeting_morning_user)
        currentHour in 12..17 -> stringResource(R.string.greeting_afternoon_user)
        else -> stringResource(R.string.greeting_evening_user)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = MaterialTheme.spacing.lg, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // 1. Prominent Sticky Paused Banner (Visible immediately at the top when paused)
        if (uiState.isPaused) {
            PausedBanner(modifier = Modifier.fillMaxWidth())
        }

        // 2. Scrub Mode Exit Banner (Visible immediately at the top when an hour is inspected)
        if (uiState.isScrubbing && uiState.selectedScrubHour != null) {
            val formattedHour = String.format(java.util.Locale.getDefault(), "%02d:00", uiState.selectedScrubHour)
            Card(
                shape = MaterialTheme.appShapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("scrub_exit_banner")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.xs),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(
                            R.string.scrub_viewing_hour_format,
                            formattedHour,
                            MetricFormatter.formatSteps(uiState.hourSteps)
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    TextButton(
                        onClick = { onScrubHour(null) },
                        modifier = Modifier.testTag("scrub_exit_button")
                    ) {
                        Text(
                            text = stringResource(R.string.return_to_full_day),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // 3. Compact Greeting Row (Matching Prototype 03 with quick Journey shortcut)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.xs),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = greetingText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            IconButton(
                onClick = onNavigateToJourney,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Explore,
                    contentDescription = stringResource(R.string.home_view_journey),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 4. Hero Step Counter Card (Progress Ring with Supportive Caption & Safe Pause Toggle)
        HeroStepRing(
            steps = uiState.displaySteps,
            goal = uiState.goalSteps,
            percent = uiState.displayPercent,
            isPaused = uiState.isPaused,
            onTogglePause = onTogglePause,
            onEditGoal = { showGoalDialog = true },
            modifier = Modifier.fillMaxWidth()
        )

        // 5. Above-the-Fold 2x2 Metric KPI Grid (Active Minutes, Distance, Calories, Progress)
        HomeKpiGrid(
            stepsInHour = uiState.displaySteps,
            distanceKm = uiState.displayKm,
            caloriesKcal = uiState.displayCalories,
            shareOfDayPercent = if (uiState.isScrubbing) uiState.hourSharePercent else uiState.displayPercent,
            isScrubbing = uiState.isScrubbing,
            isPaused = uiState.isPaused,
            stepsDeltaInHour = if (uiState.isScrubbing) uiState.hourSteps else 0,
            activeMinutes = uiState.activeMinutes,
            modifier = Modifier.fillMaxWidth()
        )

        // 6. Trust & Transparency Status Strip (Directly below KPI grid, preserving immediate clickability)
        Card(
            onClick = onOpenTrustCenter,
            shape = MaterialTheme.appShapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = if (isStale) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("today_trust_status_card")
        ) {
            Column(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.md, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(MaterialTheme.spacing.sm)
                                .clip(CircleShape)
                                .background(if (isStale) MaterialTheme.colorScheme.error else Color(0xFF2E7D32))
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.sm))
                        Text(
                            text = stringResource(R.string.tracking_active),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Devices,
                            contentDescription = null,
                            modifier = Modifier.size(MaterialTheme.dimens.iconSmall),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.xs))
                        Text(
                            text = uiState.dataSource,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = if (uiState.confidence == "HIGH") stringResource(R.string.confidence_badge_high)
                        else stringResource(R.string.confidence_badge_reconciled),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Freshness / Stale warning
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isStale) Icons.Default.Warning else Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(MaterialTheme.dimens.iconSmall),
                        tint = if (isStale) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.xs))
                    Text(
                        text = if (isStale) {
                            stringResource(R.string.data_stale)
                        } else {
                            stringResource(R.string.data_current, syncTimeFormatted)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isStale) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 7. Daily Progress Streak Card (7-Day Streak Pill + Weekday Dot Markers)
        DailyStreakCard(
            streakDays = uiState.streak,
            isGoalMetToday = uiState.currentSteps >= uiState.goalSteps && uiState.goalSteps > 0,
            onClick = onNavigateToJourney,
            modifier = Modifier.fillMaxWidth()
        )

        // 8. Primary Thumb-Friendly Action CTA (Full-width pill button matching Prototype 03)
        Button(
            onClick = { showWalkingSessionSheet = true },
            shape = MaterialTheme.appShapes.pill,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E60FF) // Radiant Cobalt Blue
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("start_walk_session_cta")
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.xs))
            Text(
                text = stringResource(R.string.nav_walk_session),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // 9. Today's Activity 24-Hour Bar Ribbon (Hour-by-hour scrub ribbon)
        HourlyScrubRibbon(
            hourlySteps = uiState.hourlySteps,
            selectedHour = uiState.selectedScrubHour,
            onScrubHour = onScrubHour,
            modifier = Modifier.fillMaxWidth()
        )

        // 10. Dedicated Goal Tiers Overview Card (Decoupled & Accessible)
        Card(
            shape = MaterialTheme.appShapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("home_goal_tiers_card")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToGoals)
                    .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.sm)
                    .testTag("home_goal_tiers_row"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.goal_min_inline_format, uiState.minGoal),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(R.string.goal_target_inline_format, uiState.goalSteps),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.goal_stretch_inline_format, uiState.stretchGoal),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = stringResource(R.string.goals_title),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(MaterialTheme.dimens.iconSmall)
                )
            }
        }

        // 11. Cadence Peak Insight Callout Card
        Card(
            shape = MaterialTheme.appShapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("home_cadence_insight_card")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF00F0FF).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = Color(0xFF00F0FF),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.md))
                Text(
                    text = stringResource(R.string.home_cadence_insight),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))
    }

    // Walking Session Bottom Sheet
    if (showWalkingSessionSheet) {
        WalkingSessionSheet(
            sheetState = walkingSheetState,
            walkingSessions = uiState.walkingSessions,
            currentDaySteps = uiState.currentSteps,
            onSaveSession = onSaveWalkingSession,
            onDeleteSession = onDeleteWalkingSession,
            onDismiss = { showWalkingSessionSheet = false }
        )
    }

    // Goal Edit Dialog
    if (showGoalDialog) {
        var goalInput by remember { mutableStateOf(uiState.goalSteps.toString()) }
        val presets = listOf(4000, 6000, 8000, 10000, 12000)

        AlertDialog(
            onDismissRequest = { showGoalDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.edit_goal),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md)) {
                    Text(
                        text = stringResource(R.string.goals_title),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = goalInput,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() } && input.length <= 6) {
                                goalInput = input
                            }
                        },
                        label = { Text(stringResource(R.string.target_goal_label)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("goal_dialog_input")
                    )

                    Text(
                        text = stringResource(R.string.quick_presets),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)
                    ) {
                        presets.forEach { preset ->
                            FilterChip(
                                selected = goalInput == preset.toString(),
                                onClick = { goalInput = preset.toString() },
                                label = {
                                    Text(
                                        text = "${preset / 1000}k",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val parsed = goalInput.toIntOrNull()
                        if (parsed != null && parsed in 1000..100000) {
                            onUpdateGoal(parsed)
                        }
                        showGoalDialog = false
                    },
                    modifier = Modifier.testTag("goal_dialog_confirm")
                ) {
                    Text(stringResource(R.string.save), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showGoalDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    if (uiState.showCelebrationDialog) {
        val context = LocalContext.current
        CelebrationDialog(
            steps = uiState.currentSteps,
            distanceKm = uiState.cumulativeKm,
            streak = uiState.streak,
            onShare = { onShareCelebration(context) },
            onDismiss = onDismissCelebration,
            onRateApp = { onRateApp(context) }
        )
    }

    if (uiState.showReviewPromptDialog) {
        val context = LocalContext.current
        RatingReviewDialog(
            onRateOnStore = { onRateOnPlayStore(context) },
            onSendFeedback = { onSendFeedback(context) },
            onDismiss = { onDismissReviewPrompt(false) },
            onNeverAskAgain = { onDismissReviewPrompt(true) }
        )
    }

    // Post-Workout Raid Summary Sheet
    if (uiState.showWorkoutSummary && uiState.lastCompletedSession != null) {
        val summarySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        WorkoutSummarySheet(
            sheetState = summarySheetState,
            session = uiState.lastCompletedSession,
            hunterName = uiState.hunterProfile.name,
            hunterLevel = uiState.hunterProfile.level,
            onClaimRewards = onClaimRaidRewards,
            onDismiss = onDismissWorkoutSummary
        )
    }
}
