package simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Particle //implements Drawable
{
Color color;
	
	int alpha = 50;
	
	double x;
	double y;
	double newX;
	double newY;
	
	double xMove;
	double yMove;
	
	public Particle(double x, double y, double newX, double newY)
	{
		this.x = x;
		this.y = y;
		this.newX = newX;
		this.newY = newY;
		xMove = x;
		yMove = y;
		
		setColor();
		//World.instance.regesterDrawable(this);
		
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public Color update()
	{
		alpha = alpha - 2;
		color = new Color(color.getRed(),color.getGreen(),color.getBlue(),alpha);
		
		
		return color;
	}
	
	public Ellipse2D updateDeeltje()
	{
		xMove += newX;
		yMove += newY; 
		
		return new Ellipse2D.Double(xMove, yMove, 5, 5);
	}
	
	public int getAlpha()
	{
		return alpha;
	}
	
	public void setColor()
	{
			if(Math.random() <0.14)
			{
				color = new Color(255, 0, 0, alpha); //rood
			}
			else if(Math.random() > 0.14 && Math.random() < 0.28)
			{
				color = new Color(0, 255, 0, alpha);// groen
			}
			else if(Math.random() > 0.18 && Math.random() < 0.42)
			{
				color = new Color(0, 0, 255, alpha); //blauw
			}
			else if(Math.random() > 0.42 && Math.random() < 0.56)
			{
				color = new Color(225, 20, 147, alpha); //roze
			}
			else if(Math.random() > 0.56 && Math.random() < 0.66)
			{
				color = new Color(225, 255, 0, alpha); //geel
			}
			else if(Math.random() > 0.66 && Math.random() < 0.80)
			{
				color = new Color(225, 140, 0, alpha); //oranje
			}
			else
			{
				color = new Color(255, 255, 255, alpha); //wit 
			}
	}

//	@Override
//	public void close()
//	{
//		World.instance.unregesterDrawable(this);
//		
//	}
//
//	@Override
//	public void draw(Graphics2D graphics, AffineTransform t)
//	{
//		
//			double x = 10 * 16;
//			double y = 10 * 16;
//			
//			if(xMove == x)
//			{
//				double bool = Math.random();
//				double newX;
//				double newY;
//				
//				if(bool < 0.25)
//				{
//					newX = Math.random() * 5;
//					newY = Math.random() * 5;
//				}
//				else if(bool > 0.25 && bool < 0.5)
//				{
//					newX = Math.random() * -5;
//					newY = Math.random() * -5;
//				}
//				else if(bool > 0.5 && bool < 0.75)
//				{
//					newX = Math.random() * -5;
//					newY = Math.random() * 5;
//				}
//				else
//				{
//					newX = Math.random() * 5;
//					newY = Math.random() * -5;
//				}
//			}
//			
//			
//			if(getAlpha() < 1)
//			{
//				new Ellipse2D.Double(x, y, 5, 5);
//				xMove = x;
//				yMove = y;
//				alpha = 50;
//			}
//	
//			graphics.setColor(update());
//			Area a = new Area(updateDeeltje());
//			graphics.setTransform(t);
//			graphics.fill(a);
//	}
}
			
			

		