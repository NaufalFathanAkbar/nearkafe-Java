package nearkafe;

import java.util.ArrayList;
import java.util.List;

/**
 * Kelas Pesanan = satu transaksi.
 * Satu pesanan berisi banyak ItemPesanan dan dilayani oleh satu Kasir.
 */
public class Pesanan {

    private int idPesanan;
    private List<ItemPesanan> daftarItem;
    private Kasir kasir;
    private double diskon;          // potongan harga dalam rupiah

    // Constructor: id pesanan dan kasir yang melayani (parameter).
    public Pesanan(int idPesanan, Kasir kasir) {
        this.idPesanan = idPesanan;
        this.kasir = kasir;
        this.daftarItem = new ArrayList<>();
        this.diskon = 0;
    }

    // Menambah item. Bila menu sama sudah ada, jumlahnya ditambah.
    // Parameter: menu apa dan berapa jumlahnya.
    public void tambahItem(Menu menu, int jumlah) {
        for (ItemPesanan item : daftarItem) {
            if (item.getMenu().getId() == menu.getId()) {
                item.tambahJumlah(jumlah);
                return;
            }
        }
        daftarItem.add(new ItemPesanan(menu, jumlah));
    }

    // Fungsi: total sebelum diskon.
    public double hitungSubtotal() {
        double total = 0;
        for (ItemPesanan item : daftarItem) {
            total = total + item.getSubtotal();
        }
        return total;
    }

    // Fungsi: total akhir = subtotal - diskon (tidak boleh minus).
    public double hitungTotal() {
        double total = hitungSubtotal() - diskon;
        if (total < 0) {
            total = 0;
        }
        return total;
    }

    public void setDiskon(double diskon) { this.diskon = diskon; }

    public boolean kosong() { return daftarItem.isEmpty(); }

    // Getter
    public int getIdPesanan() { return idPesanan; }
    public List<ItemPesanan> getDaftarItem() { return daftarItem; }
    public Kasir getKasir() { return kasir; }
    public double getDiskon() { return diskon; }
}
