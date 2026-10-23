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
1. **Universal Progressive Dissolver**:
   Header menggunakan satu formula optikal universal (`universalDissolverBrush` + `ProgressiveGradientBrush` dengan `DstIn` alpha mask) yang melandai secara natural dari warna canvas (`#E5E7EB`) di bagian status bar menuju transparan murni di bibir bawah header.
2. **Zero Over-Engineering & Zero State Detection**:
   Tidak lagi memerlukan deteksi manual atau penandaan `contentType = HeaderUnderlayState` pada list items. Semua tipe konten (foto, teks hitam di atas canvas abu-abu, maupun kontainer putih) otomatis melebur dengan transisi optikal yang mulus tanpa kedip/glitch.
3. **Crystal Clear Visuals & Clean Status Bar**:
   - Foto dan banner di bagian bawah header tetap 100% transparan dan jernih berkilau.
   - Teks hitam dan elemen konten melebur secara gradual ke dalam latar canvas `#E5E7EB` saat mendekati status bar.
   - Jam dan indikator baterai pada status bar tetap kontras dan mudah dibaca.

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

## 3. Architecture & Universal Engine

Header bekerja sama dengan `GlassmorphismTheme.kt` yang menyediakan engine terpadu:
- **`universalDissolverBrush`**: Melarutkan konten ke warna kanvas latar belakang (`#E5E7EB`) secara melandai di area status bar.
- **`ProgressiveGradientBrush`** (`BlendMode.DstIn`): Menghasilkan *optical bottom feather* tanpa garis potong tajam.
- **`OpticalStyle`**: Blur murni `24.dp` tanpa residu cat/kabut buatan.

Tidak perlu *state switching*, tidak perlu *derivedStateOf*, dan tidak perlu *recomposition overhead* saat scrolling!

---

## 4. Universal Screen Template (Copy-Paste Ready)

Setiap layar baru di aplikasi Diajak **WAJIB** mengikuti struktur baku berikut:

### Opsi A: Layar dengan Scrollable Content (List / Column / LazyColumn)

```kotlin
@Composable
fun ContohLayarScreen(
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

### Opsi B: Layar dengan LazyColumn / LazyVerticalGrid

Cukup pasang `.hazeSource(state = hazeState)` pada `LazyColumn` atau `LazyVerticalGrid`. Header `DiajakGlassHeader` langsung menangani peleburan secara universal tanpa penandaan apa pun:

```kotlin
val listState = rememberLazyListState()
val hazeState = remember { HazeState() }

Box(
    modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFE5E7EB))
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(state = hazeState)
    ) {
        item {
            Spacer(modifier = Modifier.statusBarsPadding().height(80.dp))
        }

        item {
            HeroBannerSection(...)
        }

        item {
            SectionTitleText("Seru di Sekitarmu")
        }

        items(activities) { activity ->
            ActivityCard(activity)
        }
    }

    // Pasang Header Universal - tanpa passing underlayState!
    DiajakGlassHeader(
        hazeState = hazeState,
        modifier = Modifier.align(Alignment.TopCenter).zIndex(10f)
    ) {
        // Header Content
    }
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
