# Product Requirement Document (PRD) - Aplikasi "Diajak"
**Status**: Final / Blueprint Komprehensif  
**Target Platform**: Android (Jetpack Compose, Kotlin)  
**Tujuan**: Menjadi panduan lengkap UX, alur kerja (workflow), arsitektur data, interaksi transisi, dan pedoman desain visual untuk pembuatan ulang aplikasi dengan template berbeda.

---

## 1. Pendahuluan & Ringkasan Produk

### 1.1 Deskripsi Singkat
**Diajak** adalah platform penemuan (discovery), pemesanan tiket, dan manajemen aktivitas komunitas yang menghubungkan penyelenggara acara (Kreator) dengan peserta yang ingin berpartisipasi (Peserta). Aplikasi ini mengutamakan navigasi berbasis peta (map explore), penemuan cepat melalui kategori hobi, sistem pembayaran instan (e-payment), serta pengelolaan akun kreator yang terverifikasi melalui fitur unggah dokumen identitas.

### 1.2 Target Pengguna
1. **Peserta (User/Participant)**: Individu yang ingin mencari kegiatan hobi, hiburan, olahraga, kuliner, kelas edukasi, dll., memesan tiket secara instan, dan melacak tiket mereka secara digital.
2. **Kreator/Penyelenggara (Host/Creator)**: Komunitas atau individu profesional yang menyelenggarakan kegiatan, mengelola daftar peserta, memantau pendapatan, dan memerlukan akun yang terverifikasi untuk mencairkan dana.

---

## 2. Pedoman Desain Visual & UI/UX (Design System)

Untuk memastikan konsistensi visual di seluruh modul dan screen, template masa depan harus mematuhi aturan standar desain berikut:

### 2.1 Skema Warna & Tema (Material Design 3)
*   **Warna Utama (Primary)**: `DiajakOrange` (`#FF5A36` atau `#FF5F1F`) melambangkan antusiasme, keceriaan, dan energi komunitas.
*   **Warna Latar Belakang (Background)**: Pure White (`#FFFFFF`) untuk area interaktif atau kontainer utama, dipadukan dengan Soft Gray (`#F8FAFC`) untuk latar belakang halaman guna memberikan kesan lapang dan bersih (generous negative space).
*   **Warna Teks**:
    *   **Primary Text**: Gelap/Charcoal (`#0F172A`) untuk tingkat keterbacaan yang tinggi.
    *   **Secondary Text**: Slate Gray (`#64748B`) untuk keterangan detail atau sub-header.
*   **Warna Status**:
    *   **Sukses**: Hijau (`#10B981`)
    *   **Menunggu/Pending**: Amber (`#F59E0B`)

### 2.2 Aturan Konsistensi Kartu & Kontainer (Card Styling Consistency Rule)
Sesuai instruksi desain khusus aplikasi **Diajak**, semua kartu kontainer hobi, opsi pembayaran, dan daftar kegiatan wajib menggunakan spesifikasi desain standar berikut (Jetpack Compose):
*   **Sudut Lengkung (Corner Radius)**: Selalu `16.dp` (`RoundedCornerShape(16.dp)`).
*   **Warna Kontainer**: Putih murni (`Color.White`) pada tema terang.
*   **Garis Tepi (Border Stroke)**: Garis tipis abu-abu `1.dp` dengan warna `#DADCE0` (`Color(0xFFDADCE0)`).
*   **Status Aktif/Terpilih (Selected State)**: Hanya jika kartu dipilih (misalnya, metode pembayaran aktif atau kategori terpilih), garis tepi berubah menjadi `DiajakOrange` dengan ketebalan `2.dp`.
*   **Bayangan (Elevation)**: Efek bayangan halus `2.dp` (`CardDefaults.cardElevation(defaultElevation = 2.dp)`) untuk memberikan dimensi premium tanpa terkesan kaku.

---

## 3. Arsitektur Navigasi & Manajemen State Global

