package simulator;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

public class World {
	private final int UNINITIALIZED = -1;
	
	private Tile[][] tiles;
	private Building[] buildings;
	private LinkedList<Updateable> updateables;
	private LinkedList<Drawable> drawables;
	private HashMap<String, Integer> buildingMap; // maps between the building and it's corresponding id
	
	private float realTimeToSimTime; // when a real time in ms is multiplied with this number,
					//and the ceiling of the product is taken is will give the time in simulation time in seconds
	private int worldTime; // the current time in the simulation world
	private int deltaTime; // the delta between the second to last update() and the latest update()
	private long lastRealTime = UNINITIALIZED; // the last time update was called is used to calculate deltaTime
	private float timeRemainder = 0;
	
	public final agenda.Agenda agenda;
	
	private Image map;
	
	protected static World instance;
	
	public World()
	{
		this.agenda = new agenda.Agenda();
		updateables = new LinkedList<>();
		drawables = new LinkedList<>();
		instance = this;
		realTimeToSimTime = 0.1f;
	}
	
	public World(agenda.Agenda agenda, Map<agenda.Stage, Integer> stageMap, String jsonPath)
	{
		this.agenda = agenda;
	}
	
	// this method has been checked and it works correctly 14/3/2016
	public void update()
	{
		{// update everything related to time
			long realTime = System.currentTimeMillis();
			System.out.println(timeRemainder);
			if (lastRealTime == UNINITIALIZED)
				lastRealTime = realTime;
			float deltaTimeFloat = realTime-lastRealTime+timeRemainder;
			//System.out.println(deltaTimeFloat);
			deltaTime = (int)(deltaTimeFloat*realTimeToSimTime);
			//System.out.println(deltaTime);
			timeRemainder = (deltaTimeFloat*realTimeToSimTime-deltaTime)/realTimeToSimTime;
			//System.out.println(timeRemainder);
			
			worldTime += deltaTime;
			lastRealTime = realTime;
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
