package simulator;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

public class World {
	
	private Tile[][] tiles;
	private Building[] buildings;
	private LinkedList<Updateable> updateables;
	private LinkedList<Drawable> drawables;
	private HashMap<String, Integer> buildingMap; // maps between the building and it's corresponding id
	
	private float realTimeToSimTime; // when a real time in ms is multiplied with this number,
					//and the ceiling of the product is taken is will give the time in simulation time in seconds
	private int worldTime; // the current time in the simulation world
	private int deltaTime; // the delta between the second to last update() and the latest update()
	private long lastRealTime = -1;
	
	public final agenda.Agenda agenda;
	
	private Image map;
	
	protected static World instance;
	
	public void update()
	{
		{// update everything related to time
			long realTime = System.currentTimeMillis();
			if (lastRealTime == -1)
				lastRealTime = realTime;
			deltaTime = (int)((realTime-lastRealTime)*realTimeToSimTime);
			worldTime += deltaTime;
		}
		
		{// update all updatables
			ListIterator<Updateable> iterator = updateables.listIterator();
			while (iterator.hasNext())
				iterator.next().update();
		}
	}
	
	public void draw(Graphics2D graphics)
	{
		graphics.drawImage(map, new AffineTransform(), null);
		
		ListIterator<Drawable> iterator = drawables.listIterator();
		while (iterator.hasNext())
			iterator.next().draw(graphics);
	}
	
	protected void regesterUpdateable(Updateable u) 
	{
		updateables.add(u);
	}
	protected void unregesterUpdatable(Updateable u) 
	{
		updateables.remove(u);
	}
	
	protected void regesterDrawable(Drawable d) 
	{
		drawables.add(d);
	}
	protected void unregesterDrawable(Drawable d)
	{
		drawables.remove(d);
	}
	
	protected enum PathInfo
	{
		ENTRANCE, EXIT
	}
	
	protected int getPathID(String building, PathInfo info)
	{
		if (buildingMap.containsKey(building) == false)
			return -1;
		return buildingMap.get(building) << 1 | ((info == PathInfo.ENTRANCE) ? 0 : 1);
	}
	
	protected int getWorldTime()
	{
		return worldTime;
	}
	
	protected int getDeltaTime()
	{
		return deltaTime;
	}
	
}
