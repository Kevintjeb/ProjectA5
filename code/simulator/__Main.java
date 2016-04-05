package simulator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
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

		World w = new World(agenda, map, new File("Endmap.json"), "Tiled2.png", false, false);

		// punt waar de ingang is op de tilemap is : 350, 3090.
		Visitor xxx = new Visitor(ImageIO.read(new File("code/agents/1.png")), w.getTileAt(100, 100),
				new Point2D.Double(100, 100), 1.0f);

	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// System.out.println("paintComponent");
		World.instance.inclusiveUpdate(g2, null);
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
		frame.setSize(500, 500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new Timer(1000 / 30, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}).start();

	}

}