package nearkafe;

/**
 * Kelas Menu = satu item menu di kafe (mis. Kopi Susu).
 * Menyimpan data dasar menu beserta stoknya.
 */
public class Menu {

    // ---- Atribut (dibuat private = enkapsulasi) ----
    private int id;
    private String nama;
    private String kategori;
    private double harga;
    private int stok;

    // ---- Constructor: "cetakan awal" saat objek Menu dibuat ----
    // Semua nilai awal dikirim lewat parameter.
    public Menu(int id, String nama, String kategori, double harga, int stok) {
        this.id = id;
        this.nama = nama;
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
    }

    // ---- Fungsi (method) ----

    // Mengecek apakah menu masih tersedia (stok > 0).
    public boolean tersedia() {
        return stok > 0;
    }

    // Mengurangi stok sebanyak "jumlah" (parameter).
    public void kurangiStok(int jumlah) {
        if (jumlah <= stok) {
            stok = stok - jumlah;
        }
    }

    // ---- Getter (untuk membaca atribut dari luar) ----
    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getKategori() { return kategori; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }
}
