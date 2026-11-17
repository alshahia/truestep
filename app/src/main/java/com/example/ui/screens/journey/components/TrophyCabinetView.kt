package com.example.ui.screens.journey.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.domain.content.AchievementRegistry
import com.example.ui.theme.tokens.appShapes
import com.example.ui.theme.tokens.dimens
import com.example.ui.theme.tokens.spacing

@Composable
fun TrophyCabinetView(
    unlockedAchievementIds: Set<String>,
    modifier: Modifier = Modifier
) {
    val allAchievements = AchievementRegistry.allAchievements
    val unlockedCount = allAchievements.count { unlockedAchievementIds.contains(it.id) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("trophy_cabinet_view"),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md)
    ) {
        // Cabinet Header Card
        Card(
            shape = MaterialTheme.appShapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(MaterialTheme.spacing.lg),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.tab_trophies),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$unlockedCount / ${allAchievements.size} unlocked",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Box(
                    modifier = Modifier
                        .size(MaterialTheme.dimens.minTouchTarget)
                        .clip(CircleShape)
                        .background(Color(0xFFF59E0B).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🏆", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        // Trophies List
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
            allAchievements.forEach { achievement ->
                val isUnlocked = unlockedAchievementIds.contains(achievement.id)

                Card(
                    shape = MaterialTheme.appShapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = if (isUnlocked) {
                            MaterialTheme.colorScheme.surface
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (isUnlocked) Modifier.border(
                                width = 1.dp,
                                color = Color(0xFFF59E0B).copy(alpha = 0.5f),
                                shape = MaterialTheme.appShapes.medium
                            ) else Modifier
                        )
                        .testTag("trophy_item_${achievement.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(MaterialTheme.spacing.md),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(MaterialTheme.dimens.minTouchTarget)
                                    .clip(CircleShape)
                                    .background(
                                        if (isUnlocked) Color(0xFFF59E0B).copy(alpha = 0.15f)
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = achievement.iconEmoji,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.md))

                            Column {
                                Text(
                                    text = stringResource(achievement.titleRes),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = stringResource(achievement.requirementRes),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (isUnlocked) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(MaterialTheme.dimens.iconMedium)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outlineVariant,
                                modifier = Modifier.size(MaterialTheme.dimens.iconSmall)
                            )
                        }
                    }
                }
            }
        }
    }
}
