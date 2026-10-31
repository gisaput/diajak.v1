# Implementation Plan - Desain Kontainer Menyatu Halaman Detail (Non-Floating)

Mengubah desain kontainer pada halaman detail (`DetailScreen.kt`) dari sistem kartu mengambang (*floating separate cards*) dengan celah abu-abu di sekelilingnya, menjadi satu kesatuan kontainer putih solid (*non-floating bottom sheet container*) penuh selebar layar (*edge-to-edge width*) dengan sudut atas melengkung (*rounded top corners*), yang merangkum seluruh konten dari Judul aktivitas hingga bagian rekomendasi "Mungkin Kamu Suka" sesuai dengan gambar referensi yang dikirimkan.

---

## User Review Required

> [!IMPORTANT]
> - **Kontainer Menyatu Penuh**: Kontainer putih kini menempel penuh ke sisi kiri dan kanan layar (`fillMaxWidth()`, tanpa padding horizontal luar) dan bertumpuk (*overlap*) mulus di atas gambar hero dengan sudut atas melengkung (`topStart = 28.dp, topEnd = 28.dp`).
> - **Cakupan Kontainer**: Semua bagian informasi (Judul, Jadwal & Lokasi, Tentang Aktivitas & Yang Didapat, Peserta Bergabung & Kuota, Lokasi Peta, Profil Kreator, Ulasan Peserta, hingga carousel "Mungkin Kamu Suka") kini berada di dalam satu kontainer putih yang bersih.
> - **Pemisah Antar Bagian**: Seluruh transisi antar bagian menggunakan garis pemisah halus (*subtle divider*) dan ritme spasi yang konsisten, menghilangkan kesan kartu terpotong-potong.

---

## Proposed Changes

### UI & Layout Detail (`app/src/main/java/com/example/ui/screens/DetailScreen.kt`)

#### 1. Transformasi Kontainer Utama
- Menyatukan komponen `Surface` yang sebelumnya terpisah per seksi (Seksi Judul, Seksi Lokasi & Peserta, Seksi Ulasan, dan Seksi Rekomendasi luar) menjadi **satu `Surface` putih kontinu**.
- Memberikan bentuk sudut membulat di bagian atas (`RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)`) dengan efek tumpukan (*overlap*) ke atas gambar hero (24.dp).
- Menghapus margin samping luar (`padding(horizontal = 20.dp)`) pada kontainer, sehingga kontainer membentang selebar layar (*edge-to-edge*).
- Menetapkan `padding(horizontal = 20.dp)` secara internal untuk konten teks dan elemen di dalamnya agar rapi dan simetris.

#### 2. Penyelarasan Layout Bagian Konten
- **Bagian Judul & Jadwal**: Mengatur tipografi judul yang tegas dan susunan jadwal/lokasi yang elegan sesuai referensi visual.
- **Bagian Tentang Aktivitas**: Menyertakan deskripsi ringkas dengan tombol "Selengkapnya" / "Sembunyikan" dan daftar fasilitas/yang didapat.
- **Bagian Peserta & Host/Lokasi**: Menampilkan stack avatar peserta bergabung, sisa kuota, tautan lokasi, dan profil kreator dengan pemisah garis horizontal bersih (`HorizontalDivider`).
- **Bagian Ulasan**: Menampilkan ulasan peserta dengan bintang rating dan tombol lihat semua ulasan.
- **Bagian "Mungkin Kamu Suka"**: Memasukkan carousel rekomendasi aktivitas ke dalam bagian bawah kontainer putih yang sama, dengan padding horizontal yang selaras dan transisi akhir yang mulus.

---

## Verification Plan

### Automated Verification
1. Jalankan `compile_applet` untuk memastikan seluruh perubahan kode Kotlin dan Jetpack Compose terkompilasi 100% tanpa error sintaks maupun dependensi.
2. Jalankan unit test aplikasi (`gradle :app:testDebugUnitTest`) jika diperlukan untuk memverifikasi tidak ada regresi logika.

### Manual / Visual Verification
1. Buka halaman Detail aktivitas di emulator streaming.
2. Verifikasi bahwa kontainer putih membentang penuh ke sisi layar tanpa celah samping mengambang.
3. Periksa transisi tumpukan (*overlap*) di bagian bawah foto hero utama.
4. Lakukan scroll ke bawah dan pastikan seluruh seksi (Judul, Deskripsi, Kuota/Lokasi, Ulasan, hingga "Mungkin Kamu Suka") berada di dalam kanvas putih yang menyatu dan rapi.
