package com.example.data

import android.content.Context
import com.example.R
import com.example.model.*
import com.example.data.database.AppDatabase
import com.example.data.database.ActivityEntity

import com.example.data.database.BookingEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DiajakRepository {

  val categoryNames = listOf(
    "Alam", "Amal", "Bahasa", "Bazar", "Belanja", "Bimbingan", "Bisnis", "Budaya", "Busana",
    "Diskusi", "Esport", "Festival", "Film", "Fotografi", "Gelarwicara", "Hewan", "Hiburan", "Hobi", "Identitas",
    "Investasi", "Jejaring", "Karier", "Kebugaran", "Kecantikan", "Keluarga", "Kemah", "Kesehatan", "Kesejahteraan", "Keuangan",
    "Komedi", "Komunitas", "Konser", "Kopi", "Kriya", "Kuliner", "Lari", "Lingkungan", "Lokakarya", "Media",
    "Meditasi", "Menari", "Menulis", "Musik", "Nongkrong", "Olahraga", "Otomotif", "Pameran", "Pelatihan", "Pendidikan",
    "Pendakian", "Pengembangan", "Perjalanan", "Permainan", "Pesta", "Petualangan", "Piknik", "Relaksasi", "Relawan", "Religi", "Sains",
    "Seminar", "Seni", "Sepeda", "Sosial", "Spiritualitas", "Teater", "Teknologi", "Wisata", "Yoga"
  )

  val categories = listOf(
    CategoryItem("all", "Semua", "🌟"),
    CategoryItem("alam", "Alam", "🌲"),
    CategoryItem("amal", "Amal", "💝"),
    CategoryItem("bahasa", "Bahasa", "🗣️"),
    CategoryItem("bazar", "Bazar", "🎪"),
    CategoryItem("belanja", "Belanja", "🛍️"),
    CategoryItem("bimbingan", "Bimbingan", "🧭"),
    CategoryItem("bisnis", "Bisnis", "💼"),
    CategoryItem("budaya", "Budaya", "🎭"),
    CategoryItem("busana", "Busana", "👗"),
    CategoryItem("diskusi", "Diskusi", "💬"),
    CategoryItem("esport", "Esport", "🎮"),
    CategoryItem("festival", "Festival", "🎉"),
    CategoryItem("film", "Film", "🎬"),
    CategoryItem("fotografi", "Fotografi", "📸"),
    CategoryItem("gelarwicara", "Gelarwicara", "🎙️"),
    CategoryItem("hewan", "Hewan", "🐾"),
    CategoryItem("hiburan", "Hiburan", "🎡"),
    CategoryItem("hobi", "Hobi", "🧩"),
    CategoryItem("identitas", "Identitas", "🪪"),
    CategoryItem("investasi", "Investasi", "📈"),
    CategoryItem("jejaring", "Jejaring", "🌐"),
    CategoryItem("karier", "Karier", "👔"),
    CategoryItem("kebugaran", "Kebugaran", "💪"),
    CategoryItem("kecantikan", "Kecantikan", "💄"),
    CategoryItem("keluarga", "Keluarga", "👨‍👩‍👧"),
    CategoryItem("kemah", "Kemah", "🏕️"),
    CategoryItem("kesehatan", "Kesehatan", "🩺"),
    CategoryItem("kesejahteraan", "Kesejahteraan", "🌿"),
    CategoryItem("keuangan", "Keuangan", "💰"),
    CategoryItem("komedi", "Komedi", "😂"),
    CategoryItem("komunitas", "Komunitas", "🤝"),
    CategoryItem("konser", "Konser", "🎸"),
    CategoryItem("kopi", "Kopi", "☕"),
    CategoryItem("kriya", "Kriya", "🧵"),
    CategoryItem("kuliner", "Kuliner", "🍲"),
    CategoryItem("lari", "Lari", "🏃"),
    CategoryItem("lingkungan", "Lingkungan", "🌱"),
    CategoryItem("lokakarya", "Lokakarya", "🔨"),
    CategoryItem("media", "Media", "📰"),
    CategoryItem("meditasi", "Meditasi", "🧘"),
    CategoryItem("menari", "Menari", "💃"),
    CategoryItem("menulis", "Menulis", "✍️"),
    CategoryItem("musik", "Musik", "🎵"),
    CategoryItem("nongkrong", "Nongkrong", "🛋️"),
    CategoryItem("olahraga", "Olahraga", "⚽"),
    CategoryItem("otomotif", "Otomotif", "🚗"),
    CategoryItem("pameran", "Pameran", "🖼️"),
    CategoryItem("pelatihan", "Pelatihan", "📚"),
    CategoryItem("pendidikan", "Pendidikan", "🎓"),
    CategoryItem("pendakian", "Pendakian", "🧗"),
    CategoryItem("pengembangan", "Pengembangan", "🚀"),
    CategoryItem("perjalanan", "Perjalanan", "✈️"),
    CategoryItem("permainan", "Permainan", "🎲"),
    CategoryItem("pesta", "Pesta", "🥳"),
    CategoryItem("petualangan", "Petualangan", "🗺️"),
    CategoryItem("piknik", "Piknik", "🧺"),
    CategoryItem("relaksasi", "Relaksasi", "💆"),
    CategoryItem("relawan", "Relawan", "🤲"),
    CategoryItem("religi", "Religi", "🕊️"),
    CategoryItem("sains", "Sains", "🔬"),
    CategoryItem("seminar", "Seminar", "🎤"),
    CategoryItem("seni", "Seni", "🎨"),
    CategoryItem("sepeda", "Sepeda", "🚴"),
    CategoryItem("sosial", "Sosial", "👥"),
    CategoryItem("spiritualitas", "Spiritualitas", "✨"),
    CategoryItem("teater", "Teater", "🎟️"),
    CategoryItem("teknologi", "Teknologi", "💻"),
    CategoryItem("wisata", "Wisata", "🏖️"),
    CategoryItem("yoga", "Yoga", "🧘‍♂️")
  )

  val promotions = listOf(
    PromoModel(
      id = "p4",
      title = "Aktivitas Seru di Bawah Rp 100.000",
      subtitle = "Nikmati berbagai pilihan aktivitas kreatif dan rekreasi ramah di kantong",
      buttonText = "Cek Sekarang",
      imageResId = R.drawable.diajak_activity_coffee_1783253596378,
      badge = "Hemat < 100rb"
    ),
    PromoModel(
      id = "p3",
      title = "Pengalaman Gratis Menarik",
      subtitle = "Ikuti sesi trial gratis & temukan aktivitas seru bareng komunitas kami",
      buttonText = "Coba Gratis",
      imageResId = R.drawable.diajak_onboarding_hero_1783253536983,
      badge = "Gratis 100%"
    ),
    PromoModel(
      id = "p1",
      title = "Visit 3 Beach & Get 50% Disc",
      subtitle = "Jelajah keindahan pantai Bali & Lombok bareng tim kami",
      buttonText = "Klaim Kupon",
      imageResId = R.drawable.diajak_banner_outdoor_1783253550783,
      badge = "Diskon 50%"
    ),
    PromoModel(
      id = "p2",
      title = "Workshop Kopi Spesial Weekend",
      subtitle = "Belajar latte art & sensory bareng roaster juara nasional",
      buttonText = "Daftar Diskon",
      imageResId = R.drawable.diajak_banner_workshop_1783253562818,
      badge = "Promo Spesial"
    )
  )

  private val initialActivities = listOf(
    ActivityModel(
      id = "act_1",
      title = "Barista Experience & Latte Art 101",
      category = "Kopi",
      locationName = "Badung, Bali",
      address = "Jl. Pantai Kuta No. 32, Legian, Kuta, Badung",
      schedule = "12 Sep 2026 • 09:00 - 13:00",
      rating = 4.9,
      reviewsCount = 84,
      priceFormatted = "Rp 150.000",
      priceValue = 150000,
      imageResId = R.drawable.diajak_activity_coffee_1783253596378,
      overview = "Bergabunglah dengan sesi interaktif bersama roaster & barista profesional kami di Balines Coffeeshop. Kamu akan belajar teknik dasar espresso extraction, milk frothing yang lembut, hingga menuangkan latte art bentuk heart dan tulip. Sangat cocok untuk pemula maupun pencinta kopi!",
      detailsList = listOf(
        "Sertifikat Mini Barista",
        "Free tasting 3 varian biji kopi nusantara",
        "Apron & perlengkapan brewing disediakan",
        "Goodie bag biji kopi arabika 200gr"
      ),
      galleryImages = listOf(
        R.drawable.diajak_activity_coffee_1783253596378,
        R.drawable.diajak_banner_workshop_1783253562818,
        R.drawable.diajak_onboarding_hero_1783253536983
      ),
      kreatorName = "Balines Coffeeshop Roastery",
      kreatorVerified = true,
      maxPeserta = 15,
      currentPeserta = 8,
      mapX = 0.65f,
      mapY = 0.55f
    ),
    ActivityModel(
      id = "act_2",
      title = "Glamping & Sunrise Di Ranca Upas",
      category = "Alam",
      locationName = "Bandung, INA",
      address = "Kawasan Hutan Lindung Ranca Upas, Ciwidey, Bandung",
      schedule = "20 Sep 2026 • 15:00 - 10:00",
      rating = 4.9,
      reviewsCount = 142,
      priceFormatted = "Rp 275.000",
      priceValue = 275000,
      imageResId = R.drawable.diajak_activity_glamping_1783253582807,
      overview = "Ranca Upas telah lama menjadi destinasi favorit pecinta kabut pagi dan udara pegunungan yang sejuk. Malam hari kita berkumpul di sekeliling api unggun, acoustic jam session, BBQ marshmallow, dan menyaksikan matahari terbit keemasan menyelimuti danau penangkaran rusa.",
      detailsList = listOf(
        "Tenda dome VIP kapasitas 4 orang dengan kasur tebal",
        "Makan malam BBQ & sarapan pagi hangat",
        "Undangan masuk kawasan & penangkaran rusa",
        "Pemandu outdoor profesional bersertifikat"
      ),
      galleryImages = listOf(
        R.drawable.diajak_activity_glamping_1783253582807,
        R.drawable.diajak_banner_outdoor_1783253550783,
        R.drawable.diajak_onboarding_hero_1783253536983
      ),
      kreatorName = "Bandung Explorer Club",
      kreatorVerified = true,
      maxPeserta = 20,
      currentPeserta = 16,
      mapX = 0.35f,
      mapY = 0.30f,
      kreatorLastActiveDaysAgo = 9
    ),
    ActivityModel(
      id = "act_3",
      title = "Sunset Kuta Beach Walk & Photo Meetup",
      category = "Fotografi",
      locationName = "Bali, INA",
      address = "Pantai Kuta Gerbang Utama, Denpasar Bali",
      schedule = "18 Sep 2026 • 16:30 - 19:00",
      rating = 4.8,
      reviewsCount = 210,
      priceFormatted = "Gratis",
      priceValue = 0,
      imageResId = R.drawable.diajak_banner_outdoor_1783253550783,
      overview = "Pantai Kuta selalu menarik kerumunan backpacker dan peselancar dari seluruh dunia. Dalam sesi meetup komunitas ini, kita berjalan santai menyusuri pesisir pasir keemasan sambil berbagi tips street photography dan portrait saat matahari terbenam.",
      detailsList = listOf(
        "Terbuka untuk kamera smartphone maupun DSLR/mirrorless",
        "Sesi sharing teknik golden hour lighting",
        "Networking bareng fotografer & content creator lokal",
        "Refreshment kelapa muda dingin di akhir sesi"
      ),
      galleryImages = listOf(
        R.drawable.diajak_banner_outdoor_1783253550783,
        R.drawable.diajak_activity_glamping_1783253582807
      ),
      kreatorName = "Bali Photo Community",
      kreatorVerified = true,
      maxPeserta = 30,
      currentPeserta = 24,
      mapX = 0.50f,
      mapY = 0.70f,
      isOngoing = true
    ),
    ActivityModel(
      id = "act_4",
      title = "Pottery & Clay Art Workshop Sore",
      category = "Kriya",
      locationName = "Jakarta Selatan, INA",
      address = "Studio Keramik Senopati No. 18, Kebayoran Baru",
      schedule = "26 Sep 2026 • 14:00 - 17:00",
      rating = 4.7,
      reviewsCount = 65,
      priceFormatted = "Rp 210.000",
      priceValue = 210000,
      imageResId = R.drawable.diajak_banner_workshop_1783253562818,
      overview = "Rasakan ketenangan dalam membentuk tanah liat dengan tanganmu sendiri di studio seni yang estetik. Dipandu oleh instruktur keramik berpengalaman, kamu akan membuat cangkir kopi atau mangkuk custom yang bisa dibakar dan dikirim ke rumahmu.",
      detailsList = listOf(
        "Tanah liat premium 1 kg & peminjaman meja putar",
        "Proses pembakaran gletser & pewarnaan keramik",
        "Free teh herbal atau iced americano selama sesi",
        "Garansi pengemasan aman sampai rumah"
      ),
      galleryImages = listOf(
        R.drawable.diajak_banner_workshop_1783253562818,
        R.drawable.diajak_activity_coffee_1783253596378
      ),
      kreatorName = "Tanah Liat Art Studio",
      kreatorVerified = true,
      maxPeserta = 12,
      currentPeserta = 9,
      mapX = 0.25f,
      mapY = 0.45f
    ),
    ActivityModel(
      id = "act_5",
      title = "5K City Night Run & Hydration Social",
      category = "Lari",
      locationName = "Surabaya, INA",
      address = "Taman Bungkul Start Point, Surabaya Pusat",
      schedule = "16 Sep 2026 • 19:30 - 21:30",
      rating = 4.9,
      reviewsCount = 315,
      priceFormatted = "Gratis",
      priceValue = 0,
      imageResId = R.drawable.img_sport_running_1783584004743,
      overview = "Lari malam seru keliling pusat kota Surabaya dengan pace santai 6:30 - 7:30 min/km. Sangat ramah untuk pemula (newbie friendly). Setelah finish di Taman Bungkul, kita ngobrol santai sambil menikmati hidrasi elektrolit dan kuliner jajanan malam.",
      detailsList = listOf(
        "Pacer & Marshall penunjuk rute yang ramah",
        "Water station di kilometer 2.5 dan finish",
        "Foto dokumentasi action kamera beresolusi tinggi",
        "Penitipan tas (bag drop) aman di titik start"
      ),
      galleryImages = listOf(
        R.drawable.img_sport_running_1783584004743,
        R.drawable.diajak_banner_outdoor_1783253550783
      ),
      kreatorName = "SBY Night Runners",
      kreatorVerified = true,
      maxPeserta = 50,
      currentPeserta = 42,
      mapX = 0.80f,
      mapY = 0.25f
    ),
    ActivityModel(
      id = "act_6",
      title = "Tufting Rug & Acrylic Painting Weekend",
      category = "Seni",
      locationName = "Bandung, INA",
      address = "Jl. Dago No. 88, Coblong, Bandung",
      schedule = "27 Sep 2026 • 10:00 - 14:00",
      rating = 4.9,
      reviewsCount = 98,
      priceFormatted = "Rp 245.000",
      priceValue = 245000,
      imageResId = R.drawable.diajak_banner_workshop_1783253562818,
      overview = "Ekspresikan kreativitasmu dalam workshop membuat rug custom menggunakan tufting gun modern dan melukis kanvas akrilik. Suasana studio yang santai dengan alunan musik lofi bikin weekend-mu makin berkesan.",
      detailsList = listOf(
        "Benang katun warna-warni & tufting frame 40x40cm",
        "Bimbingan mentor seni dari awal sampai selesai",
        "Hasil tufting bisa langsung dibawa pulang",
        "Free snack sore & artisan tea"
      ),
      galleryImages = listOf(
        R.drawable.diajak_banner_workshop_1783253562818,
        R.drawable.diajak_activity_coffee_1783253596378
      ),
      kreatorName = "Bandung Craft & Art Hub",
      kreatorVerified = true,
      maxPeserta = 16,
      currentPeserta = 12,
      mapX = 0.40f,
      mapY = 0.35f
    ),
    ActivityModel(
      id = "act_7",
      title = "Street Food Crawl & Coffee Tasting Suryakencana",
      category = "Kuliner",
      locationName = "Bogor, INA",
      address = "Kawasan Kuliner Jl. Suryakencana, Bogor Tengah",
      schedule = "19 Sep 2026 • 15:30 - 19:30",
      rating = 4.8,
      reviewsCount = 175,
      priceFormatted = "Rp 120.000",
      priceValue = 120000,
      imageResId = R.drawable.diajak_activity_coffee_1783253596378,
      overview = "Jelajah kuliner legendaris Bogor di sepanjang jalan Suryakencana bersama pemandu wisata kuliner lokal! Mulai dari soto kuning khas, lumpia basah, martabak arang, hingga sesi cupping kopi di kedai antik tertua di Bogor.",
      detailsList = listOf(
        "Voucher tasting 5 spot kuliner legendaris",
        "Sesi sharing sejarah kuliner akulturasi budaya",
        "Free iced coffee di kedai kopi heritage",
        "Guide lokal ramah & dokumentasi foto seru"
      ),
      galleryImages = listOf(
        R.drawable.diajak_activity_coffee_1783253596378,
        R.drawable.img_culinary_baking_1783584043430
      ),
      kreatorName = "Bogor Foodie Community",
      kreatorVerified = true,
      maxPeserta = 25,
      currentPeserta = 20,
      mapX = 0.30f,
      mapY = 0.50f
    ),
    ActivityModel(
      id = "act_8",
      title = "Sunset Yoga & Mindfulness at Sanur Beach",
      category = "Yoga",
      locationName = "Bali, INA",
      address = "Pantai Karang Sanur, Denpasar Selatan, Bali",
      schedule = "20 Sep 2026 • 17:00 - 18:30",
      rating = 4.9,
      reviewsCount = 128,
      priceFormatted = "Rp 100.000",
      priceValue = 100000,
      imageResId = R.drawable.img_sunset_yoga_1783584057064,
      overview = "Sesi yoga santai menyambut matahari terbenam di pinggir pantai Sanur. Cocok untuk semua tingkat kebugaran, dipandu oleh instruktur yoga bersertifikat untuk relaksasi tubuh dan pikiran.",
      detailsList = listOf(
        "Instruktur Yoga bersertifikat internasional",
        "Peminjaman yoga mat bersih & strap",
        "Sesi meditasi suara (sound healing mini)",
        "Free air kelapa muda organik setelah sesi"
      ),
      galleryImages = listOf(
        R.drawable.img_sunset_yoga_1783584057064,
        R.drawable.diajak_banner_outdoor_1783253550783
      ),
      kreatorName = "Sanur Wellness Collective",
      kreatorVerified = true,
      maxPeserta = 20,
      currentPeserta = 15,
      mapX = 0.55f,
      mapY = 0.65f
    ),
    ActivityModel(
      id = "act_9",
      title = "Padel Tennis Fun Match & Social Meetup",
      category = "Olahraga",
      locationName = "Jakarta Selatan, INA",
      address = "Kebayoran Padel Club, Jl. Senopati Dalam",
      schedule = "26 Sep 2026 • 08:00 - 11:00",
      rating = 4.8,
      reviewsCount = 94,
      priceFormatted = "Rp 175.000",
      priceValue = 175000,
      imageResId = R.drawable.img_padel_tennis_1783584027406,
      overview = "Main padel santai bareng komunitas! Format double fun match dengan sistem rotasi pasangan agar semua peserta bisa saling kenal dan main seru.",
      detailsList = listOf(
        "Sewa lapangan 3 jam full & bola padel baru",
        "Peminjaman raket padel standar turnamen",
        "Minuman isotonik & buah segar",
        "Dokumentasi foto dan video reel"
      ),
      galleryImages = listOf(
        R.drawable.img_padel_tennis_1783584027406
      ),
      kreatorName = "JKT Padel Enthusiasts",
      kreatorVerified = true,
      maxPeserta = 16,
      currentPeserta = 12,
      mapX = 0.22f,
      mapY = 0.42f
    ),
    ActivityModel(
      id = "act_10",
      title = "Manual Brew & Coffee Cupping Masterclass",
      category = "Kopi",
      locationName = "Bandung, INA",
      address = "Roastery Dago Atas No. 45, Coblong, Bandung",
      schedule = "19 Sep 2026 • 13:00 - 16:00",
      rating = 4.9,
      reviewsCount = 156,
      priceFormatted = "Rp 185.000",
      priceValue = 185000,
      imageResId = R.drawable.diajak_activity_coffee_1783253596378,
      overview = "Pelajari rahasia menyeduh kopi V60, Aeropress, dan Kalita Wave dengan sempurna. Sesi cupping mengeksplorasi aroma dan profil rasa kopi spesialti dari berbagai dataran tinggi Nusantara.",
      detailsList = listOf(
        "Kit manual brew selama workshop",
        "Cupping 6 varian single origin nusantara",
        "Biji kopi roasting segar 250gr untuk dibawa pulang",
        "Sertifikat apresiasi dari Q-Grader"
      ),
      galleryImages = listOf(
        R.drawable.diajak_activity_coffee_1783253596378
      ),
      kreatorName = "Bandung Coffee Academy",
      kreatorVerified = true,
      maxPeserta = 12,
      currentPeserta = 9,
      mapX = 0.38f,
      mapY = 0.32f
    ),
    ActivityModel(
      id = "act_11",
      title = "Cafe Hopping & Street Photography Walk",
      category = "Kopi",
      locationName = "Yogyakarta, INA",
      address = "Kawasan Kotabaru & Malioboro, Yogyakarta",
      schedule = "27 Sep 2026 • 15:00 - 19:00",
      rating = 4.8,
      reviewsCount = 112,
      priceFormatted = "Rp 135.000",
      priceValue = 135000,
      imageResId = R.drawable.diajak_activity_coffee_1783253596378,
      overview = "Jelajah 3 kafe bernuansa heritage di Kotabaru sambil hunting foto arsitektur dan human interest. Ditemani fotografer lokal yang siap kasih tips komposisi dan color grading.",
      detailsList = listOf(
        "Voucher minuman signature di 3 kafe hits",
        "Mentoring fotografi smartphone & kamera",
        "Sesi kurasi dan editing foto bareng",
        "Stiker komunitas & goodie bag eksklusif"
      ),
      galleryImages = listOf(
        R.drawable.diajak_activity_coffee_1783253596378
      ),
      kreatorName = "Jogja Visual Club",
      kreatorVerified = true,
      maxPeserta = 18,
      currentPeserta = 14,
      mapX = 0.45f,
      mapY = 0.52f
    ),
    ActivityModel(
      id = "act_12",
      title = "Artisan Bakery & Pastry Hopping Tour",
      category = "Kuliner",
      locationName = "Jakarta Pusat, INA",
      address = "Menteng Artisan Bakery Trail, Menteng",
      schedule = "12 Sep 2026 • 09:00 - 13:00",
      rating = 4.9,
      reviewsCount = 188,
      priceFormatted = "Rp 160.000",
      priceValue = 160000,
      imageResId = R.drawable.img_culinary_baking_1783584043430,
      overview = "Tur kuliner santai mengunjungi 4 hidden gem artisan bakery terbaik di kawasan Menteng. Nikmati croissant renyah butter Prancis, sourdough otentik, dan teh artisan bergaya klasik.",
      detailsList = listOf(
        "Tasting platter pastry & croissant di tiap perhentian",
        "Bertemu langsung dengan Executive Pastry Chef",
        "Goodie bag roti sourdough artisan",
        "Panduan kuliner & dokumentasi profesional"
      ),
      galleryImages = listOf(
        R.drawable.img_culinary_baking_1783584043430
      ),
      kreatorName = "JKT Pastry Lovers",
      kreatorVerified = true,
      maxPeserta = 15,
      currentPeserta = 11,
      mapX = 0.28f,
      mapY = 0.44f
    ),
    ActivityModel(
      id = "act_13",
      title = "Traditional Balinese Cooking Experience",
      category = "Kuliner",
      locationName = "Bali, INA",
      address = "Ubud Organic Farm & Kitchen, Gianyar, Bali",
      schedule = "23 Sep 2026 • 08:30 - 13:00",
      rating = 4.9,
      reviewsCount = 240,
      priceFormatted = "Rp 220.000",
      priceValue = 220000,
      imageResId = R.drawable.diajak_banner_workshop_1783253562818,
      overview = "Belajar memasak hidangan otentik Bali mulai dari memetik bumbu segar di kebun organik, membuat bumbu genep, hingga menyajikan ayam betutu dan sate lilit bersama chef lokal.",
      detailsList = listOf(
        "Tur kebun organik & pengenalan rempah lokal",
        "Praktek memasak 5 hidangan tradisional Bali",
        "Makan siang bersama hasil masakan sendiri",
        "Buku resep digital & sertifikat memasak"
      ),
      galleryImages = listOf(
        R.drawable.diajak_banner_workshop_1783253562818
      ),
      kreatorName = "Ubud Culinary Heritage",
      kreatorVerified = true,
      maxPeserta = 14,
      currentPeserta = 10,
      mapX = 0.62f,
      mapY = 0.58f
    ),
    ActivityModel(
      id = "act_14",
      title = "Hyrox Simulation & Functional Fitness Camp",
      category = "Kebugaran",
      locationName = "Tangerang, INA",
      address = "The Fit Hub BSD, Foresta Business Loft, Tangerang",
      schedule = "29 Sep 2026 • 07:00 - 09:30",
      rating = 4.8,
      reviewsCount = 42,
      priceFormatted = "Rp 95.000",
      priceValue = 95000,
      imageResId = R.drawable.img_sport_running_1783584004743,
      overview = "Siapkan fisikmu untuk tantangan Hyrox! Sesi training camp ini menyimulasikan perlombaan fitness race global yang terkenal. Dipandu oleh functional training Coach profesional, kita akan melatih kekuatan, ketahanan kardio, dan mental melalui kombinasi lari serta station functional exercises seperti Sled Push, Burpees, Wall Balls, dan Rowers.",
      detailsList = listOf(
        "Sesi latihan intensif sirkuit Hyrox selama 2.5 jam",
        "Bimbingan form & teknik gerakan oleh certified coach",
        "Minuman elektrolit & air mineral tak terbatas",
        "Sesi cooling down & panduan nutrisi olahraga"
      ),
      galleryImages = listOf(
        R.drawable.img_sport_running_1783584004743,
        R.drawable.diajak_banner_outdoor_1783253550783
      ),
      kreatorName = "The Fit Hub BSD",
      kreatorVerified = true,
      maxPeserta = 15,
      currentPeserta = 8,
      mapX = 0.25f,
      mapY = 0.55f
    ),
    ActivityModel(
      id = "act_15",
      title = "Acoustic Night Jamming & Chill",
      category = "Musik",
      locationName = "Bandung, INA",
      address = "Warung Kopi Ambyar, Coblong, Bandung",
      schedule = "25 Sep 2026 • 19:30 - 23:00",
      rating = 4.8,
      reviewsCount = 56,
      priceFormatted = "Rp 40.000",
      priceValue = 40000,
      imageResId = R.drawable.diajak_activity_coffee_1783253596378,
      overview = "Malam syahdu penuh lagu akustik terpopuler! Kita nyanyi bareng, jamming pakai gitar, cajon, atau piano sambil menikmati kopi susu hangat. Santai dan hangat tanpa jarak.",
      detailsList = listOf(
        "Free 1 cup kopi susu hangat / es teh manis",
        "Instrumen musik lengkap disediakan bebas digunakan",
        "Grup komunitas pencinta musik se-Bandung",
        "Dokumentasi video reels kenangan"
      ),
      galleryImages = listOf(
        R.drawable.diajak_activity_coffee_1783253596378
      ),
      kreatorName = "Ambyar Music Bandung",
      kreatorVerified = true,
      maxPeserta = 25,
      currentPeserta = 18,
      mapX = 0.42f,
      mapY = 0.38f
    ),
    ActivityModel(
      id = "act_16",
      title = "E-Sports FIFA Tournament & Board Games",
      category = "Esport",
      locationName = "Jakarta Barat, INA",
      address = "Pixel Gaming Café, Tanjung Duren",
      schedule = "20 Sep 2026 • 13:00 - 18:00",
      rating = 4.9,
      reviewsCount = 37,
      priceFormatted = "Rp 75.000",
      priceValue = 75000,
      imageResId = R.drawable.diajak_banner_workshop_1783253562818,
      overview = "Tunjukkan skill FIFA-mu dalam turnamen Double Elimination yang seru! Di sela-sela pertandingan, ada pojok Board Games seru seperti Catan, Avalon, dan Werewolf untuk yang ingin seru-seruan bareng teman baru.",
      detailsList = listOf(
        "Akses konsol PS5 & TV 4K ultra HD selama aktivitas",
        "Voucher snack & minuman segar dari kafe",
        "Hadiah merchandise eksklusif untuk juara 1-3",
        "Koleksi 20+ board games premium gratis dimainkan"
      ),
      galleryImages = listOf(
        R.drawable.diajak_banner_workshop_1783253562818
      ),
      kreatorName = "Pixel Gaming Club",
      kreatorVerified = true,
      maxPeserta = 24,
      currentPeserta = 16,
      mapX = 0.20f,
      mapY = 0.48f
    )
  )

  private lateinit var database: AppDatabase
  private lateinit var appContext: Context
  private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

  private val _activitiesFlow = MutableStateFlow<List<ActivityModel>>(initialActivities)
  val activitiesFlow: StateFlow<List<ActivityModel>> = _activitiesFlow.asStateFlow()
  val acaraFlow: StateFlow<List<ActivityModel>> get() = activitiesFlow
  val activities: List<ActivityModel> get() = _activitiesFlow.value

  private val _bookingsFlow = MutableStateFlow<List<BookingModel>>(emptyList())
  val bookingsFlow: StateFlow<List<BookingModel>> = _bookingsFlow.asStateFlow()

  fun resetDatabase() {
    repositoryScope.launch {
      database.clearAllTables()
      val activityDao = database.activityDao()
      val bookingDao = database.bookingDao()

      val entities = initialActivities.map { ActivityEntity.fromModel(it, appContext) }
      activityDao.insertAll(entities)

      val defaultBooking = BookingModel(
        id = "DJK-8829104A",
        activityId = "act_1",
        activityTitle = "Barista Experience & Latte Art 101",
        locationName = "Jl. Pantai Kuta No. 32, Legian, Kuta, Badung",
        schedule = "12 Sep 2026 • 09:00",
        undanganCount = 2,
        totalPriceFormatted = "Rp 300.000",
        bookingTimestamp = "02 Sep 2026",
        imageResId = R.drawable.diajak_activity_coffee_1783253596378,
        status = "Terkonfirmasi",
        userName = "Anggi Saputro",
        userEmail = "gisaput@gmail.com",
        userPhone = "081234567890"
      )
      bookingDao.insertAll(listOf(BookingEntity.fromModel(defaultBooking, appContext)))

      _activitiesFlow.value = initialActivities
      _bookingsFlow.value = listOf(defaultBooking)
    }
  }

  fun initialize(db: AppDatabase, context: Context) {
    database = db
    appContext = context.applicationContext

    // Perform database reset on request
    resetDatabase()

    repositoryScope.launch {
      val activityDao = database.activityDao()

      // Observe database to dynamically update our flows
      activityDao.getAllActivities().collect { entities ->
        if (entities.isNotEmpty()) {
          _activitiesFlow.value = entities.map { it.toModel(appContext) }
        }
      }
    }

    repositoryScope.launch {
      database.bookingDao().getAllBookings().collect { entities ->
        android.util.Log.d("DiajakRepository", "Bookings collection triggered: loaded ${entities.size} items from DB")
        _bookingsFlow.value = entities.map { it.toModel(appContext) }.reversed()
      }
    }
  }

  val sampleReviews = listOf(
    ReviewModel("r1", "Nadia Putri", 5, "Seru banget! Kreator super ramah dan penjelasannya mudah dipahami. Rekomendasi banget buat yang cari teman hobi baru.", "2 hari lalu"),
    ReviewModel("r2", "Bima Arya", 5, "Fasilitas lengkap dan tempatnya estetik. Senang banget bisa gabung lewat aplikasi ini!", "1 minggu lalu"),
    ReviewModel("r3", "Clarissa Devika", 4, "Aktivitas tepat waktu, peserta lain juga asik-asik. Next time bakal ikut aktivitas lainnya lagi.", "2 minggu lalu")
  )

  fun addActivity(activity: ActivityModel) {
    repositoryScope.launch {
      database.activityDao().insert(ActivityEntity.fromModel(activity, appContext))
    }
  }

  fun addAcara(activity: ActivityModel) = addActivity(activity)

  fun addBooking(booking: BookingModel) {
    android.util.Log.d("DiajakRepository", "addBooking: adding booking with ID ${booking.id}")
    repositoryScope.launch {
      try {
        database.bookingDao().insert(BookingEntity.fromModel(booking, appContext))
        android.util.Log.d("DiajakRepository", "addBooking: database insert success for ID ${booking.id}")
      } catch (e: Exception) {
        android.util.Log.e("DiajakRepository", "addBooking: database insert failed", e)
      }
      val activityDao = database.activityDao()
      val existingActivities = _activitiesFlow.value
      val targetActivity = existingActivities.find { it.id == booking.activityId }
      if (targetActivity != null) {
        val newCount = (targetActivity.currentPeserta + booking.undanganCount).coerceAtMost(targetActivity.maxPeserta)
        activityDao.updatePeserta(booking.activityId, newCount)
      }
    }
  }

  suspend fun getUserProfile(email: String): com.example.data.database.UserProfileEntity? {
    return database.userProfileDao().getUserProfile(email)
  }

  suspend fun saveUserProfile(profile: com.example.data.database.UserProfileEntity) {
    database.userProfileDao().insertUserProfile(profile)
  }

  suspend fun getFavoriteActivityIds(email: String): List<String> {
    return database.favoriteDao().getFavoriteActivityIds(email)
  }

  suspend fun getFavoriteAcaraIds(email: String): List<String> = getFavoriteActivityIds(email)

  suspend fun addFavorite(email: String, activityId: String) {
    database.favoriteDao().insertFavorite(
      com.example.data.database.FavoriteEntity(
        id = "${email}_${activityId}",
        email = email,
        activityId = activityId
      )
    )
  }

  suspend fun removeFavorite(email: String, activityId: String) {
    database.favoriteDao().deleteFavorite(email, activityId)
  }
}

