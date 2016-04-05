package simulator;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Visitor extends Agent {
	static int i = 0;

	private static ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

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
		// TODO Auto-generated method stub

	}

	public static BufferedImage getImage(File filepath) {
		if (images.isEmpty()) {
			int teller = 1;
			File[] lijst = filepath.listFiles();
			for (File f : lijst) {		
				try {
					BufferedImage temp = (BufferedImage) ImageIO.read(f);
					images.add(temp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		int getal = (int) (1 + Math.random()*16);
		System.out.println("--------------------------------------" + getal + "");
		return images.get(getal);

	}

}
