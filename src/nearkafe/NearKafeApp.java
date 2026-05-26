package nearkafe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * NearKafeApp = jendela utama aplikasi (GUI Swing).
 * Berisi layar Login dan layar Kasir (POS) dengan tema NearKafe.
 *
 * Kelas ini menggabungkan semua kelas lain:
 * Kafe, Menu, Kasir, Pesanan, ItemPesanan, Pembayaran, LaporanHarian.
 */
public class NearKafeApp extends JFrame {

    // ===== Tema warna NearKafe (dark mode) =====
    static final Color BG            = new Color(0x0E0E0E);
    static final Color SURFACE       = new Color(0x1C1B1B);
    static final Color CARD          = new Color(0x2A2929);
    static final Color PRIMARY       = new Color(0xC8831A);
    static final Color PRIMARY_LIGHT = new Color(0xE8A84A);
    static final Color TEXT          = new Color(0xFFFFFF);
    static final Color TEXT2         = new Color(0xB0B0B0);
    static final Color BUKA          = new Color(0x4CAF50);
    static final Color HAMPIR        = new Color(0xFF9800);
    static final Color TUTUP         = new Color(0xF44336);
    static final Color GARIS         = new Color(0x3A3A39);

    // ===== Data aplikasi =====
    private final Kafe kafe;
    private final Kasir[] daftarKasir;
    private final LaporanHarian laporan = new LaporanHarian();
    private Pesanan pesananAktif;
    private Kasir kasirLogin;
    private int idPesananBerikut = 1;

    // ===== Komponen GUI =====
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private DefaultTableModel modelPesanan;
    private JLabel lblSubtotal, lblDiskon, lblTotal, lblStatusKafe, lblKasir;
    private JTextField txtBayar, txtPromo;
    private int logoKlik = 0;

