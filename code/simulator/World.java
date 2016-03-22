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
	private LinkedList<Closable> toRemove;
	private HashMap<String, Integer> buildingMap; // maps between the building and it's corresponding id
	
	private double realTimeToSimTime; // when a real time in ms is multiplied with this number,
					//and the ceiling of the product is taken is will give the time in simulation time in seconds
	private int worldTime; // the current time in the simulation world
	private int deltaTime; // the delta between the second to last update() and the latest update()
	private long lastRealTime = UNINITIALIZED; // the last time update was called is used to calculate deltaTime
	private double timeRemainder = 0;
	
	public final agenda.Agenda agenda;
	
	private Image map;
	
	protected static World instance;
	
	public World()
	{
		this.agenda = new agenda.Agenda();
		updateables = new LinkedList<>();
		drawables = new LinkedList<>();
		toRemove = new LinkedList<Closable>();
		instance = this;
		realTimeToSimTime = 0.1;
	}
	
	public World(agenda.Agenda agenda, Map<agenda.Stage, Integer> stageMap, String jsonPath)
	{
		this.agenda = agenda;
	}
	
	public void inclusiveUpdate(Graphics2D g2)
	{
		update();
		draw(g2);
		cleanUp();
	}
	
	// this method has been checked and it works correctly 14/3/2016
	public void update()
	{
		{// update everything related to time
			long realTime = System.currentTimeMillis();
			System.out.println(timeRemainder);
			if (lastRealTime == UNINITIALIZED)
				lastRealTime = realTime;
			double deltaTimeDouble = realTime-lastRealTime+timeRemainder;
			deltaTime = (int)(deltaTimeDouble*realTimeToSimTime);
			timeRemainder = (deltaTimeDouble*realTimeToSimTime-deltaTime)/realTimeToSimTime;
			
			worldTime += deltaTime;
			lastRealTime = realTime;
		}
		
		{// update all updatables
			ListIterator<Updateable> iterator = updateables.listIterator();
			while (iterator.hasNext())
				iterator.next().update();
		}
	}
	
	public void cleanUp()
	{
		ListIterator<Closable> iterator = toRemove.listIterator();
		while (iterator.hasNext())
			iterator.next().close();
		
		toRemove.clear();
	}
	
	public void initJson(String filepathjson, String filepathtilemap) {
		// ATRIBUTEN, moeten worden aangemaakt boven constructor ofcourse..
		TileMap map;
		JSONParser parser = new JSONParser();
		String mappath = filepathtilemap;
		int height;
		int width;
		ArrayList<TileLayer> layerslist = new ArrayList<>();

		try {
			// tilemap maken door een tilemap object aan te maken.
			map = new TileMap(mappath, 32, 32);

			// jsonfile parsen
			Object file = parser.parse(new FileReader(filepathjson));
			JSONObject json = (JSONObject) file;
			JSONArray layers = (JSONArray) json.get("layers");
			// hoogte + breedte van de hele map.
			height = ((Long) json.get("height")).intValue();
			width = ((Long) json.get("width")).intValue();

			// jsonfile uitlezen op layers en toevoegen als tilelayer in
			// tilelayer arraylist.
			// als de layer een visible : true heeft dan wordt hij toegevoegd
			// met de parameter true zodat de tekenmethode weet dat de layer
			// moet worden getekend.
			// dit is voor de collision layers : die hebben false in de JSON.
			
			//TODO nog editten naar wat binnenkomt kwa wat er gebruikt moet worden.
			for (int i = 0; i < layers.size(); i++) {
				JSONObject currentlayer = (JSONObject) layers.get(i);
				if (currentlayer.get("type").equals("tilelayer")) {
					boolean b = false;
					if (currentlayer.get("visible").equals(true)) {
						b = true;
					}
					TileLayer e = new TileLayer((JSONArray) currentlayer.get("data"), map, height, width, b);
					layerslist.add(e);
				}
			}
			// tileheight en width bepalen door uit de json te halen.
			// width en tilewidth zijn verschillende waarde. width = groote van
			// de map en tilewidth is puur de tilewidth (32 bij ons..)
			mapimage = new BufferedImage(width * map.getTileWidth(), height * map.getTileHeight(),
					BufferedImage.TYPE_INT_ARGB);

			
			//Collision toevoegen in een arraylist van booleans?
			// 2 tiles : tileID 90 en 86.
			//op basis van collision later > tiled eigenschap!
			
		} catch (Exception e) {
			e.printStackTrace();

		}

		// tekenen op de map, dit gebeurt maar 1x en zorgt ervoor dat de map dus
		// niet meer veranderd maar wel gebruikt kan worden zonder veel
		// geheugen.
		try {
			Graphics2D g = (Graphics2D) mapimage.getGraphics();

			for (TileLayer layer : layerslist) {
				g.drawImage(layer.getLayerImage(), 0, 0, null);
			}
			layerslist.clear();
			// Garbage collecter notification voor java, om die tering rommel op
			// te ruimen >> 1500MB.
			//TODO FIX JAVA.
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
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
