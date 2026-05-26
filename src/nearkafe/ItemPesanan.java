package nearkafe;

/**
 * Kelas ItemPesanan = satu baris dalam pesanan.
 * Contoh: "Kopi Susu x2". Menggabungkan sebuah Menu dengan jumlahnya.
 */
public class ItemPesanan {

    private Menu menu;
    private int jumlah;
    private double subtotal;

    // Constructor: terima menu apa dan berapa banyak (parameter).
    public ItemPesanan(Menu menu, int jumlah) {
        this.menu = menu;
        this.jumlah = jumlah;
        this.subtotal = hitungSubtotal();   // langsung dihitung saat dibuat
    }

    // Fungsi: hitung subtotal = harga x jumlah.
    public double hitungSubtotal() {
        return menu.getHarga() * jumlah;
    }

    // Menambah jumlah jika menu yang sama dipesan lagi (parameter: n).
    public void tambahJumlah(int n) {
        this.jumlah = this.jumlah + n;
        this.subtotal = hitungSubtotal();
    }

    // Getter
    public Menu getMenu() { return menu; }
    public int getJumlah() { return jumlah; }
    public double getSubtotal() { return subtotal; }
}
