package nearkafe;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.util.Map;

/**
 * GrafikPanel = panel grafik batang penjualan menggunakan JFreeChart.
 * Seluruh tampilan disesuaikan dengan tema NearKafe (dark mode, oranye #C8831A).
 *
 * Pustaka yang digunakan: JFreeChart 1.5.6 (pustaka internal Java).
 */
public class GrafikPanel extends JPanel {

    // Warna tema NearKafe
    private static final Color BG       = new Color(0x2A2929);
    private static final Color SURFACE  = new Color(0x1C1B1B);
    private static final Color PRIMARY  = new Color(0xC8831A);
    private static final Color PRIMARY2 = new Color(0xE8A84A);
    private static final Color TEXT     = new Color(0xFFFFFF);
    private static final Color TEXT2    = new Color(0xB0B0B0);
    private static final Color GRID     = new Color(0x3A3A39);

    public GrafikPanel(LaporanHarian laporan) {
        setLayout(new BorderLayout());
        setBackground(BG);
        setPreferredSize(new Dimension(540, 320));

        Map<String, Integer> data = laporan.getPenjualanMenu();

        if (data.isEmpty()) {
            // Tampilkan pesan kosong jika belum ada transaksi
            JLabel lbl = new JLabel("Belum ada penjualan. Lakukan transaksi dulu.");
            lbl.setForeground(TEXT2);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            add(lbl, BorderLayout.CENTER);
            return;
        }

        // ---- 1. Siapkan dataset JFreeChart ----
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> e : data.entrySet()) {
            // Parameter: value, rowKey, columnKey
            dataset.addValue(e.getValue(), "Porsi Terjual", e.getKey());
        }

        // ---- 2. Buat chart batang ----
        JFreeChart chart = ChartFactory.createBarChart(
                "Grafik Penjualan Menu",   // judul
                "Menu",                    // label sumbu X
                "Porsi Terjual",           // label sumbu Y
                dataset,
                PlotOrientation.VERTICAL,
                false,   // legend
                true,    // tooltip
                false    // url
        );

        // ---- 3. Sesuaikan warna & tema NearKafe ----
        chart.setBackgroundPaint(BG);
        chart.setBorderVisible(false);

        // Judul
        TextTitle title = chart.getTitle();
        title.setFont(new Font("SansSerif", Font.BOLD, 15));
        title.setPaint(TEXT);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(SURFACE);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(GRID);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(false);

        // Renderer batang
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter());   // flat, tanpa gradien
        renderer.setShadowVisible(false);
        renderer.setSeriesPaint(0, PRIMARY);                // batang oranye NearKafe
        renderer.setSeriesOutlinePaint(0, PRIMARY2);
        renderer.setMaximumBarWidth(0.12);                  // batang tidak terlalu lebar

        // Label nilai di atas batang
        renderer.setDefaultItemLabelGenerator(
            new org.jfree.chart.labels.StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelFont(new Font("SansSerif", Font.BOLD, 12));
        renderer.setDefaultItemLabelPaint(PRIMARY2);

        // Sumbu X (nama menu)
        CategoryAxis axisX = plot.getDomainAxis();
        axisX.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        axisX.setTickLabelPaint(TEXT2);
        axisX.setLabelFont(new Font("SansSerif", Font.BOLD, 13));
        axisX.setLabelPaint(TEXT);
        axisX.setAxisLinePaint(GRID);
        axisX.setTickMarkPaint(GRID);

        // Sumbu Y (jumlah porsi)
        NumberAxis axisY = (NumberAxis) plot.getRangeAxis();
        axisY.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        axisY.setTickLabelPaint(TEXT2);
        axisY.setLabelFont(new Font("SansSerif", Font.BOLD, 13));
        axisY.setLabelPaint(TEXT);
        axisY.setAxisLinePaint(GRID);
        axisY.setTickMarkPaint(GRID);
        axisY.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // selalu integer

        // ---- 4. Masukkan chart ke panel ----
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(BG);
        chartPanel.setPopupMenu(null);   // nonaktifkan klik kanan default JFreeChart
        add(chartPanel, BorderLayout.CENTER);
    }
}
