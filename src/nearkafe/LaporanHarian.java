package nearkafe;

import java.util.HashMap;
import java.util.Map;

/**
 * Kelas LaporanHarian = rekap penjualan dalam sehari.
 * Mengumpulkan semua Pesanan menjadi data untuk grafik penjualan.
 */
public class LaporanHarian {

    private double totalPendapatan;
    private int jumlahTransaksi;
    private Map<String, Integer> penjualanMenu;   // nama menu -> jumlah terjual

    // Constructor
    public LaporanHarian() {
        this.totalPendapatan = 0;
        this.jumlahTransaksi = 0;
        this.penjualanMenu = new HashMap<>();
    }

    // Menambahkan satu pesanan ke rekap (parameter: objek Pesanan).
    public void tambahPesanan(Pesanan p) {
        totalPendapatan = totalPendapatan + p.hitungTotal();
        jumlahTransaksi = jumlahTransaksi + 1;
        for (ItemPesanan item : p.getDaftarItem()) {
            String nama = item.getMenu().getNama();
            int lama = penjualanMenu.getOrDefault(nama, 0);
            penjualanMenu.put(nama, lama + item.getJumlah());
        }
    }

    // Fungsi: cari menu dengan penjualan terbanyak.
    public String menuTerlaris() {
        String terlaris = "-";
        int max = 0;
        for (Map.Entry<String, Integer> e : penjualanMenu.entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                terlaris = e.getKey();
            }
        }
        return terlaris;
    }

    // Getter
    public double getTotalPendapatan() { return totalPendapatan; }
    public int getJumlahTransaksi() { return jumlahTransaksi; }
    public Map<String, Integer> getPenjualanMenu() { return penjualanMenu; }
}
