package simulator;

import java.util.ArrayList;

public abstract class Building {
	final int typeID;
	String name;
	String description;
	final Tile[] entrances;
	final Tile[] exits;
	final int maxAgents;
	ArrayList<Agent> agents;
	
	static final int NO_PLACE = -1;
	static final int NO_MONEY = -2;
	static final int NO_ENTRANCE = -3;
	
	private static int currentTypeID = 0;
	static int generateNewTypeID()
	{
		return currentTypeID++;
	}
	
	Building(int  typeID, String name, String description, Tile[] entrances, Tile[] exits, int maxAgents)
	{
		this.typeID = typeID;
		this.name = name;
		this.description = description;
		this.entrances = entrances;
		this.exits = exits;
		this.maxAgents = maxAgents;
		this.agents = new ArrayList<>(maxAgents);
	}
	
	abstract int visit(Agent agent);
	abstract void leave(Agent agent);
	
	public String toString()
	{
		return name + "\n" + description;
	}
}
