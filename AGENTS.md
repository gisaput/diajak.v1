# Project-Specific Design Guidelines

## Flat Card Styling Consistency Rule
To maintain visual consistency across all screens in the **Diajak** application, all standard containers, cards, chips, and option boxes must strictly use a **flat design pattern** (like Shopee). Since the app's background is already a soft gray (`Color(0xFFE5E7EB)` or similar), white containers will naturally stand out without needing borders or shadows.

**Do NOT** use shadows (`elevation`) and **Do NOT** use gray borders (`BorderStroke`) for standard containers.

### Standard Flat Container Design Specification
Every card/box/surface container should be implemented as a simple flat surface (e.g., using `Surface`, `Card`, or `OutlinedCard` with zero elevation/border) using the following layout attributes:

```kotlin
Card( // or Surface
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // MUST BE 0
    // NO BORDER
) {
    // Card inner content with padding as appropriate
}
```

### Key Elements to Preserve:
1. **Corner Radius**: Must always be `16.dp` (`RoundedCornerShape(16.dp)`).
2. **Background Color**: Must be pure white (`Color.White`) inside light theme components.
3. **Border/Outline**: **NONE**. Do not use `BorderStroke` unless the card is explicitly selected/active.
4. **Selected/Active State border**: Only when a card is selected (e.g., selected payment method or active option), the border color can change to `DiajakOrange` with a thickness of `1.dp` or `2.dp`.
5. **Elevation/Shadow**: **NONE**. Elevation must be `0.dp`. Do not use `shadowElevation`.

---

## Spacing Guidelines (ThemeSpacing & Vertical-Horizontal Harmony)
To maintain consistent padding and margins across all screens, we use a centralized `ThemeSpacing` object (defined in `Spacing.kt`). Do NOT use hardcoded `dp` values for spacing between layout components. Always use the semantic values from `ThemeSpacing` / `MaterialTheme.spacing`.

### Uniform Vertical-Horizontal Rhythm (Isotropic Harmony Rule):
- **Vertical Spacing = Horizontal Screen Margin (16.dp)**: The vertical spacing between major cards, forms, and distinct sections is strictly unified to **`16.dp`** (`ThemeSpacing.Medium` / `MaterialTheme.spacing.medium`), matching the left and right screen margin (16.dp). This eliminates wild vertical gaps and creates a balanced, harmonious 1:1 visual grid.
- **Maximum Spacing Ceiling**: No general content spacer may exceed **`16.dp`** (except for the mandatory `80.dp` glass header offset spacer and bottom scroll clearance).
- **Internal / Closely Related Elements (8.dp)**: Small related elements (e.g. form label to input field, title to subtitle) use **`8.dp`** (`ThemeSpacing.Small` / `MaterialTheme.spacing.small`).
- **Micro Spacing (4.dp)**: Badges, tags, and tight metadata use **`4.dp`** (`ThemeSpacing.ExtraSmall` / `MaterialTheme.spacing.extraSmall`).

### Standardized Spacing Scale:
1. **`ThemeSpacing.ExtraSmall` (4.dp)**: Micro-spacing between tightly coupled items.
2. **`ThemeSpacing.Small` (8.dp)**: Distance between small, closely related elements.
3. **`ThemeSpacing.Medium` (16.dp)**: Unified standard vertical distance between sections/cards, and horizontal screen margins.
4. **`ThemeSpacing.Large` (16.dp)**: Aligned with Medium for uniform visual rhythm.

---

## UNIVERSAL iOS-Style Pure Optical Glass Header
To maintain visual consistency and a premium feel matching iOS, **ALL screens (scrollable or non-scrollable)** MUST use the iOS-Style Glass Header implementation (via `DiajakGlassHeader`). This acts as a standard visual anchor. Complete architectural details and PRD specifications are documented in `DESIGN_SYSTEM_HEADER.md`.

**MANDATORY RULE FOR NEW & EXISTING SCREENS:**
Whenever creating a new screen or modifying an existing screen in the **Diajak** app, you **MUST** implement this exact header glass pattern. No plain `TopAppBar`, no solid opaque bars, and no un-blurred headers are allowed.

**Crucial Constraints (LOCKED SPECIFICATIONS):**
- **Header Height**: The overall height of the content layer MUST be exactly `56.dp` (excluding status bar).
- **Title Alignment & Typography (STRICT)**: 
  - Titles MUST use `DiajakDesignSystem.Typography.Headline`.
  - Titles MUST be wrapped in a `Box(modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 20.dp))` and use `modifier = Modifier.align(Alignment.Center)` with `textAlign = TextAlign.Center`.
