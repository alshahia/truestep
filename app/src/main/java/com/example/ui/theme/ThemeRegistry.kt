package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Top-level background functions (K2 FIR compiler workaround)
private val classicTealBg: @Composable BoxScope.() -> Unit = {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE6F4F1), Color(0xFFF7FBFB))
                )
            )
    )
}

private val classicTealDarkBg: @Composable BoxScope.() -> Unit = {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF101414), Color(0xFF141E1E))
                )
            )
    )
}

private val neonFuturisticBg: @Composable BoxScope.() -> Unit = {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    listOf(Color(0xFF1E1035), Color(0xFF090614))
                )
            )
    )
}

private val glassmorphismBg: @Composable BoxScope.() -> Unit = {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFEEF2FF), Color(0xFFF8FAFC))
                )
            )
    )
}

private val glassmorphismDarkBg: @Composable BoxScope.() -> Unit = {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF0F172A), Color(0xFF030712))
                )
            )
    )
}

private val cyberneticStrideBg: @Composable BoxScope.() -> Unit = {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    listOf(Color(0xFF171B26), Color(0xFF0F131D))
                )
            )
    )
}

object ThemeRegistry {

    private val standardShapes = Shapes(
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(16.dp),
        large = RoundedCornerShape(20.dp),
        extraLarge = RoundedCornerShape(28.dp)
    )

    private val roundedPlayfulShapes = Shapes(
        extraSmall = RoundedCornerShape(8.dp),
        small = RoundedCornerShape(12.dp),
        medium = RoundedCornerShape(20.dp),
        large = RoundedCornerShape(26.dp),
        extraLarge = RoundedCornerShape(32.dp)
    )

    private val technicalSharpShapes = Shapes(
        extraSmall = RoundedCornerShape(2.dp),
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(12.dp),
        extraLarge = RoundedCornerShape(16.dp)
    )

