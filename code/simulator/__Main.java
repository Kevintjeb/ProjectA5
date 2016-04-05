package simulator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import agenda.Artist;
import agenda.Performance;
import agenda.Stage;

// just for testing
public class __Main extends JPanel {
	World w;

	public __Main() throws Exception {

		agenda.Agenda agenda = new agenda.Agenda();
		agenda.getArtist().add(new Artist("PAULTIE", null, "SLAGER", "ZEHR GUT", agenda));
		agenda.getStages().add(new Stage("MAIN STAGE", agenda));
		agenda.getStages().add(new Stage("Dixie STAGE", agenda));
		agenda.getStages().add(new Stage("Trixie STAGE", agenda));
		agenda.getStages().add(new Stage("lol STAGE", agenda));
		agenda.getStages().add(new Stage("Kevin STAGE", agenda));
		agenda.getStages().add(new Stage("bixie STAGE", agenda));
		agenda.getStages().add(new Stage("mixie STAGE", agenda));
		agenda.getStages().add(new Stage("dixie STAGE", agenda));

		agenda.getPerformances().add(new Performance(agenda.getStages().get(0), agenda.getArtist().get(0),
				new agenda.Time(1, 00), new agenda.Time(19, 00), 123, agenda));

		HashMap<agenda.Stage, Integer> map = new HashMap<>();
		// test voor meer stages tekenen.
		map.put(agenda.getStages().get(0), 5);
		// map.put(agenda.getStages().get(1), 8);
		// map.put(agenda.getStages().get(2), 11);
		map.put(agenda.getStages().get(3), 14);
		map.put(agenda.getStages().get(4), 17);
		map.put(agenda.getStages().get(5), 20);
		map.put(agenda.getStages().get(6), 23);
		map.put(agenda.getStages().get(7), 26);

		w = new World(agenda, map, new File("Endmap.json"), "Tiled2.png", false, false);
		// punt waar de ingang is op de tilemap is : 350, 3090.

	}

	int i = 0;

	public void paintComponent(Graphics g) {
		if (i++ % 50 == 0) {
			new Visitor(w.getTileAt(8, 99), 0.01f);
			System.out.println("gelukt!");
		}

		Graphics2D g2 = (Graphics2D) g;
		AffineTransform transform = new AffineTransform();
		World.instance.inclusiveUpdate(g2, transform);
	}

	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame("simulator");
		frame.setContentPane(new __Main());
		frame.setSize(850, 850);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new Timer(1000 / 30, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.repaint();
			}
		}).start();
	}

}