Navigasi aplikasi dikelola secara terpusat melalui ViewModel utama (`DiajakViewModel`) menggunakan reactive StateFlow (`_selectedTab`). Ini memudahkan transisi mulus antar layar tanpa merusak siklus hidup data:
*   `Tab 0`: Beranda (HomeScreen)
*   `Tab 1`: Favorit (FavoritesScreen)
*   `Tab 2`: Undangan / Tiket Saya (BookingsScreen)
*   `Tab 3`: Pesan & Notifikasi (MessagesScreen)
*   `Tab 4`: Profil Pengguna (ProfileScreen)
*   `Tab 5`: Jelajah Peta (MapExploreScreen)

---

## 4. Spesifikasi Fungsional & Interaksi UX Detail (Screen-by-Screen)

### 4.1 Halaman Beranda (HomeScreen)

Halaman utama penjelajahan hobi yang didesain interaktif dengan bilah pencarian mengambang.

#### A. Elemen UI & UX Halaman Beranda
1.  **Header Lokasi**:
    *   **UX & Interaksi**: Menampilkan nama wilayah aktif (contoh: "Jakarta, Ina"). Saat diketuk, sistem meminta izin lokasi GPS (`ACCESS_FINE_LOCATION`) secara runtime. Jika diizinkan, koordinat diperbarui secara otomatis dan nama wilayah terkini ditampilkan.
2.  **Floating Unified Search Bar**:
    *   **UX & Interaksi**: Mengadopsi gaya pencarian Google Maps yang mengambang dengan lencana logo "Diajak" di sebelah kiri dan ikon pencarian di kanan.
    *   **Transisi Menuju Halaman Explore (KUNCI)**: 
        *   Ketika pengguna memfokuskan kursor pada bar pencarian atau mulai mengetik kata kunci hobi, filter pencarian disinkronkan ke ViewModel secara real-time.
        *   Begitu pengguna menekan tombol **"Cari" (Search/Enter) pada keyboard virtual** atau **mengetuk ikon pencarian (kaca pembesar)**, aplikasi secara otomatis **mengalihkan tab aktif ke Tab 5 (MapExploreScreen)** dengan membawa query pencarian tersebut. Peta dan daftar acara akan langsung tersaring secara dinamis di layar Jelajah Peta.
        *   Terdapat tombol "X" (Clear) untuk menghapus query secara instan dan mengembalikan semua kategori ke status semula.
3.  **Spesial Buat Kamu (Promotional Banners)**:
    *   **UX & Interaksi**: Berbentuk horizontal scrolling carousel yang memajang promo eksklusif.
    *   **Transisi Navigasi Berbasis Filter Promo (KUNCI)**: Mengetuk kartu promosi tertentu akan langsung mengalihkan tab ke **Tab 5 (MapExploreScreen)** dan menerapkan filter spesifik yang sesuai dengan program promo tersebut:
        *   **Promo "Hemat Abis" (p4)**: Otomatis mengarahkan ke halaman peta, mereset filter lainnya, dan mengaktifkan filter harga **"Di bawah Rp 100 Ribu" (under100k)**.
        *   **Promo "Masuk Gratis" (p3)**: Otomatis mengarahkan ke halaman peta, mereset filter lainnya, dan mengaktifkan filter harga **"Gratis" (free)**.
        *   **Promo "Outdoor Adventure" (p1)**: Otomatis mengarahkan ke halaman peta, mereset filter lainnya, dan memfokuskan kategori hobi pada **"Outdoor"**.
        *   **Promo "Santai di Coffee Shop" (p2)**: Otomatis mengarahkan ke halaman peta, mereset filter lainnya, dan memfokuskan kategori hobi pada **"Kopi / Cafe"**.
4.  **Daftar Rekomendasi Horizontal**:
    *   Terdiri dari kelompok dinamis: "Seru di Sekitarmu" (jarak terdekat berbasis lokasi GPS), "Lagi Ramai Diikuti" (jumlah pendaftar terbanyak), "Ratingnya Juara" (rating tertinggi), dan "Ada Yang Baru Loh" (acara yang baru dipublikasikan).
    *   **UX & Interaksi**: Mengetuk salah satu kartu acara akan membuka overlay **Halaman Detail Acara** dengan transisi slide-up animasi dari bawah layar.

---

### 4.2 Halaman Jelajah Peta (MapExploreScreen / Tab 5)

Halaman penjelajahan interaktif dua-dalam-satu yang menggabungkan Google Maps dinamis dengan filter komprehensif.

