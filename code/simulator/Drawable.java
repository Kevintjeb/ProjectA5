package simulator;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

interface Drawable extends Closable{
	void draw(Graphics2D graphics, AffineTransform t);
}
