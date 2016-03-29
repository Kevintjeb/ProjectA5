package simulator;

import java.util.HashMap;

class Tile {
	private static int tileSize;
	final int X, Y;
	public HashMap<Integer, Tile> directions;
	
	Tile(int x, int y)
	{
		X = x;
		Y = y;
		
		directions = new HashMap<>();
	}
	
	void addDirection(Integer i, Tile t)
	{
		directions.put(i, t);
	}
	
	Tile getDirection(Integer key)
	{
		if (directions.containsKey(key) == false)
			return null;
		return directions.get(key);
	}
	
	public String toString()
	{
		return "("+X+", " + Y + ")";
	}
	
	static void setTileSize(int in_tileSize)
	{
		tileSize = in_tileSize;
	}
	
	static int getTileSize()
	{
		return tileSize;
	}
}
