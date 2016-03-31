package simulator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import agenda.Artist;
import agenda.Performance;
import agenda.Stage;


// just for testing
public class __Main extends JPanel{
	
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
		//test voor meer stages tekenen.
		map.put(agenda.getStages().get(0), 5);
		map.put(agenda.getStages().get(3), 14);
		map.put(agenda.getStages().get(4), 17);
		map.put(agenda.getStages().get(6), 23);
		map.put(agenda.getStages().get(7), 26);
		
		World w = new World(agenda, map, new File("maps\\test.json"), "Tiled2.png");
		System.out.println("world was constructed");
		
		new Visitor(ImageIO.read(new File("code/agents/1.png")), w.getTileAt(50, 50), new Point2D.Double(50, 50), 1.0f);
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		System.out.println("paintComponent");
		AffineTransform transform = new AffineTransform();
		float scaleX = getWidth()/(float)(100*32);
		float scaleY = getHeight()/(float)(100*32);
		float scale = (scaleX > scaleY) ? scaleY : scaleX;
		transform.scale(scale, scale*-1);
		g2.transform(transform);
		World.instance.inclusiveUpdate(g2, transform);
	}
	
	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame("simulator");
		frame.setContentPane(new __Main());
		frame.setSize(500, 500);
		frame.setVisible(true);
		while (true)
		{
			Thread.sleep(100);
		}
	}
}