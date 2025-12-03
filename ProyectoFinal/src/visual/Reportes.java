package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logic.Consulta;
import logic.HistorialClinico;
import logic.Hospital;
import logic.Medico;
import logic.Paciente;

public class Reportes extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Reportes frame = new Reportes();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Reportes() {
		setTitle("Reportes del hospital");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(900, 550);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// Calcula los valores y agrega el panel de gráfica
		int[] valores = calcularValores();
		String[] etiquetas = new String[] {
				"Pacientes",
				"Enfermedades",
				"Enf. vigiladas",
				"Vacunas",
				"Historiales",
				"Hist. vigilados",
				"Hist. importantes",
				"Médicos"
		};

		BarChartPanel grafica = new BarChartPanel(etiquetas, valores);
		grafica.setPreferredSize(new Dimension(800, 450));
		contentPane.add(grafica, BorderLayout.CENTER);
	}

	/**
	 * Devuelve los valores estadísticos a mostrar.
	 */
	private int[] calcularValores() {

		Hospital h = Hospital.getInstancia();

		int cantPacientes = h.getMisPacientes().size();
		int cantEnfermedades = h.getMisEnfermedades().size();
		int cantEnfermedadesVigiladas = h.getEnfermedadesVigiladas().size();
		int cantVacunas = h.getControlVacunas().size();
		int cantMedicos = 0;
		for(Medico m: h.getMisMedicos()) {
			if(m.getDisponibilidad()) {
				cantMedicos++;
			}
		}

		int cantHistoriales = 0;
		int cantHistorialesVigilados = 0;
		int cantHistorialesImportantes = 0;

		for (Paciente p : h.getMisPacientes()) {

			HistorialClinico historial = p.getHistorial();

			if (historial != null && historial.getMisConsultas() != null
					&& !historial.getMisConsultas().isEmpty()) {

				cantHistoriales++;

				boolean vigilado = false;
				boolean importante = false;

				for (Consulta c : historial.getMisConsultas()) {

					if (c.esVigilada()) {
						vigilado = true;
					}

					if (c.esImportante()) {
						importante = true;
					}
				}

				if (vigilado) {
					cantHistorialesVigilados++;
				}

				if (importante) {
					cantHistorialesImportantes++;
				}
			}
		}

		return new int[] {
				cantPacientes,
				cantEnfermedades,
				cantEnfermedadesVigiladas,
				cantVacunas,
				cantHistoriales,
				cantHistorialesVigilados,
				cantHistorialesImportantes,
				cantMedicos
		};
	}

	/**
	 * Panel interno que dibuja la gráfica de barras.
	 */
	private static class BarChartPanel extends JPanel {

		private final String[] labels;
		private final int[] values;
		private final int maxValue;

		public BarChartPanel(String[] labels, int[] values) {
			this.labels = labels;
			this.values = values;
			int max = 0;
			for (int v : values) {
				if (v > max) {
					max = v;
				}
			}
			this.maxValue = max == 0 ? 1 : max; // evita división entre 0
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			int width = getWidth();
			int height = getHeight();

			int padding = 40;
			int labelPadding = 80;

			int availableWidth = width - 2 * padding;
			int availableHeight = height - 2 * padding - labelPadding;

			int n = values.length;
			if (n == 0) {
				return;
			}

			int barWidth = availableWidth / n;

			// Ejes
			int ejeYFin = height - padding - labelPadding;
			g2.setColor(Color.BLACK);
			g2.drawLine(padding, ejeYFin, padding + availableWidth, ejeYFin); // eje X
			g2.drawLine(padding, padding, padding, ejeYFin); // eje Y

			// Líneas de referencia horizontales y valores aproximados
			int lineas = 5;
			for (int i = 0; i <= lineas; i++) {
				int y = padding + i * availableHeight / lineas;
				g2.setColor(new Color(220, 220, 220));
				g2.drawLine(padding, y, padding + availableWidth, y);

				int valorReferencia = (int) Math.round(maxValue - (maxValue * (i / (double) lineas)));
				g2.setColor(Color.DARK_GRAY);
				g2.drawString(String.valueOf(valorReferencia), 5, y + 5);
			}

			// Barras
			for (int i = 0; i < n; i++) {

				int v = values[i];
				double ratio = v / (double) maxValue;
				int barHeight = (int) (ratio * availableHeight);

				int x = padding + i * barWidth + barWidth / 10;
				int y = ejeYFin - barHeight;

				g2.setColor(new Color(100, 149, 237));
				g2.fillRect(x, y, barWidth - barWidth / 5, barHeight);

				g2.setColor(Color.BLACK);
				g2.drawRect(x, y, barWidth - barWidth / 5, barHeight);

				// Valor encima de la barra
				String valorTexto = String.valueOf(v);
				FontMetrics fm = g2.getFontMetrics();
				int valorAncho = fm.stringWidth(valorTexto);
				g2.drawString(valorTexto, x + (barWidth - barWidth / 5 - valorAncho) / 2, y - 5);

				// Etiquetas debajo (separadas por espacios en varias líneas)
				String etiqueta = labels[i];
				String[] partes = etiqueta.split(" ");
				int labelX = x + (barWidth - barWidth / 5) / 2;
				int labelY = ejeYFin + fm.getHeight() + 5;

				for (String parte : partes) {
					int wTexto = fm.stringWidth(parte);
					g2.drawString(parte, labelX - wTexto / 2, labelY);
					labelY += fm.getHeight();
				}
			}
		}
	}
}
