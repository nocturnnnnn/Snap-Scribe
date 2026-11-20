package com.genius.snapscribe.data.sample

import com.genius.snapscribe.R
import com.genius.snapscribe.domain.model.DetectedEntity
import com.genius.snapscribe.domain.model.EntityType
import com.genius.snapscribe.domain.model.ImageSource
import com.genius.snapscribe.domain.model.NormalizedRect
import com.genius.snapscribe.domain.model.OcrBlock
import com.genius.snapscribe.domain.model.Snap
import com.genius.snapscribe.domain.model.SnapCategory
import com.genius.snapscribe.domain.model.SnapSource
import java.time.Instant
import java.time.temporal.ChronoUnit

object SampleSnaps {

    fun buildDefaultSnaps(now: Instant = Instant.now()): List<Snap> = listOf(
        // ==================== RECEIPTS (7) ====================
        Snap(
            id = "snap-01",
            image = ImageSource.Drawable(R.drawable.sample_bca_transfer),
            category = SnapCategory.RECEIPTS,
            title = "Transfer BCA – Rp 250.000",
            summary = "Ref #TRX-20241024-99482 • Toko Buku Berkah",
            merchantOrTag = "BCA",
            capturedAt = now.minus(2, ChronoUnit.HOURS),
            source = SnapSource.SCREENSHOTS,
            amountRupiah = 250_000L,
            fullText = listOf(
                "Status: m-Transfer Berhasil",
                "Waktu: Hari ini, 11:30 WIB",
                "Jumlah: Rp 250.000",
                "Tujuan: BCA 8830192841 - Toko Buku Berkah",
                "No. Referensi: TRX-20241024-99482",
            ),
            entities = listOf(
                DetectedEntity(EntityType.AMOUNT, "Rp 250.000", "Jumlah Transfer"),
                DetectedEntity(EntityType.ACCOUNT_NUMBER, "8830192841", "BCA Toko Buku Berkah"),
            ),
            blocks = listOf(
                OcrBlock("m-Transfer Berhasil", NormalizedRect(0.22f, 0.16f, 0.78f, 0.25f)),
                OcrBlock("Rp 250.000", NormalizedRect(0.24f, 0.36f, 0.72f, 0.47f)),
                OcrBlock("Toko Buku Berkah", NormalizedRect(0.22f, 0.52f, 0.76f, 0.62f)),
                OcrBlock("TRX-20241024-99482", NormalizedRect(0.22f, 0.70f, 0.78f, 0.78f)),
            ),
        ),
        Snap(
            id = "snap-02",
            image = ImageSource.Drawable(R.drawable.sample_superindo_receipt),
            category = SnapCategory.RECEIPTS,
            title = "Superindo Sudirman – Rp 284.500",
            summary = "Minyak Goreng Sania 2L, Telur Ayam Omega 10s, Beras Ramos 5kg",
            merchantOrTag = "Superindo",
            capturedAt = now.minus(1, ChronoUnit.DAYS).minus(3, ChronoUnit.HOURS),
            source = SnapSource.CAMERA,
            amountRupiah = 284_500L,
            fullText = listOf(
                "SUPERINDO SUDIRMAN JAKARTA",
                "Kasir #04 • Tunai & QRIS",
                "Minyak Goreng Sania 2L ........ 38.900",
                "Telur Ayam Omega 10s .......... 34.500",
                "Beras Ramos Setra 5kg ......... 74.500",
                "Susu UHT Full Cream 2x ........ 42.600",
                "Total Belanja: Rp 284.500",
            ),
            entities = listOf(
                DetectedEntity(EntityType.AMOUNT, "Rp 284.500", "Total Belanja"),
            ),
            blocks = listOf(
                OcrBlock("SUPERINDO SUDIRMAN", NormalizedRect(0.25f, 0.14f, 0.75f, 0.23f)),
                OcrBlock("Minyak Goreng Sania 2L", NormalizedRect(0.22f, 0.32f, 0.78f, 0.43f)),
                OcrBlock("Total Rp 284.500", NormalizedRect(0.24f, 0.68f, 0.76f, 0.79f)),
            ),
        ),
        Snap(
            id = "snap-03",
            image = ImageSource.Drawable(R.drawable.sample_pln_token),
            category = SnapCategory.RECEIPTS,
            title = "Token Listrik PLN – Rp 202.500",
            summary = "Stroom: 3829-1092-4829-7712-0041 • Meter 14028391029",
            merchantOrTag = "Listrik PLN",
            capturedAt = now.minus(1, ChronoUnit.DAYS).minus(7, ChronoUnit.HOURS),
            source = SnapSource.SCREENSHOTS,
            amountRupiah = 202_500L,
            fullText = listOf(
                "STRUK PEMBELIAN TOKEN LISTRIK PRABAYAR",
                "IDPEL / No Meter: 14028391029",
                "Nominal: Rp 200.000 + Admin Rp 2.500",
                "Total Bayar: Rp 202.500",
                "Stroom/Token: 3829-1092-4829-7712-0041",
            ),
            entities = listOf(
                DetectedEntity(EntityType.AMOUNT, "Rp 202.500", "Total Tagihan"),
                DetectedEntity(EntityType.ACCOUNT_NUMBER, "3829-1092-4829-7712-0041", "Token PLN"),
            ),
            blocks = listOf(
                OcrBlock("No Meter: 14028391029", NormalizedRect(0.18f, 0.28f, 0.80f, 0.37f)),
                OcrBlock("3829-1092-4829-7712-0041", NormalizedRect(0.16f, 0.50f, 0.85f, 0.61f)),
                OcrBlock("Rp 202.500", NormalizedRect(0.20f, 0.68f, 0.65f, 0.77f)),
            ),
        ),
        Snap(
            id = "snap-04",
            image = ImageSource.Drawable(R.drawable.sample_cafe_receipt),
            category = SnapCategory.RECEIPTS,
            title = "Roast & Brew Coffee – Rp 68.000",
            summary = "Flat White Double Shot, Butter Croissant",
            merchantOrTag = "Kafe",
            capturedAt = now.minus(4, ChronoUnit.HOURS),
            source = SnapSource.CAMERA,
            amountRupiah = 68_000L,
            fullText = listOf(
                "ROAST & BREW COFFEE",
                "Table 09 • Debit BCA",
                "1x Flat White Double Shot .... 38.000",
                "1x Butter Croissant .......... 30.000",
                "Total: Rp 68.000 (Lunas)",
            ),
            entities = listOf(
                DetectedEntity(EntityType.AMOUNT, "Rp 68.000", "Total Kafe"),
            ),
            blocks = listOf(
                OcrBlock("ROAST & BREW COFFEE", NormalizedRect(0.26f, 0.20f, 0.74f, 0.30f)),
                OcrBlock("Rp 68.000", NormalizedRect(0.30f, 0.62f, 0.70f, 0.73f)),
            ),
        ),
        Snap(
            id = "snap-05",
            image = ImageSource.Drawable(R.drawable.sample_medicine),
            category = SnapCategory.RECEIPTS,
            title = "Apotek K-24 Tebet – Rp 145.000",
            summary = "Paracetamol 500mg, Vitamin C 1000mg • QRIS",
            merchantOrTag = "Kesehatan",
            capturedAt = now.minus(2, ChronoUnit.DAYS),
            source = SnapSource.WHATSAPP,
            amountRupiah = 145_000L,
            fullText = listOf(
                "APOTEK K-24 TEBET BARAT",
                "Metode: ShopeePay / QRIS",
                "Paracetamol 500mg Strip (2x) .. 35.000",
                "Vitamin C 1000mg Box ......... 110.000",
                "Total: Rp 145.000",
            ),
            entities = listOf(
                DetectedEntity(EntityType.AMOUNT, "Rp 145.000", "Struk Apotek"),
            ),
            blocks = listOf(
                OcrBlock("APOTEK K-24 TEBET", NormalizedRect(0.20f, 0.18f, 0.72f, 0.28f)),
                OcrBlock("Rp 145.000", NormalizedRect(0.24f, 0.56f, 0.68f, 0.68f)),
            ),
        ),
        Snap(
            id = "snap-06",
            image = ImageSource.Drawable(R.drawable.sample_fuel_receipt),
            category = SnapCategory.RECEIPTS,
            title = "SPBU Shell MT Haryono – Rp 350.000",
            summary = "Shell V-Power 24.1L @ Rp 14.520/L",
            merchantOrTag = "Transportasi",
            capturedAt = now.minus(3, ChronoUnit.DAYS),
            source = SnapSource.CAMERA,
            amountRupiah = 350_000L,
            fullText = listOf(
                "SHELL MT HARYONO JAKARTA",
                "Pompa 03 • Kartu Kredit Mandiri",
                "Produk: Shell V-Power 24.10 L",
                "Harga/L: Rp 14.520",
                "Total Bayar: Rp 350.000",
            ),
            entities = listOf(
                DetectedEntity(EntityType.AMOUNT, "Rp 350.000", "BBM Shell"),
            ),
            blocks = listOf(
                OcrBlock("SHELL MT HARYONO", NormalizedRect(0.24f, 0.16f, 0.76f, 0.26f)),
                OcrBlock("Rp 350.000", NormalizedRect(0.28f, 0.64f, 0.72f, 0.75f)),
            ),
        ),
        Snap(
            id = "snap-07",
            image = ImageSource.Drawable(R.drawable.sample_grocery_receipt),
            category = SnapCategory.RECEIPTS,
            title = "Superindo Kemang – Rp 148.500",
            summary = "12 item belanja dapur mingguan",
            merchantOrTag = "Superindo",
            capturedAt = now.minus(5, ChronoUnit.DAYS),
            source = SnapSource.CAMERA,
            amountRupiah = 148_500L,
            fullText = listOf(
                "SUPERINDO KEMANG",
                "12 Item Belanja Dapur",
                "Sayur Bayam, Bawang Merah, Tahu Putih",
                "Total: Rp 148.500",
            ),
            entities = listOf(
                DetectedEntity(EntityType.AMOUNT, "Rp 148.500", "Belanja Mingguan"),
            ),
            blocks = listOf(
                OcrBlock("SUPERINDO KEMANG", NormalizedRect(0.22f, 0.20f, 0.76f, 0.30f)),
                OcrBlock("Rp 148.500", NormalizedRect(0.26f, 0.66f, 0.70f, 0.76f)),
            ),
        ),

        // ==================== CODES & LINKS (5) ====================
        Snap(
            id = "snap-08",
            image = ImageSource.Drawable(R.drawable.sample_wifi_chalkboard),
            category = SnapCategory.CODES_LINKS,
            title = "SSID: Roast & Brew Guest",
            summary = "Sandi: morningbeans99 • Wi-Fi Kafe",
            merchantOrTag = "Wi-Fi",
            capturedAt = now.minus(1, ChronoUnit.DAYS),
            source = SnapSource.CAMERA,
            fullText = listOf(
                "CoffeeCo / Roast & Brew Guest Wi-Fi",
                "SSID: RoastBrew_5G",
                "Password: morningbeans99",
                "Catatan: Kecepatan 100 Mbps",
            ),
            entities = listOf(
                DetectedEntity(EntityType.PASSWORD, "morningbeans99", "Sandi Wi-Fi"),
            ),
            blocks = listOf(
                OcrBlock("Roast & Brew Guest Wi-Fi", NormalizedRect(0.38f, 0.24f, 0.84f, 0.42f)),
                OcrBlock("morningbeans99", NormalizedRect(0.40f, 0.48f, 0.82f, 0.62f)),
            ),
        ),
        Snap(
            id = "snap-09",
            image = ImageSource.Drawable(R.drawable.sample_parcel_label),
            category = SnapCategory.CODES_LINKS,
            title = "Resi JNE Express – JNE8829103984",
            summary = "Paket komponen laptop • Sedang dikirim ke Jakarta",
            merchantOrTag = "JNE Express",
            capturedAt = now.minus(2, ChronoUnit.DAYS),
            source = SnapSource.WHATSAPP,
            fullText = listOf(
                "JNE EXPRESS REGULER",
                "No. Resi: JNE8829103984",
                "Penerima: Budi Santoso (Kebayoran Baru)",
                "Status: Sedang di Sorting Center Jakarta",
            ),
            entities = listOf(
                DetectedEntity(EntityType.TRACKING_NUMBER, "JNE8829103984", "Resi JNE"),
            ),
            blocks = listOf(
                OcrBlock("JNE8829103984", NormalizedRect(0.24f, 0.34f, 0.76f, 0.50f)),
                OcrBlock("Kebayoran Baru", NormalizedRect(0.24f, 0.55f, 0.70f, 0.66f)),
            ),
        ),
        Snap(
            id = "snap-10",
            image = ImageSource.Drawable(R.drawable.sample_parcel_label),
            category = SnapCategory.CODES_LINKS,
            title = "ShopeeXpress – SPXID0482910482",
            summary = "Kurir J&T / SPX • Tiba di Sorting Center Cakung",
            merchantOrTag = "ShopeeXpress",
            capturedAt = now.minus(4, ChronoUnit.DAYS),
            source = SnapSource.SCREENSHOTS,
            fullText = listOf(
                "SHOPEEXPress - Resi Pengiriman",
                "Nomor Pelacakan: SPXID0482910482",
                "Kurir: SPX Standard",
                "Status: Paket telah diterima di Sorting Center Cakung",
            ),
            entities = listOf(
                DetectedEntity(EntityType.TRACKING_NUMBER, "SPXID0482910482", "Resi Shopee"),
            ),
            blocks = listOf(
                OcrBlock("SPXID0482910482", NormalizedRect(0.22f, 0.36f, 0.78f, 0.52f)),
            ),
        ),
        Snap(
            id = "snap-11",
            image = ImageSource.Drawable(R.drawable.sample_wifi_chalkboard),
            category = SnapCategory.CODES_LINKS,
            title = "Wi-Fi Rumah Lantai 2",
            summary = "SSID: RumahSantoso_5G • kopi_hangat_2024",
            merchantOrTag = "Home-Fiber",
            capturedAt = now.minus(6, ChronoUnit.DAYS),
            source = SnapSource.SCREENSHOTS,
            fullText = listOf(
                "Router Biznet Home Lantai 2",
                "SSID: RumahSantoso_5G",
                "Password: kopi_hangat_2024",
                "PIN Admin Router: 448120",
            ),
            entities = listOf(
                DetectedEntity(EntityType.PASSWORD, "kopi_hangat_2024", "Sandi Wi-Fi Rumah"),
                DetectedEntity(EntityType.OTP, "448120", "PIN Admin"),
            ),
            blocks = listOf(
                OcrBlock("RumahSantoso_5G", NormalizedRect(0.26f, 0.30f, 0.74f, 0.42f)),
                OcrBlock("kopi_hangat_2024", NormalizedRect(0.26f, 0.46f, 0.76f, 0.58f)),
            ),
        ),
        Snap(
            id = "snap-12",
            image = ImageSource.Drawable(R.drawable.sample_corkboard_note),
            category = SnapCategory.CODES_LINKS,
            title = "Tautan Portal Kuliah & Slide",
            summary = "portal.akademik.ac.id/ppb-sem5 • Kode kelas PPB-5A",
            merchantOrTag = "Portal Kampus",
            capturedAt = now.minus(7, ChronoUnit.DAYS),
            source = SnapSource.SCREENSHOTS,
            fullText = listOf(
                "Akses Materi Pemrograman Perangkat Bergerak",
                "Link: https://portal.akademik.ac.id/ppb-sem5",
                "Kode Gabung Kelas: PPB-2026-5A",
            ),
            entities = listOf(
                DetectedEntity(EntityType.URL, "https://portal.akademik.ac.id/ppb-sem5", "Portal Materi"),
            ),
            blocks = listOf(
                OcrBlock("https://portal.akademik.ac.id/ppb-sem5", NormalizedRect(0.18f, 0.40f, 0.84f, 0.54f)),
            ),
        ),

        // ==================== ADDRESSES (4) ====================
        Snap(
            id = "snap-13",
            image = ImageSource.Drawable(R.drawable.sample_map_phone),
            category = SnapCategory.ADDRESSES,
            title = "Jl. Senopati No. 42, Kebayoran Baru",
            summary = "Jakarta Selatan 12190 • Lokasi antar paket",
            merchantOrTag = "Jakarta Selatan",
            capturedAt = now.minus(6, ChronoUnit.HOURS),
            source = SnapSource.WHATSAPP,
            fullText = listOf(
                "Alamat Pengantaran",
                "Jl. Senopati No. 42, Kebayoran Baru",
                "Jakarta Selatan, DKI Jakarta 12190",
                "Patokan: Sebelah toko roti warna krem",
            ),
            entities = listOf(
                DetectedEntity(EntityType.ADDRESS, "Jl. Senopati No. 42, Kebayoran Baru, Jakarta Selatan", "Alamat"),
            ),
            blocks = listOf(
                OcrBlock("Jl. Senopati No. 42", NormalizedRect(0.24f, 0.34f, 0.74f, 0.46f)),
                OcrBlock("Kebayoran Baru, Jakarta Selatan", NormalizedRect(0.22f, 0.48f, 0.78f, 0.60f)),
            ),
        ),
        Snap(
            id = "snap-14",
            image = ImageSource.Drawable(R.drawable.sample_map_phone),
            category = SnapCategory.ADDRESSES,
            title = "Klinik Sehat Bintaro Sektor 7",
            summary = "Jl. Boulevard Bintaro Jaya Blok B7/12",
            merchantOrTag = "Klinik",
            capturedAt = now.minus(3, ChronoUnit.DAYS),
            source = SnapSource.SCREENSHOTS,
            fullText = listOf(
                "Klinik Sehat Bintaro (dr. Hendra)",
                "Jl. Boulevard Bintaro Jaya Blok B7/12",
                "Tangerang Selatan 15224",
                "Telp: 021-74862910 • Buka 08.00–20.00",
            ),
            entities = listOf(
                DetectedEntity(EntityType.ADDRESS, "Jl. Boulevard Bintaro Jaya Blok B7/12, Tangerang Selatan", "Klinik"),
                DetectedEntity(EntityType.PHONE, "021-74862910", "Telepon Klinik"),
            ),
            blocks = listOf(
                OcrBlock("Klinik Sehat Bintaro", NormalizedRect(0.22f, 0.28f, 0.78f, 0.40f)),
                OcrBlock("Jl. Boulevard Bintaro Jaya Blok B7/12", NormalizedRect(0.20f, 0.45f, 0.80f, 0.58f)),
            ),
        ),
        Snap(
            id = "snap-15",
            image = ImageSource.Drawable(R.drawable.sample_map_phone),
            category = SnapCategory.ADDRESSES,
            title = "Bengkel Resmi Motor Pondok Indah",
            summary = "Jl. Metro Pondok Indah Kav. IV • Servis rutin",
            merchantOrTag = "Bengkel",
            capturedAt = now.minus(8, ChronoUnit.DAYS),
            source = SnapSource.WHATSAPP,
            fullText = listOf(
                "Bengkel Ahass Pondok Indah",
                "Jl. Metro Pondok Indah Kav. IV No. 18",
                "Jakarta Selatan 12310",
            ),
            entities = listOf(
                DetectedEntity(EntityType.ADDRESS, "Jl. Metro Pondok Indah Kav. IV No. 18, Jakarta Selatan", "Bengkel"),
            ),
            blocks = listOf(
                OcrBlock("Jl. Metro Pondok Indah Kav. IV", NormalizedRect(0.22f, 0.36f, 0.78f, 0.50f)),
            ),
        ),
        Snap(
            id = "snap-16",
            image = ImageSource.Drawable(R.drawable.sample_map_phone),
            category = SnapCategory.ADDRESSES,
            title = "Rumah Tante Rina – Bandung",
            summary = "Jl. Sukajadi No. 114, Kec. Sukajadi, Bandung",
            merchantOrTag = "Keluarga",
            capturedAt = now.minus(11, ChronoUnit.DAYS),
            source = SnapSource.WHATSAPP,
            fullText = listOf(
                "Alamat Rumah Tante Rina",
                "Jl. Sukajadi No. 114, Bandung 40162",
                "Pagar kayu cokelat depan taman",
            ),
            entities = listOf(
                DetectedEntity(EntityType.ADDRESS, "Jl. Sukajadi No. 114, Bandung 40162", "Rumah Tante Rina"),
            ),
            blocks = listOf(
                OcrBlock("Jl. Sukajadi No. 114, Bandung", NormalizedRect(0.20f, 0.38f, 0.80f, 0.52f)),
            ),
        ),

        // ==================== NOTES (4) ====================
        Snap(
            id = "snap-17",
            image = ImageSource.Drawable(R.drawable.sample_corkboard_note),
            category = SnapCategory.NOTES,
            title = "Rapat Komite Sekolah: Ruang 302",
            summary = "Kamis, 16:00 WIB • Bahas agenda kunjungan studi",
            merchantOrTag = "Jadwal",
            capturedAt = now.minus(5, ChronoUnit.HOURS),
            source = SnapSource.CAMERA,
            fullText = listOf(
                "Catatan Rapat Komite Sekolah",
                "Waktu: Kamis, 16:00 WIB",
                "Tempat: Ruang 302 Lantai 3",
                "Agenda: Jadwal ujian & konsumsi orang tua",
            ),
            entities = listOf(
                DetectedEntity(EntityType.DATE, "Kamis, 16:00 WIB", "Jadwal Rapat"),
            ),
            blocks = listOf(
                OcrBlock("Rapat Komite Sekolah", NormalizedRect(0.22f, 0.24f, 0.78f, 0.36f)),
                OcrBlock("Kamis, 16:00 WIB • Ruang 302", NormalizedRect(0.22f, 0.42f, 0.78f, 0.56f)),
            ),
        ),
        Snap(
            id = "snap-18",
            image = ImageSource.Drawable(R.drawable.sample_class_schedule),
            category = SnapCategory.NOTES,
            title = "Jadwal Kuliah Semester 5",
            summary = "Senin 08:00 PPB • Rabu 10:00 Keamanan Jaringan",
            merchantOrTag = "Kuliah",
            capturedAt = now.minus(2, ChronoUnit.DAYS),
            source = SnapSource.SCREENSHOTS,
            fullText = listOf(
                "JADWAL KULIAH SEMESTER 5",
                "Senin 08:00 - Pemrograman Perangkat Bergerak (Lab B)",
                "Selasa 13:00 - Rekayasa Perangkat Lunak (R.401)",
                "Rabu 10:00 - Keamanan Jaringan (R.305)",
                "Kamis 09:00 - Sistem Terdistribusi (R.204)",
            ),
            entities = listOf(
                DetectedEntity(EntityType.DATE, "Senin 08:00 • PPB Lab B", "Jadwal Kuliah"),
            ),
            blocks = listOf(
                OcrBlock("JADWAL KULIAH SEMESTER 5", NormalizedRect(0.20f, 0.16f, 0.80f, 0.26f)),
                OcrBlock("Senin 08:00 - Pemrograman Perangkat Bergerak", NormalizedRect(0.18f, 0.34f, 0.84f, 0.48f)),
            ),
        ),
        Snap(
            id = "snap-19",
            image = ImageSource.Drawable(R.drawable.sample_medicine),
            category = SnapCategory.NOTES,
            title = "Dosis Obat Ayah & Ibu",
            summary = "Amlodipine 5mg 1x pagi sesudah makan",
            merchantOrTag = "Resep",
            capturedAt = now.minus(4, ChronoUnit.DAYS),
            source = SnapSource.WHATSAPP,
            fullText = listOf(
                "CATATAN MINUM OBAT",
                "Amlodipine 5mg: 1 tablet tiap pagi sesudah makan",
                "Vitamin D3 1000 IU: 1 kapsul siang hari",
                "Kontrol ulang: 28 November",
            ),
            entities = listOf(
                DetectedEntity(EntityType.DATE, "Kontrol ulang: 28 November", "Jadwal Dokter"),
            ),
            blocks = listOf(
                OcrBlock("Amlodipine 5mg: 1x pagi", NormalizedRect(0.18f, 0.36f, 0.78f, 0.50f)),
            ),
        ),
        Snap(
            id = "snap-20",
            image = ImageSource.Drawable(R.drawable.sample_corkboard_note),
            category = SnapCategory.NOTES,
            title = "Daftar Belanja Persiapan Renovasi",
            summary = "Cat tembok putih 5kg, kuas 3 inch, lem keramik",
            merchantOrTag = "Catatan",
            capturedAt = now.minus(9, ChronoUnit.DAYS),
            source = SnapSource.CAMERA,
            fullText = listOf(
                "DAFTAR BELANJA RENOVASI DAPUR",
                "- Cat tembok matte putih 5kg (2 kaleng)",
                "- Kuas cat 3 inch & roller",
                "- Lem keramik tahan air 2 sak",
            ),
            entities = emptyList(),
            blocks = listOf(
                OcrBlock("DAFTAR BELANJA RENOVASI", NormalizedRect(0.20f, 0.26f, 0.80f, 0.40f)),
            ),
        ),
    )

