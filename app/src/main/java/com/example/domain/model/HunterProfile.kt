package com.example.domain.model

data class HunterProfile(
    val name: String = "Ren Amamiya",
    val classTitle: String = "Wind Courier • Speedster",
    val level: Int = 14,
    val currentExp: Int = 1850,
    val expToNextLevel: Int = 2500,
    val rankTitle: String = "B-RANK",
    val combatPower: Int = 4820,
    val sparks: Int = 48,
    val cores: Int = 6,
    val shards: Int = 1420,
    val manaOverdrivePercent: Int = 78,
    val isAuraMultiplierActive: Boolean = true,
    val auraMultiplier: Float = 1.25f,
    val equippedFootwearId: String = "gear_neon_phantom",
    val equippedCoreId: String = "gear_zephyr_engine",
    val equippedExosuitId: String = "gear_nanoweave_vest",
    val equippedChronoId: String = "gear_chrono_watch",
    val agilityBonusPercent: Int = 42,
    val staminaBonusPercent: Int = 15,
    val expBonusPercent: Int = 20,
    val fortuneBonusPercent: Int = 18
)
