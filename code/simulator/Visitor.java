package simulator;

import java.awt.Image;
import java.awt.geom.Point2D;

public class Visitor extends Agent {

	public Visitor(Image image, Tile tile, Point2D point, float speed) {
		super(image, tile, point, speed);
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
