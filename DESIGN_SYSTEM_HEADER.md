# Product Requirements Document (PRD) & Design System Standard: Diajak iOS Optical Glass Header

**Document Version:** 1.0.0  
**Status:** LOCKED SPECIFICATION / MANDATORY RULE  
**Platform:** Android (Jetpack Compose with Devs-Haze)  
**App Reference:** Diajak App (`com.example`)  

---

## 1. Executive Summary & Philosophy

Aplikasi **Diajak** menggunakan desain bertema **Pure Optical Glassmorphism** (terinspirasi dari iOS & Apple Human Interface Guidelines). 
Tujuannya adalah menghadirkan visual header yang mewah, natural, dan konsisten di **seluruh layar** (Beranda, Detail Aktivitas, Checkout/Payment, Kategori, Tiket, Profil, dll).

### Prinsip Utama:
1. **Zero Artificial Fog over Visuals (PHOTO_MODE)**:
   Saat foto banner, gambar aktivitas, atau footer card melintas di bawah header, kaca bersikap 100% optikal murni (`HazeTint = 0.0f`). Tidak boleh ada lapisan abu-abu kusam, kapur susu, atau cat putih pekat. Foto tetap hidup dan berkilau di balik blur.
2. **Text Absorption Diffusion (CONTENT_MODE)**:
   Saat teks hitam tebal di atas background abu-abu (`#E5E7EB`) melintas di bawah header (seperti judul seksi "Seru di Sekitarmu"), sistem otomatis mengaktifkan peredam optik (`textMeltingFactor = 0.78f`) agar huruf-huruf hitam tidak tembus tajam atau mengganggu keterbacaan status bar.
3. **Unified CompositionLocal**:
   Semua status dan parameter ditransmisikan secara otomatis melalui `LocalHazeConfig` (`HazeConfig` & `HazeMode`). Setiap layar atau sub-komponen dapat membaca atau mewarisi status ini tanpa perlu passing parameter berulang.

---

## 2. Technical Specification & Standards

| Parameter | Spesifikasi Wajib | Keterangan |
|---|---|---|
| **Content Header Height** | `56.dp` | Tinggi area tombol & judul (belum termasuk status bar) |
| **Status Bar Padding** | `Modifier.statusBarsPadding()` | Diterapkan pada wrapper container header |
| **Top Content Gap (Spacer)** | `80.dp` (`56.dp` + `24.dp`) | Jarak konten awal di bawah header agar tidak menabrak |
| **Glass Base Color** | `Color(0xFFE5E7EB)` | Sesuai warna kanvas layar Diajak |
| **Blur Radius** | `24.dp` | Radius blur optik Haze |
| **Progressive Fade Feather** | `Brush.verticalGradient` + `DstIn` | Menggunakan `CompositingStrategy.Offscreen` untuk transisi lembut |
| **Circular Glass Buttons** | `40.dp`, `CircleShape`, blur `16.dp` | Menggunakan modifier `.diajakGlassButton(hazeState)` |
| **Title Typography** | `DiajakDesignSystem.Typography.Headline` | Center aligned, teks `onSurface` |

---

## 3. Architecture & CompositionLocal: `LocalHazeConfig`

Header bekerja sama dengan `GlassmorphismTheme.kt` yang menyediakan:
```kotlin
enum class HazeMode {
    PHOTO_MODE,    // 0.0f tint -> Untuk Foto, Banner, Card Footer, Search Bar
    CONTENT_MODE   // 0.78f tint -> Untuk Teks di atas Gray Canvas
}

val LocalHazeConfig: ProvidableCompositionLocal<HazeConfig>
```

Transisi antar mode terjadi secara halus dengan animasi durasi `280ms` (`FastOutSlowInEasing`) sehingga pergantian dari teks ke foto tampak seamless dan natural.

---

## 4. Universal Screen Template (Copy-Paste Ready)

Setiap layar baru di aplikasi Diajak **WAJIB** mengikuti struktur baku berikut:

### Opsi A: Layar dengan Scrollable Content (List / Column)

```kotlin
@Composable
fun ContohLayarScrollableScreen(
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val hazeState = remember { HazeState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5E7EB))
    ) {
        // 1. CONTENT LAYER (MANDATORY: .hazeSource)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState)
                .verticalScroll(scrollState)
        ) {
            // Spacer wajib 80.dp (56.dp header + 24.dp gap)
            Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))

            // Isi konten layar di sini...
        }

        // 2. UNIVERSAL GLASS HEADER LAYER
        DiajakGlassHeader(
            hazeState = hazeState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(10f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(56.dp)
                    .padding(horizontal = 20.dp)
            ) {
                // Tombol Back Kaca Bulat (Kiri)
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterStart)
                        .diajakGlassButton(hazeState)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Kembali",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Judul Halaman (Tengah - STRICT HEADLINE)
                Text(
                    text = "Judul Halaman",
                    style = DiajakDesignSystem.Typography.Headline,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                )
            }
        }
    }
}
```

---

### Opsi B: Layar dengan LazyColumn (Deteksi Dynamic Agnostic)

Untuk layar dengan banyak elemen dinamis (seperti Beranda):
Gunakan `contentType` pada setiap item list agar header otomatis tahu kapan harus beralih ke `PHOTO_MODE` atau `CONTENT_MODE`:

```kotlin
val listState = rememberLazyListState()
val hazeState = remember { HazeState() }

// Dynamic Underlay State Detector
val currentUnderlayState by remember {
    derivedStateOf {
        val firstVisibleItem = listState.layoutInfo.visibleItemsInfo.firstOrNull()
        (firstVisibleItem?.contentType as? HeaderUnderlayState) ?: HeaderUnderlayState.CONTAINER
    }
}

LazyColumn(
    state = listState,
    modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
) {
    // 1. Spacing awal
    item(contentType = HeaderUnderlayState.CONTAINER) {
        Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))
    }

    // 2. Banner/Foto -> Otomatis PHOTO_MODE (Pure Optical Blur)
    item(contentType = HeaderUnderlayState.PHOTO) {
        HeroBannerSection(...)
    }

    // 3. Judul Section -> Otomatis CONTENT_MODE (Text Absorption)
    item(contentType = HeaderUnderlayState.TEXT) {
        SectionTitleText("Seru di Sekitarmu")
    }

    // 4. Kartu/Footer -> PHOTO_MODE
    items(activities, contentType = { HeaderUnderlayState.PHOTO }) { activity ->
        ActivityCard(activity)
    }
}

// Pasang Header
DiajakGlassHeader(
    hazeState = hazeState,
    underlayState = currentUnderlayState,
    modifier = Modifier.align(Alignment.TopCenter).zIndex(10f)
) {
    // Header Content
}
```

---

## 5. Checklist Verifikasi Layar Baru

Sebelum menandai pembuatan layar baru selesai, pastikan:
- [ ] Layar dibungkus `Box` dengan background `#E5E7EB`.
- [ ] Konten scrollable dipasangi `.hazeSource(state = hazeState)`.
- [ ] Spacer konten paling atas menggunakan `Modifier.statusBarsPadding().height(80.dp)`.
- [ ] Header menggunakan `DiajakGlassHeader` dengan `height(56.dp)`.
- [ ] Tombol icon menggunakan `.diajakGlassButton(hazeState)`.
- [ ] Judul layar menggunakan `DiajakDesignSystem.Typography.Headline`.
- [ ] Tidak ada garis border keras abu-abu atau shadow artifisial di kartu atau header.