#### A. Elemen UI & UX Halaman Jelajah Peta
1.  **Filter Chips Kontainer**:
    *   Menampilkan deretan filter horizontal: **Kategori Hobi** (Olahraga, Kuliner, Musik, dll.), **Filter Harga** (Semua, Gratis, <Rp 100k, >Rp 100k), **Filter Rating** (Semua, ★ 4.5+), dan **Filter Wilayah** (Jakarta, Bandung, Bali, Yogyakarta).
    *   **UX & Interaksi**: Mengetuk chip filter akan memicu penyaringan data instan pada koordinat pin peta (markers) dan daftar kartu hobi di bawah layar tanpa perlu memuat ulang seluruh halaman.
2.  **Interactive Map Canvas**:
    *   Menampilkan pin lokasi (markers) acara yang relevan dengan filter.
    *   **UX & Interaksi**: Mengetuk salah satu pin lokasi di peta akan memusatkan kamera pada koordinat tersebut dan menampilkan kartu preview kecil acara di bagian bawah peta. Mengetuk kartu preview tersebut akan membuka **Halaman Detail Acara**.

---

### 4.3 Halaman Detail Acara (Event Detail Page / DetailScreen)

Halaman penjelas mendalam mengenai aktivitas yang dipilih pengguna untuk membangun keyakinan sebelum melakukan pembelian.

#### A. Elemen UI & UX Halaman Detail
1.  **Hero Poster**:
    *   Gambar poster beresolusi tinggi di bagian atas layar dengan efek lengkungan halus di bagian bawah.
    *   Tombol navigasi melayang: "Kembali" (kiri) dan "Bagikan" (kanan).
2.  **Deskripsi Acara (About This Event)**:
    *   Teks naratif panjang mengenai detail kegiatan.
    *   Menggunakan pemotongan teks dinamis (Expandable Text) "Lihat Selengkapnya" jika deskripsi melebihi 4 baris.
3.  **Daftar Keuntungan (Highlights / Benefits)**:
    *   Daftar poin kelebihan bergabung dalam acara menggunakan ikon centang hijau atau oranye (contoh: "Mendapat sertifikat resmi", "Termasuk alat & bahan praktek").
4.  **Bar Tindakan Bawah (Sticky Action Bottom Bar)**:
    *   Ikon "Favorit/Heart" untuk menyimpan acara ke dalam bookmark lokal (data disimpan menggunakan Room database).
    *   Tombol Utama: **"Get a Ticket" (Ambil Tiket)** berwarna jingga menyala (`DiajakOrange`) dengan lebar penuh guna memicu interaksi utama.

---

### 4.4 Halaman Alur Pemesanan & Pembayaran (Booking & Payment Flow)

Sistem pemesanan interaktif multi-langkah (Multi-step Booking) yang dirancang secara linear untuk meminimalkan hambatan kognitif pengguna.

#### A. Tahap 1: Pengisian Data & Opsi Jumlah Tiket (Data Pemesanan & Pengunjung)
*   **Pilihan Tanggal**: Horizontal carousel yang menampilkan daftar tanggal alternatif yang tersedia untuk acara tersebut.
*   **Jumlah Tiket (Undangan)**: Selector kuantitas dengan tombol minus (`-`) dan plus (`+`) yang interaktif. Jumlah dibatasi berdasarkan kapasitas kuota tersisa (`maxPeserta - currentPeserta`).
*   **Data Kontak Pengunjung**: Input form untuk Name, Email, dan No. Telepon (terisi otomatis jika pengguna sudah melengkapi profilnya).
*   **Metode Pembayaran (Payment Method Selection)**:
    *   Opsi metode pembayaran disajikan sebagai kartu terpisah berdasarkan **Card Styling Consistency Rule**:
        *   **QRIS (Gopay / ShopeePay)**
        *   **BCA Virtual Account**
        *   **Mandiri Virtual Account**
        *   **Credit/Debit Card**
    *   *UX Feedback*: Kartu metode pembayaran yang aktif wajib mendapatkan garis tepi warna `DiajakOrange` setebal `2.dp` dan tanda centang aktif.
