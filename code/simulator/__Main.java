package simulator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.HashMap;

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
		//map.put(agenda.getStages().get(1), 8);
		//map.put(agenda.getStages().get(2), 11);
		map.put(agenda.getStages().get(3), 14);
		map.put(agenda.getStages().get(4), 17);
		map.put(agenda.getStages().get(5), 20);
		map.put(agenda.getStages().get(6), 23);
		map.put(agenda.getStages().get(7), 26);

		w = new World(agenda, map, new File("static_data/festival_grounds/Endmap2.json"),
				"static_data/tile_maps/Tiled2.png", false, false);

		new Visitor(w.getTileAt(8, 99), 0.01f);
	}

	int i = 0;
	int teller = 0;
	public void paintComponent(Graphics g) {
//		if (i++ % 50 == 0) {
//			new Visitor(w.getTileAt(8, 99), 0.01f);
//			teller++;
//		}
//		if (i++ % 30 == 0) {
//			new Visitor(w.getTileAt(12, 99), 0.015f);
//			teller++;
//		}
		Graphics2D g2 = (Graphics2D) g;
		// System.out.println("paintComponent");
		World.instance.inclusiveUpdate(g2, new AffineTransform());
		AffineTransform transform = new AffineTransform();
		float scaleX = getWidth() / (float) (100 * 32);
		float scaleY = getHeight() / (float) (100 * 32);
		float scale = (scaleX > scaleY) ? scaleY : scaleX;
		transform.scale(scale, scale * -1);
		g2.transform(transform);
	}

	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame("simulator");
		frame.setContentPane(new __Main());
		frame.setSize(850, 850);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				World.instance.close();
			}
		});
		{

			new Timer(1000 / 30, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					frame.repaint();

				}
			}).start();
		}
	}
}
