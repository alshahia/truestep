package com.example.ui.screens.journey.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.content.GearRegistry
import com.example.domain.model.GearItem
import com.example.domain.model.GearRarity
import com.example.domain.model.GearSlot
import com.example.domain.model.HunterProfile
import com.example.domain.model.VaultCategory
import com.example.ui.theme.tokens.appShapes
import com.example.ui.theme.tokens.dimens
import com.example.ui.theme.tokens.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroLoadoutView(
    hunterProfile: HunterProfile,
    allGearItems: List<GearItem>,
    onEquipGear: (slotType: String, gearId: String) -> Unit,
    onTuneGear: (gearId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(VaultCategory.ALL) }
    var inspectedGear by remember { mutableStateOf<GearItem?>(null) }
    var actionToastText by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    val equippedItems = remember(hunterProfile, allGearItems) {
        val equippedIds = setOf(
            hunterProfile.equippedFootwearId,
            hunterProfile.equippedCoreId,
            hunterProfile.equippedExosuitId,
            hunterProfile.equippedChronoId
        )
        allGearItems.filter { it.id in equippedIds }
    }

    val combatPower = remember(hunterProfile, equippedItems) {
        GearRegistry.calculateCombatPower(
            level = hunterProfile.level,
            streakDays = 7, // or active streak
            equippedItems = equippedItems
        )
    }

    val filteredVaultItems = remember(selectedCategory, allGearItems) {
        if (selectedCategory == VaultCategory.ALL) allGearItems
        else allGearItems.filter { it.category == selectedCategory }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("hero_loadout_view"),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md)
    ) {
        // TOP HERO STATUS / TELEMETRY HUD
        Card(
            shape = MaterialTheme.appShapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth().testTag("hunter_profile_card")
        ) {
            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
            ) {
                // Header Row: Avatar, Name, Title, Combat Power (CP)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
                    ) {
                        // Avatar Badge
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(MaterialTheme.appShapes.large)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SportsMartialArts,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(28.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(2.dp)
                                    .clip(MaterialTheme.appShapes.pill)
                                    .background(Color(0xFF4DFFB2))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "${hunterProfile.level}",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF003822)
                                )
                            }
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = hunterProfile.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "WIND COURIER",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4DFFB2),
                                    modifier = Modifier
                                        .clip(MaterialTheme.appShapes.pill)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "Class: High-Cadence Speedster",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Combat Power (CP) Badge
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .clip(MaterialTheme.appShapes.medium)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = MaterialTheme.spacing.sm, vertical = MaterialTheme.spacing.xs)
                    ) {
                        Text(
                            text = stringResource(R.string.hero_combat_power).uppercase(),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "$combatPower",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "CP",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF4DFFB2),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Currency Vault Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)
                ) {
                    // Sparks
                    Card(
                        shape = MaterialTheme.appShapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(MaterialTheme.spacing.xs),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ElectricBolt,
                                contentDescription = null,
                                tint = Color(0xFF00F0FF),
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(text = "SPARKS", fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = "${hunterProfile.sparks}", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    // Cores
                    Card(
                        shape = MaterialTheme.appShapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(MaterialTheme.spacing.xs),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Memory,
                                contentDescription = null,
                                tint = Color(0xFF4DFFB2),
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(text = "CORES", fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = "${hunterProfile.cores}", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color(0xFF4DFFB2))
                            }
                        }
                    }

                    // Shards
                    Card(
                        shape = MaterialTheme.appShapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(MaterialTheme.spacing.xs),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Diamond,
                                contentDescription = null,
                                tint = Color(0xFF7B61FF),
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(text = "SHARDS", fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = "${hunterProfile.shards}", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color(0xFF7B61FF))
                            }
                        }
                    }
                }

                // Stat Telemetry Chips Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)
                ) {
                    Text(
                        text = "AGI +42% Cadence",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00F0FF),
                        modifier = Modifier
                            .weight(1f)
                            .clip(MaterialTheme.appShapes.pill)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(vertical = MaterialTheme.spacing.xs),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Text(
                        text = "END +15% Stamina",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4DFFB2),
                        modifier = Modifier
                            .weight(1f)
                            .clip(MaterialTheme.appShapes.pill)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(vertical = MaterialTheme.spacing.xs),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Text(
                        text = "AURA +20% EXP",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7B61FF),
                        modifier = Modifier
                            .weight(1f)
                            .clip(MaterialTheme.appShapes.pill)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(vertical = MaterialTheme.spacing.xs),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        // ACTIVE LOADOUT SECTION (4/4 SLOTS TUNED)
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)) {
                    Icon(imageVector = Icons.Default.SportsMartialArts, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    Text(text = stringResource(R.string.active_loadout_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                Text(text = stringResource(R.string.active_loadout_slots), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            // 4 Equipped Slots
            equippedItems.forEach { gear ->
                Card(
                    onClick = { inspectedGear = gear },
                    shape = MaterialTheme.appShapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth().testTag("equipped_gear_${gear.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(MaterialTheme.spacing.md),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(MaterialTheme.appShapes.medium)
                                    .background(Color(gear.rarity.colorHex).copy(alpha = 0.2f))
                            ) {
                                Text(
                                    text = "Lv.${gear.level}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(gear.rarity.colorHex)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = gear.rarity.label,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(gear.rarity.colorHex),
                                        modifier = Modifier
                                            .clip(MaterialTheme.appShapes.pill)
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                    Text(
                                        text = stringResource(gear.nameRes),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Text(
                                    text = gear.statBonusText,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF4DFFB2),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "⚡ Perk: ${gear.perkText}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        FilledTonalButton(
                            onClick = {
                                onTuneGear(gear.id)
                                actionToastText = "Tuned ${context.getString(gear.nameRes)} (+2 CP)!"
                            },
                            shape = MaterialTheme.appShapes.pill,
                            modifier = Modifier.testTag("tune_button_${gear.id}")
                        ) {
                            Text(text = stringResource(R.string.gear_tune_button), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(imageVector = Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }

        // CYBERNETIC FORGE SHORTCUT BANNER
        Card(
            shape = MaterialTheme.appShapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().testTag("cybernetic_forge_banner")
        ) {
            Row(
                modifier = Modifier.padding(MaterialTheme.spacing.md),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(MaterialTheme.appShapes.medium)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    ) {
                        Icon(imageVector = Icons.Default.PrecisionManufacturing, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                    Column {
                        Text(text = stringResource(R.string.gear_forge_title), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(text = stringResource(R.string.gear_forge_desc), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Button(
                    onClick = {
                        actionToastText = "Forge Synchronized! Shards distilled into gear power."
                    },
                    shape = MaterialTheme.appShapes.pill,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.testTag("forge_enhance_button")
                ) {
                    Text(text = stringResource(R.string.gear_forge_enhance), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // STORAGE VAULT (BENTO GRID WITH TABS)
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.storage_vault_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = stringResource(R.string.vault_capacity_format, filteredVaultItems.size, 80), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            // Category Filter Pills
            LazyRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)) {
                items(VaultCategory.entries) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(text = stringResource(cat.titleRes), fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            // Bento Grid: 2 columns
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)) {
                filteredVaultItems.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)
                    ) {
                        rowItems.forEach { item ->
                            val isEquipped = item.id in listOf(
                                hunterProfile.equippedFootwearId,
                                hunterProfile.equippedCoreId,
                                hunterProfile.equippedExosuitId,
                                hunterProfile.equippedChronoId
                            )
                            val isLocked = hunterProfile.level < item.requiredHunterLevel

                            Card(
                                onClick = { inspectedGear = item },
                                shape = MaterialTheme.appShapes.medium,
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier.weight(1f).testTag("vault_card_${item.id}")
                            ) {
                                Column(
                                    modifier = Modifier.padding(MaterialTheme.spacing.sm),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = item.rarity.label,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(item.rarity.colorHex)
                                        )
                                        if (isEquipped) {
                                            Text(
                                                text = stringResource(R.string.gear_equipped_badge).uppercase(),
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF4DFFB2)
                                            )
                                        } else if (isLocked) {
                                            Text(
                                                text = "REQ. LV.${item.requiredHunterLevel}",
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFF59E0B)
                                            )
                                        }
                                    }

                                    Text(
                                        text = stringResource(item.nameRes),
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )

                                    Text(
                                        text = item.statBonusText,
                                        fontSize = 10.sp,
                                        color = if (isLocked) MaterialTheme.colorScheme.onSurfaceVariant else Color(0xFF4DFFB2),
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    item.craftProgress?.let { prog ->
                                        LinearProgressIndicator(
                                            progress = { prog },
                                            modifier = Modifier.fillMaxWidth().height(4.dp).clip(MaterialTheme.appShapes.pill),
                                            color = MaterialTheme.colorScheme.primary,
                                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    }

                                    // Action Button
                                    if (isEquipped) {
                                        FilledTonalButton(
                                            onClick = { inspectedGear = item },
                                            modifier = Modifier.fillMaxWidth().height(32.dp),
                                            shape = MaterialTheme.appShapes.pill
                                        ) {
                                            Text(text = stringResource(R.string.gear_tune_button), fontSize = 10.sp)
                                        }
                                    } else if (isLocked) {
                                        Button(
                                            onClick = { /* Locked */ },
                                            enabled = false,
                                            modifier = Modifier.fillMaxWidth().height(32.dp),
                                            shape = MaterialTheme.appShapes.pill
                                        ) {
                                            Text(text = stringResource(R.string.gear_locked_badge), fontSize = 10.sp)
                                        }
                                    } else if (item.isConsumable) {
                                        Button(
                                            onClick = {
                                                actionToastText = "Activated ${context.getString(item.nameRes)}!"
                                            },
                                            modifier = Modifier.fillMaxWidth().height(32.dp),
                                            shape = MaterialTheme.appShapes.pill,
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4DFFB2), contentColor = Color(0xFF003822))
                                        ) {
                                            Text(text = "ACTIVATE", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        Button(
                                            onClick = {
                                                onEquipGear(item.slot.name, item.id)
                                                actionToastText = "Equipped ${context.getString(item.nameRes)}!"
                                            },
                                            modifier = Modifier.fillMaxWidth().height(32.dp),
                                            shape = MaterialTheme.appShapes.pill
                                        ) {
                                            Text(text = stringResource(R.string.gear_equip_button), fontSize = 10.sp)
                                        }
                                    }
                                }
                            }
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // Action Toast Feedback
        actionToastText?.let { toast ->
            Card(
                shape = MaterialTheme.appShapes.pill,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = toast,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.xs),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }

    // GEAR INSPECTION MODAL DRAWER
    inspectedGear?.let { gear ->
        ModalBottomSheet(
            onDismissRequest = { inspectedGear = null },
            sheetState = sheetState,
            shape = MaterialTheme.appShapes.bottomSheet
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.xl, vertical = MaterialTheme.spacing.md)
                    .testTag("gear_inspection_drawer"),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)) {
                        Text(
                            text = gear.rarity.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(gear.rarity.colorHex),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(MaterialTheme.appShapes.pill)
                                .background(Color(gear.rarity.colorHex).copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                        Text(
                            text = stringResource(gear.nameRes),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = { inspectedGear = null }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                }

                Card(
                    shape = MaterialTheme.appShapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(MaterialTheme.spacing.md),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)
                    ) {
                        Text(text = stringResource(gear.descRes), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "Stat Profile: ${gear.statBonusText}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF4DFFB2))
                        Text(text = "Active Perk: ${gear.perkText}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                        Text(text = "Combat Power: +${gear.cpBonus} CP", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
                ) {
                    FilledTonalButton(
                        onClick = {
                            onTuneGear(gear.id)
                            inspectedGear = null
                            actionToastText = "Overclock matrix synchronized (+2 CP)!"
                        },
                        shape = MaterialTheme.appShapes.medium,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(R.string.tune_stats_title))
                    }

                    Button(
                        onClick = {
                            onEquipGear(gear.slot.name, gear.id)
                            inspectedGear = null
                            actionToastText = "Equipped ${context.getString(gear.nameRes)}!"
                        },
                        shape = MaterialTheme.appShapes.medium,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(R.string.confirm_loadout_title))
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))
            }
        }
    }
}
