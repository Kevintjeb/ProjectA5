package simulator;

import java.awt.Color;
import java.awt.geom.Ellipse2D;

public class Particle
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
		
		color = new Color(255, 255, 255, alpha);
		setColor();
		
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
}
			
			

		