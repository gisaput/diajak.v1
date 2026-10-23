# 100% Pure Optical Glass Header (Bebas Kabut ala Instagram)

Penyempurnaan arsitektur rendering header aplikasi Diajak untuk beralih sepenuhnya ke **100% Pure Optical Glass** identik dengan Instagram (**IMG_0477**), menghilangkan lapisan cat kanvas abu-abu yang menyebabkan foto terlihat berkabut (**IMG_0476**).

---

### User Review & Critical Decisions

> [!IMPORTANT]
> - **Keputusan Terkonfirmasi**: **Hapus Lapisan Cat Kanvas 100% (Zero Paint Overlay)**.
>   Mengeliminasi `drawRect` warna kanvas abu-abu (`#E5E7EB`) dari pipa render `diajakGlassHeaderEffect`. Menggantinya dengan pembiasan optik murni (*live optical backdrop blur*) berkekuatan 32.dp, sehingga foto dan banner di bawah header tetap 100% tajam, jernih, dan warnanya menyala cerah tanpa selaput kabut/milky film.

---

### 1. Overview & Core Concept

- **Masalah yang Diperbaiki**:
  Pada iterasi sebelumnya, `drawRect(universalDissolverBrush)` menyemprotkan pigmen warna abu-abu `#E5E7EB` dengan opasitas hingga 94%. Ketika foto (seperti foto barista kopi) digulirkan ke bawah header, pigmen abu-abu ini menutupi foto sehingga menghasilkan efek kabut putih/kusam yang kontras dengan Instagram.
- **Solusi Arsitektur**:
  - Instagram **TIDAK PERNAH** menyemprotkan pigmen warna di atas foto. Instagram murni mengandalkan **pembiasan optik GPU (live backdrop blur)** dengan radius blur yang cukup tinggi (`32.dp`).
  - Huruf dan teks hitam secara otomatis terpecah dan terbiaskan menjadi halus tanpa perlu cat tambahan.
  - Gradasi bawah menggunakan *progressive alpha mask* (`BlendMode.DstIn`) untuk transisi melandai yang sangat lembut.

---

### 2. User Experience & Visual Design

- **Tampilan Foto di Bawah Header**:
  - Warna foto (seperti kopi, dedaunan, langit, atau karakter) tetap 100% hidup, saturated, dan bercahaya, persis seperti feed Instagram pada **IMG_0477**.
  - Bebas dari kabut putih, kusam, atau selaput susu.
- **Peleburan Teks & Detail**:
  - Dengan radius blur `32.dp`, teks berukuran kecil akan terbiaskan secara optik sehingga tidak terbaca, namun latar belakang foto tetap memancarkan rona warna aslinya.
- **Kelandaian Halus (*Feathered Lip*)**:
  - Gradien `DstIn` memastikan bibir bawah header memudar melandai tanpa garis batas terpotong.

---

### 3. Key Product Decisions & Trade-Offs

- **Keputusan 1: Zero Paint Overlay vs Artificial Dissolver**
  - *Pendekatan*: Menghapus `drawRect(universalDissolverBrush)` sepenuhnya dari komposisi kanvas header.
  - *Alasan*: Menghilangkan akar penyebab kabut pada foto. Kaca murni merefraksikan piksel asli di baliknya.
- **Keputusan 2: Peningkatan Radius Blur Optik ke 32.dp**
  - *Pendekatan*: Menaikkan `blurRadius` dari `24.dp` ke `32.dp` pada `HazeStyle`.
  - *Alasan*: Memberikan peleburan tipografi yang cukup kuat di status bar secara murni optik, tanpa membutuhkan semprotan cat.

---

### 4. Technical Architecture & Optical Pipeline

```
┌──────────────────────────────────────────────────────────────┐
│             INSTAGRAM PURE OPTICAL GLASS PIPELINE            │
│                                                              │
│  Layer 1: Live Backdrop Haze Effect (32.dp Optical Blur)     │
│  ┌────────────────────────────────────────────────────────┐  │
│  │ Membiaskan warna asli foto secara murni (Zero Paint)   │  │
│  │ Warna merah, biru, dan rona foto tetap 100% cerah      │  │
│  └────────────────────────────────────────────────────────┘  │
│                             │                                │
│                             ▼                                │
│  Layer 2: Progressive Gradient Feather (BlendMode.DstIn)     │
│  ┌────────────────────────────────────────────────────────┐  │
│  │ Menghaluskan bibir bawah kaca secara melandai          │  │
│  │ 0.00f -> Solid (1.0f) hingga 1.00f -> Bening (0.0f)   │  │
│  └────────────────────────────────────────────────────────┘  │
│                             │                                │
│                   (ZERO CAT/FOG OVERLAY)                     │
└──────────────────────────────────────────────────────────────┘
```

#### File yang Diperbarui:
- **`GlassmorphismTheme.kt`**:
  - Menghapus layer cat `drawRect(universalDissolverBrush)` dari modifier `diajakGlassHeaderEffect` sehingga foto bebas dari kabut putih (zero foggy paint overlay).
  - Mengonfigurasi `UniversalStyle` dan `OpticalStyle` dengan `blurRadius = 32.dp`, `tint = Color.Transparent`, dan `backgroundColor = Color.Transparent`.
  - Mempertahankan `ProgressiveGradientBrush` dengan `BlendMode.DstIn` untuk memudarkan bibir bawah kaca secara melandai halus.
  - **Menambahkan Arsitektur Tinder/iOS Top Exit-Fade (`Modifier.diajakScrollFadeTop`)**:
    Menambahkan modifier pada kontainer gulir (`LazyColumn`, `Column.verticalScroll`) yang menerapkan gradasi transparansi (`BlendMode.DstIn`) dari `y = 0` (alpha 0.0) hingga batas status bar (alpha 1.0). Saat teks hitam dan kartu digulir ke atas mendekati status bar, opasitasnya meluruh halus dan melebur 100% ke dalam kanvas abu-abu `#E5E7EB` tanpa meninggalkan residu huruf tajam, sementara foto di bawah header kaca tetap 100% cerah dan bebas kabut.
- **`HomeScreen.kt`**:
  - Menerapkan `.diajakScrollFadeTop()` pada `LazyColumn` utama sehingga judul bagian ("Seru di Sekitarmu", "Lagi Ramai Diikuti") dan kartu melebur sempurna ke latar abu-abu saat digulir ke area status bar.
- **`FavoritesScreen.kt` & `DetailScreen.kt`**:
  - Mengintegrasikan `.diajakScrollFadeTop()` untuk menjaga keseragaman peleburan konten di seluruh layar.