    fun getThemeSpec(choice: ThemeChoice): AppThemeSpec {
        return when (choice) {
            ThemeChoice.CLASSIC_TEAL -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFF006A6A),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFBCEBEB),
                    onPrimaryContainer = Color(0xFF002020),
                    secondary = Color(0xFF4A6363),
                    surface = Color(0xFFF8FAFA),
                    background = Color(0xFFF4F7F7)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFF4DDADA),
                    onPrimary = Color(0xFF003737),
                    primaryContainer = Color(0xFF004F4F),
                    secondary = Color(0xFFB1CCCC),
                    surface = Color(0xFF191C1C),
                    background = Color(0xFF101414)
                ),
                shapes = standardShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFF006A6A), Color(0xFF028090))),
                background = classicTealBg,
                darkBackground = classicTealDarkBg
            )

            ThemeChoice.APPLE_HEALTH -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFFE63946),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFFFDAD6),
                    secondary = Color(0xFF457B9D),
                    surface = Color(0xFFFFFFFF),
                    background = Color(0xFFF8F9FA)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFFFFB4AB),
                    onPrimary = Color(0xFF690005),
                    primaryContainer = Color(0xFF93000A),
                    secondary = Color(0xFFA8DADC),
                    surface = Color(0xFF121212),
                    background = Color(0xFF000000)
                ),
                shapes = standardShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFFFF3366), Color(0xFFFF6B6B)))
            )

            ThemeChoice.BOLD_VIBRANT -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFF0052CC),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFDCE2FF),
                    secondary = Color(0xFFFF5722),
                    surface = Color(0xFFFFFFFF),
                    background = Color(0xFFF4F6F9)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFFB6C4FF),
                    onPrimary = Color(0xFF00277F),
                    primaryContainer = Color(0xFF003B99),
                    secondary = Color(0xFFFF8A65),
                    surface = Color(0xFF181C24),
                    background = Color(0xFF0E121A)
                ),
                shapes = standardShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFF0052CC), Color(0xFF6366F1)))
            )

            ThemeChoice.DATA_DASHBOARD -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFF0F766E),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFCCFBF1),
                    secondary = Color(0xFF0284C7),
                    surface = Color(0xFFF8FAFC),
                    background = Color(0xFFF1F5F9)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFF2DD4BF),
                    onPrimary = Color(0xFF134E4A),
                    primaryContainer = Color(0xFF042F2E),
                    secondary = Color(0xFF38BDF8),
                    surface = Color(0xFF0F172A),
                    background = Color(0xFF020617)
                ),
                shapes = technicalSharpShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFF0F766E), Color(0xFF0EA5E9)))
            )

            ThemeChoice.NEON_FUTURISTIC -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFF7C3AED),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFEDE9FE),
                    secondary = Color(0xFF06B6D4),
                    surface = Color(0xFFFAF5FF),
                    background = Color(0xFFF5F3FF)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFFA78BFA),
                    onPrimary = Color(0xFF3B0764),
                    primaryContainer = Color(0xFF581C87),
                    secondary = Color(0xFF22D3EE),
                    surface = Color(0xFF140D26),
                    background = Color(0xFF0A0517)
                ),
                shapes = roundedPlayfulShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFFA855F7), Color(0xFFEC4899))),
                background = neonFuturisticBg
            )

            ThemeChoice.GLASSMORPHISM -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFF4F46E5),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFE0E7FF),
                    secondary = Color(0xFF0284C7),
                    surface = Color(0xFFF8FAFC),
                    background = Color(0xFFF1F5F9)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFF818CF8),
                    onPrimary = Color(0xFF1E1B4B),
                    primaryContainer = Color(0xFF312E81),
                    secondary = Color(0xFF38BDF8),
                    surface = Color(0xFF0F172A),
                    background = Color(0xFF030712)
                ),
                shapes = standardShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFF6366F1), Color(0xFF38BDF8))),
                background = glassmorphismBg,
                darkBackground = glassmorphismDarkBg
            )

            ThemeChoice.FROSTED_GLASS -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFF0284C7),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFE0F2FE),
                    secondary = Color(0xFF0D9488),
                    surface = Color(0xFFF0F9FF),
                    background = Color(0xFFE0F2FE)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFF38BDF8),
                    onPrimary = Color(0xFF082F49),
                    primaryContainer = Color(0xFF075985),
                    secondary = Color(0xFF2DD4BF),
                    surface = Color(0xFF0B192C),
                    background = Color(0xFF040D1A)
                ),
                shapes = standardShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFF0284C7), Color(0xFF06B6D4)))
            )

            ThemeChoice.SOFT_PASTEL -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFF8B5CF6),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFF3E8FF),
                    secondary = Color(0xFFF43F5E),
                    surface = Color(0xFFFDFBFE),
                    background = Color(0xFFFAF5FF)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFFC4B5FD),
                    onPrimary = Color(0xFF4C1D95),
                    primaryContainer = Color(0xFF6D28D9),
                    secondary = Color(0xFFFB7185),
                    surface = Color(0xFF1A1429),
                    background = Color(0xFF100B1C)
                ),
                shapes = roundedPlayfulShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFFC084FC), Color(0xFFF472B6)))
            )

            ThemeChoice.BOLD_GRADIENT -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFFEA580C),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFFFEDD5),
                    secondary = Color(0xFF7C3AED),
                    surface = Color(0xFFFFF7ED),
                    background = Color(0xFFFFF1EB)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFFFB923C),
                    onPrimary = Color(0xFF7C2D12),
                    primaryContainer = Color(0xFF9A3412),
                    secondary = Color(0xFFA78BFA),
                    surface = Color(0xFF261812),
                    background = Color(0xFF180D08)
                ),
                shapes = standardShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFFEA580C), Color(0xFFD946EF)))
            )

            ThemeChoice.GAMIFIED_GOLD -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFFD97706),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFFEF3C7),
                    secondary = Color(0xFF1E293B),
                    surface = Color(0xFFFFFDF5),
                    background = Color(0xFFFEF9C3)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFFFBBF24),
                    onPrimary = Color(0xFF78350F),
                    primaryContainer = Color(0xFF92400E),
                    secondary = Color(0xFFCBD5E1),
                    surface = Color(0xFF201808),
                    background = Color(0xFF120D04)
                ),
                shapes = roundedPlayfulShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFFF59E0B), Color(0xFFEAB308)))
            )

            ThemeChoice.NATURE_ORGANIC -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFFC2410C),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFFFEDD5),
                    secondary = Color(0xFF57534E),
                    surface = Color(0xFFFAF8F5),
                    background = Color(0xFFF5F2EB)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFFFB923C),
                    onPrimary = Color(0xFF431407),
                    primaryContainer = Color(0xFF7C2D12),
                    secondary = Color(0xFFA8A29E),
                    surface = Color(0xFF1C1917),
                    background = Color(0xFF0C0A09)
                ),
                shapes = standardShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFFC2410C), Color(0xFFEA580C)))
            )

            ThemeChoice.PLAYFUL_GAMIFIED -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFFBE185D),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFFCE7F3),
                    secondary = Color(0xFF0284C7),
                    surface = Color(0xFFFFF1F2),
                    background = Color(0xFFFFF0F5)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFFF472B6),
                    onPrimary = Color(0xFF500724),
                    primaryContainer = Color(0xFF831843),
                    secondary = Color(0xFF38BDF8),
                    surface = Color(0xFF240D18),
                    background = Color(0xFF14050D)
                ),
                shapes = roundedPlayfulShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFFBE185D), Color(0xFF8B5CF6)))
            )

            ThemeChoice.GRADIENT_MESH -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFF6366F1),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFE0E7FF),
                    secondary = Color(0xFFEC4899),
                    surface = Color(0xFFFAF5FF),
                    background = Color(0xFFF5F3FF)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFF818CF8),
                    onPrimary = Color(0xFF1E1B4B),
                    primaryContainer = Color(0xFF3730A3),
                    secondary = Color(0xFFF472B6),
                    surface = Color(0xFF13112E),
                    background = Color(0xFF09071E)
                ),
                shapes = standardShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFF6366F1), Color(0xFFEC4899)))
            )

            ThemeChoice.DUAL_TONE -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFF1E293B),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFF1F5F9),
                    secondary = Color(0xFFFB923C),
                    surface = Color(0xFFFFFFFF),
                    background = Color(0xFFF8FAFC)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFFE2E8F0),
                    onPrimary = Color(0xFF0F172A),
                    primaryContainer = Color(0xFF334155),
                    secondary = Color(0xFFF97316),
                    surface = Color(0xFF1E293B),
                    background = Color(0xFF0F172A)
                ),
                shapes = standardShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFF1E293B), Color(0xFFFB923C)))
            )

            ThemeChoice.CARD_DASHBOARD -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFF2563EB),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFDBEAFE),
                    secondary = Color(0xFF475569),
                    surface = Color(0xFFFFFFFF),
                    background = Color(0xFFF1F5F9)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFF60A5FA),
                    onPrimary = Color(0xFF1E3A8A),
                    primaryContainer = Color(0xFF1D4ED8),
                    secondary = Color(0xFF94A3B8),
                    surface = Color(0xFF1E293B),
                    background = Color(0xFF0F172A)
                ),
                shapes = standardShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFF2563EB), Color(0xFF38BDF8)))
            )

            ThemeChoice.M3_DARK -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = lightColorScheme(
                    primary = Color(0xFF6366F1),
                    onPrimary = Color.White,
                    surface = Color(0xFFF8FAFC),
                    background = Color(0xFFF1F5F9)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFFA5B4FC),
                    onPrimary = Color(0xFF1E1B4B),
                    primaryContainer = Color(0xFF312E81),
                    secondary = Color(0xFFC7D2FE),
                    surface = Color(0xFF121212),
                    background = Color(0xFF000000)
                ),
                shapes = standardShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFF818CF8), Color(0xFFA5B4FC)))
            )

            ThemeChoice.CYBERNETIC_STRIDE -> AppThemeSpec(
                id = choice,
                displayName = choice.displayName,
                lightColors = darkColorScheme(
                    primary = Color(0xFF00F0FF),
                    onPrimary = Color(0xFF00363A),
                    primaryContainer = Color(0xFF004D54),
                    onPrimaryContainer = Color(0xFFDBFCFF),
                    secondary = Color(0xFF4DFFB2),
                    onSecondary = Color(0xFF003822),
                    secondaryContainer = Color(0xFF005F3D),
                    onSecondaryContainer = Color(0xFFB4FFD8),
                    tertiary = Color(0xFF7B61FF),
                    onTertiary = Color(0xFF1E0066),
                    surface = Color(0xFF171B26),
                    onSurface = Color(0xFFDFE2F1),
                    surfaceVariant = Color(0xFF262A35),
                    onSurfaceVariant = Color(0xFFB9CACB),
                    background = Color(0xFF0F131D),
                    onBackground = Color(0xFFDFE2F1)
                ),
                darkColors = darkColorScheme(
                    primary = Color(0xFF00F0FF),
                    onPrimary = Color(0xFF00363A),
                    primaryContainer = Color(0xFF004D54),
                    onPrimaryContainer = Color(0xFFDBFCFF),
                    secondary = Color(0xFF4DFFB2),
                    onSecondary = Color(0xFF003822),
                    secondaryContainer = Color(0xFF005F3D),
                    onSecondaryContainer = Color(0xFFB4FFD8),
                    tertiary = Color(0xFF7B61FF),
                    onTertiary = Color(0xFF1E0066),
                    surface = Color(0xFF171B26),
                    onSurface = Color(0xFFDFE2F1),
                    surfaceVariant = Color(0xFF262A35),
                    onSurfaceVariant = Color(0xFFB9CACB),
                    background = Color(0xFF0F131D),
                    onBackground = Color(0xFFDFE2F1)
                ),
                shapes = technicalSharpShapes,
                homeProgressGradient = Brush.horizontalGradient(listOf(Color(0xFF00F0FF), Color(0xFF4DFFB2))),
                background = cyberneticStrideBg,
                darkBackground = cyberneticStrideBg
            )
        }
    }
}
