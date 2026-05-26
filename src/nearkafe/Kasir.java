package nearkafe;

/**
 * Kelas Kasir = pegawai yang login dan melayani transaksi.
 * Di aplikasi ini, ketiga anggota kelompok menjadi akun kasir
 * (lengkap dengan NPM) -> ini bagian "branding" keunikan kelompok.
 */
public class Kasir {

    private int id;
    private String nama;
    private String npm;
    private String password;

    // Constructor
    public Kasir(int id, String nama, String npm, String password) {
        this.id = id;
        this.nama = nama;
        this.npm = npm;
        this.password = password;
    }

    // Fungsi login: bandingkan password yang dimasukkan (parameter)
    // dengan password tersimpan. Mengembalikan true bila cocok.
    public boolean login(String passwordInput) {
        return this.password.equals(passwordInput);
    }

    // Getter
    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getNpm() { return npm; }
}
