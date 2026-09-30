package com.kelompoksix.siencehub.data.repositories

import com.kelompoksix.siencehub.R
import com.kelompoksix.siencehub.data.models.BlokKonten.*
import com.kelompoksix.siencehub.data.models.IsiBab
import com.kelompoksix.siencehub.data.models.SoalMini

object IsiBabRepository {

    fun getIsiBab(topikId: String, babId: Int): IsiBab? = data[topikId]?.get(babId)

    // Tambah mapel lain di sini: "kimia_asam_basa" to mapOf(1 to IsiBab(...), ...)
    private val data: Map<String, Map<Int, IsiBab>> = mapOf(
        "fisika_newton" to mapOf(

            1 to IsiBab(
                blok = listOf(
                    TeksGambar("Pernahkah badanmu terdorong ke depan saat bus direm mendadak?" , R.drawable.bus_ilustrasi ),
                    Paragraf(
                        "Hukum I Newton",
                        "Benda diam akan tetap diam, dan benda bergerak lurus beraturan akan tetap bergerak lurus beraturan, selama resultan gaya yang bekerja padanya sama dengan nol (ΣF = 0)."
                    ),
                    Paragraf(
                        "Inersia",
                        "Inersia adalah kecenderungan benda mempertahankan keadaannya. Semakin besar massa benda, semakin besar inersianya."
                    ),
                    Rumus(
                        "Rumus Cepat Hukum Newton 1",
                        "ΣF = 0\nBenda diam atau GLB" , R.drawable.segitiga_fma
                    ),
                    Gambar(R.drawable.balok_di_meja, "Gaya-gaya seimbang pada balok")
                ),
                kuis = listOf(
                    SoalMini(
                        "Penumpang terdorong ke depan saat bus mengerem mendadak karena...",
                        listOf(
                            "Gaya dorong bus",
                            "Inersia penumpang",
                            "Gaya gravitasi",
                            "Gaya gesek udara"
                        ), 1,
                        "Tubuh penumpang cenderung mempertahankan gerak awalnya (inersia)."
                    ),
                    SoalMini(
                        "Benda tetap diam atau GLB jika resultan gayanya...",
                        listOf("Nol", "Positif", "Negatif", "Tak hingga"), 0,
                        "Hukum I Newton: ΣF = 0."
                    ),
                    SoalMini(
                        "Benda dengan inersia terbesar adalah...",
                        listOf("Kelereng 10 g", "Bola tenis", "Batu 5 kg", "Selembar kertas"), 2,
                        "Inersia sebanding dengan massa, jadi yang massanya terbesar."
                    )
                )
            ),

            2 to IsiBab(
                blok = listOf(
                    TeksGambar("Mengapa mendorong troli kosong lebih mudah daripada troli penuh?"),
                    Paragraf(
                        "Hukum II Newton",
                        "Percepatan benda sebanding dengan resultan gaya dan berbanding terbalik dengan massanya."
                    ),
                    Rumus("Rumus Cepat Hukum Newton 2", "F = m × a\na = F / m\nm = F / a"),
                    Paragraf("Contoh", "F = 10 N dan m = 2 kg\na = F / m = 10 / 2 = 5 m/s²")
                ),
                kuis = listOf(
                    SoalMini(
                        "F = 20 N, m = 4 kg. Percepatannya?",
                        listOf("2 m/s²", "5 m/s²", "16 m/s²", "80 m/s²"),
                        1,
                        "a = F/m = 20/4 = 5 m/s²."
                    ),
                    SoalMini(
                        "Massa tetap, gaya dijadikan 2 kali. Percepatannya menjadi...",
                        listOf("Setengah", "Tetap", "2 kali", "4 kali"), 2, "a sebanding dengan F."
                    ),
                    SoalMini(
                        "Percepatan berbanding terbalik dengan...",
                        listOf("Gaya", "Massa", "Kecepatan", "Waktu"),
                        1,
                        "a = F/m, jadi makin besar m, makin kecil a."
                    )
                )
            ),

            3 to IsiBab(
                blok = listOf(
                    TeksGambar("Kenapa perahu bergerak maju saat dayung didorong ke belakang?"),
                    Paragraf(
                        "Hukum III Newton",
                        "Jika benda A memberi gaya (aksi) pada benda B, maka B memberi gaya (reaksi) pada A yang sama besar dan berlawanan arah."
                    ),
                    Rumus("Rumus Cepat Hukum Newton 3", "F aksi = − F reaksi"),
                    Paragraf(
                        "Ciri Pasangan Aksi-Reaksi",
                        "Sama besar, berlawanan arah, bekerja pada dua benda yang berbeda."
                    )
                ),
                kuis = listOf(
                    SoalMini(
                        "Ciri pasangan aksi-reaksi yang benar adalah...",
                        listOf(
                            "Bekerja pada benda yang sama",
                            "Sama besar, berlawanan arah, pada dua benda berbeda",
                            "Searah",
                            "Besarnya berbeda"
                        ), 1,
                        "Aksi dan reaksi bekerja pada benda berbeda sehingga tidak saling meniadakan."
                    ),
                    SoalMini(
                        "Roket naik karena gas buang didorong ke bawah. Ini contoh Hukum...",
                        listOf("I", "II", "III", "Gravitasi"),
                        2,
                        "Gas mendorong ke bawah (aksi), roket terdorong ke atas (reaksi)."
                    ),
                    SoalMini(
                        "Tangan mendorong dinding 50 N. Gaya dinding pada tangan?",
                        listOf("0 N", "25 N", "50 N berlawanan arah", "100 N"),
                        2,
                        "Reaksi sama besar dan berlawanan arah."
                    )
                )
            ),

            4 to IsiBab(
                blok = listOf(
                    Paragraf(
                        "Hukum I: Sabuk Pengaman",
                        "Saat mobil berhenti mendadak, tubuh cenderung terus bergerak maju. Sabuk pengaman menahannya."
                    ),
                    Paragraf(
                        "Hukum II: Mendorong Troli",
                        "Gaya yang sama memberi percepatan lebih besar pada massa yang lebih kecil."
                    ),
                    Paragraf(
                        "Hukum III: Berenang & Roket",
                        "Perenang mendorong air ke belakang, air mendorong perenang ke depan."
                    ),
                    Rumus("Berat & Gaya Normal", "w = m × g\n(g ≈ 10 m/s²)")
                ),
                kuis = listOf(
                    SoalMini(
                        "Sabuk pengaman berkaitan dengan Hukum...",
                        listOf("I", "II", "III", "Kepler"),
                        0,
                        "Mencegah tubuh terus bergerak akibat inersia."
                    ),
                    SoalMini(
                        "Mobil 1000 kg berpercepatan 2 m/s². Gaya mesinnya?",
                        listOf("500 N", "1000 N", "2000 N", "3000 N"), 2, "F = m·a = 1000 × 2."
                    ),
                    SoalMini(
                        "Berat benda 5 kg (g = 10 m/s²)?",
                        listOf("5 N", "15 N", "50 N", "500 N"), 2, "w = m·g = 5 × 10."
                    )
                )
            ),

            5 to IsiBab(
                blok = listOf(
                    Paragraf(
                        "Kuis Evaluasi",
                        "Jawab 5 soal berikut untuk menguji pemahamanmu tentang Hukum Newton."
                    )
                ),
                kuis = listOf(
                    SoalMini(
                        "Bunyi Hukum I Newton berkaitan dengan...",
                        listOf("Percepatan", "Inersia", "Aksi-reaksi", "Energi"),
                        1,
                        "Hukum I disebut juga hukum kelembaman."
                    ),
                    SoalMini(
                        "Rumus Hukum II Newton?",
                        listOf("F = m/a", "F = m × a", "F = a/m", "F = m + a"), 1, "F = m × a."
                    ),
                    SoalMini(
                        "m = 3 kg, F = 12 N. Percepatannya?",
                        listOf("2 m/s²", "3 m/s²", "4 m/s²", "36 m/s²"), 2, "a = 12/3 = 4 m/s²."
                    ),
                    SoalMini(
                        "Perenang maju karena mendorong air ke belakang. Ini Hukum...",
                        listOf("I", "II", "III", "Bernoulli"), 2, "Pasangan aksi-reaksi."
                    ),
                    SoalMini(
                        "Satuan gaya dalam SI?",
                        listOf("Joule", "Watt", "Newton", "Pascal"), 2, "Newton (N) = kg·m/s²."
                    )
                )
            )
        ),

        // ===================== KIMIA =====================
        "kimia_asam_basa" to mapOf(
            1 to IsiBab(
                blok = listOf(
                    TeksGambar("Kenapa jeruk terasa asam, tapi sabun terasa licin?"),
                    Paragraf("Larutan Asam", "Rasa masam, pH < 7, mengubah lakmus biru menjadi merah. Contoh: cuka, jeruk, asam lambung."),
                    Paragraf("Larutan Basa", "Rasa pahit, terasa licin, pH > 7, mengubah lakmus merah menjadi biru. Contoh: sabun, pasta gigi, air kapur."),
                    Rumus("Skala Cepat", "pH < 7 : Asam\npH = 7 : Netral\npH > 7 : Basa")
                ),
                kuis = listOf(
                    SoalMini("Larutan dengan pH 3 bersifat...", listOf("Asam", "Netral", "Basa", "Garam"), 0, "pH < 7 berarti asam."),
                    SoalMini("Lakmus merah berubah biru dalam larutan...", listOf("Asam", "Basa", "Netral", "Gula"), 1, "Basa mengubah lakmus merah menjadi biru."),
                    SoalMini("Contoh zat bersifat basa adalah...", listOf("Cuka", "Jeruk", "Sabun", "Air aki"), 2, "Sabun bersifat basa dan terasa licin.")
                )
            ),
            2 to IsiBab(
                blok = listOf(
                    TeksGambar("Apa yang sebenarnya membuat suatu zat disebut asam?"),
                    Paragraf("Teori Arrhenius", "Asam melepaskan ion H⁺ dalam air. Basa melepaskan ion OH⁻ dalam air."),
                    Paragraf("Teori Brønsted-Lowry", "Asam adalah donor proton (H⁺). Basa adalah akseptor proton (H⁺)."),
                    Rumus("Contoh", "HCl → H⁺ + Cl⁻ (asam)\nNaOH → Na⁺ + OH⁻ (basa)")
                ),
                kuis = listOf(
                    SoalMini("Menurut Arrhenius, asam melepaskan ion...", listOf("OH⁻", "H⁺", "Na⁺", "Cl⁻"), 1, "Asam Arrhenius melepas H⁺."),
                    SoalMini("Menurut Brønsted-Lowry, basa adalah...", listOf("Donor proton", "Akseptor proton", "Donor elektron", "Pelepas OH⁻ saja"), 1, "Basa menerima proton."),
                    SoalMini("NaOH dalam air melepaskan...", listOf("H⁺", "OH⁻", "O²⁻", "Cl⁻"), 1, "NaOH → Na⁺ + OH⁻.")
                )
            ),
            3 to IsiBab(
                blok = listOf(
                    TeksGambar("Bisakah air rebusan kubis ungu mendeteksi asam dan basa?"),
                    Paragraf("Indikator Alami", "Kunyit, kubis ungu, dan bunga sepatu berubah warna sesuai sifat larutan."),
                    Paragraf("Indikator Buatan", "Kertas lakmus, fenolftalein (tak berwarna di asam, merah muda di basa), metil jingga, dan indikator universal."),
                    Rumus("Fenolftalein", "Asam : tak berwarna\nBasa : merah muda")
                ),
                kuis = listOf(
                    SoalMini("Fenolftalein dalam larutan basa berwarna...", listOf("Kuning", "Merah muda", "Biru", "Tak berwarna"), 1, "Fenolftalein merah muda pada basa."),
                    SoalMini("Contoh indikator alami adalah...", listOf("Lakmus", "Metil jingga", "Kubis ungu", "Fenolftalein"), 2, "Kubis ungu mengandung antosianin."),
                    SoalMini("Indikator yang bisa menunjukkan pH secara luas...", listOf("Lakmus", "Indikator universal", "Kunyit", "Fenolftalein"), 1, "Indikator universal mencakup rentang pH 1-14.")
                )
            ),
            4 to IsiBab(
                blok = listOf(
                    TeksGambar("Bagaimana cara mengukur seberapa asam suatu larutan?"),
                    Rumus("Rumus pH", "pH = −log [H⁺]\npOH = −log [OH⁻]\npH + pOH = 14"),
                    Paragraf("Contoh", "[H⁺] = 10⁻³ M maka pH = 3.\npH = 11 maka pOH = 14 − 11 = 3.")
                ),
                kuis = listOf(
                    SoalMini("[H⁺] = 10⁻⁵ M. pH-nya?", listOf("2", "5", "9", "14"), 1, "pH = −log 10⁻⁵ = 5."),
                    SoalMini("pH = 9. pOH-nya?", listOf("5", "7", "9", "14"), 0, "pOH = 14 − 9 = 5."),
                    SoalMini("Larutan pH 2 dibanding pH 4, keasamannya...", listOf("Lebih lemah", "Sama", "Lebih kuat", "Netral"), 2, "pH lebih kecil berarti lebih asam.")
                )
            ),
            5 to IsiBab(
                blok = listOf(Paragraf("Kuis Reaksi Netralisasi", "Netralisasi: asam + basa → garam + air. Jawab soal berikut.")),
                kuis = listOf(
                    SoalMini("Hasil reaksi netralisasi adalah...", listOf("Garam dan air", "Asam dan basa", "Gas saja", "Logam"), 0, "Asam + basa → garam + air."),
                    SoalMini("HCl + NaOH → NaCl + ...", listOf("H₂", "H₂O", "O₂", "CO₂"), 1, "Terbentuk garam NaCl dan air."),
                    SoalMini("pH larutan netral pada 25°C adalah...", listOf("0", "7", "10", "14"), 1, "Netral pH = 7."),
                    SoalMini("Menurut Arrhenius, basa melepas...", listOf("H⁺", "OH⁻", "e⁻", "Na⁺"), 1, "Basa melepas OH⁻.")
                )
            )
        ),

        // ===================== BIOLOGI =====================
        "biologi_sel" to mapOf(
            1 to IsiBab(
                blok = listOf(
                    TeksGambar("Tahukah kamu tubuhmu tersusun dari triliunan sel?"),
                    Paragraf("Penemuan Sel", "Robert Hooke (1665) mengamati sel gabus. Antonie van Leeuwenhoek mengamati sel hidup dan mikroorganisme."),
                    Paragraf("Teori Sel", "Schleiden dan Schwann: semua makhluk hidup tersusun atas sel. Virchow: sel berasal dari sel yang sudah ada."),
                    Rumus("Ingat", "Sel = unit struktural dan fungsional terkecil makhluk hidup")
                ),
                kuis = listOf(
                    SoalMini("Penemu sel pertama pada gabus...", listOf("Hooke", "Darwin", "Mendel", "Pasteur"), 0, "Robert Hooke, 1665."),
                    SoalMini("Sel adalah unit ... makhluk hidup", listOf("Terbesar", "Struktural dan fungsional terkecil", "Tidak hidup", "Hanya hewan"), 1, "Sel unit dasar kehidupan."),
                    SoalMini("Virchow menyatakan sel berasal dari...", listOf("Benda mati", "Sel sebelumnya", "Air", "Udara"), 1, "Omnis cellula e cellula.")
                )
            ),
            2 to IsiBab(
                blok = listOf(
                    TeksGambar("Sel punya 'organ' kecil di dalamnya. Apa saja?"),
                    Paragraf("Organel Utama", "Nukleus: pusat kendali, berisi DNA.\nMitokondria: menghasilkan energi (ATP).\nRibosom: sintesis protein.\nRetikulum endoplasma: transportasi zat.\nBadan Golgi: pengemasan dan sekresi.\nLisosom: pencernaan intrasel."),
                    Rumus("Ingat", "Mitokondria = pembangkit energi sel")
                ),
                kuis = listOf(
                    SoalMini("Organel penghasil energi...", listOf("Ribosom", "Mitokondria", "Golgi", "Lisosom"), 1, "Mitokondria menghasilkan ATP."),
                    SoalMini("Sintesis protein terjadi di...", listOf("Ribosom", "Vakuola", "Nukleus", "Lisosom"), 0, "Ribosom merakit protein."),
                    SoalMini("Pusat kendali sel adalah...", listOf("Sitoplasma", "Nukleus", "Membran", "Vakuola"), 1, "Nukleus menyimpan DNA.")
                )
            ),
            3 to IsiBab(
                blok = listOf(
                    TeksGambar("Kenapa daun berwarna hijau, sedangkan kulit kita tidak?"),
                    Paragraf("Sel Tumbuhan", "Punya dinding sel, kloroplas, dan vakuola besar. Bentuknya cenderung tetap."),
                    Paragraf("Sel Hewan", "Tidak punya dinding sel dan kloroplas. Punya sentriol dan vakuola kecil. Bentuknya lebih fleksibel.")
                ),
                kuis = listOf(
                    SoalMini("Organel yang hanya ada di sel tumbuhan...", listOf("Mitokondria", "Kloroplas", "Ribosom", "Nukleus"), 1, "Kloroplas untuk fotosintesis."),
                    SoalMini("Sel hewan TIDAK memiliki...", listOf("Nukleus", "Membran sel", "Dinding sel", "Mitokondria"), 2, "Dinding sel khas tumbuhan."),
                    SoalMini("Ukuran vakuola sel tumbuhan umumnya...", listOf("Kecil", "Besar", "Tidak ada", "Sama dengan hewan"), 1, "Vakuola tumbuhan besar.")
                )
            ),
            4 to IsiBab(
                blok = listOf(
                    TeksGambar("Bagaimana zat keluar-masuk sel tanpa merusaknya?"),
                    Paragraf("Transpor Pasif (tanpa energi)", "Difusi: zat berpindah dari konsentrasi tinggi ke rendah.\nOsmosis: difusi air melalui membran semipermeabel."),
                    Paragraf("Transpor Aktif (butuh energi)", "Zat berpindah dari konsentrasi rendah ke tinggi, memakai ATP. Termasuk endositosis dan eksositosis."),
                    Rumus("Ingat", "Pasif: tinggi → rendah\nAktif: rendah → tinggi (butuh ATP)")
                ),
                kuis = listOf(
                    SoalMini("Perpindahan air melalui membran semipermeabel...", listOf("Osmosis", "Fotosintesis", "Respirasi", "Mitosis"), 0, "Itu osmosis."),
                    SoalMini("Transpor aktif membutuhkan...", listOf("Air", "Energi (ATP)", "Cahaya", "Oksigen saja"), 1, "Melawan gradien konsentrasi."),
                    SoalMini("Difusi bergerak dari konsentrasi...", listOf("Rendah ke tinggi", "Tinggi ke rendah", "Sama", "Acak"), 1, "Tinggi ke rendah.")
                )
            ),
            5 to IsiBab(
                blok = listOf(Paragraf("Kuis Struktur Sel", "Uji pemahamanmu tentang sel.")),
                kuis = listOf(
                    SoalMini("Pembangkit energi sel...", listOf("Golgi", "Mitokondria", "Ribosom", "Lisosom"), 1, "Mitokondria."),
                    SoalMini("Sel tumbuhan punya, sel hewan tidak...", listOf("Nukleus", "Dinding sel", "Ribosom", "Membran"), 1, "Dinding sel."),
                    SoalMini("Osmosis adalah difusi...", listOf("Gula", "Air", "Oksigen", "Protein"), 1, "Difusi air."),
                    SoalMini("Penemu sel...", listOf("Hooke", "Newton", "Kepler", "Curie"), 0, "Robert Hooke.")
                )
            )
        ),

        // ===================== ASTRONOMI =====================
        "astronomi_tata_surya" to mapOf(
            1 to IsiBab(
                blok = listOf(
                    TeksGambar("Apa yang membuat semua planet mengorbit di satu titik?"),
                    Paragraf("Tata Surya", "Terdiri dari Matahari, 8 planet, satelit, asteroid, dan komet."),
                    Paragraf("Matahari", "Bintang pusat tata surya, sumber cahaya dan panas. Massanya sekitar 99,8% dari seluruh massa tata surya."),
                    Rumus("Ingat", "Pusat tata surya = Matahari\nJumlah planet = 8")
                ),
                kuis = listOf(
                    SoalMini("Pusat tata surya adalah...", listOf("Bumi", "Bulan", "Matahari", "Mars"), 2, "Heliosentris."),
                    SoalMini("Jumlah planet dalam tata surya...", listOf("7", "8", "9", "10"), 1, "Ada 8 planet."),
                    SoalMini("Matahari termasuk...", listOf("Planet", "Bintang", "Komet", "Satelit"), 1, "Matahari adalah bintang.")
                )
            ),
            2 to IsiBab(
                blok = listOf(
                    TeksGambar("Planet mana yang paling panas, dan mana yang punya cincin?"),
                    Paragraf("Planet Dalam", "Merkurius, Venus, Bumi, Mars. Berbatu (terestrial) dan berukuran kecil."),
                    Paragraf("Planet Luar", "Jupiter, Saturnus, Uranus, Neptunus. Raksasa gas/es dan berukuran besar."),
                    Rumus("Ingat", "Terbesar: Jupiter\nBercincin paling jelas: Saturnus")
                ),
                kuis = listOf(
                    SoalMini("Planet terbesar...", listOf("Saturnus", "Jupiter", "Neptunus", "Bumi"), 1, "Jupiter."),
                    SoalMini("Planet berbatu berikut...", listOf("Mars", "Jupiter", "Uranus", "Neptunus"), 0, "Mars adalah planet dalam."),
                    SoalMini("Planet dengan cincin paling jelas...", listOf("Venus", "Saturnus", "Mars", "Merkurius"), 1, "Saturnus.")
                )
            ),
            3 to IsiBab(
                blok = listOf(
                    TeksGambar("Apa bedanya asteroid dan komet?"),
                    Paragraf("Bulan", "Satelit alami Bumi, mengorbit Bumi sekitar 27,3 hari."),
                    Paragraf("Asteroid", "Benda berbatu kecil, kebanyakan di sabuk asteroid antara Mars dan Jupiter."),
                    Paragraf("Komet", "Benda es dan debu. Ekornya selalu mengarah menjauhi Matahari.")
                ),
                kuis = listOf(
                    SoalMini("Sabuk asteroid berada di antara...", listOf("Bumi-Mars", "Mars-Jupiter", "Jupiter-Saturnus", "Venus-Bumi"), 1, "Antara Mars dan Jupiter."),
                    SoalMini("Ekor komet mengarah...", listOf("Ke Matahari", "Menjauhi Matahari", "Ke Bumi", "Acak"), 1, "Terdorong angin surya."),
                    SoalMini("Bulan adalah...", listOf("Planet", "Satelit alami", "Bintang", "Asteroid"), 1, "Satelit alami Bumi.")
                )
            ),
            4 to IsiBab(
                blok = listOf(
                    TeksGambar("Kenapa planet dekat Matahari bergerak lebih cepat?"),
                    Paragraf("Hukum Kepler I", "Planet mengorbit Matahari dengan lintasan elips, Matahari berada di salah satu titik fokus."),
                    Paragraf("Hukum Kepler II", "Garis Matahari-planet menyapu luas yang sama dalam waktu yang sama."),
                    Rumus("Hukum Kepler III", "T² ∝ a³\n(periode kuadrat sebanding dengan sumbu semi-mayor pangkat tiga)")
                ),
                kuis = listOf(
                    SoalMini("Bentuk orbit planet menurut Kepler I...", listOf("Lingkaran", "Elips", "Persegi", "Parabola"), 1, "Elips."),
                    SoalMini("Planet bergerak paling cepat saat...", listOf("Jauh dari Matahari", "Dekat Matahari", "Selalu sama", "Berhenti"), 1, "Hukum II Kepler."),
                    SoalMini("Kepler III menghubungkan...", listOf("Massa dan warna", "Periode dan jarak orbit", "Suhu dan massa", "Cahaya dan ukuran"), 1, "T² ∝ a³.")
                )
            ),
            5 to IsiBab(
                blok = listOf(Paragraf("Kuis Evaluasi Tata Surya", "Uji pemahamanmu tentang tata surya.")),
                kuis = listOf(
                    SoalMini("Planet terbesar...", listOf("Bumi", "Jupiter", "Mars", "Venus"), 1, "Jupiter."),
                    SoalMini("Pusat tata surya...", listOf("Bumi", "Bulan", "Matahari", "Saturnus"), 2, "Matahari."),
                    SoalMini("Orbit planet berbentuk...", listOf("Elips", "Lingkaran sempurna", "Persegi", "Garis lurus"), 0, "Kepler I."),
                    SoalMini("Sabuk asteroid ada di antara...", listOf("Mars dan Jupiter", "Bumi dan Bulan", "Venus dan Bumi", "Uranus dan Neptunus"), 0, "Mars-Jupiter.")
                )
            )
        ),

        // ===================== MATEMATIKA =====================
        "matematika_aljabar" to mapOf(
            1 to IsiBab(
                blok = listOf(
                    TeksGambar("Kalau 1 buku harganya belum diketahui, bagaimana menuliskannya?"),
                    Paragraf("Variabel", "Simbol (biasanya x, y) yang mewakili nilai yang belum diketahui."),
                    Paragraf("Persamaan Linear", "Kalimat matematika dengan tanda sama dengan, variabel berpangkat 1. Contoh: 2x + 3 = 11."),
                    Rumus("Bentuk Umum", "ax + b = c")
                ),
                kuis = listOf(
                    SoalMini("Yang termasuk variabel...", listOf("5", "x", "+", "="), 1, "x mewakili nilai tak diketahui."),
                    SoalMini("Persamaan linear satu variabel berikut...", listOf("x² + 1 = 5", "2x + 3 = 7", "xy = 4", "x³ = 8"), 1, "Pangkat variabel = 1."),
                    SoalMini("Pada 3x + 2 = 8, koefisien x adalah...", listOf("2", "3", "8", "x"), 1, "Angka di depan x.")
                )
            ),
            2 to IsiBab(
                blok = listOf(
                    TeksGambar("Bagaimana mencari nilai x dengan cepat?"),
                    Paragraf("Prinsip", "Lakukan operasi yang sama pada kedua ruas agar persamaan tetap seimbang."),
                    Rumus("Contoh", "2x + 3 = 11\n2x = 11 − 3\n2x = 8\nx = 4")
                ),
                kuis = listOf(
                    SoalMini("x + 5 = 12. Nilai x?", listOf("5", "7", "12", "17"), 1, "x = 12 − 5."),
                    SoalMini("3x = 18. Nilai x?", listOf("3", "6", "15", "54"), 1, "x = 18/3."),
                    SoalMini("2x − 4 = 10. Nilai x?", listOf("3", "5", "7", "14"), 2, "2x = 14, x = 7.")
                )
            ),
            3 to IsiBab(
                blok = listOf(
                    TeksGambar("Ada dua nilai yang tidak diketahui sekaligus. Bagaimana?"),
                    Paragraf("SPLDV", "Dua persamaan linear dengan dua variabel (x dan y). Diselesaikan dengan eliminasi atau substitusi."),
                    Rumus("Contoh Eliminasi", "x + y = 5\nx − y = 1\nJumlahkan: 2x = 6, x = 3\nMaka y = 2")
                ),
                kuis = listOf(
                    SoalMini("x + y = 7 dan x − y = 1. Nilai x?", listOf("3", "4", "5", "6"), 1, "2x = 8, x = 4."),
                    SoalMini("Dari soal sebelumnya, nilai y?", listOf("2", "3", "4", "5"), 1, "y = 7 − 4 = 3."),
                    SoalMini("Metode menyelesaikan SPLDV...", listOf("Eliminasi", "Integral", "Turunan", "Logaritma"), 0, "Eliminasi/substitusi.")
                )
            ),
            4 to IsiBab(
                blok = listOf(
                    TeksGambar("Ani beli 2 pensil dan 1 buku seharga Rp7.000. Berapa harga satuan?"),
                    Paragraf("Langkah", "1. Tentukan variabel.\n2. Ubah cerita jadi persamaan.\n3. Selesaikan.\n4. Periksa jawaban."),
                    Rumus("Contoh", "Umur ayah 3 kali umur Budi, jumlahnya 48.\nx + 3x = 48\n4x = 48, x = 12")
                ),
                kuis = listOf(
                    SoalMini("Uang Rina x, uang Sita 2x, jumlah Rp30.000. Uang Rina?", listOf("5.000", "10.000", "15.000", "20.000"), 1, "3x = 30.000, x = 10.000."),
                    SoalMini("Bilangan dikali 4 lalu ditambah 2 hasilnya 22. Bilangannya?", listOf("4", "5", "6", "20"), 1, "4x + 2 = 22, x = 5."),
                    SoalMini("Langkah pertama soal cerita...", listOf("Menjawab", "Menentukan variabel", "Mengalikan", "Menebak"), 1, "Misalkan variabelnya dulu.")
                )
            ),
            5 to IsiBab(
                blok = listOf(Paragraf("Kuis Persamaan Linear", "Uji pemahamanmu.")),
                kuis = listOf(
                    SoalMini("x + 9 = 15. x = ...", listOf("5", "6", "7", "24"), 1, "15 − 9."),
                    SoalMini("4x = 28. x = ...", listOf("6", "7", "8", "24"), 1, "28/4."),
                    SoalMini("x + y = 10, x − y = 4. x = ...", listOf("5", "6", "7", "8"), 2, "2x = 14."),
                    SoalMini("Pada soal sebelumnya, y = ...", listOf("2", "3", "4", "5"), 1, "y = 10 − 7.")
                )
            )
        ),

        // ===================== GEOGRAFI =====================
        "geografi_litosfer" to mapOf(
            1 to IsiBab(
                blok = listOf(
                    TeksGambar("Apa yang ada di bawah kaki kita, jauh di dalam Bumi?"),
                    Paragraf("Lapisan Bumi", "Kerak bumi (tipis, tempat kita hidup), mantel (batuan panas dan kental), inti luar (cair), inti dalam (padat)."),
                    Rumus("Urutan Luar ke Dalam", "Kerak → Mantel → Inti luar → Inti dalam")
                ),
                kuis = listOf(
                    SoalMini("Lapisan terluar Bumi...", listOf("Mantel", "Kerak", "Inti luar", "Inti dalam"), 1, "Kerak bumi."),
                    SoalMini("Inti luar Bumi berwujud...", listOf("Padat", "Cair", "Gas", "Plasma"), 1, "Inti luar cair."),
                    SoalMini("Lapisan tepat di bawah kerak...", listOf("Mantel", "Inti dalam", "Atmosfer", "Litosfer"), 0, "Mantel.")
                )
            ),
            2 to IsiBab(
                blok = listOf(
                    TeksGambar("Kenapa Indonesia sering gempa?"),
                    Paragraf("Tektonik Lempeng", "Kerak bumi terpecah menjadi lempeng-lempeng yang terus bergerak perlahan."),
                    Paragraf("Jenis Pergerakan", "Divergen: saling menjauh.\nKonvergen: saling bertumbukan.\nTransform: saling bergeser."),
                    Paragraf("Indonesia", "Berada di pertemuan lempeng Eurasia, Indo-Australia, dan Pasifik.")
                ),
                kuis = listOf(
                    SoalMini("Lempeng saling bertumbukan disebut...", listOf("Divergen", "Konvergen", "Transform", "Statis"), 1, "Konvergen."),
                    SoalMini("Indonesia dilalui lempeng...", listOf("Eurasia, Indo-Australia, Pasifik", "Afrika saja", "Antartika saja", "Amerika saja"), 0, "Tiga lempeng besar."),
                    SoalMini("Lempeng saling menjauh...", listOf("Divergen", "Konvergen", "Transform", "Subduksi"), 0, "Divergen.")
                )
            ),
            3 to IsiBab(
                blok = listOf(
                    TeksGambar("Apa yang terjadi saat gunung api meletus?"),
                    Paragraf("Vulkanisme", "Proses keluarnya magma ke permukaan bumi. Magma yang keluar disebut lava."),
                    Paragraf("Gempa Bumi", "Getaran di permukaan bumi akibat pelepasan energi. Jenisnya: tektonik, vulkanik, dan runtuhan. Kekuatannya diukur dengan magnitudo (skala Richter)."),
                    Rumus("Ingat", "Magma (dalam bumi) → Lava (di permukaan)")
                ),
                kuis = listOf(
                    SoalMini("Magma yang keluar ke permukaan disebut...", listOf("Lava", "Lahar", "Abu", "Gas"), 0, "Lava."),
                    SoalMini("Gempa paling sering disebabkan oleh...", listOf("Tektonik", "Angin", "Hujan", "Banjir"), 0, "Pergerakan lempeng."),
                    SoalMini("Alat pencatat gempa...", listOf("Termometer", "Seismograf", "Barometer", "Higrometer"), 1, "Seismograf.")
                )
            ),
            4 to IsiBab(
                blok = listOf(
                    TeksGambar("Bagaimana kita bisa mengurangi korban saat bencana?"),
                    Paragraf("Mitigasi", "Upaya mengurangi risiko bencana."),
                    Paragraf("Contoh Mitigasi", "Bangunan tahan gempa, peta rawan bencana, jalur evakuasi, sistem peringatan dini tsunami, dan simulasi bencana."),
                    Rumus("Saat Gempa", "Lindungi kepala, berlindung di bawah meja kuat, jauhi kaca, keluar setelah guncangan berhenti")
                ),
                kuis = listOf(
                    SoalMini("Contoh mitigasi bencana...", listOf("Membangun rumah tahan gempa", "Membuang sampah", "Menebang hutan", "Menutup jalur evakuasi"), 0, "Mengurangi risiko."),
                    SoalMini("Saat gempa, sebaiknya...", listOf("Berlari ke lift", "Berlindung di bawah meja kuat", "Berdiri dekat kaca", "Diam di tangga"), 1, "Lindungi kepala."),
                    SoalMini("Peringatan dini tsunami termasuk...", listOf("Mitigasi", "Penyebab", "Hukuman", "Vulkanisme"), 0, "Termasuk mitigasi.")
                )
            ),
            5 to IsiBab(
                blok = listOf(Paragraf("Kuis Evaluasi Litosfer", "Uji pemahamanmu tentang litosfer.")),
                kuis = listOf(
                    SoalMini("Lapisan Bumi yang cair...", listOf("Kerak", "Mantel", "Inti luar", "Inti dalam"), 2, "Inti luar."),
                    SoalMini("Lempeng bertumbukan...", listOf("Konvergen", "Divergen", "Transform", "Statis"), 0, "Konvergen."),
                    SoalMini("Magma yang keluar disebut...", listOf("Lava", "Kerak", "Mantel", "Uap"), 0, "Lava."),
                    SoalMini("Upaya mengurangi risiko bencana...", listOf("Mitigasi", "Erosi", "Abrasi", "Sedimentasi"), 0, "Mitigasi.")
                )
            )
        )
    )
}