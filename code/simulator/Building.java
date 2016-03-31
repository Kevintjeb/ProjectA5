package simulator;

import java.util.ArrayList;

public abstract class Building {
	final int typeID;
	String name;
	String description;
	ArrayList<Tile> entrances;
	ArrayList<Tile> exits;
	final int maxAgents;
	ArrayList<Agent> agents;

	static final int NO_PLACE = -1;
	static final int NO_MONEY = -2;
	static final int NO_ENTRANCE = -3;

	private static int currentTypeID = 0;

	static int generateNewTypeID() {
		return currentTypeID++;
	}

	Building(int typeID, String name, String description, ArrayList<Tile> entrances, ArrayList<Tile> exits,
			int maxAgents) {
		this.typeID = typeID;
		this.name = name;
		this.description = description;
		this.entrances = entrances;
		this.exits = exits;
		this.maxAgents = maxAgents;
		this.agents = new ArrayList<>(maxAgents);
	}

	int visit(Agent agent) {
		if (agent == null)
			return NO_ENTRANCE;
		if (agents.size() >= maxAgents)
			return NO_PLACE;
		if (agents.contains(agent) == true)
			return NO_ENTRANCE;
		agents.add(agent);
		return 0;
	}

	void leave(Agent agent) {
		agents.remove(agent);
	}

	public String toString() {
		return name + "\n" + description;
	}
}
