package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.R
import com.example.ui.screens.journey.components.DailyQuestCard
import com.example.ui.screens.journey.components.ExpeditionTrailCard
import com.example.ui.screens.journey.components.HeroLoadoutView
import com.example.ui.screens.journey.components.StreakVaultCard
import com.example.ui.screens.journey.components.TrophyCabinetView
import com.example.ui.theme.tokens.appShapes
import com.example.ui.theme.tokens.spacing
import com.example.ui.viewmodel.StepUiState

private enum class JourneyTab(
    val titleRes: Int,
    val icon: ImageVector,
    val tag: String
) {
    QUESTS(R.string.tab_quests, Icons.Default.TaskAlt, "tab_quests"),
    HERO(R.string.tab_hero, Icons.Default.SportsMartialArts, "tab_hero"),
    EXPEDITIONS(R.string.tab_expeditions, Icons.Default.Explore, "tab_expeditions"),
    STREAK_VAULT(R.string.tab_streak_vault, Icons.Default.LocalFireDepartment, "tab_streak_vault"),
    TROPHIES(R.string.tab_trophies, Icons.Default.EmojiEvents, "tab_trophies")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneyScreen(
    uiState: StepUiState,
    onEquipGear: (slotType: String, gearId: String) -> Unit = { _, _ -> },
    onTuneGear: (gearId: String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = JourneyTab.entries

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = MaterialTheme.spacing.lg, vertical = MaterialTheme.spacing.md)
            .testTag("journey_screen"),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md)
    ) {
        // Screen Header
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.journey_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.journey_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Primary Tab Row
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("journey_tab_row"),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedTabIndex == index
                Tab(
                    selected = isSelected,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = stringResource(tab.titleRes),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = stringResource(tab.titleRes)
                        )
                    },
                    modifier = Modifier.testTag(tab.tag)
                )
            }
        }

        // Tab Content
        when (tabs[selectedTabIndex]) {
            JourneyTab.QUESTS -> {
                Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
                    // Quests Header Card
                    Card(
                        shape = MaterialTheme.appShapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(MaterialTheme.spacing.md),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)
                        ) {
                            Text(
                                text = stringResource(R.string.daily_quests_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(
                                    R.string.home_quests_glance_format,
                                    uiState.completedQuestsCount,
                                    uiState.totalQuestsCount
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (uiState.completedQuestsCount == uiState.totalQuestsCount && uiState.totalQuestsCount > 0) {
                                Text(
                                    text = stringResource(R.string.quest_all_completed),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    // Render list of quests
                    uiState.dailyQuests.forEach { quest ->
                        DailyQuestCard(quest = quest)
                    }
                }
            }

            JourneyTab.HERO -> {
                HeroLoadoutView(
                    hunterProfile = uiState.hunterProfile,
                    allGearItems = uiState.allGearItems,
                    onEquipGear = onEquipGear,
                    onTuneGear = onTuneGear
                )
            }

            JourneyTab.EXPEDITIONS -> {
                val allTimeKm = uiState.allHistory.sumOf { it.cumulativeKm } + uiState.cumulativeKm
                ExpeditionTrailCard(
                    allTimeKm = allTimeKm,
                    nextDestination = uiState.nextDestination
                )
            }

            JourneyTab.STREAK_VAULT -> {
                StreakVaultCard(
                    currentStreak = uiState.streak,
                    bestStreak = uiState.bestStreak,
                    bankedFreezes = uiState.bankedStreakFreezes,
                    isProtectedToday = uiState.isStreakProtectedToday
                )
            }

            JourneyTab.TROPHIES -> {
                TrophyCabinetView(
                    unlockedAchievementIds = uiState.unlockedAchievementIds
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.xl))
    }
}