    private final NumberFormat rupiah = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));

    public NearKafeApp() {
        rupiah.setMaximumFractionDigits(0);

        // ---- Siapkan akun kasir = 3 anggota kelompok (branding) ----
        daftarKasir = new Kasir[] {
            new Kasir(1, "Naufal Fathan Akbar", "24081010222", "1234"),
            new Kasir(2, "Nur Azizah Dwi Kartini Putri", "24081010161", "1234"),
            new Kasir(3, "Agnesia Betania Saragih", "24081010020", "1234")
        };

        // ---- Siapkan kafe + menu ----
        kafe = new Kafe("NearKafe Corner", "Rungkut, Surabaya", "Buka");
        kafe.tambahMenu(new Menu(1, "Kopi Susu",      "Kopi",     18000, 50));
        kafe.tambahMenu(new Menu(2, "Americano",      "Kopi",     15000, 50));
        kafe.tambahMenu(new Menu(3, "Cappuccino",     "Kopi",     22000, 40));
        kafe.tambahMenu(new Menu(4, "Matcha Latte",   "Non-Kopi", 25000, 30));
        kafe.tambahMenu(new Menu(5, "Coklat Panas",   "Non-Kopi", 20000, 30));
        kafe.tambahMenu(new Menu(6, "Teh Tarik",      "Non-Kopi", 12000, 60));
        kafe.tambahMenu(new Menu(7, "Roti Bakar",     "Makanan",  15000, 25));
        kafe.tambahMenu(new Menu(8, "Kentang Goreng", "Makanan",  17000, 25));

        // ---- Jendela ----
        setTitle("NearKafe POS - Find Space, Create Opportunity");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(980, 660);
        setMinimumSize(new Dimension(880, 600));
        setLocationRelativeTo(null);

        root.add(buatPanelLogin(), "login");
        root.add(buatPanelPOS(), "pos");
        add(root);
        cards.show(root, "login");
    }

    // ============================================================
    //  LAYAR LOGIN
    // ============================================================
    private JPanel buatPanelLogin() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createEmptyBorder(36, 48, 36, 48));

        // Logo (easter egg: klik 5x -> tampil Tentang Kami)
        JLabel logo = lbl("NearKafe", PRIMARY, 34, Font.BOLD);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logo.setToolTipText("Psst... coba klik aku beberapa kali");
        logo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                logoKlik++;
                if (logoKlik >= 5) { logoKlik = 0; tampilTentang(); }
            }
        });

        JLabel tag = lbl("Find Space, Create Opportunity", TEXT2, 13, Font.ITALIC);
        tag.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel judul = lbl("Login Kasir", TEXT, 16, Font.BOLD);
        judul.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] nama = new String[daftarKasir.length];
        for (int i = 0; i < daftarKasir.length; i++) {
            nama[i] = daftarKasir[i].getNama() + "  (" + daftarKasir[i].getNpm() + ")";
        }
        JComboBox<String> combo = new JComboBox<>(nama);
        combo.setMaximumSize(new Dimension(360, 34));
        combo.setAlignmentX(Component.CENTER_ALIGNMENT);
        combo.setBackground(SURFACE);
        combo.setForeground(TEXT);

        JPasswordField pass = new JPasswordField();
        pass.setMaximumSize(new Dimension(360, 34));
        pass.setAlignmentX(Component.CENTER_ALIGNMENT);
        pass.setBackground(SURFACE);
        pass.setForeground(TEXT);
        pass.setCaretColor(TEXT);
        pass.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JButton btn = new JButton("Masuk");
        styleTombol(btn);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(360, 42));
        btn.addActionListener(e -> {
            Kasir k = daftarKasir[combo.getSelectedIndex()];
            if (k.login(new String(pass.getPassword()))) {
                kasirLogin = k;
                mulaiPesananBaru();
                lblKasir.setText("Kasir: " + k.getNama());
                refreshPesanan();
                cards.show(root, "pos");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Password salah. (default: 1234)",
                        "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
            pass.setText("");
        });

        JLabel labKasir = lbl("Pilih akun kasir:", TEXT2, 12, Font.PLAIN);
        labKasir.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel labPass = lbl("Password:", TEXT2, 12, Font.PLAIN);
        labPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel hint = lbl("Password default semua kasir: 1234", TEXT2, 11, Font.PLAIN);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(logo);
        card.add(Box.createVerticalStrut(4));
        card.add(tag);
        card.add(Box.createVerticalStrut(28));
        card.add(judul);
        card.add(Box.createVerticalStrut(18));
        card.add(labKasir);
        card.add(Box.createVerticalStrut(4));
        card.add(combo);
        card.add(Box.createVerticalStrut(14));
        card.add(labPass);
        card.add(Box.createVerticalStrut(4));
        card.add(pass);
        card.add(Box.createVerticalStrut(22));
        card.add(btn);
        card.add(Box.createVerticalStrut(14));
        card.add(hint);

        p.add(card);
        return p;
    }

    // ============================================================
    //  LAYAR KASIR (POS)
    // ============================================================
    private JPanel buatPanelPOS() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG);

        // ---- Bar atas ----
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(SURFACE);
        top.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JPanel kiri = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        kiri.setBackground(SURFACE);
        kiri.add(lbl("NearKafe", PRIMARY, 20, Font.BOLD));
        kiri.add(lbl("POS Owner Panel", TEXT2, 12, Font.PLAIN));

        JPanel kanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 4));
        kanan.setBackground(SURFACE);
        lblStatusKafe = lbl("Buka", BUKA, 13, Font.BOLD);
        lblStatusKafe.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblStatusKafe.setToolTipText("Klik untuk ganti status kafe");
        lblStatusKafe.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { gantiStatusKafe(); }
        });
        lblKasir = lbl("Kasir: -", TEXT, 13, Font.PLAIN);
        JButton btnLogout = new JButton("Logout");
        styleTombolKecil(btnLogout);
        btnLogout.addActionListener(e -> cards.show(root, "login"));
        kanan.add(lblStatusKafe);
        kanan.add(lblKasir);
        kanan.add(btnLogout);

        top.add(kiri, BorderLayout.WEST);
        top.add(kanan, BorderLayout.EAST);

        p.add(top, BorderLayout.NORTH);
        p.add(buatPanelMenu(), BorderLayout.CENTER);
        p.add(buatPanelOrder(), BorderLayout.EAST);
        updateStatusLabel();
        return p;
    }

    // ---- Panel daftar menu (tombol-tombol) ----
    private JPanel buatPanelMenu() {
        JPanel grid = new JPanel(new GridLayout(0, 2, 10, 10));
        grid.setBackground(BG);
        grid.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        for (Menu m : kafe.getDaftarMenu()) {
            JButton b = new JButton("<html><center>" + m.getNama()
                    + "<br><font color='#E8A84A'>Rp " + rupiah.format(m.getHarga())
                    + "</font></center></html>");
            b.setBackground(CARD);
            b.setForeground(TEXT);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createEmptyBorder(18, 8, 18, 8));
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.addActionListener(e -> {
                pesananAktif.tambahItem(m, 1);
                refreshPesanan();
            });
            grid.add(b);
        }

        JScrollPane sc = new JScrollPane(grid);
        sc.getViewport().setBackground(BG);
        sc.setBorder(null);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(BG);
        JLabel h = lbl("  Menu  -  klik untuk menambah ke pesanan", TEXT2, 13, Font.PLAIN);
        h.setBorder(BorderFactory.createEmptyBorder(10, 8, 0, 0));
        wrap.add(h, BorderLayout.NORTH);
        wrap.add(sc, BorderLayout.CENTER);
        return wrap;
    }

    // ---- Panel pesanan + pembayaran ----
    private JPanel buatPanelOrder() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(SURFACE);
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        p.setPreferredSize(new Dimension(380, 0));

        p.add(lbl("Pesanan Saat Ini", TEXT, 15, Font.BOLD), BorderLayout.NORTH);

        modelPesanan = new DefaultTableModel(new String[]{"Menu", "Qty", "Subtotal"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabel = new JTable(modelPesanan);
        tabel.setBackground(CARD);
        tabel.setForeground(TEXT);
        tabel.setGridColor(GARIS);
        tabel.setRowHeight(26);
        tabel.getTableHeader().setBackground(CARD);
        tabel.getTableHeader().setForeground(PRIMARY_LIGHT);
        tabel.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        JScrollPane sc = new JScrollPane(tabel);
        sc.getViewport().setBackground(CARD);
        sc.setBorder(BorderFactory.createLineBorder(GARIS));
        p.add(sc, BorderLayout.CENTER);

        JPanel bawah = new JPanel();
        bawah.setLayout(new BoxLayout(bawah, BoxLayout.Y_AXIS));
        bawah.setBackground(SURFACE);

        // Baris promo (easter egg: ketik NPM anggota / "RUNGKUT")
        JPanel promoRow = new JPanel(new BorderLayout(6, 0));
        promoRow.setBackground(SURFACE);
        promoRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        promoRow.setMaximumSize(new Dimension(9999, 38));
        txtPromo = new JTextField();
        styleFieldInline(txtPromo);
        txtPromo.setToolTipText("Coba ketik kode promo... atau NPM seseorang ;)");
        JButton btnPromo = new JButton("Promo");
        styleTombolKecil(btnPromo);
        btnPromo.addActionListener(e -> terapkanPromo());
        promoRow.add(lbl("Kode: ", TEXT2, 13, Font.PLAIN), BorderLayout.WEST);
        promoRow.add(txtPromo, BorderLayout.CENTER);
        promoRow.add(btnPromo, BorderLayout.EAST);

        lblSubtotal = lbl("Subtotal: Rp 0", TEXT2, 13, Font.PLAIN);
        lblSubtotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDiskon = lbl(" ", PRIMARY_LIGHT, 13, Font.PLAIN);
        lblDiskon.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTotal = lbl("TOTAL: Rp 0", TEXT, 19, Font.BOLD);
        lblTotal.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel bayarRow = new JPanel(new BorderLayout(6, 0));
        bayarRow.setBackground(SURFACE);
        bayarRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        bayarRow.setMaximumSize(new Dimension(9999, 38));
        txtBayar = new JTextField();
        styleFieldInline(txtBayar);
        txtBayar.setToolTipText("Masukkan jumlah uang yang dibayar");
        bayarRow.add(lbl("Bayar: ", TEXT2, 13, Font.PLAIN), BorderLayout.WEST);
        bayarRow.add(txtBayar, BorderLayout.CENTER);

        JButton btnBayar = new JButton("Bayar & Cetak Struk");
        styleTombol(btnBayar);
        btnBayar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnBayar.setMaximumSize(new Dimension(9999, 44));
        btnBayar.addActionListener(e -> prosesBayar());

        JPanel aksiRow = new JPanel(new GridLayout(1, 2, 8, 0));
        aksiRow.setBackground(SURFACE);
        aksiRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        aksiRow.setMaximumSize(new Dimension(9999, 38));
        JButton btnGrafik = new JButton("Grafik Penjualan");
        styleTombolKecil(btnGrafik);
        btnGrafik.addActionListener(e -> tampilGrafik());
        JButton btnReset = new JButton("Batalkan Pesanan");
        styleTombolKecil(btnReset);
        btnReset.addActionListener(e -> {
            mulaiPesananBaru();
            refreshPesanan();
            txtPromo.setText("");
        });
        aksiRow.add(btnGrafik);
        aksiRow.add(btnReset);

        bawah.add(promoRow);
        bawah.add(Box.createVerticalStrut(10));
        bawah.add(lblSubtotal);
        bawah.add(lblDiskon);
        bawah.add(Box.createVerticalStrut(4));
        bawah.add(lblTotal);
        bawah.add(Box.createVerticalStrut(10));
        bawah.add(bayarRow);
        bawah.add(Box.createVerticalStrut(10));
        bawah.add(btnBayar);
        bawah.add(Box.createVerticalStrut(8));
        bawah.add(aksiRow);

        p.add(bawah, BorderLayout.SOUTH);
        return p;
    }

    // ============================================================
    //  LOGIKA / AKSI
    // ============================================================
    private void mulaiPesananBaru() {
        pesananAktif = new Pesanan(idPesananBerikut++, kasirLogin);
    }

    private void refreshPesanan() {
        modelPesanan.setRowCount(0);
        for (ItemPesanan it : pesananAktif.getDaftarItem()) {
            modelPesanan.addRow(new Object[]{
                it.getMenu().getNama(),
                it.getJumlah(),
                "Rp " + rupiah.format(it.getSubtotal())
            });
        }
        lblSubtotal.setText("Subtotal: Rp " + rupiah.format(pesananAktif.hitungSubtotal()));
        if (pesananAktif.getDiskon() > 0) {
            lblDiskon.setText("Diskon: - Rp " + rupiah.format(pesananAktif.getDiskon()));
        } else {
            lblDiskon.setText(" ");
        }
        lblTotal.setText("TOTAL: Rp " + rupiah.format(pesananAktif.hitungTotal()));
    }

    // Easter egg: NPM anggota -> diskon 50%, "RUNGKUT" -> diskon 30%.
    private void terapkanPromo() {
        String kode = txtPromo.getText().trim();

        for (Kasir k : daftarKasir) {
            if (k.getNpm().equals(kode)) {
                pesananAktif.setDiskon(pesananAktif.hitungSubtotal() * 0.5);
                refreshPesanan();
                JOptionPane.showMessageDialog(this,
                        "Wah, ini NPM tim developer NearKafe!\n"
                        + k.getNama() + "\n\n"
                        + "Diskon spesial 50% diaktifkan.\n"
                        + "Terima kasih sudah ngoding sampai subuh :)",
                        "Easter Egg ditemukan!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        if (kode.equalsIgnoreCase("RUNGKUT")) {
            pesananAktif.setDiskon(pesananAktif.hitungSubtotal() * 0.3);
            refreshPesanan();
            JOptionPane.showMessageDialog(this,
                    "Kode warga Rungkut terdeteksi!\n"
                    + "Diskon 30% untuk mahasiswa UPNVJT.",
                    "Promo Rungkut", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Kode promo tidak dikenal.",
                "Promo", JOptionPane.WARNING_MESSAGE);
    }

    private void prosesBayar() {
        if (pesananAktif.kosong()) {
            JOptionPane.showMessageDialog(this, "Pesanan masih kosong.",
                    "Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double bayar;
        try {
            String teks = txtBayar.getText().trim().replace(".", "").replace(",", "");
            bayar = Double.parseDouble(teks);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah bayar yang valid.",
                    "Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pembayaran p = new Pembayaran(pesananAktif, bayar, "Tunai");
        if (!p.cukup()) {
            JOptionPane.showMessageDialog(this,
                    "Uang bayar kurang.\nTotal: Rp " + rupiah.format(pesananAktif.hitungTotal()),
                    "Info", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kurangi stok tiap menu yang terjual
        for (ItemPesanan it : pesananAktif.getDaftarItem()) {
            it.getMenu().kurangiStok(it.getJumlah());
        }

        laporan.tambahPesanan(pesananAktif);
        tampilStruk(p);

        mulaiPesananBaru();
        refreshPesanan();
        txtBayar.setText("");
        txtPromo.setText("");
    }

    private void tampilStruk(Pembayaran p) {
        Pesanan ps = p.getPesanan();
        StringBuilder sb = new StringBuilder();
        sb.append("         N E A R K A F E\n");
        sb.append("   Find Space, Create Opportunity\n");
        sb.append("       ").append(kafe.getAlamat()).append("\n");
        sb.append("--------------------------------\n");
        sb.append("No. Pesanan : ").append(ps.getIdPesanan()).append("\n");
        sb.append("Kasir       : ").append(ps.getKasir().getNama()).append("\n");
        sb.append("--------------------------------\n");
        for (ItemPesanan it : ps.getDaftarItem()) {
            sb.append(String.format("%-18s %2dx%n", it.getMenu().getNama(), it.getJumlah()));
            sb.append(String.format("%24s%n", "Rp " + rupiah.format(it.getSubtotal())));
        }
        sb.append("--------------------------------\n");
        sb.append(String.format("Subtotal    : Rp %s%n", rupiah.format(ps.hitungSubtotal())));
        if (ps.getDiskon() > 0) {
            sb.append(String.format("Diskon      : Rp %s%n", rupiah.format(ps.getDiskon())));
        }
        sb.append(String.format("TOTAL       : Rp %s%n", rupiah.format(ps.hitungTotal())));
        sb.append(String.format("Bayar       : Rp %s%n", rupiah.format(p.getJumlahBayar())));
        sb.append(String.format("Kembalian   : Rp %s%n", rupiah.format(p.getKembalian())));
        sb.append("--------------------------------\n");
        sb.append("   Terima kasih, sampai jumpa!\n");
        sb.append("  Powered by NearKafe - Kelompok\n");
        sb.append("  Naufal, Nur Azizah & Agnesia\n");
        sb.append("         UPNVJT 2026\n");

        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ta.setBackground(Color.WHITE);
        ta.setForeground(Color.BLACK);
        ta.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JOptionPane.showMessageDialog(this, ta, "Struk Pembayaran", JOptionPane.PLAIN_MESSAGE);
    }

    private void tampilGrafik() {
        GrafikPanel gp = new GrafikPanel(laporan);
        JPanel info = new JPanel(new BorderLayout());
        info.setBackground(CARD);
        info.add(gp, BorderLayout.CENTER);
        JLabel lblInfo = new JLabel("Total pendapatan: Rp " + rupiah.format(laporan.getTotalPendapatan())
                + "   |   Transaksi: " + laporan.getJumlahTransaksi()
                + "   |   Terlaris: " + laporan.menuTerlaris());
        lblInfo.setForeground(TEXT2);
        lblInfo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        info.add(lblInfo, BorderLayout.SOUTH);
        JOptionPane.showMessageDialog(this, info, "Grafik Penjualan Harian", JOptionPane.PLAIN_MESSAGE);
    }

    private void tampilTentang() {
        String pesan = "NearKafe POS - Owner Panel\n\n"
                + "Dibuat dengan kopi (dan sedikit panik) oleh:\n"
                + "  - Naufal Fathan Akbar  (24081010222)\n"
                + "  - Nur Azizah Dwi Kartini Putri  (24081010161)\n"
                + "  - Agnesia Betania Saragih  (24081010020)\n\n"
                + "Program Studi Informatika - UPNVJT 2026\n"
                + "\"Find Space, Create Opportunity\"\n\n"
                + "(Selamat, kamu menemukan easter egg!)";
        JOptionPane.showMessageDialog(this, pesan, "Tentang Kami", JOptionPane.INFORMATION_MESSAGE);
    }

    private void gantiStatusKafe() {
        String s = kafe.getStatus();
        String baru;
        if (s.equals("Buka")) {
            baru = "Hampir Tutup";
        } else if (s.equals("Hampir Tutup")) {
            baru = "Tutup";
        } else {
            baru = "Buka";
        }
        kafe.gantiStatus(baru);
        updateStatusLabel();
    }

    private void updateStatusLabel() {
        String s = kafe.getStatus();
        lblStatusKafe.setText(s);
        if (s.equals("Buka")) {
            lblStatusKafe.setForeground(BUKA);
        } else if (s.equals("Hampir Tutup")) {
            lblStatusKafe.setForeground(HAMPIR);
        } else {
            lblStatusKafe.setForeground(TUTUP);
        }
    }

    // ============================================================
    //  HELPER tampilan
    // ============================================================
    private JLabel lbl(String teks, Color warna, int ukuran, int gaya) {
        JLabel l = new JLabel(teks);
        l.setForeground(warna);
        l.setFont(new Font("SansSerif", gaya, ukuran));
        return l;
    }

    private void styleTombol(JButton b) {
        b.setBackground(PRIMARY);
        b.setForeground(new Color(0x1C1B1B));
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleTombolKecil(JButton b) {
        b.setBackground(CARD);
        b.setForeground(PRIMARY_LIGHT);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleFieldInline(JTextField f) {
        f.setBackground(CARD);
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
    }

    // ============================================================
    //  MAIN - titik mulai program
    // ============================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NearKafeApp().setVisible(true));
    }
}