*   **Ringkasan Biaya (Price Breakdown)**:
    *   Menampilkan Subtotal Tiket, Biaya Layanan/Transaksi (jika ada), dan Total Pembayaran dalam teks tebal warna gelap.
*   **Tombol Konfirmasi**: "Bayar Sekarang" yang mematikan layar (loading state) untuk memproses pesanan secara aman.

#### B. Tahap 2: Halaman Instruksi Pembayaran (Payment Processing / Midtrans Simulation)
*   Menampilkan countdown waktu pembayaran (contoh: "Selesaikan dalam waktu 14:59").
*   Menampilkan Nomor Virtual Account (VA) atau Kode QRIS dinamis sesuai metode pembayaran yang dipilih.
*   Menyediakan tombol **"Salin" (Copy to Clipboard)** di sebelah nomor rekening VA untuk memudahkan pengguna memindahkan nomor ke aplikasi Mobile Banking mereka, dilengkapi notifikasi Toast sukses menyalin.
*   Menampilkan petunjuk langkah transfer bank secara urut dan jelas (Drop-down Accordion).
*   Sistem secara berkala mensimulasikan status pembayaran sukses secara otomatis (atau melalui simulasi interaktif "Saya Sudah Bayar") untuk memicu transisi ke status sukses.

---

### 4.5 Halaman Tiket Digital / Undangan (Digital Ticket / E-Ticket Screen)

Halaman pasca-pembelian yang menampilkan bukti otentik transaksi pengguna dalam bentuk digital tiket yang elegan.

#### A. Elemen UI & UX Tiket Digital
1.  **Header Desain Kartu Tiket**:
    *   Mengadopsi format potongan tiket fisik dengan efek lingkaran sobekan di bagian samping kiri dan kanan (dashed cut lines).
    *   Poster mini kegiatan di bagian atas.
2.  **Informasi Otentikasi**:
    *   Judul Acara Terpesan.
    *   Garis putus-putus horizontal sebagai pemisah visual.
    *   Tabel dua kolom berisi:
        *   **Tanggal**: Tanggal sesi terpilih.
        *   **Waktu**: Jam mulai hingga selesai.
        *   **Tempat/Venue**: Alamat fisik lokasi pelaksanaan (Contoh: "Gelora Bung Karno").
        *   **Nomor Kursi/Seat**: Keterangan nomor kursi (Contoh: "No Seat" untuk festival).
3.  **Grafis Kode Batang / QR Code**:
    *   Tampilan barcode linier atau kode QR matriks di bagian bawah tiket yang merujuk pada `Booking ID` unik (Format ID: `DJK-XXXXXX`).
4.  **Tombol Aksi Bawah**:
    *   **Tombol "Simpan Gambar" (Image)**: Mengonversi tampilan tiket menjadi file gambar lokal atau memicu pengunduhan ke memori telepon pengguna.
    *   **Tombol "QR Code"**: Menampilkan kode QR berukuran besar dalam bentuk dialog overlay modal untuk dipindai oleh petugas loket di lokasi fisik acara.

---

### 4.6 Halaman Dashboard Kreator & Kelola Acara (Creator Dashboard Screen)

Halaman khusus untuk pengguna yang beralih peran sebagai penyelenggara kegiatan hobi (Host/Organizer).

#### A. Elemen UI & UX Dashboard Kreator
1.  **Bilah Beralih Mode (Kreator Mode Switch)**:
    *   Berada di halaman Profil atau Beranda. Tombol toggle geser yang mengubah tema aplikasi dari mode Peserta (fokus mencari acara) ke mode Kreator (fokus mengelola acara).
2.  **Statistik Cepat (Analytics Widget)**:
    *   Kartu berdesain putih bersih yang menampilkan metrik performa: "Total Pendapatan", "Jumlah Tiket Terjual", dan "Acara Aktif".
3.  **Daftar Kelola Acara (Manage Events List)**:
    *   Menampilkan daftar kegiatan yang telah dibuat oleh kreator bersangkutan.
    *   Terdapat opsi edit detail, hapus kegiatan, atau memantau daftar peserta yang telah membeli tiket secara real-time.
4.  **Tombol Buat Acara (FAB / Floating Action Button)**:
    *   Tombol bulat berwarna `DiajakOrange` berlambang plus (`+`) di pojok kanan bawah untuk memicu **Dialog Pembuatan Acara Baru** secara instan.
