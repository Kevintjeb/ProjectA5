package simulator;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Visitor extends Agent {
	static int i = 0;
	static int i2 = 0;

	private static ArrayList<Image> images = new ArrayList<>();

	public Visitor(Tile tile, float speed) {
		super(getImage(new File("code/agents")), tile, new Point2D.Double(tile.X, -tile.Y), speed);
		setDestination(i++ % 6);
	}

	@Override
	public void update() {
		move();
	}

	@Override
	void destenationReached() {
		setDestination(i2++%6);
		//System.out.println("VISITOR destination reached");
	}

	public static Image getImage(File filepath) {
		if (images.isEmpty()) {
			int teller = 1;
			File[] lijst = filepath.listFiles();
			for (File f : lijst) {		
				try {
					BufferedImage temp = (BufferedImage) ImageIO.read(f);
					images.add(temp.getScaledInstance(32, 32, BufferedImage.SCALE_FAST));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		int getal = (int) (1 + Math.random()*16);
		return images.get(getal);

	}

}