# NearKafe POS — Versi Java (NetBeans)

Aplikasi kasir (Point of Sale) untuk **Owner Panel NearKafe**.
Tugas Pengganti EAS — Pemrograman Berorientasi Objek (PBO).

Kelompok — Informatika UPNVJT 2026:
- Naufal Fathan Akbar (24081010222)
- Nur Azizah Dwi Kartini Putri (24081010161)
- Agnesia Betania Saragih (24081010020)

---

## Isi (7 kelas — syarat minimal 5 terpenuhi)

| File | Peran |
|------|-------|
| `Menu.java` | Item menu (nama, harga, stok) |
| `Kafe.java` | Data kafe + daftar menu + status Buka/Tutup |
| `ItemPesanan.java` | Satu baris pesanan (menu + jumlah) |
| `Pesanan.java` | Satu transaksi (banyak item + diskon) |
| `Kasir.java` | Akun kasir = anggota kelompok (login) |
| `Pembayaran.java` | Hitung kembalian |
| `LaporanHarian.java` | Rekap penjualan untuk grafik |
| `GrafikPanel.java` | Grafik batang penjualan (digambar manual) |
| `NearKafeApp.java` | GUI utama + `main()` |

Semua menggunakan **pustaka internal Java** (Swing, AWT) — tidak perlu library tambahan.

---

## Cara menjalankan di NetBeans

1. Buka NetBeans → **File > New Project > Java with Ant > Java Application**.
2. Beri nama project: `NearKafePOS`. **Hapus centang** "Create Main Class".
3. Di panel Projects, klik kanan folder **Source Packages** → **New > Java Package**,
   beri nama: `nearkafe`.
4. Salin **semua file `.java`** dari folder `src/nearkafe/` ke dalam package `nearkafe` tadi
   (drag-and-drop atau copy-paste file).
5. Klik kanan project → **Properties > Run** → set **Main Class** ke `nearkafe.NearKafeApp`.
6. Tekan **F6** (Run). Aplikasi akan terbuka di layar Login.

> Login: pilih nama kasir, password default semua kasir = **1234**.

---

## Cara menjalankan tanpa NetBeans (lewat terminal)

```bash
cd NearKafe-Java
javac -d build src/nearkafe/*.java
java -cp build nearkafe.NearKafeApp
```

---

## Fitur & keunikan

- **Tema NearKafe** (dark mode, oranye #C8831A, tagline asli).
- **Branding**: 3 anggota jadi akun kasir (lengkap NPM); nama tim ada di struk & menu "Tentang Kami".
- **Status Live Kafe** (Buka / Hampir Tutup / Tutup) — klik label status di pojok kanan atas.
- **Grafik penjualan** (tombol "Grafik Penjualan") — digambar sendiri, tanpa library.
- **Struk otomatis** berkop NearKafe + footer nama kelompok.

### Easter egg (untuk presentasi)
1. Di layar Login, **klik logo "NearKafe" 5x** → muncul "Tentang Kami".
2. Di kolom **Kode promo**, ketik **NPM** salah satu anggota → diskon 50% + pesan rahasia.
3. Ketik **RUNGKUT** di kolom promo → diskon 30% (ode ke kawasan Rungkut).

---

## Konsep OOP yang ditunjukkan ke dosen

- **Constructor**: lihat `public Menu(int id, String nama, ...)` di `Menu.java`.
- **Fungsi (method)**: `hitungTotal()` di `Pesanan.java`, `hitungKembalian()` di `Pembayaran.java`.
- **Parameter**: `tambahItem(Menu menu, int jumlah)` di `Pesanan.java`.

---

## Cara menambahkan JFreeChart di NetBeans

1. Klik kanan project → **Properties > Libraries > Classpath > + (Add JAR/Folder)**.
2. Pilih file `lib/jfreechart-1.5.6.jar` dari folder project.
3. Klik **OK** → Build & Run seperti biasa.

> **Tanpa langkah ini**, grafik tidak akan muncul (error saat klik "Grafik Penjualan").
