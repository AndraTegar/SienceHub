package com.kelompoksix.siencehub.data.repositories

import com.kelompoksix.siencehub.data.models.Soal

object QuizRepository {

    fun getSoalBySubject(subjectName: String): List<Soal> {
        return when (subjectName.lowercase().trim()) {

            // ==========================================
            // 1. KUIS FISIKA (10 SOAL)
            // ==========================================
            "fisika", "physics" -> listOf(
                Soal(
                    id = 1,
                    pertanyaan = "Sebuah benda akan tetap diam atau bergerak lurus beraturan jika tidak ada gaya luar yang bekerja padanya. Hal ini sesuai dengan...",
                    opsi = listOf("Hukum I Newton", "Hukum II Newton", "Hukum III Newton", "Hukum Kepler"),
                    jawabanBenar = 0,
                    pembahasan = "Hukum I Newton menjelaskan tentang kelembaman atau inersia benda."
                ),
                Soal(
                    id = 2,
                    pertanyaan = "Besarnya percepatan yang dialami benda berbanding lurus dengan gaya total dan berbanding terbalik dengan...",
                    opsi = listOf("Kecepatan", "Massa benda", "Jarak", "Waktu"),
                    jawabanBenar = 1,
                    pembahasan = "Berdasarkan Hukum II Newton (F = m * a), percepatan berbanding terbalik dengan massa (a = F / m)."
                ),
                Soal(
                    id = 3,
                    pertanyaan = "Sebuah mobil bergerak dengan kecepatan konstan 20 m/s selama 10 detik. Berapa jarak total yang ditempuh mobil tersebut?",
                    opsi = listOf("100 m", "150 m", "200 m", "250 m"),
                    jawabanBenar = 2,
                    pembahasan = "Rumus Gerak Lurus Beraturan (GLB): s = v * t. Maka s = 20 * 10 = 200 meter."
                ),
                Soal(
                    id = 4,
                    pertanyaan = "Satuan Internasional (SI) untuk mengukur usaha dan energi adalah...",
                    opsi = listOf("Watt", "Pascal", "Newton", "Joule"),
                    jawabanBenar = 3,
                    pembahasan = "Joule adalah satuan SI untuk usaha dan energi. Watt untuk daya dan Pascal untuk tekanan."
                ),
                Soal(
                    id = 5,
                    pertanyaan = "Sebuah benda bermassa 2 kg bergerak dengan kecepatan 4 m/s. Energi kinetik benda tersebut adalah...",
                    opsi = listOf("8 Joule", "16 Joule", "32 Joule", "64 Joule"),
                    jawabanBenar = 1,
                    pembahasan = "Rumus Energi Kinetik: Ek = 0.5 * m * v^2. Maka Ek = 0.5 * 2 * (4)^2 = 16 Joule."
                ),
                Soal(
                    id = 6,
                    pertanyaan = "Sebuah rangkaian listrik memiliki kuat arus 2 A dan hambatan 10 Ohm. Berapakah beda potensialnya?",
                    opsi = listOf("5 Volt", "12 Volt", "20 Volt", "40 Volt"),
                    jawabanBenar = 2,
                    pembahasan = "Berdasarkan Hukum Ohm (V = I * R), V = 2 A * 10 Ohm = 20 Volt."
                ),
                Soal(
                    id = 7,
                    pertanyaan = "Perubahan wujud zat dari padat langsung menjadi gas dinamakan...",
                    opsi = listOf("Mencair", "Mengkristal", "Menyublim", "Menguap"),
                    jawabanBenar = 2,
                    pembahasan = "Menyublim adalah proses perubahan wujud dari padat langsung menjadi gas."
                ),
                Soal(
                    id = 8,
                    pertanyaan = "Jika suatu gelombang memiliki periode (T) sebesar 0,5 detik, berapakah frekuensi gelombang tersebut?",
                    opsi = listOf("0,5 Hz", "1 Hz", "2 Hz", "4 Hz"),
                    jawabanBenar = 2,
                    pembahasan = "Hubungan frekuensi dan periode: f = 1 / T. Maka f = 1 / 0.5 = 2 Hz."
                ),
                Soal(
                    id = 9,
                    pertanyaan = "Sifat bayangan yang dibentuk oleh cermin datar adalah...",
                    opsi = listOf("Nyata, terbalik, diperbesar", "Maya, tegak, sama besar", "Nyata, tegak, diperkecil", "Maya, terbalik, sama besar"),
                    jawabanBenar = 1,
                    pembahasan = "Cermin datar selalu membentuk bayangan maya, tegak, dan sama besar."
                ),
                Soal(
                    id = 10,
                    pertanyaan = "Sebuah lampu melakukan usaha sebesar 600 Joule dalam waktu 60 detik. Daya lampu tersebut adalah...",
                    opsi = listOf("10 Watt", "60 Watt", "100 Watt", "600 Watt"),
                    jawabanBenar = 0,
                    pembahasan = "Rumus daya: P = W / t. Maka P = 600 / 60 = 10 Watt."
                )
            )

            // ==========================================
            // 2. KUIS KIMIA (10 SOAL)
            // ==========================================
            "kimia", "chemistry" -> listOf(
                Soal(
                    id = 1,
                    pertanyaan = "Partikel subatomik bermuatan positif yang terletak di dalam inti atom adalah...",
                    opsi = listOf("Elektron", "Proton", "Neutron", "Positron"),
                    jawabanBenar = 1,
                    pembahasan = "Inti atom terdiri atas Proton (bermuatan positif) dan Neutron (netral)."
                ),
                Soal(
                    id = 2,
                    pertanyaan = "Suatu unsur memiliki nomor atom 11. Konfigurasi elektronnya pada kulit K, L, M adalah...",
                    opsi = listOf("2, 8, 1", "2, 8, 2", "2, 9", "8, 2, 1"),
                    jawabanBenar = 0,
                    pembahasan = "Kulit K terisi 2 elektron, kulit L terisi 8 elektron, dan sisa 1 elektron mengisi kulit M."
                ),
                Soal(
                    id = 3,
                    pertanyaan = "Reaksi kimia yang menyerap kalor dari lingkungan ke sistem disebut reaksi...",
                    opsi = listOf("Eksoterm", "Endoterm", "Redoks", "Oksidasi"),
                    jawabanBenar = 1,
                    pembahasan = "Reaksi endoterm menyerap energi panas dari lingkungan sehingga suhu lingkungan menurun."
                ),
                Soal(
                    id = 4,
                    pertanyaan = "Larutan yang memiliki nilai pH kurang dari 7 (pH < 7) tergolong sebagai larutan...",
                    opsi = listOf("Basa", "Netral", "Asam", "Garam"),
                    jawabanBenar = 2,
                    pembahasan = "Skala pH: pH < 7 tergolong Asam, pH = 7 Netral, dan pH > 7 Basa."
                ),
                Soal(
                    id = 5,
                    pertanyaan = "Ikatan kimia yang terbentuk akibat adanya serah terima elektron antara unsur logam dan non-logam adalah...",
                    opsi = listOf("Ikatan Kovalen", "Ikatan Ion", "Ikatan Logam", "Ikatan Hidrogen"),
                    jawabanBenar = 1,
                    pembahasan = "Ikatan ion terjadi karena adanya serah terima elektron antara ion positif dan negatif."
                ),
                Soal(
                    id = 6,
                    pertanyaan = "Lambang unsur kimia untuk Emas adalah...",
                    opsi = listOf("Ag", "Fe", "Au", "Cu"),
                    jawabanBenar = 2,
                    pembahasan = "Emas dilambangkan dengan Au (Aurum)."
                ),
                Soal(
                    id = 7,
                    pertanyaan = "Hukum Kimia yang menyatakan bahwa massa zat sebelum dan sesudah reaksi adalah sama dikemukakan oleh...",
                    opsi = listOf("Dalton", "Lavoisier", "Proust", "Avogadro"),
                    jawabanBenar = 1,
                    pembahasan = "Hukum Kekekalan Massa dikemukakan oleh Antoine Lavoisier."
                ),
                Soal(
                    id = 8,
                    pertanyaan = "Zat tunggal yang terdiri atas dua atau lebih unsur yang tergabung secara kimia dinamakan...",
                    opsi = listOf("Unsur", "Senyawa", "Campuran Homogen", "Campuran Heterogen"),
                    jawabanBenar = 1,
                    pembahasan = "Senyawa terbentuk dari gabungan dua atau lebih unsur yang berbeda melalui reaksi kimia."
                ),
                Soal(
                    id = 9,
                    pertanyaan = "Dalam satu golongan tabel periodik dari atas ke bawah, ukuran jari-jari atom akan...",
                    opsi = listOf("Makin kecil", "Makin besar", "Tetap", "Sama dengan nol"),
                    jawabanBenar = 1,
                    pembahasan = "Dari atas ke bawah dalam satu golongan, jumlah kulit atom bertambah sehingga jari-jari atom makin besar."
                ),
                Soal(
                    id = 10,
                    pertanyaan = "Metode pemisahan campuran yang didasarkan pada perbedaan ukuran partikel zat dinamakan...",
                    opsi = listOf("Distilasi", "Filtrasi", "Kromatografi", "Sublimasi"),
                    jawabanBenar = 1,
                    pembahasan = "Filtrasi (penyaringan) memisahkan padatan tak terlarut dari cairan berdasarkan perbedaan ukuran partikel."
                )
            )

            // ==========================================
            // 3. KUIS BIOLOGI (10 SOAL)
            // ==========================================
            "biologi", "biology" -> listOf(
                Soal(
                    id = 1,
                    pertanyaan = "Organel sel yang berperan sebagai pusat pembentukan energi (ATP) adalah...",
                    opsi = listOf("Ribosom", "Lisosom", "Mitokondria", "Badan Golgi"),
                    jawabanBenar = 2,
                    pembahasan = "Mitokondria melakukan respirasi seluler untuk memproduksi ATP."
                ),
                Soal(
                    id = 2,
                    pertanyaan = "Perpindahan molekul air melewati membran semipermeabel dari larutan encer ke pekat disebut...",
                    opsi = listOf("Difusi", "Osmosis", "Endositosis", "Eksositosis"),
                    jawabanBenar = 1,
                    pembahasan = "Osmosis adalah pergerakan pelarut (air) melintasi selaput semipermeabel."
                ),
                Soal(
                    id = 3,
                    pertanyaan = "Pembuluh darah utama yang membawa darah kaya oksigen keluar dari jantung ke seluruh tubuh adalah...",
                    opsi = listOf("Vena Cava", "Arteri Aorta", "Kapiler", "Vena Pulmonalis"),
                    jawabanBenar = 1,
                    pembahasan = "Aorta adalah pembuluh arteri terbesar yang membawa darah kaya O2 keluar dari bilik kiri jantung."
                ),
                Soal(
                    id = 4,
                    pertanyaan = "Proses fotosintesis pada tumbuhan hijau terjadi di dalam organel sel yaitu...",
                    opsi = listOf("Mitokondria", "Kloroplas", "Vakuola", "Ribosom"),
                    jawabanBenar = 1,
                    pembahasan = "Kloroplas mengandung pigmen klorofil untuk menangkap energi cahaya matahari."
                ),
                Soal(
                    id = 5,
                    pertanyaan = "Enzim di dalam rongga mulut yang mengubah karbohidrat (amilum) menjadi gula sederhana adalah...",
                    opsi = listOf("Pepsin", "Tripsin", "Ptialin", "Lipase"),
                    jawabanBenar = 2,
                    pembahasan = "Enzim ptialin (amilase ludah) memecah amilum menjadi gula sederhana/maltosa."
                ),
                Soal(
                    id = 6,
                    pertanyaan = "Proses penyaringan darah (filtrasi) tahap awal pada ginjal manusia terjadi di bagian...",
                    opsi = listOf("Tubulus Kontortus", "Glomerulus", "Ureter", "Kandung Kemih"),
                    jawabanBenar = 1,
                    pembahasan = "Penyaringan darah pertama kali terjadi di Glomerulus."
                ),
                Soal(
                    id = 7,
                    pertanyaan = "Sel saraf yang berfungsi membawa rangsangan dari indera menuju ke sistem saraf pusat adalah...",
                    opsi = listOf("Neuron Motorik", "Neuron Sensorik", "Neuron Konektor", "Neuron Pusat"),
                    jawabanBenar = 1,
                    pembahasan = "Neuron sensorik membawa sinyal/rangsangan dari indera ke sistem saraf pusat."
                ),
                Soal(
                    id = 8,
                    pertanyaan = "Materi genetik (DNA) pada sel eukariotik utamanya tersimpan di dalam...",
                    opsi = listOf("Sitoplasma", "Nukleus", "Membran Sel", "Ribosom"),
                    jawabanBenar = 1,
                    pembahasan = "Nukleus (inti sel) merupakan tempat tersimpannya materi genetik seperti DNA."
                ),
                Soal(
                    id = 9,
                    pertanyaan = "Dalam suatu rantai makanan, kedudukan tumbuhan hijau bertindak sebagai...",
                    opsi = listOf("Konsumen I", "Produsen", "Pengurai", "Konsumen II"),
                    jawabanBenar = 1,
                    pembahasan = "Tumbuhan hijau mampu menghasilkan makanan sendiri melalui fotosintesis sehingga berperan sebagai produsen."
                ),
                Soal(
                    id = 10,
                    pertanyaan = "Pembelahan sel yang menghasilkan dua sel anakan dengan jumlah kromosom sama seperti sel induk disebut...",
                    opsi = listOf("Meiosis", "Mitosis", "Amitosis", "Partenogenesis"),
                    jawabanBenar = 1,
                    pembahasan = "Mitosis adalah pembelahan sel tubuh yang menghasilkan sel anak identik secara genetik."
                )
            )

            // ==========================================
            // 4. KUIS ASTRONOMI (10 SOAL)
            // ==========================================
            "astronomi", "astronomy" -> listOf(
                Soal(
                    id = 1,
                    pertanyaan = "Planet terbesar di Tata Surya kita adalah...",
                    opsi = listOf("Mars", "Saturnus", "Yupiter", "Neptunus"),
                    jawabanBenar = 2,
                    pembahasan = "Yupiter adalah planet terbesar di Sistem Tata Surya kita."
                ),
                Soal(
                    id = 2,
                    pertanyaan = "Bintang yang menjadi pusat dari Sistem Tata Surya kita adalah...",
                    opsi = listOf("Bumi", "Matahari", "Proxima Centauri", "Sirius"),
                    jawabanBenar = 1,
                    pembahasan = "Matahari adalah bintang pusat yang dikelilingi oleh planet-planet di Tata Surya."
                ),
                Soal(
                    id = 3,
                    pertanyaan = "Planet terdekat dari Matahari adalah...",
                    opsi = listOf("Venus", "Merkurius", "Bumi", "Mars"),
                    jawabanBenar = 1,
                    pembahasan = "Merkurius merupakan planet paling dekat dengan Matahari."
                ),
                Soal(
                    id = 4,
                    pertanyaan = "Satelit alami yang mengelilingi planet Bumi bernama...",
                    opsi = listOf("Phobos", "Bulan", "Titan", "Ganymede"),
                    jawabanBenar = 1,
                    pembahasan = "Bulan adalah satu-satunya satelit alami milik Bumi."
                ),
                Soal(
                    id = 5,
                    pertanyaan = "Planet yang sering dijuluki sebagai 'Planet Merah' adalah...",
                    opsi = listOf("Venus", "Jupiter", "Mars", "Saturnus"),
                    jawabanBenar = 2,
                    pembahasan = "Mars dijuluki Planet Merah karena kandungan besi oksida di permukaannya."
                ),
                Soal(
                    id = 6,
                    pertanyaan = "Galaksi tempat Sistem Tata Surya kita berada dinamakan...",
                    opsi = listOf("Andromeda", "Bima Sakti (Milky Way)", "Triangulum", "Sombrero"),
                    jawabanBenar = 1,
                    pembahasan = "Sistem Tata Surya kita terletak di Galaksi Bima Sakti."
                ),
                Soal(
                    id = 7,
                    pertanyaan = "Fenomena ketika Bulan berada di antara Bumi dan Matahari sehingga menutupi sinar Matahari disebut...",
                    opsi = listOf("Gerhana Bulan", "Gerhana Matahari", "Aurora", "Supernova"),
                    jawabanBenar = 1,
                    pembahasan = "Gerhana Matahari terjadi saat posisi Bulan sejajar di antara Bumi dan Matahari."
                ),
                Soal(
                    id = 8,
                    pertanyaan = "Planet yang terkenal dengan sistem cincin yang sangat mencolok dan indah adalah...",
                    opsi = listOf("Uranus", "Neptunus", "Saturnus", "Jupiter"),
                    jawabanBenar = 2,
                    pembahasan = "Saturnus memiliki cincin spektakuler yang terdiri dari batuan dan partikel es."
                ),
                Soal(
                    id = 9,
                    pertanyaan = "Benda langit berukuran kecil terbuat dari es dan debu yang mengorbit Matahari dan memiliki ekor bercahaya adalah...",
                    opsi = listOf("Asteroid", "Komet", "Meteor", "Satelit"),
                    jawabanBenar = 1,
                    pembahasan = "Komet terdiri dari es dan debu yang menguap membentuk ekor saat mendekati Matahari."
                ),
                Soal(
                    id = 10,
                    pertanyaan = "Satuan jarak dalam astronomi yang didefinisikan sebagai jarak yang ditempuh cahaya dalam waktu satu tahun adalah...",
                    opsi = listOf("AU (Astronomical Unit)", "Tahun Cahaya (Light Year)", "Parsec", "Kilometer"),
                    jawabanBenar = 1,
                    pembahasan = "Tahun Cahaya adalah jarak yang ditempuh gelombang cahaya selama satu tahun."
                )
            )

            // ==========================================
            // 5. KUIS KEBUMIAN / GEOLOGI (10 SOAL)
            // ==========================================
            "geografi", "geologi", "earth science" -> listOf(
                Soal(
                    id = 1,
                    pertanyaan = "Lapisan terluar Bumi tempat tinggal makhluk hidup dinamakan...",
                    opsi = listOf("Kerak Bumi (Crust)", "Mantel Bumi", "Inti Luar", "Inti Dalam"),
                    jawabanBenar = 0,
                    pembahasan = "Kerak bumi adalah lapisan paling luar dan paling tipis tempat makhluk hidup berada."
                ),
                Soal(
                    id = 2,
                    pertanyaan = "Batuan yang terbentuk dari hasil pembekuan magma atau lava disebut...",
                    opsi = listOf("Batuan Beku", "Batuan Sedimen", "Batuan Metamorf", "Batuan Fosil"),
                    jawabanBenar = 0,
                    pembahasan = "Batuan beku terbentuk saat pembekuan magma di dalam bumi atau lava di permukaan bumi."
                ),
                Soal(
                    id = 3,
                    pertanyaan = "Alat yang digunakan untuk mencatat gelombang seismik saat terjadi gempa bumi adalah...",
                    opsi = listOf("Barometer", "Termometer", "Seismograf", "Anemometer"),
                    jawabanBenar = 2,
                    pembahasan = "Seismograf adalah instrumen pengukur dan pencatat getaran/gelombang gempa bumi."
                ),
                Soal(
                    id = 4,
                    pertanyaan = "Gas dengan persentase terbanyak yang menyusun atmosfer Bumi adalah...",
                    opsi = listOf("Oksigen", "Nitrogen", "Karbondioksida", "Argon"),
                    jawabanBenar = 1,
                    pembahasan = "Gas Nitrogen menyusun sekitar 78% dari total volume atmosfer Bumi."
                ),
                Soal(
                    id = 5,
                    pertanyaan = "Lapisan atmosfer tempat terjadinya fenomena cuaca seperti hujan, angin, dan awan adalah...",
                    opsi = listOf("Troposfer", "Stratosfer", "Mesosfer", "Termosfer"),
                    jawabanBenar = 0,
                    pembahasan = "Troposfer adalah lapisan terdekat dari permukaan bumi tempat seluruh aktivitas cuaca terjadi."
                ),
                Soal(
                    id = 6,
                    pertanyaan = "Lapisan ozon yang berfungsi menyerap radiasi ultraviolet sinar matahari terdapat pada lapisan...",
                    opsi = listOf("Troposfer", "Stratosfer", "Mesosfer", "Eksosfer"),
                    jawabanBenar = 1,
                    pembahasan = "Lapisan ozon berada di Stratosfer pada ketinggian sekitar 15-35 km dari permukaan bumi."
                ),
                Soal(
                    id = 7,
                    pertanyaan = "Proses pelepasan uap air dari daun tumbuh-tumbuhan ke atmosfer dalam siklus air dinamakan...",
                    opsi = listOf("Evaporasi", "Kondensasi", "Transpirasi", "Presipitasi"),
                    jawabanBenar = 2,
                    pembahasan = "Transpirasi adalah proses penguapan air yang terjadi pada jaringan makhluk hidup (tumbuhan)."
                ),
                Soal(
                    id = 8,
                    pertanyaan = "Pengikisan lapisan tanah oleh air, angin, atau es dinamakan...",
                    opsi = listOf("Erosi", "Sedimentasi", "Apresiasi", "Reboisasi"),
                    jawabanBenar = 0,
                    pembahasan = "Erosi adalah proses pengikisan dan pemindahan material tanah/batuan dari satu tempat ke tempat lain."
                ),
                Soal(
                    id = 9,
                    pertanyaan = "Teori yang menjelaskan pergerakan lempeng-lempeng litosfer bumi yang menyebabkan gempa dan pegunungan adalah...",
                    opsi = listOf("Tektonik Lempeng", "Gravitasi", "Pangaea", "Kekekalan Energi"),
                    jawabanBenar = 0,
                    pembahasan = "Teori Tektonik Lempeng menjelaskan bagaimana lempeng bumi terus bergerak dan berinteraksi."
                ),
                Soal(
                    id = 10,
                    pertanyaan = "Mineral terkeras di Bumi berdasarkan skala Mohs (skala 10) adalah...",
                    opsi = listOf("Kuarsa", "Topaz", "Intan (Diamond)", "Gipsum"),
                    jawabanBenar = 2,
                    pembahasan = "Intan memiliki tingkat kekerasan tertinggi yaitu nilai 10 pada Skala Mohs."
                )
            )

            // ==========================================
            // 6. KUIS MATEMATIKA (10 SOAL)
            // ==========================================
            "matematika", "math", "matematika_aljabar" -> listOf(
                Soal(
                    id = 1,
                    pertanyaan = "Bentuk sederhana dari 3x + 5x - 2x adalah...",
                    opsi = listOf("6x", "8x", "10x", "4x"),
                    jawabanBenar = 0,
                    pembahasan = "Jumlahkan koefisien variabel x: (3 + 5 - 2)x = 6x."
                ),
                Soal(
                    id = 2,
                    pertanyaan = "Jika x + 5 = 12, maka nilai dari x adalah...",
                    opsi = listOf("5", "6", "7", "8"),
                    jawabanBenar = 2,
                    pembahasan = "Pindahkan konstanta ke ruas kanan: x = 12 - 5 = 7."
                ),
                Soal(
                    id = 3,
                    pertanyaan = "Nilai x yang memenuhi persamaan 3x = 18 adalah...",
                    opsi = listOf("4", "5", "6", "9"),
                    jawabanBenar = 2,
                    pembahasan = "Bagi kedua ruas dengan 3: x = 18 / 3 = 6."
                ),
                Soal(
                    id = 4,
                    pertanyaan = "Hasil penyederhanaan dari 2(x + 4) adalah...",
                    opsi = listOf("2x + 4", "2x + 8", "x + 8", "2x + 6"),
                    jawabanBenar = 1,
                    pembahasan = "Kalikan 2 ke dalam kurung: 2*x + 2*4 = 2x + 8."
                ),
                Soal(
                    id = 5,
                    pertanyaan = "Koefisien dari variabel y pada bentuk aljabar 4x - 7y + 12 adalah...",
                    opsi = listOf("4", "7", "-7", "12"),
                    jawabanBenar = 2,
                    pembahasan = "Koefisien adalah angka di depan variabel beserta tandanya, yaitu -7."
                ),
                Soal(
                    id = 6,
                    pertanyaan = "Jika x = 3 dan y = 2, nilai dari 2x + 3y adalah...",
                    opsi = listOf("10", "12", "14", "16"),
                    jawabanBenar = 1,
                    pembahasan = "Substitusikan nilai x dan y: 2(3) + 3(2) = 6 + 6 = 12."
                ),
                Soal(
                    id = 7,
                    pertanyaan = "Penyelesaian dari sistem persamaan x + y = 7 dan x - y = 1 adalah...",
                    opsi = listOf("x = 4, y = 3", "x = 5, y = 2", "x = 3, y = 4", "x = 6, y = 1"),
                    jawabanBenar = 0,
                    pembahasan = "Eliminasi y: (x+y)+(x-y) = 7+1 -> 2x = 8 -> x = 4. Maka y = 7 - 4 = 3."
                ),
                Soal(
                    id = 8,
                    pertanyaan = "Jumlah dua buah bilangan adalah 15 dan selisihnya adalah 3. Bilangan terbesarnya adalah...",
                    opsi = listOf("7", "8", "9", "10"),
                    jawabanBenar = 2,
                    pembahasan = "Persamaan: a + b = 15 dan a - b = 3. Jumlahkan keduanya: 2a = 18 -> a = 9."
                ),
                Soal(
                    id = 9,
                    pertanyaan = "Bentuk aljabar dari kalimat 'Suatu bilangan x dikurangi 5 lalu dikali 2' adalah...",
                    opsi = listOf("2x - 5", "2(x - 5)", "x - 10", "5 - 2x"),
                    jawabanBenar = 1,
                    pembahasan = "Pengurangan dilakukan terlebih dahulu (x - 5), baru kemudian dikali 2 menjadi 2(x - 5)."
                ),
                Soal(
                    id = 10,
                    pertanyaan = "Hasil dari (x + 3)(x + 2) adalah...",
                    opsi = listOf("x^2 + 5x + 6", "x^2 + 6x + 5", "x^2 + 5x + 5", "x^2 + 6x + 6"),
                    jawabanBenar = 0,
                    pembahasan = "Gunakan perkalian aljabar: x*x + x*2 + 3*x + 3*2 = x^2 + 2x + 3x + 6 = x^2 + 5x + 6."
                )
            )

            // ==========================================
            // DEFAULT (JIKA MAPEL TIDAK DITEMUKAN)
            // ==========================================
            else -> listOf(
                Soal(
                    id = 1,
                    pertanyaan = "Contoh soal default untuk mata pelajaran ini...",
                    opsi = listOf("Pilihan A", "Pilihan B", "Pilihan C", "Pilihan D"),
                    jawabanBenar = 0,
                    pembahasan = "Pembahasan soal default."
                )
            )
        }
    }
}