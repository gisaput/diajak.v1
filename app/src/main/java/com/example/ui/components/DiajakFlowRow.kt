package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Bulletproof, crash-free FlowRow implementation built with standard Compose Layout.
 * Avoids NoSuchMethodError caused by binary incompatibility across Compose Foundation versions.
 */
@Composable
fun DiajakFlowRow(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 8.dp,
    verticalSpacing: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val hSpacingPx = horizontalSpacing.roundToPx()
        val vSpacingPx = verticalSpacing.roundToPx()

        val rows = mutableListOf<List<Placeable>>()
        val rowHeights = mutableListOf<Int>()
        var currentRow = mutableListOf<Placeable>()
        var currentRowWidth = 0

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

        for (placeable in placeables) {
            val neededWidth = if (currentRow.isEmpty()) placeable.width else currentRowWidth + hSpacingPx + placeable.width
            if (currentRow.isNotEmpty() && neededWidth > constraints.maxWidth) {
                rows.add(currentRow)
                rowHeights.add(currentRow.maxOfOrNull { it.height } ?: 0)
                currentRow = mutableListOf(placeable)
                currentRowWidth = placeable.width
            } else {
                currentRow.add(placeable)
                currentRowWidth = neededWidth
            }
        }
        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
            rowHeights.add(currentRow.maxOfOrNull { it.height } ?: 0)
        }

        val totalHeight = if (rows.isEmpty()) {
            constraints.minHeight
        } else {
            (rowHeights.sum() + (rows.size - 1).coerceAtLeast(0) * vSpacingPx)
                .coerceIn(constraints.minHeight, constraints.maxHeight)
        }

        val totalWidth = if (constraints.maxWidth != Constraints.Infinity) {
            constraints.maxWidth
        } else {
            rows.maxOfOrNull { r -> r.sumOf { it.width } + (r.size - 1).coerceAtLeast(0) * hSpacingPx }
                ?.coerceIn(constraints.minWidth, constraints.maxWidth) ?: constraints.minWidth
        }

        layout(totalWidth, totalHeight) {
            var y = 0
            for (i in rows.indices) {
                val row = rows[i]
                val rowHeight = rowHeights[i]
                var x = 0
                for (placeable in row) {
                    val yOffset = y + (rowHeight - placeable.height) / 2
                    placeable.placeRelative(x, yOffset)
                    x += placeable.width + hSpacingPx
                }
                y += rowHeight + vSpacingPx
            }
        }
    }
}

@Composable
fun DiajakFlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    DiajakFlowRow(
        modifier = modifier,
        horizontalSpacing = horizontalArrangement.spacing,
        verticalSpacing = verticalArrangement.spacing,
        content = content
    )
}