5.  **Status Verifikasi Akun**:
    *   Jika dokumen belum diverifikasi, sistem memajang banner peringatan merah/oranye di bagian atas dashboard dengan tombol aksi: **"Lengkapi Verifikasi Dokumen"**.

---

### 4.7 Halaman Upload Dokumen Lengkap (Document Upload Screen)

Halaman krusial bagi Kreator untuk membuktikan identitas hukum mereka sebelum diizinkan mempublikasikan acara berbayar atau melakukan penarikan dana pendapatan (pencairan saldo).

#### A. Elemen UI & UX Halaman Upload Dokumen
1.  **Sub-Header Panduan**:
    *   Teks instruksional: "Upload dokumen berikut untuk keperluan verifikasi akun dan pencairan dana Anda."
2.  **Kartu Unggah KTP (KTP Upload Section)**:
    *   Kartu interaktif putih sesuai **Card Styling Consistency Rule**.
    *   Menampilkan judul: "Kartu Tanda Penduduk (KTP)".
    *   Deskripsi: "Pastikan foto KTP terlihat jelas, tidak terpotong, dan tulisan dapat dibaca."
    *   Area interaktif unggah: Menampilkan ikon Cloud Upload dengan tombol "Pilih File KTP".
    *   *State feedback*: Jika file berhasil diunggah, area berubah menampilkan ikon centang hijau bulat, indikator teks "Sudah Diupload", dan thumbnail/nama file terpilih.
3.  **Kartu Unggah Buku Tabungan (Bank Document Upload Section)**:
    *   Menampilkan judul: "Buku Tabungan / Rekening".
    *   Deskripsi: "Upload foto bagian depan buku tabungan yang menampilkan nama dan nomor rekening."
    *   Area interaktif unggah: Menampilkan ikon Cloud Upload dengan tombol "Pilih File Buku Tabungan".
    *   *State feedback*: Memberikan feedback visual yang identik jika dokumen bank berhasil dimuat dalam aplikasi.
4.  **Tombol Simpan Dokumen (Save Action)**:
    *   Berwarna abu-abu non-aktif jika kedua dokumen (KTP dan Buku Tabungan) belum diunggah.
    *   Berwarna `DiajakOrange` aktif jika kedua dokumen telah diunggah dengan lengkap.
    *   Saat ditekan, menampilkan visual loading melingkar (Circular Progress Indicator) untuk melambangkan pengiriman data ke server aman, dilanjutkan Toast pemberitahuan: "Dokumen berhasil disimpan!".

---

## 5. Arsitektur Data & Model Utama (Data Structure & Entities)

Model data yang mendukung alur kerja di atas agar sinkron antara Room Database lokal dan State Management ViewModel:

### 5.1 Entitas Acara (Activity / Event Entity)
Menyimpan informasi dasar setiap kegiatan hobi.
*   `id` (String - Primary Key)
*   `title` (String) - Judul acara (contoh: "Oliver Tree Concert")
*   `category` (String) - Kategori hobi (contoh: "music")
*   `location` (String) - Lokasi fisik (contoh: "Jakarta, Indonesia")
*   `schedule` (String) - Tanggal dan jam pelaksanaan (contoh: "29 December 2022, 10:00 PM")
*   `priceValue` (Int) - Nilai numerik harga tiket (contoh: `650000` atau `0` untuk gratis)
*   `rating` (Double) - Angka rating kepuasan (contoh: `4.8`)
*   `description` (String) - Deskripsi acara lengkap
*   `imageRes` (Int / String) - Referensi gambar poster hobi
*   `currentPeserta` (Int) - Jumlah tiket yang sudah laku terjual
*   `maxPeserta` (Int) - Batas kuota maksimal peserta acara

