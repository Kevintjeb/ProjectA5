package simulator;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Visitor extends Agent {
	static int i = 0;
	
	public Visitor(BufferedImage image, Tile tile, float speed) {
		super(image, tile, new Point2D.Double(tile.X, -tile.Y), speed);
		setDestination(i++%6);
	}

	@Override
	public void update() {
		move();
	}

	@Override
	void destenationReached() {
		// TODO Auto-generated method stub

	}

}
