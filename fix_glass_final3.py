import re

with open('app/src/main/java/com/example/ui/components/DiajakGlassHeader.kt', 'r') as f:
    content = f.read()

new_haze = """        // Layer 1: Haze Glass Effect with Extra Smooth Progressive Fade
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            0.0f to Color.Black,
                            0.3f to Color.Black.copy(alpha = 0.9f),
                            0.5f to Color.Black.copy(alpha = 0.7f),
                            0.7f to Color.Black.copy(alpha = 0.4f),
                            0.85f to Color.Black.copy(alpha = 0.15f),
                            1.0f to Color.Transparent
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
                .hazeEffect(
                    state = hazeState,
                    style = HazeStyle(
                        backgroundColor = Color.Transparent,
                        tint = HazeTint(DiajakGlassConfig.BaseColor.copy(alpha = 0.65f)),
                        blurRadius = 24.dp,
                        noiseFactor = 0f
                    )
                )
        )"""

content = re.sub(r'        // Layer 1: Haze Glass Effect with Smooth Progressive Fade.*?\)\n        \)', new_haze, content, flags=re.DOTALL)

with open('app/src/main/java/com/example/ui/components/DiajakGlassHeader.kt', 'w') as f:
    f.write(content)
