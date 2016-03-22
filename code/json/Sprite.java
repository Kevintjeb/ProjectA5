package json;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Sprite {
	private Point2D locatie;
	private Point2D doel;
	private Point2D locatieold;
	private double richting;
	private Image image;
	private double snelheid;

	public Sprite(Point2D l) {
		this.locatie = l;
		image = new ImageIcon("sprite.png").getImage();
		richting = Math.random() * Math.PI * 2;
		snelheid = 0.1 + Math.random() * 0.4;
		doel = new Point2D.Double(400, 300);
	}

	public void draw(Graphics2D g2d) {
		AffineTransform transformatie = new AffineTransform();
		transformatie.translate(locatie.getX() - image.getWidth(null) / 2, locatie.getY() - image.getHeight(null) / 2);
		transformatie.rotate(richting, image.getWidth(null) / 2, image.getHeight(null) / 2);
		g2d.drawImage(image, transformatie, null);
	}

	public void update(ArrayList<Sprite> sprites) {
		// richting += 0.01;
		locatieold = locatie;
		double dx = doel.getX() - locatie.getX();
		double dy = doel.getY() - locatie.getY();
		double newRichting = Math.atan2(dy, dx);

		// richting = newRichting;
		double richtingsVerschil = newRichting - richting;
		while (richtingsVerschil > Math.PI)
			richtingsVerschil -= 2 * Math.PI;
		while (richtingsVerschil < -Math.PI)
			richtingsVerschil += 2 * Math.PI;

		if (richtingsVerschil < 0)
			richting -= 0.007;
		if (richtingsVerschil > 0)
			richting += 0.007;

		Point2D newLocatie = new Point2D.Double(locatie.getX() + Math.cos(richting) * snelheid,
				locatie.getY() + Math.sin(richting) * snelheid);
		
		boolean isCollision = false;
		for (Sprite b : sprites) {
			if (b == this)
				continue;
			if (b.locatie.distance(newLocatie) < 32) {
				isCollision = false;
				break;
			}
		}

		if (!isCollision)
			locatie = newLocatie;
		else
			richting += 0.01;

	}

	public void setDoel(Point2D point) {
		this.doel = point;
	}
	
	public void setLocation(Point2D locatie)
	{	
		this.locatie = locatie;
	}
	
	public Point2D getLocatie()
	{
		return locatie;
	}
	public Point2D getLocatieOld()
	{
		return locatieold;
	}

}
