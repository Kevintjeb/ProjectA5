package agenda;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Linking extends JPanel {
	Planner planner;
	BufferedImage image;
	Font font = new Font("SANS_SERIF", Font.PLAIN, 14);
	HashMap<Stage, Integer> map = new HashMap<Stage, Integer>();

	ArrayList<Rectangle2D> stages = new ArrayList<Rectangle2D>();
	ArrayList<Rectangle2D> numbers = new ArrayList<Rectangle2D>();

	Rectangle2D rectSelected;
	Rectangle2D rectNumSelected;
	
	ArrayList<ArrayList<Rectangle2D>> lines = new ArrayList<ArrayList<Rectangle2D>>();

	Stage selectedStage;
	int selectedNumber;

	int clickedStage = -1;
	int clickedNumber = -1;
	
	Rectangle2D runButton;
	JTextField input;
	File json;

	public Linking(Planner planner, File json) {
		super(null);
		this.planner = planner;
		this.json = json;
		setPreferredSize(new Dimension(400, 550));
		clicked();
		input = new JTextField();
		input.setBounds(220, 560, 30, 20);
		this.add(input);
		
		JLabel label = new JLabel("Aantal bezoekers :");
		label.setBounds(130, 560, 100, 20);
		this.add(label);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		int y = 10;
		int y2 = 10;
		if (!stages.isEmpty()) {
			stages.clear();
		}
		if (!numbers.isEmpty()) {
			numbers.clear();
		}

		for (int stage = 0; stage < planner.agenda.getStages().size(); stage++) {

			if (stage == clickedStage) {
				g2d.setColor(new Color(25, 160, 95, 255));
				System.out.println(stage);
			} else
				g2d.setColor(new Color(25, 160, 95, 128));

			Rectangle2D rect = new Rectangle2D.Double(10, y, 100, 50);
			g2d.fill(rect);

			stages.add(rect);

			int stringLen = (int) g2d.getFontMetrics()
					.getStringBounds(planner.agenda.getStages().get(stage).getName(), g2d).getWidth();
			int stringHei = (int) g2d.getFontMetrics()
					.getStringBounds(planner.agenda.getStages().get(stage).getName(), g2d).getHeight();

			int xPos = (int) 100 / 2 - stringLen / 2;
			int yPos = (int) 50 / 2 + stringHei / 4;

			g2d.setColor(Color.BLACK);
			g2d.drawString(planner.agenda.getStages().get(stage).getName(), xPos + 10, (yPos + y));

			y += 60;
			g2d.setColor(new Color(25, 160, 95, 128));
		}

		int count = 0;
		for (int i = 5; i <= 28; i = i + 3) {
			if (count == clickedNumber) {
				g2d.setColor(new Color(225, 230, 150, 255));
				System.out.println(i);
			} else
				g2d.setColor(new Color(225, 230, 150, 128));
			count++;

			Rectangle2D rect = new Rectangle2D.Double(290, y2, 100, 50);
			g2d.fill(rect);

			numbers.add(rect);

			int stringLen = (int) g2d.getFontMetrics().getStringBounds("" + i, g2d).getWidth();
			int stringHei = (int) g2d.getFontMetrics().getStringBounds("" + i, g2d).getHeight();

			int xPos = (int) 100 / 2 - stringLen / 2;
			int yPos = (int) 50 / 2 + stringHei / 4;

			g2d.setColor(Color.BLACK);
			g2d.drawString("" + count, xPos + 290, (yPos + y2));

			y2 += 60;
			g2d.setColor(new Color(225, 230, 150, 128));
		}

		// --------------------------------------------------------------------------
		g2d.setColor(new Color(121, 121, 121));
		runButton = new Rectangle2D.Double(145, 500, 110, 50);
		g2d.fill(runButton);

		int stringLenPotty = (int) g2d.getFontMetrics().getStringBounds("Run", g2d).getWidth();
		int stringHeiPotty = (int) g2d.getFontMetrics().getStringBounds("Run", g2d).getHeight();

		int xPosPotty = (int) 75 / 2 - stringLenPotty / 2;
		int yPosPotty = (int) 50 / 2 + stringHeiPotty / 4;

		g2d.setColor(Color.WHITE);
		g2d.drawString("Run", xPosPotty + 162, (yPosPotty + 500));

		//if (drawLine) {
		if(!lines.isEmpty()){
		for(ArrayList<Rectangle2D> line: lines)
		{
			int lx = (int) line.get(0).getX() + 100;
			int ly = (int) line.get(0).getY() + 25;
			int rx = (int) line.get(1).getX();
			int ry = (int) line.get(1).getY() + 25;

			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(5));
			g2d.drawLine(lx, ly, rx, ry);
		}
		}
		try {
			image = ImageIO.read(new File("maps\\MapImage.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g2d.drawImage(image, null, 500, 0);
		//}
	}

	public void clicked() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				for (int i = 0; i < stages.size(); i++) {
					if (stages.get(i).contains(e.getPoint())) {
						System.out.println("selected stage " + i);
						selectedStage = planner.agenda.getStages().get(i);
						rectSelected = stages.get(i);
						clickedStage = i;
						repaint();
					}

				}

				for (int i = 0; i < numbers.size(); i++) {
					if (numbers.get(i).contains(e.getPoint())) {
						selectedNumber = 5 + (i * 3);
						rectNumSelected = numbers.get(i);
						clickedNumber = i;
						if(!lines.isEmpty()){
							for(int y = lines.size()-1; y > 0; y--)
							{
								System.out.println(lines.size());
							if(lines.get(y).get(0).equals(rectSelected))
								lines.remove(lines.get(y));							
							}
						}
						ArrayList<Rectangle2D> line = new ArrayList<Rectangle2D>();
						line.add(rectSelected);
						line.add(rectNumSelected);
						lines.add(line);
						

						map.put(selectedStage, selectedNumber);
						System.out.println(map.entrySet());
						repaint();
					}

				}
			}
		});

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (runButton.contains(e.getPoint())) {
					planner.tabbedPane.removeTabAt(2);
					//TODO PARSE TO INT
					planner.tabbedPane.addTab("Simulatie", new Simulator(json, planner, map,Integer.parseInt(input.getText())));

					planner.repaint();
					planner.revalidate();
				}
			}
		});
	}

	public HashMap<Stage, Integer> getLinkMap() {
		return map;
	}
}
