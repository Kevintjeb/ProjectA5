package simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import javax.imageio.ImageIO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class World {
	private final int UNINITIALIZED = -1;

	private Tile[][] tiles;
	private ArrayList<Building> buildings;
	private LinkedList<Updateable> updateables;
	private LinkedList<Drawable> drawables;
	private LinkedList<Closable> toRemove;
	private HashMap<String, Integer> buildingMap; // maps between the building
													// and it's corresponding id

	private double realTimeToSimTime = 0.5; // when a real time in ms is multiplied
										// with this number,
	// and the ceiling of the product is taken is will give the time in
	// simulation time in seconds
	private int worldTime; // the current time in the simulation world
	private int deltaTime; // the delta between the second to last update() and
							// the latest update()
	private long lastRealTime = UNINITIALIZED; // the last time update was
												// called is used to calculate
												// deltaTime
	private double timeRemainder = 0;

	public final agenda.Agenda agenda;

	private BufferedImage mapImage;

	protected static World instance;

	static int index = 0;
	
	private void drawBoolArray(boolean[][] info)
	{
		final int size = 1;
		BufferedImage img = new BufferedImage(info.length*size, info[0].length*size,  BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)img.getGraphics();
		
		for (int x =0; x < info.length; x++)
		{
			for (int y = 0; y < info[0].length; y++)
			{
				if (info[x][y])
					g.setColor(Color.BLACK);
				else
					g.setColor(Color.WHITE);
				
				g.fillRect(x*size, y*size, size, size);
			}
		}
		
		try {
			ImageIO.write(img, "png", new File("debug_data/boolean_print" + index++ + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public World(agenda.Agenda agenda, Map<agenda.Stage, Integer> stageMap, File jsonPath, String tileMapPath)
	{
		this(agenda, stageMap, jsonPath, tileMapPath, false, false);
	}
	
	public World(agenda.Agenda agenda, Map<agenda.Stage, Integer> stageMap, File jsonPath, String tileMapPath, final boolean debug, final boolean numberOrText) {
		long constructorStart = System.currentTimeMillis();
		instance = this;
		this.agenda = agenda;
		buildings = new ArrayList<>();
		updateables = new LinkedList<>();
		drawables = new LinkedList<>();
		toRemove = new LinkedList<>();
		buildingMap = new HashMap<>();

		PrintStream oldStream;
		{
			PrintStream debugTxt;
			File debugInfo = new File("debug_data/");
			if (debugInfo.exists() == false)
				debugInfo.mkdir();
			
			for (File file : debugInfo.listFiles())
				file.delete();
			
			try {
				debugTxt = new PrintStream(new File("debug_data/log.txt"));
			} catch (FileNotFoundException e) {
				System.out.println("log could not be created");
				return;
			}
			System.out.println("starting world creation");
			oldStream = System.out;
			System.setOut(debugTxt);
		}
		
		boolean[][] collisionInfo = null;
		{
			// ATRIBUTEN, moeten worden aangemaakt boven constructor ofcourse..
			TileMap map;
			// ints moeten +1 omdat de eerste GID wordt opgeteld..
			final int entranceExit = 117;
			final int windowshop = 178;
			final int mainEntrance = 268;
			final int collidableTrue = 90;
			final int collidableFalse = 86;
			final int danceFloorTrue = 63;

			JSONParser parser = new JSONParser();
			int height;
			int width;

			ArrayList<TileLayer> layerslist = new ArrayList<>();

			try {
				// tilemap maken door een tilemap object aan te maken.
				map = new TileMap(tileMapPath, 32, 32);
				// jsonfile parsen
				Object file = parser.parse(new FileReader(jsonPath));
				JSONObject json = (JSONObject) file;
				JSONArray layers = (JSONArray) json.get("layers");
				// hoogte + breedte van de hele map.`
				height = ((Long) json.get("height")).intValue();
				width = ((Long) json.get("width")).intValue();

				collisionInfo = new boolean[width][height];
				if (debug) drawBoolArray(collisionInfo);
				mapImage = new BufferedImage(width * map.getTileWidth(), height * map.getTileHeight(),
						BufferedImage.TYPE_INT_ARGB);
				tiles = new Tile[width][height];
				for (int x = 0; x < width; x++)
					for (int y = 0; y < height; y++)
						tiles[x][y] = new Tile(x, y);
				// jsonfile uitlezen op layers en toevoegen als tilelayer in
				// tilelayer arraylist.
				// als de layer een visible : true heeft dan wordt hij
				// toegevoegd
				// met de parameter true zodat de tekenmethode weet dat de layer
				// moet worden getekend.
				// dit is voor de collision layers : die hebben false in de
				// JSON.
				// eerste 4 layers tekenen -> standaard layers.

				{
					//shops + toilet
					JSONObject currentlayer = (JSONObject) layers.get(30);
					if (currentlayer.get("visible").equals(true)) {
						TileLayer temp = new TileLayer((JSONArray) currentlayer.get("data"), map, height, width, true);
						layerslist.add(temp);
					} else {
						JSONArray data = (JSONArray) currentlayer.get("data");
						ArrayList<Tile> entrance = new ArrayList<>();
						for (int k = 0; k < data.size(); k++) {
							int tileType = ((Long) data.get(k)).intValue();
							switch (tileType) {
							case windowshop:
								entrance.add(tiles[k % width][k / width]);
							//	buildings.add(new Cafetaria(entrance, 5));
								break;
							}

						}
					}
				}
				for (int i = 0; i < 5; i++) {
					
					JSONObject currentlayer = (JSONObject) layers.get(i);
					if (currentlayer.get("visible").equals(true)) {
						TileLayer temp = new TileLayer((JSONArray) currentlayer.get("data"), map, height, width, true);
						layerslist.add(temp);
					} else {
						JSONArray data = (JSONArray) currentlayer.get("data");
						for (int k = 0; k < data.size(); k++) {
							int tileType = ((Long) data.get(k)).intValue();
							switch (tileType) {
							case collidableTrue:
								collisionInfo[k % width][k / width] = true;
								break;
							}

						}
					}
				}
				
				if(debug) drawBoolArray(collisionInfo);

				for (Map.Entry<agenda.Stage, Integer> entry : stageMap.entrySet()) {

					agenda.Stage stage = entry.getKey();
					int stageIndex = entry.getValue();
					ArrayList<Tile> entrance = new ArrayList<>();
					ArrayList<Tile> danceFloor = new ArrayList<>();

					JSONObject currentlayer = (JSONObject) layers.get(stageIndex);

					TileLayer e = new TileLayer((JSONArray) currentlayer.get("data"), map, height, width, true);
					layerslist.add(e);
					if (currentlayer.get("properties") != null) {

						JSONObject properties = (JSONObject) currentlayer.get("properties");
						int maxAgents = Integer.parseInt((String) properties.get("maxAgents"));

						String drawProperties = (String) properties.get("drawwith");
						String[] bundel = drawProperties.split(",");

						for (int j = 0; j < bundel.length; j++) {
							JSONObject layer = (JSONObject) layers.get(Integer.parseInt(bundel[j]));

							if (layer.get("visible").equals(true)) {
								TileLayer temp = new TileLayer((JSONArray) layer.get("data"), map, height, width, true);
								layerslist.add(temp);

							} else {
								JSONArray data = (JSONArray) layer.get("data");
								for (int i = 0; i < data.size(); i++) {
									int tileType = ((Long) data.get(i)).intValue();
									switch (tileType) {
									//collision info false wanneer er geen collision is dus je mag wel lopen.
									//collision info true wanneer er WEL collision is dus je mag NIET lopen.
									case entranceExit:
										collisionInfo[i % width][i / width] = false;
										entrance.add(tiles[i % width][i / width]);
										break;
									case collidableFalse:
										collisionInfo[i % width][i / width] = false;
										break;
									case collidableTrue:
										collisionInfo[i % width][i / width] = true;
										break;

									case danceFloorTrue:
										collisionInfo[i % width][i / width] = false;
										danceFloor.add(tiles[i % width][i / width]);
										break;
									}

								}
							}

						}
						buildings.add(new Stage("", entrance, entrance, maxAgents, stage, danceFloor));
						if (debug) drawBoolArray(collisionInfo);
					}

				}

				// tileheight en width bepalen door uit de json te halen.
				// width en tilewidth zijn verschillende waarde. width = groote
				// van
				// de map en tilewidth is puur de tilewidth (32 bij ons..)

			} catch (Exception e) {
				e.printStackTrace();

			}

			// tekenen op de map, dit gebeurt maar 1x en zorgt ervoor dat de map
			// dus
			// niet meer veranderd maar wel gebruikt kan worden zonder veel
			// geheugen.
			try {
				Graphics2D g = (Graphics2D) mapImage.getGraphics();

				for (TileLayer layer : layerslist) {
					g.drawImage(layer.getLayerImage(), 0, 0, null);
				}
				layerslist.clear();
				// saved de file naar het systeem.
				if (debug)
				{
					File outputfile = new File("debug_data/mapImage.png");
					ImageIO.write(mapImage, "png", outputfile);
				}
				// Garbage collecter notification voor java, om die tering
				// rommel op
				// te ruimen >> 1500MB.
				// TODO FIX JAVA.
				System.gc();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		{
			HashSet<Integer> typeContructed = new HashSet<>();
			for (Building b : buildings)
			{
				if (typeContructed.contains(b.typeID))
					continue;
				typeContructed.add(b.typeID);
				buildingMap.put(b.name, b.typeID);
			}
		}

		{

			{///////////////////////// Path finding
				///////////////////////// ////////////////////////////////////
				{ // check info
					if (collisionInfo == null)
						return; // TODO exception
					int lastLenght = collisionInfo[0].length;
					for (boolean[] boolArray : collisionInfo)
						if (boolArray.length != lastLenght)
							return; // TODO exception?
				}

				ArrayList<Node> graph;
				HashMap<Position, Node> positionToNodeMap;
				{ ///////////// Generating the graph ///////////
					int nodeCount = 0;
					for (boolean[] boolArray : collisionInfo)
						for (boolean bool : boolArray)
							if (!bool)
								nodeCount++;

					graph = new ArrayList<>(nodeCount);

					positionToNodeMap = new HashMap<>(nodeCount);
					
					for (int y = 0; y < collisionInfo.length; y++)
						for (int x = 0; x < collisionInfo[0].length; x++)
							if (collisionInfo[x][y] == false) {
								Node n = new Node(x, y);
								graph.add(n);
								positionToNodeMap.put(new Position(x, y), n);
								//System.out.println((positionToNodeMap.get(new Position(x,y)) == null) ? "node could not be reovered" : "node could be recovereded");
							}

					for (Node n : graph) { // adding the edges to the node TODO
											// diagonal?
						int index = 0;
						Position position = new Position(n.X - 1, n.Y);
						if (positionToNodeMap.containsKey(position)) {
							n.straitEdges[index] = positionToNodeMap.get(position);
							index++;
						}

						position.set(n.X + 1, n.Y);
						if (positionToNodeMap.containsKey(position)) {
							n.straitEdges[index] = positionToNodeMap.get(position);
							index++;
						}

						position.set(n.X, n.Y - 1);
						if (positionToNodeMap.containsKey(position)) {
							n.straitEdges[index] = positionToNodeMap.get(position);
							index++;
						}

						position.set(n.X, n.Y + 1);
						if (positionToNodeMap.containsKey(position)) {
							n.straitEdges[index] = positionToNodeMap.get(position);
							index++;
						}

					}

				} ////////////// End of graph generation ///////////
				System.out.println("positionToNodeMap size " + positionToNodeMap.size());
				{ ////////////// Graph test using depth first search /////////
					Stack<Node> nodeStack = new Stack<>();
					HashSet<Node> visitedNodes = new HashSet<>(graph.size());

					System.out.println("gaph size " + graph.size());
					nodeStack.push(graph.get(0));
					visitedNodes.add(nodeStack.peek());

					while (nodeStack.empty() == false) {
						Node n = nodeStack.peek();
						visitedNodes.add(n);
						boolean pushed = false;

						for (int i = 0; i < n.straitEdges.length; i++) {
							if (n.straitEdges[i] == null || visitedNodes.contains(n.straitEdges[i]))
								continue;
							nodeStack.push(n.straitEdges[i]);
							pushed = true;
							break;
						}

						if (pushed == false)
							nodeStack.pop();
					}
					
					if (debug) {////////// Create a debug image /////////////////
						Color inGraph = new Color(0, 255, 0, 128);
						Color outGraph = new Color(255, 0, 0, 128);
						
						BufferedImage debugImage = new BufferedImage(mapImage.getWidth(), mapImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
						Graphics2D graphics = (Graphics2D)debugImage.getGraphics();
						graphics.drawImage(mapImage, 0, 0, null);
						
						
						for (Node n : graph)
						{
							Color c = inGraph;
							
							if (visitedNodes.contains(n) == false)
								c = outGraph;
							
							graphics.setColor(c);
							graphics.fillRect(n.X*32, n.Y*32, 32, 32);
						}
						
						try {
							ImageIO.write(debugImage, "png", new File("debug_data/pathfinding_debug_image.png"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if (visitedNodes.size() != graph.size()) {
						System.out.println("not all nodes are conected"); // TODO
																			// exception
						return;
					}
				} ////////// end of graph test //////////////////////////

				{ ////////// generating directions /////////////////////
					class TypeIdTilePair {
						int typeID;
						String name;
						ArrayList<Tile> entances;
						ArrayList<Tile> exits;

						public TypeIdTilePair() {
							entances = new ArrayList<Tile>();
							exits = new ArrayList<Tile>();
						}
					}

					ArrayList<TypeIdTilePair> pairs = new ArrayList<TypeIdTilePair>();
					for (Building b : buildings) {
						boolean found = false;

						for (TypeIdTilePair pair : pairs) {
							if (pair.typeID == b.typeID) {
								pair.entances.addAll(b.entrances);
								pair.exits.addAll(b.exits);
								found = true;
							}
						}

						if (found == false) {
							TypeIdTilePair pair = new TypeIdTilePair();
							pair.typeID = b.typeID;
							pair.name = b.name;
							pair.entances.addAll(b.entrances);
							pair.exits.addAll(b.exits);
							pairs.add(pair);
						}
					}

					for (TypeIdTilePair pair : pairs) {
						System.out.println("pair<" + pair.typeID + ", " + pair.name +", " + pair.entances.size() + ">");
						
						Queue<Node> queue = new LinkedList<>();
						HashSet<Node> visited = new HashSet<Node>();
						HashMap<Position, Integer> nodeToDistanceMap = null;
						if (debug) nodeToDistanceMap = new HashMap<>(graph.size());
						
						for (Tile tile : pair.entances) {
							Node n = positionToNodeMap.get(new Position(tile.X, tile.Y));
							queue.add(n);
							tiles[n.X][n.Y].directions.put(pair.typeID, tiles[n.X][n.Y]);
							visited.add(n);
							if (debug) nodeToDistanceMap.put(new Position(tile.X, tile.Y), -1);
							System.out.println("tile(" + tile.X + ", " + tile.Y + ") " + ((n == null) ? "node is null" : "node was found")+
									((collisionInfo[tile.X][tile.Y]) ? ", there should be no node" : ", there should be a node"));
						}

						int visitedNodeCount = 0;
						int highestDistance = 0;
						while (queue.isEmpty() == false) {
							Node node = queue.poll();
							visitedNodeCount++;
							int newNodeDistance = 0;
							if (debug) newNodeDistance = nodeToDistanceMap.get(new Position(node.X, node.Y)) + 1;
							if (newNodeDistance > highestDistance)
								highestDistance = newNodeDistance;
							//System.out.println(((node == null) ? "node is null" : "node was found"));
							if (node == null)
								continue;
							for (int i = 0; i < node.straitEdges.length; i++) {
								if (node.straitEdges[i] == null)
									continue;
								if (visited.contains(node.straitEdges[i]) == true)
									continue;
								visited.add(node.straitEdges[i]);
								if (debug) nodeToDistanceMap.put(new Position(node.straitEdges[i].X, node.straitEdges[i].Y), newNodeDistance);
								queue.add(node.straitEdges[i]);
								tiles[node.straitEdges[i].X][node.straitEdges[i].Y].addDirection(pair.typeID,
										tiles[node.X][node.Y]);
							}
						}
						System.out.println("direction generation visitedNodeCount : " + visitedNodeCount);
						if (debug){/// Creating a debug image
							BufferedImage debugImage = new BufferedImage(mapImage.getWidth(), mapImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
							Graphics2D graphics = (Graphics2D)debugImage.getGraphics();
							graphics.drawImage(mapImage, 0, 0, null);
							
							
							for (Node n : graph)
							{
								int color = 255-(int)(255/(double)highestDistance*nodeToDistanceMap.get(new Position(n.X, n.Y)));
								//System.out.println("("+n.X+", " + n.Y +") " + color);
								if (color < 0 || color> 255)
									color = 128;
								graphics.setColor(new Color(color, color, color, 180));
								graphics.fillRect(n.X*32, n.Y*32, 32, 32);
								graphics.setColor(Color.BLACK);
								if (numberOrText)
								{
									graphics.drawString(nodeToDistanceMap.get(new Position(n.X, n.Y)).toString(), n.X*32+16, n.Y*32+16);
								}
								else
								{
									Tile direction = tiles[n.X][n.Y].getDirection(pair.typeID);
									if (direction.X < n.X)
									{
										graphics.drawString("left", n.X*32+16, n.Y*32+16);
									}
									else if (direction.X > n.X)
									{
										graphics.drawString("right", n.X*32+16, n.Y*32+16);
									}
									else if (direction.Y < n.Y)
									{
										graphics.drawString("up", n.X*32+16, n.Y*32+16);
									}
									else if (direction.Y > n.Y)
									{
										graphics.drawString("down", n.X*32+16, n.Y*32+16);
									}
									else 
									{
										graphics.setColor(Color.ORANGE);
										graphics.fillRect(n.X*32, n.Y*32, 32, 32);
										graphics.setColor(Color.BLACK);
										graphics.drawString("dest", n.X*32+4, n.Y*32+16);
									}
								}
							}
							
							try {
								ImageIO.write(debugImage, "png", new File("debug_data/pathfinding_debug_image" + pair.typeID + "_" + pair.name+".png"));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		
		{
			long delta = System.currentTimeMillis() - constructorStart;
			System.setOut(oldStream);
			System.out.println("World was created in " + (double)delta/1000 + " seconds");
		}

	}

	public String setRealTimeToSimTime(double realTimeToSimTime)
	{
		if (realTimeToSimTime < 0)
			return "sim time can't be lower than 0";
		this.realTimeToSimTime = realTimeToSimTime;
		return null;
	}

	protected Tile getTileAt(int x, int y) {
		if (x < 0 || y < 0 || x > tiles.length || y > tiles[0].length)
			return null;
		return tiles[x][y];
	}

	private class Node {
		final int X, Y;
		Node[] straitEdges;

		public Node(int x, int y) {
			X = x;
			Y = y;
			straitEdges = new Node[4];
		}

		@Override
		public int hashCode() {
			int hash = 89 * X + 7 * Y;
			for (int i = 0; i < straitEdges.length; i++) {
				if (straitEdges[i] != null)
					hash = hash * 89 + straitEdges[i].X + 7 * straitEdges[i].Y * 7;
				else
					continue;
			}
			return hash;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Node == false)
				return false;
			Node n = (Node) o;
			return X == n.X && Y == n.Y;
		}
	}

	private class Position {
		int x, y;

		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void set(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int hashCode() {
			return 111 * x + 7 * y;
		}

		@Override
		public boolean equals(Object e) {
			if (e instanceof Position == false)
				return false;
			Position p = (Position) e;
			return x == p.x && y == p.y;
		}
	}

	public void inclusiveUpdate(Graphics2D g2, AffineTransform t) {
		update();
		draw(g2, t);
		cleanUp();
	}

	// this method has been checked and it works correctly 14/3/2016
	public void update() {
		{// update everything related to time
			long realTime = System.currentTimeMillis();
			if (lastRealTime == UNINITIALIZED)
				lastRealTime = realTime;
			if (Double.isNaN(timeRemainder))
				timeRemainder = 0.0;
				
			double deltaTimeDouble = realTime - lastRealTime + timeRemainder;
			deltaTime = (int) (deltaTimeDouble * realTimeToSimTime);
			timeRemainder = (deltaTimeDouble * realTimeToSimTime - deltaTime) / realTimeToSimTime;

			worldTime += deltaTime;
			lastRealTime = realTime;
		}

		{// update all updatables
			ListIterator<Updateable> iterator = updateables.listIterator();
			while (iterator.hasNext())
				iterator.next().update();
		}
	}

	public void cleanUp() {
		ListIterator<Closable> iterator = toRemove.listIterator();
		while (iterator.hasNext())
			iterator.next().close();

		toRemove.clear();
	}

	public void draw(Graphics2D graphics, AffineTransform t) {
		graphics.drawImage(mapImage, t, null);

		ListIterator<Drawable> iterator = drawables.listIterator();
		while (iterator.hasNext())
			iterator.next().draw(graphics);
	}

	protected void regesterUpdateable(Updateable u) {
		updateables.add(u);
	}

	protected void unregesterUpdatable(Updateable u) {
		updateables.remove(u);
	}

	protected void regesterDrawable(Drawable d) {
		drawables.add(d);
	}

	protected void unregesterDrawable(Drawable d) {
		drawables.remove(d);
	}

	protected int getPathID(String building) {
		if (buildingMap.containsKey(building) == false)
			return -1;
		return buildingMap.get(building);
	}

	protected int getWorldTime() {
		return worldTime;
	}

	protected int getDeltaTime() {
		return deltaTime;
	}

}