### 5.2 Entitas Booking (Booking Transaction Entity)
Mencatat sejarah transaksi tiket peserta.
*   `bookingId` (String - Primary Key) - Contoh format: `DJK-A1B2C3D4`
*   `activityId` (String) - Menghubungkan ke ID Acara
*   `userName` (String) - Nama lengkap pengunjung terdaftar
*   `userEmail` (String) - Surat elektronik pengunjung
*   `userPhone` (String) - Nomor telepon genggam aktif
*   `undanganCount` (Int) - Kuantitas tiket yang dibeli
*   `totalPaid` (Int) - Total akumulasi pembayaran dalam Rupiah
*   `paymentMethod` (String) - Metode transfer pilihan (contoh: "qris", "bca_va")
*   `selectedDate` (String) - Tanggal pilihan kehadiran acara
*   `bookingTime` (Long) - Timestamp detik waktu pembelian tiket

---

## 6. Matriks Alur Pengguna & Transisi Navigasi Lengkap (User Journey Mapping)

Berikut adalah detail visual aliran navigasi interaktif dari Beranda hingga Tiket Digital:

```
[ Beranda / Temukan (HomeScreen) ]
   │
   ├─► (Ketik pada Kolom Pencarian / Tekan Enter / Klik Ikon Cari) ──► Mengalihkan ke Tab 5 [ Jelajah Peta (MapExplore) ] dengan query pencarian aktif.
   │
   ├─► (Klik Banner Promo "Hemat Abis" / p4) ───────────────────────► Mengalihkan ke Tab 5 [ Jelajah Peta (MapExplore) ] dengan filter harga < 100k.
   │
   ├─► (Klik Banner Promo "Masuk Gratis" / p3) ──────────────────────► Mengalihkan ke Tab 5 [ Jelajah Peta (MapExplore) ] dengan filter harga Gratis.
   │
   ├─► (Klik Banner Promo "Outdoor Adventure" / p1) ────────────────► Mengalihkan ke Tab 5 [ Jelajah Peta (MapExplore) ] dengan filter kategori "Outdoor".
   │
   ├─► (Klik Banner Promo "Santai di Coffee" / p2) ─────────────────► Mengalihkan ke Tab 5 [ Jelajah Peta (MapExplore) ] dengan filter kategori "Kopi".
   │
   └─► (Ketuk Kartu Acara Rekomendasi) ──────────────────────────────► Membuka overlay [ Detail Acara (DetailScreen) ].
                                                                                  │
                                                                                  ▼ (Ketuk "Get a Ticket")
                                                                     [ Booking Step 1: Formulir Kontak & Kuantitas ]
                                                                                  │
                                                                                  ▼ (Ketuk "Bayar Sekarang")
                                                                     [ Booking Step 2: Instruksi Pembayaran (Simulasi VA/QRIS) ]
                                                                                  │
                                                                                  ▼ (Simulasi Deteksi Pembayaran Berhasil)
                                                                     [ Booking Step 3: Tiket Digital (E-Ticket / Barcode) ]
```

**Beralih Peran (Kreator Workflow):**
```
[ Profil Peserta / Switch Mode ]
        │
        ▼ (Geser Toggle Mode Kreator)
[ Dashboard Kreator (Pendapatan & List Acara) ]
        │
        ▼ (Ketuk "Lengkapi Verifikasi Dokumen" - Jika baru)
[ Upload Dokumen (KTP + Buku Tabungan) ]
        │
        ▼ (Ketuk "Simpan Dokumen" -> Verifikasi Berhasil)
[ Dashboard Kreator - Aktif Kuasa Buat Acara ]
```

---

## 7. Persyaratan Non-Fungsional (Non-Functional Requirements)

1.  **Keamanan Data (Data Privacy)**: Foto dokumen KTP dan nomor rekening Buku Tabungan tidak boleh disimpan dalam folder publik telepon. Wajib diamankan dalam enkripsi sandbox storage aplikasi.
2.  **Kinerja (Performance & Speed)**:
    *   Pencarian hobi berbasis peta harus merespons dalam waktu < 500ms saat pengguna menggeser koordinat peta atau mengubah filter pencarian/kategori.
    *   Skalabilitas layout harus adaptif terhadap berbagai ukuran layar smartphone Android terkini dengan kelengkapan jarak grid padding minimal 16.dp.
3.  **Ketersediaan Luring (Offline Mode)**: Tiket digital yang telah lunas wajib dapat dibuka kapan saja meskipun pengguna sedang tidak terhubung dengan koneksi internet (menggunakan penyimpanan lokal SQLite/Room).