    fun createScannedSampleSnap(mode: String, now: Instant = Instant.now()): Snap {
        val id = "snap-scan-${now.toEpochMilli()}"
        return when (mode) {
            "WIFI" -> Snap(
                id = id,
                image = ImageSource.Drawable(R.drawable.sample_wifi_chalkboard),
                category = SnapCategory.CODES_LINKS,
                title = "Wi-Fi Baru Terpindai",
                summary = "SSID: Studio_Kopi_Guest • kopi2026",
                merchantOrTag = "Wi-Fi",
                capturedAt = now,
                source = SnapSource.SCANNED,
                fullText = listOf(
                    "SSID: Studio_Kopi_Guest",
                    "Password: kopi2026",
                ),
                entities = listOf(DetectedEntity(EntityType.PASSWORD, "kopi2026", "Sandi Wi-Fi")),
                blocks = listOf(OcrBlock("kopi2026", NormalizedRect(0.30f, 0.42f, 0.72f, 0.56f))),
            )

            else -> Snap(
                id = id,
                image = ImageSource.Drawable(R.drawable.sample_cafe_receipt),
                category = SnapCategory.RECEIPTS,
                title = "Struk Baru Terpindai – Rp 95.000",
                summary = "2x Kopi Susu Gula Aren, 1x Roti Bakar",
                merchantOrTag = "Hasil Scan",
                capturedAt = now,
                source = SnapSource.SCANNED,
                amountRupiah = 95_000L,
                fullText = listOf(
                    "KEDAI KOPI NUSANTARA",
                    "2x Kopi Susu Gula Aren ........ 56.000",
                    "1x Roti Bakar Cokelat ......... 39.000",
                    "Total: Rp 95.000",
                ),
                entities = listOf(DetectedEntity(EntityType.AMOUNT, "Rp 95.000", "Total Struk")),
                blocks = listOf(
                    OcrBlock("KEDAI KOPI NUSANTARA", NormalizedRect(0.24f, 0.20f, 0.76f, 0.30f)),
                    OcrBlock("Rp 95.000", NormalizedRect(0.28f, 0.62f, 0.72f, 0.74f)),
                ),
            )
        }
    }
}
