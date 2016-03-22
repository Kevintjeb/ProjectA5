package simulator;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

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

	private double realTimeToSimTime; // when a real time in ms is multiplied
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

	public World(agenda.Agenda agenda, Map<agenda.Stage, Integer> stageMap, String jsonPath, String tileMapPath) {
		instance = this;
		this.agenda = agenda;

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
			buildings = new ArrayList<Building>();
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
				mapImage = new BufferedImage(width * map.getTileWidth(), height * map.getTileHeight(),
						BufferedImage.TYPE_INT_ARGB);
				tiles = new  Tile[width][height];
				for(int x = 0; x < width; x++)
					for(int y = 0; y < height; y++)
						tiles[x][y] = new Tile(x, y);
				// jsonfile uitlezen op layers en toevoegen als tilelayer in
				// tilelayer arraylist.
				// als de layer een visible : true heeft dan wordt hij
				// toegevoegd
				// met de parameter true zodat de tekenmethode weet dat de layer
				// moet worden getekend.
				// dit is voor de collision layers : die hebben false in de
				// JSON.

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
									case entranceExit:
										entrance.add(tiles[i % width][i / width]);
										break;
									case collidableTrue:
										collisionInfo[i % width][i / width] = true;
										break;
									
									case danceFloorTrue:
										danceFloor.add(tiles[i % width][i / width]);
										break;

									}

								}
							}

						}
						buildings.add(new Stage(null, entrance, entrance, maxAgents, stage, danceFloor));

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
							if (bool)
								nodeCount++;

					graph = new ArrayList<>(nodeCount);

					positionToNodeMap = new HashMap<>(nodeCount);

					for (int y = 0; y < collisionInfo.length; y++)
						for (int x = 0; x < collisionInfo[0].length; x++)
							if (collisionInfo[y][x] == true) {
								Node n = new Node(x, y);
								graph.add(n);
								positionToNodeMap.put(new Position(x, y), n);
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

				{ ////////////// Graph test using depth first search /////////
					Stack<Node> nodeStack = new Stack<>();
					HashSet<Node> visitedNodes = new HashSet<>(graph.size());

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

					if (visitedNodes.size() != graph.size()) {
						System.out.println("not all nodes are conected"); // TODO
																			// exception
						return;
					}
				} ////////// end of graph test //////////////////////////

				{ ////////// generating directions /////////////////////
					class TypeIdTilePair {
						int typeID;
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
							pair.entances.addAll(b.entrances);
							pair.exits.addAll(b.exits);
						}
					}

					for (TypeIdTilePair pair : pairs) {
						Queue<Node> queue = new LinkedList<Node>();
						HashSet<Node> visited = new HashSet<Node>();

						for (Tile tile : pair.entances) {
							queue.add(positionToNodeMap.get(new Position(tile.X, tile.Y)));
						}

						while (queue.isEmpty() == false) {
							Node node = queue.remove();
							for (int i = 0; i < node.straitEdges.length; i++) {
								if (node.straitEdges[i] == null)
									continue;
								if (visited.contains(node.straitEdges[i]) == true)
									continue;
								visited.add(node.straitEdges[i]);
								queue.add(node.straitEdges[i]);
								tiles[node.straitEdges[i].X][node.straitEdges[i].Y].addDirection(pair.typeID,
										tiles[node.X][node.Y]);
							}
						}
					}
				}
			}
		}

	}
	
	protected Tile getTileAt(int x, int y)
	{
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

	public void inclusiveUpdate(Graphics2D g2) {
		update();
		draw(g2);
		cleanUp();
	}

	// this method has been checked and it works correctly 14/3/2016
	public void update() {
		{// update everything related to time
			long realTime = System.currentTimeMillis();
			System.out.println(timeRemainder);
			if (lastRealTime == UNINITIALIZED)
				lastRealTime = realTime;
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

	public void draw(Graphics2D graphics) {
		graphics.drawImage(mapImage, new AffineTransform(), null);

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

	protected enum PathInfo {
		ENTRANCE, EXIT
	}

	protected int getPathID(String building, PathInfo info) {
		if (buildingMap.containsKey(building) == false)
			return -1;
		return buildingMap.get(building) << 1 | ((info == PathInfo.ENTRANCE) ? 0 : 1);
	}

	protected int getWorldTime() {
		return worldTime;
	}

	protected int getDeltaTime() {
		return deltaTime;
	}

}
