package nearkafe;

import java.util.ArrayList;
import java.util.List;

/**
 * Kelas Kafe = kafe yang dikelola owner.
 * Punya daftar menu dan status (Buka / Hampir Tutup / Tutup),
 * meniru fitur "Status Live Cafe" di aplikasi NearKafe.
 */
public class Kafe {

    private String nama;
    private String alamat;
    private String status;            // "Buka", "Hampir Tutup", "Tutup"
    private List<Menu> daftarMenu;    // satu kafe punya banyak Menu

    // Constructor
    public Kafe(String nama, String alamat, String status) {
        this.nama = nama;
        this.alamat = alamat;
        this.status = status;
        this.daftarMenu = new ArrayList<>();
    }

    // Menambah menu baru ke kafe (parameter: objek Menu).
    public void tambahMenu(Menu m) {
        daftarMenu.add(m);
    }

    // Mengganti status kafe (parameter: status baru).
    public void gantiStatus(String statusBaru) {
        this.status = statusBaru;
    }

    // Getter
    public String getNama() { return nama; }
    public String getAlamat() { return alamat; }
    public String getStatus() { return status; }
    public List<Menu> getDaftarMenu() { return daftarMenu; }
}