- **Content Spacer (The Gap)**: The `Spacer` pushing the main content below the header must add a standard gap (`24.dp`) so content doesn't touch the header. Use `Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))` (56.dp header + 24.dp gap). This ensures the gap is completely consistent globally.
- **Glass Base Color**: Match the gray screen background `Color(0xFFE5E7EB)`.
- **Blur Intensity (Header)**: `blurRadius = 24.dp`.
- **Pure Optical Progressive Fade (CRITICAL: ZERO FOG OVER PHOTOS)**: 
  - The glass effect uses `graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }` and `drawWithContent` with `Brush.verticalGradient` using `BlendMode.DstIn` to provide full optical blur across the header with a smooth bottom feather.
  - **Dynamic 3 Transitions (`HeaderUnderlayState`)**:
    1. **Text Transition (`HeaderUnderlayState.TEXT`)**: Intelligently dissolves black letters into `#E5E7EB` gray canvas using dynamic text diffusion.
    2. **Photo Transition (`HeaderUnderlayState.PHOTO`)**: ZERO gray fog (`textMeltingFactor = 0f`), pure vibrant optical blur with 3-tier gentle curve. Photos remain 100% vibrant and crystal clear.
    3. **Container Transition (`HeaderUnderlayState.CONTAINER`)**: Soft edge melting for white cards and search bars.
  - **Dynamic Layout Agnostic Detection**: In scrollable lists (`LazyColumn`), items define their semantic type via `item(contentType = HeaderUnderlayState...)`. The header dynamically reads `(headerItem.contentType as? HeaderUnderlayState)` so banners, cards, and sections can be freely moved, added, or reordered without modifying the header logic.

### Core Component Implementation (`DiajakGlassHeader`):
```kotlin
@Composable
fun DiajakGlassHeader(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    containerColor: Color = GlassmorphismTheme.Header.BaseColor,
    style: HazeStyle? = null,
    textMeltingFactor: Float = 0f,
    photoGlowFactor: Float = 1f,
    content: @Composable BoxScope.() -> Unit
) {
    val effectiveStyle = style ?: remember(containerColor) {
        GlassmorphismTheme.Header.adaptiveStyle(containerColor = containerColor)
    }

    Box(modifier = modifier.fillMaxWidth()) {
        // Layer 1: Dynamic Haze Optical Glass Effect with 3 Adaptive Transitions
        Box(
            modifier = Modifier
                .matchParentSize()
                .diajakGlassHeaderEffect(
                    hazeState = hazeState,
                    style = effectiveStyle,
                    textMeltingFactor = textMeltingFactor,
                    photoGlowFactor = photoGlowFactor,
                    containerColor = containerColor
                )
        )
        // Layer 2: Content Layer
        content()
    }
}
```

### Standard Header Implementation (Template):
```kotlin
val scrollState = rememberScrollState()
val hazeState = remember { HazeState() }

Box(
    modifier = Modifier.fillMaxSize().background(Color(0xFFE5E7EB))
) {
    // 1. Content Layer (Always hazeSource)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(state = hazeState)
            .verticalScroll(scrollState)
    ) {
        // ALWAYS use 80.dp spacer for the content (56.dp header + 24.dp gap)
        Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))
        // Scrollable content goes here...
    }

    // 2. Glass Header Layer
    DiajakGlassHeader(
        hazeState = hazeState,
        modifier = Modifier.align(Alignment.TopCenter).zIndex(10f)
    ) {
        // Content Layer (Buttons and Title) MUST be 56.dp height
        Box(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().height(56.dp).padding(horizontal = 20.dp)
        ) {
            // Circular Back Button (Left)
            IconButton(
                onClick = { /* action */ },
                modifier = Modifier.size(40.dp).align(Alignment.CenterStart).clip(CircleShape)...
            ) { /* Icon */ }

            // Title (STRICTLY CENTERED WITH HEADLINE TYPOGRAPHY)
            Text(
                text = "Judul Halaman",
                style = DiajakDesignSystem.Typography.Headline,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center).fillMaxWidth()
            )
        }
    }
}
```

---

## Glossy Frosted Glass Circular Buttons
Any circular icon button overlaid on content (like Back, Search, Share, or Favorite buttons in a header) MUST use the following precise glass styling to ensure a 3D, layered glossy frosted effect over text/images (like GetYourGuide and iOS):
- **Shape**: `CircleShape`
- **Size**: `40.dp`
- **Elevation / Shadow**: `2.dp` (Soft ambient shadow)
- **Base Background**: `Color.White.copy(alpha = 0.90f)`
- **Border**: `0.75.dp, Color.White.copy(alpha = 0.85f)`
- **Glass Background**: `Color.White`
- **Haze Tint (Overlay)**: `Color.White.copy(alpha = 0.80f)`
- **Blur Intensity (Button)**: `blurRadius = 16.dp`

### Implementation:
Prefer using the standardized extension modifier:
```kotlin
IconButton(
    onClick = { /* action */ },
    modifier = Modifier
        .size(40.dp)
        .diajakGlassButton(hazeState)
) {
    Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Kembali", tint = MaterialTheme.colorScheme.onSurface)
}
```

---

## WhatsApp/Instagram-Style Dynamic Header Title
When dealing with a detail screen that already displays a large title in the content, the Header Title MUST NOT be static. It must remain hidden and only fade-in elegantly when the user scrolls past the main content title (like WhatsApp profiles).

### Dynamic Fade-In Rule:
Use a `derivedStateOf` driven by the `scrollState` (triggering around `240.dp`) and `AnimatedVisibility` to wrap the header title.

```kotlin
val density = LocalDensity.current
val showTitle by remember { 
    derivedStateOf { scrollState.value > with(density) { 240.dp.toPx() } } 
}

androidx.compose.animation.AnimatedVisibility(
    visible = showTitle,
    enter = androidx.compose.animation.fadeIn(),
    exit = androidx.compose.animation.fadeOut(),
    modifier = Modifier.align(Alignment.Center)
) {
    Text(
        text = activity.title,
        style = DiajakDesignSystem.Typography.Headline, // ALWAYS HEADLINE
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(horizontal = 48.dp).fillMaxWidth() // Leave room for Circular Buttons
    )
}
```
