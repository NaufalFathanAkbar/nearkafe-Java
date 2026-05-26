package nearkafe;

/**
 * Kelas Pembayaran = proses bayar untuk sebuah Pesanan.
 * Menghitung kembalian dari uang yang dibayarkan.
 */
public class Pembayaran {

    private Pesanan pesanan;
    private double jumlahBayar;
    private String metode;
    private double kembalian;

    // Constructor: pesanan mana, dibayar berapa, metode apa (parameter).
    public Pembayaran(Pesanan pesanan, double jumlahBayar, String metode) {
        this.pesanan = pesanan;
        this.jumlahBayar = jumlahBayar;
        this.metode = metode;
        this.kembalian = hitungKembalian(jumlahBayar);
    }

    // Fungsi: hitung kembalian = uang bayar (parameter) - total pesanan.
    public double hitungKembalian(double bayar) {
        return bayar - pesanan.hitungTotal();
    }

    // Mengecek apakah uang bayar cukup.
    public boolean cukup() {
        return jumlahBayar >= pesanan.hitungTotal();
    }

    // Getter
    public double getJumlahBayar() { return jumlahBayar; }
    public String getMetode() { return metode; }
    public double getKembalian() { return kembalian; }
    public Pesanan getPesanan() { return pesanan; }
}
