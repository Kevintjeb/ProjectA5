package simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public class NightView implements Updateable, Drawable
{
	boolean nightView = false;
	int overlayAlpha = 0;
	
	public NightView()
	{
		World.instance.regesterUpdateable(this);
		World.instance.regesterDrawable(this);
	}

	@Override
	public void close()
	{
		World.instance.unregesterUpdatable(this);
		World.instance.unregesterDrawable(this);
	}

	@Override
	public void update()
	{
		if(World.instance.getTime().getHours() >= 20 || World.instance.getTime().getHours() <= 6)
		{
			nightView = true;
		}
		
		if(nightView && overlayAlpha < 190)
		{
			overlayAlpha += World.instance.getRealTimeToSimTime();
		}
	}

	@Override
	public void draw(Graphics2D graphics, AffineTransform t)
	{
		Rectangle overlayer = new Rectangle(-1600, -1600, 6400, 6400);
		graphics.setTransform(t);
		graphics.setColor(new Color(0, 0, 0, overlayAlpha));
		Area a = new Area(overlayer);
		graphics.fill(a);
		
	}

}
