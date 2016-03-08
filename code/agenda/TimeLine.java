package agenda;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class TimeLine extends JPanel {

	private static final long serialVersionUID = -7331684631070766242L;
	ArrayList<Rectangle2D> rectangles = new ArrayList<Rectangle2D>();
	Planner planner;
	int teller = 0;
	Timer updatetimer = new Timer(1000, new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			repaint();		
		}
	});
	Color blockColor = new Color(25, 160, 95, 128);
	Color textColor = new Color(85, 85, 85);
	Color lineColor = new Color(121, 121, 121);
	Color backgroundColor = new Color(255, 230, 150, 128);

	public TimeLine(Planner planner) {
		super(null);
		this.planner = planner;
		updatetimer.start();
		setPreferredSize(new Dimension(-1, 1100));

		JButton edit = new JButton("Edit");

		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Window w = SwingUtilities.getWindowAncestor(edit);
				if (w != null) {
					w.dispose();
				}
				new EditorFrame(planner);
			}
		});

		addMouseListener(new MouseAdapter() {



			@Override
			public void mousePressed(MouseEvent e) {

				for (int i = 0; i < rectangles.size(); i++) {
					if (i < rectangles.size()) {

						if (rectangles.get(i).contains(e.getPoint())) {

							String info = "Artiest(en) : " + checkNamen(planner.agenda.performances.get(i))
									+ "\nStage : " + planner.agenda.performances.get(i).getStage().getName()
									+ "\nStart Tijd : " + planner.agenda.performances.get(i).getStartTime()
									+ "\nEind Tijd : " + planner.agenda.performances.get(i).getEndTime();

							Object Msg[] = { "OK", edit };

							JOptionPane.showOptionDialog(null, info, "Performance info", JOptionPane.YES_NO_OPTION,
									JOptionPane.INFORMATION_MESSAGE, null, Msg, Msg[0]);

						}
					}
				}
			}



		});
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		// top balk met rood aanmaken
		g2d.setColor(new Color(0xFFFFFFF)); // 'wit'
		g2d.setFont(new Font("Arial", Font.BOLD, 15));
		g2d.fillRect(0, 0, getWidth(), 35);

		// kleur switchen en grijs/witte balken maken.

		for (int i = 0; i <= 15; i++) {
			g2d.setColor(backgroundColor); // geel
			g2d.fillRect(0, (35 + (i * (getHeight() + 35) / 16)), getWidth(), (getHeight() + 35) / 16);
			i++;
			g2d.setColor(new Color(0xFFFFFFF)); // 'wit'
			g2d.fillRect(0, (35 + (i * (getHeight() + 35) / 16)), getWidth(), (getHeight() + 35) / 16);
		}
		// TODO lettertype met derrivefont()

		// dikte + kleur setten.
		g2d.setColor(textColor);
		g2d.setStroke(new BasicStroke(3));

		// eerste verticale lijn + laatste verticale + bovenste lijn witte blok
		// g2d.drawLine(25, 25, 25, 460);

		// g2d.drawLine(getWidth() / (planner.agenda.getStages().size() + 1) *
		// (planner.agenda.getStages().size() + 1), 25,
		// getWidth() / (planner.agenda.getStages().size() + 1) *
		// (planner.agenda.getStages().size() + 1), 460);
		//
		// g2d.drawLine(25, 25, getWidth() / (planner.agenda.getStages().size()
		// + 1) * (planner.agenda.getStages().size() + 1), 25);

		// eerste verticale lijn na tijd.
		g2d.drawLine(getWidth() / (planner.agenda.getStages().size() + 1), 0,
				getWidth() / (planner.agenda.getStages().size() + 1), getHeight());

		// Bovenste + onderste lijn van witte blok
		g2d.drawLine(0, 35 - 2, getWidth(), 35 - 2);
		// g2d.drawLine(0, 460, getWidth(), 460);

		// dikte resetten naar 1.
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(lineColor);
		// tussen lijnen podia.
		for (int i = 0; i < (planner.agenda.getStages().size() - 1); i++) {
			g2d.drawLine(getWidth() / (planner.agenda.getStages().size() + 1) * (i + 2), 0,
					getWidth() / (planner.agenda.getStages().size() + 1) * (i + 2), getHeight());
		}

		// tussenlijnen met forloop. GOED
		for (int i = 1; i <= 15; i++) {
			g2d.drawLine(0, 35 + (i * (getHeight() + 35) / 16), getWidth(), 35 + (i * (getHeight() + 35) / 16));
		}
		g2d.setColor(textColor);
		// tekst van tijd, GOED
		for (int i = 0; i <= 15; i++) {
			g2d.drawString((i + 9) + ":00", getWidth() / ((planner.agenda.getStages().size() + 1) * 2) - 20,
					35 + ((i + 1) * ((getHeight() + 35) / 16) - (getHeight() + 35) / 40));
		}
		// podia tekst toevoegen, TODO for loop maken van data met arraylists.
		for (int i = 0; i < planner.agenda.getStages().size(); i++) {
			g2d.drawString(planner.agenda.getStages().get(i).getName(),
					getWidth() / ((planner.agenda.getStages().size() + 1) * 2) * (i + (3 + i))
							- (g.getFontMetrics().stringWidth(planner.agenda.getStages().get(i).getName()) / 2),
					22);
		}
		g2d.drawString("Time", getWidth() / ((planner.agenda.getStages().size() + 1) * 2)
				- (g.getFontMetrics().stringWidth("Time") / 2), 22);

		addButton(g);

	}

	public void addButton(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		if (!(rectangles.isEmpty())) {
			rectangles.clear();
		}
		if (planner.agenda.getPerformances().size() > 0) {
			for (int i = 0; i < planner.agenda.getPerformances().size(); i++) {

				Performance performance = planner.agenda.getPerformances().get(i);

				String namen = new String("");
				String tijd = new String(
						performance.getStartTime().toString() + " - " + performance.getEndTime().toString());

				namen = checkNamen(performance);

				double width = getWidth() / (planner.agenda.getStages().size() + 1) - 25;

				double height = ((((performance.getEndTime().getHours() * 60.0) + performance.getEndTime().getMinutes())
						- ((performance.getStartTime().getHours() * 60.0) + performance.getStartTime().getMinutes()))
						* (((getHeight() + 35) / 16.0) / 60.0));

				double x = ((getWidth() / (planner.agenda.getStages().size() + 1)
						* (planner.agenda.getStages().indexOf(performance.getStage()) + 1)) + 12.5f);
				double y = (((performance.getStartTime().getHours() * ((getHeight() + 35) / 16.0))
						- (((getHeight() + 35) / 16.0) * 9) + 35)
						+ ((performance.getStartTime().getMinutes() / 60.0f) * ((getHeight() + 35) / 16.0)));
				// System.out.println("x: " + x + " y: " + y);

				Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);

				rectangles.add(rect);
				g2d.setColor(blockColor);
				g2d.fill(rect);
				g2d.setFont(new Font("Arial", Font.TRUETYPE_FONT, 13));
				g2d.setColor(textColor);

				g2d.draw(rect);

				int stringLen = (int) g2d.getFontMetrics().getStringBounds(namen, g2d).getWidth();
				int stringHei = (int) g2d.getFontMetrics().getStringBounds(namen, g2d).getHeight();
				// System.out.println(stringHei);
				int xPos = (int) width / 2 - stringLen / 2;
				int yPos = (int) height / 2 + stringHei / 4;
				g2d.drawString(namen, xPos + (int) x, (yPos + (int) y));
				g2d.setFont(new Font("Arial", Font.TRUETYPE_FONT, 10));
				g2d.drawString(tijd, (int) x, (int) y + (int) height);

				// System.out.println("naam : " + namen);

			}
			// Einde methode

		}

	}

	public String checkNamen(Performance p) {
		String namen = "";
		if (p.getArtists().size() > 1) {
			int teller = 1;
			for (int y = 0; y < p.getArtists().size(); y++) {
				if (teller == p.getArtists().size()) {
					namen = namen + p.getArtists().get(y).getName();
				} else {
					teller++;
					namen = namen + p.getArtists().get(y).getName() + ", ";
				}
			}
		} else if (p.getArtists().size() == 1){
			namen = p.getArtists().get(0).getName();
		}
		return namen;
	}

}
